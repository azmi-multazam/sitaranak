package com.zam.sidik_padang.roodiskusi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.zam.sidik_padang.R;

public class CircleImageView extends AppCompatImageView {

    private Paint paint;
    private Path path;

    public CircleImageView(Context context) {
        super(context);
        inisialisasi();
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inisialisasi();
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inisialisasi();
    }

    private void inisialisasi() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(getResources().getDisplayMetrics().density);
        paint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        path = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        inisialisasi();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int w = getWidth(), h = getHeight();
        if (w > 0 && h > 0) {
            canvas.save();
            float r = Math.min(w / 2, h / 2) - 3;
            path.reset();
            path.addCircle(w / 2, h / 2, r, Path.Direction.CW);
            canvas.clipPath(path);
            super.onDraw(canvas);
            canvas.restore();
            canvas.drawCircle(w / 2, h / 2, r, paint);

        } else super.onDraw(canvas);

    }
}
