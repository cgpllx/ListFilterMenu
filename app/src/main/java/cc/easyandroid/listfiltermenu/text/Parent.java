package cc.easyandroid.listfiltermenu.text;

import java.util.List;

import cc.easyandroid.listfiltermenu.pojo.IEasyItem;

/**
 * Created by Administrator on 2016/1/8.
 */
public class Parent implements IEasyItem {
    public String name;
    public List<Parent> subregions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Parent> getSubregions() {
        return subregions;
    }

    public void setSubregions(List<Parent> subregions) {
        this.subregions = subregions;
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    @Override
    public List<? extends IEasyItem> getChildItems() {
        return subregions;
    }
}
