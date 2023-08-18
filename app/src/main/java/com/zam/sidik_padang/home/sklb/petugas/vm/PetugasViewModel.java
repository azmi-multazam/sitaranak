package com.zam.sidik_padang.home.sklb.petugas.vm;

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
import com.zam.sidik_padang.home.sklb.petugas.vm.kab.KabResponse;
import com.zam.sidik_padang.home.sklb.petugas.vm.kec.KecResponse;
import com.zam.sidik_padang.home.sklb.petugas.vm.pemilik.PemilikResponse;
import retrofit2.Call;
import retrofit2.Callback;

public class PetugasViewModel extends AndroidViewModel {

    private final MutableLiveData<ResponseData<PetugasResponse>> responseData = new MutableLiveData<>();
    private final MutableLiveData<ResponseData<KabResponse>> responseKab = new MutableLiveData<>();
    private final MutableLiveData<ResponseData<KecResponse>> responseKec = new MutableLiveData<>();
    private final MutableLiveData<ResponseData<DesaResponse>> responseDesa = new MutableLiveData<>();
    private final MutableLiveData<ResponseData<BaseResponse>> responseAdd = new MutableLiveData<>();
    private final MutableLiveData<ResponseData<BaseResponse>> responseDelete = new MutableLiveData<>();

    private final MutableLiveData<ResponseData<PemilikResponse>> responsePemilikTernak = new MutableLiveData<>();
    private final MutableLiveData<ResponseData<BaseResponse>> responseAddPemilik = new MutableLiveData<>();
    private final MutableLiveData<ResponseData<BaseResponse>> responseDeletePemilik = new MutableLiveData<>();

    private Call<BaseResponse> callTambahPetugas;
    private Call<BaseResponse> callHapusPetugas;
    private Call<PetugasResponse> callPetugas;
    private Call<KabResponse> callKab;
    private Call<KecResponse> callKec;
    private Call<DesaResponse> callDesa;

    private Call<PemilikResponse> callPemilikTernak;
    private Call<BaseResponse> callTambahPemilik;
    private Call<BaseResponse> callHapusPemilik;

    private final ApiService apiService;

    public PetugasViewModel(@NonNull @NotNull Application application) {
        super(application);
        apiService = Injection.getInstance().getService();
    }

    public LiveData<ResponseData<PetugasResponse>> getResponseData() {
        return responseData;
    }

    public LiveData<ResponseData<KabResponse>> getResponseKab() {
        return responseKab;
    }

    public LiveData<ResponseData<KecResponse>> getResponseKec() {
        return responseKec;
    }

    public LiveData<ResponseData<DesaResponse>> getResponseDesa() {
        return responseDesa;
    }

    public LiveData<ResponseData<BaseResponse>> getResponseAdd() {
        return responseAdd;
    }

    public LiveData<ResponseData<BaseResponse>> getResponseDelete() {
        return responseDelete;
    }

    public LiveData<ResponseData<PemilikResponse>> getResponsePemilikTernak() {
        return responsePemilikTernak;
    }

    public LiveData<ResponseData<BaseResponse>> getResponseAddPemilik() {
        return responseAddPemilik;
    }

    public LiveData<ResponseData<BaseResponse>> getResponseDeletePemilik() {
        return responseDeletePemilik;
    }

    public void loadListKabupaten(Map<String, String> map) {
        responseKab.setValue(ResponseData.loading("Memuat data..."));
        callKab = apiService.getKab(map);
        callKab.enqueue(new Callback<KabResponse>() {
            @Override
            public void onResponse(@NonNull Call<KabResponse> call, @NonNull retrofit2.Response<KabResponse> response) {
                KabResponse kecResponse = response.body();
                if (response.code() == 200) {
                    if (kecResponse != null) {
                        responseKab.setValue(ResponseData.success(kecResponse, kecResponse.getMessage(), kecResponse.isSuccess()));
                    }
                } else {
                    responseKab.setValue(ResponseData.error("error"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<KabResponse> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    responseKab.setValue(ResponseData.failure("cancelled"));
                } else {
                    responseKab.setValue(ResponseData.failure("failure"));
                }
            }
        });
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

    public void requestPetugas(Map<String, String> map) {
        responseData.setValue(ResponseData.loading("Memuat data..."));
        callPetugas = apiService.getPetugas(map);
        callPetugas.enqueue(new Callback<PetugasResponse>() {
            @Override
            public void onResponse(@NonNull Call<PetugasResponse> call, @NonNull retrofit2.Response<PetugasResponse> response) {
                PetugasResponse petugasResponse = response.body();
                if (response.code() == 200) {
                    if (petugasResponse != null) {
                        responseData.setValue(ResponseData.success(petugasResponse, petugasResponse.getMessage(), petugasResponse.isSuccess()));
                    }
                } else {
                    responseData.setValue(ResponseData.error("error"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PetugasResponse> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    responseData.setValue(ResponseData.failure("cancelled"));
                } else {
                    responseData.setValue(ResponseData.failure("failure"));
                }
            }
        });
    }

    public void tambahPetugas(Map<String, String> param) {
        responseAdd.setValue(ResponseData.loading("Mengirim data..."));
        callTambahPetugas = apiService.addPetugas(param);
        callTambahPetugas.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse> call, @NonNull retrofit2.Response<BaseResponse> response) {
                BaseResponse baseResponse = response.body();
                if (response.code() == 200) {
                    if (baseResponse != null) {
                        responseAdd.setValue(ResponseData.success(baseResponse, baseResponse.getMessage(), baseResponse.isSuccess()));
                    }
                } else {
                    responseAdd.setValue(ResponseData.error("error"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    responseAdd.setValue(ResponseData.failure("cancelled"));
                } else {
                    responseAdd.setValue(ResponseData.failure("failure"));
                }
            }
        });
    }

    public void hapusPetugas(Map<String, String> param) {
        responseDelete.setValue(ResponseData.loading("Mengirim data..."));
        callHapusPetugas = apiService.deletePetugas(param);
        callHapusPetugas.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse> call, @NonNull retrofit2.Response<BaseResponse> response) {
                BaseResponse baseResponse = response.body();
                if (response.code() == 200) {
                    if (baseResponse != null) {
                        responseDelete.setValue(ResponseData.success(baseResponse, baseResponse.getMessage(), baseResponse.isSuccess()));
                    }
                } else {
                    responseDelete.setValue(ResponseData.error("error"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    responseDelete.setValue(ResponseData.failure("cancelled"));
                } else {
                    responseDelete.setValue(ResponseData.failure("failure"));
                }
            }
        });
    }

    public void getPemilikTernak(String userid) {
        responsePemilikTernak.setValue(ResponseData.loading("Memuat data..."));
        callPemilikTernak = apiService.getPemilikTernak(userid, "4");
        callPemilikTernak.enqueue(new Callback<PemilikResponse>() {
            @Override
            public void onResponse(@NonNull Call<PemilikResponse> call, @NonNull retrofit2.Response<PemilikResponse> response) {
                PemilikResponse pemilikResponse = response.body();
                if (response.code() == 200) {
                    if (pemilikResponse != null) {
                        responsePemilikTernak.setValue(ResponseData.success(pemilikResponse, pemilikResponse.getMessage(), pemilikResponse.isSuccess()));
                    }
                } else {
                    responsePemilikTernak.setValue(ResponseData.error("error"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PemilikResponse> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    responsePemilikTernak.setValue(ResponseData.failure("cancelled"));
                } else {
                    responsePemilikTernak.setValue(ResponseData.failure("failure"));
                }
            }
        });
    }

    public void tambahPemilik(Map<String, String> param) {
        responseAddPemilik.setValue(ResponseData.loading("Mengirim data..."));
        callTambahPemilik = apiService.addPetugas(param);
        callTambahPemilik.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse> call, @NonNull retrofit2.Response<BaseResponse> response) {
                BaseResponse baseResponse = response.body();
                if (response.code() == 200) {
                    if (baseResponse != null) {
                        responseAddPemilik.setValue(ResponseData.success(baseResponse, baseResponse.getMessage(), baseResponse.isSuccess()));
                    }
                } else {
                    responseAddPemilik.setValue(ResponseData.error("error"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    responseAddPemilik.setValue(ResponseData.failure("cancelled"));
                } else {
                    responseAddPemilik.setValue(ResponseData.failure("failure"));
                }
            }
        });
    }

    public void hapusPemilik(Map<String, String> param) {
        responseDeletePemilik.setValue(ResponseData.loading("Mengirim data..."));
        callHapusPemilik = apiService.deletePemilikTernak(param);
        callHapusPemilik.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse> call, @NonNull retrofit2.Response<BaseResponse> response) {
                BaseResponse baseResponse = response.body();
                if (response.code() == 200) {
                    if (baseResponse != null) {
                        responseDeletePemilik.setValue(ResponseData.success(baseResponse, baseResponse.getMessage(), baseResponse.isSuccess()));
                    }
                } else {
                    responseDeletePemilik.setValue(ResponseData.error("error"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    responseDeletePemilik.setValue(ResponseData.failure("cancelled"));
                } else {
                    responseDeletePemilik.setValue(ResponseData.failure("failure"));
                }
            }
        });
    }

    public void cancelCall() {
        if (callPetugas != null) callPetugas.cancel();
        if (callKab != null) callKab.cancel();
        if (callKec != null) callKec.cancel();
        if (callDesa != null) callDesa.cancel();
        if (callTambahPetugas != null) callTambahPetugas.cancel();
        if (callHapusPetugas != null) callHapusPetugas.cancel();
        if (callPemilikTernak != null) callPemilikTernak.cancel();
        if (callTambahPemilik != null) callTambahPemilik.cancel();
        if (callHapusPemilik != null) callHapusPemilik.cancel();
    }
}