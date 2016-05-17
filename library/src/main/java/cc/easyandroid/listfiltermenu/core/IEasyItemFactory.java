package cc.easyandroid.listfiltermenu.core;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class IEasyItemFactory {
    public static IEasyItem buildIEasyItem(final ArrayList<? extends IEasyItem> iEasyItems) {
        BaseIEasyItem iEasyItem = new BaseIEasyItem("");
        iEasyItem.setChildItems(iEasyItems);
        return iEasyItem;
    }

    public static IEasyItem buildOneIEasyItem(CharSequence displayName, HashMap<String, String> mapPara) {
        BaseIEasyItem iEasyItem = new BaseIEasyItem(displayName);
        if (mapPara != null && !mapPara.isEmpty()) {
            Set<String> keys = mapPara.keySet();
            for (String key : keys) {
                HashMap<String, String> iEasyItemMap = iEasyItem.getEasyParameter();
                iEasyItemMap.put(key, null);
            }
        }
        return iEasyItem;
    }

    /**
     *
     */
    public static class BaseIEasyItem extends SimpleEasyItem implements Parcelable {


        private CharSequence displayName;
        private ArrayList<? extends IEasyItem> childItems;

        public void setChildItems(ArrayList<? extends IEasyItem> childItems) {
            this.childItems = childItems;
        }

        public BaseIEasyItem(CharSequence displayName) {
            this.displayName = displayName;
        }

        @Override
        public ArrayList<? extends IEasyItem> getChildItems() {
            return childItems;
        }

        @Override
        public CharSequence getDisplayName() {
            return displayName;
        }

        @Override
        public boolean isNoLimitItem() {
            return true;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.displayName.toString());
            dest.writeList(this.childItems);
            dest.writeInt(this.childSelectPosion);
            dest.writeByte(noLimitItem ? (byte) 1 : (byte) 0);
        }

        protected BaseIEasyItem(Parcel in) {
            this.displayName =in.readString();
            this.childItems = new ArrayList<>();
            in.readList(this.childItems, List.class.getClassLoader());
            this.childSelectPosion = in.readInt();
            this.noLimitItem = in.readByte() != 0;
        }

        public static final Creator<BaseIEasyItem> CREATOR = new Creator<BaseIEasyItem>() {
            public BaseIEasyItem createFromParcel(Parcel source) {
                return new BaseIEasyItem(source);
            }

            public BaseIEasyItem[] newArray(int size) {
                return new BaseIEasyItem[size];
            }
        };
    }
}
