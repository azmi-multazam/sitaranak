package com.zam.sidik_padang.home.dataternak.vm.kondisi;

import com.google.gson.annotations.SerializedName;

public class KondisiStatus {

	@SerializedName("warna")
	private String warna;

	@SerializedName("id")
	private String id;

	@SerializedName("nama_kondisi")
	private String namaKondisi;

	public void setWarna(String warna){
		this.warna = warna;
	}

	public String getWarna(){
		return warna;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setNamaKondisi(String namaKondisi){
		this.namaKondisi = namaKondisi;
	}

	public String getNamaKondisi(){
		return namaKondisi;
	}
}