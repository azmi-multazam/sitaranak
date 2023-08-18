package com.zam.sidik_padang.home.newsinfo.bukaberita.komentar;


public interface KomentarContract {

    interface KometarView {
        void onAmbilStart();

        void onAmbilSuccess(KomentarResponse response);

        void onAmbilError(String message);

        void onKirimStart();

        void onKirimSuccess(KomentarResponse response);

        void onKirimError(String message);
    }

    interface Presenter {
        void ambilKomentar(String url);

        void cancelAmbil();

        void kirimKomentar(String url);

        void cancelKirim();
    }
}
