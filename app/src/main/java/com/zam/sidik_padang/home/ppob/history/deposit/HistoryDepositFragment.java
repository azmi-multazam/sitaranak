package com.zam.sidik_padang.home.ppob.history.deposit;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.ppob.history.BaseHistoryFragment;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.VolleyStringRequest;

/**
 * Created by supriyadi on 7/13/17.
 */

public class HistoryDepositFragment extends BaseHistoryFragment {
    private List<HistoryDeposit> list;
    private HistoryDepositAdapter adapter;

    private String VOLLEY_TAG = getClass().getName();
    private boolean isLoading = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        list = new ArrayList<>();
        adapter = new HistoryDepositAdapter(list);
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onViewCreated(view, savedInstanceState);
        //view.setBackgroundColor(Color.parseColor("#a5d6a7"));
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
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
		 http://bungara.com/api/history.php?userid=100001&aksi=7&dtfrom=2017-07-01&dtto=2017-07-11
		 */
        String url = Config.URL_HISTORY + "userid=" + user.userid +
                "&aksi=7&dtfrom=" + dateFormatForRequest.format(calendarMulai.getTime()) +
                "&dtto=" + dateFormatForRequest.format(calendarSampai.getTime());
        // 2017-07-01&dtto=2017-07-11

        debug(getClass(), "Load deposit url=" + url);
        progressBar.setVisibility(View.VISIBLE);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {

            @Override
            public void onResponse(JsonObject jsonObject) {
                isLoading = false;
                progressBar.setVisibility(View.GONE);
                debug(getClass(), "Load History deposit response=" + jsonObject);
                if (jsonObject.get("success").getAsBoolean()) {
                    JsonArray ja = jsonObject.get("history_deposit").getAsJsonArray();
                    list.clear();
                    Gson gson = new Gson();
                    for (JsonElement je : ja) list.add(gson.fromJson(je, HistoryDeposit.class));
                    adapter.notifyDataSetChanged();

                } else if (!isDetached())
                    Toast.makeText(getActivity(), jsonObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag(VOLLEY_TAG);

        VolleySingleton.getInstance(getActivity()).getRequestQueue().add(request);


    }

}
