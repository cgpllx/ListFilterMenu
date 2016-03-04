package cc.easyandroid.listfiltermenu.core;

import java.util.List;


public interface IEasyItem extends IEasySuperItem {
    List<? extends IEasyItem> getChildItems();
}
