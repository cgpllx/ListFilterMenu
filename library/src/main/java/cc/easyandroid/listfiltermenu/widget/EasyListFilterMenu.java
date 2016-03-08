package cc.easyandroid.listfiltermenu.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import cc.easyandroid.listfiltermenu.R;
import cc.easyandroid.listfiltermenu.core.IEasyItem;
import cc.easyandroid.listfiltermenu.core.IEasyItemFactory;
import cc.easyandroid.listfiltermenu.core.ListFilterAdapter;
import cc.easyandroid.listfiltermenu.core.OnMenuListItemClickListener;
import cc.easyandroid.listfiltermenu.core.OnMenuWithoutDataClickLinstener;
import cc.easyandroid.listfiltermenu.core.ShowBottomPopup;


public class EasyListFilterMenu extends LinearLayout implements Runnable {
    public static final String EASYID_NOFILTER = "";
    private CharSequence defultMenuText = "defultMenuText";
    protected PopupWindow pupupWindow;
    private int xoff = 0;
    private int yoff = 0;
    private final Handler mHandler;
    private ListFilterAdapter<IEasyItem> filterAdapter_List1;
    private ListFilterAdapter<IEasyItem> filterAdapter_List2;
    private ListFilterAdapter<IEasyItem> filterAdapter_List3;

    private OnMenuListItemClickListener menuListItemClickListener;
    private OnMenuWithoutDataClickLinstener menuWithoutDataClickLinstener;

    private static final int LISTFILTERMENU_LISTITEM_LAYOUT = R.layout.listfiltermenu_listitem;//list item 的layout
    private static final int LISTFILTERMENU_TITLE_LAYOUT = R.layout.listfiltermenu_titlelayout;// menu title 的layout
    private static final int POP_FILTER_LAYOUT = R.layout.pop_filter;// 3个listview 的layout

    private TextView mScreeningText;
    private View menuTitleView;

    private int selectMode;

    SparseArray<String> stringSparseArray = new SparseArray<>();

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
        mHandler = new Handler(context.getMainLooper());
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        Drawable betweenListDivider = null;//list之间的分割线
        int list1ItemViewResourceId = LISTFILTERMENU_LISTITEM_LAYOUT;
        int list2ItemViewResourceId = LISTFILTERMENU_LISTITEM_LAYOUT;
        int list3ItemViewResourceId = LISTFILTERMENU_LISTITEM_LAYOUT;

        int menuTitleViewResourceId = LISTFILTERMENU_TITLE_LAYOUT;

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EasyListFilterMenu, defStyle, 0);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.EasyListFilterMenu_menuText) {//menu title
                defultMenuText = a.getText(attr);
            } else if (attr == R.styleable.EasyListFilterMenu_menuBetweenListDivider) {//listview 之间的分割线
                betweenListDivider = a.getDrawable(attr);
            } else if (attr == R.styleable.EasyListFilterMenu_menuList1ItemView) {//第1个列表item  的资源id
                list1ItemViewResourceId = a.getResourceId(attr, LISTFILTERMENU_LISTITEM_LAYOUT);
            } else if (attr == R.styleable.EasyListFilterMenu_menuList2ItemView) {//第2个列表item  的资源id
                list2ItemViewResourceId = a.getResourceId(attr, LISTFILTERMENU_LISTITEM_LAYOUT);
            } else if (attr == R.styleable.EasyListFilterMenu_menuList3ItemView) {//第3个列表item  的资源id
                list3ItemViewResourceId = a.getResourceId(attr, LISTFILTERMENU_LISTITEM_LAYOUT);
            } else if (attr == R.styleable.EasyListFilterMenu_menuTitleView) {// menu title 的资源id
                menuTitleViewResourceId = a.getResourceId(attr, LISTFILTERMENU_TITLE_LAYOUT);
            } else if (attr == R.styleable.EasyListFilterMenu_menuListPupupXoff) {//x的偏移量
                xoff = a.getDimensionPixelSize(attr, 0);
            } else if (attr == R.styleable.EasyListFilterMenu_menuListPupupYoff) {// y的偏移量
                yoff = a.getDimensionPixelSize(attr, 0);
            } else if (attr == R.styleable.EasyListFilterMenu_menuMode) {
                selectMode = a.getInt(attr, SelectMode.SINGLE);
            }
        }
        a.recycle();
        final LinearLayout filter = (LinearLayout) View.inflate(context, POP_FILTER_LAYOUT, null);
        final LinearLayout childLayout = (LinearLayout) filter.findViewById(R.id.childlayout);
        childLayout.setVisibility(View.GONE);
        if (betweenListDivider != null) {//设置list之间的分割线
            filter.setDividerDrawable(betweenListDivider);
            childLayout.setDividerDrawable(betweenListDivider);
            filter.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            childLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        }
        pupupWindow = new ShowBottomPopup(filter, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
        initPupupWindow(pupupWindow);
        filterAdapter_List1 = new ListFilterAdapter(context, list1ItemViewResourceId);
        filterAdapter_List2 = new ListFilterAdapter(context, list2ItemViewResourceId);
        filterAdapter_List3 = new ListFilterAdapter(context, list3ItemViewResourceId);
        //listview--1
        final ListView listview_1 = (ListView) filter.findViewById(R.id.list_1);
        initListView(listview_1, filterAdapter_List1);
        listview_1.setVisibility(View.VISIBLE);
        listview_1.setItemChecked(0, true);
        //listview--2
        final ListView listview_2 = (ListView) filter.findViewById(R.id.list_2);
        initListView(listview_2, filterAdapter_List2);
        listview_2.setVisibility(View.GONE);
        //listview--3
        final ListView listview_3 = (ListView) filter.findViewById(R.id.list_3);
        initListView(listview_3, filterAdapter_List3);
        listview_3.setVisibility(View.GONE);

        listview_1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean nextListIsVisible = listview_2.getVisibility() == View.VISIBLE;
                if (!clickPositionIsChanged(listview_1, position) && nextListIsVisible) {//下一个listview 是显示的，且当前点击的位置是上次记住的位置
                    return;
                }
                IEasyItem iEasyItem = filterAdapter_List1.getItem(position);
                if (iEasyItem != null) {
                    List<? extends IEasyItem> mindleItems = iEasyItem.getChildItems();
                    if (mindleItems != null && mindleItems.size() > 0) {
                        addList2Items(iEasyItem);//传的是父类的IEasyItem ，适配器自己去里面找
                        showView(childLayout);
                        showView(listview_2);
                        listview_2.setItemChecked(iEasyItem.getChildSelectPosion(), true);
                        listview_2.setTag(null);//清空标识

                        listview_1.setTag(position);
                        hideView(listview_3);

                    } else {
                        changMenuText(iEasyItem);
                        onMenuListItemClick(iEasyItem);
                    }
                }
            }
        });
        listview_2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean nextListIsVisible = listview_3.getVisibility() == View.VISIBLE;
                if (!clickPositionIsChanged(listview_2, position) && nextListIsVisible) {//下一个listview 是显示的，且当前点击的位置是上次记住的位置
                    return;
                }
                IEasyItem iEasyItem = filterAdapter_List2.getItem(position);
                rememberPosion(filterAdapter_List2, position);
//                filterAdapter_List2.getParentIEasyItem().setChildSelectPosion(position);
                if (iEasyItem != null) {
                    List<? extends IEasyItem> rightItems = iEasyItem.getChildItems();
                    if (rightItems != null && rightItems.size() > 0) {
                        addList3Items(iEasyItem);
                        showView(listview_3);
                        listview_3.setItemChecked(iEasyItem.getChildSelectPosion(), true);

                        listview_2.setTag(position);
                        if (selectMode == SelectMode.MULTI && TextUtils.isEmpty(iEasyItem.getEasyId())) {
                            stringSparseArray.clear();
                        }
                    } else {
                        hideView(listview_3);
                        if (selectMode == SelectMode.MULTI) {
                            changMultiMenuText(iEasyItem, filterAdapter_List2);
                        } else {
                            changMenuText(iEasyItem);
                        }
                        onMenuListItemClick(iEasyItem);
                    }
                }
            }
        });
        listview_3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IEasyItem iEasyItem = filterAdapter_List3.getItem(position);
                rememberPosion(filterAdapter_List3, position);
                changMenuText(iEasyItem);
                onMenuListItemClick(iEasyItem);
            }
        });
        initMenuTitleView(context, menuTitleViewResourceId);
        setMenuTitle(defultMenuText);
    }

    public int getSelectMode() {
        return selectMode;
    }

    public void setSelectMode(int selectMode) {
        this.selectMode = selectMode;
    }

    private void initPupupWindow(PopupWindow pupupWindow) {
        pupupWindow.setBackgroundDrawable(new BitmapDrawable());
        pupupWindow.setOutsideTouchable(false);
        pupupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mScreeningText.setSelected(false);
                menuTitleView.setSelected(false);
            }
        });
    }

    private void showView(View listView) {
        if (listView.getVisibility() != View.VISIBLE) {
            listView.setVisibility(View.VISIBLE);
        }

    }

    private void hideView(View listView) {
        if (listView.getVisibility() != View.GONE) {
            listView.setVisibility(View.GONE);
        }
    }

    private void onMenuListItemClick(IEasyItem iEasyItem) {
        if (menuListItemClickListener != null) {
            menuListItemClickListener.onClick(iEasyItem);
        }
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
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
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
        mScreeningText = (TextView) menuTitleView.findViewById(R.id.easylistfilter_titledisplayname);
        menuTitleView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                postShow();
            }
        });
    }

    private void addList1Items(IEasyItem parentIEasyItem) {
        filterAdapter_List1.setParentIEasyItem(parentIEasyItem);
    }

    private void addList2Items(IEasyItem parentIEasyItem) {
        filterAdapter_List2.setParentIEasyItem(parentIEasyItem);
    }

    private void addList3Items(IEasyItem parentIEasyItem) {
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
        dismiss();
        if (iEasyItem != null) {
            String displayName = iEasyItem.getDisplayName();
            String easyId = iEasyItem.getEasyId();
            if (TextUtils.isEmpty(easyId) || EASYID_NOFILTER.equals(easyId)) {
                mScreeningText.setText(defultMenuText);
            } else {
                if (!TextUtils.isEmpty(displayName)) {
                    mScreeningText.setText(displayName);
                }
            }
        }
    }

    private void changMultiMenuText(IEasyItem iEasyItem, ListFilterAdapter<IEasyItem> adapter) {
        dismiss();
        if (!TextUtils.isEmpty(iEasyItem.getEasyId())) {
            stringSparseArray.put(adapter.getParentIEasyItem().hashCode(), iEasyItem.getDisplayName());
        } else {
            stringSparseArray.delete(adapter.getParentIEasyItem().hashCode());
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < stringSparseArray.size(); i++) {
            stringBuilder.append(stringSparseArray.valueAt(i));
            stringBuilder.append("|");
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
        }
        setMenuTitle(stringBuilder.toString());
    }

    public void setMenuTitle(CharSequence menuTitle) {
        if (!TextUtils.isEmpty(menuTitle)) {
            mScreeningText.setText(menuTitle);
        } else {
            mScreeningText.setText(defultMenuText);
        }
    }

    private void postShow() {
        mHandler.post(this);
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
            if (parent != null && parent.getId() == R.id.easylistfilter_menuparent) {
                pupupWindow.showAsDropDown(parent, xoff, yoff);
            } else {
                pupupWindow.showAsDropDown(this, xoff, yoff);
            }
            mScreeningText.setSelected(true);
            menuTitleView.setSelected(true);
        }
    }

    private void dismiss() {
        if (pupupWindow != null && pupupWindow.isShowing()) {
            pupupWindow.dismiss();
        }
    }

    @Override
    public void run() {
        showListFilter();
    }

    public void setOnMenuListItemClickListener(OnMenuListItemClickListener menuListItemClickListener) {
        this.menuListItemClickListener = menuListItemClickListener;
    }

    public void setOnMenuClickLinstener(OnMenuWithoutDataClickLinstener menuWithoutDataClickLinstener) {
        this.menuWithoutDataClickLinstener = menuWithoutDataClickLinstener;
    }
}
