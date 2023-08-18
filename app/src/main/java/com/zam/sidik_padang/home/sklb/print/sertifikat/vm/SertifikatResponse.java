package com.zam.sidik_padang.home.sklb.print.sertifikat.vm;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SertifikatResponse {

    @SerializedName("sertifikat_ternak_umum")
    private List<SertifikatTernakUmum> sertifikatTernakUmum;

    @SerializedName("sertifikat_ternak_petugas")
    private List<SertifikatTernakPetugas> sertifikatTernakPetugas;

    @SerializedName("total_sapi_petugas")
    private int totalSapiPetugas;

    @SerializedName("total_sapi_umum")
    private int totalSapiUmum;

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    public void setSertifikatTernakUmum(List<SertifikatTernakUmum> sertifikatTernakUmum) {
        this.sertifikatTernakUmum = sertifikatTernakUmum;
    }

    public List<SertifikatTernakUmum> getSertifikatTernakUmum() {
        return sertifikatTernakUmum;
    }

    public void setSertifikatTernakPetugas(List<SertifikatTernakPetugas> sertifikatTernakPetugas) {
        this.sertifikatTernakPetugas = sertifikatTernakPetugas;
    }

    public List<SertifikatTernakPetugas> getSertifikatTernakPetugas() {
        return sertifikatTernakPetugas;
    }

    public void setTotalSapiPetugas(int totalSapiPetugas) {
        this.totalSapiPetugas = totalSapiPetugas;
    }

    public int getTotalSapiPetugas() {
        return totalSapiPetugas;
    }

    public void setTotalSapiUmum(int totalSapiUmum) {
        this.totalSapiUmum = totalSapiUmum;
    }

    public int getTotalSapiUmum() {
        return totalSapiUmum;
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