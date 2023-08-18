package com.zam.sidik_padang.home.sklb.petugas;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import com.zam.sidik_padang.databinding.FragmentSklbPetugasBinding;
import com.zam.sidik_padang.home.sklb.SklbBaseFragment;
import com.zam.sidik_padang.home.sklb.petugas.vm.PetugasSource;
import com.zam.sidik_padang.home.sklb.petugas.vm.PetugasViewModel;

public class PetugasFragment extends SklbBaseFragment {

    private FragmentSklbPetugasBinding binding;
    private PetugasPagerAdapter adapter;
    private ViewPager viewPager;
    private PetugasViewModel petugasViewModel;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSklbPetugasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NotNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = binding.activitySklbViewPager;
        adapter = new PetugasPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = binding.activitySklbTabLayout;
        tabLayout.setupWithViewPager(viewPager);
        petugasViewModel = new ViewModelProvider(requireActivity()).get(PetugasViewModel.class);

        PetugasSource.getInstance().setPetugasUpdated(false);
        //https://e-rekording.com/api_pamekasan/add_pamekasan.php?userid=KS1000003&aksi=4
        Map<String, String> params = new HashMap<>();
        params.put("aksi", "4");
        params.put("userid", user.userid);
        //presenter.requestPetugas(params);
        petugasViewModel.requestPetugas(params);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (PetugasSource.getInstance().isPetugasUpdated()) {
            PetugasSource.getInstance().setPetugasUpdated(false);
            //https://e-rekording.com/api_pamekasan/add_pamekasan.php?userid=KS1000003&aksi=4
            Map<String, String> params = new HashMap<>();
            params.put("aksi", "4");
            params.put("userid", user.userid);
            //presenter.requestPetugas(params);
            petugasViewModel.requestPetugas(params);
        }
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