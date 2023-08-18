package com.zam.sidik_padang.home.sklb.dataternak.vm;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataTernakSklbResponse {

    @SerializedName("total_sapi_petugas")
    private int totalSapiPetugas;

    @SerializedName("total_sapi_umum")
    private int totalSapiUmum;

    @SerializedName("success")
    private boolean success;

    @SerializedName("data_ternak_umum")
    private List<DataTernakUmum> dataTernakUmum;

    @SerializedName("data_ternak_petugas")
    private List<DataTernakPetugas> dataTernakPetugas;

    @SerializedName("message")
    private String message;

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

    public void setDataTernakUmum(List<DataTernakUmum> dataTernakUmum) {
        this.dataTernakUmum = dataTernakUmum;
    }

    public List<DataTernakUmum> getDataTernakUmum() {
        return dataTernakUmum;
    }

    public void setDataTernakPetugas(List<DataTernakPetugas> dataTernakPetugas) {
        this.dataTernakPetugas = dataTernakPetugas;
    }

    public List<DataTernakPetugas> getDataTernakPetugas() {
        return dataTernakPetugas;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}