package com.zam.sidik_padang.home.sklb.setting.kuantitatif;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.databinding.ActivityEditKuantitatifBinding;
import com.zam.sidik_padang.home.sklb.setting.skor.Parameter;
import com.zam.sidik_padang.home.sklb.setting.skor.Score;
import com.zam.sidik_padang.home.sklb.setting.skor.ScoreEntity;
import com.zam.sidik_padang.home.sklb.setting.skor.Ukuran;
import com.zam.sidik_padang.util.bordered.view.BorderedLayout;

public class EditKuantitatifActivity extends BaseLogedinActivity {

    private ActivityEditKuantitatifBinding binding;
    private ScoreEntity entity;
    private Parameter newParam;
    private boolean isJantan = false;
    private boolean userIsInteracting = false;
    private final int selectedKelas = 0;
    private int downX;

    private EditText tpMin1, tpMax1, tpMin2, tpMax2, tpMin3, tpMax3,
            pbMin1, pbMax1, pbMin2, pbMax2, pbMin3, pbMax3,
            ldMin1, ldMax1, ldMin2, ldMax2, ldMin3, ldMax3,
            lsMin1, lsMax1, lsMin2, lsMax2, lsMin3, lsMax3;

    private EditText tpScore1, tpScore2, tpScore3, pbScore1,
            pbScore2, pbScore3, ldScore1, ldScore2,
            ldScore3, lsScore1, lsScore2, lsScore3;

    private BorderedLayout tpParam, pbParam, ldParam, lsParam;

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditKuantitatifBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        isJantan = getIntent().getBooleanExtra("isJantan", false);
        String data = getIntent().getStringExtra("data");
        entity = new Gson().fromJson(data, ScoreEntity.class);
        String jk = isJantan ? "JANTAN" : "BETINA";
        String title = getIntent().getStringExtra("title");
        //binding.rentang.setText(String.format("%s, umur %s", jk, title));
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setSubtitle(jk);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        tpParam = binding.tpParam;
        tpMin1 = binding.tpKelas1Min;
        tpMax1 = binding.tpKelas1Max;
        tpMin2 = binding.tpKelas2Min;
        tpMax2 = binding.tpKelas2Max;
        tpMin3 = binding.tpKelas3Min;
        tpMax3 = binding.tpKelas3Max;
        tpScore1 = binding.tpscore1et;
        tpScore2 = binding.tpscore2et;
        tpScore3 = binding.tpscore3et;

        showView(binding.tpkls1, false);
        showView(binding.tpukuran1, false);
        showView(binding.tpscore1, false);
        showView(binding.tpkls2, false);
        showView(binding.tpukuran2, false);
        showView(binding.tpscore2, false);
        showView(binding.tpkls3, false);
        showView(binding.tpukuran3, false);
        showView(binding.tpscore3, false);

        pbParam = binding.pbParam;
        pbMin1 = binding.pbKelas1Min;
        pbMax1 = binding.pbKelas1Max;
        pbMin2 = binding.pbKelas2Min;
        pbMax2 = binding.pbKelas2Max;
        pbMin3 = binding.pbKelas3Min;
        pbMax3 = binding.pbKelas3Max;
        pbScore1 = binding.pbscore1et;
        pbScore2 = binding.pbscore2et;
        pbScore3 = binding.pbscore3et;

        showView(binding.pbkls1, false);
        showView(binding.pbukuran1, false);
        showView(binding.pbscore1, false);
        showView(binding.pbkls2, false);
        showView(binding.pbukuran2, false);
        showView(binding.pbscore2, false);
        showView(binding.pbkls3, false);
        showView(binding.pbukuran3, false);
        showView(binding.pbscore3, false);

        ldParam = binding.ldParam;
        ldMin1 = binding.ldKelas1Min;
        ldMax1 = binding.ldKelas1Max;
        ldMin2 = binding.ldKelas2Min;
        ldMax2 = binding.ldKelas2Max;
        ldMin3 = binding.ldKelas3Min;
        ldMax3 = binding.ldKelas3Max;
        ldScore1 = binding.ldscore1et;
        ldScore2 = binding.ldscore2et;
        ldScore3 = binding.ldscore3et;

        showView(binding.ldkls1, false);
        showView(binding.ldukuran1, false);
        showView(binding.ldscore1, false);
        showView(binding.ldkls2, false);
        showView(binding.ldukuran2, false);
        showView(binding.ldscore2, false);
        showView(binding.ldkls3, false);
        showView(binding.ldukuran3, false);
        showView(binding.ldscore3, false);

        lsParam = binding.lsParam;
        lsMin1 = binding.lsKelas1Min;
        lsMax1 = binding.lsKelas1Max;
        lsMin2 = binding.lsKelas2Min;
        lsMax2 = binding.lsKelas2Max;
        lsMin3 = binding.lsKelas3Min;
        lsMax3 = binding.lsKelas3Max;
        lsScore1 = binding.lsscore1et;
        lsScore2 = binding.lsscore2et;
        lsScore3 = binding.lsscore3et;

        showView(lsParam, false);
        showView(binding.lskls1, false);
        showView(binding.lsukuran1, false);
        showView(binding.lsscore1, false);
        showView(binding.lskls2, false);
        showView(binding.lsukuran2, false);
        showView(binding.lsscore2, false);
        showView(binding.lskls3, false);
        showView(binding.lsukuran3, false);
        showView(binding.lsscore3, false);

        for (Parameter parameter : entity.getParameter()) {
            switch (parameter.getKode()) {
                case "TP":
                    if (parameter.getUkuran().size() > 0) {
                        for (Ukuran var : parameter.getUkuran()) {
                            switch (var.getKelas()) {
                                case "1":
                                    showView(binding.tpkls1, true);
                                    showView(binding.tpukuran1, true);
                                    showView(binding.tpscore1, true);
                                    tpMin1.setText(handleNol(var.getMin()));
                                    tpMax1.setText(handleNol(var.getMaks()));
                                    tpScore1.setText(var.getSkor());
                                    break;
                                case "2":
                                    showView(binding.tpkls2, true);
                                    showView(binding.tpukuran2, true);
                                    showView(binding.tpscore2, true);
                                    tpMin2.setText(handleNol(var.getMin()));
                                    tpMax2.setText(handleNol(var.getMaks()));
                                    tpScore2.setText(var.getSkor());
                                    break;
                                case "3":
                                    showView(binding.tpkls3, true);
                                    showView(binding.tpukuran3, true);
                                    showView(binding.tpscore3, true);
                                    tpMin3.setText(handleNol(var.getMin()));
                                    tpMax3.setText(handleNol(var.getMaks()));
                                    tpScore3.setText(var.getSkor());
                                    break;
                            }
                        }
                    } else {
                        showView(binding.tpkls1, true);
                        showView(binding.tpukuran1, true);
                        showView(binding.tpscore1, true);
                        showView(binding.tpkls2, true);
                        showView(binding.tpukuran2, true);
                        showView(binding.tpscore2, true);
                        showView(binding.tpkls3, true);
                        showView(binding.tpukuran3, true);
                        showView(binding.tpscore3, true);
                    }
                    break;
                case "PB":
                    if (parameter.getUkuran().size() > 0) {
                        for (Ukuran var : parameter.getUkuran()) {
                            switch (var.getKelas()) {
                                case "1":
                                    showView(binding.pbkls1, true);
                                    showView(binding.pbukuran1, true);
                                    showView(binding.pbscore1, true);
                                    pbMin1.setText(handleNol(var.getMin()));
                                    pbMax1.setText(handleNol(var.getMaks()));
                                    pbScore1.setText(var.getSkor());
                                    break;
                                case "2":
                                    showView(binding.pbkls2, true);
                                    showView(binding.pbukuran2, true);
                                    showView(binding.pbscore2, true);
                                    pbMin2.setText(handleNol(var.getMin()));
                                    pbMax2.setText(handleNol(var.getMaks()));
                                    pbScore2.setText(var.getSkor());
                                    break;
                                case "3":
                                    showView(binding.pbkls3, true);
                                    showView(binding.pbukuran3, true);
                                    showView(binding.pbscore3, true);
                                    pbMin3.setText(handleNol(var.getMin()));
                                    pbMax3.setText(handleNol(var.getMaks()));
                                    pbScore3.setText(var.getSkor());
                                    break;
                            }
                        }
                    } else {
                        showView(binding.pbkls1, true);
                        showView(binding.pbukuran1, true);
                        showView(binding.pbscore1, true);
                        showView(binding.pbkls2, true);
                        showView(binding.pbukuran2, true);
                        showView(binding.pbscore2, true);
                        showView(binding.pbkls3, true);
                        showView(binding.pbukuran3, true);
                        showView(binding.pbscore3, true);
                    }
                    break;
                case "LD":
                    if (parameter.getUkuran().size() > 0) {
                        for (Ukuran var : parameter.getUkuran()) {
                            switch (var.getKelas()) {
                                case "1":
                                    showView(binding.ldkls1, true);
                                    showView(binding.ldukuran1, true);
                                    showView(binding.ldscore1, true);
                                    ldMin1.setText(handleNol(var.getMin()));
                                    ldMax1.setText(handleNol(var.getMaks()));
                                    ldScore1.setText(var.getSkor());
                                    break;
                                case "2":
                                    showView(binding.ldkls2, true);
                                    showView(binding.ldukuran2, true);
                                    showView(binding.ldscore2, true);
                                    ldMin2.setText(handleNol(var.getMin()));
                                    ldMax2.setText(handleNol(var.getMaks()));
                                    ldScore2.setText(var.getSkor());
                                    break;
                                case "3":
                                    showView(binding.ldkls3, true);
                                    showView(binding.ldukuran3, true);
                                    showView(binding.ldscore3, true);
                                    ldMin3.setText(handleNol(var.getMin()));
                                    ldMax3.setText(handleNol(var.getMaks()));
                                    ldScore3.setText(var.getSkor());
                                    break;
                            }
                        }
                    } else {
                        showView(binding.ldkls1, true);
                        showView(binding.ldukuran1, true);
                        showView(binding.ldscore1, true);
                        showView(binding.ldkls2, true);
                        showView(binding.ldukuran2, true);
                        showView(binding.ldscore2, true);
                        showView(binding.ldkls3, true);
                        showView(binding.ldukuran3, true);
                        showView(binding.ldscore3, true);
                    }
                    break;
                case "LS":
                    if (parameter.getUkuran().size() > 0) {
                        showView(lsParam, true);
                        for (Ukuran var : parameter.getUkuran()) {
                            switch (var.getKelas()) {
                                case "1":
                                    showView(binding.lskls1, true);
                                    showView(binding.lsukuran1, true);
                                    showView(binding.lsscore1, true);
                                    lsMin1.setText(handleNol(var.getMin()));
                                    lsMax1.setText(handleNol(var.getMaks()));
                                    lsScore1.setText(var.getSkor());
                                    break;
                                case "2":
                                    showView(binding.lskls2, true);
                                    showView(binding.lsukuran2, true);
                                    showView(binding.lsscore2, true);
                                    lsMin2.setText(handleNol(var.getMin()));
                                    lsMax2.setText(handleNol(var.getMaks()));
                                    lsScore2.setText(var.getSkor());
                                    break;
                                case "3":
                                    showView(binding.lskls3, true);
                                    showView(binding.lsukuran3, true);
                                    showView(binding.lsscore3, true);
                                    lsMin3.setText(handleNol(var.getMin()));
                                    lsMax3.setText(handleNol(var.getMaks()));
                                    lsScore3.setText(var.getSkor());
                                    break;
                            }
                        }
                    } else {
                        showView(lsParam, true);
                        showView(binding.lskls1, true);
                        showView(binding.lsukuran1, true);
                        showView(binding.lsscore1, true);
                        showView(binding.lskls2, true);
                        showView(binding.lsukuran2, true);
                        showView(binding.lsscore2, true);
                        showView(binding.lskls3, true);
                        showView(binding.lsukuran3, true);
                        showView(binding.lsscore3, true);
                    }
                    break;
            }
        }

        binding.btnSimpan.setOnClickListener(v -> simpan());
    }

    private void simpan() {
        int[][][] arr = {
                {
                        {handleSimpan(tpMin1.getText().toString()), handleSimpan(tpMax1.getText().toString()), handleSimpan(tpScore1.getText().toString())},
                        {handleSimpan(tpMin2.getText().toString()), handleSimpan(tpMax2.getText().toString()), handleSimpan(tpScore2.getText().toString())},
                        {handleSimpan(tpMin3.getText().toString()), handleSimpan(tpMax3.getText().toString()), handleSimpan(tpScore3.getText().toString())},
                },
                {
                        {handleSimpan(pbMin1.getText().toString()), handleSimpan(pbMax1.getText().toString()), handleSimpan(pbScore1.getText().toString())},
                        {handleSimpan(pbMin2.getText().toString()), handleSimpan(pbMax2.getText().toString()), handleSimpan(pbScore2.getText().toString())},
                        {handleSimpan(pbMin3.getText().toString()), handleSimpan(pbMax3.getText().toString()), handleSimpan(pbScore3.getText().toString())},
                },
                {
                        {handleSimpan(ldMin1.getText().toString()), handleSimpan(ldMax1.getText().toString()), handleSimpan(ldScore1.getText().toString())},
                        {handleSimpan(ldMin2.getText().toString()), handleSimpan(ldMax2.getText().toString()), handleSimpan(ldScore2.getText().toString())},
                        {handleSimpan(ldMin3.getText().toString()), handleSimpan(ldMax3.getText().toString()), handleSimpan(ldScore3.getText().toString())},
                },
                {
                        {handleSimpan(lsMin1.getText().toString()), handleSimpan(lsMax1.getText().toString()), handleSimpan(lsScore1.getText().toString())},
                        {handleSimpan(lsMin2.getText().toString()), handleSimpan(lsMax2.getText().toString()), handleSimpan(lsScore2.getText().toString())},
                        {handleSimpan(lsMin3.getText().toString()), handleSimpan(lsMax3.getText().toString()), handleSimpan(lsScore3.getText().toString())},
                }
        };

        List<Parameter> parameters = new ArrayList<>();
        if (isJantan) {
            for (int j = 0; j < Score.parameter_jantan.length; j++) {
                Parameter parameter = new Parameter();
                parameter.setNama(Score.parameter_jantan[j]);
                parameter.setKode(Score.kode[j]);

                List<Ukuran> varsList = new ArrayList<>();
                for (int i = 0; i < Score.kelas.length; i++) {
                    int min = arr[j][i][0];
                    int max = arr[j][i][1];
                    String scr = min == 0 && max == 0 ? "" : arr[j][i][2] == 0 ? "" : String.valueOf(arr[j][i][2]);
                    if (min != 0 || max != 0) varsList.add(new Ukuran(Score.kelas[i], min, max, scr));
                }
                parameter.setUkuran(varsList);
                parameters.add(parameter);
            }
        } else {
            for (int j = 0; j < Score.parameter_betina.length; j++) {
                Parameter parameter = new Parameter();
                parameter.setNama(Score.parameter_betina[j]);
                parameter.setKode(Score.kode[j]);

                List<Ukuran> varsList = new ArrayList<>();
                for (int i = 0; i < Score.kelas.length; i++) {
                    int min = arr[j][i][0];
                    int max = arr[j][i][1];
                    String scr = min == 0 && max == 0 ? "" : arr[j][i][2] == 0 ? "" : String.valueOf(arr[j][i][2]);
                    if (min != 0 || max != 0) varsList.add(new Ukuran(Score.kelas[i], min, max, scr));
                }
                parameter.setUkuran(varsList);
                parameters.add(parameter);
            }
        }
        entity.setParameter(parameters);

        //Gson gson = new GsonBuilder().setPrettyPrinting().create();
        //String json = gson.toJson(entity);
        //Log.d("json", json);

        String data = new Gson().toJson(entity);
        Intent intent = new Intent();
        intent.putExtra("data", data);
        setResult(RESULT_OK, intent);
        finish();
    }

    private int handleSimpan(String num) {
        if (num.equals("≥") || num.isEmpty()) return 0;
        else return Integer.parseInt(num);
    }
    /*
    private void updateParam() {
        List<Parameter> parameters = entity.getParameter();
        for (int i = 0; i < parameters.size(); i++) {
            Parameter parameter = parameters.get(i);
            if (parameter.getNama().trim().equals(text.trim())) {
                newParam = parameter;
            }
        }
    }

    private void updateVars() {
        List<Vars> vartp = newParam.getVars();
        for (Vars var : vartp) {
            if (var.getKelas().equals(String.valueOf(selectedKelas))) {
                tpMin.setText(String.valueOf(var.getMin()));
                tpMax.setText(String.valueOf(var.getMaks()));
            } else {
                tpMin.setText("");
                tpMax.setText("");
            }
        }
    }
     */

    private String handleNol(int angka) {
        if (angka == 0) return "≥";
        else return String.valueOf(angka);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            downX = (int) event.getRawX();
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                int x = (int) event.getRawX();
                int y = (int) event.getRawY();
                //Was it a scroll - If skip all
                if (Math.abs(downX - x) > 5) {
                    return super.dispatchTouchEvent(event);
                }
                final int reducePx = 25;
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                //Bounding box is to big, reduce it just a little bit
                outRect.inset(reducePx, reducePx);
                if (!outRect.contains(x, y)) {
                    v.clearFocus();
                    boolean touchTargetIsEditText = false;
                    //Check if another editText has been touched
                    for (View vi : v.getRootView().getTouchables()) {
                        if (vi instanceof EditText) {
                            Rect clickedViewRect = new Rect();
                            vi.getGlobalVisibleRect(clickedViewRect);
                            //Bounding box is to big, reduce it just a little bit
                            clickedViewRect.inset(reducePx, reducePx);
                            if (clickedViewRect.contains(x, y)) {
                                touchTargetIsEditText = true;
                                break;
                            }
                        }
                    }
                    if (!touchTargetIsEditText) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    private void showView(View view, boolean show) {
        if (show) {
            if (view.getVisibility() == View.GONE) view.setVisibility(View.VISIBLE);
        } else {
            if (view.getVisibility() == View.VISIBLE) view.setVisibility(View.GONE);
        }
    }
}