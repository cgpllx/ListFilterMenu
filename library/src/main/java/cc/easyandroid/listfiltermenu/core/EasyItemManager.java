package cc.easyandroid.listfiltermenu.core;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;


public class EasyItemManager implements Serializable {
    protected ArrayList<? extends IEasyItem> mEasyItems;
    protected int mChildSelectPosion;
    protected int mChildSelectTempPosion;
    protected boolean mNoLimitItem = false;
    protected boolean mChildSelected;
    protected boolean mHasAddUnlimited;
    protected String mTag;

    public boolean isHasEasyItems() {
        return mEasyItems != null && mEasyItems.size() > 0;
    }

    public boolean isHasAddUnlimited() {
        return mHasAddUnlimited;
    }

    public void setChildSelectTempPosion(int mChildSelectTempPosion) {
        this.mChildSelectTempPosion = mChildSelectTempPosion;
    }

    public int getChildSelectTempPosion() {
        return mChildSelectTempPosion;
    }

    public void setHasAddUnlimited(boolean hasAddUnlimited) {
        this.mHasAddUnlimited = hasAddUnlimited;
    }


    public EasyItemManager(ArrayList<? extends IEasyItem> easyItems, String tag) {
        this.mEasyItems = easyItems;
        this.mTag = tag;
    }

    public EasyItemManager(ArrayList<? extends IEasyItem> easyItems) {
        this.mEasyItems = easyItems;
    }

    public ArrayList<? extends IEasyItem> getEasyItems() {
        return mEasyItems;
    }

    public boolean isNoLimitItem() {
        return mNoLimitItem;
    }

    public boolean isChildSelected() {
        return mChildSelected;
    }

    public void setChildSelected(boolean childSelected) {
        this.mChildSelected = childSelected;
    }

    public int getChildSelectPosion() {
        return mChildSelectPosion;
    }

    public void setChildSelectPosion(int posion) {
        this.mChildSelectPosion = posion;
    }

    public void setNoLimitItem(boolean mNoLimitItem) {
        this.mNoLimitItem = mNoLimitItem;
    }

    @Override
    public int hashCode() {
        if (!TextUtils.isEmpty(mTag)) return mTag.hashCode();
        return super.hashCode();
    }
}
