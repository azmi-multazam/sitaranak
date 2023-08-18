package com.zam.sidik_padang.home.dataternak.insiminator.detailternak;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.zxing.BarcodeFormat;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.dataternak.detailternak.BaseFragment;
import com.zam.sidik_padang.home.dataternak.detailternak.DetailTernakActivity;
import com.zam.sidik_padang.home.dataternak.detailternak.DetailTernakFragment;
import com.zam.sidik_padang.home.dataternak.detailternak.Kondisi;
import com.zam.sidik_padang.home.dataternak.detailternak.UpdateStatusTernakActivity;
import com.zam.sidik_padang.home.dataternak.detailternak.foto.FotoTernak;
import com.zam.sidik_padang.home.dataternak.insiminator.TernakIb;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.HttpUrlConnectionApi;
import com.zam.sidik_padang.util.Util;

/**
 * Created by supriyadi on 4/18/18.
 */

public class DetailTernakIbFragment extends BaseFragment {

    private static final String VOLLEY_TAG = DetailTernakIbFragment.class.getName();
    private static String PREF_JENIS_KONDISI_RESPONSE = "jenis_kondisi_ib_response";
    private TernakIb ternakIb;
    private ImageView imageViewFotoTernak;
    private DetailTernakFragment.OnFotoTernakDiklikListener fotoDiklikListener;
    private FloatingActionsMenu fabKondisi;
    private View progressbarFab;

    public static DetailTernakIbFragment getInstance(TernakIb ternakIb) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("ternak", ternakIb);
        DetailTernakIbFragment fragment = new DetailTernakIbFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (ternakIb != null)
            outState.putSerializable("ternak", ternakIb);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("ternak"))
            ternakIb = (TernakIb) savedInstanceState.getSerializable("ternak");
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DetailTernakFragment.OnFotoTernakDiklikListener)
            fotoDiklikListener = ((DetailTernakFragment.OnFotoTernakDiklikListener) context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("ternak"))
            ternakIb = (TernakIb) bundle.getSerializable("ternak");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_ternak_ib, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageViewFotoTernak = (ImageView) view.findViewById(R.id.fragment_detail_ternak_ImageViewFotoTernak);
        imageViewFotoTernak.setOnClickListener(v -> {
            if (fotoDiklikListener != null) fotoDiklikListener.onFotoTernakDiklikListener();
        });
        progressbarFab = view.findViewById(R.id.fragment_detail_ternak_FABProgressbar);
        fabKondisi = (FloatingActionsMenu) view.findViewById(R.id.fragment_detail_ternak_FAB);
        try {
            ((TextView) view.findViewById(R.id.fragment_detail_ternak_TextViewIdTernak)).setText(URLDecoder.decode(ternakIb.id_ternak, "utf-8"));
            ((TextView) view.findViewById(R.id.fragment_detail_ternak_TextViewNomorIrtek)).setText(URLDecoder.decode(ternakIb.no_irtek, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ((TextView) view.findViewById(R.id.fragment_detail_ternak_TextViewBangsaSapi)).setText(ternakIb.bangsa);
        ((TextView) view.findViewById(R.id.fragment_detail_ternak_TextViewNamaPemilik)).setText(ternakIb.nama);
        ((TextView) view.findViewById(R.id.textViewNoStraw)).setText(ternakIb.no_straw);
        ((TextView) view.findViewById(R.id.textViewTanggalIb)).setText(ternakIb.tanggal_ib);
        ((TextView) view.findViewById(R.id.textViewTanggalPkb)).setText(ternakIb.tanggal_pkb);
        ((TextView) view.findViewById(R.id.textViewKondisiTernak)).setText(ternakIb.kondisi_ternak);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                new LoadBarcode((ImageView) view.findViewById(R.id.fragment_detail_ternak_ImageViewBarcode));
            }
        }, 30);

        loadFotoFromServer();
        loadJenisKondisi();

    }

    private void loadDetailTernak(final View rootView) {
        if (!Util.isInternetAvailible(getActivity())) {
            Toast.makeText(getActivity(), "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = Config.URL_TAMBAH_IB + "?aksi=5&userid=" + user.userid + "&id_ternak=" + ternakIb.id_ternak;
        rootView.findViewById(R.id.fragment_detail_ternak_Progresbar).setVisibility(View.VISIBLE);
        debug(getClass(), "Load detail ternak url=" + url);
        HttpUrlConnectionApi.get(url, new HttpUrlConnectionApi.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                loadJenisKondisi();
                debug(getClass(), "Load detail ternak onresponse " + jsonObject);
                rootView.findViewById(R.id.fragment_detail_ternak_Progresbar).setVisibility(View.INVISIBLE);
                if (jsonObject.get("success").getAsBoolean()) {
                    JsonElement arrayElemen = jsonObject.get("keterangan");
                    if (!arrayElemen.isJsonNull() && arrayElemen.isJsonArray()) {
                        JsonArray ja = arrayElemen.getAsJsonArray();
                        if (ja.size() > 0) {
                            try {
                                JsonObject object = ja.get(0).getAsJsonObject();
                                ((TextView) rootView.findViewById(R.id.fragment_detail_ternak_TextViewIdTernak)).setText(object.get("id_ternak").getAsString());
                                String oriId = object.get("no_irtek").getAsString();
                                Log.e(DetailTernakIbFragment.class.getSimpleName(), "noEartek ori:" + oriId);
                                String decodedId = URLDecoder.decode(oriId, "utf-8");
                                Log.e(DetailTernakIbFragment.class.getSimpleName(), "noEartek decoded:" + decodedId);
                                ((TextView) rootView.findViewById(R.id.fragment_detail_ternak_TextViewNomorIrtek)).setText(decodedId);

                                JsonElement bangsa = object.get("bangsa");
                                if (bangsa != null && !bangsa.isJsonNull())
                                    ((TextView) rootView.findViewById(R.id.fragment_detail_ternak_TextViewBangsaSapi)).setText(bangsa.getAsString());
                                ((TextView) rootView.findViewById(R.id.fragment_detail_ternak_TextViewJenisKelamin)).setText(object.get("kelamin").getAsString());
                                JsonElement metodePerkawinanelement = object.get("hasil_perkawinan");
                                ((TextView) rootView.findViewById(R.id.fragment_detail_ternak_TextViewMetodePerkawinan)).setText(metodePerkawinanelement.isJsonNull() ? "-" : metodePerkawinanelement.getAsString());
                                JsonElement kelahiranElement = object.get("tipe_kelahiran");
                                ((TextView) rootView.findViewById(R.id.fragment_detail_ternak_TextViewTipeKelahiran)).setText(kelahiranElement.isJsonNull() ? "-" : kelahiranElement.getAsString());
                                TextView textViewInduk = (TextView) rootView.findViewById(R.id.fragment_detail_ternak_TextViewInduk);
                                textViewInduk.setOnClickListener(v -> {
                                    Intent intent = getActivity().getIntent();
                                    intent.putExtra(DetailTernakActivity.ID_TERNAK, ((TextView) v).getText().toString().trim());
                                    startActivity(intent);
                                });
                                textViewInduk.setText(object.get("induk").getAsString());
                                TextView textViewBapak = (TextView) rootView.findViewById(R.id.fragment_detail_ternak_TextViewBapak);
                                textViewBapak.setOnClickListener(v -> {
                                    Intent intent = getActivity().getIntent();
                                    intent.putExtra(DetailTernakActivity.ID_TERNAK, ((TextView) v).getText().toString().trim());
                                    startActivity(intent);
                                });
                                textViewBapak.setText(object.get("bapak").getAsString());
                                ((TextView) rootView.findViewById(R.id.fragment_detail_ternak_TextViewNamaPemilik)).setText(object.get("nama_pemilik").getAsString());
                                ((TextView) rootView.findViewById(R.id.fragment_detail_ternak_TextVieAlamat)).setText(object.get("alamat").getAsString() + ", " + object.get("provinsi").getAsString());
                            } catch (Exception e) {
                                Toast.makeText(getActivity(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                                Log.e(DetailTernakIbFragment.this.getClass().getName(), e.getMessage());
                            }
                        }
                    }
                } else
                    Toast.makeText(getActivity(), jsonObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
//				loadJenisKondisi();
            }
        });
    }

    private void loadJenisKondisi() {
        if (!Util.isInternetAvailible(getActivity())) return;
        String url = Config.URL_LIHAT_DATA_TERNAK + "?aksi=14&userid=" + user.userid;
        progressbarFab.setVisibility(View.VISIBLE);
        HttpUrlConnectionApi.get(url, new HttpUrlConnectionApi.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                progressbarFab.setVisibility(View.INVISIBLE);
                debug(getClass(), "Load daftar kondisi response=" + jsonObject);
                if (jsonObject.get("success").getAsBoolean()) {
                    pref.edit().putString(PREF_JENIS_KONDISI_RESPONSE, jsonObject.toString()).apply();
                    bindTombolKondisi(jsonObject);
                }
            }
        });
    }

    private void bindTombolKondisi(JsonObject jsonObject) {
        JsonElement arrayElement = jsonObject.get("kondisi_status");
        if (!arrayElement.isJsonArray()) return;
        debug(getClass(), "bind fab. fab childs=" + fabKondisi.getChildCount());

        FloatingActionButton fab;
        Gson gson = new Gson();
        int colorPressed = ContextCompat.getColor(getActivity(), R.color.colorPrimary);
        for (JsonElement element : arrayElement.getAsJsonArray()) {
            final Kondisi kondisi = gson.fromJson(element, Kondisi.class);
            fab = new FloatingActionButton(getActivity());
            fab.setColorNormal(Color.parseColor(kondisi.warna));
            fab.setColorPressed(colorPressed);
            fab.setTitle(kondisi.nama_kondisi);
            fab.setSize(FloatingActionButton.SIZE_MINI);
            fab.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), UpdateStatusTernakActivity.class);
                intent.putExtra(UpdateStatusTernakActivity.EXTRA_KONDISI, kondisi);
                intent.putExtra(DetailTernakActivity.ID_TERNAK, ternakIb.id_ternak);
                startActivityForResult(intent, 25);
                fabKondisi.collapse();
            });
            int id = Integer.parseInt(kondisi.id);
            fab.setId(id);
            if (fabKondisi.findViewById(id) == null) fabKondisi.addButton(fab);
        }
        fabKondisi.setVisibility(View.VISIBLE);
    }
    /*
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_detail_ternak_TextViewBapak:
            case R.id.fragment_detail_ternak_TextViewInduk:
                Intent intent = getActivity().getIntent();
                intent.putExtra(DetailTernakActivity.ID_TERNAK, ((TextView) v).getText().toString().trim());
                startActivity(intent);
                break;
            case R.id.fragment_detail_ternak_ImageViewFotoTernak:
                if (fotoDiklikListener != null) fotoDiklikListener.onFotoTernakDiklikListener();
                break;
            default:
                break;
        }
    }
     */

    private void loadFotoFromServer() {
        if (!Util.isInternetAvailible(getActivity()) || ternakIb.id_ternak.isEmpty()) return;
        String url = Config.URL_GAMBAR_SAPI + "?aksi=1&userid=" + user.userid;
        url += "&id_ternak=" + ternakIb.id_ternak;
        debug(getClass(), "Load foto ternak url=" + url);
        HttpUrlConnectionApi.get(url, new HttpUrlConnectionApi.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                debug(getClass(), "Load fotoresponse=" + jsonObject);
                if (jsonObject.get("success").getAsBoolean()) {
                    JsonElement je = jsonObject.get("lihat_gambar_ternak");
                    if (je != null) {
                        JsonArray ja = je.getAsJsonArray();
                        if (!isDetached()) bindFotoTernak(ja);
                        pref.edit().putString(Config.PREF_FOTO_TERNAK_ALL_TERSIMPAN + ternakIb.id_ternak, ja.toString()).apply();
                    }
                }
            }
        });
    }

    private void bindFotoTernak(JsonArray jsonArray) {
        if (jsonArray == null || jsonArray.size() < 1) return;
        FotoTernak ft = new Gson().fromJson(jsonArray.get(0), FotoTernak.class);
        if (!isDetached())
            Glide.with(this.getContext()).load(ft.gambar).placeholder(R.mipmap.ic_launcher).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageViewFotoTernak);
    }

    @Override
    public void onDetach() {
        cancelRequest(VOLLEY_TAG);
        super.onDetach();
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
                return Util.encodeAsBitmap(ternakIb.id_ternak, BarcodeFormat.QR_CODE, viewWidth, viewHeight);
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

