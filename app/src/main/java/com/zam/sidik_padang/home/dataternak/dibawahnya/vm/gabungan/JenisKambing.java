package com.zam.sidik_padang.home.dataternak.dibawahnya.vm.gabungan;

import com.google.gson.annotations.SerializedName;

public class JenisKambing {

	@SerializedName("keterangan")
	private String keterangan;

	@SerializedName("nama")
	private String nama;

	@SerializedName("id_jenis")
	private String idJenis;

	@SerializedName("id")
	private String id;

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

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}
}