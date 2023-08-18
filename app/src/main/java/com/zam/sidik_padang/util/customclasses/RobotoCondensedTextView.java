package com.zam.sidik_padang.util.customclasses;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by supriyadi on 3/31/18.
 */

public class RobotoCondensedTextView extends AppCompatTextView {
    public RobotoCondensedTextView(Context context) {
        super(context);
        init();
    }

    public RobotoCondensedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RobotoCondensedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "RobotoCondensed-Regular.ttf");
        setTypeface(typeface);
        setTextColor(Color.BLACK);
    }
}
