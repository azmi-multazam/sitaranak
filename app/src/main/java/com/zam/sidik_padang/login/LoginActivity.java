package com.zam.sidik_padang.login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.SetOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.zam.sidik_padang.BaseActivity;
import com.zam.sidik_padang.MainActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.roodiskusi.Common;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.User;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.VolleyStringRequest;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private final String VOLLEY_TAG = getClass().getName();
    private EditText editTextUserId, editTextPassword;
    private ImageView imageViewLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.activity_login_ButtonLogin).setOnClickListener(this);
        findViewById(R.id.activity_login_ButtonRegister).setOnClickListener(this);
        findViewById(R.id.activity_login_TextViewForgotPassword).setOnClickListener(this);
        editTextUserId = findViewById(R.id.activity_login_EditTextUser);
        editTextPassword = findViewById(R.id.activity_login_EditTextPassword);
        imageViewLogo = findViewById(R.id.activity_login_ImageViewLogo);

        String savedUserId = PreferenceManager.getDefaultSharedPreferences(this).getString(Config.PREF_USER_ID, "");
        if (!savedUserId.isEmpty()) editTextUserId.setText(savedUserId);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.activity_login_ButtonLogin) {
            login();
        } else if (id == R.id.activity_login_ButtonRegister) {
            startActivity(new Intent(this, RegisterActivity.class));
        } else if (id == R.id.activity_login_TextViewForgotPassword) {
            startActivity(new Intent(this, ForgotPasswordActivity.class));
        }
    }

    private void login() {
        String userId = editTextUserId.getText().toString().trim();
        if (userId.isEmpty()) {
            editTextUserId.setError(getString(R.string.enter_user_id));
            editTextUserId.requestFocus();
            return;
        } else if (userId.contains(" ")) {
            editTextUserId.setError(getString(R.string.invalid_user_id));
            editTextUserId.requestFocus();
            return;
        }
        String password = editTextPassword.getText().toString().trim();
        if (password.isEmpty()) {
            editTextPassword.setError(getString(R.string.enter_password));
            editTextPassword.requestFocus();
            return;
        } else if (password.contains(" ")) {
            editTextPassword.setError(getString(R.string.wrong_password));
            editTextPassword.requestFocus();
            return;
        }

        if (!Util.isInternetAvailible(this)) {
            Util.noInternetDialog(this);
            return;
        }

        String url = Config.URL_LOGIN;
        url += "?userid=" + userId;
        url += "&password=" + password;
        debug(getClass(), "Login URL: " + url);
        final Dialog d = ProgressDialog.show(this, null, getString(R.string.please_wait), true, true);
        final VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {

            @Override
            public void onResponse(JsonObject jsonObject) {
                debug(getClass(), "Login. Onresponse: " + jsonObject.toString());

                if (jsonObject.get("success").getAsBoolean()) {
                    JsonArray ja = jsonObject.get("member").getAsJsonArray();
                    if (ja.size() > 0) {
                        JsonObject jsonMember = ja.get(0).getAsJsonObject();
                        Util.saveUserDetail(LoginActivity.this, jsonMember);
                        //
                        final User user = new Gson().fromJson(jsonMember, User.class);
                        ((AlertDialog) d).setMessage("Sebentar lagi...");

                        d.dismiss();
                        Util.saveUserDetail(LoginActivity.this, user);
                        Toast.makeText(LoginActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                        Intent it = new Intent(LoginActivity.this, MainActivity.class);
                        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(it);
                        overridePendingTransition(0, 0);
                        finish();

                        /*
                        FirebaseFirestore.getInstance().collection(Common.FireStore.USERS).document(user.userid).set(user, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    d.dismiss();
                                    Util.saveUserDetail(LoginActivity.this, user);
                                    Toast.makeText(LoginActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                                    Intent it = new Intent(LoginActivity.this, MainActivity.class);
                                    it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(it);
                                    overridePendingTransition(0, 0);
                                    finish();
                                } else {
                                    d.dismiss();
                                    task.getException().printStackTrace();
                                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                         */
                        //

                    }
                } else {
                    d.dismiss();
                    if (isResummed) {
                        JsonElement je = jsonObject.get("message");
                        Util.showDialog(LoginActivity.this, je == null ? getString(R.string.an_error_ocurred) : je.getAsString());
                    }
                }
            }
        });
        d.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface p1) {
                request.cancel();
            }
        });

        request.setTag(VOLLEY_TAG);
        VolleySingleton.getInstance(this).getRequestQueue().add(request);
    }

    @Override
    protected void onResume() {
        // TODO: Implement this method
        super.onResume();

    }

    /*
    @Override
    protected void onStart() {
        // TODO: Implement this method
        super.onStart();
        debug(getClass(), "onResume");
        if (Util.isLogedin(this)) {
            Intent it = new Intent(this, MainActivity.class);
            it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            it.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            //it.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
//			it.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(it);
        } else {
            //retrieveLogo();
        }
    }
     */

    private void retrieveLogo() {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String lastUrlGambar = pref.getString("url_logo", "");
        if (!lastUrlGambar.isEmpty())
            Glide.with(LoginActivity.this).load(lastUrlGambar).crossFade(1000).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageViewLogo);
        if (!Util.isInternetAvailible(this)) return;
        debug(getClass(), "retrieveLogo");
        VolleyStringRequest request = new VolleyStringRequest(Config.URL_LOGO, new VolleyStringRequest.Callback() {

            @Override
            public void onResponse(JsonObject jsonObject) {
                debug(getClass(), "retrieveLogo OnResponse: " + jsonObject.toString());
                if (jsonObject.get("success").getAsBoolean()) {
                    JsonElement je = jsonObject.get("depan");
                    if (!je.isJsonNull()) {
                        JsonArray ja = je.getAsJsonArray();
                        if (ja.size() > 0) {
                            JsonElement e = ja.get(0);
                            String urlGambar = e.getAsJsonObject().get("gambar").getAsString();
                            pref.edit().putString("url_logo", urlGambar).apply();
                            Glide.with(LoginActivity.this).load(urlGambar).crossFade(1000).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageViewLogo);

                        }
                    }
                } else {

                }

            }
        });
        request.setTag(VOLLEY_TAG);
        VolleySingleton.getInstance(this).getRequestQueue().add(request);
    }

    @Override
    protected void onDestroy() {
        VolleySingleton.getInstance(this).getRequestQueue().cancelAll(VOLLEY_TAG);
        super.onDestroy();
    }


}
