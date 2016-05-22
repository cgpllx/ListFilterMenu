package cc.easyandroid.listfiltermenu.simple;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import cc.easyandroid.listfiltermenu.core.IEasyItem;
import cc.easyandroid.listfiltermenu.core.ListFilterAdapter;
import cc.easyandroid.listfiltermenu.core.OnMenuListItemClickListener;
import cc.easyandroid.listfiltermenu.widget.EasyFileterMenu_MoreSelection;
import cc.easyandroid.listfiltermenu.widget.EasyFilterMenu;
import cc.easyandroid.listfiltermenu.widget.EasyFilterMenu_MultiSelection;
import cc.easyandroid.listfiltermenu.widget.EasyFilterMenu_SingleSelection;
import cc.easyandroid.listfiltermenu.widget.EasyListFilterMenu;

public class MainActivity extends AppCompatActivity {
    EasyListFilterMenu easyListFilterMenu;
    EasyListFilterMenu easyListFilterMenu2;
    EasyFilterMenu_SingleSelection EasyFilterMenu_SingleSelection;
    EasyFilterMenu_MultiSelection easyFilterMenu_multiSelection;
    EasyFileterMenu_MoreSelection easyFileterMenu_moreSelection;

    //    EasyPopMenu easyPopMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        easyListFilterMenu = (EasyListFilterMenu) findViewById(R.id.easyListFilterMenu);
        easyListFilterMenu2 = (EasyListFilterMenu) findViewById(R.id.easyListFilterMenu2);
        EasyFilterMenu_SingleSelection = (EasyFilterMenu_SingleSelection) findViewById(R.id.easyListFilterMenu3);
        easyFilterMenu_multiSelection = (EasyFilterMenu_MultiSelection) findViewById(R.id.easyListFilterMenu4);
        easyFileterMenu_moreSelection= (EasyFileterMenu_MoreSelection) findViewById(R.id.easyListFilterMenu5);

        final ArrayList<Text1.ResultEntity> lists1 = dd();

        final ArrayList<Text1.ResultEntity> lists2 = dd();
        final ArrayList<Text1.ResultEntity> lists4 = dd();
        final ArrayList<Text1.ResultEntity> lists5= dd();
//        EasyMenuManager easyMenuManager =new EasyMenuManager( );
//        easyMenuManager.addMenu(easyListFilterMenu);
//        easyMenuManager.addMenu(easyListFilterMenu2);
        easyListFilterMenu.addItems(false, lists1);
        easyListFilterMenu.setOnMenuListItemClickListener(new EasyListFilterMenu.OnMenuListItemClickListener() {
            @Override
            public void onClick(IEasyItem iEasyItem) {
//                easyListFilterMenu.set
//                Toast.makeText(getApplicationContext(), iEasyItem.getDisplayName(), Toast.LENGTH_SHORT).showMenuContent();
            }
        });
        easyListFilterMenu.setOnCustomViewConfirmClickListener(new EasyListFilterMenu.OnCustomViewConfirmClickListener() {
            @Override
            public void onClick(ListView listview, ListFilterAdapter<IEasyItem> filterAdapter_List, ViewGroup viewGroup) {
                easyListFilterMenu.menuListItemClick(null);
            }
        });
//        easyListFilterMenu.setOnCustomViewConfirmClickListener(new EasyListFilterMenu.OnCustomViewConfirmClickListener() {
//            @Override
//            public void onClick(ListView listview, ListFilterAdapter<IEasyItem> filterAdapter_List,ViewGroup viewGroup) {
//                System.out.println("cgp=i  setOnMultipleChoiceClickListener");
//                easyListFilterMenu.setMenuTitle("多选");
//            }
//        });
//        easyListFilterMenu2.setOnMenuClickLinstener(new EasyListFilterMenu.OnMenuWithoutDataClickLinstener() {
//            @Override
//            public void withoutData(EasyListFilterMenu menu) {
//                Toast.makeText(getApplicationContext(), "没有数据,马上加载数据...", Toast.LENGTH_SHORT).show();
//                menu.addItems(true, lists2);
//
////                easyListFilterMenu2.getListView1().performItemClick(easyListFilterMenu2, 3, 3);
//            }
//        });
//        easyListFilterMenu2.setOnMenuListItemClickListener(new EasyListFilterMenu.OnMenuListItemClickListener() {
//            @Override
//            public void onClick(IEasyItem iEasyItem) {
//                System.out.println("onClick " + iEasyItem);
////                easyListFilterMenu2.getListView1().performItemClick(easyListFilterMenu2, 2, 2);
////                long[] longs = easyListFilterMenu2.getListView1().//trw
////                        getCheckedItemIds();
////                int position = easyListFilterMenu2.getListView1().getCheckedItemPosition();
////                System.out.println("onClick position ====" + position);
////                System.out.println("onClick longs111====" + longs);
////                System.out.println("onClick longslength===" + longs.length);
////                easyListFilterMenu2.getListView1().getCheckedItemPositions()
////                SparseBooleanArray d = null;
////                getIntent().putExtra(" ,",d.);
////                getIntent().getIntent
////                getIn
//            }
//        });
        EasyFilterMenu_SingleSelection.setMenuData(false, lists2);
        EasyFilterMenu_SingleSelection.setOnMenuWithoutDataClickLinstener(new EasyFilterMenu.OnMenuWithoutDataClickLinstener() {
            @Override
            public void withoutData(EasyFilterMenu menu) {
                Toast.makeText(getApplicationContext(), "没有数据,马上加载数据...", Toast.LENGTH_SHORT).show();
                EasyFilterMenu_SingleSelection.setMenuData(true, lists2);
            }
        });
        EasyFilterMenu_SingleSelection.setOnMenuListItemClickListener(new OnMenuListItemClickListener() {
            @Override
            public void onClick(IEasyItem iEasyItem) {
                Toast.makeText(getApplicationContext(), iEasyItem.getDisplayName(), Toast.LENGTH_SHORT).show();
            }
        });
//        easyFilterMenu_multiSelection.setOnMenuWithoutDataClickLinstener(new on);
        easyFilterMenu_multiSelection.setMenuData(false, lists4);
        easyFilterMenu_multiSelection.setOnMenuListItemClickListener(new OnMenuListItemClickListener() {
            @Override
            public void onClick(IEasyItem iEasyItem) {

            }
        });
        easyFileterMenu_moreSelection.setMenuData(false,lists5);
        easyFileterMenu_moreSelection.setOnMenuListItemClickListener(new OnMenuListItemClickListener() {
            @Override
            public void onClick(IEasyItem iEasyItem) {

            }
        });
    }

    public ArrayList<Text1.ResultEntity> dd() {
        Text1 text1 = new Gson().fromJson(Text.text, Text1.class);
        final ArrayList<Text1.ResultEntity> lists = text1.getResult();
        //IEasyItemFactory.buildIEasyItem(lists);

        return lists;
    }

    @Override
    protected void onDestroy() {
//        easyListFilterMenu.dismissMenuContent();
        super.onDestroy();
    }
}
