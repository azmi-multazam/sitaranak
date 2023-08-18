package com.zam.sidik_padang.util.listmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.zam.sidik_padang.R;


public class HomeMenuView extends RecyclerView {

    private List<ItemMenu> itemMenuList;
    private int count = 4;
    private OnMenuClickListener listener;

    public HomeMenuView(Context context) {
        super(context);
        buildMenu();
    }

    public HomeMenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        buildMenu();
    }

    public HomeMenuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        buildMenu();
    }

    public HomeMenuView setListMenu(List<ItemMenu> itemMenuList) {
        this.itemMenuList = itemMenuList;
        return this;
    }

    public HomeMenuView setCountSpace(int count) {
        this.count = count;
        return this;
    }

    public HomeMenuView setListener(OnMenuClickListener listener) {
        this.listener = listener;
        return this;
    }

    public ItemMenu getMenuItem(int position) {
        return itemMenuList.get(position);
    }

    public void buildMenu() {
        setNestedScrollingEnabled(false);
        setHasFixedSize(true);
        setLayoutManager(new GridLayoutManager(getContext(), count));
        BottomAdapter adapter = new BottomAdapter(getList());
        setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /*
    public void show() {
        if (!isShowing()) {
            this.animate().translationY(0)
                    .setInterpolator(new DecelerateInterpolator(2)).start();
        }
        visible = true;
    }

    public void hide() {
        if (isShowing()) {
            this.animate().translationY(this.getHeight() + 32)
                    .setInterpolator(new AccelerateInterpolator(2)).start();
        }
        visible = false;
    }

    public boolean isShowing() {
        return visible;
    }
     */

    public List<ItemMenu> getList() {
        if (itemMenuList == null) {
            itemMenuList = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                itemMenuList.add(new ItemMenu(R.drawable.ic_cow, "Judul " + (i + 1), String.valueOf(i + 1)));
            }
        }
        return itemMenuList;
    }

    public interface OnMenuClickListener {
        void onItemMenuClick(int position, ItemMenu item);
    }

    public static class ItemMenu {

        private int icon;
        private String title, id;

        public ItemMenu(int icon, String title, String id) {
            this.icon = icon;
            this.title = title;
            this.id = id;
        }

        public ItemMenu() {
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public class BottomAdapter extends Adapter<BottomAdapter.MenuHolder> {

        private final List<ItemMenu> itemMenuList;

        public BottomAdapter(List<ItemMenu> itemMenuList) {
            this.itemMenuList = itemMenuList;
        }

        @NonNull
        @Override
        public MenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(HomeMenuView.this.getContext()).inflate(R.layout.item_home_menu, parent, false);
            return new MenuHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MenuHolder holder, int position) {
            ItemMenu itemMenu = itemMenuList.get(position);
            holder.title.setText(itemMenu.getTitle());
            holder.ikon.setImageResource(itemMenu.getIcon());
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return itemMenuList.size();
        }

        private class MenuHolder extends ViewHolder implements OnClickListener {
            private final TextView title;
            private final ImageView ikon;
            private CardView root;

            public MenuHolder(@NonNull View itemView) {
                super(itemView);
                ikon = itemView.findViewById(R.id.icon);
                title = itemView.findViewById(R.id.title);
                root = itemView.findViewById(R.id.root);
                root.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    Log.d("OnClickItemMenu", "working " + getAbsoluteAdapterPosition());
                    listener.onItemMenuClick(getAbsoluteAdapterPosition(), itemMenuList.get(getAbsoluteAdapterPosition()));
                }
            }
        }
    }
}