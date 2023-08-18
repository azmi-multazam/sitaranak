package com.zam.sidik_padang.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.zam.sidik_padang.R;

/**
 * Created by supriyadi on 5/5/17.
 */

public class GridAdapter extends RecyclerView.Adapter<GridItemViewHolder> {

    private OnGridItemClickListener listener;
    private List<GridItem> list;

    public GridAdapter(OnGridItemClickListener listener, List<GridItem> list) {
        this.listener = listener;
        this.list = list;
    }


    @Override
    public GridItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GridItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member_area_grid, parent, false));
    }

    @Override
    public void onBindViewHolder(GridItemViewHolder holder, int position) {
        final int pos = position;
        GridItem item = list.get(pos);
        holder.imageView.setImageResource(item.iconID);
        holder.textView.setText(item.textId);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onGridItemClick(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static interface OnGridItemClickListener {
        void onGridItemClick(int position);
    }
}
