package com.zam.sidik_padang.home.sklb.petugas.vm.jeniskel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JenisKelResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("Kelamin")
    private List<Kelamin> kelamin;

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

    public void setKelamin(List<Kelamin> kelamin) {
        this.kelamin = kelamin;
    }

    public List<Kelamin> getKelamin() {
        return kelamin;
    }
}