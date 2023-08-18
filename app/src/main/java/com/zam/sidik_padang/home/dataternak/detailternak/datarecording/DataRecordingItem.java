package com.zam.sidik_padang.home.dataternak.detailternak.datarecording;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by supriyadi on 9/11/17.
 */

public class DataRecordingItem implements Parcelable {
    public static final Creator<DataRecordingItem> CREATOR = new Creator<DataRecordingItem>() {
        @Override
        public DataRecordingItem createFromParcel(Parcel in) {
            return new DataRecordingItem(in);
        }

        @Override
        public DataRecordingItem[] newArray(int size) {
            return new DataRecordingItem[size];
        }
    };
    String id, tanggal, berat_badan, panjang_badan, lingkar_dada, dalam_dada, tinggi_badan, pbbh;

    protected DataRecordingItem(Parcel in) {
        id = in.readString();
        tanggal = in.readString();
        berat_badan = in.readString();
        panjang_badan = in.readString();
        lingkar_dada = in.readString();
        dalam_dada = in.readString();
        tinggi_badan = in.readString();
        pbbh = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(tanggal);
        dest.writeString(berat_badan);
        dest.writeString(panjang_badan);
        dest.writeString(lingkar_dada);
        dest.writeString(dalam_dada);
        dest.writeString(tinggi_badan);
        dest.writeString(pbbh);

    }
}

/*
{"id":"2",
		* 	"tanggal":"2004-04-21",
		* 	"berat_badan":"64.5",
		* 	"panjang_badan":"76.5",
		* 	"tinggi_badan":"84",
		* 	"lingkar_dada":"93",
		* 	"dalam_dada":"35.5"}
*/
