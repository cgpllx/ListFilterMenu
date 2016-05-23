package cc.easyandroid.listfiltermenu.simple;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import cc.easyandroid.listfiltermenu.core.IEasyItem;
import cc.easyandroid.listfiltermenu.core.OnMenuListItemClickListener;
import cc.easyandroid.listfiltermenu.widget.EasyFileterMenu_MoreSelection;
import cc.easyandroid.listfiltermenu.widget.EasyFilterMenu;
import cc.easyandroid.listfiltermenu.widget.EasyFilterMenu_MultiSelection;
import cc.easyandroid.listfiltermenu.widget.EasyFilterMenu_SingleSelection;
import cc.easyandroid.listfiltermenu.widget.EasyListFilterMenu;

public class Main2Activity extends AppCompatActivity {
    EasyListFilterMenu easyListFilterMenu;
    EasyListFilterMenu easyListFilterMenu2;
    cc.easyandroid.listfiltermenu.widget.EasyFilterMenu_SingleSelection EasyFilterMenu_SingleSelection;
    EasyFilterMenu_MultiSelection easyFilterMenu_multiSelection;
    EasyFileterMenu_MoreSelection easyFileterMenu_moreSelection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ArrayList<Text1.ResultEntity> lists1 = dd();

        final ArrayList<Text1.ResultEntity> lists2 = dd();
        final ArrayList<Text1.ResultEntity> lists4 = dd();
        final ArrayList<Text1.ResultEntity> lists5= dd();

        easyListFilterMenu = (EasyListFilterMenu) findViewById(R.id.easyListFilterMenu);
        easyListFilterMenu2 = (EasyListFilterMenu) findViewById(R.id.easyListFilterMenu2);
        EasyFilterMenu_SingleSelection = (EasyFilterMenu_SingleSelection) findViewById(R.id.easyListFilterMenu3);
        easyFilterMenu_multiSelection = (EasyFilterMenu_MultiSelection) findViewById(R.id.easyListFilterMenu4);
        easyFileterMenu_moreSelection= (EasyFileterMenu_MoreSelection) findViewById(R.id.easyListFilterMenu5);

//        EasyFilterMenu_SingleSelection.setMenuData(false, lists2);
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
        cc.easyandroid.listfiltermenu.widget.EasyFilterMenu_SingleSelection.SingleSelectionMenuStates singleSelectionMenuStates=getIntent().getParcelableExtra("singleSelectionMenuStates");
        if(singleSelectionMenuStates!=null){
            EasyFilterMenu_SingleSelection.setMenuStates(singleSelectionMenuStates);
        }

    }
    public ArrayList<Text1.ResultEntity> dd() {
        Text1 text1 = new Gson().fromJson(Text.text, Text1.class);
        final ArrayList<Text1.ResultEntity> lists = text1.getResult();
        //IEasyItemFactory.buildIEasyItem(lists);

        return lists;
    }
}
