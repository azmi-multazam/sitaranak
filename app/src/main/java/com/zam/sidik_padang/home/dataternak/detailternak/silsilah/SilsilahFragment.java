package com.zam.sidik_padang.home.dataternak.detailternak.silsilah;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.dataternak.detailternak.BaseFragment;
import com.zam.sidik_padang.home.dataternak.detailternak.DetailTernakActivity;
import com.zam.sidik_padang.util.Config;

import static com.zam.sidik_padang.home.dataternak.detailternak.DetailTernakActivity.ID_TERNAK;

public class SilsilahFragment extends BaseFragment implements View.OnClickListener {

    private static final String PREF_SAVED_SILSILAH = "silsilah";
    private static final String TAG = SilsilahFragment.class.getName();
    private String idTernak = "", eartag = "", from = "";
    private View progressbar;
    private boolean isLoading = false;
    private SilsilahLinearLayout silsilahUtama, silsilahIndukKiri1, silsilahBapakKanan1,
            silsilahIndukKiri2, silsilahBapakKiri2, silsilahIndukKanan2, silsilahBapakKanan2;

    private TextView textGenerasi1, textGenerasi2;
    private boolean isSklb;

    public static SilsilahFragment newInstance(String idTernak, String eartag, String from) {
        SilsilahFragment fragment = new SilsilahFragment();
        Bundle args = new Bundle();
        args.putString(ID_TERNAK, idTernak);
        args.putString("eartag", eartag);
        args.putString("from", from);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (!idTernak.isEmpty()) {
            outState.putString(ID_TERNAK, idTernak);
            outState.putString("eartag", eartag);
        }
        if (!from.isEmpty()) outState.putString("from", from);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if (savedInstanceState != null && savedInstanceState.containsKey(ID_TERNAK)) {
            idTernak = savedInstanceState.getString(ID_TERNAK);
            eartag = savedInstanceState.getString("eartag");
        } else {
            if (b != null && b.containsKey(ID_TERNAK)) {
                idTernak = b.getString(ID_TERNAK);
                eartag = b.getString("eartag");
            }
        }
        if (savedInstanceState != null && savedInstanceState.containsKey("from"))
            from = savedInstanceState.getString("from");

        if (b != null && b.containsKey("from"))
            from = b.getString("from");

        if (idTernak.isEmpty())
            Toast.makeText(getActivity(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_silsilah, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isSklb = from.equals("sklb");
        silsilahUtama = view.findViewById(R.id.ternakUtama);
        silsilahUtama.setText("Ternak", idTernak, "0");
        silsilahUtama.setTextIdClickListener(this);
        silsilahIndukKiri1 = view.findViewById(R.id.ternakIndukKiri1);
        silsilahIndukKiri1.setOnClickListener(this);
        silsilahIndukKiri1.setTextIdClickListener(this);//
        silsilahBapakKanan1 = view.findViewById(R.id.ternakBapakKanan1);
        silsilahBapakKanan1.setOnClickListener(this);
        silsilahBapakKanan1.setTextIdClickListener(this);//
        silsilahIndukKiri2 = view.findViewById(R.id.ternakIndukKiri2);
        silsilahIndukKiri2.setOnClickListener(this);
        silsilahIndukKiri2.setTextIdClickListener(this);//
        silsilahBapakKiri2 = view.findViewById(R.id.ternakBapakKiri2);
        silsilahBapakKiri2.setOnClickListener(this);
        silsilahBapakKiri2.setTextIdClickListener(this);//
        silsilahIndukKanan2 = view.findViewById(R.id.ternakIndukKanan2);
        silsilahIndukKanan2.setOnClickListener(this);
        silsilahIndukKanan2.setTextIdClickListener(this);//
        silsilahBapakKanan2 = view.findViewById(R.id.ternakBapakKanan2);
        silsilahBapakKanan2.setOnClickListener(this);
        silsilahBapakKanan2.setTextIdClickListener(this);//
        progressbar = view.findViewById(R.id.progressBar);
        textGenerasi1 = view.findViewById(R.id.textViewGenerasi1);
        textGenerasi2 = view.findViewById(R.id.textViewGenerasi2);
        String saved = pref.getString(PREF_SAVED_SILSILAH + "_" + idTernak, "");
        if (!saved.isEmpty()) {
            bindSilsilah(saved);
        }
        loadSilsilah(null);
    }

    private void bindSilsilah(String response) {
        Gson gson = new Gson();
        SilsilahApiResponse silsilah = gson.fromJson(response, SilsilahApiResponse.class);
        SilsilahApiResponse.Generasi1 generasi1 = silsilah.generasi1.get(0);
        silsilahIndukKiri1.setText("Induk", generasi1.induk_kiri1, generasi1.earteg_indukkiri1);
        silsilahIndukKiri1.generasi = generasi1.generasi;
        silsilahBapakKanan1.setText("Bapak", generasi1.bapak_kanan1, generasi1.earteg_bapakkanan1);
        silsilahBapakKanan1.generasi = generasi1.generasi;
        SilsilahApiResponse.Generasi2 generasi2 = silsilah.generasi2.get(0);
        silsilahIndukKiri2.setText("Induk", generasi2.induk_kiri2, generasi2.earteg_indukkiri2);
        silsilahIndukKiri2.generasi = generasi2.generasi;
        silsilahBapakKiri2.setText("Bapak", generasi2.bapak_kiri2, generasi2.earteg_bapakkiri2);
        silsilahBapakKiri2.generasi = generasi2.generasi;
        silsilahIndukKanan2.setText("Induk", generasi2.induk_kanan2, generasi2.earteg_indukkanan2);
        silsilahIndukKanan2.generasi = generasi2.generasi;
        silsilahBapakKanan2.setText("Bapak", generasi2.bapak_kanan2, generasi2.earteg_bapakkanan2);
        silsilahBapakKanan2.generasi = generasi2.generasi;
        textGenerasi1.setText("G" + generasi1.generasi);
        textGenerasi2.setText("G" + generasi2.generasi);

    }


    private void loadSilsilah(String generasi) {
        isLoading = true;
        textGenerasi1.setText("");
        textGenerasi2.setText("");
        progressbar.setVisibility(View.VISIBLE);
        String base;
        if (isSklb) {
            base = Config.URL_LIHAT_DATA_TERNAK_SKLB;
        } else {
            base = Config.URL_LIHAT_DATA_TERNAK;
        }

        ANRequest.GetRequestBuilder getRequestBuilder = AndroidNetworking.get(base)
                .addQueryParameter("userid", user.userid)
                .addQueryParameter("aksi", "18")
                .addQueryParameter("id_ternak", idTernak)
                .setPriority(Priority.MEDIUM)
                .setTag(TAG);
        if (generasi != null) getRequestBuilder.addQueryParameter("generasi", generasi);

        getRequestBuilder.build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        isLoading = false;
                        debug(getClass(), "Response: " + response);
                        progressbar.setVisibility(View.INVISIBLE);
                        if (response.startsWith("{")) {
                            pref.edit().putString(PREF_SAVED_SILSILAH + "_" + idTernak, response).apply();
                            bindSilsilah(response);

                        } else
                            Toast.makeText(getActivity(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressbar.setVisibility(View.INVISIBLE);
                        debug(getClass(), anError.getMessage());
                        isLoading = false;
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if (isLoading) return;
        if (view instanceof SilsilahLinearLayout) {
            SilsilahLinearLayout layout = (SilsilahLinearLayout) view;
            idTernak = layout.getIdTernak();
            if (idTernak == null || idTernak.isEmpty() || idTernak.equals("0")) {
                Toast.makeText(getActivity(), "ID ternak tidak ada", Toast.LENGTH_SHORT).show();
                return;
            }
            eartag = layout.getEartag();
            silsilahUtama.setText("Ternak", idTernak, eartag);
            silsilahIndukKiri1.setText("Induk", "ID", "Ear tag");
            silsilahBapakKanan1.setText("Bapak", "ID", "Ear tag");
            silsilahIndukKiri2.setText("Induk", "ID", "Ear tag");
            silsilahBapakKiri2.setText("Bapak", "ID", "Ear tag");
            silsilahIndukKanan2.setText("Induk", "ID", "Ear tag");
            silsilahBapakKanan2.setText("Bapak", "ID", "Ear tag");
            String saved = pref.getString(PREF_SAVED_SILSILAH + "_" + idTernak, "");
            if (!saved.isEmpty()) {
                bindSilsilah(saved);
            }
            loadSilsilah(layout.generasi);
        } else if (view instanceof TextView) {
            String id = ((TextView) view).getText().toString().trim();
            if (id.isEmpty() || id.equals("0")) {
                Toast.makeText(getActivity(), "ID ternak kosong", Toast.LENGTH_SHORT).show();
                return;
            }
            String eartag = "";
            View v = (View) view.getParent();
            if (v instanceof SilsilahLinearLayout) eartag = ((SilsilahLinearLayout) v).getEartag();
            Intent it = new Intent(getActivity(), DetailTernakActivity.class);
            it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            it.putExtra(DetailTernakActivity.ID_TERNAK, id);
            it.putExtra("eartag", eartag);
            startActivity(it);
        }
    }

    @Override
    public void onDestroy() {
        AndroidNetworking.cancel(TAG);
        super.onDestroy();
    }


}
