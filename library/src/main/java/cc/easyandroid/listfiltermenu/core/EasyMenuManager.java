package cc.easyandroid.listfiltermenu.core;


import android.util.SparseArray;

import java.util.ArrayList;

import cc.easyandroid.listfiltermenu.widget.EasyFilterMenu;

public class EasyMenuManager implements EasyFilterMenu.OnMenuShowListener {

    public EasyMenuManager() {
        menus = new ArrayList<>();
    }

    ArrayList<EasyFilterMenu> menus;

    public void addMenu(EasyFilterMenu menu) {
        menus.add(menu);
        menu.setOnMenuShowListener(this);
    }

    @Override
    public void onMenuShowBefore(EasyFilterMenu clickMenu) {
        if (clickMenu != null && clickMenu.isShowing()) {
            return;
        }
        if (menus != null && !menus.isEmpty()) {
            for (EasyFilterMenu menu : menus) {
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

    public SparseArray<SingleSelectionMenuStates> getMenusStates() {
        SparseArray<SingleSelectionMenuStates> sparseArray = new SparseArray<>();
        if (menus != null && menus.size() > 0) {
            for (int i = 0; i < menus.size(); i++) {
                EasyFilterMenu easyFilterMenu = menus.get(i);
//                menus.gett
                SingleSelectionMenuStates singleSelectionMenuStates = easyFilterMenu.getMenuStates();
                sparseArray.put(i, singleSelectionMenuStates);
            }
        }
        return sparseArray;
    }
}
