package com.zam.sidik_padang.util.listmenu;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

import com.zam.sidik_padang.R;

/**
 * Created by AbangAzmi on 21/07/2017.
 */

public class LinearListView extends LinearLayout implements View.OnClickListener {

    private OnClickListener listener;
    private List<ItemMenu> list;

    public LinearListView(Context context) {
        super(context);
        build();
    }

    public LinearListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        build();
    }

    public LinearListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        build();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LinearListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        build();
    }

    public LinearListView setList(List<ItemMenu> list) {
        this.list = list;
        return this;
    }

    public LinearListView setListener(OnClickListener listener) {
        this.listener = listener;
        return this;
    }

    public void build() {
        ListAdapter adapter = new ListAdapter(this.getContext(), getList());
        for (int i = 0; i < adapter.getCount(); i++) {
            View item = adapter.getView(i, null, null);
            this.addView(item);
        }
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    public List<ItemMenu> getList() {
        if (list == null) {
            list = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                list.add(new ItemMenu(R.drawable.ic_farmer, "Judul " + (i + 1), String.valueOf(i + 1)));
            }
        }
        return list;
    }

    static class ListAdapter extends BaseAdapter {

        private final LayoutInflater lInflater;
        private final Context context;
        private ItemDetailHolder holder;
        private final List<ItemMenu> list;

        public ListAdapter(Context context, List<ItemMenu> list) {
            this.context = context;
            this.list = list;
            lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list != null ? list.size() : 30;
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View cview, ViewGroup viewGroup) {
            View view = cview;
            if (view == null) {
                view = lInflater.inflate(R.layout.item_home_menu, viewGroup, false);
                holder = new ItemDetailHolder();
                holder.icon = view.findViewById(R.id.icon);
                holder.title = view.findViewById(R.id.title);
                view.setTag(holder);
            } else {
                holder = (ItemDetailHolder) view.getTag();
            }

            ItemMenu itemList = list.get(i);

            holder.icon.setImageResource(itemList.icon);
            holder.title.setText(itemList.title);

            return view;
        }

        static class ItemDetailHolder {
            ImageView icon;
            TextView title;
        }
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
}