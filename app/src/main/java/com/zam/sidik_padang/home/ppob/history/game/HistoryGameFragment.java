package com.zam.sidik_padang.home.ppob.history.game;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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

public class HistoryGameFragment extends BaseHistoryFragment {

    private final String VOLLEY_TAG = getClass().getName();
    private List<HistoryGame> list;
    private HistoryGameAdapter adapter;
    private boolean isLoading = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        list = new ArrayList<>();
        adapter = new HistoryGameAdapter(list);
    }

    @Override
    protected void onRecyclerItemClick(View view, int position) {
        // TODO: Implement this method
    }

    @Override
    protected void onCalendarBerubah() {
        loadHistoryFromServer();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onViewCreated(view, savedInstanceState);
        //view.setBackgroundColor(Color.parseColor("#c5e1a5"));
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
		 http://bungara.com/api/history.php?userid=100001&aksi=8&dtfrom=2017-07-01&dtto=2017-07-11
		 */
        String url = Config.URL_HISTORY + "userid=" + user.userid +
                "&aksi=5&dtfrom=" + dateFormatForRequest.format(calendarMulai.getTime()) +
                "&dtto=" + dateFormatForRequest.format(calendarSampai.getTime());
        // 2017-07-01&dtto=2017-07-11

        debug(getClass(), "Load hidtory game url=" + url);
        progressBar.setVisibility(View.VISIBLE);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {

            @Override
            public void onResponse(JsonObject jsonObject) {
                isLoading = false;
                progressBar.setVisibility(View.GONE);
                debug(getClass(), "Load hidtory game response=" + jsonObject);
                if (jsonObject.get("success").getAsBoolean()) {
                    list.clear();
                    HistoryGame h = new HistoryGame();
//						h.str=jsonObject.toString();
                    list.add(h);
                    adapter.notifyDataSetChanged();
                    //Toast.makeText(getActivity(), "Load History game:\n"+jsonObject,Toast.LENGTH_SHORT).show();
                } else if (!isDetached())
                    Toast.makeText(getActivity(), jsonObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag(VOLLEY_TAG);

        VolleySingleton.getInstance(getActivity()).getRequestQueue().add(request);


    }

}
