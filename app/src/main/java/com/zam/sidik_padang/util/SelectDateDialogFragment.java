package com.zam.sidik_padang.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.zam.sidik_padang.R;

/**
 * Created by supriyadi on 9/27/17.
 */

public class SelectDateDialogFragment extends AppCompatDialogFragment {

    public static final String EXTRA_TANGGAL = "extra_tanggal";
    public static final String EXTRA_MINIMUM_DATE = "extra_minimum_date";
    public static final String EXTRA_MAXIMUM_DATE = "extra_maximum_date";
    private CalendarDay tanggal;
    private OnCalendarSelectedListener listener;
    private long minDate = 0, maxDate = 0;

    public static SelectDateDialogFragment getDialog(CalendarDay calendarDay) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_TANGGAL, calendarDay);
        SelectDateDialogFragment dialogFragment = new SelectDateDialogFragment();
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    public static SelectDateDialogFragment getDialog(CalendarDay calendarDay, long minDate, long maxDate) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_TANGGAL, calendarDay);
        bundle.putLong(EXTRA_MINIMUM_DATE, minDate);
        bundle.putLong(EXTRA_MAXIMUM_DATE, maxDate);
        SelectDateDialogFragment dialogFragment = new SelectDateDialogFragment();
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCalendarSelectedListener)
            listener = (OnCalendarSelectedListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(EXTRA_TANGGAL)) {
            tanggal = bundle.getParcelable(EXTRA_TANGGAL);
            if (bundle.containsKey(EXTRA_MINIMUM_DATE)) {
                minDate = bundle.getLong(EXTRA_MINIMUM_DATE);
                maxDate = bundle.getLong(EXTRA_MAXIMUM_DATE);
            }
        }

        if (tanggal == null) tanggal = CalendarDay.today();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (tanggal != null) {
            outState.putParcelable(EXTRA_TANGGAL, tanggal);
            if (minDate != 0) {
                outState.putLong(EXTRA_MINIMUM_DATE, minDate);
                outState.putLong(EXTRA_MAXIMUM_DATE, maxDate);
            }
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_TANGGAL)) {
            tanggal = savedInstanceState.getParcelable(EXTRA_TANGGAL);
            if (savedInstanceState.containsKey(EXTRA_MINIMUM_DATE)) {
                minDate = savedInstanceState.getLong(EXTRA_MINIMUM_DATE);
                maxDate = savedInstanceState.getLong(EXTRA_MAXIMUM_DATE);
            }
        }
        super.onViewStateRestored(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dilaog_select_date, null, false);
        builder.setView(v);
        CalendarView calendarView = (CalendarView) v.findViewById(R.id.dialog_select_date_CalendarView);
        calendarView.setDate(tanggal.getCalendar().getTimeInMillis(), false, true);
//		Log.e("select date", "minDate=" + minDate);
//		Log.e("select date", "maxDate=" + maxDate);
//		SimpleDateFormat f = new SimpleDateFormat("yyyy", Locale.getDefault());
//		Log.e("select date", "calendarMin=" + f.format(minDate));
//		Log.e("select date", "calendarMax=" + f.format(maxDate));
        if (minDate != 0) {
            calendarView.setMinDate(minDate);
            calendarView.setMaxDate(maxDate);
        }
        final TextView textView = (TextView) v.findViewById(R.id.dialog_select_date_TextViewTanggal);
        setView(textView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                tanggal = CalendarDay.from(year, month, dayOfMonth);
                setView(textView);
            }
        });
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null) listener.onCalendarSelected(tanggal);
            }
        });
        return builder.create();
    }

    private void setView(TextView textView) {
        if (tanggal == null) {
            Log.e(getClass().getName(), "Error tanggal null");
            return;
        }
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        textView.setText(format.format(tanggal.getDate()));
    }

    public static interface OnCalendarSelectedListener {
        void onCalendarSelected(CalendarDay calendarDay);
    }

}
