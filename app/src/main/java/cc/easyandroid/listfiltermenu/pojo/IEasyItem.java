package cc.easyandroid.listfiltermenu.pojo;

import java.util.List;

/**
 * Created by Administrator on 2016/1/7.
 */
public interface IEasyItem {
    String getDisplayName();

    List<? extends IEasyItem> getChildItems();
}
