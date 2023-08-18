package com.zam.sidik_padang.home.dataternak.insiminator;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import com.zam.sidik_padang.BaseApiResponse;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.basesearchdata.BaseSearchDataActivity;
import com.zam.sidik_padang.home.dataternak.insiminator.detailternak.DetailTernakIbActivity;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.Util;

/**
 * Created by supriyadi on 4/16/18.
 */

public class TernakIbListActivity extends BaseSearchDataActivity implements TernakIbListAdapter.OnListItemClickListener {

    private static final String PREF_LAST_RESPONSE = "last_response";
    private static final String TAG = TernakIbListActivity.class.getName();
    private List<TernakIb> list;
    private RecyclerView.Adapter adapter;
    private TextView textViewJumlahTernak;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(R.id.activity_base_search_data_TextViewRegion).setVisibility(View.GONE);
        list = new ArrayList<>();
        adapter = new TernakIbListAdapter(list, this);
        rv.setAdapter(adapter);

        String saved = sharedPreferences.getString(PREF_LAST_RESPONSE, "");
        if (!saved.isEmpty() && saved.startsWith("{")) {
            JsonElement object = new Gson().fromJson(saved, JsonElement.class);
            if (object != null) onResponseSuccess(object.getAsJsonObject(), false);
        } else {
            searchData(Config.URL_TAMBAH_IB + "?aksi=3&userid=" + user.userid, false);
        }
        textViewJumlahTernak = (TextView) findViewById(R.id.activity_base_search_data_TextViewTotal);

    }

    @Override
    protected void onResponseSuccess(JsonObject jsonObject, boolean fromScanner) {
        super.onResponseSuccess(jsonObject, fromScanner);
        GetTernakListApiResponse response = new Gson().fromJson(jsonObject, GetTernakListApiResponse.class);
        list.clear();
        list.addAll(response.data_ternak);
        adapter.notifyDataSetChanged();
        setTextNoDataVisible(list.size() == 0);
        textViewJumlahTernak.setText("Total: " + list.size());
        textViewJumlahTernak.setVisibility(View.VISIBLE);
    }

    @Override
    public void onListItemClick(TernakIb ternakIb, int position) {
        Intent it = new Intent(this, DetailTernakIbActivity.class);
        it.putExtra(DetailTernakIbActivity.ID_TERNAK, ternakIb.id_ternak);
        it.putExtra("ternak", ternakIb);
        startActivity(it);
    }

    @Override
    public void onDeleteButtonClick(TernakIb ternakIb, final int position) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(R.string.delete_)
                .setMessage(getString(R.string.data_ternak_, list.get(position).nama) + " - " + list.get(position).id_ternak)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteDataternak(position);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.activity_base_search_data_FAB) {
            Intent it = new Intent(this, TambahTernakIbActivity.class);
            startActivityForResult(it, 21);

        } else
            super.onClick(v);
    }

    private void deleteDataternak(final int listPosition) {
        if (!Util.isInternetAvailible(this)) {
            Util.noInternetDialog(this);
            return;
        }
        String url = Config.URL_TAMBAH_IB + "?userid=" + user.userid + "&aksi=2&id=" + list.get(listPosition).id;
        debug(getClass(), "Delete data ternak url: " + url);
        final Dialog dialog = ProgressDialog.show(this, null, getString(R.string.please_wait), true, false);
//		final VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
//			@Override
//			public void onResponse(JsonObject jsonObject) {
//				dialog.dismiss();
//				debug(getClass(), "Delete Data ternak, Onresponse: " + jsonObject);
//				Toast.makeText(TernakIbListActivity.this, jsonObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
//				if (jsonObject.get("success").getAsBoolean()) {
//					list.remove(listPosition);
//					adapter.notifyDataSetChanged();
//				}
//			}
//		});

        AndroidNetworking.get(url).setTag(TAG).build().getAsObject(BaseApiResponse.class, new ParsedRequestListener() {
            @Override
            public void onResponse(Object response) {
                dialog.dismiss();
                debug(getClass(), "Delete Data ternak, Onresponse: " + response);
                Toast.makeText(TernakIbListActivity.this, ((BaseApiResponse) response).message, Toast.LENGTH_SHORT).show();
                if (((BaseApiResponse) response).success) {
                    list.remove(listPosition);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(ANError anError) {
                dialog.dismiss();
                Log.e(getClass().getName(), "Delete onErro: " + anError.getMessage());
                if (isResummed)
                    Toast.makeText(TernakIbListActivity.this, "" + anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onDestroy() {
        AndroidNetworking.cancel(TAG);
        super.onDestroy();
    }
}
