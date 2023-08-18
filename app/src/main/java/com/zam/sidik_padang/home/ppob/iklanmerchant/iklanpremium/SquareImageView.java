package com.zam.sidik_padang.home.ppob.iklanmerchant.iklanpremium;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class SquareImageView extends AppCompatImageView {

    public SquareImageView(Context c, AttributeSet a) {
        super(c, a);
    }

    public SquareImageView(Context c, AttributeSet a, int i) {
        super(c, a, i);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO: Implement this method
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }


}
