package com.zam.sidik_padang.home.newsinfo;

import java.util.ArrayList;
import java.util.List;

import com.zam.sidik_padang.ApiResponse;
import com.zam.sidik_padang.home.newsinfo.gallery.Album;
import com.zam.sidik_padang.home.newsinfo.video.Video;

public class MediaApiResponse extends ApiResponse {
    public List<Video> video = new ArrayList<>();
    public List<Album> album_galery = new ArrayList<>();
}

	
	/*
	{"video":[
				{"id":"160",
				 "judul_video":"Selamatkan Hutan di Indonesia",
				 "youtube":"http:\/\/www.youtube.com\/embed\/hkzpLJjZQbA",
				 "tanggal":" Rabu, 02 Jul 2014 07:30:12"
				 },
				 
				 {"id":"161","judul_video":"Hutan Hujan Tropis Indonesia","youtube":"http:\/\/www.youtube.com\/embed\/5biK_PcT7S0","tanggal":" Rabu, 02 Jul 2014 07:31:23"},{"id":"162","judul_video":"Perang Sengit Pasukan Darat Israel Vs Hamas","youtube":"https:\/\/www.youtube.com\/watch?v=CeNjeD8yknI","tanggal":" Rabu, 23 Jul 2014 15:23:10"},{"id":"163","judul_video":"Isael dan Palestina Memanas","youtube":"https:\/\/www.youtube.com\/watch?v=oaJpxuDh5Ds","tanggal":" Rabu, 23 Jul 2014 15:24:30"},{"id":"164","judul_video":"Israel Tembak Mati Warga Palestina Saat Gencatan","youtube":"https:\/\/www.youtube.com\/watch?v=L6R-5XUcDSY","tanggal":" Rabu, 23 Jul 2014 15:29:41"},{"id":"165","judul_video":"Gaza conflict: Israel & Hamas face allegations of war crimes","youtube":"http:\/\/www.youtube.com\/embed\/aqI4DOilySg","tanggal":" Minggu, 17 Agu 2014 16:49:33"},{"id":"166","judul_video":"Chomsky: Calling for change on US support for Israelll","youtube":"http:\/\/www.youtube.com\/embed\/gQRJEnoxr2A","tanggal":" Minggu, 17 Agu 2014 16:51:04"}
			
				],
				
		"gallery":[{"id":"224",
					"judul_gallery":"Favorit",
					"gbr_gallery":"http:\/\/berkahbsm.com\/asset\/img_galeri\/34asemka10.jpg",
					"keterangan":"Mainan adalah barang favorit yang senantiasa diburu para pembeli. Selain murah, pilihannya pun berbagai jenis.\r\n"
					},{"id":"218","judul_gallery":"Seorang Wanita Pedagang","gbr_gallery":"http:\/\/berkahbsm.com\/asset\/img_galeri\/7asemka6.jpeg","keterangan":"Seorang wanita sedang menunggu kios aksesorisnya.\r\n"}
				  ],
	"success":true,
	"message":"sukses"}
	
	*/
