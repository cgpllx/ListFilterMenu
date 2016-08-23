package cc.easyandroid.listfiltermenu.simple;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import cc.easyandroid.listfiltermenu.R;
import cc.easyandroid.listfiltermenu.core.EasyItemManager;
import cc.easyandroid.listfiltermenu.core.EasyMenuStates;
import cc.easyandroid.listfiltermenu.core.IEasyItem;
import cc.easyandroid.listfiltermenu.core.ListFilterAdapter;
import cc.easyandroid.listfiltermenu.widget.EasyFilterMenu;

/**
 * 1点击gradview的item时候，将选择的item记录在list item的临时变量中，
 * 2 点击确认时候，将临时变量中的值赋值到正式变量
 * 3 打开的时候，讲正式变量赋值到临时变量中
 * 4.gridview中被选择的item就是临时变量的值
 *
 */
public class EasyFileterMenuCustom extends EasyFilterMenu {


    private ListView mListView1;

    /**
     * 有时候list 会有比较复杂的布局，用这个装在里面（list1要一直现实，list1就不用了，不影响效果）
     */
    public EasyFileterMenuCustom(Context context) {
        this(context, null);
    }

    public EasyFileterMenuCustom(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.EasyFilterMenuStyle);
    }

    public EasyFileterMenuCustom(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private static final int LISTFILTERMENU_LISTITEM_LAYOUT = R.layout.menu_listitem_layout;//list item 的layout

    private void init(Context context, AttributeSet attrs, int defStyle) {

        int list1ItemViewResourceId = LISTFILTERMENU_LISTITEM_LAYOUT;

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EasyFileterMenuMore, defStyle, 0);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.EasyFileterMenuMore_menuList1ItemLayout) {//第1个列表item  的资源id
                list1ItemViewResourceId = a.getResourceId(attr, LISTFILTERMENU_LISTITEM_LAYOUT);
            }
        }
        a.recycle();

        if (mListView1 != null) {
            ListFilterAdapter adapter_List1 = new ListFilterAdapter(context, list1ItemViewResourceId);
            setList1Adapter(adapter_List1);
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
        //TODO
//        setupCustomView(menuContentView, R.id.easyListFilter_CustomViewConfirm_List1, this, listview_1);//设置带有确定按钮的view



    }


    @Override
    protected boolean isEmpty() {
        return mListView1.getAdapter().isEmpty();
    }

    public void setList1Adapter(ListFilterAdapter adapter) {
        mListView1.setAdapter(adapter);
    }



    void setupListView(ListView listView) {
        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        listView.setChoiceMode(AbsListView.CHOICE_MODE_NONE);
        listView.setTextFilterEnabled(true);
    }

    @Override
    protected void onShowMenuContent() {
        super.onShowMenuContent();
        ListFilterAdapter listFilterAdapter = (ListFilterAdapter) mListView1.getAdapter();
        setMenuList1State(listFilterAdapter.getEasyItemManager().getChildSelectPosion(), false);//pop显示的时候去检查看是要现实哪一个
    }


    /**
     * 设置第一个列表的选中项
     *
     * @param position 位置
     */
    public void setMenuList1State(int position, boolean fromUserClick) {

        ListFilterAdapter listFilterAdapter = (ListFilterAdapter) mListView1.getAdapter();
        IEasyItem iEasyItem = listFilterAdapter.getItem(position);
        mListView1.setItemChecked(position, true);//标记选中项
        mListView1.setTag(position);//tag记录的就是上一次被点击的位置
        rememberPosion(listFilterAdapter, position, fromUserClick);//让父 记住被选中的子的位置
        if (iEasyItem != null) {
            EasyItemManager easyItemManager = iEasyItem.getEasyItemManager();//z这个是被点击的easyItemManager
            if (easyItemManager.isHasEasyItems()) {
            } else {
                if (fromUserClick) {
                    menuListItemClick(iEasyItem);//点击后会关闭pop
                }
            }

        }
    }


    //Save state 保存状态
    public void saveStates() {
        final SparseBooleanArray menuStatesArray = mListView1.getCheckedItemPositions();
//        mMenuStatesArray = menuStatesArray.clone();
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
        ListFilterAdapter listFilterAdapter = (ListFilterAdapter) mListView1.getAdapter();
        listFilterAdapter.setEasyItemManager(easyItemManager);
    }



    /**
     * 让ParentIEasyItem记住ChildIEasyItem被选中的位置
     *
     * @param adapter  适配器
     * @param position 位置
     */
    private void rememberPosion(ListFilterAdapter adapter, int position, boolean fromUserClick) {
        EasyItemManager easyItemManager = adapter.getEasyItemManager();
        if (easyItemManager != null) {
            easyItemManager.setChildSelectPosion(position);
            if (fromUserClick) {
                easyItemManager.setChildSelected(true);
            }
        }
    }


    @Override
    protected void onCleanMenuStatus() {
        ListFilterAdapter listFilterAdapter = (ListFilterAdapter) mListView1.getAdapter();
        listFilterAdapter.clearAllChildPosion();
        setMenuList1State(0, false);
    }

    @Override
    public EasyItemManager getMenuData() {
        ListFilterAdapter listFilterAdapter = (ListFilterAdapter) mListView1.getAdapter();
        EasyItemManager easyItemManager = listFilterAdapter.getEasyItemManager();
        return easyItemManager;
    }

    protected EasyMenuStates onCreateMenuStates(EasyItemManager easyItemManager) {
        return new EasyMenuStates.Builder()//
                .setEasyItemManager(easyItemManager)
                .setEasyMenuParas(getEasyMenuParas())
                .setMenuTitle(getMenuTitle())
                .build();
    }
}
