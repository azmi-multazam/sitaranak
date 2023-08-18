package com.zam.sidik_padang.home.sklb.print;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.moagrius.widget.ScalingScrollView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.databinding.ActivitySertifikatGeneratorBinding;
import com.zam.sidik_padang.databinding.ActivitySertifikatSettingBinding;
import com.zam.sidik_padang.home.sklb.SklbActivity;
import com.zam.sidik_padang.home.sklb.print.sertifikat.vm.Contract;
import com.zam.sidik_padang.home.sklb.print.sertifikat.vm.Presenter;
import com.zam.sidik_padang.home.sklb.print.sertifikat.vm.SertifikatTernakPetugas;
import com.zam.sidik_padang.home.sklb.setting.kuantitatif.SettingDinas;
import com.zam.sidik_padang.util.Util;
import io.paperdb.Paper;

import static com.zam.sidik_padang.home.sklb.print.sertifikat.KhususFragment.GRADE;
import static com.zam.sidik_padang.home.sklb.setting.kuantitatif.KuantitatifListFragment.setting_dinas;
import static com.zam.sidik_padang.util.Config.BASE_SERTIFIKAT;
import static com.zam.sidik_padang.util.Config.CATATAN_SERTIFIKAT;
import static com.zam.sidik_padang.util.Config.JABATAN_DINAS;

public class SertifikatGeneratorActivity extends BaseLogedinActivity implements Contract.Callback {

    private ActivitySertifikatSettingBinding binding;
    private SertifikatTernakPetugas sertifikat;
    private SettingDinas settingDinas;
    private Grade grade;
    private String data;
    private String klsTp, klsPb, klsLd, klsLs, scTp="", scPb="", scLd="", scLs="";
    private int tahun, bulan, hari;
    private TextView tvPrevKodevikasi, tvPrevBapak, tvPrevAlamat,
            tvKodevikasi, tvBapak, tvAlamat, namaDinas, nipDinas, tvCatatan, tvJabatan, tvNoSurat, tvTanggal;
    private ImageView ttdDinas;
    private Bitmap bitmapFoto;
    private Contract.Presenter presenter;
    private boolean isBetina;
    private AlertDialog progressGenerate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySertifikatSettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        presenter = new Presenter(this, this);
        data = getIntent().getStringExtra("data");
        sertifikat = new Gson().fromJson(data, SertifikatTernakPetugas.class);
        grade = Paper.book().read(GRADE+sertifikat.getId(),
                new Grade(sertifikat.getId(),"","","","","", "","","",""));

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Buat Sertifikat");
        toolbar.setNavigationOnClickListener(v -> finish());

        tahun = sertifikat.getUmur();
        bulan = sertifikat.getBulan();
        hari = sertifikat.getHari();
        isBetina = sertifikat.getKelamin().trim().toLowerCase().equals("betina");
        binding.forJantan.setVisibility(isBetina ? View.GONE : View.VISIBLE);
        binding.prevKodefikasi.setText(String.valueOf(sertifikat.getKodeVerifikasi()));
        binding.prevRumpunMurni.setText(sertifikat.getBangsa());
        binding.prevJenisKelamin.setText(sertifikat.getKelamin());
        binding.prevBapak.setText(sertifikat.getBapak());
        binding.prevMetodePerkawinan.setText(sertifikat.getHasilPerkawinan());

        String stahun = sertifikat.getUmur() == 0 ? "" : sertifikat.getUmur() + " Tahun";
        String sbulan = sertifikat.getBulan() == 0 ? "" : sertifikat.getBulan() + " Bulan";
        String shari = sertifikat.getHari() == 0 ? "" : sertifikat.getHari() + " Hari";
        binding.prevUmur.setText(stahun + " " + sbulan + " " + shari);
        binding.prevTinggiGumba.setText(String.valueOf(sertifikat.getTinggiGumba()));
        binding.prevPanjangBadan.setText(String.valueOf(sertifikat.getPanjangBadan()));
        binding.prevLingkarDada.setText(String.valueOf(sertifikat.getLingkarDada()));
        if (!isBetina) binding.prevLingkarScrotum.setText(String.valueOf(sertifikat.getLingkarScrotum()));

        binding.prevPemilik.setText(sertifikat.getNamaPemilik());
        binding.prevAlamat.setText(sertifikat.getAlamatPemilik());

        tvJabatan = binding.jabatan;
        namaDinas = binding.prevKadin;
        nipDinas = binding.prevNip;
        ttdDinas = binding.prevTtd;
        tvNoSurat = binding.prevNoSurat;
        tvTanggal = binding.prevTgl;
        tvCatatan = binding.prevCatatan;

        tvNoSurat.setText(grade.noSurat);
        tvTanggal.setText(grade.tanggal);
        tvCatatan.setText(grade.catatan);
        tvJabatan.setText(grade.jabatan);

        binding.editDinas.setOnClickListener(v -> startActivity(new Intent(this, SklbActivity.class).putExtra("dinas", "edit")));
        binding.suratEdit.setOnClickListener(v -> showDialogNomorSurat(grade.noSurat));
        binding.tanggalEdit.setOnClickListener(v -> showDialogTanggalSurat(grade.tanggal));
        binding.catatanEdit.setOnClickListener(v -> showDialogCatatan(grade.catatan));

        binding.generateSertifikat.setOnClickListener(v -> validateGrade());

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            Glide.with(this)
                    .load(sertifikat.getFoto())
                    .error(R.drawable.ic_sapi_soft)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            //binding.progressFoto.setVisibility(View.GONE);
                            bitmapFoto = getBitmap(ContextCompat.getDrawable(SertifikatGeneratorActivity.this, R.drawable.ic_sapi_soft));
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            //binding.progressFoto.setVisibility(View.GONE);
                            bitmapFoto = getBitmap((Drawable) resource);
                            return false;
                        }
                    })
                    .into(binding.prevFoto);
            hitungBulan();
        }, 1000);
    }

    @Override
    public void onResume() {
        super.onResume();
        settingDinas = Paper.book().read(setting_dinas, new SettingDinas("", "", JABATAN_DINAS,"", "", "", CATATAN_SERTIFIKAT));
        if (settingDinas.catatan.equals("")) settingDinas.catatan = CATATAN_SERTIFIKAT;
        if (settingDinas.jabatan == null || settingDinas.jabatan.equals("")) settingDinas.jabatan = JABATAN_DINAS;
        tvJabatan.setText(settingDinas.jabatan);
        namaDinas.setText(settingDinas.nama);
        nipDinas.setText(settingDinas.nip);
        ttdDinas.setImageBitmap(Util.base64ToBitmap(settingDinas.ttd));

        if (grade.noSurat.equals("")) {
            grade.noSurat = settingDinas.noSurat;
            tvNoSurat.setText(grade.noSurat);
        }
        if (grade.tanggal.equals("")) {
            grade.tanggal = settingDinas.tglSurat;
            tvTanggal.setText(grade.tanggal);
        }

        if (grade.catatan.equals("")) {
            grade.catatan = settingDinas.catatan;
            tvCatatan.setText(grade.catatan);
        }

        grade.jabatan = settingDinas.jabatan;
        grade.kadin = settingDinas.nama;
        grade.nip = settingDinas.nip;
        grade.ttd = settingDinas.ttd;
        Paper.book().write(GRADE+sertifikat.getId(), grade);
    }

    private void hitungBulan(){
        int ju, jk, umur;
        if (tahun > 0 && bulan > 0) {
            umur = tahun * 12;
            umur += bulan;
            if (hari > 27) umur += 1;
            ju = 2;
        } else {
            umur = hari;
            ju = 1;
        }

        if (sertifikat.getKelamin().trim().toLowerCase().equals("betina")) {
            jk = 2;
        } else {
            jk = 1;
        }
        presenter.setJenisKelaminDanUmur(jk, ju, umur);
    }

    private void validateGrade() {
        boolean error = false;
        String msg = "-";
        if (grade.grade.equals("")) {
            error = true;
            msg = "Grade tidak diketahui";
        }
        if (grade.skor.equals("")) {
            error = true;
            msg = "Tidak ada skor";
        }
        if (settingDinas.nama.equals("")) {
            error = true;
            msg = "Tidak ada nama Kepala Dinas";
        }
        if (grade.noSurat.equals("")) {
            error = true;
            msg = "Tidak ada nomor surat";
        }
        if (grade.tanggal.equals("")) {
            error = true;
            msg = "Tidak ada tanggal";
        }
        if (error) {
            alert(msg);
        } else {
            generate();
        }
    }

    public void generate() {
        showDialogGenerate();
        new Handler().postDelayed(() -> {
            View view = LayoutInflater.from(this).inflate(R.layout.view_sertifikat, null);

            ScalingScrollView scalingScrollView = view.findViewById(R.id.scalingscrollview);
            scalingScrollView.setScaleLimits(0, 10);
            scalingScrollView.setShouldVisuallyScaleContents(true);

            String sNo = "NO: "+grade.noSurat;
            ((TextView) view.findViewById(R.id.noSurat)).setText(sNo);
            ((ImageView) view.findViewById(R.id.foto)).setImageBitmap(bitmapFoto);
            ((TextView) view.findViewById(R.id.kodefikasi)).setText(String.valueOf(sertifikat.getKodeVerifikasi()));
            ((TextView) view.findViewById(R.id.rumpunMurni)).setText(sertifikat.getBangsa());
            ((TextView) view.findViewById(R.id.jenisKelamin)).setText(sertifikat.getKelamin());
            ((TextView) view.findViewById(R.id.bapak)).setText(sertifikat.getBapak());
            ((TextView) view.findViewById(R.id.metodePerkawinan)).setText(sertifikat.getHasilPerkawinan());

            String tahun = sertifikat.getUmur() == 0 ? "" : sertifikat.getUmur() + " Tahun";
            String bulan = sertifikat.getBulan() == 0 ? "" : sertifikat.getBulan() + " Bulan";
            String hari = sertifikat.getHari() == 0 ? "" : sertifikat.getHari() + " Hari";
            String umur = tahun + " " + bulan + " " + hari;
            ((TextView) view.findViewById(R.id.umur)).setText(umur);
            ((TextView) view.findViewById(R.id.tinggiGumba)).setText(String.valueOf(sertifikat.getTinggiGumba()));
            ((TextView) view.findViewById(R.id.panjangBadan)).setText(String.valueOf(sertifikat.getPanjangBadan()));
            ((TextView) view.findViewById(R.id.lingkarDada)).setText(String.valueOf(sertifikat.getLingkarDada()));

            TextView tvLs = view.findViewById(R.id.lingkarScrotum);
            if (!isBetina) tvLs.setText(String.valueOf(sertifikat.getLingkarScrotum()));

            ((TextView) view.findViewById(R.id.pemilik)).setText(sertifikat.getNamaPemilik());
            ((TextView) view.findViewById(R.id.alamat)).setText(sertifikat.getAlamatPemilik());
            ((TextView) view.findViewById(R.id.catatan)).setText(grade.catatan);

            ((TextView) view.findViewById(R.id.tanggal)).setText(grade.tanggal);
            ((TextView) view.findViewById(R.id.jabatan)).setText(grade.jabatan);
            ((TextView) view.findViewById(R.id.kadin)).setText(grade.kadin);
            ((ImageView) view.findViewById(R.id.ttdDinas)).setImageBitmap(Util.base64ToBitmap(grade.ttd));
            String nip = "NIP: "+grade.nip;
            ((TextView) view.findViewById(R.id.nip)).setText(nip);
            String sgrade = "GRADE "+grade.grade;
            ((TextView) view.findViewById(R.id.grade)).setText(sgrade);

            Bitmap bitmap = generateQr(BASE_SERTIFIKAT+"?id="+sertifikat.getId());
            if (bitmap!=null) {
                Bitmap overlay = BitmapFactory.decodeResource(getResources(), R.drawable.logo_pamekasan_80);
                Bitmap bmpMerged = mergeBitmaps(overlay, bitmap);
                ((ImageView) view.findViewById(R.id.barcode)).setImageBitmap(bmpMerged);
            }

            view.layout(0, 0, view.getWidth(), view.getHeight());

            Bitmap b = Util.loadBitmapFromView(view);
            String base = Util.bitmapToBase64(b);
            Paper.book().write("sertifikat_" + sertifikat.getId(), base);

            progressGenerate.dismiss();
            startActivity(new Intent(this, LihatSertifikatActivity.class)
                    .putExtra("nama", "sertifikat_" + sertifikat.getId())
                    .putExtra("data", data)
            );
            finish();

        }, 400);
    }

    private Bitmap generateQr(String qr) {
        Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        QRCodeWriter writer = new QRCodeWriter();
        Bitmap bmp = null;
        try {
            BitMatrix bitMatrix = writer.encode(qr, BarcodeFormat.QR_CODE, 800, 800, hintMap);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
        } catch (WriterException e) {
            Log.e("GenerateQr", "WriteException");
        }
        return bmp;
    }

    public Bitmap mergeBitmaps(Bitmap overlay, Bitmap bitmap) {

        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        Bitmap combined = Bitmap.createBitmap(width, height, bitmap.getConfig());
        Canvas canvas = new Canvas(combined);
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        canvas.drawBitmap(bitmap, new Matrix(), null);

        int centreX = (canvasWidth  - overlay.getWidth()) /2;
        int centreY = (canvasHeight - overlay.getHeight()) /2 ;
        canvas.drawBitmap(overlay, centreX, centreY, null);

        return combined;
    }

    @Override
    public void onEntityFound(String data) {
        //ScoreEntity entity = new Gson().fromJson(data, ScoreEntity.class);
        /*
        if (isHari== 1) {
            Log.d("entity", "hari_min:" + entity.getHariMin() + ", hari_max:" + entity.getHariMax());
        } else {
            Log.d("entity", "bulan_min:" + entity.getBulanMin() + ", bulan_max:" + entity.getBulanMax());
        }
         */
        Log.d("entity", data);
        presenter.cariSkor("tp", sertifikat.getTinggiGumba());
    }

    @Override
    public void onScoringStart(String tag) {
        Log.d(tag, "mencari");
    }

    @Override
    public void onScoringSuccess(String tag, String msg, String kelas, String skor) {
        Log.d(tag, kelas+", "+skor);
        binding.progressFoto.setVisibility(View.GONE);
        int color;
        if (msg.contains("\u2716")) color = Color.parseColor("#d50000");
        else color = Color.parseColor("#238A45");
        switch (tag) {
            case "tp" :
                klsTp = kelas;
                scTp = skor;
                binding.tpInfo.setText(msg);
                binding.tpInfo.setTextColor(color);
                showView(binding.tpInfo, true);
                presenter.cariSkor("pb", sertifikat.getPanjangBadan());
                break;
            case "pb" :
                klsPb = kelas;
                scPb = skor;
                binding.pbInfo.setText(msg);
                binding.pbInfo.setTextColor(color);
                showView(binding.pbInfo, true);
                presenter.cariSkor("ld", sertifikat.getLingkarDada());
                break;
            case "ld" :
                klsLd = kelas;
                scLd = skor;
                binding.ldInfo.setText(msg);
                binding.ldInfo.setTextColor(color);
                showView(binding.ldInfo, true);
                if (!isBetina) presenter.cariSkor("ld", sertifikat.getLingkarScrotum());
                break;
            case "ls" :
                klsLs = kelas;
                scLs = skor;
                binding.lsInfo.setText(msg);
                binding.lsInfo.setTextColor(color);
                showView(binding.lsInfo, true);
                break;
        }

        Log.d("total score", scTp+scPb+scLd+scLs);

        if (!scTp.equals("") || !scPb.equals("") || !scLd.equals("")) {
            presenter.hitung(scTp, scPb, scLd);
        }
    }

    @Override
    public void onScoringFailed(String tag, String msg) {
        Log.d(tag, msg);
        binding.progressFoto.setVisibility(View.GONE);
        switch (tag) {
            case "tp" :
                binding.tpInfo.setText(msg);
                binding.tpInfo.setTextColor(Color.parseColor("#d50000"));
                showView(binding.tpInfo, true);
                break;
            case "pb" :
                binding.pbInfo.setText(msg);
                binding.pbInfo.setTextColor(Color.parseColor("#d50000"));
                showView(binding.pbInfo, true);
                break;
            case "ld" :
                binding.ldInfo.setText(msg);
                binding.ldInfo.setTextColor(Color.parseColor("#d50000"));
                showView(binding.ldInfo, true);
                break;
            case "ls" :
                binding.lsInfo.setText(msg);
                binding.lsInfo.setTextColor(Color.parseColor("#d50000"));
                showView(binding.lsInfo, true);
                break;
            case "umur" :
                binding.umurInfo.setText(msg);
                binding.umurInfo.setTextColor(Color.parseColor("#d50000"));
                showView(binding.umurInfo, true);
                break;
        }
    }

    @Override
    public void onHitungStart(String msg) {
        Log.d("score", msg);
    }

    @Override
    public void onHitungSuccess(String sskor, String sgrade) {
        grade.grade = sgrade;
        grade.skor = sskor;
        binding.total.setText(sskor);
        binding.grade.setText(sgrade);
        Paper.book().write(GRADE+sertifikat.getId(), grade);
    }

    @Override
    public void onHitungFailed(String msg) {
        Log.d("hitung", msg);
    }

    private void showDialogGenerate() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_progress_generate, null, false);
        progressGenerate = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .show();
    }

    private void showDialogNomorSurat(String value) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edittext, null);
        builder.setView(dialogView);
        builder.setCancelable(true);
        //builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("Nomor Surat");

        EditText isi = dialogView.findViewById(R.id.isi);
        isi.setText(value);

        builder.setPositiveButton("SIMPAN", null);
        builder.setNegativeButton("BATAL", null);
        AlertDialog dialog = builder.create();
        dialog.show();

        Typeface typeface = ResourcesCompat.getFont(this, R.font.nunito_bold);

        Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        b.setTypeface(typeface);
        b.setTextColor(Color.parseColor("#238A45"));
        b.setOnClickListener(view -> {
            String val = isi.getText().toString().trim();
            if (val.equals("")) {
                alert("Wajib diisi");
            } else {
                onDismisDialogNomorSurat(val);
                dialog.dismiss();
            }
        });

        Button bc = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        bc.setTypeface(typeface);
    }

    private void onDismisDialogNomorSurat(String value) {
        tvNoSurat.setText(value);
        grade.noSurat = value;
        Paper.book().write(GRADE+sertifikat.getId(), grade);
        settingDinas.noSurat = value;
        Paper.book().write(setting_dinas, settingDinas);
    }

    private void showDialogTanggalSurat(String tanggal) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePicker = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            Calendar birthDayCalendar = Calendar.getInstance();
            birthDayCalendar.set(Calendar.YEAR, year);
            birthDayCalendar.set(Calendar.MONTH, month);
            birthDayCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String resultTgl = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(birthDayCalendar.getTime());
            onDismisDialogTanggalSurat(resultTgl);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }

    private void onDismisDialogTanggalSurat(String tanggal) {
        grade.tanggal = "Padang, "+tanggal;
        tvTanggal.setText(grade.tanggal);
        Paper.book().write(GRADE+sertifikat.getId(), grade);
        settingDinas.tglSurat = "Padang, "+tanggal;
        Paper.book().write(setting_dinas, settingDinas);
    }

    private void showDialogCatatan(String value) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_catatan, null);
        builder.setView(dialogView);
        builder.setCancelable(true);
        //builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("Catatan");

        EditText isi = dialogView.findViewById(R.id.isi);
        isi.setText(value);

        builder.setPositiveButton("SIMPAN", null);
        builder.setNegativeButton("BATAL", null);
        AlertDialog dialog = builder.create();
        dialog.show();

        Typeface typeface = ResourcesCompat.getFont(this, R.font.nunito_bold);

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

    private void alert(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void onDismisDialogCatatan(String value) {
        tvCatatan.setText(value);
        grade.catatan = value;
        Paper.book().write(GRADE+sertifikat.getId(), grade);
    }

    private void showView(View view, boolean animate) {
        if (view != null && view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
            if (animate) view.animate().alpha(1).setDuration(200).start();
        }
    }

    private void hideView(final View view, boolean animate) {
        if (view != null && view.getVisibility() == View.VISIBLE) {
            if (animate) {
                view.animate().alpha(0).setDuration(200).start();
                final Handler handler = new Handler();
                handler.postDelayed(() -> view.setVisibility(View.GONE), 200);
            } else {
                view.setVisibility(View.GONE);
            }
        }
    }

    private Bitmap getBitmap(Drawable drawable) {
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } else {
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            width = width > 0 ? width : 1;
            height = height > 0 ? height : 1;

            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas cvs = new Canvas(bitmap);
            drawable.setBounds(0, 0, cvs.getWidth(), cvs.getHeight());
            drawable.draw(cvs);
        }
        return bitmap;
    }

}