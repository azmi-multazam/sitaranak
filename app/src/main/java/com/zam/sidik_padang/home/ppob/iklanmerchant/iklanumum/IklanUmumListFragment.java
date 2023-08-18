package com.zam.sidik_padang.home.ppob.iklanmerchant.iklanumum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.ppob.iklanmerchant.BaseIklanListFragment;
import com.zam.sidik_padang.home.ppob.iklanmerchant.Category;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.HttpUrlConnectionApi;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;


/**
 * Created by supriyadi on 3/8/18.
 */

public class IklanUmumListFragment extends BaseIklanListFragment {


    private static final String TAG = IklanUmumListFragment.class.getName();
    RecyclerView.Adapter adapter;
    private List<IklanUmum> list;
    private boolean isIklanPribadi = false;

    public void setIklanPribadi(boolean iklanPribadi) {
        isIklanPribadi = iklanPribadi;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        list = new ArrayList<>();
        adapter = new IklanUmumListAdapter(list, isIklanPribadi);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRecyclerViewAdapter(adapter);
        loadIklan(null);
        view.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(view.getContext(), TambahIklanActivity.class), 11);
            }
        });
    }

    @Override
    protected void onCategorySelected(Category category) {
        // TODO: Implement this method
        super.onCategorySelected(category);
        loadIklan(category);
    }


    private void loadIklan(Category category) {
        if (!Util.isInternetAvailible(getActivity())) return;
        setProgressBarVisible(true);
        String link = Config.URL_MERCHANT_LIST + "aksi=" + (isIklanPribadi ? 7 : 1) + "&userid=" + user.userid;
        if (category != null) link += "&kategori=" + category.id_kategori;
        debug("Load iklan premium. Link: " + link);
//		VolleyStringRequest request = new VolleyStringRequest(link, new VolleyStringRequest.Callback() {
//
//			@Override
//			public void onResponse(JsonObject jsonObject) {
//				debug("Load iklan premium response. " + jsonObject);
//				setProgressBarVisible(false);
//				ApiResponse response = gson.fromJson(jsonObject, ApiResponse.class);
//				if (response != null && !isDetached()) {
//					if (response.success) {
//						list.clear();
//						list.addAll(isIklanPribadi?response.iklan_saya: response.list_merchent);
//						adapter.notifyDataSetChanged();
//						setTextViewNoDataVisible(list.size() == 0);
//					} else {
//						Toast.makeText(getActivity(), response.message, Toast.LENGTH_SHORT).show();
//					}
//				}
//			}
//		});

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
                        list.addAll(isIklanPribadi ? response.iklan_saya : response.list_merchent);
                        adapter.notifyDataSetChanged();
                        setTextViewNoDataVisible(list.size() == 0);
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
//				if (((ApiResponse)response).success) {
//					list.clear();
//					list.addAll(isIklanPribadi?((ApiResponse)response).iklan_saya: ((ApiResponse)response).list_merchent);
//					adapter.notifyDataSetChanged();
//					setTextViewNoDataVisible(list.size() == 0);
//				} else {
//					Toast.makeText(getActivity(), ((ApiResponse)response).message, Toast.LENGTH_SHORT).show();
//				}
//			}
//
//			@Override
//			public void onError(ANError anError) {
//				setProgressBarVisible(false);
//				debug("Load iklan premium response. " + anError.getMessage());
//				if (!isDetached()) Toast.makeText(getActivity(), anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
//			}
//		});

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 11) {
            if (resultCode == Activity.RESULT_OK) loadIklan(null);
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onDestroy() {
        VolleySingleton.getInstance(getActivity()).getRequestQueue().cancelAll(TAG);
        AndroidNetworking.cancel(TAG);
        super.onDestroy();
    }


    public static class ApiResponse {
        public boolean success = false;
        public String message = "Terjadi kesalahan 68326";
        public List<IklanUmum> list_merchent = new ArrayList<>();
        public List<IklanUmum> iklan_saya = new ArrayList<>();

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }
}
