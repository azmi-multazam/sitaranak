package com.zam.sidik_padang.home.dataternak.vm.list;

import androidx.annotation.NonNull;

import java.util.Map;

import com.zam.sidik_padang.api.ApiService;
import com.zam.sidik_padang.api.Injection;
import retrofit2.Call;
import retrofit2.Callback;

public class Presenter implements Contract.Presenter {

    private String TAG;
    private final Contract.Callback callback;
    private Call<DataTernakResponse> callDataTernak;
    private Call<HitungTernak> callHitungTernak;
    private ApiService apiService;

    public Presenter(Contract.Callback callback) {
        this.callback = callback;
        apiService = Injection.getInstance().getService();
    }

    @Override
    public void hitungTernak(Map<String, String> map) {
        callback.onHitungTernakStart();
        callHitungTernak = apiService.hitungTernak(map);
        callHitungTernak.enqueue(new Callback<HitungTernak>() {
            @Override
            public void onResponse(@NonNull Call<HitungTernak> call, @NonNull retrofit2.Response<HitungTernak> response) {
                HitungTernak hitungTernak = response.body();
                if (response.code() == 200) {
                    if (hitungTernak != null) {
                        if (hitungTernak.isSuccess()) {
                            callback.onHitungTernakSuccess(hitungTernak);
                        } else {
                            callback.onHitungTernakError(hitungTernak.getMessage());
                        }
                    }
                } else {
                    callback.onHitungTernakError("empty");
                }
            }

            @Override
            public void onFailure(@NonNull Call<HitungTernak> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    callback.onHitungTernakError("canceled");
                } else {
                    callback.onHitungTernakError("failure");
                }
            }
        });
    }

    @Override
    public void searchData(Map<String, String> map, boolean hasInten) {
        callback.onSearchStart();
        callDataTernak = hasInten ? apiService.getJumlahTernak(map) : apiService.getDataTernak(map);
        callDataTernak.enqueue(new Callback<DataTernakResponse>() {
            @Override
            public void onResponse(@NonNull Call<DataTernakResponse> call, @NonNull retrofit2.Response<DataTernakResponse> response) {
                DataTernakResponse dataTernakResponse = response.body();
                if (response.code() == 200) {
                    if (dataTernakResponse != null) {
                        if (dataTernakResponse.isSuccess()) {
                            callback.onSearchSuccess(dataTernakResponse);
                        } else {
                            callback.onSearchError(dataTernakResponse.getMessage());
                        }
                    }
                } else {
                    callback.onSearchError("empty");
                }
            }

            @Override
            public void onFailure(@NonNull Call<DataTernakResponse> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    callback.onSearchError("canceled");
                } else {
                    callback.onSearchError("failure");
                }
            }
        });
    }

    @Override
    public void hapusTernak(String url) {

    }

    @Override
    public void cancelRequest() {
        if (callHitungTernak != null) {
            callHitungTernak.cancel();
        }
        if (callDataTernak != null) {
            callDataTernak.cancel();
        }
    }
}
