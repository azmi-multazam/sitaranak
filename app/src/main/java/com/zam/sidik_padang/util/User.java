package com.zam.sidik_padang.util;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.Serializable;

public class User implements Serializable, Comparable<User> {
    public static final String
            ID = "id", KELOMPOK = "kelompok", KELOMPOK_TERNAK = "kelompok_ternak",
            USER_ID = "userid", NAMA = "nama", LEVEL = "level", LEVEL_SESI = "level_sesi", FOTO = "foto", PROVINSI = "provinsi";

    public static final String USERS = "users";

    public int id = -1, kelompok = 1, level_sesi = 4;
    public String userid = "", nama = "", level = "", foto = "", provinsi = "", kelompok_ternak = "";

    public String fcm;

//	public Map<String, Object> groups = new HashMap<>();

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @Override
    public int compareTo(@NonNull User u) {
        return nama.compareToIgnoreCase(u.nama);
    }
}

/*
{"member":[{"id":"5",
			"userid":"KS1000005",
			"nama":"Hartati2",
			"level":"Peternak",
			"foto":"",
			"kelompok":"2",
			"kelompok_ternak":"8",
			"provinsi":"12920"}],

			"success":true,"message":"login berhasil"}

			"member":[{"id":"16",
					   "userid":"KS1000007",
					   "nama":"Supriyadi",
					   "level":"Peternak",
					   "level_sesi":"1",
					   "foto":"http://e-rekording.com/foto_siswa/20170921165620-foto_peternak.jpg",
					   "kelompok":"1",
					   "kelompok_ternak":"",
					   "provinsi":"26141"}],

					   "success":true,"message":"login berhasil"}
*/
