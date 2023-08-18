package com.zam.sidik_padang.util.bordered.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.util.bordered.drawable.BorderDrawable;

public class BorderedLayout extends FrameLayout {

    private BorderDrawable borderDrawable;

    public BorderedLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (borderDrawable == null)
            borderDrawable = new BorderDrawable();

        if (attrs != null) {
            TypedArray a = getResources().obtainAttributes(attrs, R.styleable.BorderedLayout);

            int width, color;

            width = (int) a.getDimension(R.styleable.BorderedLayout_leftBorderWidth, 0);
            color = a.getColor(R.styleable.BorderedLayout_leftBorderColor, Color.BLACK);
            borderDrawable.setLeftBorder(width, color);

            width = (int) a.getDimension(R.styleable.BorderedLayout_topBorderWidth, 0);
            color = a.getColor(R.styleable.BorderedLayout_topBorderColor, Color.BLACK);
            borderDrawable.setTopBorder(width, color);

            width = (int) a.getDimension(R.styleable.BorderedLayout_rightBorderWidth, 0);
            color = a.getColor(R.styleable.BorderedLayout_rightBorderColor, Color.BLACK);
            borderDrawable.setRightBorder(width, color);

            width = (int) a.getDimension(R.styleable.BorderedLayout_bottomBorderWidth, 0);
            color = a.getColor(R.styleable.BorderedLayout_bottomBorderColor, Color.BLACK);
            borderDrawable.setBottomBorder(width, color);

            borderDrawable.setFillBackground(a.getColor(R.styleable.BorderedLayout_fillBackground, Color.WHITE));
        }

        if (getBackground() != null) {
            borderDrawable.setBackground(borderDrawable);
        }

        setBackground(borderDrawable);
    }

    @Override
    public void setBackground(Drawable d) {
        if (d == borderDrawable)
            super.setBackground(d);
        else {
            if (borderDrawable == null) borderDrawable = new BorderDrawable();
            borderDrawable.setBackground(d);
        }
    }

    public BorderedLayout(Context context) {
        this(context, null);
    }
}
