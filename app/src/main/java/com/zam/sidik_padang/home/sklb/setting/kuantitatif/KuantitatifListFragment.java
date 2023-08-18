package com.zam.sidik_padang.home.sklb.setting.kuantitatif;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.databinding.FragmentKuantitatifListBinding;
import com.zam.sidik_padang.home.dataternak.detailternak.BaseFragment;
import com.zam.sidik_padang.home.sklb.print.sertifikat.vm.SertifikatViewModel;
import com.zam.sidik_padang.home.sklb.setting.skor.ListKuantitatif;
import com.zam.sidik_padang.home.sklb.setting.skor.Parameter;
import com.zam.sidik_padang.home.sklb.setting.skor.Score;
import com.zam.sidik_padang.home.sklb.setting.skor.ScoreEntity;
import com.zam.sidik_padang.home.sklb.setting.skor.Ukuran;
import com.zam.sidik_padang.util.Util;
import io.paperdb.Paper;

import static android.app.Activity.RESULT_OK;
import static com.zam.sidik_padang.util.Config.CATATAN_SERTIFIKAT;
import static com.zam.sidik_padang.util.Config.JABATAN_DINAS;

public class KuantitatifListFragment extends BaseFragment implements KuantitatifAdapter.OnDataItemClickListener {

    static final String CUTOUT_EXTRA_SOURCE = "CUTOUT_EXTRA_SOURCE";
    static final String CUTOUT_EXTRA_RESULT = "CUTOUT_EXTRA_RESULT";
    static final String CUTOUT_EXTRA_BORDER_COLOR = "CUTOUT_EXTRA_BORDER_COLOR";
    static final String CUTOUT_EXTRA_CROP = "CUTOUT_EXTRA_CROP";

    public static final String kuantitatif_jantan = "kuantitatif_jantan";
    public static final String kuantitatif_betina = "kuantitatif_betina";
    public static final String setting_dinas = "setting_dinas";
    private FragmentKuantitatifListBinding binding;
    private List<ScoreEntity> jantanList;
    private List<ScoreEntity> betinaList;
    private KuantitatifAdapter adapterJantan;
    private KuantitatifAdapter adapterBetina;
    private ScoreEntity entity;
    private SettingDinas settingDinas;
    private int pos;
    private boolean isJantan;

    private AlertDialog dialog;
    private LayoutInflater inflater;

    private TextView namaDinas, nipDinas, catatan, jabatan;
    private ImageView ttdDinas;

    private SertifikatViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentKuantitatifListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //binding.fab.setOnClickListener(v -> startActivityForResult(new Intent(requireActivity(), EditKuantitatifActivity.class), 1000));
        viewModel = new ViewModelProvider(requireActivity()).get(SertifikatViewModel.class);
        settingDinas = Paper.book().read(setting_dinas, new SettingDinas("", "", JABATAN_DINAS, "", "", "", CATATAN_SERTIFIKAT));
        namaDinas = binding.namaDinas;
        nipDinas = binding.nipDinas;
        catatan = binding.catatan;
        ttdDinas = binding.ttdDinas;
        jabatan = binding.jabatan;

        namaDinas.setText(settingDinas.nama);
        nipDinas.setText(settingDinas.nip);
        if (settingDinas.jabatan == null || settingDinas.jabatan.equals("")) settingDinas.jabatan = JABATAN_DINAS;
        jabatan.setText(settingDinas.jabatan);
        if (settingDinas.catatan.equals("")) settingDinas.catatan = CATATAN_SERTIFIKAT;
        catatan.setText(settingDinas.catatan);
        ttdDinas.setImageBitmap(Util.base64ToBitmap(settingDinas.ttd));

        binding.addJantan.setOnClickListener(v -> {
            isJantan = true;
            showDialogForm("0", "0", "0", "0", true);
        });
        binding.addBetina.setOnClickListener(v -> {
            isJantan = false;
            showDialogForm("0", "0", "0", "0", true);
        });
        binding.namaDinasEdit.setOnClickListener(v -> showDialogDinas(settingDinas.nama, "nama"));
        binding.nipDinasEdit.setOnClickListener(v -> showDialogDinas(settingDinas.nip, "nip"));
        binding.catatanEdit.setOnClickListener(v -> showDialogCatatan(settingDinas.catatan));
        binding.jabatanEdit.setOnClickListener(v -> showDialogHeadTtd(settingDinas.jabatan));
        binding.ttdDinasEdit.setOnClickListener(v -> {
            startActivityForResult(new Intent(requireActivity(), TtdActivity.class), 1100);
        });

        adapterJantan = new KuantitatifAdapter(new ArrayList<>(), true, this);
        adapterBetina = new KuantitatifAdapter(new ArrayList<>(), false, this);

        RecyclerView rvJantan = binding.rvListJantan;
        RecyclerView rvBetina = binding.rvListBetina;

        rvJantan.setAdapter(adapterJantan);
        rvBetina.setAdapter(adapterBetina);
        setListKonten(false);
        observe();
    }

    private void observe() {
        viewModel.getResponseSettingSklb().observe(getViewLifecycleOwner(), setting -> {
            if (setting != null && setting.success && setting.data != null) {
                simpanSetting(setting.data);
            }
        });
        viewModel.getSettingSklb(user.userid);
    }

    private void simpanSetting(ListKuantitatif data) {
        jantanList = data.entityListJantan;
        betinaList = data.entityListBetina;
        adapterJantan.setList(jantanList);
        adapterBetina.setList(betinaList);

        Paper.book().write(kuantitatif_jantan, jantanList);
        Paper.book().write(kuantitatif_betina, betinaList);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_reset_kuantitatif, menu);
    }

    /** @noinspection deprecation*/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 1000100: {
                showDialogReset();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDialogReset() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setCancelable(true);
        builder.setIcon(R.drawable.ic_baseline_replay_24);
        builder.setTitle("Reset Kuantitatif").setMessage("Daftar kuantitatif akan dikembalikan ke pengaturan asal.");
        builder.setPositiveButton("RESET", (dialog, which) -> {
            Paper.book().delete(kuantitatif_jantan);
            Paper.book().delete(kuantitatif_betina);
            setListKonten(true);
        });
        builder.setNegativeButton("BATAL", null);
        dialog = builder.create();
        dialog.show();

        Typeface typeface = ResourcesCompat.getFont(requireActivity(), R.font.nunito_bold);
        Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        b.setTypeface(typeface);
    }

    private void setListKonten(boolean isReset) {
        jantanList = Paper.book().read(kuantitatif_jantan, new ArrayList<>());
        betinaList = Paper.book().read(kuantitatif_betina, new ArrayList<>());

        if (jantanList.size() == 0) {
            jantanList = Score.getKuantitatifJantan(requireContext());
            Paper.book().write(kuantitatif_jantan, jantanList);
        }

        if (betinaList.size() == 0) {
            betinaList = Score.getKuantitatifBetina(requireContext());
            Paper.book().write(kuantitatif_betina, betinaList);
        }
        adapterJantan.setList(jantanList);
        adapterBetina.setList(betinaList);

        if (isReset) saveToServer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void OnDataItemClick(boolean isJantan, int position, String title) {
        pos = position;
        this.isJantan = isJantan;
        if (isJantan) {
            entity = jantanList.get(position);
        } else {
            entity = betinaList.get(position);
        }
        String data = new Gson().toJson(entity);
        startActivityForResult(new Intent(requireActivity(), EditKuantitatifActivity.class)
                        .putExtra("title", title)
                        .putExtra("data", data)
                        .putExtra("isJantan", isJantan),
                1000);
    }

    @Override
    public void OnEditClick(boolean isJantan, int position) {
        pos = position;
        this.isJantan = isJantan;
        if (isJantan) {
            entity = jantanList.get(position);
        } else {
            entity = betinaList.get(position);
        }
        showDialogForm(
                String.valueOf(entity.getBulanMin()),
                String.valueOf(entity.getBulanMax()),
                String.valueOf(entity.getHariMin()),
                String.valueOf(entity.getHariMax()),
                false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            String ent = data.getStringExtra("data");
            updateList(ent);
        } else if (requestCode == 1100 && resultCode == RESULT_OK) {
            refreshDinas();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void refreshDinas() {
        settingDinas = Paper.book().read(setting_dinas, new SettingDinas("", "", JABATAN_DINAS, "", "", "", CATATAN_SERTIFIKAT));
        ttdDinas.setImageBitmap(Util.base64ToBitmap(settingDinas.ttd));
    }

    private void updateList(String ent) {
        entity = new Gson().fromJson(ent, ScoreEntity.class);
        if (isJantan) {
            jantanList.set(pos, entity);
            adapterJantan.notifyDataSetChanged();
            Paper.book().write(kuantitatif_jantan, jantanList);
        } else {
            betinaList.set(pos, entity);
            adapterBetina.notifyDataSetChanged();
            Paper.book().write(kuantitatif_betina, betinaList);
        }
        saveToServer();
    }

    private void saveToServer() {
        viewModel.sendKuantitatif(jantanList, betinaList, user.userid);
    }

    private void showDialogForm(String sbulanMin, String sbulanMax, String shariMin, String shariMax, boolean isAdd) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_rentang_umur, null);
        builder.setView(dialogView);
        builder.setCancelable(true);
        //builder.setIcon(R.mipmap.ic_launcher);
        String jk = isJantan ? "Jantan" : "Betina";
        builder.setTitle("Rentang Umur " + jk);

        EditText bulanMin = dialogView.findViewById(R.id.bulanMin);
        EditText bulanMax = dialogView.findViewById(R.id.bulanMax);
        EditText hariMin = dialogView.findViewById(R.id.hariMin);
        EditText hariMax = dialogView.findViewById(R.id.hariMax);

        bulanMin.setText(sbulanMin);
        bulanMax.setText(sbulanMax);
        hariMin.setText(shariMin);
        hariMax.setText(shariMax);

        builder.setPositiveButton("SIMPAN", null);
        builder.setNegativeButton("BATAL", null);
        if (!isAdd) builder.setNeutralButton("HAPUS", null);
        dialog = builder.create();
        dialog.show();

        Typeface typeface = ResourcesCompat.getFont(requireActivity(), R.font.nunito_bold);

        Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        b.setTypeface(typeface);
        b.setTextColor(Color.parseColor("#238A45"));
        b.setOnClickListener(view -> {
            String hmin = hariMin.getText().toString().trim();
            String hmax = hariMax.getText().toString().trim();
            String bmin = bulanMin.getText().toString().trim();
            String bmax = bulanMax.getText().toString().trim();
            if (hmin.equals("")) hmin = "0";
            if (hmax.equals("")) hmax = "0";
            if (bmin.equals("")) bmin = "0";
            if (bmax.equals("")) bmax = "0";

            if (hmin.equals("0") && hmax.equals("0") && bmin.equals("0") && bmax.equals("0")) {
                alert("wajib diisi salah satu");
            } else if (bmin.equals("0") && !bmax.equals("0")) {
                alert("minimal bulan belum diisi");
            } else {
                onDismisDialogUmur(hmin, hmax, bmin, bmax, isAdd);
                dialog.dismiss();
            }
        });

        Button bc = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        bc.setTypeface(typeface);

        if (!isAdd) {
            Button bn = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
            bn.setTypeface(typeface);
            bn.setTextColor(Color.parseColor("#d50000"));
            bn.setOnClickListener(v -> {
                hapusItem();
                dialog.dismiss();
            });
        }
    }

    private void hapusItem() {
        if (isJantan) {
            jantanList.remove(pos);
            adapterJantan.notifyDataSetChanged();
            Paper.book().write(kuantitatif_jantan, jantanList);
        } else {
            betinaList.remove(pos);
            adapterBetina.notifyDataSetChanged();
            Paper.book().write(kuantitatif_betina, betinaList);
        }
        viewModel.hapusKuantitatif(user.userid);
    }

    private void alert(String msg) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    private void onDismisDialogUmur(String shariMin, String shariMax, String sbulanMin, String sbulanMax, boolean isAdd) {
        if (shariMin.equals("")) shariMin = "0";
        if (shariMax.equals("")) shariMax = "0";
        if (sbulanMin.equals("")) sbulanMin = "0";
        if (sbulanMax.equals("")) sbulanMax = "0";
        if (isAdd) {
            ScoreEntity newEntity = new ScoreEntity();
            newEntity.setBulanMin(Integer.parseInt(sbulanMin));
            newEntity.setBulanMax(Integer.parseInt(sbulanMax));
            newEntity.setHariMin(Integer.parseInt(shariMin));
            newEntity.setHariMax(Integer.parseInt(shariMax));

            List<Parameter> parameters = new ArrayList<>();
            if (isJantan) {
                for (int j = 0; j < Score.parameter_jantan.length; j++) {
                    Parameter parameter = new Parameter();
                    parameter.setNama(Score.parameter_jantan[j]);
                    parameter.setKode(Score.kode[j]);

                    List<Ukuran> varsList = new ArrayList<>();
                    parameter.setUkuran(varsList);
                    parameters.add(parameter);
                }
                newEntity.setParameter(parameters);
                jantanList.add(newEntity);
                adapterJantan.notifyDataSetChanged();
            } else {
                for (int j = 0; j < Score.parameter_betina.length; j++) {
                    Parameter parameter = new Parameter();
                    parameter.setNama(Score.parameter_betina[j]);
                    parameter.setKode(Score.kode[j]);

                    List<Ukuran> varsList = new ArrayList<>();
                    parameter.setUkuran(varsList);
                    parameters.add(parameter);
                }
                newEntity.setParameter(parameters);
                betinaList.add(newEntity);
                adapterBetina.notifyDataSetChanged();
            }
        } else {
            entity.setBulanMin(Integer.parseInt(sbulanMin));
            entity.setBulanMax(Integer.parseInt(sbulanMax));
            entity.setHariMin(Integer.parseInt(shariMin));
            entity.setHariMax(Integer.parseInt(shariMax));
            if (isJantan) {
                jantanList.set(pos, entity);
                adapterJantan.notifyDataSetChanged();
            } else {
                betinaList.set(pos, entity);
                adapterBetina.notifyDataSetChanged();
            }
        }

        Collections.sort(jantanList, (obj1, obj2) -> {
            //return obj1.firstName.compareToIgnoreCase(obj2.firstName); // To compare string values
            if (obj1.getBulanMin() == 0 && obj2.getBulanMin() == 0) {
                return Integer.compare(obj1.getHariMax(), obj2.getHariMax()); // To compare integer values
            } else {
                return Integer.compare(obj1.getBulanMin(), obj2.getBulanMin()); // To compare integer values
            }
            // ## Descending order dengan obj dibalik
        });

        Collections.sort(betinaList, (obj1, obj2) -> {
            //return obj1.firstName.compareToIgnoreCase(obj2.firstName); // To compare string values
            if (obj1.getBulanMin() == 0 && obj2.getBulanMin() == 0) {
                return Integer.compare(obj1.getHariMax(), obj2.getHariMax()); // To compare integer values
            } else {
                return Integer.compare(obj1.getBulanMin(), obj2.getBulanMin()); // To compare integer values
            }
            // ## Descending order dengan obj dibalik
        });

        Paper.book().write(kuantitatif_jantan, jantanList);
        Paper.book().write(kuantitatif_betina, betinaList);
    }

    private void showDialogDinas(String value, String tag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edittext, null);
        builder.setView(dialogView);
        builder.setCancelable(true);
        //builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(tag.toUpperCase());

        EditText isi = dialogView.findViewById(R.id.isi);
        isi.setText(value);

        builder.setPositiveButton("SIMPAN", null);
        builder.setNegativeButton("BATAL", null);
        dialog = builder.create();
        dialog.show();

        Typeface typeface = ResourcesCompat.getFont(requireActivity(), R.font.nunito_bold);

        Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        b.setTypeface(typeface);
        b.setTextColor(Color.parseColor("#238A45"));
        b.setOnClickListener(view -> {
            String val = isi.getText().toString().trim();
            if (val.equals("")) {
                alert("Wajib diisi");
            } else {
                onDismisDialogDinas(val, tag);
                dialog.dismiss();
            }
        });

        Button bc = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        bc.setTypeface(typeface);
    }

    private void onDismisDialogDinas(String value, String tag) {
        if (tag.equals("nama")) {
            namaDinas.setText(value);
            settingDinas.nama = value;
        } else {
            nipDinas.setText(value);
            settingDinas.nip = value;
        }
        Paper.book().write(setting_dinas, settingDinas);
        viewModel.sendSettingDinas(settingDinas, user.userid);
    }

    private void showDialogCatatan(String value) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_catatan, null);
        builder.setView(dialogView);
        builder.setCancelable(true);
        //builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("Catatan");

        EditText isi = dialogView.findViewById(R.id.isi);
        isi.setText(value);

        builder.setPositiveButton("SIMPAN", null);
        builder.setNegativeButton("BATAL", null);
        dialog = builder.create();
        dialog.show();

        Typeface typeface = ResourcesCompat.getFont(requireActivity(), R.font.nunito_bold);

        Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        b.setTypeface(typeface);
        b.setTextColor(Color.parseColor("#238A45"));
        b.setOnClickListener(view -> {
            String val = isi.getText().toString().trim();
            onDismisDialogCatatan(val);
            dialog.dismiss();
        });

        Button bc = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        bc.setTypeface(typeface);
    }

    private void onDismisDialogCatatan(String value) {
        catatan.setText(value);
        settingDinas.catatan = value;
        Paper.book().write(setting_dinas, settingDinas);
        viewModel.sendSettingDinas(settingDinas, user.userid);
    }

    private void showDialogHeadTtd(String value) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_head_ttd, null);
        builder.setView(dialogView);
        builder.setCancelable(true);
        //builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("Header Ttd");

        String[] splited = value.split("\n");
        EditText isi1 = dialogView.findViewById(R.id.isi1);
        EditText isi2 = dialogView.findViewById(R.id.isi2);
        EditText isi3 = dialogView.findViewById(R.id.isi3);
        isi1.setText(splited[0]);
        isi2.setText(splited[1]);
        isi3.setText(splited[2]);

        builder.setPositiveButton("SIMPAN", null);
        builder.setNegativeButton("BATAL", null);
        dialog = builder.create();
        dialog.show();

        Typeface typeface = ResourcesCompat.getFont(requireActivity(), R.font.nunito_bold);

        Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        b.setTypeface(typeface);
        b.setTextColor(Color.parseColor("#238A45"));
        b.setOnClickListener(view -> {
            String val1 = isi1.getText().toString().trim();
            String val2 = isi2.getText().toString().trim();
            String val3 = isi3.getText().toString().trim();
            if (val1.equals("") || val2.equals("")) {
                Toast.makeText(requireActivity(), "Baris 1 harus diisi", Toast.LENGTH_SHORT).show();
            } else {
                onDismisDialogHeadTtd(val1 + "\n" + val2 + "\n" + val3);
                dialog.dismiss();
            }
        });

        Button bc = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        bc.setTypeface(typeface);
    }

    private void onDismisDialogHeadTtd(String value) {
        jabatan.setText(value);
        settingDinas.jabatan = value;
        Paper.book().write(setting_dinas, settingDinas);
        viewModel.sendSettingDinas(settingDinas, user.userid);
    }

    private void saveTtd() {
    }
}