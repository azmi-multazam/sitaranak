package com.zam.sidik_padang.home.dataternak;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.nukc.LoadMoreWrapper.LoadMoreAdapter;
import com.github.nukc.LoadMoreWrapper.LoadMoreWrapper;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.api.BaseResponse;
import com.zam.sidik_padang.api.State;
import com.zam.sidik_padang.databinding.ActivityDataTernakBinding;
import com.zam.sidik_padang.home.dataternak.detailternak.DetailTernakActivity;
import com.zam.sidik_padang.home.dataternak.dibawahnya.TambahDataTernakActivity;
import com.zam.sidik_padang.home.dataternak.vm.DataTernakRepo;
import com.zam.sidik_padang.home.dataternak.vm.TernakViewModel;
import com.zam.sidik_padang.home.dataternak.vm.list.DataTernak;
import com.zam.sidik_padang.home.dataternak.vm.list.DataTernakResponse;
import com.zam.sidik_padang.home.dataternak.vm.list.HitungTernak;
import com.zam.sidik_padang.home.dataternak.vm.list.JumlahKambing;
import com.zam.sidik_padang.home.dataternak.vm.list.JumlahSapi;
import com.zam.sidik_padang.home.dataternak.rangking.RangkingActivity;
import com.zam.sidik_padang.home.dataternak.scanner.ScanDialogFragment;
import com.zam.sidik_padang.home.selectregion.Region;
import com.zam.sidik_padang.sapiku.SapikuFragment;
import com.zam.sidik_padang.util.Util;
import io.paperdb.Paper;

/**
 * Created by supriyadi on 5/7/17.
 */

public class DataTernakActivity extends BaseDataTernakActivity
        implements OnTernakItemClickListener, ScanDialogFragment.ScanListener {

    public static final String EXTRA_SEARCH_URL = "extra_search_url";
    private final int SELECT_REGION_REQUEST_CODE = 13;

    public static final int MODE_LINEARLAYOUT = 1;
    public static final int MODE_GRIDLAYOUT = 2;
    public static final int MODE_STAGGEREDGRIDLAYOUT = 3;

    private static final int PAGE_START = 1;
    private int currentPage = PAGE_START;

    private DrawerLayout drawerLayout;
    private TextView textLoadingJumlahSapi, textloadingJumlahKambing, tvTotal;
    private View progressBarButtonSearch, buttonSearch;
    private TextView textViewNoData;
    private EditText editTextUserId;

    private ActivityDataTernakBinding binding;
    private TernakViewModel viewModel;

    private int listSize = 0, listPos;
    private DataTernakAdapter adapter;
    private Dialog dialog;
    private boolean hasInten, fromDrawer;

    private Map<String, String> searchUrl;
    private boolean layoutBottomVisible = true;
    private RecyclerView rv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDataTernakBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        String tt = getIntent().hasExtra(SapikuFragment.EXTRA_JENIS_TERNAK) ?
                getIntent().getStringExtra(SapikuFragment.EXTRA_JENIS_TERNAK) : "Data Ternak";
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(tt);
        getSupportActionBar().setSubtitle(user.nama);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(view -> finish());

        viewModel = new ViewModelProvider(this).get(TernakViewModel.class);

        rv = binding.activityBaseSearchDataRecyclerView;
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DataTernakAdapter(this);
        rv.setAdapter(adapter);
        LoadMoreWrapper.with(adapter)
                .setFooterView(-1)
                .setShowNoMoreEnabled(false)
                .setLoadFailedView(R.layout.item_footer_error)
                .setListener(new LoadMoreAdapter.OnLoadMoreListener() {
                    @Override
                    public void onLoadMore(final LoadMoreAdapter.Enabled enabled) {
                        int itemCount = adapter.getItemCount();
                        Log.d("scroll", "list:"+listSize+", adapter:"+itemCount);
                        if (listSize > 0) {
                            currentPage +=1;
                            rv.postDelayed(() -> viewModel.getListPage(currentPage), 500);
                            if (itemCount >= listSize) {
                                enabled.setLoadMoreEnabled(false);
                            }
                        }
                    }

                    @Override
                    public void onScrolled(int dx, int dy) {
                        if (dy > 0 && layoutBottomVisible) {
                            hideTotal();
                        } else if (dy < 0 && !layoutBottomVisible) {
                            showTotal();
                        }
                    }
                })
                .into(rv);

        tvTotal = binding.activityBaseSearchDataTextViewTotal;

        editTextUserId = binding.activityBaseSearchDataEditTextUserId;
        buttonSearch = binding.activityBaseSearchDataButtonSearch;
        buttonSearch.setOnClickListener(v -> {
            String uid = editTextUserId.getText().toString().trim();
            if (!uid.isEmpty()) {
                onUserIdEntered(uid, false);
                progressBarButtonSearch.setVisibility(View.VISIBLE);
                buttonSearch.setEnabled(false);
            } else {
                Toast.makeText(getApplicationContext(), R.string.enter_user_id_or_select_region, Toast.LENGTH_SHORT).show();
            }
        });
        binding.activityBaseSearchDataButtonScan.setOnClickListener(v ->
                ScanDialogFragment.newInstance(this).show(getSupportFragmentManager(), "dialog"));
        binding.activityBaseSearchDataFAB.setOnClickListener(v ->
                startActivityForResult(new Intent(this, TambahDataTernakActivity.class), 3));

        textViewNoData = binding.textViewNoData;
        progressBarButtonSearch = binding.activityBaseSearchDataProgressBarButtonSearch;

        editTextUserId.setHint("ID Ternak");
        drawerLayout = binding.drawerLayout;
        textLoadingJumlahSapi = binding.TextViewLoadingJumlahSapi;
        textloadingJumlahKambing = binding.TextViewLoadingJumlahKambing;

        Drawable d = AppCompatResources.getDrawable(this, R.drawable.ic_view_list);
        int b = (int) (getResources().getDisplayMetrics().density * 24f);
        if (d != null) {
            d.setBounds(0, 0, b, b);
            toolbar.setNavigationIcon(d);
            toolbar.setNavigationOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));
        }
        observe();
        bindJumlahTernak();
        Intent intent = getIntent();
        searchUrl = new HashMap<>();
        if (intent.hasExtra(EXTRA_SEARCH_URL)) {
            searchUrl = getQueryString(intent.getStringExtra(EXTRA_SEARCH_URL));
            //searchUrl = intent.getStringExtra(EXTRA_SEARCH_URL);
            //debug(getClass(), searchUrl.toString());
            hasInten = true;
        } else {
            //searchUrl = Config.URL_LIHAT_DATA_TERNAK + "?userid=" + user.userid + "&aksi=3";
            searchUrl.put("userid", user.userid);
            searchUrl.put("aksi", "3");
            hasInten = false;
        }

        if (Util.isInternetAvailible(this)) {
            Map<String, String> param = new HashMap<>();
            param.put("aksi", "1");
            param.put("userid", user.userid);
            fromDrawer = false;
            viewModel.hitungTernak(param, false);
            viewModel.searchData(searchUrl, hasInten);
        }
    }

    private void observe() {
        viewModel.getResponseDataTernak().observe(this, dataTernak -> {
            if (dataTernak!=null) {
                if (dataTernak.state == State.LOADING) {
                    onSearchStart();
                } else if (dataTernak.state == State.SUCCESS) {
                    if (dataTernak.data != null) {
                        onSearchSuccess(dataTernak.data);
                    } else {
                        onSearchError(dataTernak.message);
                    }
                } else {
                    onSearchError(dataTernak.message);
                }
            }
        });

        viewModel.getResponseHitungTernak().observe(this, dataTernak -> {
            if (dataTernak!=null) {
                if (dataTernak.state == State.LOADING) {
                    onHitungTernakStart();
                } else if (dataTernak.state == State.SUCCESS) {
                    if (dataTernak.data != null) {
                        onHitungTernakSuccess(dataTernak.data);
                    } else {
                        onHitungTernakError(dataTernak.message);
                    }
                } else {
                    onHitungTernakError(dataTernak.message);
                }
            }
        });

        viewModel.getResponseHapusTernak().observe(this, respon -> {
            if (respon != null) {
                if (respon.state == State.LOADING) {
                    onHapusStart();
                } else if (respon.state == State.SUCCESS) {
                    onHapusSuccess(respon.data);
                } else {
                    onHapusError(respon.message);
                }
            }
        });

        viewModel.getListDataTernak().observe(this, listTernak -> {
            if (listTernak !=null) {
                Log.d("observe", listTernak.size()+"");
                adapter.addToList(listTernak);
            }
        });
    }

    private void onUserIdEntered(String uid, boolean fromScanner) {
        //searchUrl = Config.URL_LIHAT_DATA_TERNAK + "?userid=" + user.userid + "&aksi=3&id=" + uid;
        searchUrl = new HashMap<>();
        searchUrl.put("userid", user.userid);
        searchUrl.put("aksi", "3");
        searchUrl.put("id", uid);
        viewModel.searchData(searchUrl, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_rangking, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_ranging) {
            startActivity(new Intent(this, RangkingActivity.class));
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 3) {
                viewModel.searchData(searchUrl, false);
            } else if (requestCode == IntentIntegrator.REQUEST_CODE) {
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if (result != null) {
                    String barcodeResult = result.getContents();
                    editTextUserId.setText(barcodeResult);
                    debug(getClass(), "barCodeResult: " + result);
                    if (barcodeResult != null) {
                        onUserIdEntered(barcodeResult, true);
                        Snackbar.make(findViewById(R.id.activity_base_search_data_FAB), getString(R.string.search_by_userid_, barcodeResult), Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        } else super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onReceiveChanged() {
        viewModel.searchData(searchUrl, false);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            DataTernakRepo.getInstance().clearList();
            super.onBackPressed();
    }

    private void bindJumlahTernak() {
        HitungTernak response = Paper.book().read("jumlah_ternak_" + user.userid);
        if (response == null) return;
        if (response.getJumlahSapi() == null) return;
        if (response.getJumlahKambing() == null) return;

        TableLayout tl = findViewById(R.id.tableLayoutJumlahSapi);
        tl.removeAllViews();
        View v;
        LayoutInflater inflater = LayoutInflater.from(this);
        int pos1 = 0;
        for (final JumlahSapi js : response.getJumlahSapi()) {
            v = inflater.inflate(R.layout.item_row_jumlah_ternak, null, false);
            ((TextView) v.findViewById(R.id.textViewJumlah)).setText(String.valueOf(js.getJumlah()));
            ((TextView) v.findViewById(R.id.textViewKeterangan)).setText(js.getKeterangan());
            if (pos1 % 2 == 0) {
                v.setBackgroundColor(getResources().getColor(R.color.warna_bg_menu));
            } else {
                v.setBackgroundColor(Color.WHITE);
            }
            tl.addView(v);
            v.setOnClickListener(view -> {
                drawerLayout.closeDrawer(GravityCompat.START);
                searchDataTernak(js.getKeterangan(), js.getJumlah(), js.getId(), js.getKodeTernak());
            });
            pos1++;
        }

        tl = findViewById(R.id.tableLayoutJumlahKambing);
        tl.removeAllViews();
        int pos2 = 0;
        for (final JumlahKambing jt : response.getJumlahKambing()) {
            v = inflater.inflate(R.layout.item_row_jumlah_ternak, null, false);
            ((TextView) v.findViewById(R.id.textViewJumlah)).setText(String.valueOf(jt.getJumlah()));
            ((TextView) v.findViewById(R.id.textViewKeterangan)).setText(jt.getKeterangan());
            if (pos2 % 2 == 0) {
                v.setBackgroundColor(getResources().getColor(R.color.warna_bg_menu));
            } else {
                v.setBackgroundColor(Color.WHITE);
            }
            tl.addView(v);
            v.setOnClickListener(view -> {
                drawerLayout.closeDrawer(GravityCompat.START);
                searchDataTernak(jt.getKeterangan(), jt.getJumlah(), jt.getId(), jt.getKodeTernak());
            });
            pos2++;
        }
    }

    private void searchDataTernak(String title, int jumlah, String id, int kode) {
        int j = 0;
        try {
            j = jumlah;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (j > 0) {
            //presenter.searchData(Config.URL_HITUNG_TERNAK + "?aksi=2&userid=" + user.userid +
            // "&id_bangsa=" + id + "&kode_ternak=" + kode);
            Map<String, String> param = new HashMap<>();
            param.put("aksi", "2");
            param.put("userid", user.userid);
            param.put("id_bangsa", id);
            param.put("kode_ternak", String.valueOf(kode));
            fromDrawer = true;
            viewModel.hitungTernak(param, true);

            getSupportActionBar().setTitle(title);
        }
    }

    public void onHitungTernakStart() {
        textLoadingJumlahSapi.setText("Lading...");
        textloadingJumlahKambing.setText("Loading...");
        textViewNoData.setVisibility(View.INVISIBLE);
        if (fromDrawer) {
            listSize = 0;
            adapter.clearList();
            rv.setVisibility(View.VISIBLE);
        }
    }

    public void onHitungTernakSuccess(HitungTernak response) {
        Paper.book().write("jumlah_ternak_" + user.userid, response);
        textLoadingJumlahSapi.setText("");
        textloadingJumlahKambing.setText("");

        bindJumlahTernak();
        if (fromDrawer) {
            textViewNoData.setVisibility(View.INVISIBLE);
            listSize = response.getDataTernak().size();
            boolean enableScroll = listSize > 0;
            tvTotal.setText(String.format("Total %d", response.getTotalSapi()));
            tvTotal.setVisibility(View.VISIBLE);
            if (enableScroll) {
                currentPage = PAGE_START;
                viewModel.getListPage(currentPage);
            } else {
                rv.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void onHitungTernakError(String message) {
        //Log.d("observe", message);
        textLoadingJumlahSapi.setText(message);
        textloadingJumlahKambing.setText(message);

        if (fromDrawer) {
            textViewNoData.setText(message);
            textViewNoData.setVisibility(View.VISIBLE);
        }
    }

    public void onSearchStart() {
        debug(getClass(), "start searching...");
        listSize = 0;
        textViewNoData.setVisibility(View.INVISIBLE);
        rv.setVisibility(View.VISIBLE);
        adapter.clearList();
    }

    public void onSearchSuccess(DataTernakResponse response) {
        debug(getClass(), "success searching");
        Paper.book().write("data_ternak_" + user.userid, response);
        listSize = response.getDataTernak().size();

        tvTotal.setText(String.format("Total %d", response.getTotalSapi()));
        tvTotal.setVisibility(View.VISIBLE);
        textViewNoData.setVisibility(View.INVISIBLE);
        boolean enableScroll = listSize > 0;
        if (enableScroll) {
            currentPage = PAGE_START;
            viewModel.getListPage(currentPage);
        } else {
            rv.setVisibility(View.INVISIBLE);
        }

		/*
		if (isFromScanner && list.size() == 1) {
			Intent intent = new Intent(this, DetailTernakActivity.class);
			intent.putExtra(DetailTernakActivity.ID_TERNAK, list.get(0).getIdTernak());
			intent.putExtra("eartag", "-");
			startActivity(intent);
		}
		 */
    }

    private void bindList(List<DataTernak> listTernak) {
        adapter.addToList(listTernak);
    }

    /*
    private void bindList() {
        nestedScrollView.fullScroll(View.FOCUS_UP);
        currentPage = PAGE_START;
        isLastPage = false;
        if (!list.isEmpty()) {
            list.clear();
            adapter.notifyDataSetChanged();
        }
        addList();
        layoutLoadMore.setVisibility(View.VISIBLE);
        progress.setVisibility(View.INVISIBLE);
    }

    private void addList() {
        int page = (PAGES_LIMIT * (currentPage - 1));
        int size = list.size();
        int limit = PAGES_LIMIT + page;
        if (limit > size) {
            limit = size;
        }

        isLastPage = limit == size;
        if (!isLastPage) {
            for (int i = page; i < limit; i++) {
                list.add(response.getDataTernak().get(i));
                adapter.notifyDataSetChanged();
            }
        }
        layoutLoadMore.setVisibility(isLastPage ? View.GONE : View.VISIBLE);
    }
     */

    public void onSearchError(String message) {
        //Log.d("observe", message);
        //Toast.makeText(this, R.string.an_error_ocurred, Toast.LENGTH_SHORT).show();
        textViewNoData.setText(message);
        textViewNoData.setVisibility(View.VISIBLE);
    }

    @Override
    public void OnDataTernakItemClick(String id, int position) {
        //Toast.makeText(this, "pos:"+position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, DetailTernakActivity.class);
        intent.putExtra(DetailTernakActivity.ID_TERNAK, id);
        startActivity(intent);
    }

    @Override
    public void onItemDeleteButtonClickListener(String id, String nama, int listPosition) {
        listPos = listPosition;
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(R.string.delete_)
                .setMessage(getString(R.string.data_ternak_, nama) + " - " + id)
                .setPositiveButton(R.string.delete, (dialog, which) -> deleteDataternak(id))
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void deleteDataternak(String id) {
        if (!Util.isInternetAvailible(this)) {
            Util.noInternetDialog(this);
            return;
        }
        //String url = Config.URL_LIHAT_DATA_TERNAK + "?userid=" + user.userid + "&aksi=2&id=" + list.get(listPosition).getId();
        //debug(getClass(), "Delete data ternak url: " + url);
        Map<String, String> param = new HashMap<>();
        param.put("aksi", "2");
        param.put("id", id);
        param.put("userid", user.userid);
        viewModel.hapusTernak(param);
		/*
		VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
			@Override
			public void onResponse(JsonObject jsonObject) {
				dialog.dismiss();
				debug(getClass(), "Delete Data ternak, Onresponse: " + jsonObject);
				Toast.makeText(DataTernakActivity.this, jsonObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
				if (jsonObject.get("success").getAsBoolean()) {
					list.remove(listPosition);
					adapter.notifyDataSetChanged();
				}
			}
		});
		 */
    }

    public void onHapusStart() {
        dialog = ProgressDialog.show(this, null, getString(R.string.please_wait), true, false);
    }

    public void onHapusSuccess(BaseResponse response) {
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
        Toast.makeText(DataTernakActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
        adapter.remove(listPos);
    }

    public void onHapusError(String message) {
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
        Toast.makeText(DataTernakActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public HashMap<String, String> getQueryString(String url) {
        Uri uri = Uri.parse(url);

        HashMap<String, String> map = new HashMap<>();
        for (String paramName : uri.getQueryParameterNames()) {
            if (paramName != null) {
                String paramValue = uri.getQueryParameter(paramName);
                if (paramValue != null) {
                    map.put(paramName, paramValue);
                }
            }
        }
        return map;
    }

    @Override
    public void scanCallback(String result) {
        if (result != null) {
            editTextUserId.setText(result);
            debug(getClass(), "barCodeResult: " + result);
            onUserIdEntered(result, true);
            Snackbar.make(findViewById(R.id.activity_base_search_data_FAB), getString(R.string.search_by_userid_, result), Snackbar.LENGTH_SHORT).show();
        }
    }

    private void hideTotal() {
        if (layoutBottomVisible) {
            tvTotal.animate().translationY(tvTotal.getHeight() + 32)
                    .setInterpolator(new AccelerateInterpolator(2)).start();
            layoutBottomVisible = false;
        }
    }

    private void showTotal() {
        if (!layoutBottomVisible) {
            tvTotal.animate().translationY(0)
                    .setInterpolator(new DecelerateInterpolator(2)).start();
            layoutBottomVisible = true;
        }
    }

    @IntDef({MODE_LINEARLAYOUT, MODE_GRIDLAYOUT, MODE_STAGGEREDGRIDLAYOUT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ManagerMode{}
}