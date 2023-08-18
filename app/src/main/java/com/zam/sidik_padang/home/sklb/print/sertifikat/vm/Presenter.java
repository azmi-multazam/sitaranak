package com.zam.sidik_padang.home.sklb.print.sertifikat.vm;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.zam.sidik_padang.home.sklb.setting.skor.Parameter;
import com.zam.sidik_padang.home.sklb.setting.skor.Score;
import com.zam.sidik_padang.home.sklb.setting.skor.ScoreEntity;
import com.zam.sidik_padang.home.sklb.setting.skor.Ukuran;
import io.paperdb.Paper;

import static com.zam.sidik_padang.home.sklb.print.hitungscore.KalkulatorScore.GRADE_1;
import static com.zam.sidik_padang.home.sklb.print.hitungscore.KalkulatorScore.GRADE_2;
import static com.zam.sidik_padang.home.sklb.print.hitungscore.KalkulatorScore.GRADE_3;
import static com.zam.sidik_padang.home.sklb.setting.kuantitatif.KuantitatifListFragment.kuantitatif_betina;
import static com.zam.sidik_padang.home.sklb.setting.kuantitatif.KuantitatifListFragment.kuantitatif_jantan;

public class Presenter implements Contract.Presenter {

    private final Contract.Callback callback;
    private int isHari, isJantan;
    private List<ScoreEntity> jantanList, betinaList, entityList;
    //private int umur;
    private ScoreEntity entity;

    public Presenter(Context context, Contract.Callback callback) {
        this.callback = callback;
        jantanList = Paper.book().read(kuantitatif_jantan, new ArrayList<>());
        betinaList = Paper.book().read(kuantitatif_betina, new ArrayList<>());

        if (jantanList.size() == 0) {
            jantanList = Score.getKuantitatifJantan(context);
            Paper.book().write(kuantitatif_jantan, jantanList);
        }

        if (betinaList.size() == 0) {
            betinaList = Score.getKuantitatifBetina(context);
            Paper.book().write(kuantitatif_betina, betinaList);
        }
        entityList = new ArrayList<>();
    }

    @Override
    public void setJenisKelaminDanUmur(int jk, int ju, int value) {
        Log.d("umur:", value+", jk:"+jk+", ju:"+ju);

        isJantan = jk;
        isHari = ju;
        //if (value.trim().equals("")) value = "0";
        //umur = Integer.parseInt(value);

        if (isJantan == 1) {
            entityList = jantanList;
        } else if (isJantan == 2){
            entityList = betinaList;
        }
        Log.d("ent", entityList.size()+"");

        int bulanMax = 0;
        int hariMax = 0;
        for (int i = 0; i < entityList.size(); i++) {
            if (entityList.get(i).getBulanMax() > bulanMax) {
                bulanMax = entityList.get(i).getBulanMax();
            }

            if (entityList.get(i).getHariMax() > hariMax) {
                hariMax = entityList.get(i).getHariMax();
            }

            int min;
            int max;
            if (isHari == 1) {
                min = entityList.get(i).getHariMin();
                max = entityList.get(i).getHariMax();
            } else {
                min = entityList.get(i).getBulanMin();
                max = entityList.get(i).getBulanMax();
                if (min > 0 && max == 0) max = 2000;
            }

            if (value >= min && value <= max) {
                entity = entityList.get(i);
            }
        }

        boolean isBigger = false;
        String ms = "";
        if (isHari == 1) {
            if (value > hariMax) {
                isBigger = true;
                ms = "maksimal "+hariMax+" hari";
            }
        } else {
            if (value > bulanMax) {
                isBigger = true;
                ms = "maksimal "+bulanMax+" bulan";
            }
        }
        if (isBigger) {
            callback.onScoringFailed("umur", (isJantan == 1 ? "jantan " : "betina ") + ms);
        } else {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(entity);
            //Log.d("entity", json);
            callback.onEntityFound(json);
        }
    }

    @Override
    public void cariSkor(String tag, int value) {
        callback.onScoringStart(tag);
        Log.d("ukuran "+ tag, value+"");
        //int ukuran = Integer.parseInt(value);
        if (entity == null) callback.onScoringFailed(tag,"entity null");
        else {
            Parameter parameter = null;

            for (int i = 0; i < entity.getParameter().size(); i++) {
                if (entity.getParameter().get(i).getKode().toLowerCase().equals(tag)) {
                    parameter = entity.getParameter().get(i);
                }
            }

            if (parameter == null) {
                callback.onScoringFailed(tag,"parameter null");
            } else {
                Ukuran var = null;
                int minVal=1000, maxVal=0;
                for (int i = 0; i < parameter.getUkuran().size(); i++) {
                    int min = parameter.getUkuran().get(i).getMin();
                    int max = parameter.getUkuran().get(i).getMaks();
                    if (max > maxVal) maxVal = max;
                    if (minVal > min) minVal = min;

                    if ((value > min && value < max) || (value == min || value == max)) {
                        var = parameter.getUkuran().get(i);
                    }
                }
                if (var == null) callback.onScoringSuccess(tag,"\u2716  min "+minVal + ", maks "+maxVal, "0", "0");
                else callback.onScoringSuccess(tag, "\u2714  Kelas:"+var.getKelas() +", Skor:"+ var.getSkor(), var.getKelas(), var.getSkor());
            }
        }
    }

    @Override
    public void hitung(String scr1, String scr2, String scr3) {
        String scores = scr1+scr2+scr3;
        callback.onHitungStart(scores);

        boolean ada = false;
        String sgrade = "#";

        if (Arrays.asList(GRADE_1).contains(scores)) {
            ada = true;
            sgrade = "1";
        }

        if (!ada) {
            if (Arrays.asList(GRADE_2).contains(scores)) {
                ada = true;
                sgrade = "2";
            }
        }

        if (!ada) {
            if (Arrays.asList(GRADE_3).contains(scores)) {
                ada = true;
                sgrade = "3";
            }
        }

        if (ada) {
            int total = (Integer.parseInt(scr1) * Integer.parseInt(scr2)) * Integer.parseInt(scr3);
            callback.onHitungSuccess(String.valueOf(total), sgrade);
        } else {
            callback.onHitungFailed("Tidak diketahui");
            callback.onHitungSuccess("0", "3");
        }
    }
}
