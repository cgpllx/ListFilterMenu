package cc.easyandroid.listfiltermenu.simple;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import cc.easyandroid.listfiltermenu.core.EasyFilterListener;
import cc.easyandroid.listfiltermenu.core.EasyItemManager;
import cc.easyandroid.listfiltermenu.core.EasyMenuStates;
import cc.easyandroid.listfiltermenu.core.IEasyItem;
import cc.easyandroid.listfiltermenu.widget.EasyFileterMenuMore;
import cc.easyandroid.listfiltermenu.widget.EasyFilterMenu;
import cc.easyandroid.listfiltermenu.widget.EasyFilterMenuMulti;
import cc.easyandroid.listfiltermenu.widget.EasyFilterMenuSingle;
import cc.easyandroid.listfiltermenu.widget.EasyMenuContainer;

public class MainActivity extends AppCompatActivity {
    EasyFilterMenuSingle menuFilter1;
    EasyFilterMenuSingle menuFilter2;
    EasyFilterMenuMulti menuFilter3;
    EasyFileterMenuMore menuFilter4;
    EasyMenuContainer easyMenuContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        menuFilter1 = (EasyFilterMenuSingle) findViewById(R.id.menuFilter1);
        menuFilter2 = (EasyFilterMenuSingle) findViewById(R.id.menuFilter2);
        menuFilter3 = (EasyFilterMenuMulti) findViewById(R.id.menuFilter3);
        menuFilter4 = (EasyFileterMenuMore) findViewById(R.id.menuFilter4);

        easyMenuContainer = (EasyMenuContainer) findViewById(R.id.easyMenuContainer);

        final ArrayList<Text1.ResultEntity> lists1 = dd();
        final ArrayList<Text1.ResultEntity> lists2 = dd();
        final ArrayList<Text1.ResultEntity> lists3 = dd();
        final ArrayList<Text1.ResultEntity> lists4 = dd();

        menuFilter1.setMenuData(false, new EasyItemManager(lists1));
//        menuFilter2.setMenuData(false,new EasyItemManager(lists2));
//        menuFilter3.setMenuData(false,new EasyItemManager(lists3));
//        menuFilter4.setMenuData(false,new EasyItemManager(lists4));

        menuFilter2.setOnMenuWithoutDataClickLinstener(new EasyFilterMenu.OnMenuWithoutDataClickLinstener() {
            @Override
            public void withoutData(EasyFilterMenu menu) {
                Toast.makeText(getApplicationContext(), "没有数据,马上加载数据...", Toast.LENGTH_SHORT).show();
                menuFilter2.setMenuData(true, new EasyItemManager(lists2));
            }
        });
        menuFilter3.setOnMenuWithoutDataClickLinstener(new EasyFilterMenu.OnMenuWithoutDataClickLinstener() {
            @Override
            public void withoutData(EasyFilterMenu menu) {
                Toast.makeText(getApplicationContext(), "没有数据,马上加载数据...", Toast.LENGTH_SHORT).show();
                menuFilter3.setMenuData(true, new EasyItemManager(lists3));
            }
        });
        menuFilter4.setOnMenuWithoutDataClickLinstener(new EasyFilterMenu.OnMenuWithoutDataClickLinstener() {
            @Override
            public void withoutData(EasyFilterMenu menu) {
                Toast.makeText(getApplicationContext(), "没有数据,马上加载数据...", Toast.LENGTH_SHORT).show();
                menuFilter4.setMenuData(true, new EasyItemManager(lists4));
            }
        });
        menuFilter1.setOnMenuListItemClickListener(new EasyFilterListener.OnMenuListItemClickListener() {
            @Override
            public void onClick(EasyFilterMenu easyFilterMenu, IEasyItem iEasyItem) {
                Toast.makeText(getApplicationContext(), iEasyItem.getDisplayName(), Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupBuntonClickListener(easyMenuContainer);
            }
        });
        findViewById(R.id.click2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupBuntonClickListener(null);
            }
        });
        menuFilter3.setOnCustomViewConfirmClickListener(new EasyFilterListener.OnCustomViewConfirmClickListener() {
            @Override
            public void onClick(ListView listview, ViewGroup viewGroup, EasyFilterMenu easyFilterMenu) {
                menuFilter3.saveStates();
                Toast.makeText(getApplicationContext(), "确定按钮被点击", Toast.LENGTH_SHORT).show();
                menuFilter3.dismiss();
                menuFilter3.setMenuTitle("多选");
            }
        });
    }

    private void setupListItemClickListener(EasyFilterMenu easyFilterMenu) {
        easyFilterMenu.setOnMenuListItemClickListener(new EasyFilterListener.OnMenuListItemClickListener() {
            @Override
            public void onClick(EasyFilterMenu easyFilterMenu, IEasyItem iEasyItem) {
                Toast.makeText(getApplicationContext(), iEasyItem.getDisplayName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupBuntonClickListener(EasyMenuContainer easyMenuContainer) {
        Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
        Bundle bundle = new Bundle();
        if (easyMenuContainer != null) {
            SparseArray<EasyMenuStates> sparseArray = easyMenuContainer.getMenusStates();
            bundle.putSparseParcelableArray("sparseArray", sparseArray);
        }
        intent.putExtras(bundle);
        startActivity(intent);
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
