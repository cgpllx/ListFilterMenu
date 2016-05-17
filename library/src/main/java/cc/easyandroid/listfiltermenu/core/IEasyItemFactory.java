package cc.easyandroid.listfiltermenu.core;

import android.os.Bundle;
import android.os.Parcel;

import java.util.ArrayList;
import java.util.HashMap;
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
    public static class BaseIEasyItem extends SimpleParcelableEasyItem {


        private CharSequence displayName;
        private ArrayList<? extends SimpleParcelableEasyItem> childItems;//如果对象要在组件中传递，请使用SimpleParcelableEasyItem

        public void setChildItems(ArrayList childItems) {
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

        public static final String DATA_KEY = "DATA_KEY";

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.displayName.toString());

            //将泛型的Parcelable 封装后保存
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(DATA_KEY, this.childItems);
            dest.writeBundle(bundle);

            dest.writeInt(this.childSelectPosion);

            dest.writeByte(noLimitItem ? (byte) 1 : (byte) 0);
        }

        protected BaseIEasyItem(Parcel in) {
            this.displayName = in.readString();

            this.childItems = in.readBundle().getParcelableArrayList(DATA_KEY);

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
