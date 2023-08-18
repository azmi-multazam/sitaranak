package com.zam.sidik_padang.home.newsinfo.bukaberita.komentar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;


public class KomentarPresenter implements KomentarContract.Presenter {

    private static final String TAG_AMBIL = "ambil";
    private static final String TAG_KIRIM = "kirim";
    private final KomentarContract.KometarView mainView;

    public KomentarPresenter(KomentarContract.KometarView mainView) {
        this.mainView = mainView;
    }

    @Override
    public void ambilKomentar(String url) {
        mainView.onAmbilStart();
        AndroidNetworking.get(url)
                .setTag(TAG_AMBIL)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(KomentarResponse.class, new ParsedRequestListener<KomentarResponse>() {
                    @Override
                    public void onResponse(KomentarResponse response) {
                        if (response.isSuccess()) {
                            mainView.onAmbilSuccess(response);
                        } else {
                            mainView.onAmbilError(response.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        mainView.onAmbilError(anError.getErrorDetail());
                    }
                });
    }

    @Override
    public void cancelAmbil() {
        if (AndroidNetworking.isRequestRunning(TAG_AMBIL)) AndroidNetworking.cancel(TAG_AMBIL);
    }

    @Override
    public void kirimKomentar(String url) {
        mainView.onKirimStart();
        AndroidNetworking.get(url)
                .setTag(TAG_KIRIM)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(KomentarResponse.class, new ParsedRequestListener<KomentarResponse>() {
                    @Override
                    public void onResponse(KomentarResponse response) {
                        if (response.isSuccess()) {
                            mainView.onKirimSuccess(response);
                        } else {
                            mainView.onKirimError(response.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        mainView.onKirimError(anError.getErrorDetail());
                    }
                });
    }

    @Override
    public void cancelKirim() {
        if (AndroidNetworking.isRequestRunning(TAG_KIRIM)) AndroidNetworking.cancel(TAG_KIRIM);
    }
}
