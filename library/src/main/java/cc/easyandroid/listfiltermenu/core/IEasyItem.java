package cc.easyandroid.listfiltermenu.core;

import java.io.Serializable;
import java.util.HashMap;


public interface IEasyItem extends Serializable {

    EasyItemManager getEasyItemManager();

    CharSequence getDisplayName();//显示的名称

        HashMap<String, String> getEasyParameter();//参数的封装
//   ArrayMap<String, String> getEasyParameter();
}
