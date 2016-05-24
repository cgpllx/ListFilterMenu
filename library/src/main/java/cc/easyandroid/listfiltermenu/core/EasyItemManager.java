package cc.easyandroid.listfiltermenu.core;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public class EasyItemManager implements Serializable {
    protected ArrayList<? extends IEasyItem> mEasyItems;
    protected int childSelectPosion;
    protected boolean noLimitItem = false;
    protected boolean childSelected;
    protected boolean hasAddUnlimited;

    public boolean isHasAddUnlimited() {
        return hasAddUnlimited;
    }

    public void setHasAddUnlimited(boolean hasAddUnlimited) {
        this.hasAddUnlimited = hasAddUnlimited;
    }

    public EasyItemManager(ArrayList<? extends IEasyItem> easyItems) {
        this.mEasyItems = easyItems;
    }

    public ArrayList<? extends IEasyItem> getEasyItems() {
        return mEasyItems;
    }

    public boolean isNoLimitItem() {
        return noLimitItem;
    }

    public boolean isChildSelected() {
        return childSelected;
    }

    public void setChildSelected(boolean childSelected) {
        this.childSelected = childSelected;
    }

    public int getChildSelectPosion() {
        return childSelectPosion;
    }

    public void setChildSelectPosion(int posion) {
        this.childSelectPosion = posion;
    }

    public void setNoLimitItem(boolean noLimitItem) {
        this.noLimitItem = noLimitItem;
    }



}
