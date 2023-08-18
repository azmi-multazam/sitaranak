package com.zam.sidik_padang.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

/**
 * Created by supriyadi on 4/11/18.
 */

public class DatePickerDialog extends AppCompatDialogFragment {

    private OnDateSelectedListener listener;
    private CalendarDay minDate, initialDate, maxDate;


    public static DatePickerDialog getInstance(CalendarDay minDate, CalendarDay initialDate, CalendarDay maxDate) {
        Bundle b = new Bundle();
        b.putParcelable("date", initialDate);
        b.putParcelable("max_date", maxDate);
        b.putParcelable("min_date", minDate);
        DatePickerDialog dialog = new DatePickerDialog();
        dialog.setArguments(b);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey("date")) {
            initialDate = savedInstanceState.getParcelable("date");
            maxDate = savedInstanceState.getParcelable("max_date");
            minDate = savedInstanceState.getParcelable("min_date");
        } else {
            Bundle b = getArguments();
            if (b != null && b.containsKey("date")) {
                initialDate = b.getParcelable("date");
                maxDate = b.getParcelable("max_date");
                minDate = b.getParcelable("min_date");
            }
        }
        if (initialDate == null) initialDate = CalendarDay.today();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDateSelectedListener) listener = (OnDateSelectedListener) context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialCalendarView cv = new MaterialCalendarView(getActivity());
        cv.setTopbarVisible(true);
        cv.setCurrentDate(initialDate);
        cv.setSelectedDate(initialDate);
        if (maxDate != null) cv.state().edit().setMaximumDate(maxDate).commit();
        if (minDate != null) cv.state().edit().setMinimumDate(minDate).commit();
        cv.setOnDateChangedListener(listener);
        return new androidx.appcompat.app.AlertDialog.Builder(getActivity()).setView(cv)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
