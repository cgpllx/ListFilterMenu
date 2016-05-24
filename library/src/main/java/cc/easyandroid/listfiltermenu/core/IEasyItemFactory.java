package cc.easyandroid.listfiltermenu.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 *
 */
public class IEasyItemFactory {
    public static IEasyItem buildIEasyItem(final ArrayList<? extends IEasyItem> iEasyItems) {
        BaseIEasyItem iEasyItem = new BaseIEasyItem("topIEasyItem");
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
    public static class BaseIEasyItem extends SimpleEasyItem {


        private CharSequence displayName;
        private ArrayList<? extends IEasyItem> childItems;//如果对象要在组件中传递，请使用SimpleParcelableEasyItem

        public void setChildItems(ArrayList<? extends IEasyItem> childItems) {
            this.childItems = childItems;
            setNoLimitItem(true);
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


    }
}
