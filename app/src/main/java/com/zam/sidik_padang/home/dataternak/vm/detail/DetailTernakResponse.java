package com.zam.sidik_padang.home.dataternak.vm.detail;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DetailTernakResponse{

	@SerializedName("keterangan")
	private List<Keterangan> keterangan;

	@SerializedName("success")
	private boolean success;

	@SerializedName("message")
	private String message;

	public void setKeterangan(List<Keterangan> keterangan){
		this.keterangan = keterangan;
	}

	public List<Keterangan> getKeterangan(){
		return keterangan;
	}

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}