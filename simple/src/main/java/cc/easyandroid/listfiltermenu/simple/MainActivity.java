package cc.easyandroid.listfiltermenu.simple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cc.easyandroid.listfiltermenu.core.IEasyItem;
import cc.easyandroid.listfiltermenu.core.ListFilterAdapter;
import cc.easyandroid.listfiltermenu.widget.EasyListFilterMenu;

public class MainActivity extends AppCompatActivity {
    EasyListFilterMenu easyListFilterMenu;
    EasyListFilterMenu easyListFilterMenu2;

    //    EasyPopMenu easyPopMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        easyListFilterMenu = (EasyListFilterMenu) findViewById(R.id.easyListFilterMenu);
        easyListFilterMenu2 = (EasyListFilterMenu) findViewById(R.id.easyListFilterMenu2);
        final List<Text1.ResultEntity> lists1 = dd();

        final ArrayList<Text1.ResultEntity> lists2 = dd();
//        EasyMenuManager easyMenuManager =new EasyMenuManager( );
//        easyMenuManager.addMenu(easyListFilterMenu);
//        easyMenuManager.addMenu(easyListFilterMenu2);
        easyListFilterMenu.addItems(false, lists1);
        easyListFilterMenu.setOnMenuListItemClickListener(new EasyListFilterMenu.OnMenuListItemClickListener() {
            @Override
            public void onClick(IEasyItem iEasyItem) {
//                easyListFilterMenu.set
//                Toast.makeText(getApplicationContext(), iEasyItem.getDisplayName(), Toast.LENGTH_SHORT).show();
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
        easyListFilterMenu2.setOnMenuClickLinstener(new EasyListFilterMenu.OnMenuWithoutDataClickLinstener() {
            @Override
            public void withoutData(EasyListFilterMenu menu) {
                Toast.makeText(getApplicationContext(), "没有数据,马上加载数据...", Toast.LENGTH_SHORT).show();
                menu.addItems(true, lists2);

//                easyListFilterMenu2.getListView1().performItemClick(easyListFilterMenu2, 3, 3);
            }
        });
        easyListFilterMenu2.setOnMenuListItemClickListener(new EasyListFilterMenu.OnMenuListItemClickListener() {
            @Override
            public void onClick(IEasyItem iEasyItem) {
                System.out.println("onClick " + iEasyItem);
//                easyListFilterMenu2.getListView1().performItemClick(easyListFilterMenu2, 2, 2);
//                long[] longs = easyListFilterMenu2.getListView1().//trw
//                        getCheckedItemIds();
//                int position = easyListFilterMenu2.getListView1().getCheckedItemPosition();
//                System.out.println("onClick position ====" + position);
//                System.out.println("onClick longs111====" + longs);
//                System.out.println("onClick longslength===" + longs.length);
//                easyListFilterMenu2.getListView1().getCheckedItemPositions()
//                SparseBooleanArray d = null;
//                getIntent().putExtra(" ,",d.);
//                getIntent().getIntent
//                getIn
            }
        });
//        ArrayList<EasyPara> easyParas = new ArrayList<>();
//        easyParas.add(new EasyPara(1, 3));
//        easyParas.add(new EasyPara(2, 3));
//        easyListFilterMenu2.setMenuList1AllChildSelectPosion(easyParas);
//
//        g
        getIntent().putParcelableArrayListExtra("lists2",lists2);
    }

    public ArrayList<Text1.ResultEntity> dd() {
        Text1 text1 = new Gson().fromJson(Text.text, Text1.class);
        final ArrayList<Text1.ResultEntity> lists = text1.getResult();
        //IEasyItemFactory.buildIEasyItem(lists);

        return lists;
    }

    @Override
    protected void onDestroy() {
//        easyListFilterMenu.dismiss();
        super.onDestroy();
    }
}
