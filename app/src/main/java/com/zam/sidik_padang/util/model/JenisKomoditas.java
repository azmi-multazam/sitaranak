package com.zam.sidik_padang.util.model;

import java.io.Serializable;

/**
 * Created by supriyadi on 10/2/17.
 */
public class JenisKomoditas implements Serializable {
    public String id, jenis = "";

    @Override
    public String toString() {
        return jenis;
    }
}