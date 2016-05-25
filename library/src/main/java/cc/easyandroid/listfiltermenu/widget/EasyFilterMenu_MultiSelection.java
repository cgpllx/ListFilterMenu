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
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.HashMap;
import java.util.List;

import cc.easyandroid.listfiltermenu.R;
import cc.easyandroid.listfiltermenu.core.EasyItemManager;
import cc.easyandroid.listfiltermenu.core.IEasyItem;
import cc.easyandroid.listfiltermenu.core.IEasyItemFactory;
import cc.easyandroid.listfiltermenu.core.ListFilterAdapter;
import cc.easyandroid.listfiltermenu.core.SingleSelectionMenuStates;

/**
 * 单列表的多项选择
 */
public class EasyFilterMenu_MultiSelection extends EasyFilterMenu {
    private ListView mListView1;
    private SparseBooleanArray mMenuStatesArray;//保存被选中的状态的
    private CharSequence unlimitedTermDisplayName;//不限制，默认名称  只要不为空就显示


    public EasyFilterMenu_MultiSelection(Context context) {
        this(context, null);
    }

    public EasyFilterMenu_MultiSelection(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.EasyFilterMenuStyle);
    }

    public EasyFilterMenu_MultiSelection(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private static final int LISTFILTERMENU_LISTITEM_LAYOUT = R.layout.menu_listitem_layout;//list item 的layout

    private void init(Context context, AttributeSet attrs, int defStyle) {

        int list1ItemViewResourceId = LISTFILTERMENU_LISTITEM_LAYOUT;

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EasyFilterMenu_MultiSelection, defStyle, 0);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.EasyFilterMenu_MultiSelection_menuList1ItemLayout) {//第1个列表item  的资源id
                list1ItemViewResourceId = a.getResourceId(attr, LISTFILTERMENU_LISTITEM_LAYOUT);
            } else if (attr == R.styleable.EasyFilterMenu_MultiSelection_unlimitedText) {
                unlimitedTermDisplayName = a.getText(attr);
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
        setupCustomView(menuContentView, R.id.easyListFilter_CustomViewConfirm_List1, this, listview_1);//设置带有确定按钮的view

        listview_1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                handleMultipleChoiceMode(mListView1, position);
            }
        });

    }

    /**
     * 数据准备好了  直接传送的是父item
     *
     * @param easyItemManager 数据
     */
    @Override
    protected void onMenuDataPrepared(EasyItemManager easyItemManager) {
        super.onMenuDataPrepared(easyItemManager);
        addList1Items(easyItemManager);//创建一个父容器
    }

    void handleMultipleChoiceMode(ListView listView, int position) {
        ListFilterAdapter listFilterAdapter = (ListFilterAdapter) mListView1.getAdapter();
        IEasyItem iEasyItem = listFilterAdapter.getItem(position);
        SparseBooleanArray booleanArray = listView.getCheckedItemPositions();
        if (iEasyItem == null || iEasyItem.getEasyItemManager().isNoLimitItem()) {//为null 或者是不限制
            listView.clearChoices();
            listView.setItemChecked(0, true);
            BaseAdapter adapter = (BaseAdapter) listView.getAdapter();
            adapter.notifyDataSetChanged();
            setMenuTitle(defultMenuText);
        } else {
            if (booleanArray.indexOfValue(true) != -1) {//检测是否有选中项，如果有就讲第一个的选中状态改变
                listView.setItemChecked(0, false);
            } else {
                listView.setItemChecked(0, true);
            }
        }
    }

    @Override
    protected boolean isEmpty() {
        return mListView1.getAdapter().isEmpty();
    }

    public void setList1Adapter(ListFilterAdapter adapter) {
        mListView1.setAdapter(adapter);
    }


    private void setupListView(ListView listView) {
        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setTextFilterEnabled(true);
    }


    @Override
    protected void onShowMenuContent() {
        super.onShowMenuContent();
        mListView1.clearChoices();
        if (mMenuStatesArray != null) {
            for (int i = 0; i < mMenuStatesArray.size(); i++) {
                int posion = mMenuStatesArray.keyAt(i);
                boolean value = mMenuStatesArray.valueAt(i);
                mListView1.setItemChecked(posion, value);
            }
        } else {
            mListView1.setItemChecked(0, true);
        }
    }

    /**
     * 添加数据到第一个列表
     *
     * @param easyItemManager 父IEasyItem
     */
    protected void addList1Items(EasyItemManager easyItemManager) {
        addUnlimitedToContaier(easyItemManager);//此方法检测unlimitedTermDisplayName是否是空，只要不是空就添加

        ListFilterAdapter listFilterAdapter = (ListFilterAdapter) mListView1.getAdapter();
        listFilterAdapter.setEasyItemManager(easyItemManager);
    }
    @Override
    public EasyItemManager getMenuData() {
        ListFilterAdapter listFilterAdapter = (ListFilterAdapter) mListView1.getAdapter();
        EasyItemManager easyItemManager = listFilterAdapter.getEasyItemManager();
        return easyItemManager;
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

    //Save state
    public void saveStates() {
        final SparseBooleanArray menuStatesArray = mListView1.getCheckedItemPositions();
        mMenuStatesArray = menuStatesArray.clone();
    }

    @Override
    protected void onDismissMenuContent() {
        super.onDismissMenuContent();
        saveStates();
    }

    public void setMenuStates(SingleSelectionMenuStates menuStates) {
//        tempMenuTitle = menuStates.getTempMenuTitle();
        mMenuStatesArray=menuStates.getMenuStatesArray();
        setMenuData(false, menuStates.getEasyItemManager());
        setMenuTitle(menuStates.getMenuTitle());
    }

    public SingleSelectionMenuStates getMenuStates() {
        ListFilterAdapter listFilterAdapter = (ListFilterAdapter) mListView1.getAdapter();
        EasyItemManager easyItemManager = listFilterAdapter.getEasyItemManager();
        if(easyItemManager==null){
            return  null;
        }
        return new SingleSelectionMenuStates.Builder()//
//                .setTempMenuTitle(tempMenuTitle)//
                .setMenuStatesArray(mMenuStatesArray)
                .setEasyItemManager(easyItemManager)
                .setMenuTitle(getMenuTitle())
                .build();
    }
}
