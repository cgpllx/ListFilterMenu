package cc.easyandroid.listfiltermenu;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cc.easyandroid.listfiltermenu.pojo.IEasyItem;
import cc.easyandroid.listfiltermenu.text.Text;
import cc.easyandroid.listfiltermenu.text.Text1;
import cc.easyandroid.listfiltermenu.widget.EasyListFilterMenu;

public class MainActivity extends AppCompatActivity {
    EasyListFilterMenu easyListFilterMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
//        MultiDex.install(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
