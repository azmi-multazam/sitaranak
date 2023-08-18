package com.zam.sidik_padang.util.customclasses;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.LinearLayoutCompat;

/**
 * Created by supriyadi on 10/4/17.
 */

public class LinearLayoutKotak extends LinearLayoutCompat {
    public LinearLayoutKotak(Context c, AttributeSet attributeSet) {
        super(c, attributeSet);
    }

    public LinearLayoutKotak(Context c, AttributeSet attributeSet, int i) {
        super(c, attributeSet, i);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
