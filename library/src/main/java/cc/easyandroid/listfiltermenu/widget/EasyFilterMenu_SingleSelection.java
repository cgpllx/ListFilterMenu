package cc.easyandroid.listfiltermenu.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cc.easyandroid.listfiltermenu.R;
import cc.easyandroid.listfiltermenu.core.EasyUtils;
import cc.easyandroid.listfiltermenu.core.IEasyItem;
import cc.easyandroid.listfiltermenu.core.IEasyItemFactory;
import cc.easyandroid.listfiltermenu.core.ListFilterAdapter;
import cc.easyandroid.listfiltermenu.core.OnMenuListItemClickListener;

/**
 * 最多3个列表的单选
 */
public class EasyFilterMenu_SingleSelection extends EasyFilterMenu {

    private CharSequence currentMenuText;//记录第二个列表点击不限时候要现实的title
    private SparseBooleanArray hasAddUnlimitedContainer = new SparseBooleanArray();
    private ListView mListView1;
    private ListView mListView2;
    private ListView mListView3;
    private int mShowUnlimiteds;//哪几个list 显示

    //不限的显示位置 SHOW_LIST_1 第一个list
    public static final int SHOW_LIST_NONE = 0;

    public static final int SHOW_LIST_1 = 1;

    public static final int SHOW_LIST_2 = 1 << 1;

    public static final int SHOW_LIST_3 = 1 << 2;

    private CharSequence unlimitedTermDisplayName;//不限制，默认名称

    /**
     * 有时候list 会有比较复杂的布局，用这个装在里面（list1要一直现实，list1就不用了，不影响效果）
     */
    private View list2Box;//装list1的盒子
    private View list3Box;//装list1的盒子

    public EasyFilterMenu_SingleSelection(Context context) {
        this(context, null);
    }

    public EasyFilterMenu_SingleSelection(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.EasyFilterMenuStyle);
    }

    public EasyFilterMenu_SingleSelection(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private static final int LISTFILTERMENU_LISTITEM_LAYOUT = R.layout.menu_listitem_layout;//list item 的layout

    private void init(Context context, AttributeSet attrs, int defStyle) {

        int list1ItemViewResourceId = LISTFILTERMENU_LISTITEM_LAYOUT;
        int list2ItemViewResourceId = LISTFILTERMENU_LISTITEM_LAYOUT;
        int list3ItemViewResourceId = LISTFILTERMENU_LISTITEM_LAYOUT;

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EasyFilterMenu_SingleSelection, defStyle, 0);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.EasyFilterMenu_SingleSelection_menuList1ItemLayout) {//第1个列表item  的资源id
                list1ItemViewResourceId = a.getResourceId(attr, LISTFILTERMENU_LISTITEM_LAYOUT);
            } else if (attr == R.styleable.EasyFilterMenu_SingleSelection_menuList2ItemLayout) {//第2个列表item  的资源id
                list2ItemViewResourceId = a.getResourceId(attr, LISTFILTERMENU_LISTITEM_LAYOUT);
            } else if (attr == R.styleable.EasyFilterMenu_SingleSelection_menuList3ItemLayout) {//第3个列表item  的资源id
                list3ItemViewResourceId = a.getResourceId(attr, LISTFILTERMENU_LISTITEM_LAYOUT);
            } else if (attr == R.styleable.EasyFilterMenu_SingleSelection_unlimitedText) {
                unlimitedTermDisplayName = a.getText(attr);
            } else if (attr == R.styleable.EasyFilterMenu_SingleSelection_showUnlimiteds) {
                mShowUnlimiteds = a.getInt(attr, SHOW_LIST_NONE);
            }
        }
        a.recycle();

        if (mListView1 != null) {
            ListFilterAdapter<? extends IEasyItem> adapter_List1 = new ListFilterAdapter<>(context, list1ItemViewResourceId);
            setList1Adapter(adapter_List1);
        }
        if (mListView2 != null) {
            ListFilterAdapter<? extends IEasyItem> adapter_List2 = new ListFilterAdapter<>(context, list2ItemViewResourceId);
            setList2Adapter(adapter_List2);
        }
        if (mListView3 != null) {
            ListFilterAdapter<? extends IEasyItem> adapter_List3 = new ListFilterAdapter<>(context, list3ItemViewResourceId);
            setList3Adapter(adapter_List3);
        }

    }

    @Override
    protected void onMenuContentViewCreated(ViewGroup menuContentView, EasyFilterMenu easyFilterMenu) {
        //listview--1
        ListView listview_1 = (ListView) menuContentView.findViewById(R.id.easyListFilter_MenuContent_List_1);
        mListView1 = listview_1;
        setupListView(listview_1);
        listview_1.setVisibility(View.VISIBLE);
        listview_1.setItemChecked(0, true);
        setupCustomView(menuContentView, R.id.easyListFilter_CustomViewConfirm_List1, this, listview_1);//设置带有确定按钮的view

        //listview--2
        ListView listview_2 = (ListView) menuContentView.findViewById(R.id.easyListFilter_MenuContent_List_2);
        if (listview_2 != null) {
            list2Box = menuContentView.findViewById(R.id.easyListFilter_MenuContent_List_2Box);
            mListView2 = listview_2;
            setupListView(listview_2);
            EasyUtils.hideView(list2Box);
            EasyUtils.hideView(listview_2);
            setupCustomView(menuContentView, R.id.easyListFilter_CustomViewConfirm_List2, this, listview_2);//设置带有确定按钮的view
        }

        //listview--3
        ListView listview_3 = (ListView) menuContentView.findViewById(R.id.easyListFilter_MenuContent_List_3);
        if (listview_3 != null) {
            list3Box = menuContentView.findViewById(R.id.easyListFilter_MenuContent_List_3Box);
            mListView3 = listview_3;
            setupListView(listview_3);
            EasyUtils.hideView(mListView3);
            EasyUtils.hideView(listview_3);
            setupCustomView(menuContentView, R.id.easyListFilter_CustomViewConfirm_List3, this, listview_3);//设置带有确定按钮的view
        }

        listview_1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean nextListIsVisible = mListView2 != null ? mListView2.getVisibility() == View.VISIBLE : false;
                if (!clickPositionIsChanged(mListView1, position) && nextListIsVisible) {//下一个listview 是显示的，且当前点击的位置是上次记住的位置
                    return;
                }
                setMenuList1State(position, menuListItemClickListener, true);
                mListView1.setTag(position);//tag记录的就是上一次被点击的位置
            }
        });
        if (listview_2 != null) {
            listview_2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    boolean nextListIsVisible = mListView3 != null ? mListView3.getVisibility() == View.VISIBLE : false;
                    if (!clickPositionIsChanged(mListView2, position) && nextListIsVisible) {//下一个listview 是显示的，且当前点击的位置是上次记住的位置
                        return;
                    }
                    setMenuList2State(position, menuListItemClickListener, true);
                    mListView2.setTag(position);
                }
            });
        }
        if (listview_3 != null) {
            listview_3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    setMenuList3State(position, menuListItemClickListener, true);
                }
            });
        }
    }


    @Override
    protected boolean isEmpty() {
        return mListView1.getAdapter().isEmpty();
    }

    public void setList1Adapter(ListFilterAdapter<? extends IEasyItem> adapter) {
        mListView1.setAdapter(adapter);
    }

    public void setList2Adapter(ListFilterAdapter<? extends IEasyItem> adapter) {
        mListView2.setAdapter(adapter);
    }

    public void setList3Adapter(ListFilterAdapter<? extends IEasyItem> adapter) {
        mListView3.setAdapter(adapter);
    }


    void setupListView(ListView listView) {
        listView.setBackgroundColor(getResources().getColor(android.R.color.white));
        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listView.setTextFilterEnabled(true);
    }

    @Override
    protected void onShowMenuContent() {
        super.onShowMenuContent();
        ListFilterAdapter listFilterAdapter = (ListFilterAdapter) mListView1.getAdapter();
        setMenuList1State(listFilterAdapter.getParentIEasyItem().getChildSelectPosion(), null, false);//pop显示的时候去检查看是要现实哪一个
    }

    /**
     * 设置第一个列表的选中项
     *
     * @param position 位置
     */
    public void setMenuList1State(int position, OnMenuListItemClickListener menuListItemClickListener, boolean fromUserClick) {

        ListFilterAdapter<? extends IEasyItem> listFilterAdapter = (ListFilterAdapter) mListView1.getAdapter();
        IEasyItem iEasyItem = listFilterAdapter.getItem(position);
        mListView1.setItemChecked(position, true);//标记选中项
        rememberPosion(listFilterAdapter, position, fromUserClick);//让父 记住被选中的子的位置
        if (iEasyItem != null) {
            List<? extends IEasyItem> mindleItems = iEasyItem.getChildItems();//如果child不是null，就吧第二个现实出来
            if (mindleItems != null && mindleItems.size() > 0) {
                currentMenuText = iEasyItem.getDisplayName();//记住当前被点击的item的显示的名称
                addList2Items(iEasyItem);//传的是父类的IEasyItem ，适配器自己去里面找

                EasyUtils.showView(mListView2);
                EasyUtils.showView(list2Box);

                EasyUtils.hideView(mListView3);
                EasyUtils.hideView(list3Box);
                setMenuList2State(iEasyItem.getChildSelectPosion(), menuListItemClickListener, false);
            } else {
                ((ListFilterAdapter) mListView1.getAdapter()).clearAllChildPosion(); // 点击lise1中的不限制，清除list1中记录的childselected 记录，
                EasyUtils.hideView(mListView2);
                EasyUtils.hideView(list2Box);

                EasyUtils.hideView(mListView3);
                EasyUtils.hideView(list3Box);
                if (iEasyItem.isNoLimitItem()) {
                    setMenuTitle(defultMenuText);
                } else {
                    changMenuText(iEasyItem);
                }
                if (fromUserClick) {
                    menuListItemClick(menuListItemClickListener, iEasyItem);//点击后会关闭pop
                }
            }

        }
    }

    /**
     * 设置第二个列表
     *
     * @param position                  要选择的位置
     * @param menuListItemClickListener 点击回掉
     */
    public void setMenuList2State(int position, OnMenuListItemClickListener menuListItemClickListener, boolean fromUserClick) {
        ListFilterAdapter<? extends IEasyItem> listFilterAdapter = (ListFilterAdapter) mListView2.getAdapter();
        IEasyItem iEasyItem = listFilterAdapter.getItem(position);
        mListView2.setItemChecked(position, true);//标记选中项
        if (fromUserClick) {//防止第一次显示时候执行，主要是在list1的item被点击后
            ((ListFilterAdapter) mListView1.getAdapter()).clearAllChildPosion(); // 清除list1中记录的childposion，
        }

        rememberPosion(listFilterAdapter, position, fromUserClick);//MULTI状态才会记住
        if (iEasyItem != null) {
            List<? extends IEasyItem> rightItems = iEasyItem.getChildItems();
            if (rightItems != null && rightItems.size() > 0) {
                currentMenuText = iEasyItem.getDisplayName();
                addList3Items(iEasyItem);
                EasyUtils.showView(mListView3);
                EasyUtils.showView(list3Box);
                setMenuList3State(iEasyItem.getChildSelectPosion(), menuListItemClickListener, false);
            } else {
                ((ListFilterAdapter) mListView2.getAdapter()).clearAllChildPosion(); // 点击lise1中的不限制，清除list1中记录的childselected 记录
                EasyUtils.hideView(mListView3);
                EasyUtils.hideView(list3Box);
                if (fromUserClick) {
                    changMenuText(iEasyItem);//这里和listview1有区别
                }
                if (fromUserClick) {
                    menuListItemClick(menuListItemClickListener, iEasyItem);//点击后会关闭pop
                }

            }

        }

    }

    /**
     * 设置第三个列表
     *
     * @param position                  要选择的位置
     * @param menuListItemClickListener 点击回掉
     * @param fromUserClick             是否来自用户点击
     */
    public void setMenuList3State(int position, OnMenuListItemClickListener menuListItemClickListener, boolean fromUserClick) {
        ListFilterAdapter<? extends IEasyItem> listFilterAdapter = (ListFilterAdapter) mListView3.getAdapter();
        IEasyItem iEasyItem = listFilterAdapter.getItem(position);
        mListView3.setItemChecked(position, true);//标记选中项
        if (fromUserClick) {
            ((ListFilterAdapter) mListView2.getAdapter()).clearAllChildPosion(); // 清除list1中记录的chil dposion，
        }
        rememberPosion(listFilterAdapter, position, fromUserClick);//MULTI状态才会记住
        if (fromUserClick) {
            changMenuText(iEasyItem);//这里和listview1有区别
        }
        if (fromUserClick) {
            menuListItemClick(menuListItemClickListener, iEasyItem);//点击后会关闭pop
        }


    }

    /**
     * item click
     *
     * @param iEasyItem 被点击的item
     */
    public void menuListItemClick(OnMenuListItemClickListener menuListItemClickListener, IEasyItem iEasyItem) {
        if (menuListItemClickListener != null) {
            menuListItemClickListener.onClick(iEasyItem);
            dismiss();//item 被点击后dismiss pop 最后销毁，
        }

    }

    /**
     * 如果是第一个列表点击不限，就现实默认的，然后是其他，就现实上一层的选中项
     *
     * @param iEasyItem 点击的item
     */
    private void changMenuText(IEasyItem iEasyItem) {
        if (iEasyItem != null) {
            if (iEasyItem.isNoLimitItem()) {
                setMenuTitle(currentMenuText);
            } else {
                CharSequence displayName = iEasyItem.getDisplayName();
                if (!TextUtils.isEmpty(displayName)) {
                    setMenuTitle(displayName);
                } else {
                    setMenuTitle(defultMenuText);
                }
            }
        }
    }


    /**
     * 数据准备好了  直接传送的是父item
     *
     * @param parentIEasyItem
     */
    @Override
    protected void onMenuDataPrepared(IEasyItem parentIEasyItem) {
        addList1Items(parentIEasyItem);//
    }

    @Override
    protected void onMenuDataPrepared(ArrayList<? extends IEasyItem> iEasyItems) {
        addList1Items(IEasyItemFactory.buildIEasyItem(iEasyItems));//创建一个父容器
    }

    /**
     * 添加数据到第一个列表
     *
     * @param parentIEasyItem 父IEasyItem
     */
    private void addList1Items(IEasyItem parentIEasyItem) {
        if ((mShowUnlimiteds & SHOW_LIST_1) != 0) {//是否要添加不限制
            addUnlimitedToContaier(parentIEasyItem);
        }
        ListFilterAdapter listFilterAdapter = (ListFilterAdapter) mListView1.getAdapter();
        listFilterAdapter.setParentIEasyItem(parentIEasyItem);
    }

    /**
     * 添加数据到第二个列表
     *
     * @param parentIEasyItem 父IEasyItem
     */
    private void addList2Items(IEasyItem parentIEasyItem) {
        if ((mShowUnlimiteds & SHOW_LIST_2) != 0) {//是否要添加不限制
            addUnlimitedToContaier(parentIEasyItem);
        }
        ListFilterAdapter listFilterAdapter = (ListFilterAdapter) mListView2.getAdapter();
        listFilterAdapter.setParentIEasyItem(parentIEasyItem);
    }

    /**
     * 添加数据到第三个列表
     *
     * @param parentIEasyItem 父IEasyItem
     */
    private void addList3Items(IEasyItem parentIEasyItem) {
        if ((mShowUnlimiteds & SHOW_LIST_3) != 0) {//是否要添加不限制
            addUnlimitedToContaier(parentIEasyItem);
        }
        ListFilterAdapter listFilterAdapter = (ListFilterAdapter) mListView3.getAdapter();
        listFilterAdapter.setParentIEasyItem(parentIEasyItem);
    }

    /**
     * 添加不限制到容器中，只添加一次
     *
     * @param parentIEasyItem 是他的之类添加的
     */
    void addUnlimitedToContaier(IEasyItem parentIEasyItem) {//hasAddUnlimitedContainer 中存放的是parentIEasyItem的哈希，  hashCode被重写了，解决传递时候数据问题
        if (!TextUtils.isEmpty(unlimitedTermDisplayName) && !hasAddUnlimitedContainer.get(parentIEasyItem.hashCode(), false)) {//检查是否添加过不限，如果没有才添加
            hasAddUnlimitedContainer.put(parentIEasyItem.hashCode(), true);
            List list = parentIEasyItem.getChildItems();//从父容器中取出子容器的第一个，然后把不限制添加进去
            if (list != null && list.size() > 0) {
                IEasyItem iEasyItem = (IEasyItem) list.get(0);

                HashMap<String, String> map = iEasyItem.getEasyParameter();
                IEasyItem unlimitediEasyItem = IEasyItemFactory.buildOneIEasyItem(unlimitedTermDisplayName, map);//创建一个不限
                list.add(0, unlimitediEasyItem);//添加到第一个位置
            }
        }
    }

    /**
     * 检测改listview 的item 是否被选择的
     *
     * @param listView 检查的列表
     * @param position 位置
     * @return 点击位置是否改变
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


    /**
     * 让ParentIEasyItem记住ChildIEasyItem被选中的位置
     *
     * @param adapter  适配器
     * @param position 位置
     */

    private void rememberPosion(ListFilterAdapter<? extends IEasyItem> adapter, int position, boolean fromUserClick) {
        adapter.getParentIEasyItem().setChildSelectPosion(position);
        if (fromUserClick) {
            adapter.getParentIEasyItem().setChildSelected(true);
        }
    }

    public void setMenuStates(SingleSelectionMenuStates menuStates) {
        currentMenuText = menuStates.currentMenuText;
        hasAddUnlimitedContainer = menuStates.hasAddUnlimitedContainer;
        setMenuData(false, menuStates.parentIEasyItem);
    }

    public SingleSelectionMenuStates getMenuStates() {
        ListFilterAdapter listFilterAdapter = (ListFilterAdapter) mListView1.getAdapter();
        IEasyItem parentIEasyItem = listFilterAdapter.getParentIEasyItem();
        SingleSelectionMenuStates singleSelectionMenuStates = new SingleSelectionMenuStates.Builder()//
                .setCurrentMenuText(currentMenuText)//
                .setHasAddUnlimitedContainer(hasAddUnlimitedContainer.clone())//
                .setParentIEasyItem(parentIEasyItem)//
                .build();
        return singleSelectionMenuStates;
    }

    public static class SingleSelectionMenuStates {
        SparseBooleanArray hasAddUnlimitedContainer;
        IEasyItem parentIEasyItem;
        CharSequence currentMenuText;

        private SingleSelectionMenuStates(Builder builder) {
            hasAddUnlimitedContainer = builder.hasAddUnlimitedContainer;
            parentIEasyItem = builder.parentIEasyItem;
            currentMenuText = builder.currentMenuText;
        }


        public static class Builder {
            SparseBooleanArray hasAddUnlimitedContainer;
            IEasyItem parentIEasyItem;
            CharSequence currentMenuText;

            public Builder setCurrentMenuText(CharSequence currentMenuText) {
                this.currentMenuText = currentMenuText;
                return this;
            }

            public Builder setHasAddUnlimitedContainer(SparseBooleanArray hasAddUnlimitedContainer) {
                this.hasAddUnlimitedContainer = hasAddUnlimitedContainer;
                return this;
            }

            public Builder setParentIEasyItem(IEasyItem parentIEasyItem) {
                this.parentIEasyItem = parentIEasyItem;
                return this;
            }

            public SingleSelectionMenuStates build() {
                SingleSelectionMenuStates singleSelectionMenuStates = new SingleSelectionMenuStates(this);
                return singleSelectionMenuStates;
            }
        }
    }
}
