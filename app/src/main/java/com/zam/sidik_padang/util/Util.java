package com.zam.sidik_padang.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.EnumMap;
import java.util.Map;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.login.LoginActivity;
import io.paperdb.Paper;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

/**
 * Created by supriyadi on 2/6/17.
 */

public class Util {
    public static AlertDialog showProgressDialog(Context context, String message, boolean cancelable) {

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null, false);
        if (message != null)
            ((TextView) view.findViewById(R.id.dialog_progress_TextView)).setText(message);
        return new AlertDialog.Builder(context).setView(view).setCancelable(cancelable).show();
    }

    public static String toUrlEncoded(String src) {
        try {
            return URLEncoder.encode(src, "utf-8");
        } catch (UnsupportedEncodingException e) {
            Log.e("ferryirawan", e.getMessage());
            return src;
        }
    }

    public static String decodeUrl(String src) {
        try {
            return URLDecoder.decode(src, "utf-8");
        } catch (UnsupportedEncodingException e) {
            Log.e("ferryirawan", e.getMessage());
            return src;
        }
    }

    public static void saveUserDetail(Context context, JsonObject jsonUser) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit().putString(Config.PREF_USER_DETAIL_JSON, jsonUser.toString());
        String userId = "";
        JsonElement je = jsonUser.get("userid");
        if (je != null) userId = je.getAsString();
        editor.putString(Config.PREF_USER_ID, userId);
        editor.apply();

    }

    public static void saveUserDetail(Context context, User user) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit().putString(com.zam.sidik_padang.roodiskusi.Config.PREF_USER_DETAIL_JSON, new Gson().toJson(user));
        editor.putString(com.zam.sidik_padang.roodiskusi.Config.PREF_USER_DETAIL_JSON, new Gson().toJson(user));
        editor.apply();

    }

    public static User getSavedUser(Context c) {
        String s = PreferenceManager.getDefaultSharedPreferences(c).getString(com.zam.sidik_padang.roodiskusi.Config.PREF_USER_DETAIL_JSON, "");
        if (!s.startsWith("{")) return null;
        return new Gson().fromJson(s, User.class);
    }

    public static boolean isLogedin(Context context) {
        String jsonUserString = PreferenceManager.getDefaultSharedPreferences(context).getString(Config.PREF_USER_DETAIL_JSON, "");
        JsonElement je = new Gson().fromJson(jsonUserString, JsonElement.class);
        return je != null;
    }


    public static boolean isOffline(Context c) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo == null || !activeNetworkInfo.isConnectedOrConnecting();
    }


    public static boolean isInternetAvailible(Context c) {
        if (c == null) return false;
        NetworkInfo info = ((ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    public static void noInternetDialog(Context c) {
        AlertDialog.Builder b = new AlertDialog.Builder(c);
        b.setMessage(R.string.no_internet_connection);
        b.setPositiveButton(android.R.string.ok, null);
        b.show();
    }

    public static void anErrorOcurredDialog(Context c) {
        AlertDialog.Builder b = new AlertDialog.Builder(c);
        b.setMessage(R.string.an_error_ocurred);
        b.setPositiveButton(android.R.string.ok, null);
        b.show();
    }

    public static void dialogSesiBerakhir(final Context context) {
        AlertDialog.Builder b = new AlertDialog.Builder(context);
        b.setMessage(R.string.session_expired);
        b.setCancelable(false);
        b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                logout(context);
                //Intent intent = new Intent(context, LoginActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //context.startActivity(intent);
                //((AppCompatActivity)context).finish();
            }
        }).show();
    }

    public static void logout(Context context) {
        SharedPreferences.Editor prefEditor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        prefEditor.remove(Config.PREF_USER_DETAIL_JSON);
        prefEditor.remove(Config.PREF_PROFIL_TERSIMPAN);
        Paper.book().delete(Config.PREF_PROFIL_TERSIMPAN);
        prefEditor.apply();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
//		if (context instanceof AppCompatActivity) ((AppCompatActivity)context).finish();
    }

    public static void showDialog(Context c, String m) {
        new AlertDialog.Builder(c).setMessage(m).setPositiveButton(android.R.string.ok, null).show();
    }

    public static Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }

        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

    public static Bitmap loadBitmapFromView(View v) {
        Bitmap b;
        if (v.getMeasuredHeight() <= 0) {
            int specWidth = View.MeasureSpec.makeMeasureSpec(0 /* any */, View.MeasureSpec.UNSPECIFIED);
            v.measure(specWidth, specWidth);
            //int questionWidth = v.getMeasuredWidth();
            //v.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            v.draw(c);
        } else {
            b = Bitmap.createBitmap(v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
            v.draw(c);
        }
        return b;
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 98, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }
}
