package com.zam.sidik_padang.home.sklb.print;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.databinding.ActivityCetakSertifikatBinding;
import com.zam.sidik_padang.home.sklb.print.sertifikat.SertifikatPagerAdapter;
import com.zam.sidik_padang.home.sklb.print.sertifikat.vm.SertifikatViewModel;

public class CetakSertifikatActivity extends BaseLogedinActivity {

    private ActivityCetakSertifikatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCetakSertifikatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.activityToolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Cetak Sertifikat");
        toolbar.setNavigationOnClickListener(v -> finish());

        ViewPager viewPager = binding.activitySklbViewPager;
        SertifikatPagerAdapter adapter = new SertifikatPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = binding.activitySklbTabLayout;
        tabLayout.setupWithViewPager(viewPager);

        SertifikatViewModel viewModel = new ViewModelProvider(this).get(SertifikatViewModel.class);
        viewModel.getSertifikat(user.userid);
    }
}