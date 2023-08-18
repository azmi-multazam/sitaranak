package com.zam.sidik_padang.home.sklb.setting.skor;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Parameter {

    @SerializedName("nama")
    private String nama;

    @SerializedName("kode")
    private String kode;

    @SerializedName("ukuran")
    private List<Ukuran> ukuran;

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNama() {
        return nama;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getKode() {
        return kode;
    }

    public void setUkuran(List<Ukuran> ukuran) {
        this.ukuran = ukuran;
    }

    public List<Ukuran> getUkuran() {
        return ukuran;
    }
}