package cc.easyandroid.listfiltermenu.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

import cc.easyandroid.listfiltermenu.R;
import cc.easyandroid.listfiltermenu.core.AnimatorPopup;
import cc.easyandroid.listfiltermenu.core.EasyItemManager;
import cc.easyandroid.listfiltermenu.core.IEasyItem;
import cc.easyandroid.listfiltermenu.core.OnMenuListItemClickListener;

/**
 * 下拉筛选控件
 * <pre>
 * 子类通过调用setMenuContentView 传递view
 * </pre>
 */
public abstract class EasyFilterMenu extends LinearLayout implements Runnable {
    protected CharSequence defultMenuText = "easyMenuText";
    protected PopupWindow pupupWindow;
    private int xoff = 0;//x的偏移量
    private int yoff = 0;//y的偏移量
    private TextView mTitleTextView;//现实标题的textview控件


    public EasyFilterMenu(Context context) {
        this(context, null);
    }

    public EasyFilterMenu(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.EasyFilterMenuStyle);
    }

    public EasyFilterMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        int menuTitleViewResourceId = R.layout.menu_title_layout;//title是公用模块，肯定都会有
        int menuContentLayoutResourceId = R.layout.menu_content_layout;// menu contentView  id

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EasyFilterMenu, defStyle, 0);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.EasyFilterMenu_menuTitle) {//menu title
                defultMenuText = a.getText(attr);
            } else if (attr == R.styleable.EasyFilterMenu_menuTitleLayout) {// menu title 的资源id
                menuTitleViewResourceId = a.getResourceId(attr, R.layout.menu_title_layout);
            } else if (attr == R.styleable.EasyFilterMenu_menuContentLayout) {// menucontent 的资源id
                menuContentLayoutResourceId = a.getResourceId(attr, R.layout.menu_content_layout);
            } else if (attr == R.styleable.EasyFilterMenu_menuPupupXoff) {//x的偏移量
                xoff = a.getDimensionPixelSize(attr, 0);
            } else if (attr == R.styleable.EasyFilterMenu_menuPupupYoff) {// y的偏移量
                yoff = a.getDimensionPixelSize(attr, 0);
            }
        }

        a.recycle();

        if (menuTitleViewResourceId != -1) {
            setMenuTitleViewResourceId(menuTitleViewResourceId);
        }
        if (menuContentLayoutResourceId != -1) {
            setMenuContentViewResourceId(menuContentLayoutResourceId);
        }
    }

    /**
     * 设置弹出的view
     *
     * @param menuContentLayoutResourceId pop中的view
     */
    public void setMenuContentViewResourceId(int menuContentLayoutResourceId) {
        LinearLayout menuContentView = (LinearLayout) View.inflate(getContext(), menuContentLayoutResourceId, null);
        if (pupupWindow == null) {
            pupupWindow = new AnimatorPopup(menuContentView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, false);
            setupPupupWindow(pupupWindow);
        } else {
            pupupWindow.setContentView(menuContentView);
        }
        onMenuContentViewCreated(menuContentView, this);
    }

    protected void onMenuContentViewCreated(ViewGroup menuContentView, EasyFilterMenu easyFilterMenu) {
    }

    /**
     * setupCustomView//设置带有确定按钮的view
     *
     * @param menuContentView     父容器
     * @param customViewConfirmId 确定按钮的id
     * @param easyFilterMenu      当前筛选menu
     * @param listView            确定按钮对于的listview
     */
    void setupCustomView(final ViewGroup menuContentView, int customViewConfirmId, final EasyFilterMenu easyFilterMenu, final ListView listView) {
        final View easyListFilter_CustomViewConfirm_List = menuContentView.findViewById(customViewConfirmId);//多选择时候的确定按钮的id
        if (easyListFilter_CustomViewConfirm_List != null) {
            easyListFilter_CustomViewConfirm_List.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (customViewConfirmClickListener != null) {
                        customViewConfirmClickListener.onClick(listView, menuContentView, easyFilterMenu);
                        //title 的设置请在回调中设置
                    }
                }
            });
        }
    }

//    public void setMenuData(boolean show, ArrayList<? extends IEasyItem> iEasyItems) {
//        onMenuDataPrepared(iEasyItems);
//        if (show && iEasyItems != null && iEasyItems.size() > 0) {
//            toggle();
//        }
//    }

    public void setMenuData(boolean show, EasyItemManager easyItemManager) {
        onMenuDataPrepared(easyItemManager);
        if (show && parentIEasyItem != null && parentIEasyItem.getChildItems().size() > 0) {
            toggle();
        }

    }

    protected void onMenuDataPrepared(ArrayList<? extends IEasyItem> iEasyItems) {

    }

    protected void onMenuDataPrepared(EasyItemManager easyItemManager) {

    }

    public void setMenuTitle(CharSequence menuTitle) {
        mTitleTextView.setText(menuTitle);
        if (!defultMenuText.equals(menuTitle)) {
            mTitleTextView.setEnabled(true);//通过改变mScreeningText的Enabled属性去改变他的颜色
        } else {
            mTitleTextView.setEnabled(false);
        }
    }

    public String getMenuTitle() {
        return mTitleTextView.getText().toString();
    }

    /**
     * init menuTitle
     *
     * @param menuTitleViewResourceId menuTitleViewResourceId
     */
    void setMenuTitleViewResourceId(int menuTitleViewResourceId) {
        View menuTitleView = View.inflate(getContext(), menuTitleViewResourceId, this);
        mTitleTextView = (TextView) menuTitleView.findViewById(R.id.easyListFilter_MenuTitleDisplayName);
        mTitleTextView.setEnabled(false);
        mTitleTextView.setText(defultMenuText);
        menuTitleView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onMenuClick();

            }
        });
        onMenuTitleViewCreated(menuTitleView, this);
    }

    protected void onMenuTitleViewCreated(View menuTitleView, EasyFilterMenu easyFilterMenu) {

    }

    /**
     * 设置pop
     *
     * @param pupupWindow pop
     */
    void setupPupupWindow(final PopupWindow pupupWindow) {
        pupupWindow.setBackgroundDrawable(new ColorDrawable(Color.argb(0, 0, 0, 0)));
        pupupWindow.setOutsideTouchable(false);
        pupupWindow.setFocusable(true);
        pupupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        pupupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_IS_FORWARD_NAVIGATION);
        pupupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                onDismissMenuContent();

            }
        });
    }

    /**
     * Menu 被点击后，一般这里现实pop
     */
    protected void onMenuClick() {
        System.out.println("onMenuClick");
//        pupupWindow.showAsDropDown(this);
        toggle();
    }

    /**
     * 是否是空
     *
     * @return isEmpty
     */
    protected abstract boolean isEmpty();

    /**
     * 控制现实nemu的现实和关闭
     */
    public void toggle() {
        postDelayed(this, 1);
    }

    void showMenuContent() {
        if (isEmpty()) {//检查数据是否是null，
            if (menuWithoutDataClickLinstener != null) {
                menuWithoutDataClickLinstener.withoutData(this);
            }
            return;
        }
        if (pupupWindow != null && !pupupWindow.isShowing()) {
            ViewGroup parent = (ViewGroup) this.getParent();
            if (parent != null && parent.getId() == R.id.easyListFilter_MenuParent) {//如果父容器的id是easyListFilter_MenuParent，就现实在父容器上面
                pupupWindow.showAsDropDown(parent, xoff, yoff);
            } else {
                pupupWindow.showAsDropDown(this, xoff, yoff);
            }
            onShowMenuContent();
        }

    }

    /**
     * pop显示后调用
     */
    protected void onShowMenuContent() {
        setFocusableInTouchMode(false);
        mTitleTextView.setSelected(true);
        setSelected(true);//当前layout 设置setSelected 位true
    }

    /**
     * pop消失后调用
     */
    protected void onDismissMenuContent() {
        mTitleTextView.setSelected(false);
        setFocusableInTouchMode(false);
        setFocusable(false);//当前layout
        if (dismissListener != null) {
            dismissListener.onDismiss();
        }
    }


    /**
     * dismiss pop
     */
    void dismissMenuContent() {
        if (pupupWindow != null && pupupWindow.isShowing()) {
            pupupWindow.dismiss();
        }
    }

    /**
     * MenuContent是否现实
     *
     * @return show
     */
    public boolean isShowing() {
        return pupupWindow != null && pupupWindow.isShowing();
    }

    public void dismiss() {
        if (pupupWindow != null && pupupWindow.isShowing()) {
            pupupWindow.dismiss();
        }
    }

    @Override
    public void run() {
        if (!isShowing()) {
            showMenuContent();
        } else {
            dismissMenuContent();
        }
    }

    /**
     * 设置pop小时后的监听
     *
     * @param dismissListener dismissListener
     */
    public void setOnDismissListener(OnDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    /**
     * 设置没有数据时候的监听回掉
     *
     * @param menuWithoutDataClickLinstener menuWithoutDataClickLinstener
     */
    public void setOnMenuWithoutDataClickLinstener(OnMenuWithoutDataClickLinstener menuWithoutDataClickLinstener) {
        this.menuWithoutDataClickLinstener = menuWithoutDataClickLinstener;
    }


    private OnDismissListener dismissListener;
    private OnMenuWithoutDataClickLinstener menuWithoutDataClickLinstener;

    /**
     * pop消失监听
     */

    public interface OnDismissListener {
        void onDismiss();
    }

    /**
     * 没有数据时候的回掉借口
     */
    public interface OnMenuWithoutDataClickLinstener {
        void withoutData(EasyFilterMenu menu);
    }

    OnCustomViewConfirmClickListener customViewConfirmClickListener;

    public void setOnCustomViewConfirmClickListener(OnCustomViewConfirmClickListener customViewConfirmClickListener) {
        this.customViewConfirmClickListener = customViewConfirmClickListener;
    }

    public interface OnCustomViewConfirmClickListener {
        void onClick(ListView listview, ViewGroup viewGroup, EasyFilterMenu easyFilterMenu);
    }

    public void setOnMenuListItemClickListener(OnMenuListItemClickListener menuListItemClickListener) {
        this.menuListItemClickListener = menuListItemClickListener;
    }

    protected OnMenuListItemClickListener menuListItemClickListener;
}
