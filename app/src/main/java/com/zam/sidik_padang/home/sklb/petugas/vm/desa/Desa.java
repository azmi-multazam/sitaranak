package com.zam.sidik_padang.home.sklb.petugas.vm.desa;

import com.google.gson.annotations.SerializedName;

public class Desa {

    @SerializedName("nama")
    private String nama;

    @SerializedName("id")
    private String id;

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNama() {
        return nama;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}