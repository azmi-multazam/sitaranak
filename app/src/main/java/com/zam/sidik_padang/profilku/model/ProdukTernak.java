package com.zam.sidik_padang.profilku.model;

import com.google.gson.annotations.SerializedName;

public class ProdukTernak {

    @SerializedName("produk")
    private String produk;

    @SerializedName("id")
    private String id;

    public void setProduk(String produk) {
        this.produk = produk;
    }

    public String getProduk() {
        return produk;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}