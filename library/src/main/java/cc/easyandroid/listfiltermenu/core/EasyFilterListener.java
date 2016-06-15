package cc.easyandroid.listfiltermenu.core;

import android.support.v4.util.SimpleArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import cc.easyandroid.listfiltermenu.widget.EasyFilterMenu;

/**
 * Created by Administrator on 2016/5/26.
 */
public interface EasyFilterListener {
    interface OnMenuShowListener {
        /**
         * 显示之前的监听
         *
         * @param menu EasyFilterMenu
         * @param view pop的view
         */
        void onMenuShowBefore(EasyFilterMenu menu, View view);
    }

    interface OnMenuListItemClickListener {
        void onClick(EasyFilterMenu easyFilterMenu, IEasyItem iEasyItem);
    }

    interface OnCustomViewConfirmClickListener<T extends EasyFilterMenu> {
        void onClick(ListView listview, ViewGroup viewGroup, T easyFilterMenu);
    }

    interface OnEasyMenuParasChangedListener {
        void onChanged(SimpleArrayMap<String, String> easyMenuParas);
    }
}
