package com.zam.sidik_padang.home.dataternak.dibawahnya;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

import com.zam.sidik_padang.home.dataternak.dibawahnya.vm.induk.DataInduk;

/**
 * Created by supriyadi on 4/30/18.
 */

public class DataTernakArrayAdapter extends ArrayAdapter<DataInduk> {

    private List<DataInduk> list;

    public DataTernakArrayAdapter(Context context, List<DataInduk> list) {
        super(context, android.R.layout.simple_dropdown_item_1line, list);
        this.list = list;
    }


    //	@Override
//	public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//		if (convertView == null) {
//			convertView = new TextView(parent.getContext());
//		}
//		((TextView) convertView).setText(list.get(position).id_ternak);
//		return convertView;
//	}
}
