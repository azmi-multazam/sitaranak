package com.zam.sidik_padang.home.newsinfo.gallery.bukaalbum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by supriyadi on 3/6/18.
 */

public class Response {
    public boolean success = false;
    public String message = "Terjadi kesalahan 63727";
    public List<Gambar> gallery = new ArrayList<>();

}

/*
	 {"gallery":[
	 {"id_gallery":"224",
	 "jdl_gallery":"Favorit",
	 "gambar":"http:\/\/berkahbsm.com\/asset\/img_galeri\/ksi-9.png",
	 "keterangan":"Mainan adalah barang favorit yang senantiasa diburu para pembeli. Selain murah, pilihannya pun berbagai jenis.\r\n"
	 },
	 {"id_gallery":"218","jdl_gallery":"Seorang Wanita Pedagang","gambar":"http:\/\/berkahbsm.com\/asset\/img_galeri\/ksi-10.png","keterangan":"Seorang wanita sedang menunggu kios aksesorisnya.\r\n"}
	 ],
	 "success":true,
	 "message":"sukses"
	 }
	 */