package com.zam.sidik_padang.home.dataternak.rangking;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.dataternak.detailternak.DetailTernakActivity;

/**
 * Created by supriyadi on 9/10/17.
 */

public class RangkingListAdapter extends RecyclerView.Adapter<RangkingItemViewHolder> {


    private List<RangkingItem> list;

    public RangkingListAdapter(List<RangkingItem> list) {
        this.list = list;
    }

    @Override
    public RangkingItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RangkingItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rangking, parent, false));
    }

    @Override
    public void onBindViewHolder(RangkingItemViewHolder holder, int position) {
        final RangkingItem rangkingItem = list.get(position);
        holder.textViewIDTernak.setText(rangkingItem.id_ternak);
        holder.textViewBeratLahir.setText(rangkingItem.bb_lahir + " kg");
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailTernakActivity.class);
                intent.putExtra("eartag", "Eartag:-");
                intent.putExtra(DetailTernakActivity.ID_TERNAK, rangkingItem.id_ternak);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
