package com.zam.sidik_padang.home.sklb;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.databinding.ActivitySklbBinding;
import com.zam.sidik_padang.home.sklb.dataternak.DataTernakFragment;
import com.zam.sidik_padang.home.sklb.petugas.PetugasFragment;
import com.zam.sidik_padang.home.sklb.setting.kuantitatif.KuantitatifListFragment;
import io.paperdb.Paper;

public class SklbActivity extends BaseLogedinActivity {

    public static final String PREF_RESPONSE_PETUGAS_DINAS = "response_petugas_dinas";
    private ActivitySklbBinding binding;

    private final FragmentManager fm = getSupportFragmentManager();
    private Fragment[] fragments;
    private Fragment fragmentActive;
    public int layoutPosition, positionClickMenu;
    private String[] subtitles = {"Petugas/Peternak", "Data Ternak", "Pengaturan"};
    private boolean hasInten;

    //private Contract.Presenter presenter;

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        if (item.getItemId() == R.id.navigation_petugas) {
            switchFragment(fragments[0], 1);
            return true;
        } else if (item.getItemId() == R.id.navigation_data_ternak) {
            switchFragment(fragments[1], 2);
            return true;
        } else if (item.getItemId() == R.id.navigation_sertifikat) {
            switchFragment(fragments[2], 3);
            return true;
        }
        return false;
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySklbBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.activityToolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("SKLB");
        toolbar.setNavigationOnClickListener(v -> finish());
        binding.navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        hasInten = getIntent().hasExtra("dinas");
        if (hasInten) {
            createFragfment(3);
            binding.navView.setSelectedItemId(R.id.navigation_sertifikat);
            getSupportActionBar().setSubtitle(subtitles[2]);
        } else {
            getSupportActionBar().setSubtitle(subtitles[0]);
            createFragfment(1);
        }
        //presenter = new Presenter(this);
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_more, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_pemilik_ternak) {
            startActivity(new Intent(this, PemilikTernakListActivity.class));
            return true;
        } else return false;
    }
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void createFragfment(int pos) {
        fragments = new Fragment[]{new PetugasFragment(), new DataTernakFragment(), new KuantitatifListFragment()};
        for (int f = 0; f < fragments.length; f++) {
            fm.beginTransaction().add(R.id.frame, fragments[f], "fragment" + f).hide(fragments[f]).commit();
        }

        fm.beginTransaction().show(fragments[pos-1]).commit();
        fragmentActive = fragments[pos-1];
        layoutPosition = pos;
    }

    private void switchFragment(Fragment fragment, int pos) {
        positionClickMenu = pos;
        if (fragmentActive != fragment) {
            int enterNext = layoutPosition < positionClickMenu ? R.anim.enter_from_left : R.anim.enter_from_right;
            int exitNext = layoutPosition < positionClickMenu ? R.anim.exit_to_left : R.anim.exit_to_right;

            int exitCurrent = layoutPosition < positionClickMenu ? R.anim.exit_to_right : R.anim.exit_to_left;
            int enterCurrent = layoutPosition < positionClickMenu ? R.anim.enter_from_right : R.anim.enter_from_left;

            fm.beginTransaction()
                    .setCustomAnimations(enterNext, exitCurrent, exitNext, enterCurrent)
                    .hide(fragmentActive).show(fragment).commit();
            fragmentActive = fragment;
        }
        layoutPosition = pos;
        getSupportActionBar().setSubtitle(subtitles[pos - 1]);
    }

    private Fragment getDisplayedFragment(int fragPos) {
        return fm.findFragmentByTag("fragment" + fragPos);
    }

    private void removeFragment() {
        for (int f = 0; f < fragments.length; f++) {
            if (getDisplayedFragment(f) != null) {
                fm.beginTransaction().remove(fragments[f]).commit();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (hasInten) {
            finish();
        } else {
            if (layoutPosition > 1) {
                //switchFragment(fragments[0], 1);
                binding.navView.setSelectedItemId(R.id.navigation_petugas);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeFragment();
        Paper.book().delete(PREF_RESPONSE_PETUGAS_DINAS);
    }
}
