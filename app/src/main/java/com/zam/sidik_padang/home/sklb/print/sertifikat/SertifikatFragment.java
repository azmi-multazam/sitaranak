package com.zam.sidik_padang.home.sklb.print.sertifikat;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import com.zam.sidik_padang.databinding.FragmentSklbDataTernakBinding;
import com.zam.sidik_padang.home.dataternak.detailternak.BaseFragment;
import com.zam.sidik_padang.home.sklb.print.sertifikat.vm.SertifikatViewModel;

public class SertifikatFragment extends BaseFragment {

    private FragmentSklbDataTernakBinding binding;

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
        binding = FragmentSklbDataTernakBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NotNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewPager viewPager = binding.activitySklbViewPager;
        SertifikatPagerAdapter adapter = new SertifikatPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = binding.activitySklbTabLayout;
        tabLayout.setupWithViewPager(viewPager);

        SertifikatViewModel viewModel = new ViewModelProvider(requireActivity()).get(SertifikatViewModel.class);
        viewModel.getSertifikat(user.userid);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
