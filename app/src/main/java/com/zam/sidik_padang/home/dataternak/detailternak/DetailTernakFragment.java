package com.zam.sidik_padang.home.dataternak.detailternak;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.api.State;
import com.zam.sidik_padang.databinding.FragmentDetailTernakBinding;
import com.zam.sidik_padang.home.dataternak.detailternak.foto.FotoTernak;
import com.zam.sidik_padang.home.dataternak.vm.detail.DetailTernakResponse;
import com.zam.sidik_padang.home.dataternak.vm.detail.Keterangan;
import com.zam.sidik_padang.home.dataternak.vm.TernakViewModel;
import com.zam.sidik_padang.home.dataternak.vm.foto.FotoTernakResponse;
import com.zam.sidik_padang.home.dataternak.vm.foto.LihatGambarTernak;
import com.zam.sidik_padang.home.dataternak.vm.kondisi.KondisiResponse;
import com.zam.sidik_padang.home.dataternak.vm.kondisi.KondisiStatus;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.HttpUrlConnectionApi;
import com.zam.sidik_padang.util.Util;
import io.paperdb.Paper;

/**
 * Created by supriyadi on 9/10/17.
 */

public class DetailTernakFragment extends BaseFragment {

    private FragmentDetailTernakBinding binding;
    private static final String VOLLEY_TAG = DetailTernakFragment.class.getName();
    private static String PREF_JENIS_KONDISI_RESPONSE = "jenis_kondisi_response";
    private String idTernak = "", from = "";
    private ImageView imageViewFotoTernak;
    private OnFotoTernakDiklikListener fotoDiklikListener;
    private FloatingActionsMenu fabKondisi;
    private View progressbar, progressbarFab;
    private boolean isSklb;
    private TernakViewModel viewModel;

    public static DetailTernakFragment getInstance(String idTernak, String from) {
        Bundle bundle = new Bundle();
        bundle.putString(DetailTernakActivity.ID_TERNAK, idTernak);
        bundle.putString("from", from);
        DetailTernakFragment fragment = new DetailTernakFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (!idTernak.isEmpty()) outState.putString(DetailTernakActivity.ID_TERNAK, idTernak);
        if (!from.isEmpty()) outState.putString("from", from);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(DetailTernakActivity.ID_TERNAK))
            idTernak = savedInstanceState.getString(DetailTernakActivity.ID_TERNAK);

        if (savedInstanceState != null && savedInstanceState.containsKey("from"))
            from = savedInstanceState.getString("from");
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFotoTernakDiklikListener)
            fotoDiklikListener = ((OnFotoTernakDiklikListener) context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(DetailTernakActivity.ID_TERNAK))
            idTernak = bundle.getString(DetailTernakActivity.ID_TERNAK);

        if (bundle != null && bundle.containsKey("from"))
            from = bundle.getString("from");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetailTernakBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(TernakViewModel.class);
        progressbar = binding.fragmentDetailTernakProgresbar;
        imageViewFotoTernak = binding.fragmentDetailTernakImageViewFotoTernak;
        imageViewFotoTernak.setOnClickListener(view1 -> {
            if (fotoDiklikListener != null) fotoDiklikListener.onFotoTernakDiklikListener();
        });
        progressbarFab = binding.fragmentDetailTernakFABProgressbar;
        fabKondisi = binding.fragmentDetailTernakFAB;
        isSklb = from.equals("sklb");

        binding.fabParent.setVisibility(isSklb ? View.GONE : View.VISIBLE);

        String tersimpan = pref.getString(Config.PREF_FOTO_TERNAK_ALL_TERSIMPAN + idTernak, "");
        if (!tersimpan.isEmpty()) bindFotoTernak(new Gson().fromJson(tersimpan, JsonElement.class).getAsJsonArray());

        //loadDetailTernak(view);
        loadFotoFromServer();
        view.postDelayed(() -> binding.fragmentDetailTernakImageViewBarcode.setImageBitmap(generateQr(idTernak)), 300);

        if (!Util.isInternetAvailible(getActivity())) {
            alert("Tidak ada koneksi internet");
        }

        observe();
    }

    private void observe() {
        viewModel.getResponseDetailTernak().observe(getViewLifecycleOwner(), detail -> {
            if (detail != null) {
                hideLoading();
                if (detail.state == State.LOADING) {
                    showLoading();
                } else if (detail.state == State.SUCCESS) {
                    if (detail.data == null) {
                        alert("data kosong");
                    } else {
                        bindView(detail.data);
                    }
                } else {
                    alert(detail.message);
                }
            }
        });

        viewModel.getResponseFotoTernak().observe(getViewLifecycleOwner(), foto -> {
            if (foto != null) {
                if (foto.state == State.LOADING) {
                    //showLoading();
                } else if (foto.state == State.SUCCESS) {
                    if (foto.data == null) {
                        alert("data kosong");
                    } else {
                        bindFoto(foto.data);
                    }
                } else {
                    alert(foto.message);
                }
            }
        });

        viewModel.getResponseKondisiTernak().observe(getViewLifecycleOwner(), kondisi -> {
            if (kondisi != null) {
                hideLoadingKondisi();
                if (kondisi.state == State.LOADING) {
                    showLoadingKondisi();
                } else if (kondisi.state == State.SUCCESS) {
                    if (kondisi.data == null) {
                        alert("data kosong");
                    } else {
                        bindKondisi(kondisi.data);
                    }
                } else {
                    alert(kondisi.message);
                }
            }
        });
    }

    private void bindView(DetailTernakResponse data) {
        if (data.getKeterangan() != null && data.getKeterangan().size() > 0) {
            Keterangan ket = data.getKeterangan().get(0);
            binding.fragmentDetailTernakTextViewIdTernak.setText(ket.getIdTernak());

            try {
                binding.fragmentDetailTernakTextViewNomorIrtek.setText(URLDecoder.decode(ket.getNoIrtek(), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            binding.fragmentDetailTernakTextViewBangsaSapi.setText(ket.getBangsa());
            binding.fragmentDetailTernakTextViewJenisKelamin.setText(ket.getKelamin());
            binding.fragmentDetailTernakTextViewMetodePerkawinan.setText(handleNull(ket.getHasilPerkawinan()));
            binding.fragmentDetailTernakTextViewTipeKelahiran.setText(handleNull(ket.getTipeKelahiran()));
            TextView textViewInduk = binding.fragmentDetailTernakTextViewInduk;
            textViewInduk.setOnClickListener(v -> {
                Intent intent = getActivity().getIntent();
                intent.putExtra(DetailTernakActivity.ID_TERNAK, ((TextView) v).getText().toString().trim());
                startActivity(intent);
            });
            textViewInduk.setText(ket.getInduk());
            TextView textViewBapak = binding.fragmentDetailTernakTextViewBapak;
            textViewBapak.setOnClickListener(v1 -> {
                Intent intent = getActivity().getIntent();
                intent.putExtra(DetailTernakActivity.ID_TERNAK, ((TextView) v1).getText().toString().trim());
                startActivity(intent);
            });
            textViewBapak.setText(ket.getBapak());
            binding.fragmentDetailTernakTextViewNamaPemilik.setText(ket.getNamaPemilik());
            String alamat = ket.getAlamat() + ", " + ket.getProvinsi();
            binding.fragmentDetailTernakTextVieAlamat.setText(alamat);
        }
        if (!isSklb) {
            String url = Config.URL_LIHAT_DATA_TERNAK + "?aksi=14&userid=" + user.userid;
            viewModel.getKondisiTernak(url);
        }
    }

    private String handleNull(String value) {
        if (value==null || value.equals("")) return "-";
        else return value;
    }

    private void loadDetailTernak(final View rootView) {
        if (!Util.isInternetAvailible(getActivity())) {
            Toast.makeText(getActivity(), "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
            return;
        }
        String base;
        if (isSklb) {
            base = Config.URL_LIHAT_DATA_TERNAK_SKLB;
        } else {
            base = Config.URL_LIHAT_DATA_TERNAK;
        }
        String url = base + "?aksi=5&userid=" + user.userid + "&id_ternak=" + idTernak;
        rootView.findViewById(R.id.fragment_detail_ternak_Progresbar).setVisibility(View.VISIBLE);
        debug(getClass(), "Load detail ternak url=" + url);
        HttpUrlConnectionApi.get(url, new HttpUrlConnectionApi.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                if (!isSklb) loadJenisKondisi();
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
                                ((TextView) rootView.findViewById(R.id.fragment_detail_ternak_TextViewNomorIrtek)).setText(URLDecoder.decode(object.get("no_irtek").getAsString(), "utf-8"));
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
                                e.printStackTrace();
                                Toast.makeText(getActivity(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                                Log.e(DetailTernakFragment.this.getClass().getName(), e.getMessage());
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
                    if (!isDetached()) bindTombolKondisi(jsonObject);
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
        final Activity activity = getActivity();
        if (activity == null) return;
        int colorPressed = ContextCompat.getColor(activity, R.color.colorPrimary);
        for (JsonElement element : arrayElement.getAsJsonArray()) {
            final Kondisi kondisi = gson.fromJson(element, Kondisi.class);
            fab = new FloatingActionButton(getActivity());
            fab.setColorNormal(Color.parseColor(kondisi.warna));
            fab.setColorPressed(colorPressed);
            fab.setTitle(kondisi.nama_kondisi);
            fab.setSize(FloatingActionButton.SIZE_MINI);
            fab.setOnClickListener(v -> {
                Intent intent = new Intent(activity, UpdateStatusTernakActivity.class);
                intent.putExtra(UpdateStatusTernakActivity.EXTRA_KONDISI, kondisi);
                intent.putExtra(DetailTernakActivity.ID_TERNAK, idTernak);
                startActivityForResult(intent, 25);
                fabKondisi.collapse();
            });
            int id = Integer.parseInt(kondisi.id);
            fab.setId(id);
            if (fabKondisi.findViewById(id) == null) fabKondisi.addButton(fab);
        }
        fabKondisi.setVisibility(View.VISIBLE);
    }

    private void bindKondisi(KondisiResponse data) {
        Paper.book().write(PREF_JENIS_KONDISI_RESPONSE, data);

        FloatingActionButton fab;
        Gson gson = new Gson();
        final Activity activity = getActivity();
        if (activity == null) return;
        int colorPressed = ContextCompat.getColor(activity, R.color.colorPrimary);

        if (data.getKondisiStatus() !=null && data.getKondisiStatus().size() > 0) {
            List<KondisiStatus> kondisiList = data.getKondisiStatus();
            for (KondisiStatus kondisi : kondisiList) {
                fab = new FloatingActionButton(getActivity());
                fab.setColorNormal(Color.parseColor(kondisi.getWarna()));
                fab.setColorPressed(colorPressed);
                fab.setTitle(kondisi.getNamaKondisi());
                fab.setSize(FloatingActionButton.SIZE_MINI);
                fab.setOnClickListener(v -> {
                    Intent intent = new Intent(activity, UpdateStatusTernakActivity.class);
                    intent.putExtra(UpdateStatusTernakActivity.GSON_KONDISI, gson.toJson(kondisi));
                    intent.putExtra(DetailTernakActivity.ID_TERNAK, idTernak);
                    startActivityForResult(intent, 25);
                    fabKondisi.collapse();
                });
                int id = Integer.parseInt(kondisi.getId());
                fab.setId(id);
                if (fabKondisi.findViewById(id) == null) fabKondisi.addButton(fab);
            }
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
        if (!Util.isInternetAvailible(getActivity()) || idTernak.isEmpty()) return;
        String url = Config.URL_GAMBAR_SAPI + "?aksi=1&userid=" + user.userid;
        url += "&id_ternak=" + idTernak;
        debug(getClass(), "Load foto ternak url=" + url);
        HttpUrlConnectionApi.get(url, new HttpUrlConnectionApi.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                debug(getClass(), "Load fotoresponse=" + jsonObject);
                if (jsonObject.get("success").getAsBoolean()) {
                    JsonElement je = jsonObject.get("lihat_gambar_ternak");
                    if (je != null) {
                        JsonArray ja = je.getAsJsonArray();
                        bindFotoTernak(ja);
                        pref.edit().putString(Config.PREF_FOTO_TERNAK_ALL_TERSIMPAN + idTernak, ja.toString()).apply();
                    }
                }
            }
        });
    }

    private void bindFotoTernak(JsonArray jsonArray) {
        if (jsonArray == null || jsonArray.size() < 1) return;
        FotoTernak ft = new Gson().fromJson(jsonArray.get(0), FotoTernak.class);
        if (!isDetached() && isResumed())
            Glide.with(getContext()).load(ft.gambar).placeholder(R.mipmap.ic_launcher).into(imageViewFotoTernak);
    }

    private void bindFoto(FotoTernakResponse data) {
        if (data.getLihatGambarTernak() != null && data.getLihatGambarTernak().size() > 0) {
            LihatGambarTernak foto = data.getLihatGambarTernak().get(0);
            if (!isDetached() && isResumed())
                Glide.with(getContext())
                        .load(foto.getGambar())
                        .placeholder(R.mipmap.ic_launcher)
                        .into(imageViewFotoTernak);
        }
    }

    @Override
    public void onDetach() {
        cancelRequest(VOLLEY_TAG);
        super.onDetach();
    }

    public interface OnFotoTernakDiklikListener {
        void onFotoTernakDiklikListener();
    }

    private Bitmap generateQr(String qr) {
        QRCodeWriter writer = new QRCodeWriter();
        Bitmap bmp = null;
        try {
            BitMatrix bitMatrix = writer.encode(qr, BarcodeFormat.QR_CODE, 400, 400);
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

    private void showLoadingKondisi() {
        progressbarFab.setVisibility(View.VISIBLE);
    }

    private void hideLoadingKondisi() {
        progressbarFab.setVisibility(View.INVISIBLE);
    }

    private void alert(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void showLoading() {
        progressbar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        progressbar.setVisibility(View.INVISIBLE);
    }

}
