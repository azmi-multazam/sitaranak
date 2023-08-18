package com.zam.sidik_padang.home.sklb.dataternak.vm;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response {

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
}