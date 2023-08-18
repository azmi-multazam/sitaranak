package com.zam.sidik_padang.home.sklb.print.sertifikat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.zam.sidik_padang.api.State;
import com.zam.sidik_padang.databinding.FragmentSklbDataTernakKhususBinding;
import com.zam.sidik_padang.home.dataternak.detailternak.BaseFragment;
import com.zam.sidik_padang.home.sklb.print.LihatSertifikatActivity;
import com.zam.sidik_padang.home.sklb.print.SertifikatGeneratorActivity;
import com.zam.sidik_padang.home.sklb.print.sertifikat.vm.SertifikatTernakPetugas;
import com.zam.sidik_padang.home.sklb.print.sertifikat.vm.SertifikatTernakUmum;
import com.zam.sidik_padang.home.sklb.print.sertifikat.vm.SertifikatViewModel;
import io.paperdb.Paper;

public class UmumFragment extends BaseFragment implements SertifikatAdapter.OnDataItemClickListener {

    private FragmentSklbDataTernakKhususBinding binding;
    private SertifikatAdapter adapter;
    private List<SertifikatTernakPetugas> petugasList;
    private SertifikatViewModel viewModel;

    private RecyclerView rv;
    private LinearLayout progress;
    private LinearLayout layoutMsg;
    private TextView tvError, tvTotal;

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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        adapter = new SertifikatAdapter(this);
        rv.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity()).get(SertifikatViewModel.class);
        viewModel.getResponseData().observe(getViewLifecycleOwner(), responseData -> {
            if (responseData != null) {
                if (responseData.success && responseData.data != null) {
                    String datas = listToString(responseData.data.getSertifikatTernakUmum());
                    petugasList = stringToList(datas);
                    success(String.valueOf(responseData.data.getTotalSapiUmum()));
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
            for (int i = 0; i < petugasList.size(); i++) {
                String base = Paper.book().read("sertifikat_" + petugasList.get(i).getId());
                petugasList.get(i).setAdaSertifikat(base!=null);
            }
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

    @Override
    public void OnDataItemClick(String id, int position) {
        SertifikatTernakPetugas srt = petugasList.get(position);
        String base = Paper.book().read("sertifikat_" + id);
        String data = new Gson().toJson(srt);
        if (base == null) {
            Intent intent = new Intent(requireActivity(), SertifikatGeneratorActivity.class);
            intent.putExtra("data", data);
            startActivity(intent);
        } else {
            startActivity(new Intent(requireActivity(), LihatSertifikatActivity.class)
                    .putExtra("nama", "sertifikat_" + id)
                    .putExtra("data", data)
            );
        }
    }

    public List<SertifikatTernakPetugas> stringToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<SertifikatTernakPetugas>>() {
        }.getType();

        return new Gson().fromJson(data, listType);
    }

    public String listToString(List<SertifikatTernakUmum> someObjects) {
        return new Gson().toJson(someObjects);
    }

}
