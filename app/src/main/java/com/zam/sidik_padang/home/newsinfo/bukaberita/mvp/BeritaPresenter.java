package com.zam.sidik_padang.home.newsinfo.bukaberita.mvp;

import androidx.annotation.NonNull;

import java.util.Map;

import com.zam.sidik_padang.api.ApiService;
import com.zam.sidik_padang.api.Injection;
import com.zam.sidik_padang.api.ResponseData;
import com.zam.sidik_padang.home.newsinfo.bukaberita.komentar.KomentarResponse;
import com.zam.sidik_padang.home.sklb.petugas.vm.desa.DesaResponse;
import com.zam.sidik_padang.home.sklb.petugas.vm.kec.KecResponse;
import retrofit2.Call;
import retrofit2.Callback;


public class BeritaPresenter implements BeritaContract.Presenter {

    private static final String TAG_AMBIL_KOMENTAR = "ambilKomentar";
    private static final String TAG_LOAD_BERITA = "loadBerita";
    private final BeritaContract.BeritaView mainView;
    private Call<BukaBeritaResponse> callNews;
    private Call<KomentarResponse> callKomentar;
    private final ApiService apiService;

    public BeritaPresenter(BeritaContract.BeritaView mainView) {
        this.mainView = mainView;
        apiService = Injection.getInstance().getService();
    }

    @Override
    public void loadBerita(Map<String, String> map) {
        mainView.onLoadBeritaStart();
        callNews = apiService.getNews(map);
        callNews.enqueue(new Callback<BukaBeritaResponse>() {
            @Override
            public void onResponse(@NonNull Call<BukaBeritaResponse> call, @NonNull retrofit2.Response<BukaBeritaResponse> response) {
                BukaBeritaResponse kecResponse = response.body();
                if (response.code() == 200) {
                    if (kecResponse != null) {
                        mainView.onLoadBeritaSuccess(kecResponse);
                    } else {
                        mainView.onLoadBeritaError("error");
                    }
                } else {
                    mainView.onLoadBeritaError("error");
                }
            }

            @Override
            public void onFailure(@NonNull Call<BukaBeritaResponse> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    mainView.onLoadBeritaError("cancelled");
                } else {
                    mainView.onLoadBeritaError("failure");
                }
            }
        });
    }

    @Override
    public void ambilKomentar(Map<String, String> map) {
        mainView.onAmbilKomentarStart();
        callKomentar = apiService.getKomentar(map);
        callKomentar.enqueue(new Callback<KomentarResponse>() {
            @Override
            public void onResponse(@NonNull Call<KomentarResponse> call, @NonNull retrofit2.Response<KomentarResponse> response) {
                KomentarResponse kecResponse = response.body();
                if (response.code() == 200) {
                    if (kecResponse != null) {
                        mainView.onAmbilKomentarSuccess(kecResponse);
                    } else {
                        mainView.onAmbilKomentarError("error");
                    }
                } else {
                    mainView.onAmbilKomentarError("error");
                }
            }

            @Override
            public void onFailure(@NonNull Call<KomentarResponse> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    mainView.onAmbilKomentarError("cancelled");
                } else {
                    mainView.onAmbilKomentarError(t.getMessage());
                }
            }
        });
    }

    @Override
    public void cancelCall() {
        if (callNews != null) callNews.cancel();
        if (callKomentar != null) callKomentar.cancel();
    }

}
