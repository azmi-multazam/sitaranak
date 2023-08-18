package com.zam.sidik_padang.util.customclasses;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.zam.sidik_padang.R;

/**
 * Created by supriyadi on 10/7/16.
 */

public class ImageViewBulat extends AppCompatImageView {
    protected int lebar = 0, tinggi = 0;
    private Path path;
    private Paint paintLingkaran;
    private float garisLingkaran;
    private int warnaBorder = Color.WHITE, warnaBg = Color.TRANSPARENT;

    public ImageViewBulat(Context context, AttributeSet attr) {
        super(context, attr, 0);
        setUp(context, attr);

    }

//	public ImageViewBulat(Context context) {
//		super(context);
//		setUp();
//	}

    public void setWarnaBorder(int warnaBorder) {
        this.warnaBorder = warnaBorder;
        paintLingkaran.setColor(warnaBorder);
        invalidate();
    }

    protected void setUp(Context context, AttributeSet attr) {
        setWillNotDraw(false);
        path = new Path();
        TypedArray a = context.obtainStyledAttributes(attr, R.styleable.ImageViewBulat);

        warnaBorder = a.getColor(R.styleable.ImageViewBulat_border_warna, Color.WHITE);
        garisLingkaran = a.getDimension(R.styleable.ImageViewBulat_border_ring, 1);
        paintLingkaran = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintLingkaran.setColor(warnaBorder);
        paintLingkaran.setStyle(Paint.Style.STROKE);
        paintLingkaran.setStrokeWidth(garisLingkaran);
        warnaBg = ContextCompat.getColor(context, R.color.primaryLight);
        a.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (lebar == 0 || tinggi == 0) {
            lebar = getWidth();
            tinggi = getHeight();
        }

        if (lebar != 0 && tinggi != 0) {
            int radius = Math.min(lebar, tinggi) / 2 - 1;
            int setengaLebar = lebar / 2, setengahTinggi = tinggi / 2;
            try {
                path.reset();
                path.addCircle(setengaLebar, setengahTinggi, radius, Path.Direction.CW);
                canvas.clipPath(path);
            } catch (Exception e) {
                Log.e(this.getClass().getName(), "canvas ora support clip path");
                e.printStackTrace();
            }
            canvas.drawColor(warnaBg);
            super.onDraw(canvas);
            canvas.drawCircle(setengaLebar, setengahTinggi, radius - garisLingkaran / 2, paintLingkaran);

        } else super.onDraw(canvas);

    }
}
