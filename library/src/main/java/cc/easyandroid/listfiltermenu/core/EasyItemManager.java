package cc.easyandroid.listfiltermenu.core;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public class EasyItemManager implements Serializable, Parcelable {
    protected ArrayList<IEasyItem> mEasyItems;
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

    public EasyItemManager(ArrayList<IEasyItem> easyItems) {
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.mEasyItems);
        dest.writeInt(this.childSelectPosion);
        dest.writeByte(this.noLimitItem ? (byte) 1 : (byte) 0);
        dest.writeByte(this.childSelected ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasAddUnlimited ? (byte) 1 : (byte) 0);
    }

    protected EasyItemManager(Parcel in) {
        this.mEasyItems = in.createTypedArrayList(EasyItemManager.CREATOR);
        this.childSelectPosion = in.readInt();
        this.noLimitItem = in.readByte() != 0;
        this.childSelected = in.readByte() != 0;
        this.hasAddUnlimited = in.readByte() != 0;
    }

    public static final Creator<EasyItemManager> CREATOR = new Creator<EasyItemManager>() {
        @Override
        public EasyItemManager createFromParcel(Parcel source) {
            return new EasyItemManager(source);
        }

        @Override
        public EasyItemManager[] newArray(int size) {
            return new EasyItemManager[size];
        }
    };
}
