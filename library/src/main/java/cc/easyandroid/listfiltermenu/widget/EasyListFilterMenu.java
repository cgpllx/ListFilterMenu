package cc.easyandroid.listfiltermenu.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
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
import cc.easyandroid.listfiltermenu.core.ListFilterAdapter;
import cc.easyandroid.listfiltermenu.core.SeletorUtils;
import cc.easyandroid.listfiltermenu.core.ShowBottomPopup;


public class EasyListFilterMenu extends LinearLayout {
    public static final String EASYID_NOFILTER = "Easy_noFilter";
    private CharSequence defultMenuText = "defultMenuText";
    protected PopupWindow pupupWindow;
    private int xoff = 0;
    private int yoff = 0;


    ListFilterAdapter<IEasyItem> filterAdapter_List1;
    ListFilterAdapter<IEasyItem> filterAdapter_List2;
    ListFilterAdapter<IEasyItem> filterAdapter_List3;

    public EasyListFilterMenu(Context context) {
        super(context);
        init(context, null, 0);
    }

    public EasyListFilterMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, R.attr.EasyListFilterMenuStyle);
    }

    public EasyListFilterMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    //    ListView mChildListView;
    private void init(Context context, AttributeSet attrs, int defStyle) {
        ColorStateList textColor = null;
        ColorStateList listItemTextColor = null;
        Drawable pressedBackground = null;
        Drawable normalBackground = null;
        Drawable list1ItempressedBackground = null;
        Drawable list1ItemnormalBackground = null;
        Drawable list2ItempressedBackground = null;
        Drawable list2ItemnormalBackground = null;
        Drawable list3ItempressedBackground = null;
        Drawable list3ItemnormalBackground = null;
        Drawable drawableRight = null;
        Drawable betweenListDivider = null;//list之间的分割线
        Drawable listItemDrawableRight = null;
        int textSize = 15;
        int listItemTextSize = 15;
        int listItemViewResourceId = R.layout.listfiltermenu_listitem;
        int menuTitleViewResourceId = R.layout.listfiltermenu_titlelayout;

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EasyListFilterMenu, defStyle, 0);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);


            if (attr == R.styleable.EasyListFilterMenu_menuText) {
                defultMenuText = a.getText(attr);
            } else if (attr == R.styleable.EasyListFilterMenu_menuTextColor) {
                textColor = a.getColorStateList(attr);
            } else if (attr == R.styleable.EasyListFilterMenu_menuPressedBackground) {
                pressedBackground = a.getDrawable(attr);
            } else if (attr == R.styleable.EasyListFilterMenu_menuNormalBackground) {
                normalBackground = a.getDrawable(attr);
            } else if (attr == R.styleable.EasyListFilterMenu_menuList1ItemPressedBackground) {
                list1ItempressedBackground = a.getDrawable(attr);
            } else if (attr == R.styleable.EasyListFilterMenu_menuList1ItemNormalBackground) {
                list1ItemnormalBackground = a.getDrawable(attr);
            } else if (attr == R.styleable.EasyListFilterMenu_menuList2ItemPressedBackground) {
                list2ItempressedBackground = a.getDrawable(attr);
            } else if (attr == R.styleable.EasyListFilterMenu_menuList2ItemNormalBackground) {
                list2ItemnormalBackground = a.getDrawable(attr);
            } else if (attr == R.styleable.EasyListFilterMenu_menuList3ItemPressedBackground) {
                list3ItempressedBackground = a.getDrawable(attr);
            } else if (attr == R.styleable.EasyListFilterMenu_menuList3ItemNormalBackground) {
                list3ItemnormalBackground = a.getDrawable(attr);
            } else if (attr == R.styleable.EasyListFilterMenu_menuTextSize) {
                textSize = a.getDimensionPixelSize(attr, textSize);
            } else if (attr == R.styleable.EasyListFilterMenu_menuDrawableRight) {
                drawableRight = a.getDrawable(attr);
            } else if (attr == R.styleable.EasyListFilterMenu_menuBetweenListDivider) {
                betweenListDivider = a.getDrawable(attr);
            } else if (attr == R.styleable.EasyListFilterMenu_menuListItemDrawableRight) {
                listItemDrawableRight = a.getDrawable(attr);
            } else if (attr == R.styleable.EasyListFilterMenu_menuListItemTextColor) {
                listItemTextColor = a.getColorStateList(attr);
            } else if (attr == R.styleable.EasyListFilterMenu_menuListItemTextSize) {
                listItemTextSize = a.getDimensionPixelSize(attr, listItemTextSize);
            } else if (attr == R.styleable.EasyListFilterMenu_menuListItemView) {
                listItemViewResourceId = a.getResourceId(attr, R.layout.listfiltermenu_listitem);
            } else if (attr == R.styleable.EasyListFilterMenu_menuTitleView) {
                menuTitleViewResourceId = a.getResourceId(attr, R.layout.listfiltermenu_titlelayout);
            } else if (attr == R.styleable.EasyListFilterMenu_menuListPupupXoff) {
                xoff = a.getDimensionPixelSize(attr, 0);
            } else if (attr == R.styleable.EasyListFilterMenu_menuListPupupYoff) {
                yoff = a.getDimensionPixelSize(attr, 0);
            }
        }
        a.recycle();
        final LinearLayout filter = (LinearLayout) View.inflate(context, R.layout.pop_filter, null);

        final LinearLayout childLayout = (LinearLayout) filter.findViewById(R.id.childlayout);
        childLayout.setVisibility(View.GONE);
        if (betweenListDivider != null) {//设置list之间的分割线
            filter.setDividerDrawable(betweenListDivider);
            childLayout.setDividerDrawable(betweenListDivider);
            filter.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            childLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        }
        pupupWindow = new ShowBottomPopup(filter, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
        pupupWindow.setBackgroundDrawable(new BitmapDrawable());
        pupupWindow.setOutsideTouchable(false);
        pupupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mScreeningText.setSelected(false);
            }
        });
        //父 listview
        final ListView listview_1 = (ListView) filter.findViewById(R.id.list_1);
        listview_1.setBackgroundColor(getResources().getColor(android.R.color.white));
        listview_1.setSelector(new ColorDrawable(Color.TRANSPARENT));
        listview_1.setVisibility(View.VISIBLE);
        listview_1.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

//        mParentListView.setit
        filterAdapter_List1 = new ListFilterAdapter(context, SeletorUtils.creatSelector(list1ItemnormalBackground, list1ItempressedBackground));
        filterAdapter_List1.setListItemDrawableRight(listItemDrawableRight);
        filterAdapter_List1.setListItemTextColor(listItemTextColor);
        filterAdapter_List1.setListItemTextSize(listItemTextSize);
        filterAdapter_List1.setListItemViewResourceId(listItemViewResourceId);
        listview_1.setAdapter(filterAdapter_List1);
        listview_1.setItemChecked(0, true);
        //mindle listview
        final ListView listview_2 = (ListView) filter.findViewById(R.id.list_2);
        listview_2.setBackgroundColor(getResources().getColor(android.R.color.white));
        listview_2.setSelector(new ColorDrawable(Color.TRANSPARENT));
        listview_2.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        listview_2.setTextFilterEnabled(true);
        listview_2.setVisibility(View.GONE);
        filterAdapter_List2 = new ListFilterAdapter(context, SeletorUtils.creatSelector(list2ItemnormalBackground, list2ItempressedBackground));
        filterAdapter_List2.setListItemDrawableRight(listItemDrawableRight);
        filterAdapter_List2.setListItemTextColor(listItemTextColor);
        filterAdapter_List2.setListItemTextSize(listItemTextSize);
        filterAdapter_List2.setListItemViewResourceId(listItemViewResourceId);
        listview_2.setAdapter(filterAdapter_List2);
        //right listview
        final ListView listview_3 = (ListView) filter.findViewById(R.id.list_3);
        listview_3.setBackgroundColor(getResources().getColor(android.R.color.white));
        listview_3.setSelector(new ColorDrawable(Color.TRANSPARENT));
        //rightListView.setOnItemClickListener(this);
        listview_3.setTextFilterEnabled(true);
        listview_3.setVisibility(View.GONE);
        filterAdapter_List3 = new ListFilterAdapter(context, SeletorUtils.creatSelector(list3ItemnormalBackground, list3ItempressedBackground));
        filterAdapter_List3.setListItemDrawableRight(listItemDrawableRight);
        filterAdapter_List3.setListItemTextColor(listItemTextColor);
        filterAdapter_List3.setListItemTextSize(listItemTextSize);
        filterAdapter_List3.setListItemViewResourceId(listItemViewResourceId);
        listview_3.setAdapter(filterAdapter_List3);

        listview_1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean clickIsHasChosen = true;//被选择的item不再重复执行逻辑
                Object tag = listview_1.getTag();
                if (tag == null || !(tag instanceof Integer)) {
                    clickIsHasChosen = false;
                } else {
                    int selectedItemId = (int) tag;
                    if (selectedItemId != position) {
                        clickIsHasChosen = false;
                    }
                }
                boolean nextListIsVisible = listview_2.getVisibility() == View.VISIBLE;
                if (clickIsHasChosen && nextListIsVisible) {
                    return;
                }
                IEasyItem iEasyItem = filterAdapter_List1.getItem(position);
                if (iEasyItem != null) {
                    List<? extends IEasyItem> mindleItems = iEasyItem.getChildItems();
                    if (mindleItems != null && mindleItems.size() > 0) {
                        addList2Items(mindleItems);
                        listview_2.setItemChecked(0, true);
                        if (childLayout.getVisibility() != View.VISIBLE) {
                            childLayout.setVisibility(View.VISIBLE);
                        }
                        listview_2.setTag(null);//清空标识
                        if (listview_2.getVisibility() != View.VISIBLE) {
                            listview_2.setVisibility(View.VISIBLE);
                        }
                        listview_1.setTag(position);
                        if (listview_3.getVisibility() != View.GONE) {
                            listview_3.setVisibility(View.GONE);
                        }
                    } else {
                        if (menuListItemClickListener != null) {
                            menuListItemClickListener.onClick(iEasyItem);
                        }
                        changMenuText(iEasyItem);
                    }
                }
            }
        });

        listview_2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean clickIsHasChosen = true;//被选择的item不再重复执行逻辑
                Object tag = listview_2.getTag();
                if (tag == null || !(tag instanceof Integer)) {
                    clickIsHasChosen = false;
                } else {
                    int selectedItemId = (int) tag;
                    if (selectedItemId != position) {
                        clickIsHasChosen = false;
                    }
                }
                boolean nextListIsVisible = listview_3.getVisibility() == View.VISIBLE;
                if (clickIsHasChosen && nextListIsVisible) {
                    return;
                }
                IEasyItem iEasyItem = filterAdapter_List2.getItem(position);
                if (iEasyItem != null) {
                    List<? extends IEasyItem> rightItems = iEasyItem.getChildItems();
                    if (rightItems != null && rightItems.size() > 0) {
                        addList3Items(rightItems);
                        listview_3.setItemChecked(0, true);
                        if (listview_3.getVisibility() != View.VISIBLE) {
                            listview_3.setVisibility(View.VISIBLE);
                        }
                        listview_2.setTag(position);
                    } else {
                        if (menuListItemClickListener != null) {
                            menuListItemClickListener.onClick(iEasyItem);
                        }
                        changMenuText(iEasyItem);
                    }
                }
            }
        });
        listview_3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IEasyItem iEasyItem = filterAdapter_List3.getItem(position);
                if (menuListItemClickListener != null) {
                    menuListItemClickListener.onClick(iEasyItem);
                }
                changMenuText(iEasyItem);
            }
        });


        initView(context, menuTitleViewResourceId);

        if (textColor != null) {
            mScreeningText.setTextColor(textColor);
        }
        StateListDrawable background = SeletorUtils.creatSelector(normalBackground, pressedBackground);
        if (background != null) {
            mScreeningText.setBackgroundDrawable(background);
        }
        if (!TextUtils.isEmpty(defultMenuText)) {
            mScreeningText.setText(defultMenuText);
        }
        mScreeningText.setTextSize(textSize);
        if (drawableRight != null) {
            mScreeningText.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableRight, null);
        }
    }

    TextView mScreeningText;

    /**
     * init menuTitle
     *
     * @param context
     */
    private void initView(Context context, int menuTitleViewResourceId) {
        View view = View.inflate(context, menuTitleViewResourceId, this);
        mScreeningText = (TextView) view.findViewById(R.id.easylistfilter_titledisplayname);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showListFilter();
            }
        });
//        mScreeningText.setText(name);
    }

    private void addList1Items(List<? extends IEasyItem> iEasyItems) {
        filterAdapter_List1.setmItems(iEasyItems);
    }

    private void addList2Items(List<? extends IEasyItem> iEasyItems) {
        filterAdapter_List2.setmItems(iEasyItems);
    }

    private void addList3Items(List<? extends IEasyItem> iEasyItems) {
        filterAdapter_List3.setmItems(iEasyItems);
    }

    /**
     * add item
     *
     * @param show       是否马上显示窗口
     * @param iEasyItems 数据
     */
    public void addItems(boolean show, List<? extends IEasyItem> iEasyItems) {
        addList1Items(iEasyItems);
        if (show && iEasyItems != null && iEasyItems.size() > 0) {
            showListFilter();
        }
    }

    public void addItems(List<? extends IEasyItem> iEasyItems) {
        addItems(true, iEasyItems);
    }

    public void changMenuText(IEasyItem iEasyItem) {
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

    private void showListFilter() {
        if (filterAdapter_List1.isEmpty()) {
            if (menuClickLinstener != null) {
                menuClickLinstener.withoutData(this);
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
        }
    }

    private void dismiss() {
        if (pupupWindow != null && pupupWindow.isShowing()) {
            pupupWindow.dismiss();
        }
    }

    OnMenuListItemClickListener menuListItemClickListener;
    OnMenuClickLinstener menuClickLinstener;


    public void setOnMenuListItemClickListener(OnMenuListItemClickListener menuListItemClickListener) {
        this.menuListItemClickListener = menuListItemClickListener;
    }

    public void setOnMenuClickLinstener(OnMenuClickLinstener menuClickLinstener) {
        this.menuClickLinstener = menuClickLinstener;
    }

    public interface OnMenuListItemClickListener<T extends IEasyItem> {
        void onClick(T t);
    }

    public interface OnMenuClickLinstener {
        void withoutData(EasyListFilterMenu menu);
    }
}
