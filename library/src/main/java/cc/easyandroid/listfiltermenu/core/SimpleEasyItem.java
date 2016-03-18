package cc.easyandroid.listfiltermenu.core;

import java.util.List;

/**
 * simple IEasyItem
 */
public class SimpleEasyItem implements IEasyItem {
    private int childSelectPosion;

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
    public String getEasyKey() {
        return null;
    }

    @Override
    public String getEasyValue() {
        return null;
    }
}
