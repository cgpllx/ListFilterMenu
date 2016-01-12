package cc.easyandroid.listfiltermenu.core;

import java.util.List;


public interface IEasyItem  {
    String getDisplayName();

    List<? extends IEasyItem> getChildItems();

    String getEasyId();
}
