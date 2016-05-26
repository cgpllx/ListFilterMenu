package cc.easyandroid.listfiltermenu.core;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 *
 */
public class IEasyItemFactory {

    public static IEasyItem buildOneIEasyItem(CharSequence displayName, HashMap<String, String> mapPara) {
        BaseIEasyItem iEasyItem = new BaseIEasyItem(displayName.toString());
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
        private String displayName;

        public BaseIEasyItem(String displayName) {
            this.displayName = displayName;

        }

        @Override
        public EasyItemManager getEasyItemManager() {
            ArrayList<IEasyItem> arrayList = new ArrayList<>();
            EasyItemManager easyItemManager = new EasyItemManager(arrayList);
            easyItemManager.setNoLimitItem(true);
            return easyItemManager;
        }

        @Override
        public CharSequence getDisplayName() {
            return displayName;
        }
    }
}
