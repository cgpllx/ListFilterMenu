package cc.easyandroid.listfiltermenu.core;


import android.util.SparseArray;

import java.util.ArrayList;

import cc.easyandroid.listfiltermenu.widget.EasyFilterMenu;

public class EasyMenuManager implements EasyFilterListener.OnMenuShowListener {

    public EasyMenuManager() {
        menus = new ArrayList<>();
    }

    private ArrayList<EasyFilterMenu> menus;

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

    public SparseArray<EasyMenuStates> getMenusStates() {
        SparseArray<EasyMenuStates> sparseArray = new SparseArray<>();
        if (menus != null && menus.size() > 0) {
            for (int i = 0; i < menus.size(); i++) {
                EasyFilterMenu easyFilterMenu = menus.get(i);
                EasyMenuStates easyMenuStates = easyFilterMenu.getMenuStates();
                int menuSerialNumber = easyFilterMenu.getMenuSerialNumber();
                sparseArray.put(menuSerialNumber, easyMenuStates);
            }
        }
        return sparseArray;
    }

    public void setMenusStates(SparseArray<EasyMenuStates> menusStates) {
        if (menus != null && menus.size() > 0) {
            for (int i = 0; i < menus.size(); i++) {
                EasyFilterMenu easyFilterMenu = menus.get(i);
                int menuSerialNumber = easyFilterMenu.getMenuSerialNumber();
                EasyMenuStates sparseArray = menusStates.get(menuSerialNumber);
                if (sparseArray != null) {
                    easyFilterMenu.setMenuStates(sparseArray);
                }
            }
        }
    }

}
