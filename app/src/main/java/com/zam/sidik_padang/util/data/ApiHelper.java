package com.zam.sidik_padang.util.data;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.PreferenceManager;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.ActionMenuView.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

//import com.zam.sidik_padang.BuildConfig;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.User;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.VolleyStringRequest;

public class ApiHelper {
    private static ApiHelper helper;

    private Context c;
    private User user;

    private ApiHelper(Context c) {
        this.c = c;
        String userJson = PreferenceManager.getDefaultSharedPreferences(c).getString(Config.PREF_USER_DETAIL_JSON, "");
        if (!userJson.isEmpty()) {
            user = new Gson().fromJson(userJson, User.class);
        }
    }

    public static ApiHelper getHelper(Context c) {
        if (null == helper) helper = new ApiHelper(c.getApplicationContext());
        return helper;
    }

    public static void cekUlangTagian(final Context context, String tagihanId, final OnDoneListener listener) {
        if (!Util.isInternetAvailible(context)) {
            Util.noInternetDialog(context);
            return;
        }

        final StringBuilder url = new StringBuilder();
        url.append(Config.URL_PROSES + "aksi=6");
        url.append("&id=" + tagihanId);

        url.append("&userid=" + DataHelper.getDataHelper(context).getUser());
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        TextInputLayout til = new TextInputLayout(context);
        int p = (int) (context.getResources().getDisplayMetrics().density * 16f);
        til.setPadding(p, 0, p, 0);
        final TextInputEditText editText = new TextInputEditText(context);
        til.addView(editText);
        editText.setHint("PIN/Token");
        editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        //editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        LinearLayout ll = new LinearLayout(context);
        ll.addView(til, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        builder.setView(ll);
        builder.setNegativeButton("Batal", null);
        builder.setPositiveButton("Cek Ulang", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String pin = editText.getText().toString().trim();
                if (pin.isEmpty()) {
                    editText.setError("Harus diisi");
                    editText.requestFocus();
                    return;
                }
                url.append("&token=" + pin);
                debug("Cek ulang tagihan " + url.toString());
                final Dialog d = ProgressDialog.show(context, null, "Harap tunggu...", true, false);
                VolleyStringRequest request = new VolleyStringRequest(url.toString(), new VolleyStringRequest.Callback() {
                    @Override
                    public void onResponse(JsonObject jsonObject) {
                        d.dismiss();
                        debug("Cek ulang tagihan response=" + jsonObject);
                        listener.onDone(jsonObject.get("success").getAsBoolean());
                        if (!jsonObject.get("success").getAsBoolean()) Toast.makeText(context,
                                jsonObject.get("message").getAsString(), Toast.LENGTH_SHORT
                        ).show();
                    }
                });

                VolleySingleton.getInstance(context).getRequestQueue().add(request);
            }
        });
        builder.show();
    }

    public static void bayarTagihan(final Context context, String tagihanId, final OnDoneListener listener) {
        if (!Util.isInternetAvailible(context)) {
            Util.noInternetDialog(context);
            return;
        }

        final StringBuilder url = new StringBuilder();
        url.append(Config.URL_PROSES + "aksi=4");
        url.append("&id=" + tagihanId);
        url.append("&userid=" + DataHelper.getDataHelper(context).getUser().userid);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        TextInputLayout til = new TextInputLayout(context);
        int p = (int) (context.getResources().getDisplayMetrics().density * 16f);
        til.setPadding(p, 0, p, 0);
        final TextInputEditText editText = new TextInputEditText(context);
        til.addView(editText);
        editText.setHint("PIN/Token");
        editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        LinearLayout ll = new LinearLayout(context);
        ll.addView(til, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        builder.setView(ll);
        builder.setNegativeButton("Batal", null);
        builder.setPositiveButton("Bayar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String pin = editText.getText().toString().trim();
                if (pin.isEmpty()) {
                    editText.setError("Harus diisi");
                    editText.requestFocus();
                    return;
                }
                url.append("&token=" + pin);
                debug("Bayar tagihan url=" + url.toString());
                final Dialog d = ProgressDialog.show(context, null, "Harap tunggu...", true, false);
                VolleyStringRequest request = new VolleyStringRequest(url.toString(), new VolleyStringRequest.Callback() {
                    @Override
                    public void onResponse(JsonObject jsonObject) {
                        d.dismiss();
                        debug("Bayar tagihan response=" + jsonObject);
                        listener.onDone(jsonObject.get("success").getAsBoolean());
                        new AlertDialog.Builder(context)
                                .setMessage(jsonObject.get("message").getAsString())
                                .show();
                    }
                });

                VolleySingleton.getInstance(context).getRequestQueue().add(request);
            }
        });
        builder.show();
    }

    static void debug(String s) {
        Log.d(ApiHelper.class.getName(), s);
        //if (BuildConfig.DEBUG) Log.e(ApiHelper.class.getName(), s);
    }

    public synchronized void getJenisProdukPulsa(final OnDoneListener listener) {
        String url = Config.URL_PPOB + "userid=" + user.userid + "&aksi=1";
        debug("get produk pulsa url= " + url);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {

            @Override
            public void onResponse(JsonObject jsonObject) {
                debug("get Jenis produk response= " + jsonObject);

                if (jsonObject.get("success").getAsBoolean()) {
                    DataHelper.getDataHelper(c).saveProdukPulsa(jsonObject.get("produk_pulsa").getAsJsonArray());
                    listener.onDone(true);
                } else listener.onDone(false);
            }
        });
        VolleySingleton.getInstance(c).getRequestQueue().add(request);
		/*
		 ambil kode game : http://bungara.com/api/ppob.php?userid=100002&aksi=5&code=GA
		*/
    }

    public synchronized void getProdukPaketNet(final OnDoneListener listener) {
        String url = Config.URL_PPOB + "userid=" + user.userid + "&aksi=3";
        debug("get produk paket net url= " + url);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {

            @Override
            public void onResponse(JsonObject jsonObject) {
                debug("get Jenis produk pket net response= " + jsonObject);

                if (jsonObject.get("success").getAsBoolean()) {
                    DataHelper.getDataHelper(c).saveProdukPaketNet(jsonObject.get("produk_paket_net").getAsJsonArray());
                    listener.onDone(true);
                } else listener.onDone(false);
            }
        });
        VolleySingleton.getInstance(c).getRequestQueue().add(request);
		/*
		 ambil kode game : http://bungara.com/api/ppob.php?userid=100002&aksi=5&code=GA
		 */
    }

    public synchronized void getJenisVoucherGame(final OnDoneListener listener) {
        String url = Config.URL_PPOB + "userid=" + user.userid + "&aksi=4";
        debug("get janis voucher game url= " + url);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {

            @Override
            public void onResponse(JsonObject jsonObject) {
                debug("get Jenis voucher game response= " + jsonObject);

                if (jsonObject.get("success").getAsBoolean()) {
                    DataHelper.getDataHelper(c).saveJenisVoucherGame(jsonObject.get("produk_game").getAsJsonArray());
                    listener.onDone(true);
                } else listener.onDone(false);
            }
        });
        VolleySingleton.getInstance(c).getRequestQueue().add(request);
		/*
		 ambil kode game : http://bungara.com/api/ppob.php?userid=100002&aksi=5&code=GA
		*/
    }

    public synchronized void getJenisPembayaran(final OnDoneListener listener) {
        String url = Config.URL_PPOB + "userid=" + user.userid + "&aksi=6";
        debug("get janis pembayaran url= " + url);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {

            @Override
            public void onResponse(JsonObject jsonObject) {
                debug("get Jenis pembayaran response= " + jsonObject);

                if (jsonObject.get("success").getAsBoolean()) {
                    DataHelper.getDataHelper(c).saveJenisPembayaran(jsonObject.get("ppob_bayar").getAsJsonArray());
                    listener.onDone(true);
                } else listener.onDone(false);
            }
        });
        VolleySingleton.getInstance(c).getRequestQueue().add(request);
		/*
		 ambil kode game : http://bungara.com/api/ppob.php?userid=100002&aksi=5&code=GA
		*/
    }

    public synchronized void getProdukPLNs(final OnDoneListener listener) {
        String url = Config.URL_PPOB + "userid=" + user.userid + "&aksi=2&code=pln";
        debug("get produk PLN= " + url);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {

            @Override
            public void onResponse(JsonObject jsonObject) {
                debug("get Produk PLN response= " + jsonObject);

                if (jsonObject.get("success").getAsBoolean()) {
                    DataHelper.getDataHelper(c).saveProdukPLN(jsonObject.get("produk_pilihan").getAsJsonArray());
                    listener.onDone(true);
                } else listener.onDone(false);
            }
        });
        VolleySingleton.getInstance(c).getRequestQueue().add(request);
		/*
		 http://bungara.com/api/ppob.php?userid=100002&aksi=2&code=PLN

		*/
    }

    public static interface OnDoneListener {
        void onDone(boolean success);
    }
}
