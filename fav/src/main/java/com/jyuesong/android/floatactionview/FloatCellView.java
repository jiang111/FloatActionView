package com.jyuesong.android.floatactionview;

import android.content.Context;
import android.graphics.Color;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;


public class FloatCellView extends FrameLayout {

    public static int IMAGE_SIZE = 45;
    public static int TEXT_SIZE = 16;
    public static int TEXT_COLOR = Color.parseColor("#000000");
    private TextView tip;
    private ImageView icon;
    private int padding;

    public FloatCellView(@NonNull Context context, String tip, int redID) {
        super(context);
        init(tip, redID);
    }

    private void init(String tip, int redID) {
        padding = dip2px(getContext(), 15);
        this.tip = new TextView(getContext());
        this.tip.setLayoutParams(FloatActiongViewUtils.generateLayoutParam(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        this.tip.setTextSize(TEXT_SIZE);
        this.tip.setText(tip);
        this.tip.setTextColor(TEXT_COLOR);
        icon = new ImageView(getContext());
        icon.setLayoutParams(FloatActiongViewUtils.generateLayoutParam(dip2px(getContext(), IMAGE_SIZE), dip2px(getContext(), IMAGE_SIZE)));
        icon.setImageResource(redID);
        addView(this.tip);
        addView(icon);
        setProgress(0);

    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int icon_left = getWidth() - padding - icon.getWidth();
        icon.layout(icon_left, getHeight() / 2 - icon.getHeight() / 2, getWidth() - padding, getHeight() / 2 + icon.getHeight() / 2);
        tip.layout(icon_left - padding - tip.getWidth(), getHeight() / 2 - tip.getHeight() / 2, icon_left - padding, getHeight() / 2 + tip.getHeight() / 2);

    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public void setProgress(float progress) {
        this.currentProgress = progress;

        if (currentProgress <= 0.2) {
            tip.setVisibility(GONE);
            icon.setVisibility(GONE);
        } else {
            tip.setVisibility(VISIBLE);
            icon.setVisibility(VISIBLE);
            float result = (float) (currentProgress - 0.2);
            if (currentProgress >= 1) {
                result = 1;
            }
            setAlpha(result < 0 ? 0 : result);

            icon.setScaleX(progress);
            icon.setScaleY(progress);
        }

    }

    private float currentProgress;
}
