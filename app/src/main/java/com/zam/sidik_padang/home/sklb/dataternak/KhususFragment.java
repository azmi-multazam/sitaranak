package com.zam.sidik_padang.home.sklb.dataternak;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import com.zam.sidik_padang.api.State;
import com.zam.sidik_padang.databinding.FragmentSklbDataTernakKhususBinding;
import com.zam.sidik_padang.home.dataternak.detailternak.DetailTernakActivity;
import com.zam.sidik_padang.home.sklb.SklbBaseFragment;
import com.zam.sidik_padang.home.sklb.dataternak.vm.DataTernakPetugas;
import com.zam.sidik_padang.home.sklb.dataternak.vm.DataTernakViewModel;

public class KhususFragment extends SklbBaseFragment implements DataTernakAdapter.OnTernakItemClickListener {

    private FragmentSklbDataTernakKhususBinding binding;
    private DataTernakAdapter adapter;
    private List<DataTernakPetugas> petugasList;
    private DataTernakViewModel viewModel;

    private RecyclerView rv;
    private LinearLayout progress;
    private LinearLayout layoutMsg;
    private TextView tvError, tvTotal;
    private boolean layoutBottomVisible = true;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSklbDataTernakKhususBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layoutMsg = binding.layoutMessage;
        tvError = binding.msgText;
        tvTotal = binding.TextViewTotal;
        progress = binding.progress;
        rv = binding.listPetugas;
        petugasList = new ArrayList<>();
        adapter = new DataTernakAdapter(this);
        rv.setAdapter(adapter);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && layoutBottomVisible) {
                    hideTotal();
                } else if (dy < 0 && !layoutBottomVisible) {
                    showTotal();
                }
            }
        });

        viewModel = new ViewModelProvider(requireActivity()).get(DataTernakViewModel.class);
        viewModel.getResponseData().observe(getViewLifecycleOwner(), responseData -> {
            if (responseData != null) {
                if (responseData.success && responseData.data != null) {
                    petugasList = responseData.data.getDataTernakPetugas();
                    success(String.valueOf(responseData.data.getTotalSapiPetugas()));
                } else if (responseData.state == State.LOADING) loading();
                else error(responseData.message);
            }
        });
    }

    public void loading() {
        rv.setVisibility(View.INVISIBLE);
        layoutMsg.setVisibility(View.INVISIBLE);
        progress.setVisibility(View.VISIBLE);
        tvTotal.setVisibility(View.INVISIBLE);
    }

    private void success(String total) {
        progress.setVisibility(View.INVISIBLE);
        if (petugasList.size() == 0) {
            rv.setVisibility(View.INVISIBLE);
            layoutMsg.setVisibility(View.VISIBLE);
            tvError.setText("Data kosong");
        } else {
            layoutMsg.setVisibility(View.INVISIBLE);
            rv.setVisibility(View.VISIBLE);
            adapter.setList(petugasList);

            tvTotal.setText(String.format("Total %s", total));
            tvTotal.setVisibility(View.VISIBLE);
        }
    }

    private void error(String message) {
        rv.setVisibility(View.INVISIBLE);
        progress.setVisibility(View.INVISIBLE);

        tvTotal.setVisibility(View.INVISIBLE);
        tvError.setVisibility(View.VISIBLE);
        tvError.setText(message);
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

    @Override
    public void OnDataTernakItemClick(int position) {
        Intent intent = new Intent(requireActivity(), DetailTernakActivity.class);
        intent.putExtra(DetailTernakActivity.ID_TERNAK, petugasList.get(position).getIdTernak());
        intent.putExtra("from", "sklb");
        startActivity(intent);
    }

    @Override
    public void onItemDeleteButtonClickListener(int listPosition) {

    }
}
