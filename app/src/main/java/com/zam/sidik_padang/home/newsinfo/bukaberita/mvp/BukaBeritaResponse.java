package com.zam.sidik_padang.home.newsinfo.bukaberita.mvp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.zam.sidik_padang.home.memberhome.mvp.Berita;

public class BukaBeritaResponse {

    @SerializedName("berita_terkait")
    private List<Berita> beritaTerkait;

    @SerializedName("success")
    private boolean success;

    @SerializedName("baca_juga")
    private List<Berita> bacaJuga;

    @SerializedName("detail_berita")
    private List<DetailBerita> detailBerita;

    @SerializedName("message")
    private String message;

    public void setBeritaTerkait(List<Berita> beritaTerkait) {
        this.beritaTerkait = beritaTerkait;
    }

    public List<Berita> getBeritaTerkait() {
        return beritaTerkait;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setBacaJuga(List<Berita> bacaJuga) {
        this.bacaJuga = bacaJuga;
    }

    public List<Berita> getBacaJuga() {
        return bacaJuga;
    }

    public void setDetailBerita(List<DetailBerita> detailBerita) {
        this.detailBerita = detailBerita;
    }

    public List<DetailBerita> getDetailBerita() {
        return detailBerita;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}