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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cc.easyandroid.listfiltermenu.R;
import cc.easyandroid.listfiltermenu.adapter.ListFilterAdapter;
import cc.easyandroid.listfiltermenu.pojo.IEasyItem;


public class EasyListFilterMenu extends LinearLayout implements View.OnClickListener {
    public static final String EASYID_NOFILTER = "Easy_noFilter";
    private CharSequence defultMenuText = "defultMenuText";
    protected PopupWindow pupupWindow;

    ListFilterAdapter<IEasyItem> filterAdapter_List1;
    ListFilterAdapter<IEasyItem> filterAdapter_list2;
    ListFilterAdapter<IEasyItem> filterAdapter_list3;

    public EasyListFilterMenu(Context context) {
        super(context);
        init(null, 0);
    }

    public EasyListFilterMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public EasyListFilterMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }


    //    ListView mChildListView;
    private void init(AttributeSet attrs, int defStyle) {
        ColorStateList textColor = null;
        Drawable pressedBackground = null;
        Drawable normalBackground = null;
        Drawable list1ItempressedBackground = null;
        Drawable list1ItemnormalBackground = null;
        Drawable list2ItempressedBackground = null;
        Drawable list2ItemnormalBackground = null;
        Drawable list3ItempressedBackground = null;
        Drawable list3ItemnormalBackground = null;
        Drawable drawableRight = null;
        int textSize = 15;

        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.EasyListFilterMenu, defStyle, 0);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.EasyListFilterMenu_menuText:
                    defultMenuText = a.getText(attr);
                    break;
                case R.styleable.EasyListFilterMenu_menuTextColor:
                    textColor = a.getColorStateList(attr);
                    break;
                case R.styleable.EasyListFilterMenu_menuPressedBackground:
                    pressedBackground = a.getDrawable(attr);
                    break;
                case R.styleable.EasyListFilterMenu_menuNormalBackground:
                    normalBackground = a.getDrawable(attr);
                    break;
                case R.styleable.EasyListFilterMenu_menuList1ItemPressedBackground:
                    list1ItempressedBackground = a.getDrawable(attr);
                    break;
                case R.styleable.EasyListFilterMenu_menuList1ItemNormalBackground:
                    list1ItemnormalBackground = a.getDrawable(attr);
                    break;
                case R.styleable.EasyListFilterMenu_menuList2ItemPressedBackground:
                    list2ItempressedBackground = a.getDrawable(attr);
                    break;
                case R.styleable.EasyListFilterMenu_menuList2ItemNormalBackground:
                    list2ItemnormalBackground = a.getDrawable(attr);
                    break;
                case R.styleable.EasyListFilterMenu_menuList3ItemPressedBackground:
                    list3ItempressedBackground = a.getDrawable(attr);
                    break;
                case R.styleable.EasyListFilterMenu_menuList3ItemNormalBackground:
                    list3ItemnormalBackground = a.getDrawable(attr);
                    break;
                case R.styleable.EasyListFilterMenu_menuTextSize:
                    textSize = a.getDimensionPixelSize(attr, textSize);
                    break;
                case R.styleable.EasyListFilterMenu_menuDrawableRight:
                    drawableRight = a.getDrawable(attr);
                    break;

            }
        }


        a.recycle();

        final View filter = View.inflate(getContext(), R.layout.pop_filter, null);

        final View childLayout = filter.findViewById(R.id.childlayout);
        childLayout.setVisibility(View.GONE);

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
        filterAdapter_List1 = new ListFilterAdapter(getContext(), SeletorUtils.creatSelector(list1ItemnormalBackground, list1ItempressedBackground));
        listview_1.setAdapter(filterAdapter_List1);
        listview_1.setItemChecked(0, true);
        //mindle listview
        final ListView listview_2 = (ListView) filter.findViewById(R.id.list_2);
        listview_2.setBackgroundColor(getResources().getColor(android.R.color.white));
        listview_2.setSelector(new ColorDrawable(Color.TRANSPARENT));
        listview_2.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        listview_2.setTextFilterEnabled(true);
        listview_2.setVisibility(View.GONE);
        filterAdapter_list2 = new ListFilterAdapter(getContext(), SeletorUtils.creatSelector(list2ItemnormalBackground, list2ItempressedBackground));
        listview_2.setAdapter(filterAdapter_list2);
        //right listview
        final ListView listview_3 = (ListView) filter.findViewById(R.id.list_3);
        listview_3.setBackgroundColor(getResources().getColor(android.R.color.white));
        listview_3.setSelector(new ColorDrawable(Color.TRANSPARENT));
//        rightListView.setOnItemClickListener(this);
        listview_3.setTextFilterEnabled(true);
        listview_3.setVisibility(View.GONE);
        filterAdapter_list3 = new ListFilterAdapter(getContext(), SeletorUtils.creatSelector(list3ItemnormalBackground, list3ItempressedBackground));
        listview_3.setAdapter(filterAdapter_list3);

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
                boolean nextListIsVisible = listview_2.getVisibility() != View.VISIBLE;
                if (clickIsHasChosen && nextListIsVisible) {
                    return;
                }
//                Toast.makeText(getContext(), "selectedItemId=" + "----id=" + id, Toast.LENGTH_SHORT).show();
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
                        if (listener != null) {
                            listener.onClick(iEasyItem);
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
                boolean nextListIsVisible = listview_3.getVisibility() != View.VISIBLE;
                if (clickIsHasChosen && nextListIsVisible) {
                    return;
                }
                IEasyItem iEasyItem = filterAdapter_list2.getItem(position);
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
                        if (listener != null) {
                            listener.onClick(iEasyItem);
                        }
                        changMenuText(iEasyItem);
                    }
                }
            }
        });
        listview_3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IEasyItem iEasyItem = filterAdapter_list3.getItem(position);
                if (listener != null) {
                    listener.onClick(iEasyItem);
                }
                changMenuText(iEasyItem);
            }
        });


        initView(getContext());

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
     * @param name    titleName
     */
    private void initView(Context context) {
        View view = View.inflate(context, R.layout.listfilter, this);
        mScreeningText = (TextView) view.findViewById(R.id.tv_screening);
        mScreeningText.setOnClickListener(this);
//        mScreeningText.setText(name);
    }

    private void addList1Items(List<? extends IEasyItem> iEasyItems) {
        filterAdapter_List1.setmItems(iEasyItems);
    }

    private void addList2Items(List<? extends IEasyItem> iEasyItems) {
        filterAdapter_list2.setmItems(iEasyItems);
    }

    private void addList3Items(List<? extends IEasyItem> iEasyItems) {
        filterAdapter_list3.setmItems(iEasyItems);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_screening:// 点击menu影藏
                showListFilter();
                break;
        }
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
            return;
        }
        if (pupupWindow != null && !pupupWindow.isShowing()) {
            pupupWindow.showAsDropDown(mScreeningText);
            mScreeningText.setSelected(true);
        }
    }

    private void dismiss() {
        if (pupupWindow != null && pupupWindow.isShowing()) {
            pupupWindow.dismiss();
        }
    }

    OnMenuItemClickListener listener;

    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnMenuItemClickListener<T extends IEasyItem> {
        void onClick(T t);
    }
}
