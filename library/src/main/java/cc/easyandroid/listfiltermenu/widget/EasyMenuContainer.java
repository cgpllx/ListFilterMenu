package cc.easyandroid.listfiltermenu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import cc.easyandroid.listfiltermenu.core.EasyMenuManager;
import cc.easyandroid.listfiltermenu.core.SingleSelectionMenuStates;

/**
 *
 */
public class EasyMenuContainer extends LinearLayout {
    EasyMenuManager easyMenuManager;

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
        if (childView != null && childView instanceof EasyFilterMenu) {
            easyMenuManager.addMenu(((EasyFilterMenu) childView));
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (easyMenuManager != null) {
            easyMenuManager.clear();
        }
    }

    public SparseArray<SingleSelectionMenuStates> getMenusStates() {
        SparseArray<SingleSelectionMenuStates> sparseArray=new SparseArray<>();
//        sparseArray.put();
//        ArrayMap<String, SingleSelectionMenuStates> arrayMap=new ArrayMap<>();
//        Bundle bundle=null;
//        bundle.putSparseParcelableArray();
//        return  arrayMap;
        return  null;
    }
}
