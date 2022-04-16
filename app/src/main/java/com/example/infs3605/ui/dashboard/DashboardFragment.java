package com.example.infs3605.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infs3605.R;
import com.example.infs3605.adapters.DashboardAdapter;
import com.example.infs3605.constants.Information;
import com.example.infs3605.databinding.FragmentDashboardBinding;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {
    private View view;
    public RecyclerView mDashboardRecycleView;
    private DashboardAdapter mDashboardAdapter;

    private ArrayList<Information> newslist = new ArrayList<Information>();

    private FragmentDashboardBinding binding;

    String[] names = {"COVID-19 in NSW","COVID-19 clinic locations","Rules for people in NSW","Self-isolation rules in NSW","Business rules and restrictions in NSW"};

    int[] imgs = {R.drawable.dash_1,R.drawable.dash_2,R.drawable.dash_3,R.drawable.dash_4,R.drawable.dash_5};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        initRecyclerView();
        initData();
        return view;

    }

    private void initData() {
        for (int i = 0; i < names.length-1; i++) {
            Information news = new Information();
            news.setTitle(names[i]);
            newslist.add(news);
        }

    }

    private void initRecyclerView() {

        mDashboardRecycleView= view.findViewById(R.id.dash_recyclerView);

        mDashboardAdapter = new DashboardAdapter(getActivity(), newslist);

        mDashboardRecycleView.setAdapter(mDashboardAdapter);

        mDashboardRecycleView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        mDashboardRecycleView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}