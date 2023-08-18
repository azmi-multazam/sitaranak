package com.zam.sidik_padang.home.selectregion;

import java.io.Serializable;

public class Region implements Serializable {
    public static final String ID = "id", NAMA = "nama";
    public String id = "";
    public String nama = "";

    public Region(String s, String s1) {
        this.id = s;
        this.nama = s1;
    }
}
