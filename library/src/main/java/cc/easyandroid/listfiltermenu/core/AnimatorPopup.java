package cc.easyandroid.listfiltermenu.core;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.PopupWindow;


public class AnimatorPopup extends PopupWindow {
    private ObjectAnimator animDismiss;
    private AnimatorSet animSet;

    public AnimatorPopup(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
    }

    public void showAsDropDown(View anchor, int xoff, int yoff) {
        super.showAsDropDown(anchor, xoff, yoff);
        if (animSet == null) {
            animSet = creatShowAnimatorSet(anchor, xoff, yoff);
        }
        animSet.start();
    }

    private AnimatorSet creatShowAnimatorSet(View anchor, int xoff, int yoff) {
        int height = anchor.getMeasuredHeight();
        int[] location = new int[2];
        int screenHeight = getContentView().getResources().getDisplayMetrics().heightPixels;

        int rootViewHeight = screenHeight - location[1] - height;
        int paddingBottom = getContentView().getPaddingBottom();
        final View rootView = getContentView();
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ObjectAnimator animShow = ObjectAnimator.ofFloat(rootView, View.TRANSLATION_Y, height - rootViewHeight + paddingBottom - 10, 0).setDuration(200);
        ValueAnimator colorChangeAnimator = ValueAnimator.ofInt(0);//
        colorChangeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedFraction = animation.getAnimatedFraction();
                int h = (int) (77 * animatedFraction);
                rootView.setBackgroundColor(Color.argb(h, 0, 0, 0));
            }
        });
        colorChangeAnimator.setDuration(200);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(colorChangeAnimator).after(animShow);
        animSet.setInterpolator(new LinearInterpolator());
        animDismiss = ObjectAnimator.ofFloat(rootView, View.TRANSLATION_Y, 0, height - rootViewHeight + paddingBottom - 10).setDuration(200);
        return animSet;
    }

    /**
     * 这里是重点：两次调用dismiss，如果直接使用super方法是没有办法显示动画的，
     * 所以这里的做法是，通过一个boolean变量进行控制，第一次的dismiss的时候先显示动画，
     * 动画结束后，再调用自身的dismiss方法，将整个window消失掉
     */
    @Override
    public void dismiss() {
        if (animDismiss == null) {
            return;
        }
        animDismiss.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (animSet != null && animSet.isRunning()) {
                    animSet.cancel();
                }
                getContentView().setBackgroundColor(Color.TRANSPARENT);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                AnimatorPopup.super.dismiss();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                AnimatorPopup.super.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                AnimatorPopup.super.dismiss();
            }
        });
        animDismiss.start();
    }
}
