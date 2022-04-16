package com.example.infs3605.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infs3605.R;
import com.example.infs3605.constants.Information;

import java.util.ArrayList;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.myViewHodler>{
    private Context mcontext;

    private Intent intent;

    private ArrayList<Information> newslist;


    String[] names = {"COVID-19 in NSW","COVID-19 clinic locations","Rules for people in NSW","Self-isolation rules in NSW","Business rules and restrictions in NSW"};

    int[] imgs = {R.drawable.dash_1,R.drawable.dash_2,R.drawable.dash_3,R.drawable.dash_4,R.drawable.dash_5};
    public DashboardAdapter(Context context,ArrayList<Information> newslist) {
        this.mcontext = context;
        this.newslist = newslist;
    }

    @NonNull
    @Override
    public myViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(mcontext, R.layout.item_dashboard, null);
        return new myViewHodler(view);
    }

    @Override
    public void onBindViewHolder(myViewHodler holder, int position) {
        holder.mItem.setText(names[position]);
        holder.mImage.setImageResource(imgs[position]);
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class myViewHodler extends RecyclerView.ViewHolder {
        private TextView mItem;
        private ImageView mImage;

        public myViewHodler(View itemView) {
            super(itemView);
            mItem = (TextView) itemView.findViewById(R.id.dash);
            mImage = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch(getAdapterPosition()){
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
                    }
                    mcontext.startActivity(intent);
                }

            });
        }
    }


}