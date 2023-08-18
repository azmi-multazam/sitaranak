package com.zam.sidik_padang.home.dataternak.vm.kondisi;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class KondisiResponse{

	@SerializedName("success")
	private boolean success;

	@SerializedName("kondisi_status")
	private List<KondisiStatus> kondisiStatus;

	@SerializedName("message")
	private String message;

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	public void setKondisiStatus(List<KondisiStatus> kondisiStatus){
		this.kondisiStatus = kondisiStatus;
	}

	public List<KondisiStatus> getKondisiStatus(){
		return kondisiStatus;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}