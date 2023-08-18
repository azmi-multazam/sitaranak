package com.zam.sidik_padang.home.dataternak.dibawahnya;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by supriyadi on 5/14/17.
 */

public class DataTernak implements Parcelable {
    public static final Creator<DataTernak> CREATOR = new Creator<DataTernak>() {
        @Override
        public DataTernak createFromParcel(Parcel in) {
            return new DataTernak(in);
        }

        @Override
        public DataTernak[] newArray(int size) {
            return new DataTernak[size];
        }
    };
    public int id;
    public String id_ternak = "", jenis = "", bangsa = "", kelamin = "", id_pemilik = "", nama = "", kondisi_warna = "#00ff00", kondisi_ternak, id_bangsa;
    public int umur = 0, bulan = 0, hari = 0;

    public DataTernak() {

    }

    protected DataTernak(Parcel in) {
        id = in.readInt();
        id_ternak = in.readString();
        jenis = in.readString();
        bangsa = in.readString();
        kelamin = in.readString();
        id_pemilik = in.readString();
        nama = in.readString();
        umur = in.readInt();
        bulan = in.readInt();
        hari = in.readInt();
        kondisi_warna = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(id_ternak);
        dest.writeString(jenis);
        dest.writeString(bangsa);
        dest.writeString(kelamin);
        dest.writeString(id_pemilik);
        dest.writeString(nama);
        dest.writeInt(umur);
        dest.writeInt(bulan);
        dest.writeInt(hari);
        dest.writeString(kondisi_warna);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return id_ternak;
    }
}
