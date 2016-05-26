package cc.easyandroid.listfiltermenu.core;

import android.view.ViewGroup;
import android.widget.ListView;

import cc.easyandroid.listfiltermenu.widget.EasyFilterMenu;

/**
 * Created by Administrator on 2016/5/26.
 */
public interface EasyFilterListener {
    interface OnMenuShowListener {
        void onMenuShowBefore(EasyFilterMenu menu);
    }

    interface OnMenuListItemClickListener {
        void onClick(IEasyItem iEasyItem);
    }

    interface OnCustomViewConfirmClickListener {
        void onClick(ListView listview, ViewGroup viewGroup, EasyFilterMenu easyFilterMenu);
    }
}
