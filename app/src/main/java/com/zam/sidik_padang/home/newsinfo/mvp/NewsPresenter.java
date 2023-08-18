package com.zam.sidik_padang.home.newsinfo.mvp;

import androidx.annotation.NonNull;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.util.Map;

import com.zam.sidik_padang.api.ApiService;
import com.zam.sidik_padang.api.Injection;
import com.zam.sidik_padang.home.memberhome.mvp.BeritaResponse;
import com.zam.sidik_padang.home.newsinfo.bukaberita.komentar.KomentarResponse;
import com.zam.sidik_padang.home.newsinfo.bukaberita.mvp.BukaBeritaResponse;
import com.zam.sidik_padang.home.newsinfo.mvp.navmenu.NavMenuResponse;
import retrofit2.Call;
import retrofit2.Callback;

public class NewsPresenter implements NewsContract.Presenter {

    private final NewsContract.NewsView mainView;
    private Call<BeritaResponse> callNews;
    private Call<NavMenuResponse> callNav;
    private final ApiService apiService;

    public NewsPresenter(NewsContract.NewsView mainView) {
        this.mainView = mainView;
        apiService = Injection.getInstance().getService();
    }

    @Override
    public void requestNavigation(Map<String, String> map) {
        mainView.onRequestNavigationMenuStart();
        callNav = apiService.getNewsNav(map);
        callNav.enqueue(new Callback<NavMenuResponse>() {
            @Override
            public void onResponse(@NonNull Call<NavMenuResponse> call, @NonNull retrofit2.Response<NavMenuResponse> response) {
                NavMenuResponse kecResponse = response.body();
                if (response.code() == 200) {
                    if (kecResponse != null) {
                        mainView.onRequestNavigationSuccess(kecResponse);
                    } else {
                        mainView.onRequestNavigationError("error");
                    }
                } else {
                    mainView.onRequestNavigationError("error");
                }
            }

            @Override
            public void onFailure(@NonNull Call<NavMenuResponse> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    mainView.onRequestNavigationError("cancelled");
                } else {
                    mainView.onRequestNavigationError("failure");
                }
            }
        });

    }

    @Override
    public void requestBerita(Map<String, String> map) {
        mainView.onRequestBeritaStart();
        callNews = apiService.getNewsHome(map);
        callNews.enqueue(new Callback<BeritaResponse>() {
            @Override
            public void onResponse(@NonNull Call<BeritaResponse> call, @NonNull retrofit2.Response<BeritaResponse> response) {
                BeritaResponse kecResponse = response.body();
                if (response.code() == 200) {
                    if (kecResponse != null) {
                        mainView.onRequestBeritaSuccess(kecResponse);
                    } else {
                        mainView.onRequestBeritaError("error");
                    }
                } else {
                    mainView.onRequestBeritaError("error");
                }
            }

            @Override
            public void onFailure(@NonNull Call<BeritaResponse> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    mainView.onRequestBeritaError("cancelled");
                } else {
                    mainView.onRequestBeritaError("failure");
                }
            }
        });
    }

    @Override
    public void cancelRequest() {
        if (callNav !=null) callNav.cancel();
        if (callNews!=null) callNews.cancel();
    }
}
