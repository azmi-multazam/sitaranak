package com.zam.sidik_padang.home.dataternak.detailternak.datarecording;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

//import com.zam.sidik_padang.BuildConfig;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.dataternak.detailternak.BaseFragment;
import com.zam.sidik_padang.home.dataternak.detailternak.DetailTernakActivity;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.HttpUrlConnectionApi;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.VolleyStringRequest;
import com.zam.sidik_padang.util.customclasses.FAB;

/**
 * Created by supriyadi on 9/10/17.
 */

public class DataRecordingTernakFragment extends BaseFragment {


    private static final String VOLLEY_TAG = DataRecordingTernakFragment.class.getName();
    private String idTernak = "", from = "";
    private List<DataRecordingItem> list;
    private ListDataAdapter adapter;
    private View progressbar;
    private boolean isSklb;

    public static DataRecordingTernakFragment getInstance(String idTernak, String from) {
        Bundle bundle = new Bundle();
        bundle.putString(DetailTernakActivity.ID_TERNAK, idTernak);
        bundle.putString("from", from);
        DataRecordingTernakFragment fragment = new DataRecordingTernakFragment();
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(DetailTernakActivity.ID_TERNAK))
            idTernak = bundle.getString(DetailTernakActivity.ID_TERNAK);

        if (bundle != null && bundle.containsKey("from"))
            from = bundle.getString("from");
    }

    private void deleteDataRecording(DataRecordingItem item) {
        if (!Util.isInternetAvailible(getActivity())) {
            Toast.makeText(getActivity(), "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = Config.URL_LIHAT_DATA_TERNAK + "?aksi=11&id=" + item.id;
        url += "&userid=" + user.userid;
        url += "&id_ternak=" + idTernak;
        debug(getClass(), "Hapus data recording url=" + url);
        final Dialog dialog = ProgressDialog.show(getActivity(), null, "Menghapus data...", true, false);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                dialog.dismiss();
                debug(DataRecordingTernakFragment.this.getClass(), "Hapus data response=" + jsonObject);
                if (jsonObject.get("success").getAsBoolean()) {
                    loadData();
                    Toast.makeText(getActivity(), "Berhasil", Toast.LENGTH_SHORT).show();
                } else if (!isDetached())
                    Util.showDialog(getActivity(), jsonObject.get("message").getAsString());
            }
        });

        addRequest(request, VOLLEY_TAG);
        VolleySingleton.getInstance(getActivity()).getRequestQueue().add(request);
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
        list = new ArrayList<>();
        adapter = new ListDataAdapter(list, new ListDataAdapter.OnRecordingItemClickListener() {
            @Override
            public void onRecordingItemClick(DataRecordingItem item) {
                Intent intent = new Intent(getActivity(), TambahRecordingActivity.class);
                intent.putExtra(DetailTernakActivity.ID_TERNAK, idTernak);
                intent.putExtra(TambahRecordingActivity.EXTRA_DATA_RECORDING_ITEM, item);
                startActivityForResult(intent, 1);
            }

            @Override
            public void onRecordingItemDeleteButtonClick(final DataRecordingItem item) {
                new AlertDialog.Builder(getActivity())
                        .setMessage("Hapus data recording tanggal " + item.tanggal + "?")
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteDataRecording(item);
                            }
                        })
                        .show();
            }
        });

        RecyclerView rv = view.findViewById(R.id.fragment_data_recording_ternak_RecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(adapter);
        progressbar = view.findViewById(R.id.fragment_data_recording_ternak_Progresbar);
        loadData();
        view.findViewById(R.id.fragment_data_recording_ternak_FAB).setVisibility(isSklb ? View.GONE : View.VISIBLE);
        view.findViewById(R.id.fragment_data_recording_ternak_FAB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TambahRecordingActivity.class);
                intent.putExtra(DetailTernakActivity.ID_TERNAK, idTernak);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) loadData();
    }

    private void loadData() {
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

        String url = base + "?aksi=4&userid=" + user.userid + "&id_ternak=" + idTernak;
        debug(getClass(), "Load data recording url=" + url);
        progressbar.setVisibility(View.VISIBLE);
        HttpUrlConnectionApi.get(url, new HttpUrlConnectionApi.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                debug(getClass(), "Load data recording response " + jsonObject);
                progressbar.setVisibility(View.INVISIBLE);
                if (jsonObject.get("success").getAsBoolean()) {
                    try {
                        list.clear();
                        JsonArray jsonArray = jsonObject.get("data_recording").getAsJsonArray();
                        Gson gson = new Gson();
                        DataRecordingItem item;
                        for (JsonElement je : jsonArray) {
                            item = gson.fromJson(je, DataRecordingItem.class);
                            list.add(item);
                        }
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                        Log.e(getClass().getName(), e.getMessage());
                    }
                } else
                    Toast.makeText(getActivity(), jsonObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
            }
        });
		/*VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
			@Override
			public void onResponse(JsonObject jsonObject) {
				debug(getClass(), "Load data recording response " + jsonObject);
				progressbar.setVisibility(View.INVISIBLE);
				if (jsonObject.get("success").getAsBoolean()) {
					try {
						list.clear();
						JsonArray jsonArray = jsonObject.get("data_recording").getAsJsonArray();
						Gson gson = new Gson();
						DataRecordingItem item;
						for (JsonElement je : jsonArray) {
							item = gson.fromJson(je, DataRecordingItem.class);
							list.add(item);
						}
						adapter.notifyDataSetChanged();
					} catch (Exception e) {
						Toast.makeText(getActivity(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
						Log.e(getClass().getName(), e.getMessage());
					}
				} else
					Toast.makeText(getActivity(), jsonObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
			}
		});
//		addRequest(request, VOLLEY_TAG);
		addRequest(request,VOLLEY_TAG);
		VolleySingleton.getInstance(getActivity()).getRequestQueue().add(request);*/
    }

    /*http://e-rekording.com/android_api/lihat_dataternak.php?userid=KS1000005&aksi=4&id_ternak=TQ2000002*/

    @Override
    public void onDestroy() {
        cancelRequest(VOLLEY_TAG);
        super.onDestroy();
    }

    @Override
    protected void debug(Class<?> cls, String msg) {
        Log.d(cls.getName(), msg);
        //if (BuildConfig.DEBUG) Log.e(cls.getName(), msg);
    }
}


/*
http://e-rekording.com/android_api/lihat_dataternak.php?userid=KS1000005&aksi=4&id_ternak=TQ2000002
* {"id":"2",
* 	"tanggal":"2004-04-21",
* 	"berat_badan":"64.5",
* 	"panjang_badan":"76.5",
* 	"tinggi_badan":"84",
* 	"lingkar_dada":"93",
* 	"dalam_dada":"35.5"}
* 	*/