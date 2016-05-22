package cc.easyandroid.listfiltermenu.core;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public interface IEasyItem extends Serializable {
    ArrayList<? extends IEasyItem> getChildItems();

    int getChildSelectPosion();

    void setChildSelectPosion(int posion);//设置child中那个posion被选中

    CharSequence getDisplayName();//显示的名称

    HashMap<String, String> getEasyParameter();//参数的封装

    boolean isNoLimitItem();//是否是不限的项

    boolean isChildSelected();//child是否被选中

    void setChildSelected(boolean isChildSelected);


}
