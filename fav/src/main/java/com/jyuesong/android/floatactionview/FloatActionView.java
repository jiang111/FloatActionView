package com.jyuesong.android.floatactionview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class FloatActionView extends FrameLayout {
    public static int IMAGE_SIZE = 50;
    public static int ANIM_DELAY_TIME = 50;
    public static int AMINATION_DURATION = 200;
    public static boolean MAIN_ROTATEABLE = true;
    public static final int NORMAL = 1;
    public static final int EXPANDING = 2;
    public static final int EXPANDED = 3;

    private OnClick onClick;

    private ImageView mainButton;

    private Paint mBGPaint;
    private BlurringView mBGView;

    private int state = NORMAL;

    private AnimatorSet animatorSet = new AnimatorSet();
    private ValueAnimator blurAnimator;

    private List<ObjectAnimator> menuAnimators = new ArrayList<>();

    private boolean setItemClickDismiss = true;


    private List<FloatCellView> floatCellViewList = new ArrayList<>();
    private int paddingVertical;
    private int padding;

    public int getState() {
        return state;
    }

    public FloatActionView(Context context) {
        this(context, null);
    }

    public FloatActionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatActionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initBlurAnimation();

        padding = FloatActiongViewUtils.dip2px(getContext(), 15);
        paddingVertical = FloatActiongViewUtils.dip2px(getContext(), 30);

        initBGView();
        intiBGPaint();
        initMainButton();

    }


    private void initBlurAnimation() {
        blurAnimator = new ObjectAnimator();
        blurAnimator.setInterpolator(new DecelerateInterpolator());
        blurAnimator.setFloatValues(0f, 1);
        blurAnimator.setDuration(AMINATION_DURATION);
        blurAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float blur_progress = (float) animation.getAnimatedValue();

                if (blur_progress == 0) {
                    state = NORMAL;
                } else if (blur_progress < 1) {
                    state = EXPANDING;
                } else {
                    state = EXPANDED;
                }
                mBGView.setProgress(blur_progress);
                if (MAIN_ROTATEABLE) {
                    mainButton.setRotation(blur_progress * 90);
                }
            }
        });
    }

    private void initBGView() {
        mBGView = new BlurringView(getContext());
        mBGView.setLayoutParams(FloatActiongViewUtils.generateLayoutParam(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(mBGView);
        mBGView.setClickable(true);
        mBGView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state == EXPANDED) {
                    animatMenu(false);
                }
            }
        });

    }


    private void intiBGPaint() {
        mBGPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBGPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    private View findBlurView() {
        if (getParent() instanceof ViewGroup) {

            ViewGroup viewGroup = (ViewGroup) getParent();
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                if (viewGroup.getChildAt(i) instanceof FloatActionView) continue;
                if (viewGroup.getChildAt(i).getLayoutParams().width == ViewGroup.LayoutParams.MATCH_PARENT ||
                        viewGroup.getChildAt(i).getLayoutParams().height == ViewGroup.LayoutParams.MATCH_PARENT
                ) {
                    return viewGroup.getChildAt(i);
                }
            }

        }
        return null;
    }

    private void initMainButton() {
        mainButton = new ImageView(getContext());
        mainButton.setLayoutParams(FloatActiongViewUtils.generateLayoutParam(FloatActiongViewUtils.dip2px(getContext(), IMAGE_SIZE), FloatActiongViewUtils.dip2px(getContext(), IMAGE_SIZE)));
        addView(mainButton);
        mainButton.setClickable(true);
        mainButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (state == EXPANDING) {
                    return;
                }
                if (state == EXPANDED) {
                    animatMenu(false);
                } else {
                    animatMenu(true);
                }

                if (onClick != null) {
                    onClick.mainClicked();
                }

            }
        });

    }

    public void animatMenu(boolean expand) {
        if (state == EXPANDING) return;
        if (mBGView.getBlurredView() == null) {
            mBGView.setBlurredView(findBlurView());
        }

        if (expand) {
            blurAnimator.setInterpolator(null);
            blurAnimator.setStartDelay(0);
            AnimatorSet.Builder builder = animatorSet.play(blurAnimator);
            for (int i = 0; i < menuAnimators.size(); i++) {
                menuAnimators.get(i).setStartDelay(ANIM_DELAY_TIME * i);
                menuAnimators.get(i).setInterpolator(new OvershootInterpolator());
                builder.with(menuAnimators.get(i));
            }
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);
                }
            });
            animatorSet.removeAllListeners();
            animatorSet.start();
        } else {
            blurAnimator.setInterpolator(new ReverseInterpolator());
            blurAnimator.setStartDelay(menuAnimators.size() / 2 * ANIM_DELAY_TIME);
            AnimatorSet.Builder builder = animatorSet.play(blurAnimator);

            for (int i = 0; i < menuAnimators.size(); i++) {
                menuAnimators.get(i).setStartDelay(ANIM_DELAY_TIME * (menuAnimators.size() - i));
                menuAnimators.get(i).setInterpolator(new ReverseInterpolator());
                builder.with(menuAnimators.get(i));
            }
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);
                    if (onClick != null) {
                        onClick.dismissed();
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (onClick != null) {
                        onClick.dismissed();
                    }
                }
            });
            animatorSet.start();
        }

    }

    public void setMainButtonIcon(int resID) {
        mainButton.setImageResource(resID);
    }

    public void setMainButtonIcon(Drawable drawable) {
        mainButton.setImageDrawable(drawable);
    }

    public void setSetItemClickDismiss(boolean clickDismiss) {
        this.setItemClickDismiss = clickDismiss;
    }

    public void setData(List<String> tips, List<Integer> resID) {
        floatCellViewList.clear();
        menuAnimators.clear();
        if (tips.size() != resID.size()) {
            throw new IllegalArgumentException("tips size not equals resID size");
        }

        for (int i = tips.size() - 1; i >= 0; i--) {

            final FloatCellView floatCellView = new FloatCellView(getContext(), tips.get(i), resID.get(i));
            floatCellView.setLayoutParams(FloatActiongViewUtils.generateLayoutParam(LayoutParams.MATCH_PARENT, FloatActiongViewUtils.dip2px(getContext(), 60)));
            floatCellViewList.add(floatCellView);
            addView(floatCellView);
            floatCellView.setClickable(true);
            final int finalI = i;
            floatCellView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (state != EXPANDED) return;
                    if (setItemClickDismiss) {
                        animatMenu(false);
                    }
                    if (onClick != null) {
                        onClick.positionClicked(finalI);
                    }
                }
            });

            ObjectAnimator objectAnimator = new ObjectAnimator();
            objectAnimator.setDuration(AMINATION_DURATION);
            objectAnimator.setFloatValues(0f, 1);
            objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float menuProgress = (float) animation.getAnimatedValue();
                    floatCellView.setProgress(menuProgress);
                }
            });
            menuAnimators.add(objectAnimator);


        }
    }

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        mBGView.layout(left, top, right, bottom);
        mainButton.layout(getWidth() - mainButton.getLayoutParams().width - padding, getHeight() - paddingVertical - mainButton.getLayoutParams().height, getWidth() - padding, getHeight() - paddingVertical);

        int cellTop = getHeight() - mainButton.getLayoutParams().height - paddingVertical - padding;

        for (int i = 0; i < floatCellViewList.size(); i++) {
            View view = floatCellViewList.get(i);
            view.layout(0, cellTop - view.getMeasuredHeight(), getWidth(), cellTop);
            cellTop = cellTop - padding - view.getMeasuredHeight();


        }
    }


    public interface OnClick {
        void positionClicked(int position);

        void mainClicked();

        void dismissed();
    }

    public class ReverseInterpolator implements Interpolator {
        @Override
        public float getInterpolation(float paramFloat) {
            return Math.abs(paramFloat - 1f);
        }
    }


    public static class Builder {

        public Builder menuTextSize(int dpSize) {
            FloatCellView.TEXT_SIZE = dpSize;
            return Builder.this;
        }

        public Builder menuTextColor(int color) {
            FloatCellView.TEXT_COLOR = color;
            return Builder.this;
        }

        public Builder menuImageSize(int imageSize) {
            FloatCellView.IMAGE_SIZE = imageSize;
            return Builder.this;
        }

        public Builder mainImageSize(int imageSize) {
            FloatActionView.IMAGE_SIZE = imageSize;
            return Builder.this;
        }

        public Builder animatDuration(int duration) {
            FloatActionView.AMINATION_DURATION = duration;
            return Builder.this;
        }

        public Builder animatDelay(int delay) {
            FloatActionView.ANIM_DELAY_TIME = delay;
            return Builder.this;
        }

        public Builder mainRotateAble(boolean rotatable) {
            FloatActionView.MAIN_ROTATEABLE = rotatable;
            return Builder.this;
        }

        public Builder blurRadius(int radius) {
            BlurringView.RADIUS = radius;
            return this;
        }

        public Builder blurFactor(int factor) {
            BlurringView.FACTOR = factor;
            return this;
        }

        public void build() {

        }
    }

    public void dismiss() {
        if (state == EXPANDED) {
            animatMenu(false);
        }
    }

}
