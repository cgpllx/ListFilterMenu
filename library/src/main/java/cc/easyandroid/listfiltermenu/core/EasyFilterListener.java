package cc.easyandroid.listfiltermenu.core;

import android.support.v4.util.ArrayMap;
import android.support.v4.util.SimpleArrayMap;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.Map;

import cc.easyandroid.listfiltermenu.widget.EasyFilterMenu;

/**
 * Created by Administrator on 2016/5/26.
 */
public interface EasyFilterListener {
    interface OnMenuShowListener {
        void onMenuShowBefore(EasyFilterMenu menu);
    }

    interface OnMenuListItemClickListener {
        void onClick(EasyFilterMenu easyFilterMenu,IEasyItem iEasyItem);
    }

    interface OnCustomViewConfirmClickListener {
        void onClick(ListView listview, ViewGroup viewGroup, EasyFilterMenu easyFilterMenu);
    }

    interface OnEasyMenuParasChangedListener{
        void onChanged(SimpleArrayMap<String, String> easyMenuParas);
    }
}
