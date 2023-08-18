package com.zam.sidik_padang.home.sklb.petugas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zam.sidik_padang.api.State;
import com.zam.sidik_padang.databinding.FragmentSklbPetugasKhususBinding;
import com.zam.sidik_padang.home.sklb.SklbBaseFragment;
import com.zam.sidik_padang.home.sklb.petugas.tambah.TambahPetugasActivity;
import com.zam.sidik_padang.home.sklb.petugas.vm.Petugas;
import com.zam.sidik_padang.home.sklb.petugas.vm.PetugasViewModel;
import com.zam.sidik_padang.util.Util;

public class KhususFragment extends SklbBaseFragment implements PetugasCallback {

    private FragmentSklbPetugasKhususBinding binding;
    private PetugasAdapter adapter;
    private List<Petugas> petugasList;
    private PetugasViewModel petugasViewModel;

    private RecyclerView rv;
    private LinearLayout progress;
    private LinearLayout layoutMsg;
    private TextView tvError;
    private FloatingActionButton fab;
    private boolean isAllowed;

    public static KhususFragment newInstance() {
        return new KhususFragment();
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSklbPetugasKhususBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NotNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isAllowed = user.kelompok == 4 || user.kelompok == 5;
        fab = binding.fab;
        if (isAllowed) {
            fab.show();
        } else {
            fab.hide();
        }
        fab.setOnClickListener(view1 -> {
            startActivity(new Intent(requireActivity(), TambahPetugasActivity.class));
        });

        layoutMsg = binding.layoutMessage;
        tvError = binding.msgText;
        progress = binding.progress;
        rv = binding.listPetugas;
        petugasList = new ArrayList<>();
        adapter = new PetugasAdapter(petugasList, this);
        rv.setAdapter(adapter);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.isShown()) {
                    fab.hide();
                } else if (dy < 0 && !fab.isShown()) {
                    if (isAllowed) fab.show();
                }
            }
        });

        petugasViewModel = new ViewModelProvider(requireActivity()).get(PetugasViewModel.class);
        petugasViewModel.getResponseData().observe(getViewLifecycleOwner(), petugasResponseResponseData -> {
            if (petugasResponseResponseData != null) {
                if (petugasResponseResponseData.success && petugasResponseResponseData.data != null) {
                    petugasList = petugasResponseResponseData.data.getPetugas();
                    success();
                } else if (petugasResponseResponseData.state == State.LOADING) loading();
                else error(petugasResponseResponseData.message);
            }
        });

        petugasViewModel.getResponseDelete().observe(getViewLifecycleOwner(), response -> {
            if (response != null) {
                hideProgress();
                if (response.state == State.SUCCESS && response.success && response.data != null) {
                    new AlertDialog.Builder(requireActivity())
                            //.setTitle("")
                            .setMessage(response.message)
                            .setPositiveButton("OK", (dialog, which) -> updateList())
                            .show();
                } else if (response.state == State.LOADING) {
                    showProgress("", response.message);
                } else {
                    Util.showDialog(requireActivity(), response.message);
                }
            }
        });
    }

    private void updateList() {
        Map<String, String> params = new HashMap<>();
        params.put("aksi", "4");
        params.put("userid", user.userid);
        petugasViewModel.requestPetugas(params);
    }

    public void loading() {
        rv.setVisibility(View.INVISIBLE);
        layoutMsg.setVisibility(View.INVISIBLE);
        progress.setVisibility(View.VISIBLE);
    }

    private void success() {
        progress.setVisibility(View.INVISIBLE);
        if (petugasList.size() == 0) {
            rv.setVisibility(View.INVISIBLE);
            layoutMsg.setVisibility(View.VISIBLE);
            tvError.setText("Data kosong");
        } else {
            layoutMsg.setVisibility(View.INVISIBLE);
            rv.setVisibility(View.VISIBLE);
            adapter.setList(petugasList);
        }
    }

    private void error(String message) {
        rv.setVisibility(View.INVISIBLE);
        progress.setVisibility(View.INVISIBLE);

        layoutMsg.setVisibility(View.VISIBLE);
        tvError.setText(message);
    }

    @Override
    public void onEditClicked(String data) {
        startActivity(new Intent(requireActivity(), TambahPetugasActivity.class)
                .putExtra("data", data));
    }

    @Override
    public void onDeleteClicked(String id, String nama) {
        Map<String, String> param = new HashMap<>();
        param.put("id", id);
        param.put("aksi", "3");
        param.put("userid", user.userid);

        new AlertDialog.Builder(requireActivity())
                .setTitle("Hapus Petugas")
                .setMessage("Hapus petugas " + nama + "?")
                .setPositiveButton("Ya", (dialog, which) -> petugasViewModel.hapusPetugas(param))
                .setNegativeButton("Batal", null)
                .show();
    }
}
