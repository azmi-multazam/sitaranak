package com.zam.sidik_padang.profilku.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

import com.zam.sidik_padang.api.ApiService;
import com.zam.sidik_padang.api.BaseResponse;
import com.zam.sidik_padang.api.Injection;
import com.zam.sidik_padang.api.ResponseData;
import com.zam.sidik_padang.home.sklb.petugas.vm.desa.DesaResponse;
import com.zam.sidik_padang.home.sklb.petugas.vm.kec.KecResponse;
import retrofit2.Call;
import retrofit2.Callback;

public class ProfileViewModel extends AndroidViewModel {

    private final MutableLiveData<ResponseData<ProfileResponse>> responseProfile = new MutableLiveData<>();
    private final MutableLiveData<ResponseData<BaseResponse>> responseEdit = new MutableLiveData<>();
    private final MutableLiveData<ResponseData<KecResponse>> responseKec = new MutableLiveData<>();
    private final MutableLiveData<ResponseData<DesaResponse>> responseDesa = new MutableLiveData<>();

    private Call<ProfileResponse> callProfile;
    private Call<BaseResponse> callEdit;
    private Call<KecResponse> callKec;
    private Call<DesaResponse> callDesa;

    private final ApiService apiService;

    public ProfileViewModel(@NonNull @NotNull Application application) {
        super(application);
        apiService = Injection.getInstance().getService();
    }

    public LiveData<ResponseData<ProfileResponse>> getResponseProfile() {
        return responseProfile;
    }

    public LiveData<ResponseData<BaseResponse>> getResponseEdit() {
        return responseEdit;
    }

    public LiveData<ResponseData<KecResponse>> getResponseKec() {
        return responseKec;
    }

    public LiveData<ResponseData<DesaResponse>> getResponseDesa() {
        return responseDesa;
    }

    public void loadListKecamatan(Map<String, String> map) {
        responseKec.setValue(ResponseData.loading("Memuat data..."));
        callKec = apiService.getKec(map);
        callKec.enqueue(new Callback<KecResponse>() {
            @Override
            public void onResponse(@NonNull Call<KecResponse> call, @NonNull retrofit2.Response<KecResponse> response) {
                KecResponse kecResponse = response.body();
                if (response.code() == 200) {
                    if (kecResponse != null) {
                        responseKec.setValue(ResponseData.success(kecResponse, kecResponse.getMessage(), kecResponse.isSuccess()));
                    }
                } else {
                    responseKec.setValue(ResponseData.error("error"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<KecResponse> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    responseKec.setValue(ResponseData.failure("cancelled"));
                } else {
                    responseKec.setValue(ResponseData.failure("failure"));
                }
            }
        });
    }

    public void loadListDesa(Map<String, String> map) {
        responseDesa.setValue(ResponseData.loading("Memuat data..."));
        callDesa = apiService.getPDesa(map);
        callDesa.enqueue(new Callback<DesaResponse>() {
            @Override
            public void onResponse(@NonNull Call<DesaResponse> call, @NonNull retrofit2.Response<DesaResponse> response) {
                DesaResponse desaResponse = response.body();
                if (response.code() == 200) {
                    if (desaResponse != null) {
                        responseDesa.setValue(ResponseData.success(desaResponse, desaResponse.getMessage(), desaResponse.isSuccess()));
                    }
                } else {
                    responseDesa.setValue(ResponseData.error("error"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<DesaResponse> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    responseDesa.setValue(ResponseData.failure("cancelled"));
                } else {
                    responseDesa.setValue(ResponseData.failure("failure"));
                }
            }
        });
    }

    public void getProfile(Map<String, String> map) {
        responseProfile.setValue(ResponseData.loading("Memuat data..."));
        callProfile = apiService.getProfile(map);
        callProfile.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProfileResponse> call, @NonNull retrofit2.Response<ProfileResponse> response) {
                ProfileResponse profileResponse = response.body();
                if (response.code() == 200) {
                    if (profileResponse != null) {
                        responseProfile.setValue(ResponseData.success(profileResponse, profileResponse.getMessage(), profileResponse.isSuccess()));
                    }
                } else {
                    responseProfile.setValue(ResponseData.error("error"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProfileResponse> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    responseProfile.setValue(ResponseData.failure("cancelled"));
                } else {
                    responseProfile.setValue(ResponseData.failure("failure"));
                }
            }
        });
    }

    public void editProfile(Map<String, String> param) {
        responseEdit.setValue(ResponseData.loading("Mengirim data..."));
        callEdit = apiService.editProfile(param);
        callEdit.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse> call, @NonNull retrofit2.Response<BaseResponse> response) {
                BaseResponse baseResponse = response.body();
                if (response.code() == 200) {
                    if (baseResponse != null) {
                        responseEdit.setValue(ResponseData.success(baseResponse, baseResponse.getMessage(), baseResponse.isSuccess()));
                    }
                } else {
                    responseEdit.setValue(ResponseData.error("error"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    responseEdit.setValue(ResponseData.failure("cancelled"));
                } else {
                    responseEdit.setValue(ResponseData.failure("failure"));
                }
            }
        });
    }

    public void cancelCall() {
        if (callProfile != null) callProfile.cancel();
        if (callEdit != null) callEdit.cancel();
        if (callKec != null) callKec.cancel();
        if (callDesa != null) callDesa.cancel();
    }
}