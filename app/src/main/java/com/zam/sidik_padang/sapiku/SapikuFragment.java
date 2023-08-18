package com.zam.sidik_padang.sapiku;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.dataternak.DataTernakActivity;
import com.zam.sidik_padang.home.dataternak.detailternak.BaseFragment;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleyStringRequest;

public class SapikuFragment extends BaseFragment implements View.OnClickListener {

    public static final String EXTRA_JENIS_TERNAK = "extra_jenis_ternak";
    private static final String VOLLEY_TAG = SapikuFragment.class.getName();
    private View progressbar;
    private View layoutDewasa, layoutMuda, layoutSapih, layoutPedet;
    private TextView textTotalSapi;
    private Gson gson;
    private JumlahTernak jumlahTernak;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        gson = new Gson();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_setting, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_setting) {
            startActivityForResult(new Intent(getActivity(), PengaturanSapikuActivity.class), 31);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 31) {
                loadFromServer();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sapiku_new, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textTotalSapi = (TextView) view.findViewById(R.id.fragment_sapiku_TextViewTotalSapi);
        layoutDewasa = view.findViewById(R.id.fragment_sapiku_LayoutDewasa);
        layoutMuda = view.findViewById(R.id.fragment_sapiku_LayoutMuda);
        ((TextView) layoutMuda.findViewById(R.id.sapiku_TextViewTitle)).setText("Muda");
        layoutSapih = view.findViewById(R.id.fragment_sapiku_LayoutLepasSapih);
        ((TextView) layoutSapih.findViewById(R.id.sapiku_TextViewTitle)).setText("Lepas Sapih");
        layoutPedet = view.findViewById(R.id.fragment_sapiku_LayoutPedet);
        ((TextView) layoutPedet.findViewById(R.id.sapiku_TextViewTitle)).setText("Pedet");
        progressbar = view.findViewById(R.id.fragment_sapiku_Progressbar);
        String tersimpan = pref.getString(Config.PREF_SAPIKU_TERSIMPAN + user.userid, "");
        if (!tersimpan.isEmpty()) {
            JsonObject jo = gson.fromJson(tersimpan, JsonElement.class).getAsJsonObject();
            bindView(jo);
        }
        setClickListener();
        view.postDelayed(this::loadFromServer, 500);
    }

    private void loadFromServer() {
        if (!Util.isInternetAvailible(getActivity())) {
            progressbar.setVisibility(View.INVISIBLE);
            Toast.makeText(getActivity(), "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = Config.URL_JUMLAH_TERNAK + "?aksi=1&userid=" + user.userid;
        debug(getClass(), "Load jumlah ternak url=" + url);
        progressbar.setVisibility(View.VISIBLE);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                debug(getClass(), "Load jumlah ternak.Response=" + jsonObject);
                progressbar.setVisibility(View.INVISIBLE);
                if (jsonObject.get("success").getAsBoolean()) {
                    JsonElement je = jsonObject.get("jumlah_ternak");
                    if (je != null) {
                        pref.edit().putString(Config.PREF_SAPIKU_TERSIMPAN + user.userid, je.getAsJsonObject().toString()).apply();
                        bindView(je.getAsJsonObject());
                    } else
                        Toast.makeText(getActivity(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getActivity(), jsonObject.get("messsage").getAsString(), Toast.LENGTH_SHORT).show();
            }
        });
        addRequest(request, VOLLEY_TAG);
    }

    private void bindView(JsonObject jo) {
        jumlahTernak = gson.fromJson(jo, JumlahTernak.class);

        textTotalSapi.setText(String.valueOf(jumlahTernak.total_semua));
        ((TextView) layoutDewasa.findViewById(R.id.sapiku_TextViewJumlah1)).setText(String.valueOf(jumlahTernak.jantan_dewasa));
        ((TextView) layoutDewasa.findViewById(R.id.sapiku_TextViewJumlah2)).setText(String.valueOf(jumlahTernak.betina_dewasa));

        ((TextView) layoutMuda.findViewById(R.id.sapiku_TextViewJudul1)).setText("Jantan Muda");
        ((TextView) layoutMuda.findViewById(R.id.sapiku_TextViewJumlah1)).setText(String.valueOf(jumlahTernak.jantan_muda));
        ((TextView) layoutMuda.findViewById(R.id.sapiku_TextViewJudul2)).setText("Betina Dara");
        ((TextView) layoutMuda.findViewById(R.id.sapiku_TextViewJumlah2)).setText(String.valueOf(jumlahTernak.betina_muda));

        ((TextView) layoutSapih.findViewById(R.id.sapiku_TextViewJudul1)).setText("Jantan Sapih");
        ((TextView) layoutSapih.findViewById(R.id.sapiku_TextViewJumlah1)).setText(String.valueOf(jumlahTernak.jantan_sapih));
        ((TextView) layoutSapih.findViewById(R.id.sapiku_TextViewJudul2)).setText("Betina Sapih");
        ((TextView) layoutSapih.findViewById(R.id.sapiku_TextViewJumlah2)).setText(String.valueOf(jumlahTernak.betina_sapih));

        ((TextView) layoutPedet.findViewById(R.id.sapiku_TextViewJudul1)).setText("Jantan Pedet");
        ((TextView) layoutPedet.findViewById(R.id.sapiku_TextViewJumlah1)).setText(String.valueOf(jumlahTernak.jantan_pedet));
        ((TextView) layoutPedet.findViewById(R.id.sapiku_TextViewJudul2)).setText("Betina Pedet");
        ((TextView) layoutPedet.findViewById(R.id.sapiku_TextViewJumlah2)).setText(String.valueOf(jumlahTernak.betina_pedet));

    }

    private void setClickListener() {
        ((ViewGroup) layoutDewasa)
                .findViewById(R.id.layout_left)
                //.getChildAt(1)
                .setOnClickListener(v -> {
                    String s = "&awal=" + jumlahTernak.dtfromjd + "&akhir=" + jumlahTernak.dttojd + "&kelamin=" + jumlahTernak.kelamin_jantan_dewasa + "&jenis=jantan_dewasa";
                    loadDaftarTernak(s, "Jantan Dewasa");
                });
        ((ViewGroup) layoutDewasa)
                .findViewById(R.id.layout_right)
                //.getChildAt(2)
                .setOnClickListener(v -> {
                    String s = "&awal=" + jumlahTernak.dtfrombd + "&akhir=" + jumlahTernak.dttobd + "&kelamin=" + jumlahTernak.kelamin_betina_dewasa + "&jenis=betina_dewasa";
                    loadDaftarTernak(s, "Induk Dewasa");
                });
        ((ViewGroup) layoutMuda)
                .findViewById(R.id.layout_left)
                //.getChildAt(1)
                .setOnClickListener(v -> {
                    String s = "&awal=" + jumlahTernak.dtfromjm + "&akhir=" + jumlahTernak.dttojm + "&kelamin=" + jumlahTernak.kelamin_jantan_muda + "&jenis=jantan_muda";
                    loadDaftarTernak(s, "Jantan Muda");
                });
        ((ViewGroup) layoutMuda)
                .findViewById(R.id.layout_right)
                //.getChildAt(2)
                .setOnClickListener(v -> {
                    String s = "&awal=" + jumlahTernak.dtfrombm + "&akhir=" + jumlahTernak.dttobm + "&kelamin=" + jumlahTernak.kelamin_betina_muda + "&jenis=betina_muda";
                    loadDaftarTernak(s, "Betina Muda");
                });

        ((ViewGroup) layoutSapih)
                .findViewById(R.id.layout_left)
                //.getChildAt(1)
                .setOnClickListener(v -> {
                    String s = "&awal=" + jumlahTernak.dtfromjs + "&akhir=" + jumlahTernak.dttojs + "&kelamin=" + jumlahTernak.kelamin_jantan_sapih + "&jenis=jantan_sapih";
                    loadDaftarTernak(s, "Jantan Sapih");
                });
        ((ViewGroup) layoutSapih)
                .findViewById(R.id.layout_right)
                //.getChildAt(2)
                .setOnClickListener(v -> {
                    String s = "&awal=" + jumlahTernak.dtfrombs + "&akhir=" + jumlahTernak.dttobs + "&kelamin=" + jumlahTernak.kelamin_betina_sapih + "&jenis=betina_sapih";
                    loadDaftarTernak(s, "Betina Sapih");
                });

        ((ViewGroup) layoutPedet)
                .findViewById(R.id.layout_left)
                //.getChildAt(1)
                .setOnClickListener(v -> {
                    String s = "&awal=" + jumlahTernak.dtfromjp + "&akhir=" + jumlahTernak.dttojp + "&kelamin=" + jumlahTernak.kelamin_jantan_pedet + "&jenis=jantan_pedet";
                    loadDaftarTernak(s, "Jantan Pedet");
                });
        ((ViewGroup) layoutPedet)
                .findViewById(R.id.layout_right)
                //.getChildAt(2)
                .setOnClickListener(v -> {
                    String s = "&awal=" + jumlahTernak.dtfrombp + "&akhir=" + jumlahTernak.dttobp + "&kelamin=" + jumlahTernak.kelamin_betina_pedet + "&jenis=betina_pedet";
                    loadDaftarTernak(s, "Betina Pedet");
                });
    }

    private void loadDaftarTernak(String suffixParam, final String jenis) {
        if (jumlahTernak == null) {
            Toast.makeText(getActivity(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(getActivity(), DataTernakActivity.class);
        intent.putExtra(DataTernakActivity.EXTRA_SEARCH_URL, Config.URL_JUMLAH_TERNAK + "?aksi=2&userid=" + user.userid + suffixParam);
        intent.putExtra(EXTRA_JENIS_TERNAK, jenis);
        startActivity(intent);

    }

    @Override
    public void onDetach() {
        cancelRequest(VOLLEY_TAG);
        super.onDetach();
    }

    @Override
    public void onClick(View v) {

    }

    private class JumlahTernak {
        int jantan_dewasa = 0, kelamin_jantan_dewasa = 0;
        String dtfromjd, dttojd;

        int betina_dewasa = 0, kelamin_betina_dewasa = 0;
        String dtfrombd, dttobd;

        int jantan_muda = 0, kelamin_jantan_muda = 0;
        String dtfromjm, dttojm;

        int betina_muda = 0, kelamin_betina_muda = 0;
        String dtfrombm, dttobm;

        int jantan_sapih = 0, kelamin_jantan_sapih = 0;
        String dtfromjs, dttojs;

        int betina_sapih = 0, kelamin_betina_sapih = 0;
        String dtfrombs, dttobs;

        int jantan_pedet = 0, kelamin_jantan_pedet = 0;
        String dtfromjp, dttojp;

        int betina_pedet = 0, kelamin_betina_pedet = 0;
        String dtfrombp, dttobp;

        int total_semua = 0;
    }
}



/*Response={"jumlah_ternak":
				{"jantan_dewasa":0,"dtfromjd":"2003-10-06","dttojd":"2015-10-06",
				 "kelamin_jantan_dewasa":1,

				 "betina_dewasa":0, "dtfrombd":"2003-10-06","dttobd":"2015-10-06",
				 "kelamin_betina_dewasa":2,

				 "jantan_muda":0,"dtfromjm":"2015-10-06","dttojm":"2016-10-06",
				 "kelamin_jantan_muda":1,

				 "betina_muda":1, "dtfrombm":"2015-10-06","dttobm":"2016-10-06",
				 "kelamin_betina_muda":2,

				 "jantan_sapih":0, "dtfromjs":"2016-10-06","dttojs":"2017-04-06",
				 "kelamin_jantan_sapih":1,

				 "betina_sapih":0,"dtfrombs":"2016-10-06","dttobs":"2017-04-06",
				 "kelamin_betina_sapih":2,

				 "jantan_pedet":0,"dtfromjp":"2017-04-06","dttojp":"2017-10-06",
				 "kelamin_jantan_pedet":1,

				 "betina_pedet":0,"dtfrombp":"2017-04-06","dttobp":"2017-10-06",
				 "kelamin_betina_pedet":2,

				 "total_semua":1

				 },"success":true,"message":" berhasil"}*/
