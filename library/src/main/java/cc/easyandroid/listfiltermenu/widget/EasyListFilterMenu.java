package cc.easyandroid.listfiltermenu.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import cc.easyandroid.listfiltermenu.R;
import cc.easyandroid.listfiltermenu.core.AnimatorPopup;
import cc.easyandroid.listfiltermenu.core.IEasyItem;
import cc.easyandroid.listfiltermenu.core.IEasyItemFactory;
import cc.easyandroid.listfiltermenu.core.ListFilterAdapter;


public class EasyListFilterMenu extends LinearLayout implements Runnable {
    private CharSequence defultMenuText = "defultMenuText";
    private CharSequence currentMenuText;
    protected PopupWindow pupupWindow;
    private int xoff = 0;
    private int yoff = 0;
    private ListFilterAdapter<IEasyItem> filterAdapter_List1;
    private ListFilterAdapter<IEasyItem> filterAdapter_List2;
    private ListFilterAdapter<IEasyItem> filterAdapter_List3;

    private OnMenuListItemClickListener menuListItemClickListener;
    private OnMenuWithoutDataClickLinstener menuWithoutDataClickLinstener;
    private OnMultiMenuTitleFormat onMultiMenuTitleFormat;

    private static final int LISTFILTERMENU_LISTITEM_LAYOUT = R.layout.menu_listitem_layout;//list item 的layout
    private static final int LISTFILTERMENU_TITLE_LAYOUT = R.layout.menu_title_layout;// menu title 的layout
    private static final int MENUCONTENT_LAYOUT = R.layout.menu_content_layout;// 3个listview 的layout

    private TextView mScreeningText;
    private View menuTitleView;
    private int selectMode;//选择模式，单选还是多选
    SparseBooleanArray hasAddUnlimitedContainer = new SparseBooleanArray();
    SparseArray<CharSequence> multiTitles = new SparseArray<>();//多选择时候，记住标题的容器
    private CharSequence unlimitedTermDisplayName;//不限制，默认名称

    //不限的显示位置 SHOW_LIST_1 第一个list
    public static final int SHOW_LIST_NONE = 0;

    public static final int SHOW_LIST_1 = 1;

    public static final int SHOW_LIST_2 = 1 << 1;

    public static final int SHOW_LIST_3 = 1 << 2;

    private int mShowUnlimiteds;//哪几个list 显示

    private boolean highlightMenu;


    public interface SelectMode {
        int SINGLE = 0;
        int MULTI = 1;
    }

    public EasyListFilterMenu(Context context) {
        this(context, null);
    }

    public EasyListFilterMenu(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.EasyListFilterMenuStyle);
    }

    public EasyListFilterMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        Drawable betweenListDivider = null;//list之间的分割线
        Drawable list1Divider = null;//list之间的分割线
        Drawable list2Divider = null;//list之间的分割线
        Drawable list3Divider = null;//list之间的分割线
        int list1ItemViewResourceId = LISTFILTERMENU_LISTITEM_LAYOUT;
        int list2ItemViewResourceId = LISTFILTERMENU_LISTITEM_LAYOUT;
        int list3ItemViewResourceId = LISTFILTERMENU_LISTITEM_LAYOUT;

        int menuTitleViewResourceId = LISTFILTERMENU_TITLE_LAYOUT;

        int menuContentLayoutResourceId = MENUCONTENT_LAYOUT;

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EasyListFilterMenu, defStyle, 0);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.EasyListFilterMenu_menuText) {//menu title
                defultMenuText = a.getText(attr);
            } else if (attr == R.styleable.EasyListFilterMenu_menuBetweenListDivider) {//listview 之间的分割线
                betweenListDivider = a.getDrawable(attr);
            } else if (attr == R.styleable.EasyListFilterMenu_menuList1ItemLayout) {//第1个列表item  的资源id
                list1ItemViewResourceId = a.getResourceId(attr, LISTFILTERMENU_LISTITEM_LAYOUT);
            } else if (attr == R.styleable.EasyListFilterMenu_menuList2ItemLayout) {//第2个列表item  的资源id
                list2ItemViewResourceId = a.getResourceId(attr, LISTFILTERMENU_LISTITEM_LAYOUT);
            } else if (attr == R.styleable.EasyListFilterMenu_menuList3ItemLayout) {//第3个列表item  的资源id
                list3ItemViewResourceId = a.getResourceId(attr, LISTFILTERMENU_LISTITEM_LAYOUT);
            } else if (attr == R.styleable.EasyListFilterMenu_menuTitleView) {// menu title 的资源id
                menuTitleViewResourceId = a.getResourceId(attr, LISTFILTERMENU_TITLE_LAYOUT);
            } else if (attr == R.styleable.EasyListFilterMenu_menuListPupupXoff) {//x的偏移量
                xoff = a.getDimensionPixelSize(attr, 0);
            } else if (attr == R.styleable.EasyListFilterMenu_menuListPupupYoff) {// y的偏移量
                yoff = a.getDimensionPixelSize(attr, 0);
            } else if (attr == R.styleable.EasyListFilterMenu_menuMode) {
                selectMode = a.getInt(attr, SelectMode.SINGLE);
            } else if (attr == R.styleable.EasyListFilterMenu_unlimitedText) {
                unlimitedTermDisplayName = a.getText(attr);
            } else if (attr == R.styleable.EasyListFilterMenu_showUnlimiteds) {
                mShowUnlimiteds = a.getInt(R.styleable.EasyListFilterMenu_showUnlimiteds, SHOW_LIST_NONE);
            } else if (attr == R.styleable.EasyListFilterMenu_menuContentLayout) {
                menuContentLayoutResourceId = a.getResourceId(attr, MENUCONTENT_LAYOUT);
            } else if (attr == R.styleable.EasyListFilterMenu_list1Divider) {
                list1Divider = a.getDrawable(attr);
            } else if (attr == R.styleable.EasyListFilterMenu_list2Divider) {
                list2Divider = a.getDrawable(attr);
            } else if (attr == R.styleable.EasyListFilterMenu_list3Divider) {
                list3Divider = a.getDrawable(attr);
            }
        }
        a.recycle();
        final LinearLayout filter = (LinearLayout) View.inflate(context, menuContentLayoutResourceId, null);
        final LinearLayout childLayout = (LinearLayout) filter.findViewById(R.id.easyListFilter_MenuContent_ChildLayout);
        childLayout.setVisibility(View.GONE);
        if (betweenListDivider != null) {//设置list之间的分割线
            filter.setDividerDrawable(betweenListDivider);
            childLayout.setDividerDrawable(betweenListDivider);
            filter.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            childLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        }
        final EditText editText = (EditText) filter.findViewById(R.id.priceFrom);

        pupupWindow = new AnimatorPopup(filter, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, false);
        initPupupWindow(pupupWindow);
        if (editText != null) {
            editText.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    pupupWindow.setFocusable(true);
                    return false;
                }
            });
        }
        filterAdapter_List1 = new ListFilterAdapter(context, list1ItemViewResourceId);
        filterAdapter_List2 = new ListFilterAdapter(context, list2ItemViewResourceId);
        filterAdapter_List3 = new ListFilterAdapter(context, list3ItemViewResourceId);

        //listview--1
        final ListView listview_1 = (ListView) filter.findViewById(R.id.easyListFilter_MenuContent_List_1);
        initListView(listview_1, filterAdapter_List1);
        listview_1.setVisibility(View.VISIBLE);
        listview_1.setItemChecked(0, true);
        listview_1.setDivider(list1Divider);
        //listview--2
        final ListView listview_2 = (ListView) filter.findViewById(R.id.easyListFilter_MenuContent_List_2);
        if (listview_2 != null) {
            initListView(listview_2, filterAdapter_List2);
            listview_2.setVisibility(View.GONE);
            listview_2.setDivider(list2Divider);
        }
        //listview--3
        final ListView listview_3 = (ListView) filter.findViewById(R.id.easyListFilter_MenuContent_List_3);
        if (listview_3 != null) {
            initListView(listview_3, filterAdapter_List3);
            listview_3.setVisibility(View.GONE);
            listview_3.setDivider(list3Divider);
        }

        View easyList1MultipleConfirm = filter.findViewById(R.id.easyList1CustomViewConfirm);//多选择时候的确定按钮的id
        if (easyList1MultipleConfirm != null) {
            easyList1MultipleConfirm.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (customViewConfirmClickListener != null) {
                        customViewConfirmClickListener.onClick(listview_1, filterAdapter_List1, filter);
                        //title 的设置请在回调中设置
                    }
                }
            });
        }
        listview_1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean nextListIsVisible = listview_2.getVisibility() == View.VISIBLE;
                if (!clickPositionIsChanged(listview_1, position) && nextListIsVisible) {//下一个listview 是显示的，且当前点击的位置是上次记住的位置
                    return;
                }
                IEasyItem iEasyItem = filterAdapter_List1.getItem(position);
                if (listview_1.getChoiceMode() == ListView.CHOICE_MODE_MULTIPLE) {//listview 选择模式  是多选
                    SparseBooleanArray booleanArray = listview_1.getCheckedItemPositions();
                    if (iEasyItem == null || iEasyItem.isNoLimitItem()) {
                        listview_1.clearChoices();
                        listview_1.setItemChecked(0, true);
                        filterAdapter_List1.notifyDataSetChanged();
                        setMenuTitle(defultMenuText);
                        menuListItemClick(iEasyItem);
                    } else {
                        if (booleanArray.indexOfValue(true) != -1) {
                            listview_1.setItemChecked(0, false);
                        } else {
                            listview_1.setItemChecked(0, true);
                        }
                    }
                    return;
                } else {//listview 选择模式 不是多选
                    if (iEasyItem != null) {
                        List<? extends IEasyItem> mindleItems = iEasyItem.getChildItems();
                        if (mindleItems != null && mindleItems.size() > 0) {
                            currentMenuText = iEasyItem.getDisplayName();
                            addList2Items(iEasyItem);//传的是父类的IEasyItem ，适配器自己去里面找
                            showView(childLayout);
                            showView(listview_2);
                            listview_2.setItemChecked(iEasyItem.getChildSelectPosion(), true);
                            listview_2.setTag(null);//清空标识
                            hideView(listview_3);
                        } else {
                            hideView(childLayout);
                            hideView(listview_2);
                            if (selectMode == SelectMode.MULTI) {
                                changMultiMenuText(iEasyItem, filterAdapter_List2);
                            } else {
                                if (iEasyItem.isNoLimitItem()) {
                                    setMenuTitle(defultMenuText);
                                } else {
                                    changMenuText(iEasyItem);
                                }
                            }
                            menuListItemClick(iEasyItem);
                        }
                        listview_1.setTag(position);
                    }
                }
            }
        });
        if (listview_2 != null) {
            listview_2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    boolean nextListIsVisible = listview_3.getVisibility() == View.VISIBLE;
                    if (!clickPositionIsChanged(listview_2, position) && nextListIsVisible) {//下一个listview 是显示的，且当前点击的位置是上次记住的位置
                        return;
                    }
                    IEasyItem iEasyItem = filterAdapter_List2.getItem(position);
                    rememberPosion(filterAdapter_List2, position);
                    if (iEasyItem != null) {
                        List<? extends IEasyItem> rightItems = iEasyItem.getChildItems();
                        if (rightItems != null && rightItems.size() > 0) {
                            currentMenuText = iEasyItem.getDisplayName();
                            addList3Items(iEasyItem);
                            showView(listview_3);
                            //listview_3.setItemChecked(iEasyItem.getChildSelectPosion(), true);
                            if (selectMode == SelectMode.MULTI && iEasyItem.isNoLimitItem()) {
                                multiTitles.clear();
                            }
                        } else {
                            hideView(listview_3);
                            if (selectMode == SelectMode.MULTI) {
                                changMultiMenuText(iEasyItem, filterAdapter_List2);
                            } else {
                                changMenuText(iEasyItem);
                            }
                            menuListItemClick(iEasyItem);
                        }
                        listview_2.setTag(position);
                    }
                }
            });
        }
        if (listview_3 != null) {
            listview_3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    IEasyItem iEasyItem = filterAdapter_List3.getItem(position);
                    rememberPosion(filterAdapter_List3, position);
                    changMenuText(iEasyItem);
                    menuListItemClick(iEasyItem);
                }
            });
        }
        initMenuTitleView(context, menuTitleViewResourceId);
        setMenuTitle(defultMenuText);
        buildDefaultMultiMenuTitleFormat();
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (isShowing()) {
                        dismiss();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    /**
     * 打开软键盘
     */
    private void openKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void closeKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(getWindowToken(), 0);
        }
    }

    public int getSelectMode() {
        return selectMode;
    }

    public void setSelectMode(int selectMode) {
        this.selectMode = selectMode;
    }


    private void initPupupWindow(final PopupWindow pupupWindow) {
        pupupWindow.setBackgroundDrawable(new ColorDrawable(Color.argb(0, 0, 0, 0)));
        pupupWindow.setOutsideTouchable(false);
//        pupupWindow.setTouchable(true);
        pupupWindow.setFocusable(true);
//        pupupWindow.getContentView().getRootView().setOnKeyListener(new OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                System.out.println("onKey  1=" + keyCode);
//                return false;
//            }
//        });
        pupupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        pupupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_IS_FORWARD_NAVIGATION);
//        pupupWindow.sett
//        pupupWindow.setTouchInterceptor(new OnTouchListener() {
//
//            public boolean onTouch(View v, MotionEvent event) {
//                System.out.println("cgp= onTouch==" + v);
//                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
//
//                }
////                pupupWindow.setFocusable(false);
//                return v.onTouchEvent(event);
//            }
//        });
        pupupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (!defultMenuText.equals(mScreeningText.getText())) {
                    mScreeningText.setEnabled(true);
                } else {
                    mScreeningText.setEnabled(false);
                }
                mScreeningText.setSelected(false);
                menuTitleView.setSelected(false);
                setFocusableInTouchMode(false);
                setFocusable(false);
                closeKeyBoard();
            }
        });
    }

    private void showView(View listView) {
        if (listView.getVisibility() != View.VISIBLE) {
            listView.setVisibility(View.VISIBLE);
        }
    }

    public CharSequence getUnlimitedTermDisplayName() {
        return unlimitedTermDisplayName;
    }

    public int getShowUnlimiteds() {
        return mShowUnlimiteds;
    }

    public void setShowUnlimiteds(int mShowUnlimiteds) {
        this.mShowUnlimiteds = mShowUnlimiteds;
    }

    public SparseArray<CharSequence> getMultiTitles() {
        return multiTitles;
    }

    public void setMultiTitles(SparseArray<CharSequence> multiTitles) {
        this.multiTitles = multiTitles;
    }

    public void setUnlimitedTermDisplayName(CharSequence unlimitedTermDisplayName) {
        this.unlimitedTermDisplayName = unlimitedTermDisplayName;
    }

    private void hideView(View listView) {
        if (listView.getVisibility() != View.GONE) {
            listView.setVisibility(View.GONE);
        }
    }

    /**
     * item click
     *
     * @param iEasyItem
     */
    public void menuListItemClick(IEasyItem iEasyItem) {
        if (menuListItemClickListener != null) {
            menuListItemClickListener.onClick(iEasyItem);
        }
        dismiss();//item 被点击后dismiss pop 最后销毁，
    }

    /**
     * 检测改listview 的item 是否被选择的
     *
     * @param listView
     * @param position 位置
     * @return
     */
    private boolean clickPositionIsChanged(ListView listView, int position) {
        boolean clickIsHasChosen = false;//被选择的item不再重复执行逻辑
        Object tag = listView.getTag();
        if (tag == null || !(tag instanceof Integer)) {
            clickIsHasChosen = true;
        } else {
            int selectedItemId = (int) tag;
            if (selectedItemId != position) {
                clickIsHasChosen = true;
            }
        }
        return clickIsHasChosen;
    }


    private void initListView(ListView listView, ListFilterAdapter<IEasyItem> adapter) {
        listView.setBackgroundColor(getResources().getColor(android.R.color.white));
        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
//        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listView.setTextFilterEnabled(true);
        listView.setAdapter(adapter);
    }

    private void rememberPosion(ListFilterAdapter<IEasyItem> adapter, int position) {
        if (selectMode == SelectMode.MULTI) {
            adapter.getParentIEasyItem().setChildSelectPosion(position);
        }
    }

    /**
     * init menuTitle
     *
     * @param context
     */
    private void initMenuTitleView(Context context, int menuTitleViewResourceId) {
        menuTitleView = View.inflate(context, menuTitleViewResourceId, this);
        mScreeningText = (TextView) menuTitleView.findViewById(R.id.easyListFilter_MenuTitleDisplayName);
        mScreeningText.setEnabled(false);
        menuTitleView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                menuTitleViewClick(v);

            }
        });
    }

    private void menuTitleViewClick(View view) {
        if (!isShowing()) {
            if (menuShowListener != null) {
                menuShowListener.onMenuShowBefore(this);
            }
        }
        postShow();
    }

    private void addList1Items(IEasyItem parentIEasyItem) {
        if ((mShowUnlimiteds & SHOW_LIST_1) != 0) {
            addUnlimitedToContaier(parentIEasyItem);
        }
        filterAdapter_List1.setParentIEasyItem(parentIEasyItem);
    }


    private void addList2Items(IEasyItem parentIEasyItem) {
        if ((mShowUnlimiteds & SHOW_LIST_2) != 0) {
            addUnlimitedToContaier(parentIEasyItem);
        }
        filterAdapter_List2.setParentIEasyItem(parentIEasyItem);
    }

    public void addUnlimitedToContaier(IEasyItem parentIEasyItem) {
        if (!TextUtils.isEmpty(unlimitedTermDisplayName) && !hasAddUnlimitedContainer.get(parentIEasyItem.hashCode(), false)) {
            hasAddUnlimitedContainer.put(parentIEasyItem.hashCode(), true);
            List list = parentIEasyItem.getChildItems();
            if (list != null && list.size() > 0) {
                IEasyItem iEasyItem = (IEasyItem) list.get(0);

                HashMap<String, String> map = iEasyItem.getEasyParameter();
                IEasyItem unlimitediEasyItem = IEasyItemFactory.buildOneIEasyItem(unlimitedTermDisplayName, map);
                list.add(0, unlimitediEasyItem);
            }
        }
    }

    private void addList3Items(IEasyItem parentIEasyItem) {
        if ((mShowUnlimiteds & SHOW_LIST_3) != 0) {
            addUnlimitedToContaier(parentIEasyItem);
        }
        filterAdapter_List3.setParentIEasyItem(parentIEasyItem);
    }

    /**
     * set items
     *
     * @param iEasyItems 数据
     */
    public void setItems(List<? extends IEasyItem> iEasyItems) {
        if (iEasyItems != null && iEasyItems.size() > 0) {
            addList1Items(IEasyItemFactory.buildIEasyItem(iEasyItems));
        }
    }

    /**
     * add item
     *
     * @param show       是否马上显示窗口
     * @param iEasyItems 数据
     */
    public void addItems(boolean show, List<? extends IEasyItem> iEasyItems) {
        addList1Items(IEasyItemFactory.buildIEasyItem(iEasyItems));
        if (show && iEasyItems != null && iEasyItems.size() > 0) {
            postShow();
        }
    }

    public void addItems(List<? extends IEasyItem> iEasyItems) {
        addItems(true, iEasyItems);
    }

    private void changMenuText(IEasyItem iEasyItem) {
//        dismiss();
        if (iEasyItem != null) {
            if (iEasyItem.isNoLimitItem()) {
                mScreeningText.setText(currentMenuText);
            } else {
                CharSequence displayName = iEasyItem.getDisplayName();
                if (!TextUtils.isEmpty(displayName)) {
                    mScreeningText.setText(displayName);
                }
            }
        }
    }

    private void changMultiMenuText(IEasyItem iEasyItem, ListFilterAdapter<IEasyItem> adapter) {

        if (!iEasyItem.isNoLimitItem()) {
            multiTitles.put(adapter.getParentIEasyItem().hashCode(), iEasyItem.getDisplayName());
        } else {
            multiTitles.delete(adapter.getParentIEasyItem().hashCode());
        }
        if (onMultiMenuTitleFormat != null) {
            onMultiMenuTitleFormat.format(this, multiTitles);
        }
//        onMenuTitleChanged(multiTitles);
    }

    private void buildDefaultMultiMenuTitleFormat() {
        onMultiMenuTitleFormat = new OnMultiMenuTitleFormat() {
            @Override
            public void format(EasyListFilterMenu easyListFilterMenu, SparseArray<CharSequence> multiTitles) {
                onMenuTitleChanged(easyListFilterMenu, multiTitles);
            }
        };
    }

    /**
     * 多选情况下title的样式
     *
     * @param multiTitles 装有所有被选择的title集合
     */
    protected void onMenuTitleChanged(EasyListFilterMenu easyListFilterMenu, SparseArray<CharSequence> multiTitles) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < multiTitles.size(); i++) {
            stringBuilder.append(multiTitles.valueAt(i));
            stringBuilder.append("|");
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
        }
        easyListFilterMenu.setMenuTitle(stringBuilder.toString());
    }

    public void setMenuTitle(CharSequence menuTitle) {
        if (!TextUtils.isEmpty(menuTitle)) {
            mScreeningText.setText(menuTitle);
        } else {
            mScreeningText.setText(defultMenuText);
        }
    }

    private void postShow() {
        postDelayed(this, 1);
    }

    private void showListFilter() {
        if (filterAdapter_List1.isEmpty()) {
            if (menuWithoutDataClickLinstener != null) {
                menuWithoutDataClickLinstener.withoutData(this);
            }
            return;
        }
        if (pupupWindow != null && !pupupWindow.isShowing()) {
            ViewGroup parent = (ViewGroup) this.getParent();
            if (parent != null && parent.getId() == R.id.easyListFilter_MenuParent) {
                pupupWindow.showAsDropDown(parent, xoff, yoff);
            } else {
                pupupWindow.showAsDropDown(this, xoff, yoff);
            }
            mScreeningText.setSelected(true);
            menuTitleView.setSelected(true);
            setFocusableInTouchMode(true);
//            setFocusable(true);
        }

    }

    public void dismiss() {
        if (pupupWindow != null && pupupWindow.isShowing()) {
            pupupWindow.dismiss();
        }
    }

    public boolean isShowing() {
        if (pupupWindow != null && pupupWindow.isShowing()) {
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        if (pupupWindow != null && !pupupWindow.isShowing()) {
            showListFilter();
        } else {
            dismiss();
        }
    }

    OnMenuShowListener menuShowListener;

    public void setOnMenuShowListener(OnMenuShowListener menuShowListener) {
        this.menuShowListener = menuShowListener;
    }

    public interface OnMenuShowListener {
        void onMenuShowBefore(EasyListFilterMenu menu);
    }

    public void setOnMenuListItemClickListener(OnMenuListItemClickListener menuListItemClickListener) {
        this.menuListItemClickListener = menuListItemClickListener;

    }

    public void setOnMultiMenuTitleFormat(OnMultiMenuTitleFormat onMultiMenuTitleFormat) {
        this.onMultiMenuTitleFormat = onMultiMenuTitleFormat;
    }

    public void setOnMenuClickLinstener(OnMenuWithoutDataClickLinstener menuWithoutDataClickLinstener) {
        this.menuWithoutDataClickLinstener = menuWithoutDataClickLinstener;
    }

    public interface OnMultiMenuTitleFormat {
        void format(EasyListFilterMenu easyListFilterMenu, SparseArray<CharSequence> multiTitles);
    }

    public interface OnMenuListItemClickListener<T extends IEasyItem> {
        void onClick(T t);
    }

    OnCustomViewConfirmClickListener customViewConfirmClickListener;

    public void setOnCustomViewConfirmClickListener(OnCustomViewConfirmClickListener customViewConfirmClickListener) {
        this.customViewConfirmClickListener = customViewConfirmClickListener;
    }

    public interface OnCustomViewConfirmClickListener {
        void onClick(ListView listview, ListFilterAdapter<IEasyItem> filterAdapter_List, ViewGroup viewGroup);
    }

    public interface OnMenuWithoutDataClickLinstener {
        void withoutData(EasyListFilterMenu menu);
    }
}
