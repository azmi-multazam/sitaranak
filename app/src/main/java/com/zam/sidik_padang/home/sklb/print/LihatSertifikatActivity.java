package com.zam.sidik_padang.home.sklb.print;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.jsibbold.zoomage.ZoomageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.zam.sidik_padang.BaseLogedinActivity;
//import com.zam.sidik_padang.BuildConfig;
import com.zam.sidik_padang.databinding.ActivityLihatSertifikatBinding;
import com.zam.sidik_padang.home.sklb.print.hitungscore.ScoringActivity;
import com.zam.sidik_padang.util.Util;
import io.paperdb.Paper;

public class LihatSertifikatActivity extends BaseLogedinActivity {

    private ActivityLihatSertifikatBinding binding;
    private ZoomageView zoomageView;
    private String nama;
    private Bitmap bitmap;
    private BottomSheetBehavior sheetBehavior;
    private LinearLayout bottom_sheet;
    //private SertifikatTernakPetugas sertifikat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLihatSertifikatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Sertifikat");
        toolbar.setNavigationOnClickListener(v -> finish());

        String data = getIntent().getStringExtra("data");
        //sertifikat = new Gson().fromJson(data, SertifikatTernakPetugas.class);
        binding.edit.setOnClickListener(v -> {
            Intent intent = new Intent(this, SertifikatGeneratorActivity.class);
            intent.putExtra("data", data);
            startActivity(intent);
            finish();
        });

        bottom_sheet = binding.bottomSheetLayout;
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);

        binding.share.setOnClickListener(v -> {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            new Handler().postDelayed(this::checkPdf, 100);
        });
        binding.print.setVisibility(View.GONE);

        new Handler().postDelayed(() -> {
            nama = getIntent().getStringExtra("nama");
            String base = Paper.book().read(nama);
            zoomageView = binding.zoomageView;
            bitmap = Util.base64ToBitmap(base);
            zoomageView.setImageBitmap(bitmap);

            binding.progress.setVisibility(View.INVISIBLE);
        }, 300);
    }

    private void checkPdf() {
        File dir = new File(getFilesDir(), "sertifikat");
        if (dir.exists()) {
            createPdf(dir);
            /*
            if (dir.listFiles().length >0 ) {
                boolean exist = false;
                for (File f : dir.listFiles()) {
                    if (nama.equals(f.getName())) exist = true;
                }

                if (exist) sendTo();
                else createPdf(dir);
            } else {
                createPdf(dir);
            }
             */
        } else {
            if (dir.mkdir()) createPdf(dir);
        }
    }

    private void createPdf(File dir) {
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(842, 595, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        canvas.drawBitmap(bitmap, null, new RectF(0, 0, 842, 595), null);
        document.finishPage(page);

        boolean created = false;
        try {
            document.writeTo(new FileOutputStream(dir + "/" + nama + ".pdf"));
            created = true;
            debug(getClass(), "pdf sukses");

        } catch (IOException e) {
            e.printStackTrace();
        }
        document.close();

        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        if (created) sendTo();
    }

    private void sendTo() {
        try {
            File file = new File(getFilesDir(), "sertifikat/" + nama + ".pdf");
            file.setReadable(true, false);

            Uri uri = FileProvider.getUriForFile(this,
                    "com.zam.sidik_padang.provider",//BuildConfig.APPLICATION_ID + ".provider",
                    file);

            final Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.setType("application/pdf");
            startActivity(Intent.createChooser(intent, "Bgikan Sertifikat ke:"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showBottomSheetDialog() {

    }
}