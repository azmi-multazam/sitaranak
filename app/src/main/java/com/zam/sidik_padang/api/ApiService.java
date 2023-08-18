package com.zam.sidik_padang.api;

import static com.zam.sidik_padang.util.Config.URL_BERITA;
import static com.zam.sidik_padang.util.Config.URL_HITUNG_TERNAK;
import static com.zam.sidik_padang.util.Config.URL_JUMLAH_TERNAK;
import static com.zam.sidik_padang.util.Config.URL_LIHAT_DATA_TERNAK;
import static com.zam.sidik_padang.util.Config.URL_LIHAT_DATA_TERNAK_SKLB;
import static com.zam.sidik_padang.util.Config.URL_NAMA_DAERAH;
import static com.zam.sidik_padang.util.Config.URL_PEMILIK;
import static com.zam.sidik_padang.util.Config.URL_PETUGAS;
import static com.zam.sidik_padang.util.Config.URL_PROFILE;
import static com.zam.sidik_padang.util.Config.URL_SERTIFIKAT_SKLB;
import static com.zam.sidik_padang.util.Config.URL_SETTING_SKLB;

import java.util.Map;

import com.zam.sidik_padang.home.dataternak.dibawahnya.vm.bangsa.BangsaResponse;
import com.zam.sidik_padang.home.dataternak.dibawahnya.vm.gabungan.GabunganResponse;
import com.zam.sidik_padang.home.dataternak.dibawahnya.vm.induk.IndukResponse;
import com.zam.sidik_padang.home.dataternak.vm.detail.DetailTernakResponse;
import com.zam.sidik_padang.home.dataternak.vm.foto.FotoTernakResponse;
import com.zam.sidik_padang.home.dataternak.vm.kondisi.KondisiResponse;
import com.zam.sidik_padang.home.dataternak.vm.list.DataTernakResponse;
import com.zam.sidik_padang.home.dataternak.vm.list.HitungTernak;
import com.zam.sidik_padang.home.memberhome.mvp.BeritaResponse;
import com.zam.sidik_padang.home.newsinfo.bukaberita.komentar.KomentarResponse;
import com.zam.sidik_padang.home.newsinfo.bukaberita.mvp.BukaBeritaResponse;
import com.zam.sidik_padang.home.newsinfo.mvp.navmenu.NavMenuResponse;
import com.zam.sidik_padang.home.sklb.dataternak.vm.DataTernakSklbResponse;
import com.zam.sidik_padang.home.sklb.petugas.vm.PetugasResponse;
import com.zam.sidik_padang.home.sklb.petugas.vm.desa.DesaResponse;
import com.zam.sidik_padang.home.sklb.petugas.vm.kab.KabResponse;
import com.zam.sidik_padang.home.sklb.petugas.vm.kec.KecResponse;
import com.zam.sidik_padang.home.sklb.petugas.vm.pemilik.PemilikResponse;
import com.zam.sidik_padang.home.sklb.print.sertifikat.vm.SertifikatResponse;
import com.zam.sidik_padang.home.sklb.setting.kuantitatif.SettingDinas;
import com.zam.sidik_padang.home.sklb.setting.skor.ListKuantitatif;
import com.zam.sidik_padang.profilku.model.ProfileResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface ApiService {

    @GET(URL_PROFILE)
    Call<ProfileResponse> getProfile(@QueryMap Map<String, String> stringMap);

    @GET(URL_PROFILE)
    Call<BaseResponse> editProfile(@QueryMap Map<String, String> stringMap);

    @GET(URL_BERITA)
    Call<BukaBeritaResponse> getNews(@QueryMap Map<String, String> stringMap);

    @GET(URL_BERITA)
    Call<BeritaResponse> getNewsHome(@QueryMap Map<String, String> stringMap);

    @GET(URL_BERITA)
    Call<NavMenuResponse> getNewsNav(@QueryMap Map<String, String> stringMap);

    @GET(URL_BERITA)
    Call<KomentarResponse> getKomentar(@QueryMap Map<String, String> stringMap);

    // Detail Ternak
    @GET(URL_LIHAT_DATA_TERNAK)
    Call<DataTernakResponse> getDataTernak(@QueryMap Map<String, String> stringMap);

    @GET(URL_LIHAT_DATA_TERNAK)
    Call<BaseResponse> hapusTernak(@QueryMap Map<String, String> stringMap);

    @GET(URL_JUMLAH_TERNAK)
    Call<DataTernakResponse> getJumlahTernak(@QueryMap Map<String, String> stringMap);

    @GET(URL_HITUNG_TERNAK)
    Call<HitungTernak> hitungTernak(@QueryMap Map<String, String> stringMap);

    @GET
    Call<DetailTernakResponse> getDetailTernak(@Url String url);

    @GET
    Call<FotoTernakResponse> getFotoTernak(@Url String url);

    @GET
    Call<KondisiResponse> getKondisiTernak(@Url String url);

    @GET
    Call<GabunganResponse> getGabungan(@Url String url);

    @GET
    Call<BangsaResponse> getBangsa(@Url String url);

    @GET
    Call<IndukResponse> getInduk(@Url String url);

    @GET
    Call<IndukResponse> getBapak(@Url String url);

    @GET
    Call<BaseResponse> sendTernak(@Url String url);

    // register petugas
    @GET(URL_NAMA_DAERAH)
    Call<KabResponse> getKab(@QueryMap Map<String, String> stringMap);

    @GET(URL_NAMA_DAERAH)
    Call<KecResponse> getKec(@QueryMap Map<String, String> stringMap);

    @GET(URL_NAMA_DAERAH)
    Call<DesaResponse> getPDesa(@QueryMap Map<String, String> stringMap);

    @GET(URL_PETUGAS)
    Call<PetugasResponse> getPetugas(@QueryMap Map<String, String> stringMap);

    @GET(URL_PETUGAS)
    Call<BaseResponse> addPetugas(@QueryMap Map<String, String> stringMap);

    @GET(URL_PETUGAS)
    Call<BaseResponse> editPetugas(@QueryMap Map<String, String> stringMap);

    @GET(URL_PETUGAS)
    Call<BaseResponse> deletePetugas(@QueryMap Map<String, String> stringMap);

    @GET(URL_PEMILIK)
    Call<PemilikResponse> getPemilikTernak(@Query("userid") String user, @Query("aksi") String aksi);

    @GET(URL_PEMILIK)
    Call<BaseResponse> addPemilikTernak(@QueryMap Map<String, String> stringMap);

    @GET(URL_PEMILIK)
    Call<BaseResponse> editPemilikTernak(@QueryMap Map<String, String> stringMap);

    @GET(URL_PEMILIK)
    Call<BaseResponse> deletePemilikTernak(@QueryMap Map<String, String> stringMap);

    //https://e-rekording.com/api_pamekasan/lihat_dataternak_sklb.php?userid=KS1000005&aksi=3
    @GET(URL_LIHAT_DATA_TERNAK_SKLB)
    Call<DataTernakSklbResponse> getDataTernakSklb(@Query("userid") String user, @Query("aksi") String aksi);

    //https://e-rekording.com/api_pamekasan/sertifikat_sklb.php?userid=KS1000005&aksi=1
    @GET(URL_SERTIFIKAT_SKLB)
    Call<SertifikatResponse> getSertifikat(@Query("userid") String user, @Query("aksi") String aksi);

    @Multipart
    @POST(URL_SETTING_SKLB)
    Call<BaseResponse> sendKuantitatif(@Part() MultipartBody.Part file);

    @POST(URL_SETTING_SKLB)
    Call<BaseResponse> sendKuantitatifList(@Body ListKuantitatif listKuantitatif);

    @POST(URL_SETTING_SKLB)
    Call<BaseResponse> sendKuantitatifJson(@Body String json);

    @Multipart
    @POST("sertifikat.php")
    Call<SertifikatResponse> sendSettingDinas(@Body SettingDinas settingDinas);

    @GET("setingsklb.txt")
    Call<ListKuantitatif> getSettingSklb();

    /*
    @Multipart
    @POST("konsumen.php")
    Call<ServerRespons> editKonsumenWithFoto(@QueryMap Map<String, String> stringMap,
                                             @Part() MultipartBody.Part file,
                                             @Part("foto") RequestBody name);
     */
}
