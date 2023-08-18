package com.zam.sidik_padang.splash;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SplashResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("depan")
    private List<DepanItem> depan;

    @SerializedName("message")
    private String message;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setDepan(List<DepanItem> depan) {
        this.depan = depan;
    }

    public List<DepanItem> getDepan() {
        return depan;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}