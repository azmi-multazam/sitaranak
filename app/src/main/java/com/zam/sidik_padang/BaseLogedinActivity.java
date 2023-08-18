package com.zam.sidik_padang;

import android.os.Bundle;

import androidx.annotation.Nullable;

//import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.User;

/**
 * Created by supriyadi on 5/5/17.
 */

public class BaseLogedinActivity extends BaseActivity {

    public User user;
    //protected SharedPreferences sharedPreferences;
    //protected FirebaseFirestore firestore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //firestore = FirebaseFirestore.getInstance();
//		FirebaseFirestore.setLoggingEnabled(true);
        loadUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUser();
    }

    public void loadUser() {
        String userJson = sharedPreferences.getString(Config.PREF_USER_DETAIL_JSON, "");
        if (!userJson.isEmpty()) {
            //debug(getClass(), "userString: " + userJson);
            user = new Gson().fromJson(userJson, User.class);
        }
    }
}
