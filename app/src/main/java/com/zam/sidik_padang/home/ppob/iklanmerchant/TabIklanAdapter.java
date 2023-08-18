package com.zam.sidik_padang.home.ppob.iklanmerchant;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.zam.sidik_padang.home.ppob.iklanmerchant.iklanpremium.IklanPremiumListFragment;
import com.zam.sidik_padang.home.ppob.iklanmerchant.iklanpribadi.IklanPribadiListFragment;
import com.zam.sidik_padang.home.ppob.iklanmerchant.iklanumum.IklanUmumListFragment;


/**
 * Created by supriyadi on 3/8/18.
 */

public class TabIklanAdapter extends FragmentPagerAdapter {


    public TabIklanAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return position == 0 ? new IklanPremiumListFragment() : position == 1 ? new IklanUmumListFragment() : new IklanPribadiListFragment();
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return position == 0 ? "Premium" : position == 1 ? "Umum" : "Iklanku";
    }
}
