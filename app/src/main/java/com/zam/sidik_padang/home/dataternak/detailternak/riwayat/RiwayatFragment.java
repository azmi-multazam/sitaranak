package com.zam.sidik_padang.home.dataternak.detailternak.riwayat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.dataternak.detailternak.BaseFragment;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.HttpUrlConnectionApi;
import com.zam.sidik_padang.util.Util;

import static com.zam.sidik_padang.home.dataternak.detailternak.DetailTernakActivity.ID_TERNAK;

/**
 * Created by supriyadi on 11/1/17.
 */

public class RiwayatFragment extends BaseFragment {

    private static final String PREF_LAST_RIWAYAT = "last_riwayat_";
    private List<Riwayat> list;
    private RiwayatListAdapter adapter;
    private String lastRiawayat = "", from = "";
    private Gson gson;
    private View progressbar;
    private String idTernak;
    private boolean isSklb;

    public RiwayatFragment() {

    }

    public static RiwayatFragment newInstance(String idTernak, String from) {
        RiwayatFragment f = new RiwayatFragment();
        Bundle b = new Bundle();
        b.putString("from", from);
        b.putString(ID_TERNAK, idTernak);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (idTernak != null) outState.putString(ID_TERNAK, idTernak);
        if (!from.isEmpty()) outState.putString("from", from);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(ID_TERNAK))
            idTernak = savedInstanceState.getString(ID_TERNAK);
        if (savedInstanceState != null && savedInstanceState.containsKey("from"))
            from = savedInstanceState.getString("from");
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if (b != null && b.containsKey("from"))
            from = b.getString("from");
        if (b != null && b.containsKey(ID_TERNAK))
            idTernak = b.getString(ID_TERNAK);
        else if (savedInstanceState != null && savedInstanceState.containsKey(ID_TERNAK))
            idTernak = savedInstanceState.getString(ID_TERNAK);
        gson = new Gson();
        list = new ArrayList<>();
        adapter = new RiwayatListAdapter(list);
        lastRiawayat = pref.getString(PREF_LAST_RIWAYAT + user.userid, "");
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recording_ternak, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isSklb = from.equals("sklb");
        view.findViewById(R.id.fragment_data_recording_ternak_FAB).setVisibility(View.GONE);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.fragment_data_recording_ternak_RecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(adapter);
        progressbar = view.findViewById(R.id.fragment_data_recording_ternak_Progresbar);
        JsonElement je = gson.fromJson(lastRiawayat, JsonElement.class);
        if (je != null && je.isJsonObject()) bindResponse(je.getAsJsonObject());
        loadFromServer();
    }

    private void bindResponse(JsonObject jsonObject) {
        JsonArray array = jsonObject.get("riwayat").getAsJsonArray();
        list.clear();
        for (JsonElement e : array) list.add(gson.fromJson(e, Riwayat.class));
        adapter.notifyDataSetChanged();
    }

    private void loadFromServer() {
        if (!Util.isInternetAvailible(getActivity())) return;
        String base;
        if (isSklb) {
            base = Config.URL_LIHAT_DATA_TERNAK_SKLB;
        } else {
            base = Config.URL_LIHAT_DATA_TERNAK;
        }
        String url = base + "?aksi=15&userid=" + user.userid;
        url += "&id_ternak=" + idTernak;
        debug(getClass(), "Load riwayat ternak url=" + url);
        progressbar.setVisibility(View.VISIBLE);
        HttpUrlConnectionApi.get(url, new HttpUrlConnectionApi.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                debug(getClass(), "Load Riwayat resonse=" + jsonObject);
                progressbar.setVisibility(View.INVISIBLE);
                if (jsonObject.get("success").getAsBoolean()) {
                    bindResponse(jsonObject);
                    pref.edit().putString(PREF_LAST_RIWAYAT + idTernak, jsonObject.toString()).apply();
                } else
                    Toast.makeText(getActivity(), jsonObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
