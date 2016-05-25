package cc.easyandroid.listfiltermenu.core;

import java.util.HashMap;

/**
 * simple IEasyItem
 */
public class SimpleEasyItem implements IEasyItem {
    protected HashMap<String, String> easyParameter = new HashMap<>();

    EasyItemManager easyItemManager;

    @Override
    public EasyItemManager getEasyItemManager() {
        if (easyItemManager == null) {
            easyItemManager = onCreatChildEasyItemManager();
        }
        return easyItemManager;
    }

    public EasyItemManager onCreatChildEasyItemManager() {
        return new EasyItemManager(null);
    }

    ;

    @Override
    public CharSequence getDisplayName() {
        return null;
    }

    @Override
    public HashMap<String, String> getEasyParameter() {
        return easyParameter;
    }


}
