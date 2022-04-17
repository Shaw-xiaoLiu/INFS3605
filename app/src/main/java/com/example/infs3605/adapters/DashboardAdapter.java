package com.example.infs3605.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infs3605.R;
import com.example.infs3605.databinding.ItemDashboardBinding;
import com.example.infs3605.dto.DashboardInfo;

import java.util.ArrayList;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.PlaceHolder> {

    private final Context context;
    private final ArrayList<DashboardInfo> dashboardList;

    public DashboardAdapter(Context context, ArrayList<DashboardInfo> dashboardList) {
        this.context = context;
        this.dashboardList = dashboardList;
    }

    @NonNull
    @Override
    public PlaceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dashboard, parent, false);
        return new PlaceHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaceHolder holder, int position) {
        DashboardInfo dashboardInfo = dashboardList.get(position);
        holder.binding.tvDash.setText(dashboardInfo.getTitle());
        holder.binding.iv.setImageResource(dashboardInfo.getImage());
    }

    @Override
    public int getItemCount() {
        return dashboardList.size();
    }

    class PlaceHolder extends RecyclerView.ViewHolder {
        public ItemDashboardBinding binding;

        public PlaceHolder(View itemView) {
            super(itemView);
            binding = ItemDashboardBinding.bind(itemView);

            itemView.setOnClickListener(v -> {
                Intent intent;

                switch (getAdapterPosition()) {
                    case 0:
                        Uri uri1 = Uri.parse("https://www.health.nsw.gov.au/Infectious/covid-19/Pages/stats-nsw.aspx");
                        intent = new Intent(Intent.ACTION_VIEW, uri1);
                        break;
                    case 1:
                        Uri uri2 = Uri.parse("https://www.nsw.gov.au/covid-19/stay-safe/testing/clinics");
                        intent = new Intent(Intent.ACTION_VIEW, uri2);
                        break;
                    case 2:
                        Uri uri3 = Uri.parse("https://www.nsw.gov.au/covid-19/stay-safe/rules/people-in-nsw");
                        intent = new Intent(Intent.ACTION_VIEW, uri3);
                        break;
                    case 3:
                        Uri uri4 = Uri.parse("https://www.nsw.gov.au/covid-19/stay-safe/testing/self-isolation-rules");
                        intent = new Intent(Intent.ACTION_VIEW, uri4);
                        break;
                    case 4:
                        Uri uri5 = Uri.parse("https://www.nsw.gov.au/covid-19/business/rules-guidance/nsw-rules");
                        intent = new Intent(Intent.ACTION_VIEW, uri5);
                        break;

                    default:
                        throw new IllegalArgumentException("No intent for this item.");
                }
                context.startActivity(intent);
            });
        }
    }


}