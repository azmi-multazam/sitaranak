package com.zam.sidik_padang.home.basesearchdata;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zam.sidik_padang.R;

/**
 * Created by supriyadi on 5/12/17.
 */

public class SelectSearchModeDialog extends DialogFragment {
    private OnSearchModeSelectedListener listener;

    public SelectSearchModeDialog() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSearchModeSelectedListener)
            listener = (OnSearchModeSelectedListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, getTheme());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView rv = new RecyclerView(getActivity());
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        rv.setAdapter(new A());
        return rv;
    }

    @Override
    public void onResume() {
        Dialog dialog = getDialog();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
//			params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.TOP | Gravity.END;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setWindowAnimations(R.style.AnimAtasBawah);
        super.onResume();
    }

    public static interface OnSearchModeSelectedListener {
        void onSearchModeSelected(int position);
    }

    class A extends RecyclerView.Adapter<VH> {

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VH(new TextView(getActivity()));
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            final int pos = position;
            int textRes = position == 0 ? R.string.search_by_region : position == 1 ? R.string.enter_user_id : R.string.scan_barcode;
            holder.view.setText(textRes);
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    listener.onSearchModeSelected(pos);
                }
            });

        }


        @Override
        public int getItemCount() {
            return 3;
        }
    }

    private class VH extends RecyclerView.ViewHolder {
        TextView view;

        VH(TextView view) {
            super(view);
            this.view = view;
            float d = getResources().getDisplayMetrics().density;
            int p = (int) (d * 16);
            view.setPadding(p, p, p, p);
            view.setClickable(true);
            view.setTextColor(ResourcesCompat.getColor(getResources(), R.color.selector_grey_accent, null));
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }
}