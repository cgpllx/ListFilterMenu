package cc.easyandroid.listfiltermenu.simple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import cc.easyandroid.listfiltermenu.core.IEasyItem;
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

        easyListFilterMenu.addItems(false, lists1);
        easyListFilterMenu.setOnMenuListItemClickListener(new EasyListFilterMenu.OnMenuListItemClickListener() {
            @Override
            public void onClick(IEasyItem iEasyItem) {
                Toast.makeText(getApplicationContext(), iEasyItem.getEasyId(), Toast.LENGTH_SHORT).show();
            }
        });
        easyListFilterMenu2.setOnMenuClickLinstener(new EasyListFilterMenu.OnMenuWithoutDataClickLinstener() {
            @Override
            public void withoutData(EasyListFilterMenu menu) {
                Toast.makeText(getApplicationContext(), "没有数据", Toast.LENGTH_SHORT).show();
                menu.addItems(true, lists2);
            }
        });
    }

    public List<Text1.ResultEntity> dd() {
        Text1 text1 = new Gson().fromJson(Text.text, Text1.class);
        final List<Text1.ResultEntity> lists = text1.getResult();
//        IEasyItemFactory.buildIEasyItem(lists);
        return lists;
    }
}
