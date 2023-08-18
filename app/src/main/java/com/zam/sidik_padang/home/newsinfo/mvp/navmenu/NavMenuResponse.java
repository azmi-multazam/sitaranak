package com.zam.sidik_padang.home.newsinfo.mvp.navmenu;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NavMenuResponse {

    @SerializedName("kategori_berita")
    private List<KategoriBerita> kategoriBerita;

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    public void setKategoriBerita(List<KategoriBerita> kategoriBerita) {
        this.kategoriBerita = kategoriBerita;
    }

    public List<KategoriBerita> getKategoriBerita() {
        return kategoriBerita;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}