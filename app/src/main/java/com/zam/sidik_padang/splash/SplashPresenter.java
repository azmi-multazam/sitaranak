package com.zam.sidik_padang.splash;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import com.zam.sidik_padang.util.Config;

public class SplashPresenter implements SplashContract.Presenter {

    private Context context;
    private SplashContract.SplashView view;

    public SplashPresenter(Context context, SplashContract.SplashView view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void requestData() {
        if (view == null) return;
        view.onRequestStart();
        String TAG = Config.URL_LOGO;
        AndroidNetworking.post(TAG)
                .setPriority(Priority.MEDIUM)
                .setTag(TAG)
                .build()
                .getAsObject(SplashResponse.class, new ParsedRequestListener<SplashResponse>() {
                    @Override
                    public void onResponse(SplashResponse response) {
                        if (view != null) {
                            if (response.isSuccess()) view.onRequestSuccess(response);
                            else view.onRequestError(response.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (view != null) view.onRequestError(anError.getErrorDetail());
                    }
                });
    }

    @Override
    public void cancelRequest() {

    }
}