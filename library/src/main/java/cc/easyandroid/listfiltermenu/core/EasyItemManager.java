package cc.easyandroid.listfiltermenu.core;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;


public class EasyItemManager implements Serializable {
    protected ArrayList<? extends IEasyItem> mEasyItems;
    protected int childSelectPosion;
    protected boolean noLimitItem = false;
    protected boolean childSelected;
    protected boolean hasAddUnlimited;
    protected String tag;

    public boolean isHasEasyItems() {
        return mEasyItems != null && mEasyItems.size() > 0;
    }

    public boolean isHasAddUnlimited() {
        return hasAddUnlimited;
    }

    public void setHasAddUnlimited(boolean hasAddUnlimited) {
        this.hasAddUnlimited = hasAddUnlimited;
    }

    public EasyItemManager(ArrayList<? extends IEasyItem> easyItems, String tag) {
        this.mEasyItems = easyItems;
        this.tag = tag;
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

    @Override
    public int hashCode() {
        if (!TextUtils.isEmpty(tag)) return tag.hashCode();
        return super.hashCode();
    }
}
