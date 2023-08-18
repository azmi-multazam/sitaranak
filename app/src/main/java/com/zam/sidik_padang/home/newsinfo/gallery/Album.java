package com.zam.sidik_padang.home.newsinfo.gallery;

import java.io.Serializable;

public class Album implements Serializable {
    public String
            id_album,
            gbr_album,
            keterangan,
            jdl_album = "Tidak ada judul";

    public int jumlah_foto = 0;
}

/*
{
	"id_album":"43",
	"gbr_album":"http://berkahbsm.com/asset/img_album/ksi-7.png",
	"keterangan":"Para macan tua yang digawangi Iwan Fals, Setiawan Djody dan Sawung Jabo di Stadion Gelora Bung Karno, Jakarta,\r\nJumat (30/12) malam. Kantata kembali membawakan lagu-lagu legendarisnya\r\nsetelah 21 tahun vakum dari dunia musik.\r\n<div style=\"overflow: hidden; color: #000000; background-color: #ffffff; text-align: left; text-decoration: none; border: medium none\">\r\n<br />\r\n</div>\r\n",
	"jumlah_foto":1
},

*/
