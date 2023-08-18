package com.zam.sidik_padang.home.basesearchdata;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.SelectRegionActivity;
import com.zam.sidik_padang.home.dataternak.scanner.ScanDialogFragment;
import com.zam.sidik_padang.home.selectregion.Region;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.VolleyStringRequest;

public class BaseSearchDataActivity extends BaseLogedinActivity
        implements View.OnClickListener, VolleyStringRequest.Callback, SelectSearchModeDialog.OnSearchModeSelectedListener, ScanDialogFragment.ScanListener {

    private final String VOLLEY_TAG = getClass().getName();
    private final int SELECT_REGION_REQUEST_CODE = 13;
    protected View progressBar, progressBarButtonSearch, buttonSearch, textViewNoData;
    protected VolleyStringRequest searchRequest;
    protected RecyclerView rv;
    protected EditText editTextUserId;
    private TextView textViewRegion;
    private Region selectedRegion;
    private boolean fromScanner = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_search_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setSubtitle(user.nama);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rv = (RecyclerView) findViewById(R.id.activity_base_search_data_RecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        buttonSearch = findViewById(R.id.activity_base_search_data_ButtonSearch);
        buttonSearch.setOnClickListener(this);
        findViewById(R.id.activity_base_search_data_ButtonScan).setOnClickListener(this);
        textViewRegion = (TextView) findViewById(R.id.activity_base_search_data_TextViewRegion);
        textViewRegion.setOnClickListener(this);
        findViewById(R.id.activity_base_search_data_FAB).setOnClickListener(this);
        progressBar = findViewById(R.id.activity_data_ternak_ProgressBar);
        editTextUserId = (EditText) findViewById(R.id.activity_base_search_data_EditTextUserId);
        textViewNoData = findViewById(R.id.textViewNoData);
        progressBarButtonSearch = findViewById(R.id.activity_base_search_data_ProgressBarButtonSearch);

		/*
		Drawable drawableSapi = ResourcesCompat.getDrawableForDensity(getResources(), R.mipmap.ic_launcher, 16, getTheme());
		if (drawableSapi != null) {
			int b = (int) (getResources().getDisplayMetrics().density * 16f);
			drawableSapi.setBounds(0, 0, b, b);
			TextViewCompat.setCompoundDrawablesRelative(editTextUserId, drawableSapi, null, null, null);
		}
		 */
    }

    protected void setTextNoDataVisible(boolean visible) {
        textViewNoData.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    protected void searchData(String url, boolean fromScanner) {
        this.fromScanner = fromScanner;
        if (!Util.isInternetAvailible(this)) {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        if (searchRequest != null) searchRequest.cancel();
        searchRequest = new VolleyStringRequest(url, this);
        searchRequest.setTag(VOLLEY_TAG);
        progressBar.setVisibility(View.VISIBLE);
        VolleySingleton.getInstance(this).getRequestQueue().add(searchRequest);
        debug(getClass(), "Search data. Url: " + url);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.activity_base_search_data_ButtonScan) {
            ScanDialogFragment.newInstance(this).show(getSupportFragmentManager(), "dialog");
            /*
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setBeepEnabled(true);
            integrator.setOrientationLocked(false);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.initiateScan();
             */
        } else if (id == R.id.activity_base_search_data_ButtonSearch) {
            String uid = editTextUserId.getText().toString().trim();
            if (!uid.isEmpty()) {
                onUserIdEntered(uid, false);
                progressBarButtonSearch.setVisibility(View.VISIBLE);
                buttonSearch.setEnabled(false);
            } else if (selectedRegion != null) {
                onRegionKodEntered(selectedRegion.id);
                progressBarButtonSearch.setVisibility(View.VISIBLE);
                buttonSearch.setEnabled(false);
            } else {
                Toast.makeText(this, R.string.enter_user_id_or_select_region, Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.activity_base_search_data_TextViewRegion) {
            Intent intent = new Intent(this, SelectRegionActivity.class);
            intent.putExtra(SelectRegionActivity.EXTRA_WILAYAH_MODE, user.level.equals("korpus") ? "provinsi" : "kabupaten");
            intent.putExtra(SelectRegionActivity.EXTRA_WILAYAH_CODE, user.level.equals("korpus") ? "0" : user.provinsi);
            startActivityForResult(intent, SELECT_REGION_REQUEST_CODE);
        }
    }

    @Override
    public void onResponse(JsonObject jsonObject) {
        progressBarButtonSearch.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        buttonSearch.setEnabled(true);
        String msgs = jsonObject.get("message").getAsString();
        debug(getClass(), "Search data onResponse: " + jsonObject);
        if (jsonObject.get("success").getAsBoolean()) {
            onResponseSuccess(jsonObject, fromScanner);
        } else if (isResummed) {
            Util.showDialog(this, msgs);
        }
    }

    protected void onResponseSuccess(JsonObject jsonObject, boolean fromScanner) {

    }


    protected void onUserIdEntered(String userId, boolean fromScaner) {

    }

    protected void onRegionKodEntered(String regionKode) {

    }


    @Override
    protected void onDestroy() {
        VolleySingleton.getInstance(this).getRequestQueue().cancelAll(VOLLEY_TAG);
        super.onDestroy();
    }

    @Override
    public void onSearchModeSelected(int position) {
//		if (position == 0) {
//			Intent intent = new Intent(this, SelectRegionActivity.class);
//			intent.putExtra(SelectRegionActivity.EXTRA_WILAYAH_MODE, user.level.equals("korpus") ? "provinsi" : "kabupaten");
//			intent.putExtra(SelectRegionActivity.EXTRA_WILAYAH_CODE, user.level.equals("korpus") ? "0" : user.provinsi);
//			startActivityForResult(intent, SELECT_REGION_REQUEST_CODE);
//		} else if (position == 1) {
//			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//			TextInputLayout layout = new TextInputLayout(this);
//			int p = (int) (16 * getResources().getDisplayMetrics().density);
//			layout.setPadding(p, 0, p, 0);
//			layout.setLayoutParams(params);
//			final TextInputEditText editText = new TextInputEditText(this);
//			editText.setHint(R.string.enter_user_id);
//			editText.setInputType(InputType.TYPE_CLASS_TEXT);
//			layout.addView(editText, params);
//			new AlertDialog.Builder(this)
//					.setTitle(R.string.search_by_userid)
//					.setView(layout).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					String s = editText.getText().toString().trim();
//					if (!s.isEmpty()) {
//						onUserIdEntered(s);
//					}
//				}
//			}).show();
//
//		} else if (position == 2) {
//			IntentIntegrator integrator = new IntentIntegrator(this);
//			integrator.setBeepEnabled(true);
//			integrator.setOrientationLocked(false);
////			integrator.setBarcodeImageEnabled(true);
//			integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
//			integrator.initiateScan();
//		}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_REGION_REQUEST_CODE) {
                selectedRegion = (Region) data.getSerializableExtra(SelectRegionActivity.EXTRA_WILAYAH);
                onRegionKodEntered(selectedRegion.id);
                textViewRegion.setText(selectedRegion.nama);
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
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void scanCallback(String result) {
        if (result != null) {
            editTextUserId.setText(result);
            debug(getClass(), "barCodeResult: " + result);
            onUserIdEntered(result, true);

            Snackbar snackbar = Snackbar.make(
                    findViewById(R.id.activity_base_search_data_FAB),
                    getString(R.string.search_by_userid_, result),
                    Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            TextView textView = sbView.findViewById(R.id.textViewNoData);
            if (textView!=null) textView.setTextColor(ContextCompat.getColor(this, R.color.black_1));
            snackbar.show();
            //Snackbar.make(findViewById(R.id.activity_base_search_data_FAB), getString(R.string.search_by_userid_, result), Snackbar.LENGTH_SHORT).show();
        }
    }
}
