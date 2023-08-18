package com.zam.sidik_padang.home.newsinfo.bukaberita.mvp;


import java.util.Map;

import com.zam.sidik_padang.home.newsinfo.bukaberita.komentar.KomentarResponse;

public interface BeritaContract {

    interface BeritaView {
        void onAmbilKomentarStart();

        void onAmbilKomentarSuccess(KomentarResponse response);

        void onAmbilKomentarError(String message);

        void onLoadBeritaStart();

        void onLoadBeritaSuccess(BukaBeritaResponse response);

        void onLoadBeritaError(String message);
    }

    interface Presenter {
        void loadBerita(Map<String, String> map);

        void cancelCall();

        void ambilKomentar(Map<String, String> map);
    }
}
