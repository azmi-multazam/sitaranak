package com.zam.sidik_padang.home.dataternak.dibawahnya.vm;

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
import com.zam.sidik_padang.home.dataternak.dibawahnya.vm.bangsa.BangsaResponse;
import com.zam.sidik_padang.home.dataternak.dibawahnya.vm.gabungan.GabunganResponse;
import com.zam.sidik_padang.home.dataternak.dibawahnya.vm.induk.IndukResponse;
import com.zam.sidik_padang.home.dataternak.vm.DataTernakRepo;
import com.zam.sidik_padang.home.dataternak.vm.detail.DetailTernakResponse;
import com.zam.sidik_padang.home.dataternak.vm.foto.FotoTernakResponse;
import com.zam.sidik_padang.home.dataternak.vm.kondisi.KondisiResponse;
import com.zam.sidik_padang.home.dataternak.vm.list.DataTernak;
import com.zam.sidik_padang.home.dataternak.vm.list.DataTernakResponse;
import com.zam.sidik_padang.home.dataternak.vm.list.HitungTernak;
import retrofit2.Call;
import retrofit2.Callback;

public class TambahViewModel extends AndroidViewModel {

    private final MutableLiveData<ResponseData<GabunganResponse>> responseGabungan = new MutableLiveData<>();
    private final MutableLiveData<ResponseData<BangsaResponse>> responseBangsa = new MutableLiveData<>();
    private final MutableLiveData<ResponseData<IndukResponse>> responseInduk = new MutableLiveData<>();
    private final MutableLiveData<ResponseData<IndukResponse>> responseBapak = new MutableLiveData<>();
    private final MutableLiveData<ResponseData<BaseResponse>> responseSend = new MutableLiveData<>();

    private Call<GabunganResponse> callGabungan;
    private Call<BangsaResponse> callBangsa;
    private Call<IndukResponse> callInduk;
    private Call<IndukResponse> callBapak;
    private Call<BaseResponse> callSend;

    private final ApiService apiService;

    public TambahViewModel(@NonNull @NotNull Application application) {
        super(application);
        apiService = Injection.getInstance().getService();
    }

    public void getGabungan(String url) {
        responseGabungan.setValue(ResponseData.loading("Memuat data..."));
        callGabungan = apiService.getGabungan(url);
        callGabungan.enqueue(new Callback<GabunganResponse>() {
            @Override
            public void onResponse(@NonNull Call<GabunganResponse> call, @NonNull retrofit2.Response<GabunganResponse> response) {
                GabunganResponse dataResponse = response.body();
                if (response.code() == 200) {
                    if (dataResponse != null) {
                        responseGabungan.setValue(ResponseData.success(dataResponse, dataResponse.getMessage(), dataResponse.isSuccess()));
                    }
                } else {
                    responseGabungan.setValue(ResponseData.error("error"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<GabunganResponse> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    responseGabungan.setValue(ResponseData.failure("cancelled"));
                } else {
                    responseGabungan.setValue(ResponseData.failure(t.getMessage()));
                }
            }
        });
    }

    public void getBangsa(String url) {
        responseBangsa.setValue(ResponseData.loading("Memuat data..."));
        callBangsa = apiService.getBangsa(url);
        callBangsa.enqueue(new Callback<BangsaResponse>() {
            @Override
            public void onResponse(@NonNull Call<BangsaResponse> call, @NonNull retrofit2.Response<BangsaResponse> response) {
                BangsaResponse dataResponse = response.body();
                if (response.code() == 200) {
                    if (dataResponse != null) {
                        responseBangsa.setValue(ResponseData.success(dataResponse, dataResponse.getMessage(), dataResponse.isSuccess()));
                    }
                } else {
                    responseBangsa.setValue(ResponseData.error("error"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<BangsaResponse> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    responseBangsa.setValue(ResponseData.failure("cancelled"));
                } else {
                    responseBangsa.setValue(ResponseData.failure(t.getMessage()));
                }
            }
        });
    }

    public void getInduk(String url) {
        responseInduk.setValue(ResponseData.loading("Memuat data..."));
        callInduk = apiService.getInduk(url);
        callInduk.enqueue(new Callback<IndukResponse>() {
            @Override
            public void onResponse(@NonNull Call<IndukResponse> call, @NonNull retrofit2.Response<IndukResponse> response) {
                IndukResponse dataResponse = response.body();
                if (response.code() == 200) {
                    if (dataResponse != null) {
                        responseInduk.setValue(ResponseData.success(dataResponse, dataResponse.getMessage(), dataResponse.isSuccess()));
                    }
                } else {
                    responseInduk.setValue(ResponseData.error("error"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<IndukResponse> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    responseInduk.setValue(ResponseData.failure("cancelled"));
                } else {
                    responseInduk.setValue(ResponseData.failure(t.getMessage()));
                }
            }
        });
    }

    public void getBapak(String url) {
        responseBapak.setValue(ResponseData.loading("Memuat data..."));
        callBapak = apiService.getBapak(url);
        callBapak.enqueue(new Callback<IndukResponse>() {
            @Override
            public void onResponse(@NonNull Call<IndukResponse> call, @NonNull retrofit2.Response<IndukResponse> response) {
                IndukResponse dataResponse = response.body();
                if (response.code() == 200) {
                    if (dataResponse != null) {
                        responseBapak.setValue(ResponseData.success(dataResponse, dataResponse.getMessage(), dataResponse.isSuccess()));
                    }
                } else {
                    responseBapak.setValue(ResponseData.error("error"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<IndukResponse> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    responseBapak.setValue(ResponseData.failure("cancelled"));
                } else {
                    responseBapak.setValue(ResponseData.failure(t.getMessage()));
                }
            }
        });
    }

    public void sendDataTernak(String url) {
        responseSend.setValue(ResponseData.loading("Mengirim data ternak..."));
        callSend = apiService.sendTernak(url);
        callSend.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse> call, @NonNull retrofit2.Response<BaseResponse> response) {
                BaseResponse dataResponse = response.body();
                if (response.code() == 200) {
                    if (dataResponse != null) {
                        responseSend.setValue(ResponseData.success(dataResponse, dataResponse.getMessage(), dataResponse.isSuccess()));
                    } else {
                        responseSend.setValue(ResponseData.error("null response"));
                    }
                } else {
                    responseSend.setValue(ResponseData.error("error"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    responseSend.setValue(ResponseData.failure("cancelled"));
                } else {
                    responseSend.setValue(ResponseData.failure(t.getMessage()));
                }
            }
        });
    }

    public void cancelCall() {
        if (callGabungan != null) callGabungan.cancel();
        if (callBangsa != null) callBangsa.cancel();
        if (callInduk != null) callInduk.cancel();
        if (callBapak != null) callBapak.cancel();
        if (callSend != null) callSend.cancel();
    }

    public MutableLiveData<ResponseData<GabunganResponse>> getResponseGabungan() {
        return responseGabungan;
    }

    public MutableLiveData<ResponseData<BangsaResponse>> getResponseBangsa() {
        return responseBangsa;
    }

    public MutableLiveData<ResponseData<IndukResponse>> getResponseInduk() {
        return responseInduk;
    }

    public MutableLiveData<ResponseData<IndukResponse>> getResponseBapak() {
        return responseBapak;
    }

    public MutableLiveData<ResponseData<BaseResponse>> getResponseSend() {
        return responseSend;
    }

}