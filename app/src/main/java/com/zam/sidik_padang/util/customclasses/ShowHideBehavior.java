package com.zam.sidik_padang.util.customclasses;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

/**
 * Created by supriyadi on 5/9/17.
 */

public class ShowHideBehavior extends CoordinatorLayout.Behavior {
    public ShowHideBehavior() {
        super();
    }

    public ShowHideBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        debug("dependency  is " + dependency.getClass().getName());
        return dependency instanceof FAB;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        debug("child is textView =" + (child instanceof TextView));
        child.setVisibility(dependency.getVisibility() == View.VISIBLE ? View.VISIBLE : View.INVISIBLE);
        return true;
    }

    void debug(String s) {
//		Log.e(getClass().getName(), s);
    }

}
