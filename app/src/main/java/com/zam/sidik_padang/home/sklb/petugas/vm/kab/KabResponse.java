package com.zam.sidik_padang.home.sklb.petugas.vm.kab;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class KabResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("kabupaten")
    private List<Kabupaten> kabupaten;

    @SerializedName("message")
    private String message;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setKabupaten(List<Kabupaten> kabupaten) {
        this.kabupaten = kabupaten;
    }

    public List<Kabupaten> getKabupaten() {
        return kabupaten;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}