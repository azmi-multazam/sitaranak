package com.zam.sidik_padang.home.sklb;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.User;

public class SklbBaseFragment extends Fragment {
    protected User user;
    protected SharedPreferences pref;
    private Dialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String userJson = pref.getString(Config.PREF_USER_DETAIL_JSON, "");
        if (!userJson.isEmpty()) {
            user = new Gson().fromJson(userJson, User.class);
        }
    }

    protected void debug(Class<?> cls, String msg) {
        //if (Config.DEBUG)
        Log.d(cls.getName(), msg);
    }

    protected void showProgress(String title, String msg) {
        dialog = ProgressDialog.show(requireContext(), title, msg, true, false);
    }

    protected void hideProgress() {
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }
}
