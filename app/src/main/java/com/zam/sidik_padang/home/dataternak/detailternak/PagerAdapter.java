package com.zam.sidik_padang.home.dataternak.detailternak;

import androidx.collection.SparseArrayCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.zam.sidik_padang.home.dataternak.detailternak.datarecording.DataRecordingTernakFragment;
import com.zam.sidik_padang.home.dataternak.detailternak.foto.FotoTernakFragment;
import com.zam.sidik_padang.home.dataternak.detailternak.riwayat.RiwayatFragment;
import com.zam.sidik_padang.home.dataternak.detailternak.silsilah.SilsilahFragment;

/**
 * Created by supriyadi on 9/10/17.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    private final String[] pageTitles = {
            "Detail", "Data Recording", "Foto", "Keterangan", "Riwayat", "Silsilah"
    };
    private String idTernak, eartag, from;
    private SparseArrayCompat<Fragment> sparseArray;

    public PagerAdapter(FragmentManager fm, String idTernak, String eartag, String from) {
        super(fm);
        this.idTernak = idTernak;
        this.eartag = eartag;
        this.from = from;
        sparseArray = new SparseArrayCompat<>();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f;
        if (position == 0) f = DetailTernakFragment.getInstance(idTernak, from);
        else if (position == 1) f = DataRecordingTernakFragment.getInstance(idTernak, from);
        else if (position == 2) f = FotoTernakFragment.newInstance(idTernak, from);
        else if (position == 3) f = DataSapihFragment.getInstance(idTernak, from);
        else if (position == 4) f = RiwayatFragment.newInstance(idTernak, from);
        else f = SilsilahFragment.newInstance(idTernak, eartag, from);
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

    public void setDataTernak(String id, String eartag) {
        this.idTernak = id;
        this.eartag = eartag;
        notifyDataSetChanged();
    }
}
