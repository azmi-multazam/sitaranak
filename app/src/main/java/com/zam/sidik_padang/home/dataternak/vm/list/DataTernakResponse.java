package com.zam.sidik_padang.home.dataternak.vm.list;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataTernakResponse {

    @SerializedName("total_sapi")
    private int totalSapi;

    @SerializedName("success")
    private boolean success;

    @SerializedName("data_ternak")
    private List<DataTernak> dataTernak;

    @SerializedName("message")
    private String message;

    public void setTotalSapi(int totalSapi) {
        this.totalSapi = totalSapi;
    }

    public int getTotalSapi() {
        return totalSapi;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setDataTernak(List<DataTernak> dataTernak) {
        this.dataTernak = dataTernak;
    }

    public List<DataTernak> getDataTernak() {
        return dataTernak;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}