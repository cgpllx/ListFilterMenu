package cc.easyandroid.listfiltermenu.core;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class IEasyItemFactory {
    public static IEasyItem buildIEasyItem(final List<? extends IEasyItem> iEasyItems) {
        BaseIEasyItem iEasyItem = new BaseIEasyItem("");
        iEasyItem.setChildItems(iEasyItems);
        return iEasyItem;
    }

    public static IEasyItem buildOneIEasyItem(CharSequence displayName) {
        IEasyItem iEasyItem = new BaseIEasyItem(displayName);
        return iEasyItem;
    }

    public static IEasyItem buildOneIEasyItem(CharSequence displayName, String easyKey, String easyValue) {
        BaseIEasyItem iEasyItem = new BaseIEasyItem(displayName);
        iEasyItem.setEasyKey(easyKey);
        iEasyItem.setEasyValue(easyValue);
        return iEasyItem;
    }

    public static IEasyItem buildTwoLevelIEasyItem(CharSequence displayName) {
        BaseIEasyItem iEasyItem = new BaseIEasyItem(displayName);
        IEasyItem childIEasyItem = new BaseIEasyItem(displayName);
        List<IEasyItem> iEasyItemList = new ArrayList<>();
        iEasyItemList.add(childIEasyItem);
        iEasyItem.setChildItems(iEasyItemList);
        return iEasyItem;
    }

    public static class BaseIEasyItem implements IEasyItem {
        private CharSequence displayName;
        private List<? extends IEasyItem> childItems;
        private String easyKey;
        private String easyValue;

        public void setEasyKey(String easyKey) {
            this.easyKey = easyKey;
        }

        public void setEasyValue(String easyValue) {
            this.easyValue = easyValue;
        }

        public void setChildItems(List<? extends IEasyItem> childItems) {
            this.childItems = childItems;
        }

        public BaseIEasyItem(CharSequence displayName) {
            this.displayName = displayName;
        }

        @Override
        public List<? extends IEasyItem> getChildItems() {
            return childItems;
        }

        @Override
        public int getChildSelectPosion() {
            return -1;
        }

        @Override
        public void setChildSelectPosion(int posion) {
            //no treatment
        }

        @Override
        public CharSequence getDisplayName() {
            return displayName;
        }

        @Override
        public String getEasyKey() {
            return easyKey;
        }

        @Override
        public String getEasyValue() {
            return easyValue;
        }
    }
}
