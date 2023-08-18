package com.zam.sidik_padang.home.newsinfo.gallery.bukaalbum;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import com.zam.sidik_padang.R;


public class BukaGambarDialog extends AppCompatDialogFragment {
    private static final String EXTRA_LINK = "extra_link";
    private String link;

    public BukaGambarDialog() {
    }

    public static BukaGambarDialog getInstance(String link) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_LINK, link);
        BukaGambarDialog bukaGambarDialog = new BukaGambarDialog();
        bukaGambarDialog.setArguments(bundle);
        return bukaGambarDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, getTheme());
        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_LINK)) {
            link = savedInstanceState.getString(EXTRA_LINK);
        } else {
            Bundle b = getArguments();
            if (b != null && b.containsKey(EXTRA_LINK))
                link = b.getString(EXTRA_LINK);
        }
        if (link == null)
            Toast.makeText(getActivity(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (link != null) outState.putString(EXTRA_LINK, link);
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_buka_gambar, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imageView = (ImageView) view.findViewById(R.id.photoView);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        Glide.with(getActivity()).load(link)
                .placeholder(R.drawable.ic_photo_library_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
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
                .into(imageView);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(params);
        }
    }
}
