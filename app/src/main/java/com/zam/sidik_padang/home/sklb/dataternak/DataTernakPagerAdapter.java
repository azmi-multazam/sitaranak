package com.zam.sidik_padang.home.sklb.dataternak;

import androidx.collection.SparseArrayCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

public class DataTernakPagerAdapter extends FragmentPagerAdapter {

    private final String[] pageTitles = {"Petugas", "Peternak Umum"};
    private final SparseArrayCompat<Fragment> sparseArray;

    public DataTernakPagerAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        sparseArray = new SparseArrayCompat<>();
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        Fragment f;
        if (position == 0) f = new KhususFragment();
        else f = new UmumFragment();
        sparseArray.put(position, f);
        return f;
    }

    public Fragment getFragment(int position) {
        return sparseArray.get(position, null);
    }

    @Override
    public int getCount() {
        return pageTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitles[position];
    }
}
