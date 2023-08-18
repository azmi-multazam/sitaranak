package com.zam.sidik_padang.util.customclasses;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.zam.sidik_padang.R;


public class PageIndicator extends View implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private int indicatorCounts = 0;
    private int position = 0;
    private float[] pointXs;
    private Paint paintFill, paintBackground;
    private float circleRadius = 0;
    private float density;


    public PageIndicator(Context context, AttributeSet attr) {
        super(context, attr);
        density = getResources().getDisplayMetrics().density;
        paintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintFill.setStyle(Paint.Style.FILL);
        paintBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBackground.setStyle(Paint.Style.FILL);
//		paintBackground.setStrokeWidth(density);
        TypedArray a = context.obtainStyledAttributes(attr, R.styleable.PageIndicator);
        int color = a.getColor(R.styleable.PageIndicator_indicatorColor, Color.BLUE);
        a.recycle();
        paintFill.setColor(color);
        paintBackground.setColor(Color.WHITE);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO: Implement this method
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        calculate();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // TODO: Implement this method
        super.onSizeChanged(w, h, oldw, oldh);
        calculate();
    }

    private void calculate() {
        int w = getWidth(), h = getHeight();
        if (w == 0 || h == 0 || mViewPager == null || mViewPager.getAdapter() == null || mViewPager.getAdapter().getCount() == 0)
            return;
        int availibleX = w - getPaddingLeft() - getPaddingRight();
        indicatorCounts = mViewPager.getAdapter().getCount();
        float circleRadiusWidthBased = availibleX / indicatorCounts / 2;
        float circleRadiusHeghtBased = (getHeight() - getPaddingTop() - getPaddingBottom()) / 2;
        circleRadius = Math.min(circleRadiusWidthBased, circleRadiusHeghtBased);
        float startX = w / 2 - (circleRadius * 2 * indicatorCounts / 2) - circleRadius;
        pointXs = new float[indicatorCounts];
        for (int i = 0; i < indicatorCounts; i++) {
            pointXs[i] = startX + 2 * circleRadius;
            startX = pointXs[i];
        }
        circleRadius -= 2 * density;
        invalidate();
    }

    public void setViewPager(ViewPager mViewPager) {
        this.mViewPager = mViewPager;
        mViewPager.addOnPageChangeListener(this);
        calculate();
    }

    @Override
    public void onPageScrolled(int p1, float p2, int p3) {
        // TODO: Implement this method
    }

    @Override
    public void onPageSelected(int p1) {
        position = p1;
        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int p1) {
        // TODO: Implement this method
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int w = getWidth(), h = getHeight();
        if (w == 0 || h == 0 || mViewPager == null || circleRadius == 0) return;
        debug("onDraw. w:" + w + " h:" + h + " radius:" + circleRadius + " indicatorCounts:" + position + "/" + indicatorCounts);
        for (int i = 0; i < pointXs.length; i++) {
            canvas.drawCircle(pointXs[i], h / 2, circleRadius, paintBackground);
            if (i == position) canvas.drawCircle(pointXs[i], h / 2, circleRadius, paintFill);
        }


    }

    void debug(String s) {
        Log.e(getClass().getName(), s);
    }
}
