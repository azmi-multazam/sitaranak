package com.zam.sidik_padang.util.customclasses;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import java.util.List;

import com.zam.sidik_padang.R;

public class SpinnerAdapter extends BaseAdapter implements android.widget.SpinnerAdapter {


    private Drawable icDropDownDrawable;
    private List<Object> list;
    private int padding = -1;

    public SpinnerAdapter(Resources res, List<Object> list) {
        this.list = list;
        icDropDownDrawable = ResourcesCompat.getDrawable(res, R.drawable.selector_ic_drop_down, null);
        float density = res.getDisplayMetrics().density;
        icDropDownDrawable.setBounds(0, 0, (int) (20 * density), (int) (20 * density));
    }

    @Override

    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) view = new TextView(viewGroup.getContext());
        if (padding == -1)
            padding = (int) (viewGroup.getContext().getResources().getDisplayMetrics().density * 10);
        view.setPadding(padding, padding, padding, padding);
        ((TextView) view).setGravity(Gravity.CENTER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            ((TextView) view).setCompoundDrawablesRelative(null, null, icDropDownDrawable, null);
        } else ((TextView) view).setCompoundDrawables(null, null, icDropDownDrawable, null);
        ((TextView) view).setText(list.get(i).toString());
        return view;
    }

    @Override
    public View getDropDownView(int i, View view, ViewGroup viewGroup) {
        if (view == null) view = new TextView(viewGroup.getContext());
        if (padding == -1)
            padding = (int) (viewGroup.getContext().getResources().getDisplayMetrics().density * 10);
        view.setPadding(padding, padding, padding, padding);

        ((TextView) view).setText(list.get(i).toString());
        return view;
    }

}
