package com.zam.sidik_padang.profilku;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

import com.zam.sidik_padang.R;

/**
 * Created by supriyadi on 10/2/17.
 */

public class PilihanSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
    public List<Object> list;
    private final int padding = -1;
    LayoutInflater inflater;

    public PilihanSpinnerAdapter(Context context, List<Object> list) {
        this.list = list;
        inflater = (LayoutInflater.from(context));
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

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
		/*
		if (view == null) view = new TextView(viewGroup.getContext());
		if (padding == -1)
			padding = (int) (viewGroup.getContext().getResources().getDisplayMetrics().density * 10);
		view.setPadding(0, padding, padding, padding);
		((TextView) view).setText(list.get(i).toString());
		 */
        view = inflater.inflate(R.layout.item_spinner, null);
        TextView tv = view.findViewById(R.id.text);
        tv.setText(list.get(i).toString());
        return view;
    }

    @Override
    public View getDropDownView(int i, View view, ViewGroup viewGroup) {
		/*
		if (view == null) view = new TextView(viewGroup.getContext());
		if (padding == -1)
			padding = (int) (viewGroup.getContext().getResources().getDisplayMetrics().density * 10);
		view.setPadding(padding, padding, padding, padding);
		((TextView) view).setText(list.get(i).toString());
		 */
        view = inflater.inflate(R.layout.item_spinner, null);
        TextView tv = view.findViewById(R.id.text);
        tv.setText(list.get(i).toString());
        return view;
    }
}
