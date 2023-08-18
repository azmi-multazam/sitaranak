package com.zam.sidik_padang.home.newsinfo.gallery;

import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.newsinfo.BaseMediaActivity;
import com.zam.sidik_padang.home.newsinfo.MediaApiResponse;


public class GalleryActivity extends BaseMediaActivity {

    private List<Album> list;
    private AlbumListAdapter adapter;


    @Override
    protected void onSetContentView() {
        // TODO: Implement this method
        super.onSetContentView();
        setContentView(R.layout.activity_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        list = new ArrayList<>();
        adapter = new AlbumListAdapter(list);
        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
    }

    @Override
    protected void onUpdateView(MediaApiResponse response) {
        // TODO: Implement this method
        super.onUpdateView(response);
        debug(getClass(), "onUpdateView. gallery.size: " + response.album_galery.size());
        if (!response.success) {
            Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show();
            return;
        }
        list.clear();
        list.addAll(response.album_galery);
        adapter.notifyDataSetChanged();
        textViewNoData.setVisibility(list.size() > 0 ? View.INVISIBLE : View.VISIBLE);
    }


}

