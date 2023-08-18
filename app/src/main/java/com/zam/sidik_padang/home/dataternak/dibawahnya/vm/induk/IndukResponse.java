package com.zam.sidik_padang.home.dataternak.dibawahnya.vm.induk;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class IndukResponse{

	@SerializedName("success")
	private boolean success;

	@SerializedName("data_induk")
	private List<DataInduk> dataInduk;

	@SerializedName("message")
	private String message;

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	public void setDataInduk(List<DataInduk> dataInduk){
		this.dataInduk = dataInduk;
	}

	public List<DataInduk> getDataInduk(){
		return dataInduk;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}