package com.zam.sidik_padang.util.customclasses;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;

//import com.zam.sidik_padang.BuildConfig;
import com.zam.sidik_padang.R;

/**
 * Created by supriyadi on 3/20/18.
 */

public class PrintPreviewLayout extends RelativeLayout {
    private boolean porttrait = true;

    private int maxHeight;

    public PrintPreviewLayout(Context context) {
        super(context);
        init(null);
    }

    public PrintPreviewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PrintPreviewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PrintPreviewLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        maxHeight = (int) (getResources().getDisplayMetrics().density * 400f);
        debug("init maxHeight: " + maxHeight);
        TypedArray ta = getContext().obtainStyledAttributes(attributeSet, R.styleable.PrintPreviewLayout);
        porttrait = ta.getInt(R.styleable.PrintPreviewLayout_pageOrientation, 1) == 1;
        ta.recycle();
        debug("Orientation isPorttrait: " + porttrait);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        debug("onSize changed w: " + w + " h: " + h);
        if (h == 0 || h == oldh) return;
        ViewGroup.LayoutParams params = getLayoutParams();
        if (porttrait) {
            if (params == null || params.height == 0) return;
            if (h > maxHeight) params.height = maxHeight;
            params.width = params.height * 50 / 76;
        } else {
            if (params == null || w <= 0) return;
            if (w > maxHeight) params.width = maxHeight;
            params.height = w * 50 / 76;
        }
        setLayoutParams(params);
        debug("param w:" + params.width + " h:" + params.height);


    }

    public void saveBitmap(String filePath, OnBitmapSavedListener listener) {
        new SaveBitmap(this, listener).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, filePath);
    }

    void debug(String s) {
        Log.d(getClass().getName(), s);
        //if (BuildConfig.DEBUG) Log.e(getClass().getName(), s);
    }

    public static interface OnBitmapSavedListener {
        void onBitmapSaved(boolean success);
    }

    private static class SaveBitmap extends AsyncTask<String, Void, Boolean> {
        private WeakReference<PrintPreviewLayout> weakReference;
        private Bitmap bitmap = null;
        private OnBitmapSavedListener listener;

        public SaveBitmap(PrintPreviewLayout layout, OnBitmapSavedListener listener) {
            this.weakReference = new WeakReference<>(layout);
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            PrintPreviewLayout layout = weakReference.get();
            if (layout == null) return;
            layout.setDrawingCacheEnabled(true);
            layout.buildDrawingCache();
            bitmap = layout.getDrawingCache();
//			layout.setDrawingCacheEnabled(false);
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            if (bitmap == null) return false;

            try {
                FileOutputStream os = new FileOutputStream(strings[0]);

                PrintPreviewLayout layout = weakReference.get();

                if (layout != null) {
                    if (layout.porttrait) {
                        Bitmap finalBitmap = Bitmap.createScaledBitmap(bitmap, 475, 722, false);
                        finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    } else {
                        Bitmap finalBitmap = Bitmap.createScaledBitmap(bitmap, 722, 475, false);
                        Bitmap bm = Bitmap.createBitmap(finalBitmap.getHeight(), finalBitmap.getWidth(), Bitmap.Config.RGB_565);
                        Canvas canvas = new Canvas(bm);
                        canvas.rotate(90, bm.getWidth() / 2, bm.getHeight() / 2);
                        canvas.drawColor(Color.BLUE);
                        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                        paint.setColor(Color.RED);
                        canvas.drawBitmap(finalBitmap, -(722 - 475) / 2, (722 - 475) / 2, paint);
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    }
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(new File(strings[0])));
                    layout.getContext().sendBroadcast(intent);
                }

                os.flush();
                os.close();
            } catch (java.io.IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            listener.onBitmapSaved(aBoolean);
        }
    }

}
