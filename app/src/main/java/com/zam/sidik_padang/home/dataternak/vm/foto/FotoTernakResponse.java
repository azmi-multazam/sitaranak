package com.zam.sidik_padang.home.dataternak.vm.foto;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class FotoTernakResponse{

	@SerializedName("lihat_gambar_ternak")
	private List<LihatGambarTernak> lihatGambarTernak;

	@SerializedName("success")
	private boolean success;

	@SerializedName("message")
	private String message;

	public List<LihatGambarTernak> getLihatGambarTernak(){
		return lihatGambarTernak;
	}

	public boolean isSuccess(){
		return success;
	}

	public String getMessage(){
		return message;
	}

	public void setLihatGambarTernak(List<LihatGambarTernak> lihatGambarTernak) {
		this.lihatGambarTernak = lihatGambarTernak;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}