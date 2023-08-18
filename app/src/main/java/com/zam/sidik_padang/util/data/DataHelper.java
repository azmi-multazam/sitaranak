package com.zam.sidik_padang.util.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

import com.zam.sidik_padang.home.ppob.internet.ProdukPaketNet;
import com.zam.sidik_padang.home.ppob.tokenpln.ProdukPLN;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.User;

public class DataHelper {
    private static DataHelper dataHelper;
    private Db db;
    private Context context;

    private DataHelper(Context c) {
        db = new Db(c.getApplicationContext());
        this.context = c;
    }

    public static DataHelper getDataHelper(Context c) {
        if (null == dataHelper) dataHelper = new DataHelper(c.getApplicationContext());
        return dataHelper;
    }

    public synchronized void saveUserJson(String json) {
        SQLiteDatabase database = db.getWritableDatabase();
        database.delete(Db.TABLE_USER, null, null);
        ContentValues cv = new ContentValues();
        cv.put("user_json", json);
        database.insert(Db.TABLE_USER, null, cv);
    }

    public synchronized User getUser() {
        User user = null;
//		Log.e(getClass().getName(), "context: " + context.getPackageName());
        String userJson = PreferenceManager.getDefaultSharedPreferences(context).getString(Config.PREF_USER_DETAIL_JSON, "");
        if (!userJson.isEmpty()) {
            user = new Gson().fromJson(userJson, User.class);
        }
        return user;
    }

    public synchronized void deleteUser() {
        SQLiteDatabase database = db.getReadableDatabase();
        database.delete(Db.TABLE_USER, null, null);

    }

    public synchronized void saveProdukPulsa(JsonArray array) {
        if (array == null || array.size() == 0) return;
        SQLiteDatabase database = db.getWritableDatabase();
        database.delete(Db.TABLE_PRODUK_PULSA, null, null);
        database.beginTransaction();
        ContentValues cv;
        for (JsonElement jsonElement : array) {
            cv = new ContentValues();
            cv.put("produk_pulsa", jsonElement.toString());
            database.insertOrThrow(Db.TABLE_PRODUK_PULSA, null, cv);
        }
        database.setTransactionSuccessful();
        database.endTransaction();
//		database.close();

    }

    public synchronized void saveJenisPembayaran(JsonArray array) {
        if (array == null || array.size() == 0) return;
        SQLiteDatabase database = db.getWritableDatabase();
        database.delete(Db.TABLE_JENIS_PEMBAYARAN, null, null);
        database.beginTransaction();
        ContentValues cv;
        for (JsonElement jsonElement : array) {
            cv = new ContentValues();
            cv.put("jenis_pembayaran", jsonElement.toString());
            database.insertOrThrow(Db.TABLE_JENIS_PEMBAYARAN, null, cv);
        }
        database.setTransactionSuccessful();
        database.endTransaction();
//		database.close();

    }


    public synchronized List<ProdukPulsa> getAllProdukPulsa() {
        List<ProdukPulsa> list = new ArrayList<>();
        SQLiteDatabase database = db.getReadableDatabase();
        Cursor cursor = database.query(Db.TABLE_PRODUK_PULSA, null, null, null, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                Gson gson = new Gson();
                ProdukPulsa pp;
                while (!cursor.isAfterLast()) {
                    pp = gson.fromJson(cursor.getString(0), ProdukPulsa.class);
                    list.add(pp);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
        return list;
    }

    public synchronized List<JenisPembayaran> getAllJenisPembayaran() {
        List<JenisPembayaran> list = new ArrayList<>();
        SQLiteDatabase database = db.getReadableDatabase();
        Cursor cursor = database.query(Db.TABLE_JENIS_PEMBAYARAN, null, null, null, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                Gson gson = new Gson();
                JenisPembayaran jp;
                while (!cursor.isAfterLast()) {
                    jp = gson.fromJson(cursor.getString(0), JenisPembayaran.class);
                    list.add(jp);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
        return list;
    }

    public synchronized void saveProdukPLN(JsonArray array) {
        if (array == null || array.size() == 0) return;
        SQLiteDatabase database = db.getWritableDatabase();
        database.delete(Db.TABLE_PRODUK_PLN, null, null);
        database.beginTransaction();
        ContentValues cv;
        for (JsonElement jsonElement : array) {
            cv = new ContentValues();
            cv.put("produk_pln", jsonElement.toString());
            database.insertOrThrow(Db.TABLE_PRODUK_PLN, null, cv);
        }
        database.setTransactionSuccessful();
        database.endTransaction();
//		database.close();

    }

    public synchronized List<ProdukPLN> getAllProdukPLN() {
        List<ProdukPLN> list = new ArrayList<>();
        SQLiteDatabase database = db.getReadableDatabase();
        Cursor cursor = database.query(Db.TABLE_PRODUK_PLN, null, null, null, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                Gson gson = new Gson();
                ProdukPLN pp;
                while (!cursor.isAfterLast()) {
                    pp = gson.fromJson(cursor.getString(0), ProdukPLN.class);
                    list.add(pp);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
        return list;
    }

    public synchronized void saveJenisVoucherGame(JsonArray array) {
        if (array == null || array.size() == 0) return;
        SQLiteDatabase database = db.getWritableDatabase();
        database.delete(Db.TABLE_PRODUK_GAME, null, null);
        database.beginTransaction();
        ContentValues cv;
        for (JsonElement jsonElement : array) {
            cv = new ContentValues();
            cv.put("produk_game", jsonElement.toString());
            database.insertOrThrow(Db.TABLE_PRODUK_GAME, null, cv);
        }
        database.setTransactionSuccessful();
        database.endTransaction();
//		database.close();

    }

    public synchronized List<VoucherGame> getJenisVoucherGame() {
        List<VoucherGame> list = new ArrayList<>();
        SQLiteDatabase database = db.getReadableDatabase();
        Cursor cursor = database.query(Db.TABLE_PRODUK_GAME, null, null, null, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                Gson gson = new Gson();
                VoucherGame vg;
                while (!cursor.isAfterLast()) {
                    vg = gson.fromJson(cursor.getString(0), VoucherGame.class);
                    list.add(vg);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
        return list;
    }

    public synchronized void saveProdukPaketNet(JsonArray array) {
        if (array == null || array.size() == 0) return;
        SQLiteDatabase database = db.getWritableDatabase();
        database.delete(Db.TABLE_PRODUK_NET, null, null);
        database.beginTransaction();
        ContentValues cv;
        for (JsonElement jsonElement : array) {
            cv = new ContentValues();
            cv.put("produk_paket_net", jsonElement.toString());
            database.insertOrThrow(Db.TABLE_PRODUK_NET, null, cv);
        }
        database.setTransactionSuccessful();
        database.endTransaction();
//		database.close();

    }

    public synchronized List<ProdukPaketNet> getProdukPaketNet() {
        List<ProdukPaketNet> list = new ArrayList<>();
        SQLiteDatabase database = db.getReadableDatabase();
        Cursor cursor = database.query(Db.TABLE_PRODUK_NET, null, null, null, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                Gson gson = new Gson();
                ProdukPaketNet pNet;
                while (!cursor.isAfterLast()) {
                    pNet = gson.fromJson(cursor.getString(0), ProdukPaketNet.class);
                    list.add(pNet);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
        return list;
    }
}
