package com.zam.sidik_padang.home.dataternak.dibawahnya.vm.gabungan;

import com.google.gson.annotations.SerializedName;

public class MetodeKelahiran {

	@SerializedName("kelamin")
	private String kelamin;

	@SerializedName("id_kelamin")
	private String idKelamin;

	@SerializedName("keterangan")
	private String keterangan;

	@SerializedName("nama")
	private String nama;

	@SerializedName("id_jenis")
	private String idJenis;

	@SerializedName("id_hasil")
	private String idHasil;

	@SerializedName("nama_hasil")
	private String namaHasil;

	@SerializedName("id")
	private String id;

	public void setKelamin(String kelamin){
		this.kelamin = kelamin;
	}

	public String getKelamin(){
		return kelamin;
	}

	public void setIdKelamin(String idKelamin){
		this.idKelamin = idKelamin;
	}

	public String getIdKelamin(){
		return idKelamin;
	}

	public void setKeterangan(String keterangan){
		this.keterangan = keterangan;
	}

	public String getKeterangan(){
		return keterangan;
	}

	public void setNama(String nama){
		this.nama = nama;
	}

	public String getNama(){
		return nama;
	}

	public void setIdJenis(String idJenis){
		this.idJenis = idJenis;
	}

	public String getIdJenis(){
		return idJenis;
	}

	public void setIdHasil(String idHasil){
		this.idHasil = idHasil;
	}

	public String getIdHasil(){
		return idHasil;
	}

	public void setNamaHasil(String namaHasil){
		this.namaHasil = namaHasil;
	}

	public String getNamaHasil(){
		return namaHasil;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}
}