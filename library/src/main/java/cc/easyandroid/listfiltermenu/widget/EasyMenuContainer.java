package cc.easyandroid.listfiltermenu.widget;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import cc.easyandroid.listfiltermenu.core.EasyMenuManager;

/**
 *
 */
public class EasyMenuContainer extends LinearLayout {
    EasyMenuManager easyMenuManager;
    FragmentManager fragmentManager;

    public EasyMenuContainer(Context context) {
        super(context);
        init();

    }

    public EasyMenuContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EasyMenuContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        easyMenuManager = new EasyMenuManager();
    }

    @Override
    public void addView(View childView, int index, ViewGroup.LayoutParams params) {
        super.addView(childView, index, params);
        if (childView != null && childView instanceof EasyListFilterMenu) {
            easyMenuManager.addMenu(((EasyListFilterMenu) childView));
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(easyMenuManager!=null){
            easyMenuManager.clear();
        }
    }
}
