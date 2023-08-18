package com.zam.sidik_padang.home.dataternak.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import com.zam.sidik_padang.api.ApiService;
import com.zam.sidik_padang.api.BaseResponse;
import com.zam.sidik_padang.api.Injection;
import com.zam.sidik_padang.api.ResponseData;
import com.zam.sidik_padang.home.dataternak.vm.detail.DetailTernakResponse;
import com.zam.sidik_padang.home.dataternak.vm.foto.FotoTernakResponse;
import com.zam.sidik_padang.home.dataternak.vm.kondisi.KondisiResponse;
import com.zam.sidik_padang.home.dataternak.vm.list.DataTernak;
import com.zam.sidik_padang.home.dataternak.vm.list.DataTernakResponse;
import com.zam.sidik_padang.home.dataternak.vm.list.HitungTernak;
import retrofit2.Call;
import retrofit2.Callback;

public class TernakViewModel extends AndroidViewModel {

    private final MutableLiveData<ResponseData<DataTernakResponse>> responseDataTernak;
    private final MutableLiveData<ResponseData<HitungTernak>> responseHitungTernak;
    private final MutableLiveData<List<DataTernak>> listDataTernak;
    private final MutableLiveData<ResponseData<BaseResponse>> responseHapusTernak = new MutableLiveData<>();

    private final MutableLiveData<ResponseData<DetailTernakResponse>> responseDetailTernak = new MutableLiveData<>();
    private final MutableLiveData<ResponseData<FotoTernakResponse>> responseFotoTernak = new MutableLiveData<>();
    private final MutableLiveData<ResponseData<KondisiResponse>> responseKondisiTernak = new MutableLiveData<>();

    private Call<BaseResponse> callHapusTernak;

    private Call<DetailTernakResponse> callDetailTernak;
    private Call<FotoTernakResponse> callFotoTernak;
    private Call<KondisiResponse> callKondisiTernak;

    private final ApiService apiService;
    private DataTernakRepo dataTernakRepo;

    public TernakViewModel(@NonNull @NotNull Application application) {
        super(application);
        apiService = Injection.getInstance().getService();

        dataTernakRepo = DataTernakRepo.getInstance();
        responseDataTernak = dataTernakRepo.responseDataTernak;
        responseHitungTernak = dataTernakRepo.responseHitungTernak;
        listDataTernak = dataTernakRepo.listDataTernak;
    }

    public MutableLiveData<ResponseData<DataTernakResponse>> getResponseDataTernak() {
        return responseDataTernak;
    }

    public MutableLiveData<ResponseData<HitungTernak>> getResponseHitungTernak() {
        return responseHitungTernak;
    }

    public MutableLiveData<ResponseData<BaseResponse>> getResponseHapusTernak() {
        return responseHapusTernak;
    }

    public MutableLiveData<List<DataTernak>> getListDataTernak() {
        return listDataTernak;
    }

    public LiveData<ResponseData<DetailTernakResponse>> getResponseDetailTernak() {
        return responseDetailTernak;
    }

    public MutableLiveData<ResponseData<FotoTernakResponse>> getResponseFotoTernak() {
        return responseFotoTernak;
    }

    public MutableLiveData<ResponseData<KondisiResponse>> getResponseKondisiTernak() {
        return responseKondisiTernak;
    }

    public void hitungTernak(Map<String, String> map, boolean fromDrawer) {
        dataTernakRepo.hitungTernak(map, fromDrawer);
    }

    public void searchData(Map<String, String> map, boolean hasInten) {
        dataTernakRepo.searchData(map, hasInten);
    }

    public void getListPage(int page) {
        dataTernakRepo.getListPage(page);
    }

    public void getDetailTernak(String url) {
        responseDetailTernak.setValue(ResponseData.loading("Memuat data..."));
        callDetailTernak = apiService.getDetailTernak(url);
        callDetailTernak.enqueue(new Callback<DetailTernakResponse>() {
            @Override
            public void onResponse(@NonNull Call<DetailTernakResponse> call, @NonNull retrofit2.Response<DetailTernakResponse> response) {
                DetailTernakResponse dataResponse = response.body();
                if (response.code() == 200) {
                    if (dataResponse != null) {
                        responseDetailTernak.setValue(ResponseData.success(dataResponse, dataResponse.getMessage(), dataResponse.isSuccess()));
                    }
                } else {
                    responseDetailTernak.setValue(ResponseData.error("error"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<DetailTernakResponse> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    responseDetailTernak.setValue(ResponseData.failure("cancelled"));
                } else {
                    responseDetailTernak.setValue(ResponseData.failure(t.getMessage()));
                }
            }
        });
    }

    public void getFotoTernak(String url) {
        responseFotoTernak.setValue(ResponseData.loading("Memuat data..."));
        callFotoTernak = apiService.getFotoTernak(url);
        callFotoTernak.enqueue(new Callback<FotoTernakResponse>() {
            @Override
            public void onResponse(@NonNull Call<FotoTernakResponse> call, @NonNull retrofit2.Response<FotoTernakResponse> response) {
                FotoTernakResponse dataResponse = response.body();
                if (response.code() == 200) {
                    if (dataResponse != null) {
                        responseFotoTernak.setValue(ResponseData.success(dataResponse, dataResponse.getMessage(), dataResponse.isSuccess()));
                    }
                } else {
                    responseFotoTernak.setValue(ResponseData.error("error"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<FotoTernakResponse> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    responseFotoTernak.setValue(ResponseData.failure("cancelled"));
                } else {
                    responseFotoTernak.setValue(ResponseData.failure(t.getMessage()));
                }
            }
        });
    }

    public void getKondisiTernak(String url) {
        responseKondisiTernak.setValue(ResponseData.loading("Memuat data..."));
        callKondisiTernak = apiService.getKondisiTernak(url);
        callKondisiTernak.enqueue(new Callback<KondisiResponse>() {
            @Override
            public void onResponse(@NonNull Call<KondisiResponse> call, @NonNull retrofit2.Response<KondisiResponse> response) {
                KondisiResponse dataResponse = response.body();
                if (response.code() == 200) {
                    if (dataResponse != null) {
                        responseKondisiTernak.setValue(ResponseData.success(dataResponse, dataResponse.getMessage(), dataResponse.isSuccess()));
                    }
                } else {
                    responseKondisiTernak.setValue(ResponseData.error("error"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<KondisiResponse> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    responseKondisiTernak.setValue(ResponseData.failure("cancelled"));
                } else {
                    responseKondisiTernak.setValue(ResponseData.failure(t.getMessage()));
                }
            }
        });
    }

    public void hapusTernak(Map<String, String> map) {
        responseHapusTernak.setValue(ResponseData.loading("Memuat data..."));
        callHapusTernak = apiService.hapusTernak(map);
        callHapusTernak.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse> call, @NonNull retrofit2.Response<BaseResponse> response) {
                BaseResponse dataResponse = response.body();
                if (response.code() == 200) {
                    if (dataResponse != null) {
                        responseHapusTernak.setValue(ResponseData.success(dataResponse, dataResponse.getMessage(), dataResponse.isSuccess()));
                    }
                } else {
                    responseHapusTernak.setValue(ResponseData.error("error"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    responseHapusTernak.setValue(ResponseData.failure("cancelled"));
                } else {
                    responseHapusTernak.setValue(ResponseData.failure(t.getMessage()));
                }
            }
        });
    }

    public void cancelCall() {
        //if (callDataTernak != null) callDataTernak.cancel();
        //if (callHitungTernak != null) callHitungTernak.cancel();
        if (callDetailTernak != null) callDetailTernak.cancel();
        if (callFotoTernak != null) callFotoTernak.cancel();
        if (callKondisiTernak != null) callKondisiTernak.cancel();
        if (callHapusTernak != null) callHapusTernak.cancel();
    }
}