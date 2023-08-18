package com.zam.sidik_padang.home.newsinfo.video;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.newsinfo.BaseMediaActivity;
import com.zam.sidik_padang.home.newsinfo.MediaApiResponse;


public class VideoActivity extends BaseMediaActivity
        implements VideoListAdapter.OnVideoClickListener, VideoPlayerFragment.OnInitializationSuccessListener {
    private static final String EXTRA_VIDEO = "extra_video";
    private List<Video> videoList, allVideoList;
    private VideoListAdapter videoListAdapter;
    private Video aktifVideo;
    //private ScrollView scrollView;
    private TextView textViewJudulVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_VIDEO))
            aktifVideo = (Video) savedInstanceState.getSerializable(EXTRA_VIDEO);
    }


    @Override
    protected void onSetContentView() {
        setContentView(R.layout.activity_video);
		/*
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view)
				{
					finish();
				}
			});
			*/
        videoList = new ArrayList<>();
        allVideoList = new ArrayList<>();
        videoListAdapter = new VideoListAdapter(videoList, this);
        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(videoListAdapter);
        //scrollView=(ScrollView)findViewById(R.id.scrollView);
        textViewJudulVideo = (TextView) findViewById(R.id.textViewJudul);
    }

    @Override
    public void onVideoClick(Video video, int position) {
        videoList.clear();
        for (Video v : allVideoList) {
            if (v.youtube.equals(video.youtube)) {
                aktifVideo = v;
                openVideo();
            } else {
                videoList.add(v);
            }
        }

        videoListAdapter.notifyDataSetChanged();


    }

    @Override
    protected void onUpdateView(MediaApiResponse response) {
        super.onUpdateView(response);

        allVideoList.clear();
        allVideoList.addAll(response.video);
        videoList.clear();

        for (Video v : allVideoList) {
            if (aktifVideo == null) {
                aktifVideo = v;
                openVideo();
            } else if (aktifVideo.youtube.equals(v.youtube)) {
                continue;
            } else {
                videoList.add(v);
            }
        }
        videoListAdapter.notifyDataSetChanged();
        textViewNoData.setVisibility(videoList.size() > 0 ? View.INVISIBLE : View.VISIBLE);

    }

    private void openVideo() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutVideo, VideoPlayerFragment.getInstance(aktifVideo.getVideoId())).commitAllowingStateLoss();
        //scrollView.fullScroll(ScrollView.FOCUS_UP);
        textViewJudulVideo.setText(aktifVideo.judul_video);
    }

    @Override
    public void onInitializationSuccess() {
        //scrollView.fullScroll(View.FOCUS_UP);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (aktifVideo != null) outState.putSerializable(EXTRA_VIDEO, aktifVideo);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_VIDEO))
            aktifVideo = (Video) savedInstanceState.getSerializable(EXTRA_VIDEO);
        super.onRestoreInstanceState(savedInstanceState);
    }


}
	
