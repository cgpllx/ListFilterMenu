package cc.easyandroid.listfiltermenu.simple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

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
        final List<Text1.ResultEntity> lists2 = dd();
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
            }
        });
    }

    public List<Text1.ResultEntity> dd() {
        Text1 text1 = new Gson().fromJson(Text.text, Text1.class);
        final List<Text1.ResultEntity> lists = text1.getResult();
        //IEasyItemFactory.buildIEasyItem(lists);
        return lists;
    }

    @Override
    protected void onDestroy() {
//        easyListFilterMenu.dismiss();
        super.onDestroy();
    }
}
