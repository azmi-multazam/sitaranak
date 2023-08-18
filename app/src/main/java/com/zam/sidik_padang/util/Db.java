package com.zam.sidik_padang.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Db extends SQLiteOpenHelper {

    public static final String TABLE_PROVINSI = "provinsi";
    public static final String TABLE_PROVINSI_NAMA = "nama";
    public static final String TABLE_PROVINSI_ID = "id";


    private static Db instance;

    private Db(Context c) {
        super(c.getApplicationContext(), "db", null, 1);
    }

    public static synchronized Db getInstance(Context c) {
        if (instance == null) instance = new Db(c);
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqlite) {
        sqlite.execSQL("create table if not exists " + TABLE_PROVINSI + "(" +
                TABLE_PROVINSI_ID + " text," +
                TABLE_PROVINSI_NAMA + " text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase p1, int p2, int p3) {
        p1.execSQL("drop table if exists " + TABLE_PROVINSI);
        onCreate(p1);
    }

    public synchronized void saveprovinsi(JsonArray jsonArray) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_PROVINSI, null, null);
        db.beginTransaction();
        if (jsonArray.size() > 0) {
            ContentValues cv;
            for (JsonElement je : jsonArray) {
                if (je != null) {
                    cv = new ContentValues();
                    cv.put(TABLE_PROVINSI_ID, je.getAsJsonObject().get(TABLE_PROVINSI_ID).getAsString());
                    cv.put(TABLE_PROVINSI_NAMA, je.getAsJsonObject().get(TABLE_PROVINSI_NAMA).getAsString());
                    db.insert(TABLE_PROVINSI, null, cv);
                }
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public synchronized List<Map<String, String>> getAllProvinsi() {
        List<Map<String, String>> hasil = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_PROVINSI, null, null, null, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                Map<String, String> map;
                while (!cursor.isAfterLast()) {
                    map = new HashMap<>();
                    map.put(TABLE_PROVINSI_ID, cursor.getString(cursor.getColumnIndex(TABLE_PROVINSI_ID)));
                    map.put(TABLE_PROVINSI_NAMA, cursor.getString(cursor.getColumnIndex(TABLE_PROVINSI_NAMA)));
                    hasil.add(map);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }

        db.close();
        return hasil;
    }
}
