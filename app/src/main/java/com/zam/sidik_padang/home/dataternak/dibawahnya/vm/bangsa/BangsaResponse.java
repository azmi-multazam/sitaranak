package com.zam.sidik_padang.home.dataternak.dibawahnya.vm.bangsa;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class BangsaResponse{

	@SerializedName("bangsa_sapi")
	private List<BangsaSapi> bangsaSapi;

	@SerializedName("bangsa_kambing")
	private List<BangsaKambing> bangsaKambing;

	@SerializedName("success")
	private boolean success;

	@SerializedName("message")
	private String message;

	public void setBangsaSapi(List<BangsaSapi> bangsaSapi){
		this.bangsaSapi = bangsaSapi;
	}

	public List<BangsaSapi> getBangsaSapi(){
		return bangsaSapi;
	}

	public void setBangsaKambing(List<BangsaKambing> bangsaKambing){
		this.bangsaKambing = bangsaKambing;
	}

	public List<BangsaKambing> getBangsaKambing(){
		return bangsaKambing;
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