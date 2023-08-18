package com.zam.sidik_padang.home.memberhome.mvp;


import java.util.Map;

public interface NewsContract {

    interface NewsView {
        void onRequestStart();

        void onRequestSuccess(BeritaResponse response);

        void onRequestError(String message);
    }

    interface Presenter {
        void requestData(Map<String, String> map);

        void cancelRequest();
    }
}
