package cc.easyandroid.listfiltermenu.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.HashMap;
import java.util.List;

import cc.easyandroid.listfiltermenu.R;
import cc.easyandroid.listfiltermenu.core.EasyItemManager;
import cc.easyandroid.listfiltermenu.core.EasyUtils;
import cc.easyandroid.listfiltermenu.core.IEasyItem;
import cc.easyandroid.listfiltermenu.core.IEasyItemFactory;
import cc.easyandroid.listfiltermenu.core.ListFilterAdapter;
import cc.easyandroid.listfiltermenu.core.OnMenuListItemClickListener;
import cc.easyandroid.listfiltermenu.core.SingleSelectionMenuStates;

/**
 * 多个折叠的单选 list2 点击不限制，要清空list1中被选择的状态，这个是和EasyFilterMenu_SingleSelection的差别
 */
public class EasyFileterMenu_MoreSelection extends EasyFilterMenu {

    private ListView mListView1;
    private ListView mListView2;
    private int mShowUnlimiteds;//哪几个list 显示
//    SparseArray<CharSequence> multiTitles = new SparseArray<>();//多选择时候，记住标题的容器
    ArrayMap<Integer ,String> multiTitles=new ArrayMap<>();
    //不限的显示位置 SHOW_LIST_1 第一个list
    public static final int SHOW_LIST_NONE = 0;

    public static final int SHOW_LIST_1 = 1;

    public static final int SHOW_LIST_2 = 1 << 1;

    private CharSequence unlimitedTermDisplayName;//不限制，默认名称

    /**
     * 有时候list 会有比较复杂的布局，用这个装在里面（list1要一直现实，list1就不用了，不影响效果）
     */
    private View list2Box;//装list2的盒子  根据情况选择

    public EasyFileterMenu_MoreSelection(Context context) {
        this(context, null);
    }

    public EasyFileterMenu_MoreSelection(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.EasyFilterMenuStyle);
    }

    public EasyFileterMenu_MoreSelection(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private static final int LISTFILTERMENU_LISTITEM_LAYOUT = R.layout.menu_listitem_layout;//list item 的layout

    private void init(Context context, AttributeSet attrs, int defStyle) {

        int list1ItemViewResourceId = LISTFILTERMENU_LISTITEM_LAYOUT;
        int list2ItemViewResourceId = LISTFILTERMENU_LISTITEM_LAYOUT;

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EasyFileterMenu_MoreSelection, defStyle, 0);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.EasyFileterMenu_MoreSelection_menuList1ItemLayout) {//第1个列表item  的资源id
                list1ItemViewResourceId = a.getResourceId(attr, LISTFILTERMENU_LISTITEM_LAYOUT);
            } else if (attr == R.styleable.EasyFileterMenu_MoreSelection_menuList2ItemLayout) {//第2个列表item  的资源id
                list2ItemViewResourceId = a.getResourceId(attr, LISTFILTERMENU_LISTITEM_LAYOUT);
            } else if (attr == R.styleable.EasyFileterMenu_MoreSelection_unlimitedText) {
                unlimitedTermDisplayName = a.getText(attr);
            } else if (attr == R.styleable.EasyFileterMenu_MoreSelection_showUnlimiteds) {
                mShowUnlimiteds = a.getInt(attr, SHOW_LIST_NONE);
            }
        }
        a.recycle();

        if (mListView1 != null) {
            ListFilterAdapter adapter_List1 = new ListFilterAdapter(context, list1ItemViewResourceId);
            setList1Adapter(adapter_List1);
        }
        if (mListView2 != null) {
            ListFilterAdapter adapter_List2 = new ListFilterAdapter(context, list2ItemViewResourceId);
            setList2Adapter(adapter_List2);
        }

        buildDefaultMultiMenuTitleFormat();

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

        listview_1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean nextListIsVisible = mListView2 != null ? mListView2.getVisibility() == View.VISIBLE : false;
                if (!clickPositionIsChanged(mListView1, position) && nextListIsVisible) {//下一个listview 是显示的，且当前点击的位置是上次记住的位置
                    return;
                }
                setMenuList1State(position, menuListItemClickListener, true);
//                mListView1.setTag(position);//tag记录的就是上一次被点击的位置
            }
        });
        if (listview_2 != null) {
            listview_2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    setMenuList2State(position, menuListItemClickListener, true);
                }
            });
        }

    }


    @Override
    protected boolean isEmpty() {
        return mListView1.getAdapter().isEmpty();
    }

    public void setList1Adapter(ListFilterAdapter adapter) {
        mListView1.setAdapter(adapter);
    }

    public void setList2Adapter(ListFilterAdapter adapter) {
        mListView2.setAdapter(adapter);
    }


    void setupListView(ListView listView) {
        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listView.setTextFilterEnabled(true);
    }

    @Override
    protected void onShowMenuContent() {
        super.onShowMenuContent();
        ListFilterAdapter listFilterAdapter = (ListFilterAdapter) mListView1.getAdapter();
        setMenuList1State(listFilterAdapter.getEasyItemManager().getChildSelectPosion(), null, false);//pop显示的时候去检查看是要现实哪一个
    }



    /**
     * 设置第一个列表的选中项
     *
     * @param position 位置
     */
    public void setMenuList1State(int position, OnMenuListItemClickListener menuListItemClickListener, boolean fromUserClick) {

        ListFilterAdapter listFilterAdapter = (ListFilterAdapter) mListView1.getAdapter();
        IEasyItem iEasyItem = listFilterAdapter.getItem(position);
        mListView1.setItemChecked(position, true);//标记选中项
        mListView1.setTag(position);//tag记录的就是上一次被点击的位置
        rememberPosion(listFilterAdapter, position, fromUserClick);//让父 记住被选中的子的位置
        if (iEasyItem != null) {
            EasyItemManager easyItemManager = iEasyItem.getEasyItemManager();//z这个是被点击的easyItemManager
            if (easyItemManager.isHasEasyItems()) {
                addList2Items(easyItemManager);//传的是父类的IEasyItem ，适配器自己去里面找

                EasyUtils.showView(mListView2);
                EasyUtils.showView(list2Box);

                setMenuList2State(easyItemManager.getChildSelectPosion(), menuListItemClickListener, false);
            } else {
                EasyUtils.hideView(mListView2);
                EasyUtils.hideView(list2Box);

                if (easyItemManager.isNoLimitItem()) {// 如果是不限制
                    setMenuTitle(defultMenuText);
                    ((ListFilterAdapter) mListView1.getAdapter()).clearAllChildPosion(); // 点击lise1中的不限制，清除list1中记录的childselected 记录，
                    multiTitles.clear();
                } else {//一般不会到这里 TODO 如果这里是不限制
//                    changMultiMenuText(iEasyItem, mListView1);
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
        ListFilterAdapter listFilterAdapter = (ListFilterAdapter) mListView2.getAdapter();
        IEasyItem iEasyItem = listFilterAdapter.getItem(position);
        mListView2.setItemChecked(position, true);//标记选中项

        if (fromUserClick) {//fromUserClick 防止第一次显示时候执行，主要是在list1的item被点击后
            if (iEasyItem.getEasyItemManager().isNoLimitItem()) {
                listFilterAdapter.getEasyItemManager().setChildSelected(false);
                listFilterAdapter.getEasyItemManager().setChildSelectPosion(0);
                //和 EasyFilterMenu_SingleSelection的主要差别((ListFilterAdapter) mListView1.getAdapter()).clearAllChildPosion(); // 清除list1中记录的childposion，
            } else {
                listFilterAdapter.getEasyItemManager().setChildSelected(true);
                listFilterAdapter.getEasyItemManager().setChildSelectPosion(position);
            }

            changMultiMenuText(iEasyItem);

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
     * 数据准备好了  直接传送的是父item
     *
     * @param easyItemManager
     */
    @Override
    protected void onMenuDataPrepared(EasyItemManager easyItemManager) {
        addList1Items(easyItemManager);//
    }


    /**
     * 添加数据到第一个列表
     *
     * @param easyItemManager 父IEasyItem
     */
    private void addList1Items(EasyItemManager easyItemManager) {
        if ((mShowUnlimiteds & SHOW_LIST_1) != 0) {//是否要添加不限制
            addUnlimitedToContaier(easyItemManager);
        }
        ListFilterAdapter listFilterAdapter = (ListFilterAdapter) mListView1.getAdapter();
        listFilterAdapter.setEasyItemManager(easyItemManager);
    }

    /**
     * 添加数据到第二个列表
     *
     * @param parentIEasyItem 父IEasyItem
     */
    private void addList2Items(EasyItemManager parentIEasyItem) {
        if ((mShowUnlimiteds & SHOW_LIST_2) != 0) {//是否要添加不限制
            addUnlimitedToContaier(parentIEasyItem);
        }
        ListFilterAdapter listFilterAdapter = (ListFilterAdapter) mListView2.getAdapter();
        listFilterAdapter.setEasyItemManager(parentIEasyItem);
    }


    /**
     * 添加不限制到容器中，只添加一次
     *
     * @param easyItemManager 是他的之类添加的
     */
    void addUnlimitedToContaier(EasyItemManager easyItemManager) {//hasAddUnlimitedContainer 中存放的是parentIEasyItem的哈希，  hashCode被重写了，解决传递时候数据问题
        if (!TextUtils.isEmpty(unlimitedTermDisplayName) && !easyItemManager.isHasAddUnlimited()) {//检查是否添加过不限，如果没有才添加
            easyItemManager.setHasAddUnlimited(true);
            List list = easyItemManager.getEasyItems();//从父容器中取出子容器的第一个，然后把不限制添加进去
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

    private void rememberPosion(ListFilterAdapter adapter, int position, boolean fromUserClick) {
        adapter.getEasyItemManager().setChildSelectPosion(position);
        if (fromUserClick) {
            adapter.getEasyItemManager().setChildSelected(true);
        }
    }


    /**
     * list2中的item被点击后改变title
     *
     * @param iEasyItem 被点击相应的对象
     */
    private void changMultiMenuText(IEasyItem iEasyItem) {
        int index=  mListView1.getCheckedItemPosition();
        if (!iEasyItem.getEasyItemManager().isNoLimitItem()) {//TODO 这里有问题
            multiTitles.put(index, iEasyItem.getDisplayName().toString());// index 第一个列表被点击的位置  被选中的name 添加到里面
//            multiTitles.put()
        } else {
//            multiTitles.remove()
            multiTitles.remove(index);//和上面相反
        }
        if (onMultiMenuTitleFormat != null) {
            onMultiMenuTitleFormat.format(this, multiTitles);
        }
    }

    private void buildDefaultMultiMenuTitleFormat() {
        onMultiMenuTitleFormat = new OnMultiMenuTitleFormat() {
            @Override
            public void format(EasyFileterMenu_MoreSelection easyFileterMenu_moreSelection, ArrayMap<Integer,String> multiTitles) {
                onMenuTitleChanged(easyFileterMenu_moreSelection, multiTitles);
            }
        };
    }

    /**
     * 多选情况下title的样式
     *
     * @param multiTitles 装有所有被选择的title集合
     */
    protected void onMenuTitleChanged(EasyFileterMenu_MoreSelection easyListFilterMenu, ArrayMap<Integer,String> multiTitles) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < multiTitles.size(); i++) {
            stringBuilder.append(multiTitles.valueAt(i));
            stringBuilder.append("|");
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
        }
        if (!TextUtils.isEmpty(stringBuilder.toString())) {
            easyListFilterMenu.setMenuTitle(stringBuilder.toString());
        } else {
            easyListFilterMenu.setMenuTitle(defultMenuText);
        }

    }

    public interface OnMultiMenuTitleFormat {
        void format(EasyFileterMenu_MoreSelection easyFileterMenu_moreSelection, ArrayMap<Integer ,String>   multiTitles);
    }

    private OnMultiMenuTitleFormat onMultiMenuTitleFormat;

    public void setOnMultiMenuTitleFormat(OnMultiMenuTitleFormat onMultiMenuTitleFormat) {
        this.onMultiMenuTitleFormat = onMultiMenuTitleFormat;
    }

    @Override
    public void setMenuStates(SingleSelectionMenuStates menuStates) {
        multiTitles=menuStates.getMultiTitles();
        super.setMenuStates(menuStates);

    }
    @Override
    public EasyItemManager getMenuData() {
        ListFilterAdapter listFilterAdapter = (ListFilterAdapter) mListView1.getAdapter();
        EasyItemManager easyItemManager = listFilterAdapter.getEasyItemManager();
        return easyItemManager;
    }

    public SingleSelectionMenuStates getMenuStates() {
//        SparseArray<CharSequence> multiTitles
        ListFilterAdapter listFilterAdapter = (ListFilterAdapter) mListView1.getAdapter();
        EasyItemManager easyItemManager = listFilterAdapter.getEasyItemManager();
        if(easyItemManager==null){
            return  null;
        }

        return new SingleSelectionMenuStates.Builder()//
//                .setTempMenuTitle(tempMenuTitle)//
                .setMultiTitles(multiTitles)
                .setEasyItemManager(easyItemManager)
                .setMenuTitle(getMenuTitle())
                .build();
    }


}
