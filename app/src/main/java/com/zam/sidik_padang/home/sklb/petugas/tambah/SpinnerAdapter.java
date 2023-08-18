package com.zam.sidik_padang.home.sklb.petugas.tambah;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.util.Db;

public class SpinnerAdapter extends BaseAdapter implements android.widget.SpinnerAdapter {

    private final List<Map<String, String>> list;
    private final int padding = -1;
    LayoutInflater inflater;

    public SpinnerAdapter(Context context, List<Map<String, String>> list) {
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
		/*
		if (view == null) view = new TextView(viewGroup.getContext());
		if (padding == -1)
			padding = (int) (viewGroup.getContext().getResources().getDisplayMetrics().density * 10);
		view.setPadding(padding, padding, padding, padding);
		((TextView) view).setText(list.get(i).get(Db.TABLE_PROVINSI_NAMA));
		 */
        view = inflater.inflate(R.layout.item_spinner, null);
        TextView tv = view.findViewById(R.id.text);
        tv.setText(list.get(i).get(Db.TABLE_PROVINSI_NAMA));

        return view;
    }

    @Override
    public View getDropDownView(int i, View view, ViewGroup viewGroup) {
		/*
		if (view == null) view = new TextView(viewGroup.getContext());
		if (padding == -1)
			padding = (int) (viewGroup.getContext().getResources().getDisplayMetrics().density * 10);
		view.setPadding(padding, padding, padding, padding);
		((TextView) view).setText(list.get(i).get(Db.TABLE_PROVINSI_NAMA));
		 */
        view = inflater.inflate(R.layout.item_spinner, null);
        TextView tv = view.findViewById(R.id.text);
        tv.setText(list.get(i).get(Db.TABLE_PROVINSI_NAMA));
        return view;
    }

}
