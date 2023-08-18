package com.zam.sidik_padang.home.ppob.history;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.zam.sidik_padang.home.ppob.history.deposit.HistoryDepositFragment;
import com.zam.sidik_padang.home.ppob.history.game.HistoryGameFragment;
import com.zam.sidik_padang.home.ppob.history.mutasi.HistoryMutasiFragment;
import com.zam.sidik_padang.home.ppob.history.pulsa.HistoryPulsaFragment;
import com.zam.sidik_padang.home.ppob.history.tagihan.HistoryTagihanFragment;


/**
 * Created by supriyadi on 7/13/17.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private static final String[] titles = {"Pulsa", "Deposit", "Tagihan", "Game", "Mutasi"};

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) return new HistoryPulsaFragment();
        if (position == 1) return new HistoryDepositFragment();
        if (position == 2) return new HistoryTagihanFragment();
        if (position == 3) return new HistoryGameFragment();
        return new HistoryMutasiFragment();
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
