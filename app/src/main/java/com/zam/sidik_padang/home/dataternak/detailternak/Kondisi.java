package com.zam.sidik_padang.home.dataternak.detailternak;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by supriyadi on 10/19/17.
 */

public class Kondisi implements Parcelable {
    public static final Creator<Kondisi> CREATOR = new Creator<Kondisi>() {
        @Override
        public Kondisi createFromParcel(Parcel in) {
            return new Kondisi(in);
        }

        @Override
        public Kondisi[] newArray(int size) {
            return new Kondisi[size];
        }
    };
    public String id, nama_kondisi, warna;

    protected Kondisi(Parcel in) {
        id = in.readString();
        nama_kondisi = in.readString();
        warna = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nama_kondisi);
        dest.writeString(warna);
    }
}
