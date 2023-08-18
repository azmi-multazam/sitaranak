package com.zam.sidik_padang.home.sklb.petugas.vm.pemilik;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PemilikResponse {

    @SerializedName("pemilik_ternak")
    private List<PemilikTernak> pemilikTernak;

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    public List<PemilikTernak> getPemilikTernak() {
        return pemilikTernak;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}