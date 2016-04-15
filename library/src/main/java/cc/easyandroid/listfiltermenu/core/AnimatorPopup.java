package cc.easyandroid.listfiltermenu.core;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.PopupWindow;


public class AnimatorPopup extends PopupWindow {
    private Animator animator_Show;
    private ObjectAnimator animator_Dismiss;

//    public AnimatorPopup(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }

    public AnimatorPopup(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
        setBackgroundDrawable(new ColorDrawable());
//        setAnimationStyle(R.style.mypopwindow_anim_style);
//        setWindowLayoutType(WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY);
//        setEnterTransition(null);
//        set
//        set
//        getContentView().getContext().get
    }

    public void showAsDropDown(View anchor, int xoff, int yoff) {
        super.showAsDropDown(anchor, xoff, yoff);
        if (animator_Show == null) {
            animator_Show = creatAnimator(anchor, xoff, yoff);
        }
        getContentView().startAnimation(createTranslationInAnimation());
//        animator_Show.start();
    }

    private Animator creatAnimator(View anchor, int xoff, int yoff) {
        int height = anchor.getMeasuredHeight();
        int[] location = new int[2];
        int screenHeight = getContentView().getResources().getDisplayMetrics().heightPixels;
        int rootViewHeight = screenHeight - location[1] - height;
        int paddingBottom = getContentView().getPaddingBottom();
        View rootView = getContentView();
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        animator_Show = createShowAnimator(rootView, height - rootViewHeight + paddingBottom - 20);
        animator_Dismiss = createDismissAnimator(rootView, height - rootViewHeight + paddingBottom - 20);

        return animator_Show;
    }

    private Animator createShowAnimator(View together, int translationValue) {
        int duration = getContentView().getResources().getInteger(android.R.integer.config_shortAnimTime);
        ObjectAnimator animShow = ObjectAnimator.ofFloat(getContentView(), View.TRANSLATION_Y, translationValue, 0, 0).setDuration(1000);
//        animShow.
//        ObjectAnimator animShow = ObjectAnimator.ofFloat(getContentView().getRootView(), View.TRANSLATION_Y, -getContentView().getResources().getDisplayMetrics().heightPixels, 0,0).setDuration(500);
//        ObjectAnimator animShow = getContentView().getRootView().animate().translationY(translationValue);
        ValueAnimator animator_color = ValueAnimator.ofInt(0).setDuration(duration);
        animator_color.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedFraction = animation.getAnimatedFraction();
                int h = (int) (77 * animatedFraction);
                getContentView().getRootView().setBackgroundColor(Color.argb(h, 0, 0, 0));
            }
        });
//        animator_color.setStartDelay();
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animator_color).after(animShow);
        animatorSet.setInterpolator(new LinearInterpolator());
        return animatorSet;
    }


    private ObjectAnimator createDismissAnimator(View together, int translationValue) {
        int duration = together.getResources().getInteger(android.R.integer.config_shortAnimTime);
        ObjectAnimator animShow = ObjectAnimator.ofFloat(getContentView(), View.TRANSLATION_Y, 0, translationValue).setDuration(duration);
        animShow.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedFraction = animation.getAnimatedFraction();
                int h = (int) (77 - 77 * animatedFraction);
//                getContentView().getRootView().setBackgroundColor(Color.argb(h, 0, 0, 0));
            }
        });
        animShow.setInterpolator(new LinearInterpolator());
        return animShow;
    }

    private Animation createTranslationInAnimation() {
        int type = Animation.RELATIVE_TO_SELF;
//        TranslateAnimation(int fromXType, float fromXValue, int toXType, float toXValue,
//        int fromYType, float fromYValue, int toYType, float toYValue)
        TranslateAnimation an = new TranslateAnimation(type, 0, type, 0, type,
                -1, type, 0);
        an.setDuration(200);
//        an.setStartOffset(100);
        an.setFillAfter(true);

        return an;
    }

    private Animation createTranslationOutAnimation() {
        int type = Animation.RELATIVE_TO_SELF;
        TranslateAnimation an = new TranslateAnimation(type, 0, type, 0, type,
                0, type, -1);
        an.setDuration(200);
        an.setFillAfter(true);
        return an;
    }

    /**
     * 这里是重点：两次调用dismiss，如果直接使用super方法是没有办法显示动画的，
     * 所以这里的做法是，通过一个boolean变量进行控制，第一次的dismiss的时候先显示动画，
     * 动画结束后，再调用自身的dismiss方法，将整个window消失掉
     */
    @Override
    public void dismiss() {
        Animation animation= createTranslationOutAnimation();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AnimatorPopup.super.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

        });
//        animation.li
        getContentView().startAnimation(animation);
//        getContentView().setlis

//        if (animator_Dismiss == null) {
//            return;
//        }
//        animator_Dismiss.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                if (animator_Show != null && animator_Show.isRunning()) {
//                    animator_Show.cancel();
//                }
//                getContentView().getRootView().setBackgroundColor(Color.argb(0, 0, 0, 0));
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                AnimatorPopup.super.dismiss();
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//                AnimatorPopup.super.dismiss();
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//                AnimatorPopup.super.dismiss();
//            }
//        });
//        animator_Dismiss.start();
    }
}
