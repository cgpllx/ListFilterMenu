package cc.easyandroid.listfiltermenu.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import cc.easyandroid.listfiltermenu.R;
import cc.easyandroid.listfiltermenu.adapter.ChildListFilterAdapter;
import cc.easyandroid.listfiltermenu.adapter.ParentListFilterAdapter;
import cc.easyandroid.listfiltermenu.pojo.IEasyItem;


public class EasyListFilterMenu extends LinearLayout implements View.OnClickListener, AdapterView.OnItemClickListener {
    protected PopupWindow pupupWindow;

    ChildListFilterAdapter<IEasyItem> childFilterAdapter;
    ParentListFilterAdapter<IEasyItem> parentFilterAdapter;

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
        String menuTitle = null;
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.EasyListFilterMenu, defStyle, 0);
        int menuTitlesId = a.getResourceId(R.styleable.EasyListFilterMenu_menuTitle, 0);
        if (menuTitlesId == 0) {
            menuTitle = a.getString(R.styleable.EasyListFilterMenu_menuTitle);
        } else {
            menuTitle = getResources().getString(menuTitlesId);
        }
        a.recycle();

        View filter = View.inflate(getContext(), R.layout.pop_filter, null);

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
        ListView mParentListView = (ListView) filter.findViewById(R.id.list1);
        mParentListView.setBackgroundColor(getResources().getColor(android.R.color.white));
        mParentListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mParentListView.setVisibility(View.VISIBLE);

        mParentListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        mParentListView.setItemChecked(0, true);
        parentFilterAdapter = new ParentListFilterAdapter(getContext());
        mParentListView.setAdapter(parentFilterAdapter);

        //子 listview
        final ListView mChildListView = (ListView) filter.findViewById(R.id.list2);
        mChildListView.setBackgroundColor(getResources().getColor(android.R.color.white));
        mChildListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mChildListView.setOnItemClickListener(this);
        mChildListView.setTextFilterEnabled(true);
        mChildListView.setVisibility(View.GONE);
        childFilterAdapter = new ChildListFilterAdapter(getContext());
        mChildListView.setAdapter(childFilterAdapter);

        mParentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IEasyItem iEasyItem = parentFilterAdapter.getItem(position);
                System.out.println("iEasyItem" + iEasyItem);
                if (iEasyItem != null) {
                    List<? extends IEasyItem> childItems = iEasyItem.getChildItems();
                    System.out.println("childItems" + childItems);
                    if (childItems != null && childItems.size() > 0) {
                        addChildItems(childItems);
                        if (mChildListView.getVisibility() != View.VISIBLE) {
                            mChildListView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (listener != null) {
                            listener.onClick(iEasyItem);
                        }
                        String displayName = iEasyItem.getDisplayName();
                        if (!TextUtils.isEmpty(displayName)) {
                            mScreeningText.setText(displayName);
                            dismiss();
                        }
                    }
                }
            }
        });
        initView(getContext(), menuTitle);
    }

    TextView mScreeningText;

    /**
     * init menuTitle
     *
     * @param context
     * @param name    titleName
     */
    private void initView(Context context, String name) {
        View view = View.inflate(context, R.layout.listfilter, this);
        mScreeningText = (TextView) view.findViewById(R.id.tv_screening);
        mScreeningText.setOnClickListener(this);
        mScreeningText.setText(name);
    }

    private void addChildItems(List<? extends IEasyItem> iEasyItems) {
        childFilterAdapter.setmItems(iEasyItems);
    }

    /**
     * add item
     *
     * @param show       是否马上显示窗口
     * @param iEasyItems 数据
     */
    public void addItems(boolean show, List<? extends IEasyItem> iEasyItems) {
        parentFilterAdapter.addAll(iEasyItems);
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

    private void showListFilter() {
        if (parentFilterAdapter.isEmpty()) {
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        IEasyItem iEasyItem = childFilterAdapter.getItem(position);
        if (listener != null) {
            listener.onClick(iEasyItem);
        }
        String displayName = iEasyItem.getDisplayName();
        if (!TextUtils.isEmpty(displayName)) {
            mScreeningText.setText(displayName);
            dismiss();
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
