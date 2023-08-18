package com.zam.sidik_padang.home.ppob.history.mutasi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.ppob.history.BaseHistoryFragment;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.VolleyStringRequest;

/**
 * Created by supriyadi on 7/13/17.
 */

public class HistoryMutasiFragment extends BaseHistoryFragment {

    private List<HistoryMutasi> list;
    private HistoryMutasiAdapter adapter;
    private TextView textTotalKredit, textTotalDebit;
    private double totalKredit = 0, totalDebet = 0;

    private final String VOLLEY_TAG = getClass().getName();
    private boolean isLoading = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        list = new ArrayList<>();
        adapter = new HistoryMutasiAdapter(list);
    }

    @Override
    protected void onRecyclerItemClick(View view, int position) {
        // TODO: Implement this method
    }

    @Override
    protected void onCalendarBerubah() {
        list.clear();
        adapter.notifyDataSetChanged();
        debug(getClass(), "OnCalender Berubah");

        loadHistoryFromServer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: Implement this method
        return inflater.inflate(R.layout.fragment_history_mutasi, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onViewCreated(view, savedInstanceState);
        //view.setBackgroundColor(Color.parseColor("#b2dfdb"));
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        TextView tv = view.findViewById(R.id.fragment_history_mutasiinclude_header).findViewById(R.id.itemhistorymutasiTextViewTanggal);
        tv.setText("Tanggal");
        TextView tv2 = view.findViewById(R.id.fragment_history_mutasiinclude_total).findViewById(R.id.itemhistorymutasiTextViewTanggal);
        tv2.setText("Total ===>");
        textTotalKredit = view.findViewById(R.id.fragment_history_mutasiinclude_total).findViewById(R.id.itemhistorymutasiTextViewKredit);
        TextView tv3 = view.findViewById(R.id.fragment_history_mutasiinclude_header).findViewById(R.id.itemhistorymutasiTextViewKode);
        tv3.setText("Kode");
        textTotalKredit.setText("0");
        textTotalDebit = view.findViewById(R.id.fragment_history_mutasiinclude_total).findViewById(R.id.itemhistorymutasiTextViewDebet);
        textTotalDebit.setText("0");

    }

    @Override
    public void onStart() {
        // TODO: Implement this method
        super.onStart();
        recyclerView.postDelayed(new Runnable() {

            @Override
            public void run() {
                loadHistoryFromServer();
            }
        }, 200);
    }

    private void loadHistoryFromServer() {
        if (isLoading) return;
        if (!Util.isInternetAvailible(getActivity())) {
            Toast.makeText(getActivity(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }
        isLoading = true;
		/*
		 http://bungara.com/api/history.php?userid=100001&aksi=8&dtfrom=2017-07-01&dtto=2017-07-11
		*/
        String url = Config.URL_HISTORY + "userid=" + user.userid +
                "&aksi=8&dtfrom=" + dateFormatForRequest.format(calendarMulai.getTime()) +
                "&dtto=" + dateFormatForRequest.format(calendarSampai.getTime());
        // 2017-07-01&dtto=2017-07-11

        debug(getClass(), "Load mutasi url=" + url);
        progressBar.setVisibility(View.VISIBLE);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {

            @Override
            public void onResponse(JsonObject jsonObject) {
                totalDebet = totalKredit = 0;
                isLoading = false;
                progressBar.setVisibility(View.GONE);
                debug(getClass(), "Load mutasi response=" + jsonObject);
                if (jsonObject.get("success").getAsBoolean()) {
                    JsonArray ja = jsonObject.get("mutasi").getAsJsonArray();
                    list.clear();
                    Gson gson = new Gson();
                    HistoryMutasi hm;
                    for (JsonElement je : ja) {
                        hm = gson.fromJson(je, HistoryMutasi.class);
                        totalDebet += hm.debet;
                        totalKredit += hm.kredit;
                        list.add(hm);

                    }
                    adapter.notifyDataSetChanged();
                    NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());
                    if (totalKredit > 0) textTotalKredit.setText("Rp." + nf.format(totalKredit));
                    if (totalDebet > 0) textTotalDebit.setText("Rp." + nf.format(totalDebet));
                } else if (!isDetached())
                    Toast.makeText(getActivity(), jsonObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag(VOLLEY_TAG);

        VolleySingleton.getInstance(getActivity()).getRequestQueue().add(request);


    }


}
