package com.zam.sidik_padang.home.newsinfo.video;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import com.zam.sidik_padang.R;


public class VideoListAdapter extends RecyclerView.Adapter<VideoViewHolder> {
    private List<Video> list;
    private OnVideoClickListener listener;
    //private final String key;

    public VideoListAdapter(List<Video> list, OnVideoClickListener listener) {
        this.list = list;
        this.listener = listener;
        //key=context.getString(R.string.youtube_api_key);
    }


    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup p1, int p2) {
        // TODO: Implement this method
        return new VideoViewHolder(LayoutInflater.from(p1.getContext()).inflate(R.layout.item_video, p1, false));
    }

    @Override
    public void onBindViewHolder(final VideoViewHolder p1, int p2) {
        final Video video = list.get(p2);
        final int position = p2;
        p1.textViewJudul.setText(video.judul_video);
        p1.textViewTanggal.setText(video.tanggal);// +" id:"+video.getVideoId()+" "+ video.youtube);

        ((View) p1.textViewJudul.getParent()).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View p1) {
                listener.onVideoClick(video, position);
            }
        });
        //Glide.with(p1.thumbnailView.getContext()).load("https://img.youtube.com/vi/"+"hkzpLJjZQbA"+"/0.jpg").into(p1.thumbnailView);
        p1.progressBar.setVisibility(View.VISIBLE);
        Glide.with(p1.thumbnailView.getContext()).load("https://img.youtube.com/vi/" + video.getVideoId() + "/0.jpg").placeholder(R.drawable.ic_video_black_24dp)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        p1.progressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        p1.progressBar.setVisibility(View.INVISIBLE);

                        return false;
                    }
                })
                .into(p1.thumbnailView);

    }

    @Override
    public int getItemCount() {
        // TODO: Implement this method
        return list.size();
    }

    interface OnVideoClickListener {
        void onVideoClick(Video video, int position);
    }
}
