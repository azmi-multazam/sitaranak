package com.zam.sidik_padang.home.newsinfo;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

import com.zam.sidik_padang.home.memberhome.mvp.Headline;

/**
 * Created by supriyadi on 10/11/17.
 */

public class HeaderPagerAdapter extends FragmentPagerAdapter {
    private List<Headline> list;

    public HeaderPagerAdapter(FragmentManager fm, List<Headline> list) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return HeaderFragment.getInstance(list.get(position));
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
