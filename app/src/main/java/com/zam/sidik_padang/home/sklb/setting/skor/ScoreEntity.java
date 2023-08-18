package com.zam.sidik_padang.home.sklb.setting.skor;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ScoreEntity {

    @SerializedName("hari_min")
    private int hariMin;

    @SerializedName("bulan_max")
    private int bulanMax;

    @SerializedName("hari_max")
    private int hariMax;

    @SerializedName("bulan_min")
    private int bulanMin;

    @SerializedName("parameter")
    private List<Parameter> parameter;

    public void setHariMin(int hariMin) {
        this.hariMin = hariMin;
    }

    public int getHariMin() {
        return hariMin;
    }

    public void setBulanMax(int bulanMax) {
        this.bulanMax = bulanMax;
    }

    public int getBulanMax() {
        return bulanMax;
    }

    public void setHariMax(int hariMax) {
        this.hariMax = hariMax;
    }

    public int getHariMax() {
        return hariMax;
    }

    public void setBulanMin(int bulanMin) {
        this.bulanMin = bulanMin;
    }

    public int getBulanMin() {
        return bulanMin;
    }

    public void setParameter(List<Parameter> parameter) {
        this.parameter = parameter;
    }

    public List<Parameter> getParameter() {
        return parameter;
    }
}