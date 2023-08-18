package com.zam.sidik_padang.home.selectregion;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RegionListAdapter extends RecyclerView.Adapter<RegionViewHolder> {

    private OnRegionItemClickListener listener;
    private List<Region> list;

    public RegionListAdapter(List<Region> list, OnRegionItemClickListener listener) {
        this.listener = listener;
        this.list = list;
    }


    @Override
    public RegionViewHolder onCreateViewHolder(ViewGroup p1, int p2) {
        TextView tv = new TextView(p1.getContext());
        float density = p1.getResources().getDisplayMetrics().density;
        int p = (int) (density * 10);
        tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setPadding(p, p, p, p);
        tv.setClickable(true);

        return new RegionViewHolder(tv);
    }

    @Override
    public void onBindViewHolder(RegionViewHolder p1, int p2) {
        final int pos = p2;
        p1.tv.setText(list.get(pos).nama);
        p1.tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View p1) {
                listener.onProvinsiItemClick(pos);
            }
        });

    }

    @Override
    public int getItemCount() {
        // TODO: Implement this method
        return list.size();
    }

    public static interface OnRegionItemClickListener {
        void onProvinsiItemClick(int position);
    }

}
