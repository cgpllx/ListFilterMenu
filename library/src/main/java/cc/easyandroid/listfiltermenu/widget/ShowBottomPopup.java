package cc.easyandroid.listfiltermenu.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.View;
import android.widget.PopupWindow;


public class ShowBottomPopup extends PopupWindow {
    private ObjectAnimator animShow;
    private ObjectAnimator animDismiss;

    public ShowBottomPopup(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
    }

    public void showAsDropDown(View anchor ) {
        super.showAsDropDown(anchor);
        int height = anchor.getMeasuredHeight();
        int[] location = new int[2];
        int screenHeight=getContentView().getResources().getDisplayMetrics().heightPixels;
        int rootViewHeight = screenHeight - location[1] - height;
        final View rootView = getContentView();
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        animShow = ObjectAnimator.ofFloat(rootView, View.TRANSLATION_Y, height - rootViewHeight, 0, 0, 0).setDuration(500);
        animShow.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedFraction = animation.getAnimatedFraction();
                int h = (int) (77 * animatedFraction);
                rootView.setBackgroundColor(Color.argb(h, 0, 0, 0));
            }
        });
        animDismiss = ObjectAnimator.ofFloat(rootView, View.TRANSLATION_Y, 0, -rootViewHeight).setDuration(200);
        animShow.start();
    }

    /**
     * 这里是重点：两次调用dismiss，如果直接使用super方法是没有办法显示动画的，
     * 所以这里的做法是，通过一个boolean变量进行控制，第一次的dismiss的时候先显示动画，
     * 动画结束后，再调用自身的dismiss方法，将整个window消失掉
     */
    @Override
    public void dismiss() {
        animDismiss.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

                if (animShow != null && animShow.isRunning()) {
                    animShow.cancel();
                }
                getContentView().setBackgroundColor(Color.TRANSPARENT);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ShowBottomPopup.super.dismiss();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                ShowBottomPopup.super.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                ShowBottomPopup.super.dismiss();
            }
        });
        animDismiss.start();
    }
}
