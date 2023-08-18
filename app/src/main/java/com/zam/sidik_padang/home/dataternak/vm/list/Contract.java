package com.zam.sidik_padang.home.dataternak.vm.list;

import java.util.Map;

public interface Contract {

    interface Callback {
        void onHitungTernakStart();

        void onHitungTernakSuccess(HitungTernak response);

        void onHitungTernakError(String message);

        void onSearchStart();

        void onSearchSuccess(DataTernakResponse response);

        void onSearchError(String message);

        void onHapusStart();

        void onHapusSuccess(DataTernakResponse response);

        void onHapusError(String message);
    }

    interface Presenter {
        void hitungTernak(Map<String, String> map);

        void searchData(Map<String, String> map, boolean hasInten);

        void hapusTernak(String url);

        void cancelRequest();
    }
}
