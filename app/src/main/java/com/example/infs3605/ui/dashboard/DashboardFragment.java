package com.example.infs3605.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.infs3605.R;
import com.example.infs3605.adapters.DashboardAdapter;
import com.example.infs3605.databinding.FragmentDashboardBinding;
import com.example.infs3605.dto.DashboardInfo;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private final ArrayList<DashboardInfo> dashboardList = new ArrayList<>();

    String[] names = {"COVID-19 in NSW", "COVID-19 clinic locations", "Rules for people in NSW", "Self-isolation rules in NSW", "Business rules and restrictions in NSW"};
    int[] images = {R.drawable.dash_1, R.drawable.dash_2, R.drawable.dash_3, R.drawable.dash_4, R.drawable.dash_5};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecyclerView();
        initData();
    }

    private void initData() {
        for (int i = 0; i < names.length; i++) {
            DashboardInfo news = new DashboardInfo(names[i], images[i]);
            dashboardList.add(news);
        }
    }

    private void initRecyclerView() {
        DashboardAdapter dashboardAdapter = new DashboardAdapter(getActivity(), dashboardList);
        binding.dashRecyclerView.setAdapter(dashboardAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}