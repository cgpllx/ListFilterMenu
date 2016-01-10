package cc.easyandroid.listfiltermenu.adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cc.easyandroid.listfiltermenu.R;
import cc.easyandroid.listfiltermenu.pojo.IEasyItem;


public class ListFilterAdapter<T extends IEasyItem> extends ArrayAdapter<T> {

    LayoutInflater inflater;
    Drawable backgroundDrawable;
    public ListFilterAdapter(Context context, Drawable backgroundDrawable) {
        inflater = LayoutInflater.from(context);
        this.backgroundDrawable = backgroundDrawable;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listfilter_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setBackgroundDrawable(backgroundDrawable.getConstantState().newDrawable());
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        IEasyItem iEasyItem = getItem(position);
        if (iEasyItem != null) {
            String displayName = iEasyItem.getDisplayName();
            if (!TextUtils.isEmpty(displayName)) {
                viewHolder.name.setText(displayName);
            }
        }
        return convertView;
    }

    final class ViewHolder {
        public ViewHolder(View convertView) {
            name = (TextView) convertView.findViewById(R.id.name);
//            select = (ImageView) convertView.findViewById(R.id.main_select);
        }

        TextView name;
//        ImageView select;
    }
}
