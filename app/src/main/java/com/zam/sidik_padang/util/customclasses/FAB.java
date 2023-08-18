package com.zam.sidik_padang.util.customclasses;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Created by supriyadi on 9/10/17.
 */

public class FAB extends FloatingActionButton {

    public FAB(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);
    }

    public FAB(Context context, AttributeSet attributeSet, int defaultStyle) {
        super(context, attributeSet, defaultStyle);
    }

    @Override
    public void hide() {
        super.hide(new OnVisibilityChangedListener() {
            @Override
            public void onHidden(FloatingActionButton fab) {
                super.onHidden(fab);
                setVisibility(INVISIBLE);
            }
        });
    }
}
