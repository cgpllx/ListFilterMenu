package cc.easyandroid.listfiltermenu.core;

import java.util.HashMap;
import java.util.List;


public interface IEasyItem {
    List<? extends IEasyItem> getChildItems();

    int getChildSelectPosion();

    void setChildSelectPosion(int posion);

    CharSequence getDisplayName();

    HashMap<String,String> getEasyParameter();

    boolean isNoLimitItem();



}
