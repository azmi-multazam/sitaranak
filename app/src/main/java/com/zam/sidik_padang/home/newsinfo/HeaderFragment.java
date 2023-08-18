package com.zam.sidik_padang.home.newsinfo;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.memberhome.mvp.BeritaResponse;
import com.zam.sidik_padang.home.memberhome.mvp.Headline;
import com.zam.sidik_padang.home.newsinfo.bukaberita.BukaBeritaActivity;
import io.paperdb.Paper;

import static com.zam.sidik_padang.home.newsinfo.NewsInfoActivity.PREF_RESPONSE_BERITA;

/**
 * Created by supriyadi on 10/11/17.
 */

public class HeaderFragment extends Fragment {

    private Headline headline;
    private String idBerita;

    public static HeaderFragment getInstance(Headline headline) {
        HeaderFragment fragment = new HeaderFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(NewsInfoActivity.EXTRA_BERITA, headline.getId());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
		/*
		if (b != null && b.containsKey(NewsInfoActivity.EXTRA_BERITA))
			berita = (Headline) b.getSerializable(NewsInfoActivity.EXTRA_BERITA);

		 */
        if (b != null && b.containsKey(NewsInfoActivity.EXTRA_BERITA))
            idBerita = b.getString(NewsInfoActivity.EXTRA_BERITA);

        BeritaResponse beritaResponse = Paper.book().read(PREF_RESPONSE_BERITA);
        headline = getBeritaById(beritaResponse.getHeadline(), idBerita);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (idBerita != null) outState.putString(NewsInfoActivity.EXTRA_BERITA, idBerita);
        //if (berita != null) outState.putSerializable(NewsInfoActivity.EXTRA_BERITA, berita);
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_header_berita, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imageView = view.findViewById(R.id.item_header_berita_ImageView);
        if (headline.getGambar() != null && !headline.getGambar().isEmpty()) {
            final View progressbar = view.findViewById(R.id.item_header_berita_Progreebar);
            progressbar.setVisibility(View.VISIBLE);
            Glide.with(getActivity()).load(headline.getGambar()).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    progressbar.setVisibility(View.INVISIBLE);
                    return false;
                }
            }).into(imageView);
        }
        ((TextView) view.findViewById(R.id.item_header_berita_TextViewJudul)).setText(headline.getJudul());
        ((TextView) view.findViewById(R.id.item_header_berita_TextViewTanggal)).setText(headline.getTanggal());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BukaBeritaActivity.class);
                intent.putExtra(NewsInfoActivity.EXTRA_BERITA, headline.getId());
                v.getContext().startActivity(intent);
            }
        });
    }

    private Headline getBeritaById(List<Headline> list, String idBerita) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(idBerita)) {
                return list.get(i);
            }
        }
        return null;
    }

}
