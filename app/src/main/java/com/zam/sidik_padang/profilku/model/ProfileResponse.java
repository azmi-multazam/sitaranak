package com.zam.sidik_padang.profilku.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProfileResponse {

    @SerializedName("jenis_usaha")
    private List<JenisUsaha> jenisUsaha;

    @SerializedName("success")
    private boolean success;

    @SerializedName("member")
    private List<Member> member;

    @SerializedName("lihat_profil")
    private List<LihatProfil> lihatProfil;

    @SerializedName("produk_ternak")
    private List<ProdukTernak> produkTernak;

    @SerializedName("jenis_komoditas")
    private List<JenisKomoditas> jenisKomoditas;

    @SerializedName("status_agama")
    private List<StatusAgama> statusAgama;

    @SerializedName("message")
    private String message;

    public void setJenisUsaha(List<JenisUsaha> jenisUsaha) {
        this.jenisUsaha = jenisUsaha;
    }

    public List<JenisUsaha> getJenisUsaha() {
        return jenisUsaha;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setMember(List<Member> member) {
        this.member = member;
    }

    public List<Member> getMember() {
        return member;
    }

    public void setLihatProfil(List<LihatProfil> lihatProfil) {
        this.lihatProfil = lihatProfil;
    }

    public List<LihatProfil> getLihatProfil() {
        return lihatProfil;
    }

    public void setProdukTernak(List<ProdukTernak> produkTernak) {
        this.produkTernak = produkTernak;
    }

    public List<ProdukTernak> getProdukTernak() {
        return produkTernak;
    }

    public void setJenisKomoditas(List<JenisKomoditas> jenisKomoditas) {
        this.jenisKomoditas = jenisKomoditas;
    }

    public List<JenisKomoditas> getJenisKomoditas() {
        return jenisKomoditas;
    }

    public void setStatusAgama(List<StatusAgama> statusAgama) {
        this.statusAgama = statusAgama;
    }

    public List<StatusAgama> getStatusAgama() {
        return statusAgama;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}