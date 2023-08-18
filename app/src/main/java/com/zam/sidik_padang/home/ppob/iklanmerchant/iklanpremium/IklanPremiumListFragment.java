package com.zam.sidik_padang.home.ppob.iklanmerchant.iklanpremium;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.ppob.iklanmerchant.BaseIklanListFragment;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.HttpUrlConnectionApi;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;

/**
 * Created by supriyadi on 3/8/18.
 */

public class IklanPremiumListFragment extends BaseIklanListFragment {

    private static final String TAG = IklanPremiumListFragment.class.getName();
    RecyclerView.Adapter adapter;
    private List<IklanPremium> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        list = new ArrayList<>();
        adapter = new IklanPremiumListAdapter(list);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.frameLayoutSpinner).setVisibility(View.GONE);
        setRecyclerViewAdapter(adapter);
        view.findViewById(R.id.fab).setVisibility(View.GONE);
        loadIklan();
    }

    private void loadIklan() {
        if (!Util.isInternetAvailible(getActivity())) return;
        setProgressBarVisible(true);
        String link = Config.URL_MERCHANT_LIST + "aksi=3&userid=" + user.userid;
        debug("Load iklan premium. Link: " + link);
//		final VolleyStringRequest request=new VolleyStringRequest(link, new VolleyStringRequest.Callback(){
//
//				@Override
//				public void onResponse(JsonObject jsonObject)
//				{
//					debug("Load iklan premium response. " + jsonObject);
//					setProgressBarVisible(false);
//					ApiResponse response=gson.fromJson(jsonObject, ApiResponse.class);
//					if(response!=null && !isDetached()){
//						if(response.success){
//							list.clear();
//							list.addAll(response.merchent_premium);
//							adapter.notifyDataSetChanged();
//						}else{
//							Toast.makeText(getActivity(), response.message, Toast.LENGTH_SHORT).show();
//						}
//					}
//				}
//			});
//
//		request.setTag(TAG);
//		VolleySingleton.getInstance(getActivity()).getRequestQueue().add(request);

        HttpUrlConnectionApi.get(link, new HttpUrlConnectionApi.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                debug("Load iklan premium response. " + jsonObject);
                setProgressBarVisible(false);
                ApiResponse response = gson.fromJson(jsonObject, ApiResponse.class);
                if (response != null && !isDetached()) {
                    if (response.success) {
                        list.clear();
                        list.addAll(response.merchent_premium);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), response.message, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

//		AndroidNetworking.get(link).setTag(TAG).build().getAsObject(ApiResponse.class, new ParsedRequestListener() {
//			@Override
//			public void onResponse(Object response) {
//				debug("Load iklan premium response. " + response);
//				setProgressBarVisible(false);
//				if(((ApiResponse)response).success){
//					list.clear();
//					list.addAll(((ApiResponse)response).merchent_premium);
//					adapter.notifyDataSetChanged();
//				}else{
//					Toast.makeText(getActivity(), ((ApiResponse)response).message, Toast.LENGTH_SHORT).show();
//				}
//			}
//
//			@Override
//			public void onError(ANError anError) {
//				debug("Load iklan premium response. " + anError.getMessage());
//				setProgressBarVisible(false);
//			}
//
//		});
    }


    @Override
    public void onDestroy() {
        VolleySingleton.getInstance(getActivity()).getRequestQueue().cancelAll(TAG);
        AndroidNetworking.cancel(TAG);
        super.onDestroy();
    }


    public static class ApiResponse {
        public boolean success = false;
        public String message = "Terjadi kesalahan 63826";
        public List<IklanPremium> merchent_premium = new ArrayList<>();
    }
}
