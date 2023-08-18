package com.zam.sidik_padang.util.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by supriyadi on 6/27/17.
 */

class Db extends SQLiteOpenHelper {

    static final String
            TABLE_USER = "user",
            TABLE_PRODUK_PULSA = "produk_pulsa",
            TABLE_PRODUK_GAME = "produk_game",
            TABLE_PRODUK_PLN = "produk_pln",
            TABLE_JENIS_PEMBAYARAN = "jenis_pembayaran",
            TABLE_PRODUK_NET = "produk_net";
    private final Context context;
//			TABLE_PHISTORY_PULSA="history_pulsa";


    Db(Context context) {
        super(context, "data.db", null, 6);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL("create table if not exists " + TABLE_USER + "(" +
                "user_json text not null )"
        );

        db.execSQL("create table if not exists " + TABLE_PRODUK_NET + "(" +
                "produk_paket_net text not null )"
        );

        db.execSQL("create table if not exists " + TABLE_PRODUK_GAME + "(" +
                "produk_game text not null )"
        );

        db.execSQL("create table if not exists " + TABLE_PRODUK_PULSA + "(" +
                "produk_pulsa text not null )"
        );

        db.execSQL("create table if not exists " + TABLE_JENIS_PEMBAYARAN + "(" +
                "jenis_pembayaran text not null )"
        );

        db.execSQL("create table if not exists " + TABLE_PRODUK_PLN + "(" +
                "produk_pln text not null )"
        );
    }

    @SuppressLint("ApplySharedPref")
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_USER);
        db.execSQL("drop table if exists " + TABLE_PRODUK_NET);
        db.execSQL("drop table if exists " + TABLE_PRODUK_PULSA);
        db.execSQL("drop table if exists " + TABLE_PRODUK_PLN);
        db.execSQL("drop table if exists " + TABLE_PRODUK_GAME);
        db.execSQL("drop table if exists " + TABLE_JENIS_PEMBAYARAN);
        onCreate(db);
    }

}
