package com.zam.sidik_padang.home.newsinfo.bukaberita.komentar;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class KomentarResponse {

    @SerializedName("total_komentar")
    private Object totalKomentar;

    @SerializedName("success")
    private boolean success;

    @SerializedName("komentar_berita")
    private List<KomentarBerita> komentarBerita;

    @SerializedName("message")
    private String message;

    public void setTotalKomentar(Object totalKomentar) {
        this.totalKomentar = totalKomentar;
    }

    public Object getTotalKomentar() {
        return totalKomentar;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setKomentarBerita(List<KomentarBerita> komentarBerita) {
        this.komentarBerita = komentarBerita;
    }

    public List<KomentarBerita> getKomentarBerita() {
        return komentarBerita;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}