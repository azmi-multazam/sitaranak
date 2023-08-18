package com.zam.sidik_padang.home.dataternak.vm;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zam.sidik_padang.api.ApiService;
import com.zam.sidik_padang.api.Injection;
import com.zam.sidik_padang.api.ResponseData;
import com.zam.sidik_padang.home.dataternak.vm.list.DataTernak;
import com.zam.sidik_padang.home.dataternak.vm.list.DataTernakResponse;
import com.zam.sidik_padang.home.dataternak.vm.list.HitungTernak;
import retrofit2.Call;
import retrofit2.Callback;

public class DataTernakRepo {

    private static final int PAGES_LIMIT = 15;

    MutableLiveData<ResponseData<DataTernakResponse>> responseDataTernak = new MutableLiveData<>();
    MutableLiveData<ResponseData<HitungTernak>> responseHitungTernak = new MutableLiveData<>();
    MutableLiveData<List<DataTernak>> listDataTernak = new MutableLiveData<>();
    private Call<DataTernakResponse> callDataTernak;
    private Call<HitungTernak> callHitungTernak;
    private final ApiService apiService;

    private static DataTernakRepo INSTANCE;
    private List<DataTernak> dataTernakList;

    private DataTernakRepo() {
        apiService = Injection.getInstance().getService();
        dataTernakList = new ArrayList<>();
    }

    public synchronized static DataTernakRepo getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataTernakRepo();
        }
        return INSTANCE;
    }

    public void hitungTernak(Map<String, String> map, boolean fromDrawer) {
        responseHitungTernak.setValue(ResponseData.loading("loading..."));
        callHitungTernak = apiService.hitungTernak(map);
        callHitungTernak.enqueue(new Callback<HitungTernak>() {
            @Override
            public void onResponse(@NonNull Call<HitungTernak> call, @NonNull retrofit2.Response<HitungTernak> response) {
                HitungTernak hitungTernak = response.body();
                if (response.code() == 200) {
                    if (hitungTernak != null) {
                        if (hitungTernak.isSuccess()) {
                            if (fromDrawer) {
                                //clearList();
                                dataTernakList = hitungTernak.getDataTernak();
                            }
                            responseHitungTernak.setValue(ResponseData.success(hitungTernak, hitungTernak.getMessage(), hitungTernak.isSuccess()));
                        } else {
                            responseHitungTernak.setValue(ResponseData.error(hitungTernak.getMessage()));
                        }
                    }
                } else {
                    responseHitungTernak.setValue(ResponseData.error("empty"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<HitungTernak> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    responseHitungTernak.setValue(ResponseData.failure("canceled"));
                } else {
                    responseHitungTernak.setValue(ResponseData.failure(t.getMessage()));
                }
            }
        });
    }

    public void searchData(Map<String, String> map, boolean hasInten) {
        responseDataTernak.setValue(ResponseData.loading("loading..."));
        callDataTernak = hasInten ? apiService.getJumlahTernak(map) : apiService.getDataTernak(map);
        callDataTernak.enqueue(new Callback<DataTernakResponse>() {
            @Override
            public void onResponse(@NonNull Call<DataTernakResponse> call, @NonNull retrofit2.Response<DataTernakResponse> response) {
                DataTernakResponse dataTernakResponse = response.body();
                if (response.code() == 200) {
                    if (dataTernakResponse != null) {
                        if (dataTernakResponse.isSuccess()) {
                            //clearList();
                            dataTernakList = dataTernakResponse.getDataTernak();
                            responseDataTernak.setValue(ResponseData.success(dataTernakResponse, dataTernakResponse.getMessage(), dataTernakResponse.isSuccess()));
                        } else {
                            responseDataTernak.setValue(ResponseData.error(dataTernakResponse.getMessage()));
                        }
                    }
                } else {
                    responseDataTernak.setValue(ResponseData.error("empty"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<DataTernakResponse> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    responseDataTernak.setValue(ResponseData.failure("canceled"));
                } else {
                    responseDataTernak.setValue(ResponseData.failure(t.getMessage()));
                }
            }
        });
    }

    public void clearList() {
        listDataTernak.setValue(null);
    }

    void getListPage(int currentPage) {
        listDataTernak.setValue(null);
        int page = (PAGES_LIMIT * (currentPage - 1));
        int size = dataTernakList.size();
        int limit = PAGES_LIMIT + page;
        if (limit > size) {
            limit = size;
        }

        List<DataTernak> newList = new ArrayList<>();
        for (int i = page; i < limit; i++) {
            newList.add(dataTernakList.get(i));
        }
        listDataTernak.setValue(newList);
    }
}