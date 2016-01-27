package cc.easyandroid.listfiltermenu.core;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cc.easyandroid.listfiltermenu.R;


public class ListFilterAdapter<T extends IEasyItem> extends EasyListFilterBaseAdaptre<T> {

    LayoutInflater inflater;
    Drawable backgroundDrawable;
    Drawable listItemDrawableRight = null;
    ColorStateList listItemTextColor;
    int listItemTextSize = 15;
    int listItemViewResourceId = 0;

    public void setListItemViewResourceId(int listItemViewResourceId) {
        this.listItemViewResourceId = listItemViewResourceId;
    }

    public void setListItemTextSize(int listItemTextSize) {
        this.listItemTextSize = listItemTextSize;
    }

    public void setListItemTextColor(ColorStateList listItemTextColor) {
        this.listItemTextColor = listItemTextColor;
    }

    public void setListItemDrawableRight(Drawable listItemDrawableRight) {
        this.listItemDrawableRight = listItemDrawableRight;
    }

    public ListFilterAdapter(Context context, Drawable backgroundDrawable) {
        inflater = LayoutInflater.from(context);
        this.backgroundDrawable = backgroundDrawable;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(listItemViewResourceId, parent, false);
            viewHolder = new ViewHolder(convertView);
            if(backgroundDrawable!=null){
                convertView.setBackgroundDrawable(backgroundDrawable.getConstantState().newDrawable());
            }
            if (listItemTextColor != null) {
                viewHolder.name.setTextColor(listItemTextColor);
            }
            viewHolder.name.setTextSize(listItemTextSize);
            if(listItemDrawableRight!=null){
                viewHolder.name.setCompoundDrawablesWithIntrinsicBounds(null, null, listItemDrawableRight.getConstantState().newDrawable(), null);
            }
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
            name = (TextView) convertView.findViewById(R.id.easylistfilter_itemdisplayname);
        }
        TextView name;
    }
}
