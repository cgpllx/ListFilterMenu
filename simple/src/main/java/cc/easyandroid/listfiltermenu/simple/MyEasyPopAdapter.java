package cc.easyandroid.listfiltermenu.simple;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cc.easyandroid.listfiltermenu.core.EasyPopMenuAdapter;
import cc.easyandroid.listfiltermenu.core.IEasySuperItem;

/**
 * Created by Administrator on 2016/2/2.
 */
public class MyEasyPopAdapter<T extends IEasySuperItem> extends EasyPopMenuAdapter<T> {
    private final Context context;
    public MyEasyPopAdapter(Context context){
        this.context=context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView=new TextView(context);
        textView.setHeight(80);
        textView.setText("测试");
        textView.setBackgroundColor(context.getResources().getColor(R.color.q_ff0000));
        return textView;
    }


}
