package com.zam.sidik_padang.home.sklb.petugas.vm.jeniskel;

import com.google.gson.annotations.SerializedName;

public class Kelamin {

    @SerializedName("kelamin")
    private String kelamin;

    @SerializedName("id")
    private String id;

    public void setKelamin(String kelamin) {
        this.kelamin = kelamin;
    }

    public String getKelamin() {
        return kelamin;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}