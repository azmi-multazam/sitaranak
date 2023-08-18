package com.zam.sidik_padang.util.customclasses;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by supriyadi on 5/6/17.
 */

public class GridRecyclerView extends RecyclerView {

    private GridLayoutManager layoutManager;
    private float density = 1;

    public GridRecyclerView(Context c, AttributeSet a) {
        super(c, a);
        layoutManager = new GridLayoutManager(c, 2);
        setLayoutManager(layoutManager);
        density = c.getResources().getDisplayMetrics().density;
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        layoutManager.setSpanCount(Math.max(2, (int) (getMeasuredWidth() / (150 * density))));
    }
}
