package com.zam.sidik_padang.home.ppob.iklanmerchant.iklanpribadi;

import android.os.Bundle;

import com.zam.sidik_padang.home.ppob.iklanmerchant.iklanumum.IklanUmumListFragment;


/**
 * Created by supriyadi on 3/14/18.
 */

public class IklanPribadiListFragment extends IklanUmumListFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setIklanPribadi(true);
    }
}
