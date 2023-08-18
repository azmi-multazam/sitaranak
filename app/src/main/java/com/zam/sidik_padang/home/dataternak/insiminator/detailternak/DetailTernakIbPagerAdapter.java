package com.zam.sidik_padang.home.dataternak.insiminator.detailternak;

import androidx.collection.SparseArrayCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.zam.sidik_padang.home.dataternak.detailternak.DataSapihFragment;
import com.zam.sidik_padang.home.dataternak.detailternak.foto.FotoTernakFragment;
import com.zam.sidik_padang.home.dataternak.detailternak.riwayat.RiwayatFragment;
import com.zam.sidik_padang.home.dataternak.insiminator.TernakIb;

/**
 * Created by supriyadi on 4/17/18.
 */

public class DetailTernakIbPagerAdapter extends FragmentPagerAdapter {

    private static final String[] pageTitles = {
            "Detail", "Foto", "Keterangan", "Riwayat"
    };
    private TernakIb ternakIb;
    private String from = "home";
    private SparseArrayCompat<Fragment> sparseArray;

    public DetailTernakIbPagerAdapter(FragmentManager fm, TernakIb ternakIb) {
        super(fm);
        this.ternakIb = ternakIb;
        sparseArray = new SparseArrayCompat<>();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f;
        if (position == 0) f = DetailTernakIbFragment.getInstance(ternakIb);
        else if (position == 1) f = FotoTernakFragment.newInstance(ternakIb.id_ternak, from);
        else if (position == 2) f = DataSapihFragment.getInstance(ternakIb.id_ternak, from);
        else f = RiwayatFragment.newInstance(ternakIb.id_ternak, from);
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
