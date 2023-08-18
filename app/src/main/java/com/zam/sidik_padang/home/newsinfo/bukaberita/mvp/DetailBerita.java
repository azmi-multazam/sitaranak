package com.zam.sidik_padang.home.newsinfo.bukaberita.mvp;

import com.google.gson.annotations.SerializedName;

public class DetailBerita {

    @SerializedName("sub_judul")
    private String subJudul;

    @SerializedName("link")
    private String link;

    @SerializedName("id")
    private String id;

    @SerializedName("tanggal")
    private String tanggal;

    @SerializedName("judul")
    private String judul;

    @SerializedName("gambar")
    private String gambar;

    @SerializedName("isi_berita")
    private String isiBerita;

    public void setSubJudul(String subJudul) {
        this.subJudul = subJudul;
    }

    public String getSubJudul() {
        return subJudul;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getJudul() {
        return judul;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getGambar() {
        return gambar;
    }

    public void setIsiBerita(String isiBerita) {
        this.isiBerita = isiBerita;
    }

    public String getIsiBerita() {
        return isiBerita;
    }
}