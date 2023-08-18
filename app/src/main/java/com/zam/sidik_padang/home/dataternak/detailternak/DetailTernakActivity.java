package com.zam.sidik_padang.home.dataternak.detailternak;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.dataternak.vm.TernakViewModel;
import com.zam.sidik_padang.util.Config;

/**
 * Created by supriyadi on 9/10/17.
 */

public class DetailTernakActivity extends BaseLogedinActivity implements DetailTernakFragment.OnFotoTernakDiklikListener {

    public static final String ID_TERNAK = "id_ternak";
    private String idTernak = "", eartag = "", from = "home";
    private PagerAdapter adapter;
    private ViewPager viewPager;
    private TernakViewModel viewModel;
    private boolean isSklb;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (!idTernak.isEmpty()) {
            outState.putString(ID_TERNAK, idTernak);
            outState.putString("eartag", eartag);
            outState.putString("from", from);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(ID_TERNAK)) {
            idTernak = savedInstanceState.getString(ID_TERNAK);
            eartag = savedInstanceState.getString("eartag");
            from = savedInstanceState.getString("from");
        }
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ternak);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent it = getIntent();
        if (it.hasExtra("from")) {
            from = it.getStringExtra("from");
        } else if (savedInstanceState != null && savedInstanceState.containsKey("from")) {
            from = savedInstanceState.getString("from");
        }

        if (it.hasExtra(ID_TERNAK)) {
            idTernak = it.getStringExtra(ID_TERNAK);
            eartag = it.getStringExtra("eartag");
        } else if (savedInstanceState != null && savedInstanceState.containsKey(ID_TERNAK)) {
            idTernak = savedInstanceState.getString(ID_TERNAK);
            eartag = savedInstanceState.getString("eartag");
        }
        viewPager = (ViewPager) findViewById(R.id.activity_detail_ternak_ViewPager);
        adapter = new PagerAdapter(getSupportFragmentManager(), idTernak, eartag, from);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.activity_detail_ternak_TabLayout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);

        viewModel = new ViewModelProvider(this).get(TernakViewModel.class);

        isSklb = from.equals("sklb");
        String base;
        if (isSklb) {
            base = Config.URL_LIHAT_DATA_TERNAK_SKLB;
        } else {
            base = Config.URL_LIHAT_DATA_TERNAK;
        }
        String url = base + "?aksi=5&userid=" + user.userid + "&id_ternak=" + idTernak;
        debug(getClass(), "Load detail ternak url=" + url);
        viewModel.getDetailTernak(url);

        String urlFoto = Config.URL_GAMBAR_SAPI + "?aksi=1&userid=" + user.userid;
        url += "&id_ternak=" + idTernak;
        debug(getClass(), "Load foto ternak url=" + url);
        viewModel.getFotoTernak(urlFoto);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        debug(getClass(), "onActivity result req Code=" + requestCode);
        if (requestCode == 11 && resultCode == RESULT_OK) {
            Fragment f = adapter.getFragment(2);
            if (f != null)
                f.onActivityResult(requestCode, resultCode, data);
            debug(getClass(), "Fragent = " + f);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onFotoTernakDiklikListener() {
        viewPager.setCurrentItem(2, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_detail_ternak, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_print) {
            Intent it = new Intent(this, PrintTernakIdCardActivity.class);
            it.putExtra("idTernak", idTernak);
            startActivity(it);
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }
}
