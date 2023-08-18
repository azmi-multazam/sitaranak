package com.zam.sidik_padang.home.newsinfo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.zam.sidik_padang.R;

/**
 * Created by supriyadi on 10/13/17.
 */

class NavigationMenuAdapter extends RecyclerView.Adapter<NavigationMenuItemViewHolder> {
    private List<NavigationMenuItem> list;
    private OnNavigationMenuItemClickListener listener;

    public NavigationMenuAdapter(List<NavigationMenuItem> list, OnNavigationMenuItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public NavigationMenuItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NavigationMenuItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_navigation_news_info, parent, false));
    }

    @Override
    public void onBindViewHolder(final NavigationMenuItemViewHolder holder, int position) {
        final NavigationMenuItem item = list.get(position);
        holder.textView.setText(item.nama);
        holder.itemView.setSelected(item.selected);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNavigationMeniItemClick(item);
                for (int i = 0; i < list.size(); i++)
                    list.get(i).selected = i == holder.getAdapterPosition();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    interface OnNavigationMenuItemClickListener {
        void onNavigationMeniItemClick(NavigationMenuItem item);
    }
}
