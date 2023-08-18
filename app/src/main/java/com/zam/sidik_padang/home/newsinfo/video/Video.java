package com.zam.sidik_padang.home.newsinfo.video;

import android.net.Uri;

import java.io.Serializable;

public class Video implements Serializable {
    String id, judul_video, youtube, tanggal;

    public String getVideoId() {
        String vId = "";
        if (youtube != null && !youtube.isEmpty() && youtube.contains("/")) {
            if (youtube.contains("v=")) {
                String[] splited = youtube.split("v=");
                if (splited.length > 1) vId = splited[1];
            } else {
                Uri uri = Uri.parse(youtube);
                vId = uri.getLastPathSegment();
            }

        }

        return vId;
    }
}

/*  
{"id":"160",
				 "judul_video":"Selamatkan Hutan di Indonesia",
				 "youtube":"http:\/\/www.youtube.com\/embed\/hkzpLJjZQbA",
				 "tanggal":" Rabu, 02 Jul 2014 07:30:12"
				 }
*/
