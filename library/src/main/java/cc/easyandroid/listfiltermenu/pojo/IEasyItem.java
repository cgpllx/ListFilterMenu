package cc.easyandroid.listfiltermenu.pojo;

import java.util.List;


public interface IEasyItem  {
    String getDisplayName();

    List<? extends IEasyItem> getChildItems();

    String getEasyId();
}
