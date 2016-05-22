package cc.easyandroid.listfiltermenu.core;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;
import android.util.SparseBooleanArray;

/**
 * Created by chenguoping on 16/5/21.
 */
public class EasyMenuInfo implements Parcelable {
    SparseBooleanArray hasAddUnlimitedContainer ;//
    SparseArray<CharSequence> multiTitles ;
    private String currentMenuText;//记录当前要显示的menutitle
    private IEasyItem iEasyItem;

    public EasyMenuInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSparseBooleanArray(this.hasAddUnlimitedContainer);
        dest.writeSparseArray((SparseArray) this.multiTitles);
        dest.writeString(this.currentMenuText);
        dest.writeSerializable(this.iEasyItem);
    }

    protected EasyMenuInfo(Parcel in) {
        this.hasAddUnlimitedContainer = in.readSparseBooleanArray();
        this.multiTitles = in.readSparseArray(CharSequence.class.getClassLoader());
        this.currentMenuText = in.readString();
        this.iEasyItem = (IEasyItem) in.readSerializable();
    }

    public static final Creator<EasyMenuInfo> CREATOR = new Creator<EasyMenuInfo>() {
        @Override
        public EasyMenuInfo createFromParcel(Parcel source) {
            return new EasyMenuInfo(source);
        }

        @Override
        public EasyMenuInfo[] newArray(int size) {
            return new EasyMenuInfo[size];
        }
    };
}
