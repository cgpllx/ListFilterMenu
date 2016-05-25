package cc.easyandroid.listfiltermenu.core;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.util.ArrayMap;
import android.util.SparseBooleanArray;

/**
 * menu 数据
 */
public class SingleSelectionMenuStates implements Parcelable {
    private EasyItemManager easyItemManager;
    private String menuTitle;
    private SparseBooleanArray menuStatesArray;//保存被选中的状态的
    ArrayMap<Integer, String> multiTitles;//多选时候存放被选择的标题的集合

    public ArrayMap<Integer, String> getMultiTitles() {
        return multiTitles;
    }

    public SparseBooleanArray getMenuStatesArray() {
        return menuStatesArray;
    }

    public EasyItemManager getEasyItemManager() {
        return easyItemManager;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    private SingleSelectionMenuStates(Builder builder) {
        easyItemManager = builder.easyItemManager;
        menuTitle = builder.menuTitle;
        menuStatesArray = builder.menuStatesArray;
        multiTitles = builder.multiTitles;
    }


    public static class Builder {
        EasyItemManager easyItemManager;
        String menuTitle;
        SparseBooleanArray menuStatesArray;
        ArrayMap<Integer, String> multiTitles;//多选时候存放被选择的标题的集合

        public Builder setMenuStatesArray(SparseBooleanArray menuStatesArray) {
            this.menuStatesArray = menuStatesArray;
            return this;
        }

        public Builder setMultiTitles(ArrayMap<Integer, String> multiTitles) {
            this.multiTitles = multiTitles;
            return this;
        }



        public Builder setMenuTitle(String title) {
            this.menuTitle = title;
            return this;
        }


        public Builder setEasyItemManager(EasyItemManager easyItemManager) {
            this.easyItemManager = easyItemManager;
            return this;
        }

        public SingleSelectionMenuStates build() {
            return new SingleSelectionMenuStates(this);
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.easyItemManager);
        dest.writeString(this.menuTitle);
        dest.writeSparseBooleanArray(this.menuStatesArray);
        dest.writeMap(this.multiTitles);
    }

    protected SingleSelectionMenuStates(Parcel in) {
        this.easyItemManager = (EasyItemManager) in.readSerializable();
        this.menuTitle = in.readString();
        this.menuStatesArray = in.readSparseBooleanArray();
        this.multiTitles = new ArrayMap<Integer, String>();
        in.readMap(this.multiTitles, multiTitles.getClass().getClassLoader());
    }

    public static final Creator<SingleSelectionMenuStates> CREATOR = new Creator<SingleSelectionMenuStates>() {
        public SingleSelectionMenuStates createFromParcel(Parcel source) {
            return new SingleSelectionMenuStates(source);
        }

        public SingleSelectionMenuStates[] newArray(int size) {
            return new SingleSelectionMenuStates[size];
        }
    };
}