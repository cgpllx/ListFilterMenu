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




}
