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
//        iEasyItem.setChildItems(iEasyItems);
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


        public BaseIEasyItem(CharSequence displayName) {
            this.displayName = displayName;

        }

        @Override
        public EasyItemManager getEasyItemManager() {
            EasyItemManager easyItemManager = new EasyItemManager (null);
            easyItemManager.setNoLimitItem(true);
            return easyItemManager;
        }

        @Override
        public CharSequence getDisplayName() {
            return displayName;
        }


    }
}
