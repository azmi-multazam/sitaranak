package com.zam.sidik_padang.home.ppob.history;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

//import com.zam.sidik_padang.BuildConfig;
import com.zam.sidik_padang.R;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.User;
import com.zam.sidik_padang.util.customclasses.RecyclerItemClickListener;


/**
 * Created by supriyadi on 7/13/17.
 */

public abstract class BaseHistoryFragment extends Fragment implements View.OnClickListener {

    protected RecyclerView recyclerView;
    protected Calendar calendarMulai, calendarSampai;
    protected SimpleDateFormat dateFormatForRequest;
    protected User user;
    protected View progressBar;
    private TextView textTanggalMulai, textTanggalSampai;
    private SimpleDateFormat dateFormat;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        String userJson = PreferenceManager.getDefaultSharedPreferences(context).getString(Config.PREF_USER_DETAIL_JSON, "");
        if (!userJson.isEmpty()) {
            user = new Gson().fromJson(userJson, User.class);
        } else debug(getClass(), "Terjadi kesalahan saat load user. userJson=" + userJson);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        calendarMulai = Calendar.getInstance();
        calendarSampai = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
        dateFormatForRequest = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_history_RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressBar = view.findViewById(R.id.fragment_historyProgressBar);
        textTanggalMulai = (TextView) view.findViewById(R.id.pilih_tanggal_Mulai);
        textTanggalMulai.setOnClickListener(this);
        textTanggalMulai.setText(dateFormat.format(calendarMulai.getTime()));
        textTanggalSampai = (TextView) view.findViewById(R.id.pilih_tanggal_sampai);
        textTanggalSampai.setOnClickListener(this);
        textTanggalSampai.setText(dateFormat.format(calendarSampai.getTime()));

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                onRecyclerItemClick(view, position);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                // TODO: Implement this method
            }
        }));
    }


    protected void debug(Class<?> cls, String s) {
        Log.d(cls.getName(), s);
        //if (BuildConfig.DEBUG) Log.e(cls.getName(), s);
    }

    @Override
    public void onClick(View p1) {
        int id = p1.getId();
        if (id == R.id.pilih_tanggal_Mulai) {
            pilihTanggal(true);
        } else if (id == R.id.pilih_tanggal_sampai) {
            pilihTanggal(false);
        }
    }

    private void pilihTanggal(final boolean tanggalMulai) {
        final Calendar calToday = Calendar.getInstance();

        DatePickerDialog dialog;
        if (tanggalMulai) {
            dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker p1, int p2, int p3, int p4) {
                    calendarMulai.set(p2, p3, p4);
                    if (calendarMulai.getTime().after(calToday.getTime()))
                        calendarMulai.set(calToday.get(Calendar.YEAR), calToday.get(Calendar.MONTH), calToday.get(Calendar.DAY_OF_MONTH));
                    textTanggalMulai.setText(dateFormat.format(calendarMulai.getTime()));
                    onCalendarBerubah();
                }
            }, calendarMulai.get(Calendar.YEAR), calendarMulai.get(Calendar.MONTH), calendarMulai.get(Calendar.DAY_OF_MONTH));
        } else {

            dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker p1, int p2, int p3, int p4) {
                    calendarSampai.set(p2, p3, p4);
                    if (calendarSampai.getTime().after(calToday.getTime()))
                        calendarSampai.set(calToday.get(Calendar.YEAR), calToday.get(Calendar.MONTH), calToday.get(Calendar.DAY_OF_MONTH));
                    textTanggalSampai.setText(dateFormat.format(calendarSampai.getTime()));
                    onCalendarBerubah();
                }
            }, calendarSampai.get(Calendar.YEAR), calendarSampai.get(Calendar.MONTH), calendarSampai.get(Calendar.DAY_OF_MONTH));
        }
        dialog.show();
    }

    protected abstract void onCalendarBerubah();

    protected abstract void onRecyclerItemClick(View view, int position);
}
