package com.zam.sidik_padang.home.sklb.petugas.vm.kec;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class KecResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("kecamatan")
    private List<Kecamatan> kecamatan;

    @SerializedName("message")
    private String message;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setKecamatan(List<Kecamatan> kecamatan) {
        this.kecamatan = kecamatan;
    }

    public List<Kecamatan> getKecamatan() {
        return kecamatan;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}