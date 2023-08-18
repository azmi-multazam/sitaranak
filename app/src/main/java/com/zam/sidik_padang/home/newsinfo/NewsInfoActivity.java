package com.zam.sidik_padang.home.newsinfo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.memberhome.mvp.Berita;
import com.zam.sidik_padang.home.memberhome.mvp.BeritaResponse;
import com.zam.sidik_padang.home.memberhome.mvp.Headline;
import com.zam.sidik_padang.home.newsinfo.gallery.GalleryActivity;
import com.zam.sidik_padang.home.newsinfo.mvp.NewsContract;
import com.zam.sidik_padang.home.newsinfo.mvp.NewsPresenter;
import com.zam.sidik_padang.home.newsinfo.mvp.navmenu.KategoriBerita;
import com.zam.sidik_padang.home.newsinfo.mvp.navmenu.NavMenuResponse;
import com.zam.sidik_padang.home.newsinfo.video.VideoActivity;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.VolleySingleton;
import io.paperdb.Paper;
import pl.pzienowicz.autoscrollviewpager.AutoScrollViewPager;

/**
 * Created by supriyadi on 5/7/17.
 */

public class NewsInfoActivity extends BaseLogedinActivity implements NewsContract.NewsView, NestedScrollView.OnScrollChangeListener {

    public static final String EXTRA_BERITA = "berita";
    private static final String VOLLEY_TAG = NewsInfoActivity.class.getName();
    public static final String PREF_RESPONSE_BERITA = "response_berita";
    private static final String PREF_RESPONSE_BERITA_NAVIGATION_MENU = "response_berita_navigation_menu";

    private int pagerHeaderHeight = 0;

    private static final int PAGE_START = 1;
    private static int PAGES_LIMIT = 20;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int lastAdapterSize = 0;
    private RelativeLayout layoutLoadMore;
    private ProgressBar progressLoadMore;

    private List<Berita> listBerita;
    private List<Berita> tempListBerita;
    private BeritaListAdapter listAdapter;
    //private List<Berita> listBeritaHeader;
    private List<Headline> listBeritaHeader;
    private FragmentPagerAdapter pagerAdapter;
    private List<NavigationMenuItem> listNavigationMenu;
    private NavigationMenuAdapter navigationMenuAdapter;
    private DrawerLayout drawer;
    private Gson gson;
    private View progressbar, progressbarNavigation;
    private AutoScrollViewPager pager;
    private NestedScrollView nestedScrollView;

    private NewsContract.Presenter presenter;
    private BeritaResponse response;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_info);
        presenter = new NewsPresenter(this);
        pagerHeaderHeight = (int) (getResources().getDisplayMetrics().density * 200f);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        drawer = (DrawerLayout) findViewById(R.id.activity_news_info_DrawerLayout);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawer();
            }
        });

        nestedScrollView = findViewById(R.id.activity_news_info_ScrollView);
        progressbar = findViewById(R.id.activity_news_info_Progressbar);
        layoutLoadMore = findViewById(R.id.layoutLoadMore);
        progressLoadMore = findViewById(R.id.progressLoadMore);
        progressbarNavigation = findViewById(R.id.activity_news_info_ProgressbarNavigationView);
        RecyclerView rv = findViewById(R.id.activity_news_info_RecyclerView);
        rv.setNestedScrollingEnabled(false);
        rv.setLayoutManager(new LinearLayoutManager(this));
        listBerita = new ArrayList<>();
        listAdapter = new BeritaListAdapter(listBerita);
        rv.setAdapter(listAdapter);

        pager = findViewById(R.id.activity_news_info_ViewPager);
        listBeritaHeader = new ArrayList<>();
        pagerAdapter = new HeaderPagerAdapter(getSupportFragmentManager(), listBeritaHeader);
        pager.setAdapter(pagerAdapter);
//		PageIndicator indicator = (PageIndicator) findViewById(R.id.activity_news_info_PageIndicator);
//		indicator.setViewPager(pager);

		/*
		gson = new Gson();
		String response = sharedPreferences.getString(PREF_RESPONSE_BERITA, "");
		new Handler(Looper.getMainLooper()).postDelayed(() -> {
			if (!response.isEmpty() && response.startsWith("{")) {
				JsonElement resPonseElement = gson.fromJson(response, JsonElement.class);
				if (resPonseElement != null) bindView(resPonseElement.getAsJsonObject());
			}
			loadFromServer(null);
		}, 100);
		 */
        response = Paper.book().read(PREF_RESPONSE_BERITA);
        tempListBerita = response.getBerita();
        if (response != null) bindView(response);
        nestedScrollView.setOnScrollChangeListener(this);

        listNavigationMenu = new ArrayList<>();
        listNavigationMenu.add(new NavigationMenuItem(null, "Umum", true));
        navigationMenuAdapter = new NavigationMenuAdapter(listNavigationMenu, item -> {
            drawer.closeDrawer(GravityCompat.START);
            getSupportActionBar().setTitle(item.nama);
            loadFromServer(item.id);
        });

        RecyclerView rvNavigation = findViewById(R.id.activity_news_info_RecyclerViewNavigationView);
        rvNavigation.setLayoutManager(new LinearLayoutManager(this));
        rvNavigation.setAdapter(navigationMenuAdapter);
        //rvNavigation.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
		/*
		String nvstring = sharedPreferences.getString(PREF_RESPONSE_BERITA_NAVIGATION_MENU, "");
		if (!nvstring.isEmpty() && nvstring.startsWith("{")) {
			bindNavigationMenu(gson.fromJson(nvstring, JsonElement.class).getAsJsonObject());
		}

		rvNavigation.postDelayed(this::loadNavigationMenuFromServer, 500);
		 */
        NavMenuResponse navMenu = Paper.book().read(PREF_RESPONSE_BERITA_NAVIGATION_MENU);
        if (navMenu != null) {
            bindNavigationMenu(navMenu);
        }

        Map<String, String> params = new HashMap<>();
        params.put("aksi", "3");
        params.put("userid", user.userid);
        presenter.requestNavigation(params);
        //presenter.requestNavigation(Config.URL_BERITA + "?aksi=3&userid=" + user.userid);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.design_bottom_sheet);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem p1) {
                int id = p1.getItemId();
                if (id == R.id.menu_back) {
                    finish();
                } else if (id == R.id.menu_video) {
                    startActivity(new Intent(NewsInfoActivity.this, VideoActivity.class));
                } else if (id == R.id.menu_gallery) {
                    startActivity(new Intent(NewsInfoActivity.this, GalleryActivity.class));
                }
                return false;
            }
        });

        loadFromServer(null);
    }

    private void loadFromServer(String idKat) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            //String url = Config.URL_BERITA + "?aksi=1&userid=" + user.userid;
            //if (idKat != null) url += "&kategori=" + idKat;

            Map<String, String> params = new HashMap<>();
            params.put("aksi", "1");
            params.put("userid", user.userid);
            if (idKat != null) params.put("kategori", idKat);
            presenter.requestBerita(params);
        }, 300);
    }

    private void toggleDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else drawer.openDrawer(GravityCompat.START);
    }

    private void bindNavigationMenu(NavMenuResponse response) {
        listNavigationMenu.clear();
        listNavigationMenu.add(new NavigationMenuItem(null, "Umum", false));
        for (KategoriBerita kategoriBerita : response.getKategoriBerita())
            listNavigationMenu.add(new NavigationMenuItem(kategoriBerita.getId(), kategoriBerita.getNama(), false));

        int s = 0;
        for (int i = 0; i < listNavigationMenu.size(); i++) {
            if (listNavigationMenu.get(i).selected) s = i;
            break;
        }
        listNavigationMenu.get(s).selected = true;
        navigationMenuAdapter.notifyDataSetChanged();
        getSupportActionBar().setTitle(listNavigationMenu.get(s).nama);
    }

    private void bindView(BeritaResponse response) {
        listBeritaHeader.clear();
        listBeritaHeader.addAll(response.getHeadline());
        ViewGroup.LayoutParams params = pager.getLayoutParams();
        params.height = listBeritaHeader.size() > 0 ? pagerHeaderHeight : 0;
        pager.setLayoutParams(params);
        pagerAdapter.notifyDataSetChanged();
        debug(getClass(), "pagerSize=" + pagerAdapter.getCount());
        //pager.postDelayed(runnableSlide, 5000);
        pager.setInterval(5000);
        pager.setScrollDurationFactor(6);
        pager.startAutoScroll(8000);
        nestedScrollView.fullScroll(View.FOCUS_UP);

        currentPage = PAGE_START;
        isLastPage = false;
        if (!listBerita.isEmpty()) {
            listBerita.clear();
            listAdapter.notifyDataSetChanged();
        }
        addList();
    }

    private void addList() {
        int page = (PAGES_LIMIT * (currentPage - 1));
        int size = tempListBerita.size();
        int limit = PAGES_LIMIT + page;
        if (limit > size) {
            limit = size;
        }

        isLastPage = limit == size;
        if (!isLastPage) {
            for (int i = page; i < limit; i++) {
                listBerita.add(response.getBerita().get(i));
            }
        }
        listAdapter.notifyDataSetChanged();
        layoutLoadMore.setVisibility(isLastPage ? View.GONE : View.VISIBLE);
    }

	/*
	private void loadNavigationMenuFromServer() {
		if (!Util.isInternetAvailible(this)) return;
		String url = Config.URL_BERITA + "?aksi=3&userid=" + user.userid;
		debug(getClass(), "Load NavigationMenu url=" + url);
		progressbarNavigation.setVisibility(View.VISIBLE);
		VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
			@Override
			public void onResponse(JsonObject jsonObject) {
				debug(getClass(), "Load navigation menu response =" + jsonObject);
				progressbarNavigation.setVisibility(View.INVISIBLE);
				if (jsonObject.get("success").getAsBoolean()) {
					sharedPreferences.edit().putString(PREF_RESPONSE_BERITA_NAVIGATION_MENU, jsonObject.toString()).apply();
					bindNavigationMenu(jsonObject);
				}
			}
		});
		request.setTag(VOLLEY_TAG);
		VolleySingleton.getInstance(this).getRequestQueue().add(request);
	}

	private void bindNavigationMenu(JsonObject jsonObject) {
		JsonElement arrayelement = jsonObject.get("kategori_berita");
		if (!arrayelement.isJsonArray()) return;
		int s = 0;
		for (int i = 0; i < listNavigationMenu.size(); i++) {
			if (listNavigationMenu.get(i).selected) s = i;
			break;
		}
		listNavigationMenu.clear();
		listNavigationMenu.add(new NavigationMenuItem(null, "Umum", false));
		for (JsonElement e : arrayelement.getAsJsonArray())
			listNavigationMenu.add(gson.fromJson(e, NavigationMenuItem.class));
		listNavigationMenu.get(s).selected = true;
		navigationMenuAdapter.notifyDataSetChanged();
		getSupportActionBar().setTitle(listNavigationMenu.get(s).nama);
	}
	 */

	/*
	private void loadFromServer(String kategoriId) {
		if (!Util.isInternetAvailible(this)) {
			Toast.makeText(this, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
			return;
		}
		String url = Config.URL_BERITA + "?aksi=1&userid=" + user.userid;
		debug(getClass(), "Load berita url=" + url);
		if (kategoriId != null) url += "&kategori=" + kategoriId;

		progressbar.setVisibility(View.VISIBLE);
		VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
			@Override
			public void onResponse(JsonObject jsonObject) {
				debug(getClass(), "Load berita response=" + jsonObject);
				progressbar.setVisibility(View.INVISIBLE);
				if (jsonObject.get("success").getAsBoolean()) {
					sharedPreferences.edit().putString(PREF_RESPONSE_BERITA, jsonObject.toString()).apply();
					bindView(jsonObject);
				} else
					Toast.makeText(NewsInfoActivity.this, jsonObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
			}
		});
		request.setTag(VOLLEY_TAG);
		VolleySingleton.getInstance(this).getRequestQueue().add(request);
	}

	private void bindView(JsonObject jsonResponse) {
		JsonElement headerElement = jsonResponse.get("headline");
		if (headerElement.isJsonArray()) {
			JsonArray headerArray = headerElement.getAsJsonArray();
			listBeritaHeader.clear();
			for (JsonElement e : headerArray) listBeritaHeader.add(gson.fromJson(e, Berita.class));
			ViewGroup.LayoutParams params = pager.getLayoutParams();
			params.height = listBeritaHeader.size() > 0 ? pagerHeaderHeight : 0;
			pager.setLayoutParams(params);
			pagerAdapter.notifyDataSetChanged();
			debug(getClass(), "pagerSize=" + pagerAdapter.getCount());
			//pager.postDelayed(runnableSlide, 5000);
			pager.setInterval(5000);
			pager.setScrollDurationFactor(6);
			pager.startAutoScroll(8000);
			((NestedScrollView) findViewById(R.id.activity_news_info_ScrollView)).fullScroll(View.FOCUS_UP);
		}

		JsonElement listElement = jsonResponse.get("berita");
		if (listElement.isJsonArray()) {
			JsonArray listArray = listElement.getAsJsonArray();
			listBerita.clear();
			for (JsonElement e : listArray) listBerita.add(gson.fromJson(e, Berita.class));
			listAdapter.notifyDataSetChanged();
		}

	}
	 */

    @Override
    protected void onDestroy() {
        VolleySingleton.getInstance(this).getRequestQueue().cancelAll(VOLLEY_TAG);
        super.onDestroy();
    }

    @Override
    public void onRequestNavigationMenuStart() {
        progressbarNavigation.setVisibility(View.VISIBLE);
        debug(getClass(), "onRequestNavigationMenuStart");
    }

    @Override
    public void onRequestNavigationSuccess(NavMenuResponse response) {
        progressbarNavigation.setVisibility(View.GONE);
        Paper.book().write(PREF_RESPONSE_BERITA_NAVIGATION_MENU, response);
        bindNavigationMenu(response);
    }

    @Override
    public void onRequestNavigationError(String message) {

    }

    @Override
    public void onRequestBeritaStart() {
        progressbar.setVisibility(View.VISIBLE);
        debug(getClass(), "onRequestBeritaStart");
    }

    @Override
    public void onRequestBeritaSuccess(BeritaResponse response) {
        debug(getClass(), "onRequestBeritaSuccess");
        progressbar.setVisibility(View.GONE);
        Paper.book().write(PREF_RESPONSE_BERITA, response);

        this.response = response;
        tempListBerita = response.getBerita();
        bindView(response);
    }

    @Override
    public void onRequestBeritaError(String message) {

    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (tempListBerita.isEmpty()) return;
        if (isLastPage) return;
        if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
            Log.d("scroll", "BOTTOM SCROLL");
            //int page = currentPage += 1;
            //presenter.loadMore(page, PAGES_LIMIT);
            currentPage += 1;
            addList();
        }
    }
}
