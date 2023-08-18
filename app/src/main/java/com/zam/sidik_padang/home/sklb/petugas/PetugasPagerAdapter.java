package com.zam.sidik_padang.home.sklb.petugas;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

public class PetugasPagerAdapter extends FragmentPagerAdapter {

    private final String[] pageTitles = {
            "Petugas", "Peternak Umum"
    };

    public PetugasPagerAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return KhususFragment.newInstance();
        } else {
            return UmumFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return pageTitles.length;
    }

    @Override
    public int getItemPosition(@NotNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitles[position];
    }
}
