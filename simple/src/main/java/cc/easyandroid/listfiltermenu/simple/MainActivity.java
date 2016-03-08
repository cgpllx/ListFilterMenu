package cc.easyandroid.listfiltermenu.simple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cc.easyandroid.listfiltermenu.core.IEasySuperItem;
import cc.easyandroid.listfiltermenu.core.OnMenuListItemClickListener;
import cc.easyandroid.listfiltermenu.core.OnMenuWithoutDataClickLinstener;
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
//        easyPopMenu = (EasyPopMenu) findViewById(R.id.easyPopMenuid);
//        Text1 text1 = new Gson().fromJson(Text.text, Text1.class);
        final List<Text1.ResultEntity> lists1 = dd();
        final List<Text1.ResultEntity> lists2 = dd();



        easyListFilterMenu.addItems(false, lists1);

        easyListFilterMenu.setOnMenuListItemClickListener(new OnMenuListItemClickListener() {
            @Override
            public void onClick(IEasySuperItem iEasyItem) {
                Toast.makeText(getApplicationContext(), iEasyItem.getEasyId(), Toast.LENGTH_SHORT).show();
            }
        });
        easyListFilterMenu2.setOnMenuClickLinstener(new OnMenuWithoutDataClickLinstener() {
            @Override
            public void withoutData(EasyListFilterMenu menu) {
                Toast.makeText(getApplicationContext(), "没有数据", Toast.LENGTH_SHORT).show();
                menu.addItems(true, lists2);
            }
        });
//        MyEasyPopAdapter adapter= new MyEasyPopAdapter(this);
//        adapter.addAll(iEasySuperItem,iEasySuperItem);
//        easyPopMenu.setPopBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
//        easyPopMenu.setPopBackgroundDrawable(getResources().getDrawable(R.drawable.qfang_pop_bg));
//        easyPopMenu.setEasyPopMenuAdapter(adapter);
//        View v=new View(this);
//        v.setBackgroundDrawable(getResources().getDrawable(R.drawable.qfang_pop_bg));
//        easyPopMenu.setPromptView(v);
    }

    public List<Text1.ResultEntity> dd() {
        Text1 text1 = new Gson().fromJson(Text.text, Text1.class);
        final List<Text1.ResultEntity> lists = text1.getResult();


        Text1.ResultEntity resultEntity = new Text1.ResultEntity();
        resultEntity.setName("不限");

        List<Text1.ResultEntity.SubregionsEntity> lists_sub = new ArrayList<>();
        Text1.ResultEntity.SubregionsEntity resultEntity_sub = new Text1.ResultEntity.SubregionsEntity();
        resultEntity_sub.setName("不限");
        lists_sub.add(resultEntity_sub);


        List<Text1.ResultEntity.SubregionsEntity> lists_sub_sub = new ArrayList<>();
        Text1.ResultEntity.SubregionsEntity resultEntity_sub_sub = new Text1.ResultEntity.SubregionsEntity();
        resultEntity_sub_sub.setName("不限");
        lists_sub_sub.add(resultEntity_sub_sub);
        resultEntity_sub.setSub(lists_sub_sub);

        resultEntity.setSubregions(lists_sub);

        for (Text1.ResultEntity resultEntity1 : lists) {
            List list = resultEntity1.getChildItems();//
            list.add(0, resultEntity_sub_sub);
        }
        lists.add(0, resultEntity);
        return lists;
    }
}
