package com.zam.sidik_padang.home.ppob.downline;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.zam.sidik_padang.R;


/**
 * Created by supriyadi on 7/29/17.
 */

public class DownlineListAdapter extends RecyclerView.Adapter<DownlineViewHolder> {
    private List<Downline> list;

    public DownlineListAdapter(List<Downline> list) {
        this.list = list;
    }

    @Override
    public DownlineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DownlineViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_downline, parent, false));
    }

    @Override
    public void onBindViewHolder(DownlineViewHolder holder, int position) {
        holder.textViewId.setText(list.get(position).userid);
        holder.textViewNama.setText(list.get(position).nama);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
