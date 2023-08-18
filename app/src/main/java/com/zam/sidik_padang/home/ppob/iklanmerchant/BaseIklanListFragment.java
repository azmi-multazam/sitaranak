package com.zam.sidik_padang.home.ppob.iklanmerchant;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

//import com.zam.sidik_padang.BuildConfig;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.HttpUrlConnectionApi;
import com.zam.sidik_padang.util.User;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.customclasses.SpinnerAdapter;

/**
 * Created by supriyadi on 3/8/18.
 */

public class BaseIklanListFragment extends Fragment
        implements AdapterView.OnItemSelectedListener {


    public static final String PREF_SAVED_CATEGORIES = "categories_response";
    private static final String TAG = BaseIklanListFragment.class.getName();
    protected User user;
    protected Gson gson;
    private RecyclerView recyclerView;
    private View textViewNoData, progressBar;
    private int selectedKategory = 0;
    private SpinnerAdapter spinnerAdapter;
    private List<Object> categoryList;
    private View progressBarSpinner;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
//		user= DataHelper.getDataHelper(getActivity()).getUser();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        gson = new Gson();
        loadUser();
    }

    public void loadUser() {
        String userJson = sharedPreferences.getString(Config.PREF_USER_DETAIL_JSON, "");
        if (!userJson.isEmpty()) {
            user = new Gson().fromJson(userJson, User.class);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_iklan_merchant, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (user == null) {
            Toast.makeText(getActivity(), "Terjadi Kesalahan 41", Toast.LENGTH_SHORT).show();
            return;
        }
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressBar = view.findViewById(R.id.progressBar);
        textViewNoData = view.findViewById(R.id.textViewNoData);
        categoryList = new ArrayList<>();
        categoryList.add("-Pilih Kategori-");
        spinnerAdapter = new SpinnerAdapter(getResources(), categoryList);
        AppCompatSpinner spinner = (AppCompatSpinner) view.findViewById(R.id.spinner);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(selectedKategory);
        spinner.setOnItemSelectedListener(this);
        progressBarSpinner = view.findViewById(R.id.progressBarSpinner);
        loadCategories();
        String saved = sharedPreferences.getString(PREF_SAVED_CATEGORIES, null);
        if (saved != null && saved.startsWith("{")) {
            CategoriesResponse response = gson.fromJson(saved, CategoriesResponse.class);
            if (response != null) updateSpinnerList(response);
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
        selectedKategory = p3;
        Category category = selectedKategory == 0 ? null : (Category) categoryList.get(selectedKategory);
        onCategorySelected(category);
    }

    @Override
    public void onNothingSelected(AdapterView<?> p1) {
        // TODO: Implement this method
    }

    private void updateSpinnerList(CategoriesResponse response) {
        categoryList.clear();
        categoryList.add("-Pilih Kategori-");
        categoryList.addAll(response.kategori_merchent);
        spinnerAdapter.notifyDataSetChanged();
        if (progressBarSpinner.getVisibility() == View.VISIBLE)
            progressBarSpinner.setVisibility(View.GONE);

    }

    protected void onCategorySelected(Category category) {

    }


    private void loadCategories() {
        if (!Util.isInternetAvailible(getActivity())) return;

        String link = Config.URL_MERCHANT_LIST + "aksi=2&userid=" + user.userid;
        debug("Load iklan categories. Link: " + link);
        progressBarSpinner.setVisibility(View.VISIBLE);
//		VolleyStringRequest request=new VolleyStringRequest(link, new VolleyStringRequest.Callback(){
//
//				@Override
//				public void onResponse(JsonObject jsonObject)
//				{
//					progressBarSpinner.setVisibility(View.GONE);
//					debug("Load categori iklan response: "+jsonObject);
//					CategoriesResponse response=gson.fromJson(jsonObject, CategoriesResponse.class);
//					if(response==null) return;
//					if(response.success){
//						updateSpinnerList(response);
//						sharedPreferences.edit().putString(PREF_SAVED_CATEGORIES, jsonObject.toString()).apply();
//					}else{
//						if(!isDetached()) Toast.makeText(getActivity(), response.message,Toast.LENGTH_SHORT).show();
//					}
//				}
//			});
//
//			request.setTag(TAG);
//			VolleySingleton.getInstance(getActivity()).getRequestQueue().add(request);

        HttpUrlConnectionApi.get(link, new HttpUrlConnectionApi.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                progressBarSpinner.setVisibility(View.GONE);
                debug("Load categori iklan response: " + jsonObject);
                CategoriesResponse response = gson.fromJson(jsonObject, CategoriesResponse.class);
                if (response == null) return;
                if (response.success) {
                    updateSpinnerList(response);
                    sharedPreferences.edit().putString(PREF_SAVED_CATEGORIES, jsonObject.toString()).apply();
                } else {
                    if (!isDetached())
                        Toast.makeText(getActivity(), response.message, Toast.LENGTH_SHORT).show();
                }
            }
        });

//		AndroidNetworking.get(link).setTag(TAG).build().getAsObject(CategoriesResponse.class, new ParsedRequestListener() {
//			@Override
//			public void onResponse(Object response) {
//				progressBarSpinner.setVisibility(View.GONE);
//				updateSpinnerList((CategoriesResponse) response);
//				sharedPreferences.edit().putString(PREF_SAVED_CATEGORIES, response.toString()).apply();
//			}
//
//			@Override
//			public void onError(ANError anError) {
//				progressBarSpinner.setVisibility(View.GONE);
//				Toast.makeText(getActivity(), ""+anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
//			}
//		});
    }

    protected void setProgressBarVisible(boolean visible) {
        progressBar.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    protected void setTextViewNoDataVisible(boolean visible) {
        textViewNoData.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    protected void setRecyclerViewAdapter(RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onDestroyView() {
        // TODO: Implement this method
        super.onDestroyView();
        VolleySingleton.getInstance(getActivity()).getRequestQueue().cancelAll(TAG);
        AndroidNetworking.cancel(TAG);
    }


    protected void debug(String s) {
        Log.d(getClass().getName(), s);
        //if (BuildConfig.DEBUG) Log.e(getClass().getName(), s);
    }

    public static class CategoriesResponse {
        //http://berkahbsm.com/api/list_mercent.php?aksi=2&userid=MB1000005

        public boolean success = false;
        public String message = "Terjadi kesalahan 63837";
        public List<Category> kategori_merchent = new ArrayList<>();

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }

		/*
		 {"kategori_merchent":
		 [
		 {"id_kategori":"4",
		  "kategori":"Jasa"},
		 
		  {"id_kategori":"3","kategori":"Produk Anak-anak"},
		 {"id_kategori":"2","kategori":"Produk Rumah Tangga"},{"id_kategori":"1","kategori":"Produk Kesehatan"}],"success":true,"message":"berhasil"}
		*/
    }


}
