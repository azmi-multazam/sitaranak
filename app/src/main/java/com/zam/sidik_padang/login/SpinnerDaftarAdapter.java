package com.zam.sidik_padang.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.util.Db;

/**
 * Created by supriyadi on 5/21/17.
 */

public class SpinnerDaftarAdapter extends BaseAdapter implements android.widget.SpinnerAdapter {

    private List<Map<String, String>> list;
    private int padding = -1;
    LayoutInflater inflater;

    public SpinnerDaftarAdapter(Context context, List<Map<String, String>> list) {
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
        view = inflater.inflate(R.layout.item_spinner, null);
        TextView tv = view.findViewById(R.id.text);
        tv.setText(list.get(i).get(Db.TABLE_PROVINSI_NAMA));
		/*
		if (view == null) view = new TextView(viewGroup.getContext());
		if (padding == -1)
			padding = (int) (viewGroup.getContext().getResources().getDisplayMetrics().density * 10);
		view.setPadding(padding, padding, padding, padding);
		((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
		((TextView) view).setTextColor(Color.BLACK);
		((TextView) view).setText(list.get(i).get(Db.TABLE_PROVINSI_NAMA));
		 */
        return view;
    }

    @Override
    public View getDropDownView(int i, View view, ViewGroup viewGroup) {
        if (view == null) view = new TextView(viewGroup.getContext());
        if (padding == -1)
            padding = (int) (viewGroup.getContext().getResources().getDisplayMetrics().density * 10);
        view.setPadding(padding, padding, padding, padding);
        ((TextView) view).setTextColor(Color.BLACK);
        ((TextView) view).setText(list.get(i).get(Db.TABLE_PROVINSI_NAMA));
        return view;
    }

}
