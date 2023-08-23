package com.zam.sidik_padang.home.memberhome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.datapeternak.DataPeternakActivity;
import com.zam.sidik_padang.home.dataternak.DataTernakActivity;
import com.zam.sidik_padang.home.dataternak.insiminator.TernakIbListActivity;
import com.zam.sidik_padang.home.kelompokternak.KelompokTernakActivity;
import com.zam.sidik_padang.home.koordinator.KoordinatorActivity;
import com.zam.sidik_padang.home.memberhome.mvp.Berita;
import com.zam.sidik_padang.home.memberhome.mvp.BeritaResponse;
import com.zam.sidik_padang.home.memberhome.mvp.NewsContract;
import com.zam.sidik_padang.home.memberhome.mvp.NewsPresenter;
import com.zam.sidik_padang.home.newsinfo.NewsInfoActivity;
import com.zam.sidik_padang.home.ppob.PPOBActivity;
import com.zam.sidik_padang.home.ppob.SaldoActivity;
import com.zam.sidik_padang.home.ppob.bayartagihan.BayarTagihanActivity;
import com.zam.sidik_padang.home.ppob.berita.BeritaActivity;
import com.zam.sidik_padang.home.ppob.game.VoucherGameActivity;
import com.zam.sidik_padang.home.ppob.history.HistoryActivity;
import com.zam.sidik_padang.home.ppob.iklanmerchant.IklanMerchantActivity;
import com.zam.sidik_padang.home.ppob.internet.PaketInternetActivity;
import com.zam.sidik_padang.home.ppob.isipulsa.IsiPulsaActivity;
import com.zam.sidik_padang.home.ppob.komisi.KoperasiActivity;
import com.zam.sidik_padang.home.ppob.settings.SettingsActivity;
import com.zam.sidik_padang.home.ppob.tambahsaldo.TambahSaldoActivity;
import com.zam.sidik_padang.home.ppob.tokenpln.TokenPlnActivity;
import com.zam.sidik_padang.home.ppob.transfersaldo.TransferSaldoActivity;
import com.zam.sidik_padang.home.sklb.SklbActivity;
import com.zam.sidik_padang.home.sklb.petugas.pemilikternak.PemilikTernakListActivity;
import com.zam.sidik_padang.home.sklb.print.CetakSertifikatActivity;
import com.zam.sidik_padang.roodiskusi.RoomDiskusiActivity;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.User;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.listmenu.HomeMenuView;
import io.paperdb.Paper;

import static com.zam.sidik_padang.home.newsinfo.NewsInfoActivity.PREF_RESPONSE_BERITA;

/**
 * Created by supriyadi on 5/5/17.
 */

public class MemberFragment extends Fragment
        implements View.OnClickListener, NewsContract.NewsView {

    private static final String EXTRA_LEVEL = "extra_level";
    User user;
    private int kelompok = 4;
    private OnUserInfoViewClickListener onUserInfoViewClickListener;
    private String TAG = MemberFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private HomeAdapter homeAdapter;
    private List<Berita> homeModelList;

    private NewsPresenter newsPresenter;
    private HomeMenuView homeMenuView, ppobMenuView;

    public static MemberFragment newInstance(int kelompok) {
        MemberFragment f = new MemberFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_LEVEL, kelompok);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUserInfoViewClickListener)
            onUserInfoViewClickListener = (OnUserInfoViewClickListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(EXTRA_LEVEL))
            kelompok = bundle.getInt(EXTRA_LEVEL);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(EXTRA_LEVEL, kelompok);
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(level == 4 ? R.layout.fragment_member : R.layout.fragment_member_koordinator, container, false);
        return inflater.inflate(R.layout.fragment_member, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<HomeMenuView.ItemMenu> homeMenuList = new ArrayList<>();

        if (kelompok == 4) {
            homeMenuList.add(new HomeMenuView.ItemMenu(R.drawable.ic_ternak, "Ternak", "1"));
            homeMenuList.add(new HomeMenuView.ItemMenu(R.drawable.ic_group, "Pemilik Ternak", "2"));
            homeMenuList.add(new HomeMenuView.ItemMenu(R.drawable.ic_print, "Cetak SKLB", "4"));
            homeMenuList.add(new HomeMenuView.ItemMenu(R.drawable.ic_news, "Berita/Info", "10"));
        } else if (kelompok == 5) {
            homeMenuList.add(new HomeMenuView.ItemMenu(R.drawable.ic_ternak, "Ternak", "1"));
            //homeMenuList.add(new HomeMenuView.ItemMenu(R.drawable.ic_group, "Pemilik Ternak", "2"));
            //homeMenuList.add(new HomeMenuView.ItemMenu(R.drawable.ic_baseline_assignment_24, "SKLB", "3"));
            //homeMenuList.add(new HomeMenuView.ItemMenu(R.drawable.ic_print, "Cetak SKLB", "4"));
            homeMenuList.add(new HomeMenuView.ItemMenu(R.drawable.ic_news, "Berita/Info", "10"));
        } else {
            homeMenuList.add(new HomeMenuView.ItemMenu(R.drawable.ic_baseline_assignment_ind_24, "Ternak IB", "5"));
            homeMenuList.add(new HomeMenuView.ItemMenu(R.drawable.ic_peternak, "Peternak", "6"));
            homeMenuList.add(new HomeMenuView.ItemMenu(R.drawable.ic_group, "Koordinator", "7"));
            homeMenuList.add(new HomeMenuView.ItemMenu(R.drawable.ic_kelompok, "Kelompok", "8"));
            //homeMenuList.add(new HomeMenuView.ItemMenu(R.drawable.ic_forum, "Diskusi", "9"));
            homeMenuList.add(new HomeMenuView.ItemMenu(R.drawable.ic_news, "Berita/Info", "10"));
        }

        homeMenuView = view.findViewById(R.id.homeMenu);
        homeMenuView.setCountSpace(4).setListMenu(homeMenuList).setListener((position, item) -> {
            Intent intent = null;
            switch (item.getId()) {
                case "1":
                    intent = new Intent(getActivity(), DataTernakActivity.class);
                    //intent = new Intent(getActivity(), PagingTestActivity.class);
                    break;
                case "2":
                    intent = new Intent(getActivity(), PemilikTernakListActivity.class);
                    break;
                case "3":
                    intent = new Intent(getActivity(), SklbActivity.class);
                    break;
                case "4":
                    intent = new Intent(getActivity(), CetakSertifikatActivity.class);
                    break;
                case "5":
                    intent = new Intent(getActivity(), TernakIbListActivity.class);
                    break;
                case "6":
                    intent = new Intent(getActivity(), DataPeternakActivity.class);
                    break;
                case "7":
                    intent = new Intent(getActivity(), KoordinatorActivity.class);
                    break;
                case "8":
                    intent = new Intent(getActivity(), KelompokTernakActivity.class);
                    break;
                case "9":
                    intent = new Intent(getActivity(), RoomDiskusiActivity.class);
                    break;
                case "10":
                    intent = new Intent(getActivity(), NewsInfoActivity.class);
                    break;
                case "11":
                    Toast.makeText(requireActivity(), item.getTitle() + " dalam pengembangan", Toast.LENGTH_SHORT).show();
                    break;
            }
            if (intent != null) startActivity(intent);
        }).buildMenu();

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String userJson = pref.getString(Config.PREF_USER_DETAIL_JSON, "");
        if (!userJson.isEmpty()) {
            user = new Gson().fromJson(userJson, User.class);
        }
        if (user != null) {
            view.findViewById(R.id.fragment_member_CardUserInfo).setOnClickListener(this);
            Log.d(getClass().getSimpleName(), "onViewCreated: FotoUrl:" + user.foto);
            if (!user.foto.isEmpty()) {
                Glide.with(getActivity())
                        .load(user.foto)
                        .error(R.drawable.ic_menu_utama_default_profile)
                        .into((ImageView) view.findViewById(R.id.fragment_member_ImageViewFoto));
            }
            ((TextView) view.findViewById(R.id.fragment_member_TextViewMemberName)).setText(user.nama);
            ((TextView) view.findViewById(R.id.fragment_member_TextViewMemberBarcode)).setText(user.userid);
            view.findViewById(R.id.fragment_member_menu_ternak).setOnClickListener(this);
            view.findViewById(R.id.fragment_member_menu_room_diskusi).setOnClickListener(this);
            view.findViewById(R.id.fragment_member_menu_berita).setOnClickListener(this);
            view.findViewById(R.id.fragment_member_menu_ppob).setOnClickListener(this);
            //if (user.kelompok == 4 || user.kelompok == 5) {
            //} else {
            //	((TextView) view.findViewById(R.id.fragment_member_TextViewMemberBarcode)).setText(user.nama + "\n" + user.userid);
            //}
        }

        view.postDelayed(() -> {
            ((ImageView)view.findViewById(R.id.fragment_member_ImageViewBarcode)).setImageBitmap(generateQr(user.userid));
            //new LoadBarcode(view.findViewById(R.id.fragment_member_ImageViewBarcode));
        }, 200);

        ((ImageView) view.findViewById(R.id.imageViewMenuTernak)).setImageResource(user.kelompok == 3 ? R.drawable.ic_farmer : R.drawable.ic_cow);

        //PPOB
        ppobMenuView = view.findViewById(R.id.rv_ppob_menu);
        createMenuPPOB();

        recyclerView = view.findViewById(R.id.recyclerview_recommended);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setNestedScrollingEnabled(false);

        homeModelList = new ArrayList<>();
        BeritaResponse beritaResponse = Paper.book().read(PREF_RESPONSE_BERITA);
        if (beritaResponse != null) {
            List<Berita> list = beritaResponse.getBerita();
            if (list != null) {
                for (int i = 0; i < 5; i++) {
                    homeModelList.add(beritaResponse.getBerita().get(i));
                }
            }
        }
        homeAdapter = new HomeAdapter(homeModelList);
        recyclerView.setAdapter(homeAdapter);

        newsPresenter = new NewsPresenter(this);
        Map<String, String> params = new HashMap<>();
        params.put("aksi", "1");
        params.put("userid", user.userid);
        newsPresenter.requestData(params);

        view.findViewById(R.id.all_news).setOnClickListener(v -> startActivity(new Intent(getActivity(), NewsInfoActivity.class)));
    }

    private void createMenuPPOB() {
        List<HomeMenuView.ItemMenu> menus = new ArrayList<>();
        menus.add(new HomeMenuView.ItemMenu(R.drawable.ic_wallet, "Saldo", "13"));
        menus.add(new HomeMenuView.ItemMenu(R.mipmap.ic_action_phone, "Isi Pulsa", "1"));
        menus.add(new HomeMenuView.ItemMenu(R.mipmap.ic_internet, "Internet", "2"));
        menus.add(new HomeMenuView.ItemMenu(R.mipmap.ic_flash, "Token PLN", "3"));
        menus.add(new HomeMenuView.ItemMenu(R.mipmap.ic_videogame, "Game", "4"));
        menus.add(new HomeMenuView.ItemMenu(R.drawable.ic_money, "Bayar Tagihan", "5"));
        menus.add(new HomeMenuView.ItemMenu(R.drawable.ic_news, "Info & Berita", "6"));
        menus.add(new HomeMenuView.ItemMenu(R.mipmap.ic_action_wallet, "Tambah Saldo", "7"));
        menus.add(new HomeMenuView.ItemMenu(R.mipmap.ic_action_list, "Transfer Saldo", "8"));
        menus.add(new HomeMenuView.ItemMenu(R.drawable.ic_business, "Koperasi", "9"));
        menus.add(new HomeMenuView.ItemMenu(R.drawable.ic_store, "Iklan", "10"));
        menus.add(new HomeMenuView.ItemMenu(R.mipmap.ic_action_mutasi, "Histori", "11"));
        menus.add(new HomeMenuView.ItemMenu(R.drawable.ic_settings, "Setting", "12"));

        ppobMenuView.setListMenu(menus).setListener((position, item) -> onClickPPOB(item.getId())).buildMenu();
    }

    /**
     * todo remove
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = null;
        if (id == R.id.fragment_member_menu_ternak || id == R.id.fragment_member_koordinator_Card1) {
            if (user.kelompok == 3) {
                intent = new Intent(getActivity(), TernakIbListActivity.class);
            } else
                intent = new Intent(getActivity(), DataTernakActivity.class);
        } else if (id == R.id.fragment_member_menu_room_diskusi) {
            intent = new Intent(getActivity(), RoomDiskusiActivity.class);
        } else if (id == R.id.fragment_member_menu_berita) {
            intent = new Intent(getActivity(), NewsInfoActivity.class);
        } else if (id == R.id.fragment_member_menu_ppob) {
            intent = new Intent(getActivity(), PPOBActivity.class);
        } else if (id == R.id.fragment_member_koordinator_Card2) {
            intent = new Intent(getActivity(), KoordinatorActivity.class);
        } else if (id == R.id.fragment_member_koordinator_Card3) {
            intent = new Intent(getActivity(), DataPeternakActivity.class);
        } else if (id == R.id.fragment_member_koordinator_Card4) {
            intent = new Intent(getActivity(), KelompokTernakActivity.class);
        } else if (id == R.id.fragment_member_CardUserInfo) {
            if (onUserInfoViewClickListener != null)
                onUserInfoViewClickListener.onUserInfoViewClick();
            return;
        }

        startActivity(intent);
    }

    @Override
    public void onRequestStart() {

    }

    @Override
    public void onRequestSuccess(BeritaResponse response) {
        //Log.d("NEWS", "HEADLINE:" + response.getHeadline().size() + ", BERITA:" + response.getBerita().size());
        Paper.book().write(PREF_RESPONSE_BERITA, response);
        homeModelList.clear();
        for (int i = 0; i < 5; i++) {
            homeModelList.add(response.getBerita().get(i));
        }
        homeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestError(String message) {

    }

    private Bitmap generateQr(String qr) {
        QRCodeWriter writer = new QRCodeWriter();
        Bitmap bmp = null;
        try {
            BitMatrix bitMatrix = writer.encode(qr, BarcodeFormat.QR_CODE, 400, 400);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
        } catch (WriterException e) {
            Log.e("GenerateQr", "WriteException");
        }
        return bmp;
    }

    public interface OnUserInfoViewClickListener {
        void onUserInfoViewClick();
    }

    private class LoadBarcode extends AsyncTask<Void, Void, Bitmap> {

        private ImageView imageView;
        private int viewWidth, viewHeight;

        LoadBarcode(ImageView imageView) {
            this.imageView = imageView;
            execute();
        }

        @Override
        protected void onPreExecute() {
            viewWidth = imageView.getWidth();
            viewHeight = imageView.getHeight();
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                return Util.encodeAsBitmap(user.userid, BarcodeFormat.QR_CODE, viewWidth, viewHeight);
            } catch (Exception e) {
                Log.e(getClass().getName(), e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) imageView.setImageBitmap(bitmap);
        }
    }

    public void onClickPPOB(String id) {
        Log.e("KLIK", id);
        switch (id) {
            case "1":
                startActivity(new Intent(requireActivity(), IsiPulsaActivity.class));
                break;
            case "2":
                startActivity(new Intent(requireActivity(), PaketInternetActivity.class));
                break;
            case "3":
                startActivity(new Intent(requireActivity(), TokenPlnActivity.class));
                break;
            case "4":
                startActivity(new Intent(requireActivity(), VoucherGameActivity.class));
                break;
            case "5":
                startActivity(new Intent(requireActivity(), BayarTagihanActivity.class));
                break;
            case "6":
                startActivity(new Intent(requireActivity(), BeritaActivity.class));
                break;
            case "7":
                startActivity(new Intent(requireActivity(), TambahSaldoActivity.class));
                break;
            case "8":
                startActivity(new Intent(requireActivity(), TransferSaldoActivity.class));
                break;
            case "9":
                startActivity(new Intent(requireActivity(), KoperasiActivity.class));
                break;
            case "10":
                startActivity(new Intent(requireActivity(), IklanMerchantActivity.class));
                break;
            case "11":
                startActivity(new Intent(requireActivity(), HistoryActivity.class));
                break;
            case "12":
                startActivity(new Intent(requireActivity(), SettingsActivity.class));
                break;
            case "13":
                startActivity(new Intent(requireActivity(), SaldoActivity.class));
                break;
        }
    }

}
