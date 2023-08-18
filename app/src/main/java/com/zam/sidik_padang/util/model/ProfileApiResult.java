package com.zam.sidik_padang.util.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by supriyadi on 10/2/17.
 */

public class ProfileApiResult implements Serializable {
    public List<Profile> lihat_profil = new ArrayList<>();
    public ArrayList<StatusAgama> status_agama = new ArrayList<>();
    public ArrayList<JenisKomoditas> jenis_komoditas = new ArrayList<>();
    public ArrayList<ProdukTernak> produk_ternak = new ArrayList<>();
    public ArrayList<JenisUsaha> jenis_usaha = new ArrayList<>();
}
