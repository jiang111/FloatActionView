package com.jyuesong.android.floatactionview;

import android.content.Context;
import android.view.ViewGroup;

public class FloatActiongViewUtils {


    public static ViewGroup.LayoutParams generateLayoutParam(int w, int h) {
        return new ViewGroup.LayoutParams(w, h);
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
