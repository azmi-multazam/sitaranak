package com.zam.sidik_padang.home.dataternak.vm.foto;

import com.google.gson.annotations.SerializedName;

public class LihatGambarTernak {

	@SerializedName("id")
	private String id;

	@SerializedName("tanggal")
	private String tanggal;

	@SerializedName("gambar")
	private String gambar;

	public String getId(){
		return id;
	}

	public String getTanggal(){
		return tanggal;
	}

	public String getGambar(){
		return gambar;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setTanggal(String tanggal) {
		this.tanggal = tanggal;
	}

	public void setGambar(String gambar) {
		this.gambar = gambar;
	}
}