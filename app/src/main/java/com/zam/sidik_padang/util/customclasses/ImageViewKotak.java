package com.zam.sidik_padang.util.customclasses;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class ImageViewKotak extends AppCompatImageView {
    public ImageViewKotak(Context context, AttributeSet attr) {
        super(context, attr, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO: Implement this method
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

}
