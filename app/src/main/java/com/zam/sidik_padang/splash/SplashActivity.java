package com.zam.sidik_padang.splash;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.zam.sidik_padang.BaseActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.login.LoginActivity;
import com.zam.sidik_padang.util.Util;
import io.paperdb.Paper;

/**
 * Created by supriyadi on 9/26/17.
 */

public class SplashActivity extends BaseActivity implements SplashContract.SplashView {

    private final String VOLLEY_TAG = getClass().getName();
    private ImageView imageViewLogo;

    private boolean resummed = false;

    private SplashContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imageViewLogo = findViewById(R.id.activity_splash_ImageView);
        imageViewLogo.setVisibility(View.INVISIBLE);

        presenter = new SplashPresenter(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        resummed = true;

        new Handler(Looper.getMainLooper()).postDelayed(this::jalankanAnimasi, 100);
        //retrieveLogo();
    }

    @Override
    protected void onPause() {
        resummed = false;
        super.onPause();
    }

    private void jalankanAnimasi() {
        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(imageViewLogo, View.ALPHA, 0, 1f);
        alphaAnim.setDuration(100);
        alphaAnim.setInterpolator(new LinearInterpolator());
        PropertyValuesHolder scaleValuesHolderX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0, 1f);
        PropertyValuesHolder scaleValuesHolderY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0, 1f);
        ObjectAnimator scaleAnim = ObjectAnimator.ofPropertyValuesHolder(imageViewLogo, scaleValuesHolderX, scaleValuesHolderY);
        scaleAnim.setDuration(1000);
        scaleAnim.setInterpolator(new OvershootInterpolator());
        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(alphaAnim, scaleAnim);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                imageViewLogo.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!resummed) return;
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    }
                }, 300);

            }

            @Override
            public void onAnimationStart(Animator animation) {
                imageViewLogo.setVisibility(View.VISIBLE);
            }
        });
        animSet.start();

    }

    private void retrieveLogo() {
        String lastUrlGambar = Paper.book().read("url_logo");
        //final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        //String lastUrlGambar = pref.getString("url_logo", "");
        if (lastUrlGambar != null && !lastUrlGambar.isEmpty()) {
            debug(getClass(), lastUrlGambar);
            Glide.with(SplashActivity.this).load(lastUrlGambar).crossFade(500).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageViewLogo);
        } else {
            if (!Util.isInternetAvailible(this)) return;
            presenter.requestData();
        }

        //debug(getClass(), "retrieveLogo");
		/*
		VolleyStringRequest request = new VolleyStringRequest(Config.URL_LOGO, new VolleyStringRequest.Callback() {
			@Override
			public void onResponse(JsonObject jsonObject) {
				debug(getClass(), "retrieveLogo OnResponse: " + jsonObject.toString());
				if (jsonObject.get("success").getAsBoolean()) {
					JsonElement je = jsonObject.get("depan");
					if (!je.isJsonNull()) {
						JsonArray ja = je.getAsJsonArray();
						if (ja.size() > 0) {
							JsonElement e = ja.get(0);
							String urlGambar = e.getAsJsonObject().get("gambar").getAsString();
							pref.edit().putString("url_logo", urlGambar).apply();
							if (resummed)
								Glide.with(SplashActivity.this).load(urlGambar).crossFade(1000).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageViewLogo);
						}
					}
				} else {

				}

			}
		});
		request.setTag(VOLLEY_TAG);
		VolleySingleton.getInstance(this).getRequestQueue().add(request);
		 */
    }

    @Override
    public void onRequestStart() {

    }

    @Override
    public void onRequestSuccess(SplashResponse response) {
        String urlGambar = response.getDepan().get(0).getGambar();
        //pref.edit().putString("url_logo", urlGambar).apply();
        Paper.book().write("url_logo", urlGambar);
        if (resummed)
            Glide.with(SplashActivity.this).load(urlGambar).crossFade(500).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageViewLogo);

    }

    @Override
    public void onRequestError(String message) {

    }
}
