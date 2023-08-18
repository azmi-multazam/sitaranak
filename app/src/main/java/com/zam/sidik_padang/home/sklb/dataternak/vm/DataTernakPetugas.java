package com.zam.sidik_padang.home.sklb.dataternak.vm;

import com.google.gson.annotations.SerializedName;

public class DataTernakPetugas {

    @SerializedName("hari")
    private int hari;

    @SerializedName("nama_pemilik")
    private String namaPemilik;

    @SerializedName("bangsa")
    private String bangsa;

    @SerializedName("umur")
    private int umur;

    @SerializedName("earteg")
    private String earteg;

    @SerializedName("nama_petugas")
    private String namaPetugas;

    @SerializedName("kelamin")
    private String kelamin;

    @SerializedName("nama")
    private String nama;

    @SerializedName("id_ternak")
    private String idTernak;

    @SerializedName("id_pemilik")
    private String idPemilik;

    @SerializedName("jenis")
    private String jenis;

    @SerializedName("id")
    private String id;

    @SerializedName("kondisi_ternak")
    private String kondisiTernak;

    @SerializedName("kondisi_warna")
    private String kondisiWarna;

    @SerializedName("bulan")
    private int bulan;

    public void setHari(int hari) {
        this.hari = hari;
    }

    public int getHari() {
        return hari;
    }

    public void setNamaPemilik(String namaPemilik) {
        this.namaPemilik = namaPemilik;
    }

    public String getNamaPemilik() {
        return namaPemilik;
    }

    public void setBangsa(String bangsa) {
        this.bangsa = bangsa;
    }

    public String getBangsa() {
        return bangsa;
    }

    public void setUmur(int umur) {
        this.umur = umur;
    }

    public int getUmur() {
        return umur;
    }

    public void setEarteg(String earteg) {
        this.earteg = earteg;
    }

    public String getEarteg() {
        return earteg;
    }

    public void setNamaPetugas(String namaPetugas) {
        this.namaPetugas = namaPetugas;
    }

    public String getNamaPetugas() {
        return namaPetugas;
    }

    public void setKelamin(String kelamin) {
        this.kelamin = kelamin;
    }

    public String getKelamin() {
        return kelamin;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNama() {
        return nama;
    }

    public void setIdTernak(String idTernak) {
        this.idTernak = idTernak;
    }

    public String getIdTernak() {
        return idTernak;
    }

    public void setIdPemilik(String idPemilik) {
        this.idPemilik = idPemilik;
    }

    public String getIdPemilik() {
        return idPemilik;
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

    public void setKondisiTernak(String kondisiTernak) {
        this.kondisiTernak = kondisiTernak;
    }

    public String getKondisiTernak() {
        return kondisiTernak;
    }

    public void setKondisiWarna(String kondisiWarna) {
        this.kondisiWarna = kondisiWarna;
    }

    public String getKondisiWarna() {
        return kondisiWarna;
    }

    public void setBulan(int bulan) {
        this.bulan = bulan;
    }

    public int getBulan() {
        return bulan;
    }
}