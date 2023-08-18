package com.zam.sidik_padang.home.dataternak.dibawahnya.vm.gabungan;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("jenis_kambing")
	private List<JenisKambing> jenisKambing;

	@SerializedName("metode_kelahiran")
	private List<MetodeKelahiran> metodeKelahiran;

	@SerializedName("nama_ternak")
	private List<NamaTernak> namaTernak;

	@SerializedName("jenis_kelamin")
	private List<JenisKelamin> jenisKelamin;

	@SerializedName("jenis_sapi")
	private List<JenisSapi> jenisSapi;

	public void setJenisKambing(List<JenisKambing> jenisKambing){
		this.jenisKambing = jenisKambing;
	}

	public List<JenisKambing> getJenisKambing(){
		return jenisKambing;
	}

	public void setMetodeKelahiran(List<MetodeKelahiran> metodeKelahiran){
		this.metodeKelahiran = metodeKelahiran;
	}

	public List<MetodeKelahiran> getMetodeKelahiran(){
		return metodeKelahiran;
	}

	public void setNamaTernak(List<NamaTernak> namaTernak){
		this.namaTernak = namaTernak;
	}

	public List<NamaTernak> getNamaTernak(){
		return namaTernak;
	}

	public void setJenisKelamin(List<JenisKelamin> jenisKelamin){
		this.jenisKelamin = jenisKelamin;
	}

	public List<JenisKelamin> getJenisKelamin(){
		return jenisKelamin;
	}

	public void setJenisSapi(List<JenisSapi> jenisSapi){
		this.jenisSapi = jenisSapi;
	}

	public List<JenisSapi> getJenisSapi(){
		return jenisSapi;
	}
}