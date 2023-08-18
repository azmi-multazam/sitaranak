package com.zam.sidik_padang.home.newsinfo.bukaberita.komentar;

import com.google.gson.annotations.SerializedName;

public class KomentarBerita {

    @SerializedName("iduser")
    private String iduser;

    @SerializedName("foto")
    private String foto;

    @SerializedName("nama")
    private String nama;

    @SerializedName("komentar")
    private String komentar;

    @SerializedName("id")
    private String id;

    @SerializedName("tanggal")
    private String tanggal;

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getIduser() {
        return iduser;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getFoto() {
        return foto;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNama() {
        return nama;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getTanggal() {
        return tanggal;
    }
}