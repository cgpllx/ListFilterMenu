package cc.easyandroid.listfiltermenu.core;

import android.os.Parcel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * simple IEasyItem
 */
public class SimpleEasyItem implements IEasyItem {
    protected HashMap<String, String> easyParameter = new HashMap<>();


    @Override
    public EasyItemManager getEasyItemManager() {
        return new EasyItemManager(null);
    }

    @Override
    public CharSequence getDisplayName() {
        return null;
    }

    @Override
    public HashMap<String, String> getEasyParameter() {
        return easyParameter;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.easyParameter);
    }

    public SimpleEasyItem() {
    }

    protected SimpleEasyItem(Parcel in) {
        this.easyParameter = (HashMap<String, String>) in.readSerializable();
    }

    public static final Creator<SimpleEasyItem> CREATOR = new Creator<SimpleEasyItem>() {
        @Override
        public SimpleEasyItem createFromParcel(Parcel source) {
            return new SimpleEasyItem(source);
        }

        @Override
        public SimpleEasyItem[] newArray(int size) {
            return new SimpleEasyItem[size];
        }
    };
}
