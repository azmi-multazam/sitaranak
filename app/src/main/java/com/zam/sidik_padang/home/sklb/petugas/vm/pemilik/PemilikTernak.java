package com.zam.sidik_padang.home.sklb.petugas.vm.pemilik;

import com.google.gson.annotations.SerializedName;

public class PemilikTernak {

    @SerializedName("nama")
    private String nama;

    @SerializedName("pemilik_ternak")
    private String pemilikTernak;

    @SerializedName("hp")
    private String hp;

    @SerializedName("id")
    private String id;

    @SerializedName("alamat")
    private String alamat;

    public String getNama() {
        return nama;
    }

    public String getPemilikTernak() {
        return pemilikTernak;
    }

    public String getHp() {
        return hp;
    }

    public String getId() {
        return id;
    }

    public String getAlamat() {
        return alamat;
    }
}