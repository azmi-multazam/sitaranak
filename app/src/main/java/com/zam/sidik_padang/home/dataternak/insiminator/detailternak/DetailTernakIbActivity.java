package com.zam.sidik_padang.home.dataternak.insiminator.detailternak;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.dataternak.detailternak.DetailTernakFragment;
import com.zam.sidik_padang.home.dataternak.detailternak.PrintTernakIdCardActivity;
import com.zam.sidik_padang.home.dataternak.insiminator.TernakIb;


/**
 * Created by supriyadi on 4/17/18.
 */

public class DetailTernakIbActivity extends BaseLogedinActivity implements DetailTernakFragment.OnFotoTernakDiklikListener {

    public static final String ID_TERNAK = "id_ternak";
    private String idTernak = "";
    private DetailTernakIbPagerAdapter adapter;
    private ViewPager viewPager;
    private TernakIb ternakIb;


    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (!idTernak.isEmpty()) outState.putString(ID_TERNAK, idTernak);
        if (ternakIb != null) outState.putSerializable("ternak", ternakIb);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(ID_TERNAK)) {
                idTernak = savedInstanceState.getString(ID_TERNAK);
            }
            if (savedInstanceState.containsKey("ternak")) {
                ternakIb = (TernakIb) savedInstanceState.getSerializable("ternak");
            }
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
        if (it.hasExtra(ID_TERNAK))
            idTernak = it.getStringExtra(ID_TERNAK);
        else if (savedInstanceState != null && savedInstanceState.containsKey(ID_TERNAK))
            idTernak = savedInstanceState.getString(ID_TERNAK);

        if (it.hasExtra("ternak")) {
            ternakIb = (TernakIb) it.getSerializableExtra("ternak");
        } else if (savedInstanceState != null && savedInstanceState.containsKey("ternak"))
            ternakIb = (TernakIb) savedInstanceState.getSerializable("ternak");

        viewPager = (ViewPager) findViewById(R.id.activity_detail_ternak_ViewPager);
        adapter = new DetailTernakIbPagerAdapter(getSupportFragmentManager(), ternakIb);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.activity_detail_ternak_TabLayout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onFotoTernakDiklikListener() {
        viewPager.setCurrentItem(1, true);
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
