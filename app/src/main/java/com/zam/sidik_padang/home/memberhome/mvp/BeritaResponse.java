package com.zam.sidik_padang.home.memberhome.mvp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BeritaResponse {
    @SerializedName("headline")
    @Expose
    private List<Headline> headline = null;
    @SerializedName("berita")
    @Expose
    private List<Berita> berita = null;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;

    public List<Headline> getHeadline() {
        return headline;
    }

    public void setHeadline(List<Headline> headline) {
        this.headline = headline;
    }

    public List<Berita> getBerita() {
        return berita;
    }

    public void setBerita(List<Berita> berita) {
        this.berita = berita;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
