package cc.easyandroid.listfiltermenu.core;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class IEasyItemFactory {
    public static IEasyItem buildIEasyItem(final List<? extends IEasyItem> iEasyItems) {
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

    public static class BaseIEasyItem extends SimpleEasyItem {
        private CharSequence displayName;
        private List<? extends IEasyItem> childItems;

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
        public CharSequence getDisplayName() {
            return displayName;
        }

        @Override
        public boolean isNoLimitItem() {
            return true;
        }
    }
}
