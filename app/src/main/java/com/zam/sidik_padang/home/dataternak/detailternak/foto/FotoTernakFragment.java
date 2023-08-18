package com.zam.sidik_padang.home.dataternak.detailternak.foto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
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
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleyStringRequest;

import static android.app.Activity.RESULT_OK;
import static com.zam.sidik_padang.home.dataternak.detailternak.DetailTernakActivity.ID_TERNAK;


public class FotoTernakFragment extends BaseFragment implements FotoTernakRecyclerViewAdapter.OnFotoItemClickListener {


    private static final String VOLLEY_TAG = FotoTernakFragment.class.getName();
    private String idTernak = "", from = "";
    private FotoTernakRecyclerViewAdapter adapter;
    private List<FotoTernak> list;
    private SharedPreferences pref;
    private View progressbar;
    private boolean isSklb;

    public FotoTernakFragment() {
    }

    public static FotoTernakFragment newInstance(String idTernak, String from) {
        FotoTernakFragment fragment = new FotoTernakFragment();
        Bundle args = new Bundle();
        args.putString(ID_TERNAK, idTernak);
        args.putString("from", from);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(ID_TERNAK)) {
            idTernak = bundle.getString(ID_TERNAK);
        }
        if (bundle != null && bundle.containsKey("from"))
            from = bundle.getString("from");

        list = new ArrayList<>();
        adapter = new FotoTernakRecyclerViewAdapter(list, this);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fototernak, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isSklb = from.equals("sklb");
        progressbar = view.findViewById(R.id.fragment_fototernak_Progressbar);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setAdapter(adapter);
        String tersimpan = pref.getString(Config.PREF_FOTO_TERNAK_ALL_TERSIMPAN + idTernak, "");
        if (!tersimpan.isEmpty())
            bindView(new Gson().fromJson(tersimpan, JsonElement.class).getAsJsonArray());
        loadFromServer();
        view.findViewById(R.id.activity_fototernak_FAB).setVisibility(isSklb ? View.GONE : View.VISIBLE);
        view.findViewById(R.id.activity_fototernak_FAB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UploadFotoActivity.class);
                intent.putExtra("id_ternak", idTernak);
                startActivityForResult(intent, 99);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        debug(getClass(), "onActivity result req Code=" + requestCode);

        if (resultCode == RESULT_OK) {
            loadFromServer();
            if (requestCode == 11) {
                FotoTernak ft = (FotoTernak) data.getSerializableExtra(BukaFotoActivity.EXTRA_FOTO_TERNAK);
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).id.equals(ft.id)) {
                        list.remove(i);
                        adapter.notifyItemRemoved(i);
                        break;
                    }
                }
            }

        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    private void bindView(JsonArray jsonArray) {
        list.clear();
        Gson gson = new Gson();
        FotoTernak fotoTernak;
        for (JsonElement je : jsonArray) {
            fotoTernak = gson.fromJson(je, FotoTernak.class);
            list.add(fotoTernak);
        }
        adapter.notifyDataSetChanged();
    }

    private void loadFromServer() {
        if (!Util.isInternetAvailible(getActivity()) || idTernak.isEmpty()) return;
        String url = Config.URL_GAMBAR_SAPI + "?aksi=1&userid=" + user.userid;
        url += "&id_ternak=" + idTernak;
        debug(getClass(), "Load foto ternak url=" + url);
        progressbar.setVisibility(View.VISIBLE);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                debug(getClass(), "Load fotoresponse=" + jsonObject);
                progressbar.setVisibility(View.INVISIBLE);
                if (jsonObject.get("success").getAsBoolean()) {
                    JsonElement je = jsonObject.get("lihat_gambar_ternak");
                    if (je != null) {
                        JsonArray ja = je.getAsJsonArray();
                        bindView(ja);
                        pref.edit().putString(Config.PREF_FOTO_TERNAK_ALL_TERSIMPAN + idTernak, ja.toString()).apply();
                    }
                }
            }
        });

        addRequest(request, VOLLEY_TAG);
    }

    @Override
    public void onDestroy() {
        cancelRequest(VOLLEY_TAG);
        super.onDestroy();
    }

    @Override
    public void onFotoItemClick(View fotoImageView, FotoTernak fotoTernak) {
        ViewCompat.setTransitionName(fotoImageView, "transisi_foto");
        Intent it = new Intent(getActivity(), BukaFotoActivity.class);
        it.putExtra(BukaFotoActivity.EXTRA_FOTO_TERNAK, fotoTernak);
        ActivityOptionsCompat optios = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), fotoImageView, "transisi_foto");
        ActivityCompat.startActivityForResult(getActivity(), it, 11, optios.toBundle());
    }


}
