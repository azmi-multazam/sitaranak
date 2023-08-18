package com.zam.sidik_padang.home.sklb.dataternak.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import com.zam.sidik_padang.api.ApiService;
import com.zam.sidik_padang.api.Injection;
import com.zam.sidik_padang.api.ResponseData;
import retrofit2.Call;
import retrofit2.Callback;

public class DataTernakViewModel extends AndroidViewModel {

    private final MutableLiveData<ResponseData<DataTernakSklbResponse>> responseData = new MutableLiveData<>();
    private Call<DataTernakSklbResponse> callDataTernak;

    private final ApiService apiService;

    public DataTernakViewModel(@NonNull @NotNull Application application) {
        super(application);
        apiService = Injection.getInstance().getService();
    }

    public LiveData<ResponseData<DataTernakSklbResponse>> getResponseData() {
        return responseData;
    }

    public void getDataTernak(String userid) {
        responseData.setValue(ResponseData.loading("Memuat data..."));
        callDataTernak = apiService.getDataTernakSklb(userid, "3");
        callDataTernak.enqueue(new Callback<DataTernakSklbResponse>() {
            @Override
            public void onResponse(@NonNull Call<DataTernakSklbResponse> call,
                                   @NonNull retrofit2.Response<DataTernakSklbResponse> response) {
                DataTernakSklbResponse dataResponse = response.body();
                if (response.code() == 200) {
                    if (dataResponse != null) {
                        responseData.setValue(ResponseData.success(dataResponse, dataResponse.getMessage(), dataResponse.isSuccess()));
                    }
                } else {
                    responseData.setValue(ResponseData.error("error"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<DataTernakSklbResponse> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    responseData.setValue(ResponseData.failure("cancelled"));
                } else {
                    responseData.setValue(ResponseData.failure("failure"));
                }
            }
        });
    }

    public void cancelCall() {
        if (callDataTernak != null) callDataTernak.cancel();
    }
}