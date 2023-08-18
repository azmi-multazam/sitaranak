package com.zam.sidik_padang.home.dataternak.vm.list;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HitungTernak {

    @SerializedName("success")
    private boolean success;

    @SerializedName("jumlah_sapi")
    private List<JumlahSapi> jumlahSapi;

    @SerializedName("jumlah_kambing")
    private List<JumlahKambing> jumlahKambing;

    @SerializedName("total_sapi")
    private int totalSapi;

    @SerializedName("data_ternak")
    private List<DataTernak> dataTernak;

    @SerializedName("message")
    private String message;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setJumlahSapi(List<JumlahSapi> jumlahSapi) {
        this.jumlahSapi = jumlahSapi;
    }

    public List<JumlahSapi> getJumlahSapi() {
        return jumlahSapi;
    }

    public void setTotalSapi(int totalSapi) {
        this.totalSapi = totalSapi;
    }

    public int getTotalSapi() {
        return totalSapi;
    }

    public void setJumlahKambing(List<JumlahKambing> jumlahKambing) {
        this.jumlahKambing = jumlahKambing;
    }

    public List<JumlahKambing> getJumlahKambing() {
        return jumlahKambing;
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