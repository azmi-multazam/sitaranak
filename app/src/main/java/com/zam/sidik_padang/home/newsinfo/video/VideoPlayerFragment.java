package com.zam.sidik_padang.home.newsinfo.video;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import com.zam.sidik_padang.R;


public class VideoPlayerFragment extends YouTubePlayerSupportFragment
        implements YouTubePlayer.OnInitializedListener {

    private static final String EXTRA_VIDEO_ID = "video_id";
    private YouTubePlayer mPlayer;
    private String videoId;
    private OnInitializationSuccessListener onPlayerResumeListener;

    public VideoPlayerFragment() {
        super();
    }

    public static VideoPlayerFragment getInstance(String videoId) {
        Bundle b = new Bundle();
        b.putString(EXTRA_VIDEO_ID, videoId);
        VideoPlayerFragment player = new VideoPlayerFragment();
        player.setArguments(b);
        return player;
    }

    @Override
    public void onSaveInstanceState(Bundle b) {
        if (videoId != null) {
            b.putString(EXTRA_VIDEO_ID, videoId);
        }
        super.onSaveInstanceState(b);
    }

    @Override
    public void onAttach(Context context) {
        // TODO: Implement this method
        super.onAttach(context);
        if (context instanceof OnInitializationSuccessListener)
            onPlayerResumeListener = (OnInitializationSuccessListener) context;
    }

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        if (b != null && b.containsKey(EXTRA_VIDEO_ID)) {
            videoId = b.getString(EXTRA_VIDEO_ID);
        } else {
            Bundle bd = getArguments();
            if (bd.containsKey(EXTRA_VIDEO_ID)) videoId = bd.getString(EXTRA_VIDEO_ID);
        }


    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_VIDEO_ID))
            videoId = savedInstanceState.getString(EXTRA_VIDEO_ID);
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        view.setFocusable(true);
        initialize(getString(R.string.youtube_api_key), this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider p1, YouTubePlayer player, boolean wasRestored) {
        mPlayer = player;
        //	mPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
        if (!wasRestored) {
            mPlayer.cueVideo(videoId);
            //getView().requestFocus();
            if (onPlayerResumeListener != null) onPlayerResumeListener.onInitializationSuccess();
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider p1, YouTubeInitializationResult p2) {
        Toast.makeText(getActivity(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        if (mPlayer != null && mPlayer.isPlaying()) mPlayer.pause();
        super.onPause();
    }


    @Override
    public void onDestroy() {
        if (mPlayer != null) mPlayer.release();
        super.onDestroy();
    }


    public interface OnInitializationSuccessListener {
        void onInitializationSuccess();
    }

}
