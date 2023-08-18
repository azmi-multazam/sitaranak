package com.zam.sidik_padang.home.newsinfo.gallery.bukaalbum;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.newsinfo.gallery.Album;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.VolleyStringRequest;

public class BukaAlbumActivity extends BaseLogedinActivity
        implements GambarListAdapter.OnGambarClickListener {

    public static final String EXTRA_ALBUM = "extra_album";
    private static final String TAG = BukaAlbumActivity.class.getName();
    private static final String SAVED_RESPONSE = "album_";
    private Album album;
    private boolean isLoading = false;
    private List<Gambar> list;
    private GambarListAdapter adapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buka_album);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_ALBUM))
            album = (Album) savedInstanceState.getSerializable(EXTRA_ALBUM);
        else if (getIntent().hasExtra(EXTRA_ALBUM))
            album = (Album) getIntent().getSerializableExtra(EXTRA_ALBUM);

        if (album == null) {
            Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
            return;
        }

        toolbar.setSubtitle(album.jdl_album);
		/*
		TextView tv=(TextView)findViewById(R.id.textView);
		if (album.keterangan.contains("<")) {
			Spanned keterangan = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? Html.fromHtml(album.keterangan, 0) : Html.fromHtml(album.keterangan);
			tv.setText(keterangan);
		}else {
			tv.setText(album.keterangan);
		}
		*/
        list = new ArrayList<>();
        adapter = new GambarListAdapter(list, this);
        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerView);
        //rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

		/*
		findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				BukaGambarDialog.getInstance(album.gbr_album).show(getSupportFragmentManager(), "buka_gambar");
			}
		});

		final View progressBar=findViewById(R.id.progressBar);
		Glide.with(this).load(album.gbr_album)
				.diskCacheStrategy(DiskCacheStrategy.SOURCE)
				.placeholder(R.drawable.hydrangeas)
			.listener(new RequestListener<String, GlideDrawable>() {
					@Override
					public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
							progressBar.setVisibility(View.INVISIBLE);
						return false;
					}

					@Override
				public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
						progressBar.setVisibility(View.INVISIBLE);

						return false;
				}
				})
				.into(((ImageView)findViewById(R.id.imageView)));
				*/
        String saved = sharedPreferences.getString(SAVED_RESPONSE + album.id_album, "");
        if (saved.startsWith("{")) {
            Response response = new Gson().fromJson(saved, Response.class);
            if (response != null) updateView(response);
        }
    }

    @Override
    protected void onStart() {
        // TODO: Implement this method
        super.onStart();
        if (!isLoading) loadFromServer();
    }


    private void loadFromServer() {
        if (!Util.isInternetAvailible(this)) {

            Toast.makeText(this, "Tidak ada internet", Toast.LENGTH_SHORT).show();
            return;
        }

        isLoading = true;
        String link = Config.URL_GALERY;
        link += "?aksi=1&userid=" + user.userid;
        link += "&id_album=" + album.id_album;
        debug(getClass(), "Load album. link:" + link);

        VolleyStringRequest request = new VolleyStringRequest(link, new VolleyStringRequest.Callback() {

            @Override
            public void onResponse(JsonObject jsonObject) {
                debug(getClass(), "Load album response: " + jsonObject);
                isLoading = false;
                Response response = new Gson().fromJson(jsonObject, Response.class);
                if (response.success) {
                    sharedPreferences.edit().putString(SAVED_RESPONSE + album.id_album, jsonObject.toString()).apply();
                    updateView(response);
                } else {
                    Toast.makeText(BukaAlbumActivity.this, response.message, Toast.LENGTH_SHORT).show();
                }

            }
        });

        request.setTag(TAG);

        VolleySingleton.getInstance(this).getRequestQueue().add(request);
    }

    private void updateView(Response apiResponse) {
        debug(getClass(), "update view. List size:" + apiResponse.gallery.size());
        list.clear();
        list.addAll(apiResponse.gallery);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (album != null) outState.putSerializable(EXTRA_ALBUM, album);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_ALBUM))
            album = (Album) savedInstanceState.getSerializable(EXTRA_ALBUM);

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onGambarClick(Gambar gambar) {
        BukaGambarDialog.getInstance(gambar.gambar).show(getSupportFragmentManager(), "buka_gambar");
    }


}
