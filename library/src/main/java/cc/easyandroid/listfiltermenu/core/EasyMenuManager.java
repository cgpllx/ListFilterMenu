package cc.easyandroid.listfiltermenu.core;


import java.util.ArrayList;

import cc.easyandroid.listfiltermenu.widget.EasyListFilterMenu;

public class EasyMenuManager implements EasyListFilterMenu.OnMenuShowListener {

    public EasyMenuManager() {
        menus = new ArrayList<>();
    }


    ArrayList<EasyListFilterMenu> menus;

    public void addMenu(EasyListFilterMenu menu) {
        menus.add(menu);
        menu.setOnMenuShowListener(this);
//        fragmentManager.beginTransaction().
    }

    @Override
    public void onMenuShowBefore(EasyListFilterMenu clickMenu) {
        if (clickMenu != null && clickMenu.isShowing()) {
            return;
        }
        if (menus != null && !menus.isEmpty()) {
            for (EasyListFilterMenu menu : menus) {
                if (menu.isShowing()) {
                    menu.dismiss();
                }
            }
        }
    }

    public void clear() {
        if (menus != null) {
            menus.clear();
            menus = null;
        }
    }
}
