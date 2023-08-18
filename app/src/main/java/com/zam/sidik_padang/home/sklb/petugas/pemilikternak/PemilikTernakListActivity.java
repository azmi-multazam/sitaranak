package com.zam.sidik_padang.home.sklb.petugas.pemilikternak;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zam.sidik_padang.BaseLogedinActivity;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.api.State;
import com.zam.sidik_padang.databinding.ActivityPemilikTernakListBinding;
import com.zam.sidik_padang.home.sklb.petugas.vm.PetugasSource;
import com.zam.sidik_padang.home.sklb.petugas.vm.PetugasViewModel;
import com.zam.sidik_padang.home.sklb.petugas.vm.pemilik.PemilikTernak;
import com.zam.sidik_padang.util.Util;

public class PemilikTernakListActivity extends BaseLogedinActivity implements PemilikAdapter.PemilikCallback {

    private ActivityPemilikTernakListBinding binding;
    private PetugasViewModel viewModel;

    private LinearLayout layoutMsg;
    private TextView txtMsg;
    private ProgressBar progressBar;
    private RecyclerView rvList;
    private PemilikAdapter adapter;
    private List<PemilikTernak> list;
    private boolean picked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPemilikTernakListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pemilik Ternak");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        picked = getCallingActivity() != null;

        progressBar = binding.progress;
        layoutMsg = binding.layoutMessage;
        txtMsg = binding.msgText;

        viewModel = new ViewModelProvider(this).get(PetugasViewModel.class);
        list = new ArrayList<>();
        adapter = new PemilikAdapter(list, this);
        rvList = binding.rvList;
        rvList.setAdapter(adapter);

        observe();
    }

    private void observe() {
        viewModel.getResponsePemilikTernak().observe(this, pemilikResponse -> {
            if (pemilikResponse != null) {
                progressBar.setVisibility(View.INVISIBLE);
                if (pemilikResponse.state == State.SUCCESS && pemilikResponse.success && pemilikResponse.data != null) {
                    success(pemilikResponse.data.getPemilikTernak());
                } else if (pemilikResponse.state == State.LOADING) {
                    rvList.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    layoutMsg.setVisibility(View.INVISIBLE);
                } else {
                    rvList.setVisibility(View.INVISIBLE);
                    layoutMsg.setVisibility(View.VISIBLE);
                    txtMsg.setText(pemilikResponse.message);
                }
            }
        });

        if (Util.isInternetAvailible(this)) {
            viewModel.getPemilikTernak(user.userid);
        } else {
            rvList.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            layoutMsg.setVisibility(View.VISIBLE);
            txtMsg.setText("Tidak ada koneksi internet");
        }

        viewModel.getPemilikTernak(user.userid);
    }

    private void success(List<PemilikTernak> list) {
        if (list.size() == 0) {
            rvList.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            layoutMsg.setVisibility(View.VISIBLE);
            txtMsg.setText("Data kosong");
        } else {
            rvList.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            layoutMsg.setVisibility(View.INVISIBLE);

            adapter.setList(list);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (PetugasSource.getInstance().isPetugasUpdated()) {
            PetugasSource.getInstance().setPemilikUpdated(false);
            viewModel.getPemilikTernak(user.userid);
        }
    }

    private void updateList() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add) {
            startActivity(new Intent(this, TambahPemilikTernakActivity.class));
            return true;
        } else return false;
    }

    @Override
    public void onPemilikClick(String kode, String nama) {
        if (picked) {
            Intent data = new Intent();
            data.putExtra("kode", kode);
            data.putExtra("nama", nama);
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    public void onEditClick(String data) {
        startActivity(new Intent(this, TambahPemilikTernakActivity.class)
                .putExtra("data", data));
    }

    @Override
    public void onDeleteClick(String id, String nama) {
        Map<String, String> param = new HashMap<>();
        param.put("id", id);
        param.put("aksi", "3");
        param.put("userid", user.userid);

        new AlertDialog.Builder(this)
                .setTitle("Hapus Pemilik Ternak")
                .setMessage("Hapus " + nama + "?")
                .setPositiveButton("Ya", (dialog, which) -> viewModel.hapusPemilik(param))
                .setNegativeButton("Batal", null)
                .show();
    }
}