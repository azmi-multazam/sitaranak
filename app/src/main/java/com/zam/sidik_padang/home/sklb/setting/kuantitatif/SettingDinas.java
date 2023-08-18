package com.zam.sidik_padang.home.sklb.setting.kuantitatif;

import com.google.gson.annotations.SerializedName;

public class SettingDinas {

    @SerializedName("nama")
    public String nama;

    @SerializedName("nip")
    public String nip;

    @SerializedName("jabatan")
    public String jabatan;

    @SerializedName("ttd")
    public String ttd;

    @SerializedName("no_surat")
    public String noSurat;

    @SerializedName("tanggal_surat")
    public String tglSurat;

    @SerializedName("catatan")
    public String catatan;

    @SerializedName("userid")
    private String userId;

    public SettingDinas(String nama, String nip, String jabatan, String ttd, String noSurat, String tglSurat, String catatan) {
        this.nama = nama;
        this.nip = nip;
        this.jabatan = jabatan;
        this.ttd = ttd;
        this.noSurat = noSurat;
        this.tglSurat = tglSurat;
        this.catatan = catatan;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}