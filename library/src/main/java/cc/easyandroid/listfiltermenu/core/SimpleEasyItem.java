package cc.easyandroid.listfiltermenu.core;

import java.util.HashMap;
import java.util.List;

/**
 * simple IEasyItem
 */
public class SimpleEasyItem implements IEasyItem {
    protected int childSelectPosion;
    protected boolean noLimitItem = false;
    protected HashMap<String, String> easyParameter = new HashMap<>();

    @Override
    public List<? extends IEasyItem> getChildItems() {
        return null;
    }

    @Override
    public int getChildSelectPosion() {
        return childSelectPosion;
    }

    @Override
    public void setChildSelectPosion(int posion) {
        this.childSelectPosion = posion;
    }

    @Override
    public CharSequence getDisplayName() {
        return null;
    }

    @Override
    public HashMap<String, String> getEasyParameter() {
        return easyParameter;
    }

    @Override
    public boolean isNoLimitItem() {
        return noLimitItem;
    }

    public void setNoLimitItem(boolean noLimitItem) {
        this.noLimitItem = noLimitItem;
    }
}
