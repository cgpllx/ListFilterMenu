package cc.easyandroid.listfiltermenu.core;


import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cc.easyandroid.listfiltermenu.R;


public class ListFilterAdapter<T extends IEasyItem> extends EasyListFilterBaseAdaptre<T> {

    LayoutInflater inflater;
    int listItemViewResourceId = 0;
    private IEasyItem parentIEasyItem;

    public ListFilterAdapter(Context context, int listItemViewResourceId) {
        inflater = LayoutInflater.from(context);
        this.listItemViewResourceId = listItemViewResourceId;
    }

    @Override
    public int getCount() {
        if (parentIEasyItem != null) {
            List<? extends IEasyItem> childItems = parentIEasyItem.getChildItems();
            if (childItems != null) {
                return childItems.size();
            }
        }
        return 0;
    }

    @Override
    public IEasyItem getItem(int position) {
        if (parentIEasyItem != null) {
            List<? extends IEasyItem> childItems = parentIEasyItem.getChildItems();
            if (childItems != null) {
                return childItems.get(position);
            }
        }
        return null;
    }

    public void setParentIEasyItem(IEasyItem parentIEasyItem) {
        this.parentIEasyItem = parentIEasyItem;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null || convertView.getTag() == null) {
            convertView = inflater.inflate(listItemViewResourceId, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        IEasyItem iEasyItem = getItem(position);
        if (iEasyItem != null) {
            String displayName = iEasyItem.getDisplayName();
            int posion = iEasyItem.getChildSelectPosion();
            if (!TextUtils.isEmpty(displayName)) {
                viewHolder.name.setText(displayName);
            }
            if (posion > 0) {
                viewHolder.name.setSelected(true);
            }else{
                viewHolder.name.setSelected(false);
            }
        }
        return convertView;
    }

    public IEasyItem getParentIEasyItem() {
        return parentIEasyItem;
    }

    final class ViewHolder {
        public ViewHolder(View convertView) {
            name = (TextView) convertView.findViewById(R.id.easylistfilter_itemdisplayname);
        }
        TextView name;
    }
}
