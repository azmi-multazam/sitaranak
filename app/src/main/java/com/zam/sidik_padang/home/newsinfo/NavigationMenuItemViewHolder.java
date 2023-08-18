package com.zam.sidik_padang.home.newsinfo;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zam.sidik_padang.R;

/**
 * Created by supriyadi on 10/13/17.
 */

class NavigationMenuItemViewHolder extends RecyclerView.ViewHolder {
    TextView textView;
    View itemView;

    NavigationMenuItemViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        textView = (TextView) itemView.findViewById(R.id.item_menu_navigation_news_info_TextView);
    }
}
