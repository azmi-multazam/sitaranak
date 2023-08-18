package com.zam.sidik_padang.home.dataternak.vm.list;

import com.google.gson.annotations.SerializedName;

public class JumlahKambing {

    @SerializedName("keterangan")
    private String keterangan;

    @SerializedName("jumlah")
    private int jumlah;

    @SerializedName("kode_ternak")
    private int kodeTernak;

    @SerializedName("id")
    private String id;

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setKodeTernak(int kodeTernak) {
        this.kodeTernak = kodeTernak;
    }

    public int getKodeTernak() {
        return kodeTernak;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}