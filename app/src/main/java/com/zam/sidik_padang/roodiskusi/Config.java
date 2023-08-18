package com.zam.sidik_padang.roodiskusi;

/**
 * Created by supriyadi on 2/6/17.
 */

public class Config {
    //ubah jadi false untuk menonaktifkan fungsi debugging
    public static final boolean DEBUG = true;

    //localhost hp
    //public static final String BASE_URL = "http://localhost:8080";

    //localhost komputer (genymotion)
//		public static final String BASE_URL="http://192.168.57.1";


    public static final String BASE_URL = "http://e-rekording.com";


    public static final String
            URL_REGISTRATION = BASE_URL + "/crud/registration.php",
            URL_LOGIN = BASE_URL + "/android_api/login.php",//?userid=KS100000&password=123456,
            URL_ALL_NAMA_KELOMPOK_TERNAK = BASE_URL + "/android_api/nama_kelompok_ternak.php",
            URL_KELOMPOK_TERNAK = BASE_URL + "/android_api/add_kelompok_ternak.php",
            URL_NAMA_DAERAH = BASE_URL + "/android_api/kode_daerah.php",
            URL_PROFILE = BASE_URL + "/android_api/profil.php",
            URL_KOORDINATOR = BASE_URL + "/android_api/kordinator.php",
            URL_LIHAT_DATA_PETERNAK = BASE_URL + "/android_api/lihat_datapeternak.php",
            URL_LIHAT_DATA_TERNAK = BASE_URL + "/android_api/lihat_dataternak.php",
            URl_STATUS_PERNIKAHAN = BASE_URL + "/android_api/status_pernikahan.php",
            URL_JENIS_KELAMIN = BASE_URL + "/android_api/jenis_kelamin.php",
            URL_AGAMA = BASE_URL + "/android_api/agama.php",
            URL_STATUS_PETERNAKAN = BASE_URL + "/android_api/status_peternakan.php",
            URL_KELOMPOK = BASE_URL + "/android_api/kelompok.php",
            URL_JENIS_KOMODITAS = BASE_URL + "/android_api/jenis_komoditas.php",
            URL_JENIS_USAHA = BASE_URL + "/android_api/jenis_usaha.php",
            URL_NAMA_KELOMPOK = BASE_URL + "/android_api/nama_kelompok.php",
            URL_PRODUK_TERNAK = BASE_URL + "/android_api/produk_ternak.php",
            URL_TAMBAH_GAMBAR = BASE_URL + "/android_api/tambah_gambar.php",
            URL_TAMBAH_IB = BASE_URL + "/android_api/tambah_ib.php",
    //URL_TAMBAH_GAMBAR = "http://192.168.61.1/unggah.php",
    //URL_TAMBAH_GAMBAR = "http://codecrot.com/inyongmobile/upload.php",
    URL_DATA_TERNAK = BASE_URL + "/android_api/data_ternak.php",
            URL_DAFTAR = BASE_URL + "/android_api/daftar.php",
            URL_GAMBAR_SAPI = BASE_URL + "/android_api/gambar_sapi.php",
            URL_JUMLAH_TERNAK = BASE_URL + "/android_api/jumlah_ternak.php",
            URL_GABUNGAN = BASE_URL + "/android_api/gabungan.php",
            URL_BERITA = BASE_URL + "/android_api/berita.php",

    URL_LOGO = BASE_URL + "/android_api/index.php";

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
    public static final String PREF_FOTO_TERNAK_ALL_TERSIMPAN = "foto_ternak_all";
    public static final String PREF_SAPIKU_TERSIMPAN = "response_sapiku";


}
