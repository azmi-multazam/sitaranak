package com.zam.sidik_padang.ubahsandi;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.dataternak.detailternak.BaseFragment;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleyStringRequest;

/**
 * A simple {@link Fragment} subclass.
 */
public class UbahSandiFragment extends BaseFragment {

    private static final String VOLLEY_TAG = UbahSandiFragment.class.getName();
    private TextView textSandiLama, textSandiBaru1, textSandiBaru2;

    public UbahSandiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ubah_sandi, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textSandiLama = (TextView) view.findViewById(R.id.fragment_ubah_sandi_TextViewSandiLama);
        textSandiBaru1 = (TextView) view.findViewById(R.id.fragment_ubah_sandi_TextViewSandBaru1);
        textSandiBaru2 = (TextView) view.findViewById(R.id.fragment_ubah_sandi_TextViewSandBaru2);
        view.findViewById(R.id.fragment_ubah_sandi_Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }

    private void validate() {
        String sandiLama = textSandiLama.getText().toString().trim();
        if (sandiLama.isEmpty()) {
            textSandiLama.setError("Harus diisi");
            textSandiLama.requestFocus();
            return;
        }

        String sandiBaru1 = textSandiBaru1.getText().toString().trim();
        if (sandiBaru1.isEmpty()) {
            textSandiBaru1.setError("Harus diisi");
            textSandiBaru1.requestFocus();
            return;
        }

        String sandiBaru2 = textSandiBaru2.getText().toString().trim();
        if (sandiBaru2.isEmpty()) {
            textSandiBaru2.setError("Harus diisi");
            textSandiBaru2.requestFocus();
            return;
        }

        if (!sandiBaru1.equals(sandiBaru2)) {
            textSandiBaru2.setError("Sandi baru tidak sama");
            textSandiBaru2.requestFocus();
            return;
        }

        if (!Util.isInternetAvailible(getActivity())) {
            Toast.makeText(getActivity(), "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = Config.URL_PROFILE + "?aksi=3&userid=" + user.userid;
        url += "&pwlama=" + sandiLama;
        url += "&pwbaru1=" + sandiBaru1;
        url += "&pwbaru2=" + sandiBaru2;
        debug(getClass(), "Ubah sandi url=" + url);
        final Dialog dialog = ProgressDialog.show(getActivity(), null, "Memproses...", true, false);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                dialog.dismiss();
                debug(getClass(), "Ubah sandi response =" + jsonObject);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(jsonObject.get("message").getAsString());
                builder.setPositiveButton(android.R.string.ok, null);
                builder.show();
                if (jsonObject.get("success").getAsBoolean()) {
                    textSandiLama.setText("");
                    textSandiBaru1.setText("");
                    textSandiBaru2.setText("");
                }
            }
        });

        addRequest(request, VOLLEY_TAG);
    }

    @Override
    public void onDetach() {
        cancelRequest(VOLLEY_TAG);
        super.onDetach();
    }
}
