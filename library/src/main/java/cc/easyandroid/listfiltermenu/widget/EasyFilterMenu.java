package cc.easyandroid.listfiltermenu.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.util.ArrayMap;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import cc.easyandroid.listfiltermenu.R;
import cc.easyandroid.listfiltermenu.core.AnimatorPopup;
import cc.easyandroid.listfiltermenu.core.EasyFilterListener;
import cc.easyandroid.listfiltermenu.core.EasyItemManager;
import cc.easyandroid.listfiltermenu.core.EasyMenuStates;
import cc.easyandroid.listfiltermenu.core.IEasyItem;

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

    protected ArrayMap<String, String> easyMenuParas = new ArrayMap<>();


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
        int menuContentLayoutResourceId = R.layout.menu_content_single_layout;// menu contentView  id

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EasyFilterMenu, defStyle, 0);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.EasyFilterMenu_menuTitle) {//menu title
                defultMenuText = a.getText(attr);
            } else if (attr == R.styleable.EasyFilterMenu_menuTitleLayout) {// menu title 的资源id
                menuTitleViewResourceId = a.getResourceId(attr, R.layout.menu_title_layout);
            } else if (attr == R.styleable.EasyFilterMenu_menuContentLayout) {// menucontent 的资源id
                menuContentLayoutResourceId = a.getResourceId(attr, R.layout.menu_content_single_layout);
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
        ViewGroup menuContentView = (ViewGroup) View.inflate(getContext(), menuContentLayoutResourceId, null);
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
    void setupCustomView(final ViewGroup menuContentView, final int customViewConfirmId, final EasyFilterMenu easyFilterMenu, final ListView listView) {
        final View easyListFilter_CustomViewConfirm_List = menuContentView.findViewById(customViewConfirmId);//多选择时候的确定按钮的id
        if (easyListFilter_CustomViewConfirm_List != null) {
            easyListFilter_CustomViewConfirm_List.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (customViewConfirmClickListener != null) {
                        customViewConfirmClickListener.onClick(listView, menuContentView, easyFilterMenu);
                        onCustomViewConfirmBuntonClicked(menuContentView, customViewConfirmId, listView);
                        //title 的设置请在回调中设置
                    }
                }
            });
        }
    }

    protected void onCustomViewConfirmBuntonClicked(ViewGroup menuContentView, int customViewConfirmId, final ListView listView) {

    }


    public void setMenuData(boolean show, EasyItemManager easyItemManager) {
        onMenuDataPrepared(easyItemManager);
        if (show && easyItemManager != null && easyItemManager.isHasEasyItems()) {
            toggle();
        }

    }

    public abstract EasyItemManager getMenuData();

    protected void onMenuDataPrepared(EasyItemManager easyItemManager) {

    }

    public void setMenuTitle(CharSequence menuTitle) {
        setMenuTitle(menuTitle, false);
    }

    public void setMenuTitle(CharSequence menuTitle, boolean forceHighlight) {
        mTitleTextView.setText(menuTitle);
        if (!defultMenuText.equals(menuTitle) || forceHighlight) {
            mTitleTextView.setEnabled(true);//通过改变mScreeningText的Enabled属性去改变他的颜色
        } else {
            mTitleTextView.setEnabled(false);
        }
    }

    public CharSequence getDefultMenuText() {
        return defultMenuText;
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
        pupupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
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
            if (menuShowListeners.size() > 0) {
                for (EasyFilterListener.OnMenuShowListener menuShowListener : this.menuShowListeners) {
                    menuShowListener.onMenuShowBefore(this, pupupWindow.getContentView());
                }
            }
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
     * serial number
     */
    public int getMenuSerialNumber() {
        return defultMenuText.toString().hashCode();
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

    EasyFilterListener.OnCustomViewConfirmClickListener customViewConfirmClickListener;

    public <T extends EasyFilterMenu> void setOnCustomViewConfirmClickListener(EasyFilterListener.OnCustomViewConfirmClickListener<T> customViewConfirmClickListener) {
        this.customViewConfirmClickListener = customViewConfirmClickListener;
    }


    public void setOnMenuListItemClickListener(EasyFilterListener.OnMenuListItemClickListener menuListItemClickListener) {
        this.menuListItemClickListener = menuListItemClickListener;
    }

    protected EasyFilterListener.OnMenuListItemClickListener menuListItemClickListener;

    public void setMenuStates(EasyMenuStates easyMenuStates) {
        setMenuData(false, easyMenuStates.getEasyItemManager());
        putEasyMenuParas(easyMenuStates.getEasyMenuParas());
        setMenuTitle(easyMenuStates.getMenuTitle());
    }

    public void cleanMenuStates() {
        clearEasyMenuParas();//清除参数
        setMenuTitle(defultMenuText);
        onCleanMenuStatus();
    }

    protected abstract void onCleanMenuStatus();

    /**
     * 获取当前menu的数据状态，可以传给其他menu使用
     *
     * @return
     */
    public EasyMenuStates getMenuStates() {
        EasyItemManager easyItemManager = getMenuData();
        if (easyItemManager == null) {
            return null;
        } else {
            return onCreateMenuStates(easyItemManager);
        }
    }

    protected abstract EasyMenuStates onCreateMenuStates(EasyItemManager easyItemManager);

    ;

    //    EasyFilterListener.OnMenuShowListener menuShowListener;
    /**
     * 可能有多个地方需要监听
     */
    ArrayList<EasyFilterListener.OnMenuShowListener> menuShowListeners = new ArrayList<>();

    public void addOnMenuShowListener(EasyFilterListener.OnMenuShowListener menuShowListener) {
        this.menuShowListeners.add(menuShowListener);
    }

    public void menuListItemClick(IEasyItem iEasyItem) {
        addParaFromIEasyItem(iEasyItem);//记录参数
        if (this.menuListItemClickListener != null) {
            this.menuListItemClickListener.onClick(this, iEasyItem);
        }
        this.dismiss();
    }

    private void addParaFromIEasyItem(IEasyItem iEasyItem) {
        putEasyMenuParas(iEasyItem.getEasyParameter());
    }

    private EasyFilterListener.OnEasyMenuParasChangedListener onEasyMenuParasChangedListener;

    public void setOnEasyMenuParasChangedListener(EasyFilterListener.OnEasyMenuParasChangedListener onEasyMenuParasChangedListener) {
        this.onEasyMenuParasChangedListener = onEasyMenuParasChangedListener;
    }

    public void putEasyMenuParas(Map<String, String> easyMenuParas) {
        this.easyMenuParas.putAll(easyMenuParas);
        if (onEasyMenuParasChangedListener != null) {
            onEasyMenuParasChangedListener.onChanged(this.easyMenuParas);
        }
    }

    public ArrayMap<String, String> getEasyMenuParas() {
        return this.easyMenuParas;
    }

    public void deleteEasyMenuPara(String key) {
        this.easyMenuParas.remove(key);
    }

    /**
     * 清除参数
     */
    public void clearEasyMenuParas() {
        this.easyMenuParas.clear();
    }

    public boolean hasSelectedValues() {
        return true;
    }
}
