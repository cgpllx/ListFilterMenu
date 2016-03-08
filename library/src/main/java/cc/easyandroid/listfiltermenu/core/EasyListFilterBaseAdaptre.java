package cc.easyandroid.listfiltermenu.core;

import android.widget.BaseAdapter;

public abstract class EasyListFilterBaseAdaptre<T> extends BaseAdapter {





    @Override
    public long getItemId(int position) {
        return position;
    }





}
