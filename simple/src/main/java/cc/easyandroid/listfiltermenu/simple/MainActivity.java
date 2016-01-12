package cc.easyandroid.listfiltermenu.simple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cc.easyandroid.listfiltermenu.pojo.IEasyItem;
import cc.easyandroid.listfiltermenu.widget.EasyListFilterMenu;

public class MainActivity extends AppCompatActivity {
    EasyListFilterMenu easyListFilterMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        easyListFilterMenu = (EasyListFilterMenu) findViewById(R.id.easyListFilterMenu);
        Text1 text1 = new Gson().fromJson(Text.text, Text1.class);
        List<Text1.ResultEntity> lists = text1.getResult();
        Text1.ResultEntity resultEntity = new Text1.ResultEntity();
        resultEntity.setName("不限");

        List<Text1.ResultEntity.SubregionsEntity> lists_sub=new ArrayList<>();
        Text1.ResultEntity.SubregionsEntity resultEntity_sub = new Text1.ResultEntity.SubregionsEntity();
        resultEntity_sub.setName("不限");
        lists_sub.add(resultEntity_sub);


        List<Text1.ResultEntity.SubregionsEntity> lists_sub_sub=new ArrayList<>();
        Text1.ResultEntity.SubregionsEntity resultEntity_sub_sub = new Text1.ResultEntity.SubregionsEntity();
        resultEntity_sub_sub.setName("不限");
        lists_sub_sub.add(resultEntity_sub_sub);
        resultEntity_sub.setSub(lists_sub_sub);

        resultEntity.setSubregions(lists_sub);
        lists.add(0, resultEntity);
        easyListFilterMenu.addItems(false, lists);

        easyListFilterMenu.setOnMenuItemClickListener(new EasyListFilterMenu.OnMenuItemClickListener() {
            @Override
            public void onClick(IEasyItem iEasyItem) {
                Toast.makeText(getApplicationContext(),iEasyItem.getEasyId(),Toast.LENGTH_SHORT).show();
            }
        });
    }



}
