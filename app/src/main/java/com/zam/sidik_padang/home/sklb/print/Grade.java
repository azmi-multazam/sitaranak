package com.zam.sidik_padang.home.sklb.print;

public class Grade {

    public String id;
    public String grade;
    public String skor;
    public String tanggal;
    public String noSurat;
    public String catatan;
    public String jabatan;
    public String kadin;
    public String nip;
    public String ttd;

    public Grade(String id, String grade, String skor, String tanggal, String noSurat,
                 String catatan, String jabatan, String kadin, String nip, String ttd) {
        this.id = id;
        this.grade = grade;
        this.skor = skor;
        this.tanggal = tanggal;
        this.noSurat = noSurat;
        this.catatan = catatan;
        this.jabatan = jabatan;
        this.kadin = kadin;
        this.nip = nip;
        this.ttd = ttd;
    }
}
