package com.zam.sidik_padang.util.customclasses;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.core.content.res.ResourcesCompat;

import com.zam.sidik_padang.R;

/**
 * Created by supriyadi on 3/17/17.
 */

public class BottomShadowFrameLayout extends FrameLayout {

    private final Paint shadowPaint, bgPaint;
    private int shadowHeight = 0;
    private int shadowColor;
    private int bgColor = Color.WHITE;


    public BottomShadowFrameLayout(Context c, AttributeSet a) {
        super(c, a);
        setWillNotDraw(false);
        TypedArray ta = c.obtainStyledAttributes(a, R.styleable.BottomShadowFrameLayout);
        shadowHeight = ta.getDimensionPixelSize(R.styleable.BottomShadowFrameLayout_bottomShadow,
                (int) (c.getResources().getDisplayMetrics().density * 4f));
        shadowColor = ta.getColor(R.styleable.BottomShadowFrameLayout_shadowColor,
                ResourcesCompat.getColor(getResources(), R.color.cardview_shadow_start_color, c.getTheme()));
        bgColor = ta.getColor(R.styleable.BottomShadowFrameLayout_bgColor, Color.WHITE);
        ta.recycle();
        shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        bgPaint = new Paint();
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(bgColor);
//		setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        calculateShadow();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculateShadow();
    }

    private void calculateShadow() {
        if (shadowHeight == 0) return;
        shadowPaint.setShader(new LinearGradient(0, getHeight() - shadowHeight, 0, getHeight(), shadowColor, Color.TRANSPARENT, Shader.TileMode.CLAMP));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (shadowHeight > 0) {
            canvas.drawRect(0, getHeight() - shadowHeight, getWidth(), getHeight(), shadowPaint);
        }
        canvas.drawRect(0, 0, getWidth(), getHeight() - shadowHeight, bgPaint);
        super.onDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        setChildMarginBottom();
        super.onLayout(changed, left, top, right, bottom);
    }

    private void setChildMarginBottom() {
        int childCount = getChildCount();
        if (childCount == 0) return;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            LayoutParams params = (LayoutParams) child.getLayoutParams();
            params.bottomMargin = shadowHeight;
            updateViewLayout(child, params);
        }
    }
}
