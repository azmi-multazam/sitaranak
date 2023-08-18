package com.zam.sidik_padang.home.sklb.petugas.vm;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class PetugasResponse{

	@SerializedName("peternak_umum")
	private List<PeternakUmum> peternakUmum;

	@SerializedName("success")
	private boolean success;

	@SerializedName("message")
	private String message;

	@SerializedName("petugas")
	private List<Petugas> petugas;

	public void setPeternakUmum(List<PeternakUmum> peternakUmum){
		this.peternakUmum = peternakUmum;
	}

	public List<PeternakUmum> getPeternakUmum(){
		return peternakUmum;
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

	public void setPetugas(List<Petugas> petugas){
		this.petugas = petugas;
	}

	public List<Petugas> getPetugas(){
		return petugas;
	}
}