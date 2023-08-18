package com.zam.sidik_padang.home.sklb.petugas.vm;

import com.google.gson.annotations.SerializedName;

public class PeternakUmum {

	@SerializedName("desa")
	private String desa;

	@SerializedName("umur")
	private int umur;

	@SerializedName("hp")
	private String hp;

	@SerializedName("id_jenis_kelamin")
	private String idJenisKelamin;

	@SerializedName("alamat")
	private String alamat;

	@SerializedName("nama")
	private String nama;

	@SerializedName("foto")
	private String foto;

	@SerializedName("tempat_lahir")
	private String tempatLahir;

	@SerializedName("kecamatan")
	private String kecamatan;

	@SerializedName("id")
	private String id;

	@SerializedName("jenis_kelamin")
	private String jenisKelamin;

	@SerializedName("email")
	private String email;

	@SerializedName("tanggal_lahir")
	private String tanggalLahir;

	public void setDesa(String desa){
		this.desa = desa;
	}

	public String getDesa(){
		return desa;
	}

	public void setUmur(int umur){
		this.umur = umur;
	}

	public int getUmur(){
		return umur;
	}

	public void setHp(String hp){
		this.hp = hp;
	}

	public String getHp(){
		return hp;
	}

	public void setIdJenisKelamin(String idJenisKelamin){
		this.idJenisKelamin = idJenisKelamin;
	}

	public String getIdJenisKelamin(){
		return idJenisKelamin;
	}

	public void setAlamat(String alamat){
		this.alamat = alamat;
	}

	public String getAlamat(){
		return alamat;
	}

	public void setNama(String nama){
		this.nama = nama;
	}

	public String getNama(){
		return nama;
	}

	public void setFoto(String foto){
		this.foto = foto;
	}

	public String getFoto(){
		return foto;
	}

	public void setTempatLahir(String tempatLahir){
		this.tempatLahir = tempatLahir;
	}

	public String getTempatLahir(){
		return tempatLahir;
	}

	public void setKecamatan(String kecamatan){
		this.kecamatan = kecamatan;
	}

	public String getKecamatan(){
		return kecamatan;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setJenisKelamin(String jenisKelamin){
		this.jenisKelamin = jenisKelamin;
	}

	public String getJenisKelamin(){
		return jenisKelamin;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setTanggalLahir(String tanggalLahir){
		this.tanggalLahir = tanggalLahir;
	}

	public String getTanggalLahir(){
		return tanggalLahir;
	}
}