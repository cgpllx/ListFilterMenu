package cc.easyandroid.listfiltermenu.core;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.PopupWindow;


public class AnimatorPopup extends PopupWindow {
    private Animator animator_Show;
    private Animator animator_Dismiss;

    public AnimatorPopup(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
        setAnimationStyle(0);
        creatAnimator(contentView);
    }


    public void showAsDropDown(View anchor, int xoff, int yoff) {
        super.showAsDropDown(anchor, xoff, yoff);
         getContentView().getRootView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                System.out.println("onKey  1=" + keyCode);
                return false;
            }
        });
        if (animator_Show != null) {
            animator_Show.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    if (animator_Dismiss != null) {
                        animator_Dismiss.cancel();
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {
//                    AnimatorPopup.super.dismiss();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
//                    AnimatorPopup.super.dismiss();
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
//                    AnimatorPopup.super.dismiss();
                }
            });
            animator_Show.start();
        }
    }

    private void creatAnimator(View contentView) {
        int[] location = new int[2];
        int screenHeight = contentView.getResources().getDisplayMetrics().heightPixels;
        int rootViewHeight = screenHeight - location[1];
        int paddingBottom = contentView.getPaddingBottom();
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        animator_Show = createShowAnimator(contentView, -rootViewHeight + paddingBottom - 5);
        animator_Dismiss = createDismissAnimator(contentView, -rootViewHeight + paddingBottom - 5);

    }

    private Animator createShowAnimator(final View together, int translationValue) {
        int duration = together.getResources().getInteger(android.R.integer.config_shortAnimTime);
        ObjectAnimator animShow = ObjectAnimator.ofFloat(getContentView(), View.TRANSLATION_Y, translationValue, 0, 0).setDuration(duration);
        animShow.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedFraction = animation.getAnimatedFraction();
                int h = (int) (100 * animatedFraction);
                together.getRootView().setBackgroundColor(Color.argb(h, 0, 0, 0));
            }
        });
        animShow.setInterpolator(new LinearInterpolator());
        return animShow;
    }


    private ObjectAnimator createDismissAnimator(View together, int translationValue) {
        int duration = together.getResources().getInteger(android.R.integer.config_shortAnimTime);
        ObjectAnimator animShow = ObjectAnimator.ofFloat(getContentView(), View.TRANSLATION_Y, 0, translationValue).setDuration(150);
        animShow.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedFraction = animation.getAnimatedFraction();
                int h = (int) (100 - 100 * animatedFraction);
                getContentView().getRootView().setBackgroundColor(Color.argb(h, 0, 0, 0));
            }
        });
        animShow.setInterpolator(new LinearInterpolator());
        return animShow;
    }


    /**
     * 这里是重点：两次调用dismiss，如果直接使用super方法是没有办法显示动画的，
     * 所以这里的做法是，通过一个boolean变量进行控制，第一次的dismiss的时候先显示动画，
     * 动画结束后，再调用自身的dismiss方法，将整个window消失掉
     */
    @Override
    public void dismiss() {

        if (animator_Dismiss == null || animator_Dismiss.isRunning()) {
            return;
        }
        animator_Dismiss.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (animator_Show != null && animator_Show.isRunning()) {
                    animator_Show.cancel();
                }
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
        animator_Dismiss.start();
    }
}
