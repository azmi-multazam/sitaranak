package com.zam.sidik_padang.home.ppob.history.pulsa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import com.zam.sidik_padang.home.ppob.history.BaseHistoryFragment;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.VolleyStringRequest;

/**
 * Created by supriyadi on 7/13/17.
 */

public class HistoryPulsaFragment extends BaseHistoryFragment {

    private final String VOLLEY_TAG = getClass().getName();
    private HistoryPulsaListAdapter adapter;
    private List<HistoryPulsa> list;

    @Override
    protected void onRecyclerItemClick(View view, int position) {
        Intent it = new Intent(getActivity(), DetailTrxPulsaActivity.class);
        it.putExtra("history_id", list.get(position).id);
        startActivity(it);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        list = new ArrayList<>();
        adapter = new HistoryPulsaListAdapter(list);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onViewCreated(view, savedInstanceState);
        //view.setBackgroundColor(Color.parseColor("#c8e6ca"));
        recyclerView.setAdapter(adapter);
        loadFromServer();
    }

    @Override
    protected void onCalendarBerubah() {
        list.clear();
        adapter.notifyDataSetChanged();
        debug(getClass(), "OnCalender Berubah");
        loadFromServer();
    }
	

	/*
	 http://bungara.com/api/history.php?userid=100003&aksi=1&dtfrom=2017-07-01&dtto=2017-07-11
	 */

    private void loadFromServer() {
        if (!Util.isInternetAvailible(getActivity())) return;
        String url = Config.URL_HISTORY
                + "userid=" + user.userid
                + "&aksi=1"
                + "&dtfrom=" + dateFormatForRequest.format(calendarMulai.getTime())
                + "&dtto=" + dateFormatForRequest.format(calendarSampai.getTime());

        debug(getClass(), "Load history pulsa url=" + url);
        progressBar.setVisibility(View.VISIBLE);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {

            @Override
            public void onResponse(JsonObject jsonObject) {
                progressBar.setVisibility(View.GONE);
                debug(getClass(), "Load History pulsa response=" + jsonObject);
                if (jsonObject.get("success").getAsBoolean()) {
                    JsonArray ja = jsonObject.get("history_pulsa").getAsJsonArray();
                    list.clear();
                    Gson gson = new Gson();
                    for (JsonElement je : ja) list.add(gson.fromJson(je, HistoryPulsa.class));
                    adapter.notifyDataSetChanged();

                } else if (!isDetached())
                    Toast.makeText(getActivity(), jsonObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag(VOLLEY_TAG);

        VolleySingleton.getInstance(getActivity()).getRequestQueue().add(request);


    }


}
