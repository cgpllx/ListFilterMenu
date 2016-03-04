package cc.easyandroid.listfiltermenu.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import cc.easyandroid.listfiltermenu.R;
import cc.easyandroid.listfiltermenu.core.EasyPopMenuAdapter;
import cc.easyandroid.listfiltermenu.core.IEasySuperItem;

/**
 *
 */
public class EasyPopMenu extends TextView implements View.OnClickListener {
    ListPopupWindow listPopupWindow;
    EasyPopMenuAdapter<IEasySuperItem> adapter;
    private CharSequence defultMenuText;
    public static final String EASYID_NOFILTER = "Easy_noFilter";

    private View mDropDownAnchorView;

    public EasyPopMenu(Context context) {
        this(context, null);
    }

    public EasyPopMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EasyPopMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EasyPopMenu, defStyle, 0);
        defultMenuText = "";
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.EasyPopMenu_text) {
                defultMenuText = a.getText(attr);
            }
        }
        a.recycle();
        setOnClickListener(this);

        listPopupWindow = new ListPopupWindow(context);
//        listPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.school_list_bg));
//            listPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        listPopupWindow.setWidth(300);
        listPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        listPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setSelected(false);
            }
        });
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IEasySuperItem iEasySuperItem = adapter.getItem(position);
                if (menuListItemClickListener != null) {
                    menuListItemClickListener.onClick(position, iEasySuperItem);
                }
                changMenuText(iEasySuperItem);
            }
        });
//        adapter = new EasyPopMenuAdapter<IEasySuperItem>();
        if (adapter != null) {
            listPopupWindow.setAdapter(adapter);
//            listPopupWindow.getListView().setItemChecked(0, true);
        }
    }

    /**
     * add item
     *
     * @param show            是否马上显示窗口
     * @param iEasySuperItems 数据
     */
    public void addItems(boolean show, List<? extends IEasySuperItem> iEasySuperItems) {
        adapter.addAll(iEasySuperItems);
        if (show && iEasySuperItems != null && iEasySuperItems.size() > 0) {
            show();
        }
    }

    public void setEasyPopMenuAdapter(EasyPopMenuAdapter adapter) {
        this.adapter = adapter;
        if (listPopupWindow != null) {
            listPopupWindow.setAdapter(this.adapter);
        }
    }

    /**
     * set item
     *
     * @param iEasySuperItems 数据
     */
    public void setItems(List<? extends IEasySuperItem> iEasySuperItems) {
        adapter.setmItems(iEasySuperItems);
        if (iEasySuperItems != null && iEasySuperItems.size() > 0) {
            show();
        }
    }

    public void changMenuText(IEasySuperItem iEasySuperItem) {
        dismiss();
        if (iEasySuperItem != null) {
            String displayName = iEasySuperItem.getDisplayName();
            String easyId = iEasySuperItem.getEasyId();
            if (TextUtils.isEmpty(easyId) || EASYID_NOFILTER.equals(easyId)) {
                setText(defultMenuText);
            } else {
                if (!TextUtils.isEmpty(displayName)) {
                    setText(displayName);
                }
            }
        }
    }

    OnMenuListItemClickListener menuListItemClickListener;

    public void setOnMenuListItemClickListener(OnMenuListItemClickListener menuListItemClickListener) {
        this.menuListItemClickListener = menuListItemClickListener;
    }

    public interface OnMenuListItemClickListener<T extends IEasySuperItem> {
        void onClick(int position, T t);
    }

    public void show() {
        if (listPopupWindow != null && !listPopupWindow.isShowing()) {

            setSelected(true);
            listPopupWindow.setModal(true);
            listPopupWindow.setAnchorView(this);
//            listPopupWindow.setPromptView(new TextView(getContext()));
//            listPopupWindow.setDropDownGravity(Gravity.BOTTOM|Gravity.START);
//            listPopupWindow.  setVerticalOffset(-100);
//            listPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.school_list_bg));
//            listPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//            listPopupWindow.setWidth(300);
//            listPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//
            listPopupWindow.show();
//            listPopupWindow.set
//            listPopupWindow.getListView().setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
//            listPopupWindow.getListView().setBackgroundDrawable(getResources().getDrawable(R.drawable.school_list_bg));

//            listPopupWindow.show();
//            listPopupWindow.getListView().setItemChecked(0, true);
//            listPopupWindow.get
        }
    }

    public void setPromptView(View promptView) {
        if (listPopupWindow != null) {
            listPopupWindow.setPromptView(promptView);
        }
    }

    public void dismiss() {
        if (listPopupWindow != null && listPopupWindow.isShowing()) {
            listPopupWindow.dismiss();
            setSelected(false);
        }
    }

    public void setPopBackgroundDrawable(Drawable drawable) {

        listPopupWindow.setBackgroundDrawable(drawable);
//        listPopupWindow.setPromptView(null);
    }

    /**
     * Sets the popup's anchor view. This popup will always be positioned relative to
     * the anchor view when shown.
     *
     * @param anchor The view to use as an anchor.
     */
    public void setAnchorView(View anchor) {
        mDropDownAnchorView = anchor;
    }

    @Override
    public void onClick(View v) {
        if (listPopupWindow.isShowing()) {
            dismiss();
        } else {
            show();
        }
    }
}
