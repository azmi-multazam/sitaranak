package com.zam.sidik_padang.home.memberhome.mvp;

import androidx.annotation.NonNull;

import java.util.Map;

import com.zam.sidik_padang.api.ApiService;
import com.zam.sidik_padang.api.Injection;
import retrofit2.Call;
import retrofit2.Callback;

public class NewsPresenter implements NewsContract.Presenter {

    private final NewsContract.NewsView mainView;
    private Call<BeritaResponse> callNews;
    private final ApiService apiService;

    public NewsPresenter(NewsContract.NewsView mainView) {
        this.mainView = mainView;
        apiService = Injection.getInstance().getService();
    }

    @Override
    public void requestData(Map<String, String> map) {
        mainView.onRequestStart();
        callNews = apiService.getNewsHome(map);
        callNews.enqueue(new Callback<BeritaResponse>() {
            @Override
            public void onResponse(@NonNull Call<BeritaResponse> call, @NonNull retrofit2.Response<BeritaResponse> response) {
                BeritaResponse kecResponse = response.body();
                if (response.code() == 200) {
                    if (kecResponse != null) {
                        mainView.onRequestSuccess(kecResponse);
                    } else {
                        mainView.onRequestError("error");
                    }
                } else {
                    mainView.onRequestError("error");
                }
            }

            @Override
            public void onFailure(@NonNull Call<BeritaResponse> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    mainView.onRequestError("cancelled");
                } else {
                    mainView.onRequestError("failure");
                }
            }
        });
    }

    @Override
    public void cancelRequest() {
        if (callNews != null) callNews.cancel();
    }
}
