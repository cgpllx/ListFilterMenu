package cc.easyandroid.listfiltermenu.core;

import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;


public interface IEasyItem {
    ArrayList<? extends IEasyItem> getChildItems();

    int getChildSelectPosion();

    void setChildSelectPosion(int posion);

    CharSequence getDisplayName();

    HashMap<String, String> getEasyParameter();

    boolean isNoLimitItem();


}
