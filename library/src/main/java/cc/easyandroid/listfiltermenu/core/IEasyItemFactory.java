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
        List<? extends IEasyItem> childItems;

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
        public String getEasyId() {
            return null;
        }
    }
}
