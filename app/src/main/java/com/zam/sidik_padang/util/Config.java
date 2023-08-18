package com.zam.sidik_padang.util;

//import com.zam.sidik_padang.BuildConfig;

/**
 * Created by supriyadi on 2/6/17.
 */

public class Config {
    //ubah jadi false untuk menonaktifkan fungsi debugging
    //public static final boolean DEBUG = BuildConfig.DEBUG;

    //public static final String BASE_BERITA = "https://santer.id/android_api/";
    public static final String BASE_URL = "https://e-rekording.com";
    public static final String BASE_PADANG = BASE_URL + "/padang/";

    public static final String
            URL_LOGO = BASE_PADANG + "index.php",
            URL_REGISTRATION = BASE_PADANG + "crud/registration.php",
            URL_LOGIN = BASE_PADANG + "login.php",//?userid=KS100000&password=123456,
            URL_ALL_NAMA_KELOMPOK_TERNAK = BASE_PADANG + "nama_kelompok_ternak.php",
            URL_KELOMPOK_TERNAK = BASE_PADANG + "add_kelompok_ternak.php",
            URL_NAMA_DAERAH = BASE_PADANG + "kode_daerah.php",
            URL_PROFILE = BASE_PADANG + "profil.php",
            URL_KOORDINATOR = BASE_PADANG + "kordinator.php",
            URL_LIHAT_DATA_PETERNAK = BASE_PADANG + "lihat_datapeternak.php",
            URL_LIHAT_DATA_TERNAK = BASE_PADANG + "lihat_dataternak.php",
            URL_LIHAT_DATA_TERNAK_SKLB = BASE_PADANG + "lihat_dataternak_sklb.php",
            URl_STATUS_PERNIKAHAN = BASE_PADANG + "status_pernikahan.php",
            URL_JENIS_KELAMIN = BASE_PADANG + "jenis_kelamin.php",
            URL_AGAMA = BASE_PADANG + "agama.php",
            URL_STATUS_PETERNAKAN = BASE_PADANG + "status_peternakan.php",
            URL_KELOMPOK = BASE_PADANG + "kelompok.php",
            URL_JENIS_KOMODITAS = BASE_PADANG + "jenis_komoditas.php",
            URL_JENIS_USAHA = BASE_PADANG + "jenis_usaha.php",
            URL_NAMA_KELOMPOK = BASE_PADANG + "nama_kelompok.php",
            URL_PRODUK_TERNAK = BASE_PADANG + "produk_ternak.php",
            URL_TAMBAH_GAMBAR = BASE_PADANG + "tambah_gambar.php",
            URL_TAMBAH_IB = BASE_PADANG + "tambah_ib.php",
            URL_DATA_TERNAK = BASE_PADANG + "data_ternak.php",
            URL_DAFTAR = BASE_PADANG + "daftar.php",
            URL_GAMBAR_SAPI = BASE_PADANG + "gambar_sapi.php",
            URL_JUMLAH_TERNAK = BASE_PADANG + "jumlah_ternak.php",
            URL_GABUNGAN = BASE_PADANG + "gabungan.php",
            URL_HITUNG_TERNAK = BASE_PADANG + "hitung_ternak.php",
            URL_BERITA = BASE_PADANG + "berita.php",
            URL_PEMILIK = BASE_PADANG + "pemilik_ternak.php",
            URL_PETUGAS = BASE_PADANG + "add_pamekasan.php",
            URL_SERTIFIKAT = BASE_PADANG + "sertifikat.php",
            URL_SERTIFIKAT_SKLB = BASE_PADANG + "sertifikat_sklb.php",
            URL_SETTING_SKLB = BASE_PADANG + "setingsklb.php";


    //URL_TAMBAH_GAMBAR = "http://192.168.61.1/unggah.php",
    //URL_TAMBAH_GAMBAR = "http://codecrot.com/inyongmobile/upload.php",

    public static final String URL_SALDO = BASE_URL + "/api/saldo.php?",
            URL_PROSES = BASE_URL + "/api/proses.php?",
            URL_HISTORY = BASE_URL + "/api/history.php?",
            URL_BERITA_PPOB = BASE_URL + "/api/berita.php?",
            URL_CEK_MEMBER = BASE_URL + "/api/cekmember.php?",
            URL_DEPOSIT = BASE_URL + "/api/deposit.php?",
            URL_DOWNLINE = BASE_URL + "/api/downline.php?",
            URL_SETING = BASE_URL + "/api/seting.php?",
            URL_BANK = BASE_URL + "/api/bank.php?",
            URL_GALERY = BASE_URL + "/api/galery.php",
            URL_VIDEO = BASE_URL + "/api/video.php",
            URL_PPOB = BASE_URL + "/api/ppob.php?",
            URL_MERCHANT_LIST = BASE_URL + "/api/list_mercent.php?",
            URL_TRX_MERCHANT = BASE_URL + "/api/trx_merchent.php?";

    public static final String PREF_PILIHAN_BANK = "pilihan_bank";

    //http://e-rekording.com/
    public static final String
            PREF_USER_DETAIL_JSON = "user_detail_json",
            PREF_IS_LOGED_IN = "is_loged_in",
            PREF_WAKTU_DAFTAR = "waktu_daftar",
            PREF_USER_ID = "user_id";
    public static final String PREF_NOMOR_HP = "nomor_hp";
    public static final String PREF_NOMOR_CONFIRMED = "nomor_confirmed";
    public static final String PREF_PROFIL_TERSIMPAN = "profile_tersimpan";
    public static final String PREF_FOTO_TERNAK_ALL_TERSIMPAN = "foto_ternak_all";
    public static final String PREF_SAPIKU_TERSIMPAN = "response_sapiku";

    public static final String BASE_SERTIFIKAT = BASE_PADANG + "/sertifikat.php";
    public static final String CATATAN_SERTIFIKAT = "Surat keterangan ini tidak boleh hilang/rusak dan mengikuti setiap perpindahan ternak";
    public static final String JABATAN_DINAS = "KEPALA DINAS\nKETAHANAN PANGAN DAN PERTANIAN\nKABUPATEN Padang";
}
