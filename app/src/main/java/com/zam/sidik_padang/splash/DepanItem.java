package com.zam.sidik_padang.splash;

import com.google.gson.annotations.SerializedName;

public class DepanItem {

    @SerializedName("id")
    private String id;

    @SerializedName("gambar")
    private String gambar;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getGambar() {
        return gambar;
    }
}