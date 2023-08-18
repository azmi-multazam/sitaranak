package com.zam.sidik_padang.login;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.JsonObject;

import com.zam.sidik_padang.BaseActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.VolleyStringRequest;

/**
 * Created by supriyadi on 5/7/17.
 */

public class ForgotPasswordActivity extends BaseActivity {

    private static final String VOLLEY_TAG = ForgotPasswordActivity.class.getName();
    private EditText editTextUserId, editTextNomorHp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        editTextUserId = (EditText) findViewById(R.id.activity_reset_sandi_EditTextUser);
        editTextNomorHp = (EditText) findViewById(R.id.activity_reset_sandi_EditTexNomorHp);
        findViewById(R.id.activity_reset_sandi_Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }

    private void validate() {
        String userId = editTextUserId.getText().toString().trim();
        if (userId.isEmpty()) {
            editTextUserId.requestFocus();
            editTextUserId.setError("Harus diisi");
            return;
        }

        String hp = editTextNomorHp.getText().toString().trim();
        if (hp.isEmpty()) {
            editTextNomorHp.requestFocus();
            editTextNomorHp.setError("Harus diisi");
            return;
        }

        if (!Util.isInternetAvailible(this)) {
            Util.noInternetDialog(this);
            return;
        }

        String url = Config.URL_PROFILE + "?aksi=4&userid=" + userId + "&hp=" + hp;
        debug(getClass(), "reset sandi url=" + url);
        final Dialog dialog = ProgressDialog.show(this, null, "Harap tunggu...", true, false);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                debug(getClass(), "reset sandi response =" + jsonObject);
                dialog.dismiss();
                Util.showDialog(ForgotPasswordActivity.this, jsonObject.get("message").getAsString());

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
