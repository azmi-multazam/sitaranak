package com.zam.sidik_padang.home.ppob.history.tagihan;

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
import com.zam.sidik_padang.util.data.ApiHelper;

/**
 * Created by supriyadi on 7/13/17.
 */

public class HistoryTagihanFragment extends BaseHistoryFragment implements HistoryTagihanAdapter.OnItemButtonClickListener {

    private final String VOLLEY_TAG = getClass().getName();
    private HistoryTagihanAdapter adapter;
    private List<HistoryTagihan> list;

    @Override
    protected void onRecyclerItemClick(View view, int position) {
//		Intent it=new Intent(getActivity(), DetailTagihanActivity.class);
//		it.putExtra("history_id", list.get(position).id);
//		startActivity(it);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        list = new ArrayList<>();
        adapter = new HistoryTagihanAdapter(list, this);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onViewCreated(view, savedInstanceState);
        //view.setBackgroundColor(Color.parseColor("#ddedc8"));
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


    private void loadFromServer() {
        if (!Util.isInternetAvailible(getActivity())) return;
        String url = Config.URL_HISTORY
                + "userid=" + user.userid
                + "&aksi=3"
                + "&dtfrom=" + dateFormatForRequest.format(calendarMulai.getTime())
                + "&dtto=" + dateFormatForRequest.format(calendarSampai.getTime());

        debug(getClass(), "Load history tagihan url=" + url);
        progressBar.setVisibility(View.VISIBLE);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {

            @Override
            public void onResponse(JsonObject jsonObject) {
                progressBar.setVisibility(View.GONE);
                debug(getClass(), "Load History tagihan response=" + jsonObject);
                if (jsonObject.get("success").getAsBoolean()) {
                    JsonArray ja = jsonObject.get("history_tagihan").getAsJsonArray();
                    list.clear();
                    Gson gson = new Gson();
                    for (JsonElement je : ja) list.add(gson.fromJson(je, HistoryTagihan.class));
                    adapter.notifyDataSetChanged();

                } else if (!isDetached())
                    Toast.makeText(getActivity(), jsonObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag(VOLLEY_TAG);

        VolleySingleton.getInstance(getActivity()).getRequestQueue().add(request);


    }

    @Override
    public void onItemButtonClickCekUlang(int position) {
        ApiHelper.cekUlangTagian(this.getActivity(), list.get(position).id, new ApiHelper.OnDoneListener() {
            @Override
            public void onDone(boolean success) {
                if (success) loadFromServer();
            }
        });
    }

    @Override
    public void onItemButtonClickBayar(int position) {
        ApiHelper.bayarTagihan(this.getActivity(), list.get(position).id, new ApiHelper.OnDoneListener() {
            @Override
            public void onDone(boolean success) {
                if (success) loadFromServer();
            }
        });
    }
}
