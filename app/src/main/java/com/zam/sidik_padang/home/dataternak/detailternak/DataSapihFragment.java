package com.zam.sidik_padang.home.dataternak.detailternak;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleyStringRequest;

/**
 * Created by supriyadi on 9/10/17.
 */

public class DataSapihFragment extends BaseFragment {

    private static final String VOLLEY_TAG = DataSapihFragment.class.getName();
    private String idTernak = "", from = "";
    private boolean isSklb;

    public static DataSapihFragment getInstance(String idTernak, String from) {
        Bundle bundle = new Bundle();
        bundle.putString(DetailTernakActivity.ID_TERNAK, idTernak);
        bundle.putString("from", from);
        DataSapihFragment fragment = new DataSapihFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (!idTernak.isEmpty())
            outState.putString(DetailTernakActivity.ID_TERNAK, idTernak);
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
        return inflater.inflate(R.layout.fragment_data_sapih, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isSklb = from.equals("sklb");
        loadData(view);
    }

    private void loadData(final View view) {
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
        String url = base + "?aksi=6&userid=" + user.userid + "&id_ternak=" + idTernak;
        /*http://e-rekording.com/android_api/lihat_dataternak.php?userid=KS1000005&aksi=6&id_ternak=TQ2000004*/
        view.findViewById(R.id.fragment_data_sapi_Progressbar).setVisibility(View.VISIBLE);
        debug(getClass(), "load data sapi url=" + url);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                debug(getClass(), "Load data sapih response " + jsonObject);
                view.findViewById(R.id.fragment_data_sapi_Progressbar).setVisibility(View.INVISIBLE);
                if (jsonObject.get("success").getAsBoolean()) {
                    try {
                        JsonObject object = jsonObject.get("data_sapih").getAsJsonArray().get(0).getAsJsonObject();
                        ((TextView) view.findViewById(R.id.fragment_data_sapi_TextViewTAnggalLahir)).setText(object.get("lahir").getAsString());
                        ((TextView) view.findViewById(R.id.fragment_data_sapi_TextViewBBLAhir)).setText(object.get("bb_lahir").getAsString());
                        ((TextView) view.findViewById(R.id.fragment_data_sapi_TextViewTgLAhir)).setText(object.get("tg_lahir").getAsString());
                        ((TextView) view.findViewById(R.id.fragment_data_sapi_TextViewLingkarDada)).setText(object.get("lingkar_dada").getAsString());
                        ((TextView) view.findViewById(R.id.fragment_data_sapi_TextViewTanggalSapi2)).setText(object.get("tgl_sapih").getAsString());
                        ((TextView) view.findViewById(R.id.fragment_data_sapi_TextViewBBSapih2)).setText(object.get("bb_sapih").getAsString());
                        ((TextView) view.findViewById(R.id.fragment_data_sapi_TextViewBB365)).setText(object.get("bb_365").getAsString());
                        ((TextView) view.findViewById(R.id.fragment_data_sapi_TextViewTanggal365)).setText(object.get("tgl_365").getAsString());
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                        Log.e(getClass().getName(), e.getMessage());
                    }
                } else
                    Toast.makeText(getActivity(), jsonObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
            }
        });
        addRequest(request, VOLLEY_TAG);
    }

	/*"data_sapih":[
		{"lahir":"2003-12-20",
		"bb_lahir":"25",
		"pb_lahir":"",
		"tg_lahir":"",
		"lingkar_dada":"",
		"tgl_sapih":"12/07/2004",
		"bb_sapih":"127.64"}*/

    @Override
    public void onDetach() {
        cancelRequest(VOLLEY_TAG);
        super.onDetach();
    }
}
