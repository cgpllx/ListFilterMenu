package cc.easyandroid.listfiltermenu.core;

import java.util.List;


public interface IEasyItem {
    List<? extends IEasyItem> getChildItems();

    int getChildSelectPosion();

    void setChildSelectPosion(int posion);

    CharSequence getDisplayName();

    String getEasyKey();

    String getEasyValue();


}
