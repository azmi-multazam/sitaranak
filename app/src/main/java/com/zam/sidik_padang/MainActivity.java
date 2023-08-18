package com.zam.sidik_padang;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.InstanceIdResult;

import com.zam.sidik_padang.home.memberhome.MemberFragment;
import com.zam.sidik_padang.home.memberhome.MemberFragment.OnUserInfoViewClickListener;
import com.zam.sidik_padang.login.LoginActivity;
import com.zam.sidik_padang.profilku.ProfileFragment;
import com.zam.sidik_padang.sapiku.SapikuFragment;
import com.zam.sidik_padang.ubahsandi.UbahSandiFragment;
import com.zam.sidik_padang.util.Util;

/**
 * Created by supriyadi on 5/4/17.
 */

public class MainActivity extends BaseLogedinActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        //GridAdapter.OnGridItemClickListener,
        OnUserInfoViewClickListener {

    private DrawerLayout drawerLayout;
    private NavigationView nv;
    private Toolbar toolbar;

    private FragmentManager fragmentManager;
    private Mode mode = Mode.HOME;

    @Override
    public void onUserInfoViewClick() {
        showProfileFragment();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        if (!Util.isLogedin(this)) {
            Intent it = new Intent(this, LoginActivity.class);
            it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            it.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(it);
            finish();
        } else {

            setContentView(R.layout.activity_main);
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            nv = findViewById(R.id.activity_main_NavigationView);
            nv.setNavigationItemSelectedListener(this);

            if (user != null) {
                ((TextView) nv.getHeaderView(0).findViewById(R.id.header_activity_main_TexViewUserName)).setText(user.nama);
                ((TextView) nv.getHeaderView(0).findViewById(R.id.header_activity_main_TexViewUserId)).setText(user.userid);
            }

            drawerLayout = findViewById(R.id.activity_main_DrawerLayout);
            String prefFirstTime = "prefFirstTime";
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            if (preferences.getBoolean(prefFirstTime, true)) {
                drawerLayout.openDrawer(GravityCompat.END, true);
                preferences.edit().putBoolean(prefFirstTime, false).apply();
            }

            fragmentManager = getSupportFragmentManager();
            showMemberFragment();

            //if (user != null) saveTokenToServer();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (user != null && !user.foto.isEmpty()) {
            ImageView imageView = nv.getHeaderView(0).findViewById(R.id.header_activity_main_ImageView);
            Glide.with(this).load(user.foto).placeholder(R.drawable.ic_account_circle_white).into(imageView);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem p1) {
        int id = p1.getItemId();
        if (id == R.id.activity_main_navigationview_home) {
            if (mode != Mode.HOME) showMemberFragment();
        } else if (id == R.id.activity_main_navigationview_logout) {
            new AlertDialog.Builder(this).setTitle(R.string.logout_).setMessage(user.nama)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(R.string.logout, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Util.logout(MainActivity.this);
                        }
                    }).show();
        } else if (id == R.id.activity_main_navigationview_profilku) {
            if (mode != Mode.PROFILEKU) showProfileFragment();
        } else if (id == R.id.activity_main_navigationview_sapiku) {
            if (mode != Mode.SAPIKU) showSapikuFragment();
        } else if (id == R.id.activity_main_navigationview_ubah_kata_sandi) {
            if (mode != Mode.UBAH_SANDI) showUbahSandiFragment();
		}/* else if (id == R.id.activity_main_navigationview_anggota) {
        } else if (id == R.id.activity_main_navigationview_kegiatan) {
            Toast.makeText(this, "Dalam pengembangan", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.activity_main_navigationview_bantuan) {
            Toast.makeText(this, "Dalam pengembangan", Toast.LENGTH_SHORT).show();
            //startActivity(new Intent(this, DaftarStep3Activity.class));
        } else if (id == R.id.activity_main_navigationview_info_aplikasi) {
            Toast.makeText(this, "Dalam pengembangan", Toast.LENGTH_SHORT).show();
            //startActivity(new Intent(this, TambahPeternakActivity.class));
        }
         */
        bukaTutupDrawer();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.activity_main_OptionButton) {
            bukaTutupDrawer();
            return true;
        } else return false;

    }

    private void bukaTutupDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END, true);
        } else drawerLayout.openDrawer(GravityCompat.END, true);
    }

    private void showMemberFragment() {
        mode = Mode.HOME;
        FragmentTransaction ft = newFragmentTransaction();
        ft.replace(R.id.activity_main_FrameLayoutContainer, MemberFragment.newInstance(user.kelompok), null);
        ft.commit();
        nv.setCheckedItem(R.id.activity_main_navigationview_home);
        toolbar.setTitle(R.string.member_area);
    }

    private void showProfileFragment() {
        mode = Mode.PROFILEKU;
        FragmentTransaction ft = newFragmentTransaction();
        ft.replace(R.id.activity_main_FrameLayoutContainer, ProfileFragment.newInstance(user), null);
        ft.commit();
        toolbar.setTitle(R.string.profile);
    }

    private void showSapikuFragment() {
        mode = Mode.SAPIKU;
        FragmentTransaction ft = newFragmentTransaction();
        ft.replace(R.id.activity_main_FrameLayoutContainer, new SapikuFragment(), null);
        ft.commit();
        toolbar.setTitle("Jumlah Sapiku");
    }

    private void showUbahSandiFragment() {
        mode = Mode.UBAH_SANDI;
        FragmentTransaction ft = newFragmentTransaction();
        ft.replace(R.id.activity_main_FrameLayoutContainer, new UbahSandiFragment(), null);
        ft.commit();
        toolbar.setTitle("Ubah Kata Sandi");
    }

    /*
    public void onGridItemClick(int position) {
        Intent intent;
        if (user.level_sesi == 4) {
            if (!Util.isInternetAvailible(this)) {
                Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                return;
            }
            if (position == 0) {
                intent = new Intent(this, DataTernakActivity.class);
            } else if (position == 1) {
                intent = new Intent(this, DataRecordingTernakActivity.class);
            } else if (position == 2) {
                intent = new Intent(this, RoomDiskusiActivity.class);
            } else {
                intent = new Intent(this, NewsInfoActivity.class);
            }
        } else {
            if (!Util.isInternetAvailible(this)) {
                Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                return;
            }
            if (position == 0) {
                intent = new Intent(this, KelompokTernakActivity.class);
            } else if (position == 1) {
                intent = new Intent(this, KoordinatorActivity.class);
            } else if (position == 2) {
                intent = new Intent(this, DataPeternakActivity.class);
            } else intent = new Intent(this, DataTernakDibawahnyaActivity.class);

        }

        startActivity(intent);
    }
     */

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END, true);
        } else if (mode != Mode.HOME) {
            showMemberFragment();
        } else
            super.onBackPressed();
    }

    private FragmentTransaction newFragmentTransaction() {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        return ft;
    }

    /*
    private void saveTokenToServer() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                if (token.isEmpty()) {
                    debug(getClass(), "onCreate. Token empty");
                    return;
                }
                if (!Util.isOffline(MainActivity.this)) {
                    firestore.collection("users").document(user.userid).update("fcm", token)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    debug(getClass(), "Update token complite");
                                }
                            });
                }
            }
        });
    }
     */


    private enum Mode {
        HOME, PROFILEKU, SAPIKU, KEGIATAN_HARI_INI, UBAH_SANDI, BANTUAN, INFO_APLIKASI;
    }


}
