package com.zam.sidik_padang.home.sklb.print.hitungscore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import com.zam.sidik_padang.databinding.ActivityScoringBinding;
import com.zam.sidik_padang.home.sklb.print.sertifikat.vm.Contract;
import com.zam.sidik_padang.home.sklb.setting.skor.Score;
import com.zam.sidik_padang.home.sklb.setting.skor.ScoreEntity;
import io.paperdb.Paper;

import static com.zam.sidik_padang.home.sklb.setting.kuantitatif.KuantitatifListFragment.kuantitatif_betina;
import static com.zam.sidik_padang.home.sklb.setting.kuantitatif.KuantitatifListFragment.kuantitatif_jantan;

public class ScoringActivity extends AppCompatActivity {//implements TextWatcher, Contract.Callback {

    private ActivityScoringBinding binding;
    private int isHari, isJantan;
    private List<ScoreEntity> jantanList, betinaList, entityList;
    private EditText etUmur, etTp, etPb, etLd, etLs;
    private String klsTp, klsPb, klsLd, klsLs, scTp, scPb, scLd, scLs;
    private Contract.Presenter presenter;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScoringBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Buat Sertifikat");
        toolbar.setNavigationOnClickListener(v -> finish());

        //presenter = new Presenter(this, this);
        jantanList = Paper.book().read(kuantitatif_jantan, new ArrayList<>());
        betinaList = Paper.book().read(kuantitatif_betina, new ArrayList<>());
        if (jantanList.size() == 0) {
            jantanList = Score.getKuantitatifJantan(this);
            Paper.book().write(kuantitatif_jantan, jantanList);
        }

        if (betinaList.size() == 0) {
            betinaList = Score.getKuantitatifBetina(this);
            Paper.book().write(kuantitatif_betina, betinaList);
        }

        //entityJantan = new Gson().fromJson(dataJantan, ScoreEntity.class);
        //entityBetina = new Gson().fromJson(dataBetina, ScoreEntity.class);

        etUmur = binding.umur;
        etTp = binding.ettp;
        etPb = binding.etpb;
        etLd = binding.etld;
        etLs = binding.etls;
        /*
        etUmur.addTextChangedListener(this);
        etTp.addTextChangedListener(this);
        etPb.addTextChangedListener(this);
        etLd.addTextChangedListener(this);
        etLs.addTextChangedListener(this);
         */

        binding.jenisKelamin.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
            boolean isChecked = checkedRadioButton.isChecked();
            if (isChecked) {
                isJantan = checkedRadioButton.getText().toString().equalsIgnoreCase("jantan") ? 1 : 2;
                binding.jkInfo.setText("");
                //presenter.setJenisKelaminDanUmur(isJantan, isHari, etUmur.getText().toString());
                /*
                if (isJantan == 1) {
                    entityList = jantanList;
                } else {
                    entityList = betinaList;
                }
                 */
            }
        });

        binding.rumur.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
            boolean isChecked = checkedRadioButton.isChecked();
            if (isChecked) {
                isHari = checkedRadioButton.getText().toString().toLowerCase().equals("hari") ? 1 : 2;
                //presenter.setJenisKelaminDanUmur(isJantan, isHari, etUmur.getText().toString());
            }
        });

        binding.hitung.setOnClickListener(v -> {
            boolean error = false;
            String msg = "-";
            if (isJantan == 0) {
                error = true;
                msg = "Jenis Kelamin belum dipilih";
            }
            if (isHari == 0) {
                error = true;
                msg = "Umur belum dipilih";
            }
            if (error) {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, klsTp, Toast.LENGTH_SHORT).show();
            }
        });
    }
    /*
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }

    @Override
    public void afterTextChanged(Editable editable) {
        if (timer != null) timer.cancel();
        if (editable.toString().trim().isEmpty()) return;
        timer = new Timer();
        timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        if (editable == etTp.getEditableText()) {
                            presenter.cariSkor("tp", editable.toString());
                        } else if (editable == etPb.getEditableText()) {
                            presenter.cariSkor("tp", editable.toString());
                        } else if (editable == etLd.getEditableText()) {
                            presenter.cariSkor("tp", editable.toString());
                        } else if (editable == etLs.getEditableText()) {
                            presenter.cariSkor("tp", editable.toString());
                        } else if (editable == etUmur.getEditableText()) {
                            binding.umurInfo.setText("");
                            presenter.setJenisKelaminDanUmur(isJantan, isHari, editable.toString());
                        }
                    }
                }, 700);
    }

    @Override
    public void onEntityFound(String data) {
        ScoreEntity entity = new Gson().fromJson(data, ScoreEntity.class);

        if (isHari== 1) {
            Log.d("entity", "hari_min:" + entity.getHariMin() + ", hari_max:" + entity.getHariMax());
        } else {
            Log.d("entity", "bulan_min:" + entity.getBulanMin() + ", bulan_max:" + entity.getBulanMax());
        }
        Log.d("entity", data);
    }

    @Override
    public void onScoringStart(String tag) {
        Log.d(tag, "mencari");
    }

    @Override
    public void onScoringSuccess(String tag, String kelas, String skor) {
        Log.d(tag, kelas+", "+skor);
        switch (tag) {
            case "tp" :
                binding.tpInfo.setText(String.format("Kelas:%s, Skor:%s", kelas, skor));
                break;
            case "pb" :
                binding.pbInfo.setText(String.format("Kelas:%s, Skor:%s", kelas, skor));
                break;
            case "ld" :
                binding.ldInfo.setText(String.format("Kelas:%s, Skor:%s", kelas, skor));
                break;
            case "ls" :
                binding.lsInfo.setText(String.format("Kelas:%s, Skor:%s", kelas, skor));
                break;
        }
    }

    @Override
    public void onScoringFailed(String tag, String msg) {
        Log.d(tag, msg);
        switch (tag) {
            case "tp" :
                binding.tpInfo.setText(msg);
                break;
            case "pb" :
                binding.pbInfo.setText(msg);
                break;
            case "ld" :
                binding.ldInfo.setText(msg);
                break;
            case "ls" :
                binding.lsInfo.setText(msg);
                break;
            case "umur" :
                binding.umurInfo.setText(msg);
                break;
        }
    }

    @Override
    public void onHitungStart() {

    }

    @Override
    public void onHitungSuccess(String data) {

    }

    @Override
    public void onHitungFailed(String msg) {

    }
    */
}