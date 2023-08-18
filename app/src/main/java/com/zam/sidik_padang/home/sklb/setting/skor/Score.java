package com.zam.sidik_padang.home.sklb.setting.skor;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

public class Score {

    public static String SCORE_JANTAN = "kuant_jantan.json";
    public static String SCORE_BETINA = "kuant_betina.json";

    public static String[] kelamin = {"-- Pilih Jenis Kelamin --", "JANTAN", "BETINA"};
    public static String[] parameter_jantan = {"Tinggi Pundak", "Panjang Badan", "Lingkar Dada", "Lingkar Skrotum"};
    public static String[] parameter_betina = {"Tinggi Pundak", "Panjang Badan", "Lingkar Dada"};
    public static String[] kode = {"TP", "PB", "LD", "LS"};
    public static String[] kelas = {"1", "2", "3"};
    public static String[] scores = {"3", "2", "1"};

    public static List<ScoreEntity> getKuantitatifJantan(Context context) {
        String jsonFileString = getJsonFromAssets(context, SCORE_JANTAN);

        Gson gson = new Gson();
        Type listUserType = new TypeToken<List<ScoreEntity>>() {
        }.getType();
        return gson.fromJson(jsonFileString, listUserType);
    }

    public static List<ScoreEntity> getKuantitatifBetina(Context context) {
        String jsonFileString = getJsonFromAssets(context, SCORE_BETINA);

        Gson gson = new Gson();
        Type listUserType = new TypeToken<List<ScoreEntity>>() {
        }.getType();
        return gson.fromJson(jsonFileString, listUserType);
    }

    static String getJsonFromAssets(Context context, String fileName) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open(fileName);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return jsonString;
    }
}
