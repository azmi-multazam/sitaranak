package com.zam.sidik_padang.home.sklb.dataternak;

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
import com.zam.sidik_padang.home.sklb.dataternak.vm.DataTernakViewModel;

public class DataTernakFragment extends BaseFragment {

    private FragmentSklbDataTernakBinding binding;

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
        binding = FragmentSklbDataTernakBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NotNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewPager viewPager = binding.activitySklbViewPager;
        DataTernakPagerAdapter adapter = new DataTernakPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = binding.activitySklbTabLayout;
        tabLayout.setupWithViewPager(viewPager);

        DataTernakViewModel viewModel = new ViewModelProvider(requireActivity()).get(DataTernakViewModel.class);
        viewModel.getDataTernak(user.userid);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
