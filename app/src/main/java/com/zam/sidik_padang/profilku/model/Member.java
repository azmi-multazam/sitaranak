package com.zam.sidik_padang.profilku.model;

import com.google.gson.annotations.SerializedName;

public class Member {

    @SerializedName("nama")
    private String nama;

    @SerializedName("level_sesi")
    private String levelSesi;

    @SerializedName("foto")
    private String foto;

    @SerializedName("level")
    private String level;

    @SerializedName("jenis")
    private String jenis;

    @SerializedName("id")
    private String id;

    @SerializedName("userid")
    private String userid;

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNama() {
        return nama;
    }

    public void setLevelSesi(String levelSesi) {
        this.levelSesi = levelSesi;
    }

    public String getLevelSesi() {
        return levelSesi;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getFoto() {
        return foto;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getJenis() {
        return jenis;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserid() {
        return userid;
    }
}