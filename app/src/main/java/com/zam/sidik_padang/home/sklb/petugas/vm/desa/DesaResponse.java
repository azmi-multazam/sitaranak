package com.zam.sidik_padang.home.sklb.petugas.vm.desa;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DesaResponse {

    @SerializedName("desa")
    private List<Desa> desa;

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    public void setDesa(List<Desa> desa) {
        this.desa = desa;
    }

    public List<Desa> getDesa() {
        return desa;
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