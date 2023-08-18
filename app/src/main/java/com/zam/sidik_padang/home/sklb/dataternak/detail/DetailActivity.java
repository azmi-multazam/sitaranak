package com.zam.sidik_padang.home.sklb.dataternak.detail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.zxing.BarcodeFormat;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.dataternak.detailternak.PrintTernakIdCardActivity;
import com.zam.sidik_padang.util.Util;

public class DetailActivity extends AppCompatActivity {

    private static final String VOLLEY_TAG = DetailActivity.class.getName();
    private static String PREF_JENIS_KONDISI_RESPONSE = "jenis_kondisi_response";
    private String idTernak = "";
    private ImageView imageViewFotoTernak;
    private OnFotoTernakDiklikListener fotoDiklikListener;
    private FloatingActionsMenu fabKondisi;
    private View progressbarFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sklb_detail_ternak);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_detail_ternak, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_print) {
            Intent it = new Intent(this, PrintTernakIdCardActivity.class);
            it.putExtra("idTernak", idTernak);
            startActivity(it);
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    public static interface OnFotoTernakDiklikListener {
        void onFotoTernakDiklikListener();
    }

    private class LoadBarcode extends AsyncTask<Void, Void, Bitmap> {

        private ImageView imageView;
        private int viewWidth, viewHeight;

        LoadBarcode(ImageView imageView) {
            this.imageView = imageView;
            execute();
        }

        @Override
        protected void onPreExecute() {
            viewWidth = imageView.getWidth();
            viewHeight = imageView.getHeight();
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                return Util.encodeAsBitmap(idTernak, BarcodeFormat.QR_CODE, viewWidth, viewHeight);
            } catch (Exception e) {
                Log.e(getClass().getName(), e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) imageView.setImageBitmap(bitmap);
        }
    }

}