package com.zam.sidik_padang.home.dataternak.detailternak;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.User;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.VolleyStringRequest;

/**
 * Created by supriyadi on 9/10/17.
 */

public class BaseFragment extends Fragment {
    protected User user;
    protected SharedPreferences pref;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String userJson = pref.getString(Config.PREF_USER_DETAIL_JSON, "");
        if (!userJson.isEmpty()) {
            user = new Gson().fromJson(userJson, User.class);
        }
    }

    protected void cancelRequest(String tag) {
        VolleySingleton.getInstance(getActivity()).getRequestQueue().cancelAll(tag);
    }

    protected void addRequest(VolleyStringRequest request, String tag) {
        request.setTag(tag);
        VolleySingleton.getInstance(getActivity()).getRequestQueue().add(request);
    }

    protected void debug(Class<?> cls, String msg) {
        //if (Config.DEBUG)
        Log.d(cls.getName(), msg);
    }
}
