package com.zam.sidik_padang.home.sklb.setting.skor;

import com.google.gson.annotations.SerializedName;

public class Ukuran {

	@SerializedName("min")
	private int min;

	@SerializedName("kelas")
	private String kelas;

	@SerializedName("skor")
	private String skor;

	@SerializedName("maks")
	private int maks;

	public Ukuran(String kelas, int min, int maks, String skor) {
		this.skor = skor;
		this.min = min;
		this.kelas = kelas;
		this.maks = maks;
	}

	public void setMin(int min){
		this.min = min;
	}

	public int getMin(){
		return min;
	}

	public void setKelas(String kelas){
		this.kelas = kelas;
	}

	public String getKelas(){
		return kelas;
	}

	public void setSkor(String skor){
		this.skor = skor;
	}

	public String getSkor(){
		return skor;
	}

	public void setMaks(int maks){
		this.maks = maks;
	}

	public int getMaks(){
		return maks;
	}
}