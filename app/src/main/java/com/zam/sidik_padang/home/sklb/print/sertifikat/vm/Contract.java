package com.zam.sidik_padang.home.sklb.print.sertifikat.vm;

public interface Contract {

    interface Callback {
        void onEntityFound(String data);
        void onScoringStart(String tag);
        void onScoringSuccess(String tag, String msg, String kelas, String skor);
        void onScoringFailed(String tag, String msg);

        void onHitungStart(String msg);
        void onHitungSuccess(String skor, String grade);
        void onHitungFailed(String msg);
    }

    interface Presenter {
        void setJenisKelaminDanUmur(int jenisKelamin, int jenisUmur, int value);
        void cariSkor(String tag, int value);
        void hitung(String scr1, String scr2, String scr3);
    }
}