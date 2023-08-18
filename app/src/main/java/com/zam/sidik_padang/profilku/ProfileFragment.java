package com.zam.sidik_padang.profilku;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.IdCardPrintActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.api.State;
import com.zam.sidik_padang.profilku.model.LihatProfil;
import com.zam.sidik_padang.profilku.model.Member;
import com.zam.sidik_padang.profilku.model.ProfileResponse;
import com.zam.sidik_padang.profilku.model.ProfileViewModel;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.User;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.model.ProfileApiResult;
import io.paperdb.Paper;

import static com.zam.sidik_padang.util.Config.PREF_PROFIL_TERSIMPAN;

public class ProfileFragment extends Fragment {

    private static final String EXTRA_USER = "extra_user";
    private final String VOLLEY_TAG = getClass().getName();

    private ProfileViewModel viewModel;
    private User user;
    private View progressBar;
    private View rootView;
    private LihatProfil profile = null;
    private final ProfileApiResult profileApiResult = null;
    private SharedPreferences pref;
    private ImageView imageView;

    private Button btnPeternak, btnUsaha, btnAlamat;
    private CardView cardPeternak, cardUsaha, cardAlamat;
    private NestedScrollView profileScroll;
    private boolean edited, isLoading;

    public ProfileFragment() {

    }

    public static ProfileFragment newInstance(User user) {
        ProfileFragment profileFragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_USER, user);
        profileFragment.setArguments(bundle);
        return profileFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(EXTRA_USER)) {
            user = (User) bundle.getSerializable(EXTRA_USER);
        }
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (isLoading) {
            Toast.makeText(requireContext(), "Sedang memuat data...", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (id == R.id.action_edit) {
                if (profile == null) {
                    Util.showDialog(getActivity(), "Data tidak termuat dengan benar.\nSilakan buka ulang halaman ini");
                    return false;
                }
                Intent it = new Intent(getActivity(), EditProfileActivity.class);
                //it.putExtra(EditProfileActivity.EXTRA_PROFILE_API_RESULT, profileApiResult);
                startActivityForResult(it, 23);
            } else if (id == R.id.action_print) {
                Intent intent = new Intent(this.getActivity(), IdCardPrintActivity.class);
                intent.putExtra("kota", profile.getKabupaten());
                intent.putExtra("tgl_lahir", profile.getTanggalLahir());
                intent.putExtra("tgl_daftar", profile.getTglDaftar());
                intent.putExtra("id_kelompok_ternak", profile.getIdKelompokTernak());
                startActivity(intent);
            }
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (user != null) outState.putSerializable(EXTRA_USER, user);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: Implement this method
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
        profileScroll = view.findViewById(R.id.profile_scrollview);
        imageView = view.findViewById(R.id.header_activity_main_ImageView);
        Drawable drawable;
        if (user.kelompok == 3) {
            drawable = AppCompatResources.getDrawable(getActivity(), R.drawable.ic_petugas_ib);
        } else {
            drawable = AppCompatResources.getDrawable(getActivity(), R.drawable.ic_account_circle_white);
            int b = (int) (getResources().getDisplayMetrics().density * 80f);
            drawable.setBounds(0, 0, b, b);
        }
        imageView.setImageDrawable(drawable);

        cardPeternak = view.findViewById(R.id.profile_data_peternak);
        cardUsaha = view.findViewById(R.id.profile_data_usaha);
        cardAlamat = view.findViewById(R.id.profile_data_alamat);
        btnPeternak = view.findViewById(R.id.btn_ternak);
        btnUsaha = view.findViewById(R.id.btn_usaha);
        btnAlamat = view.findViewById(R.id.btn_alamat);
        btnPeternak.setSelected(true);
        btnPeternak.setOnClickListener(v -> showDataPeternak());
        btnUsaha.setOnClickListener(v -> showDataUsaha());
        btnAlamat.setOnClickListener(v -> showDataAlamat());

        ((TextView) view.findViewById(R.id.fragment_profile_TesxViewUserName)).setText(user.nama);
        ((TextView) view.findViewById(R.id.fragment_profile_TesxViewUserId)).setText(user.userid);
        //((TextView) view.findViewById(R.id.fragment_profile_TextViewKelompok)).setText(String.valueOf(user.kelompok));
        //((TextView) view.findViewById(R.id.fragment_profile_TextViewKelompokTernak)).setText(String.valueOf(user.kelompok_ternak));
        //((TextView) view.findViewById(R.id.fragment_profile_TextViewLevel)).setText(String.valueOf(user.level));
        progressBar = view.findViewById(R.id.fragment_profile_ProgressBar);

        rootView = view;
        if (user == null) return;
        /*
			String profileTersimpan = pref.getString(PREF_PROFIL_TERSIMPAN, null);
			if (profileTersimpan != null) {
				JsonElement je = new Gson().fromJson(profileTersimpan, JsonElement.class);
				if (je != null) updateUI(je.getAsJsonObject());
			}
			 */
        rootView.postDelayed(this::observe, 500);
    }

    private void observe() {
        viewModel.getResponseProfile().observe(getViewLifecycleOwner(), response -> {
            if (response != null) {
                isLoading = response.state == State.LOADING;
                if (progressBar != null && progressBar.getVisibility() == View.VISIBLE) {
                    progressBar.setVisibility(View.GONE);
                }
                if (response.state == State.SUCCESS && response.success && response.data != null) {
                    debug(response.message);
                    updateUI(response.data);
                } else if (response.state == State.LOADING) {
                    if (progressBar != null && progressBar.getVisibility() == View.GONE) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    debug(response.message);
                } else {
                    Toast.makeText(requireContext(), "FAILURE", Toast.LENGTH_SHORT).show();
                    debug("error_message: " + response.message);
                }
            }
        });

        ProfileResponse presponse = Paper.book().read(PREF_PROFIL_TERSIMPAN);
        if (presponse != null) {
            updateUI(presponse);
        }
        getProfileDataFromServer(false);
    }

    private void showDataAlamat() {
        btnPeternak.setSelected(false);
        btnUsaha.setSelected(false);
        btnAlamat.setSelected(true);

        cardPeternak.setVisibility(View.GONE);
        cardUsaha.setVisibility(View.GONE);
        cardAlamat.setVisibility(View.VISIBLE);
    }

    private void showDataUsaha() {
        btnPeternak.setSelected(false);
        btnUsaha.setSelected(true);
        btnAlamat.setSelected(false);

        cardPeternak.setVisibility(View.GONE);
        cardUsaha.setVisibility(View.VISIBLE);
        cardAlamat.setVisibility(View.GONE);
    }

    private void showDataPeternak() {
        btnPeternak.setSelected(true);
        btnUsaha.setSelected(false);
        btnAlamat.setSelected(false);

        cardPeternak.setVisibility(View.VISIBLE);
        cardUsaha.setVisibility(View.GONE);
        cardAlamat.setVisibility(View.GONE);
    }

    private void updateUI(ProfileResponse data) {
        data.getLihatProfil().get(0).setEmail(Util.decodeUrl(data.getLihatProfil().get(0).getEmail()));
        profile = data.getLihatProfil().get(0);
        if (profile.getFoto() != null && !profile.getFoto().isEmpty()) {
            Glide.with(getActivity()).load(profile.getFoto()).placeholder(R.drawable.ic_account_circle_white).into(imageView);
        }
        ((TextView) rootView.findViewById(R.id.fragment_profile_TesxViewUserName)).setText(profile.getNama());
        ((TextView) rootView.findViewById(R.id.fragment_profile_TextViewAgama)).setText(profile.getAgama());
        ((TextView) rootView.findViewById(R.id.fragment_profile_TextViewUmur)).setText(profile.getUmur() + " Tahun");
        ((TextView) rootView.findViewById(R.id.fragment_profile_TextViewNoHP)).setText(profile.getHp());
        ((TextView) rootView.findViewById(R.id.fragment_profile_TextViewNoKTP)).setText(profile.getKtp());
        ((TextView) rootView.findViewById(R.id.fragment_profile_TextViewDesa)).setText(profile.getDesa());
        ((TextView) rootView.findViewById(R.id.fragment_profile_TextViewKecamatan)).setText(profile.getKecamatan());
        ((TextView) rootView.findViewById(R.id.fragment_profile_TextViewKabupaten)).setText(profile.getKabupaten());
        ((TextView) rootView.findViewById(R.id.fragment_profile_TextViewProvinsi)).setText(profile.getProvinsi());
        //((TextView) rootView.findViewById(R.id.fragment_profile_TextViewKelompok)).setText(profile.getKelompok());
        //((TextView) rootView.findViewById(R.id.fragment_profile_TextViewKelompokTernak)).setText(profile.getKelompokTernak());
        //((TextView) rootView.findViewById(R.id.fragment_profile_TextViewLevel)).setText(user.level);
        ((TextView) rootView.findViewById(R.id.fragment_profile_TextViewNamaUsaha)).setText(profile.getNamaUsaha());
        ((TextView) rootView.findViewById(R.id.fragment_profile_TextViewNoTelepon)).setText(profile.getTelepon());
        ((TextView) rootView.findViewById(R.id.fragment_profile_TextViewEmail)).setText(profile.getEmail());
        ((TextView) rootView.findViewById(R.id.fragment_profile_TextViewKomoditas)).setText(profile.getKomoditas());
        ((TextView) rootView.findViewById(R.id.fragment_profile_TextViewJenisUsaha)).setText(profile.getJenisUsaha());
        ((TextView) rootView.findViewById(R.id.fragment_profile_TextViewProdukPenjualan)).setText(profile.getProdukPenjualan());
        new LoadMap(profile.getDesa() + " " + profile.getKecamatan() + " " + profile.getKabupaten() + " " + profile.getProvinsi() + " Indonesia");

        List<Member> userMember = data.getMember();
        if (edited && userMember != null && userMember.size() > 0) {
            Member mb = userMember.get(0);
            String member = new Gson().toJson(mb);
            JsonObject jsonUser = new Gson().fromJson(member, JsonElement.class).getAsJsonObject();
            Util.saveUserDetail(getActivity(), jsonUser);
            User newUser = new Gson().fromJson(jsonUser, User.class);
            user = newUser;
            ((BaseLogedinActivity) getActivity()).user = newUser;
        }
        //pref.edit().putString(PREF_PROFIL_TERSIMPAN, jsonObject.toString()).apply();
        Paper.book().write(PREF_PROFIL_TERSIMPAN, data);
    }

	/*
	private void updateUI(JsonObject jsonObject) {
		JsonElement je = jsonObject.get("lihat_profil");
		if (je != null) {
			JsonArray ja = je.getAsJsonArray();
			if (ja.size() > 0) {
				profileApiResult = new Gson().fromJson(jsonObject, ProfileApiResult.class);
				profile = profileApiResult.lihat_profil.get(0);
				if (profile.foto != null && !profile.foto.isEmpty())
					Glide.with(getActivity()).load(profile.foto).placeholder(R.drawable.ic_account_circle_white)
							.into(imageView);
				((TextView) rootView.findViewById(R.id.fragment_profile_TesxViewUserName)).setText(profile.nama);
				((TextView) rootView.findViewById(R.id.fragment_profile_TextViewAgama)).setText(profile.agama);
				((TextView) rootView.findViewById(R.id.fragment_profile_TextViewUmur)).setText(profile.umur + " Tahun");
				((TextView) rootView.findViewById(R.id.fragment_profile_TextViewNoHP)).setText(profile.hp);
				((TextView) rootView.findViewById(R.id.fragment_profile_TextViewNoKTP)).setText(profile.ktp);
				((TextView) rootView.findViewById(R.id.fragment_profile_TextViewDesa)).setText(profile.desa);
				((TextView) rootView.findViewById(R.id.fragment_profile_TextViewKecamatan)).setText(profile.kecamatan);
				((TextView) rootView.findViewById(R.id.fragment_profile_TextViewKabupaten)).setText(profile.kabupaten);
				((TextView) rootView.findViewById(R.id.fragment_profile_TextViewProvinsi)).setText(profile.provinsi);
				((TextView) rootView.findViewById(R.id.fragment_profile_TextViewKelompok)).setText(profile.kelompok);
				((TextView) rootView.findViewById(R.id.fragment_profile_TextViewKelompokTernak)).setText(profile.kelompok_ternak);
				((TextView) rootView.findViewById(R.id.fragment_profile_TextViewLevel)).setText(user.level);
				((TextView) rootView.findViewById(R.id.fragment_profile_TextViewNamaUsaha)).setText(profile.nama_usaha);
//							((TextView) rootView.findViewById(R.id.fragment_profile_TextViewProvinsi)).setText(profile.sta);
				((TextView) rootView.findViewById(R.id.fragment_profile_TextViewNoTelepon)).setText(profile.telepon);
				((TextView) rootView.findViewById(R.id.fragment_profile_TextViewEmail)).setText(profile.email);
				((TextView) rootView.findViewById(R.id.fragment_profile_TextViewKomoditas)).setText(profile.komoditas);
				((TextView) rootView.findViewById(R.id.fragment_profile_TextViewJenisUsaha)).setText(profile.jenis_usaha);
				((TextView) rootView.findViewById(R.id.fragment_profile_TextViewProdukPenjualan)).setText(profile.produk_penjualan);
				new LoadMap(profile.desa + " " + profile.kecamatan + " " + profile.kabupaten + " " + profile.provinsi + " Indonesia");
			}
		}
	}
	 */

    private void getProfileDataFromServer(final boolean edited) {
        if (!Util.isInternetAvailible(getActivity())) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(requireContext(), "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
            return;
        }
        this.edited = edited;
        Map<String, String> map = new HashMap<>();
        map.put("userid", user.userid);
        map.put("aksi", "2");
        viewModel.getProfile(map);
		/*
		String url = Config.URL_PROFILE;
		url += "?userid=" + user.userid;
		url += "&aksi=2";
		debug("getProfile from server url: " + url);
		AndroidNetworking.get(url).build().getAsString(new StringRequestListener() {
			@Override
			public void onResponse(String response) {
				progressBar.setVisibility(View.GONE);
				if (!response.startsWith("{")){
					Toast.makeText(getContext(), "Response from server error", Toast.LENGTH_SHORT).show();
				}else {
					JsonObject jsonObject = new Gson().fromJson(response, JsonElement.class).getAsJsonObject();
					if (jsonObject.get("success").getAsBoolean()) {
						updateUI(jsonObject);
						JsonElement userElement = jsonObject.get("member");
						if (edited && userElement != null) {
							JsonArray ja = userElement.getAsJsonArray();
							if (ja.size() > 0) {
								JsonObject jsonUser = ja.get(0).getAsJsonObject();
								Util.saveUserDetail(getActivity(), jsonUser);
								User newUser = new Gson().fromJson(jsonUser, User.class);
								user = newUser;
								((BaseLogedinActivity) getActivity()).user = newUser;
							}
						}
						pref.edit().putString(PREF_PROFIL_TERSIMPAN, jsonObject.toString()).apply();
					}
				}
			}

			@Override
			public void onError(ANError anError) {
				progressBar.setVisibility(View.GONE);
				Toast.makeText(getContext(), anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
			}
		});
		 */
    }
//	private void getProfileDataFromServer(final boolean edited) {
//		if (!Util.isInternetAvailible(getActivity())) return;
//
//		String url = Config.URL_PROFILE;
//		url += "?userid=" + user.userid;
//		url += "&aksi=2";
//		debug("getProfile from server url: " + url);
//
//		VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
//			@Override
//			public void onResponse(JsonObject jsonObject) {
//				debug("getProfile from server onresponse: " + jsonObject);
//				progressBar.setVisibility(View.GONE);
//				if (jsonObject.get("success").getAsBoolean()) {
//					updateUI(jsonObject);
//					JsonElement userElement = jsonObject.get("member");
//					if (edited && userElement != null) {
//						JsonArray ja = userElement.getAsJsonArray();
//						if (ja.size() > 0) {
//							JsonObject jsonUser = ja.get(0).getAsJsonObject();
//							Util.saveUserDetail(getActivity(), jsonUser);
//							User newUser = new Gson().fromJson(jsonUser, User.class);
//							user = newUser;
//							((BaseLogedinActivity) getActivity()).user = newUser;
//						}
//					}
//					pref.edit().putString(PREF_PROFIL_TERSIMPAN, jsonObject.toString()).apply();
//				}
//			}
//		});
//		request.setTag(VOLLEY_TAG);
//		VolleySingleton.getInstance(getActivity()).getRequestQueue().add(request);
//	}

    void debug(String s) {
        //if (Config.DEBUG)
        Log.d(getClass().getName(), s);
    }

    @Override
    public void onDetach() {
        VolleySingleton.getInstance(getActivity()).getRequestQueue().cancelAll(VOLLEY_TAG);
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 23 && resultCode == Activity.RESULT_OK && !isDetached()) {
            getProfileDataFromServer(true);
        }
    }

    private class LoadMap extends AsyncTask<Void, Void, Address> {

        private GoogleMap map;
        private final String stringAddress;

        public LoadMap(String stringAddress) {
            this.stringAddress = stringAddress;

            MyMapFragment mSupportMapFragment = (MyMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_profile_MapView);
            if (mSupportMapFragment != null) {
                mSupportMapFragment.getMapAsync(googleMap -> map = googleMap);
                mSupportMapFragment.setListener(() -> profileScroll.requestDisallowInterceptTouchEvent(true));
            }
			/*
			((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_profile_MapView)).getMapAsync(new OnMapReadyCallback() {
				@Override
				public void onMapReady(GoogleMap googleMap) {
					map = googleMap;
				}
			});
			 */
            execute();
        }

        @Override
        protected Address doInBackground(Void... params) {
            debug("Load map do in background address== " + stringAddress);
            try {
                List<Address> addressList = null;
                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                addressList = geocoder.getFromLocationName(stringAddress, 1);
                if (addressList != null && addressList.size() > 0) return addressList.get(0);
            } catch (Exception e) {
                Log.e(getClass().getName(), e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Address address) {
            debug("Load map postexecute address= " + address);
            if (address != null) {
                if (map != null) {
                    zoomMap(map, address);
                } else
                    ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_profile_MapView)).getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            zoomMap(googleMap, address);
                        }
                    });
            }
        }

        private void zoomMap(GoogleMap googleMap, Address address) {
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Lokasi Peternakan");
            googleMap.addMarker(markerOptions);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
        }
    }
}


/*
http://e-rekording.com/android_api/profil.php?userid=KS1000002&aksi=2
{"lihat_profil":[
                 {"nama":"Ferry Irawan",
				  "ktp":"12345678",
				  "id_provinsi":"6728",
				  "provinsi":"SUMATERA UTARA",
				  "id_kabupaten":"8688",
				  "kabupaten":"DELI SERDANG",
				  "id_kecamatan":"8973",
				  "kecamatan":"SUNGGAL",
				  "id_desa":"8974",
				  "desa":"SEI SEMAYANG",
				  "hp":"+6285760525722"}
				 ],
	"success":true,
	"message":" berhasil"
}
*/
