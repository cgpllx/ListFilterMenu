package cc.easyandroid.listfiltermenu.core;


import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cc.easyandroid.listfiltermenu.R;


public class ListFilterAdapter<T extends IEasyItem> extends BaseAdapter {

    private LayoutInflater inflater;
    private int listItemViewResourceId = 0;
    private IEasyItem parentIEasyItem;

    public ListFilterAdapter(Context context, int listItemViewResourceId) {
        inflater = LayoutInflater.from(context);
        this.listItemViewResourceId = listItemViewResourceId;
    }

    @Override
    public long getItemId(int position) {
        return position;
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
            CharSequence displayName = iEasyItem.getDisplayName();
            int posion = iEasyItem.getChildSelectPosion();
            if (!TextUtils.isEmpty(displayName)) {
                viewHolder.name.setText(displayName);
            }
            if (iEasyItem.isChildSelected()) {// 判断子类是否被选择，如果是，将自己的变色（通过选择器变色）
                viewHolder.name.setSelected(true);
            } else {
                viewHolder.name.setSelected(false);
            }
            if (viewHolder.arrowImage != null) {
                if (position == 0 && iEasyItem.isNoLimitItem()) {
                    viewHolder.arrowImage.setVisibility(View.GONE);
                } else {
                    viewHolder.arrowImage.setVisibility(View.VISIBLE);
                }
            }
        }
        return convertView;
    }

    public IEasyItem getParentIEasyItem() {
        return parentIEasyItem;
    }

    final class ViewHolder {
        public ViewHolder(View convertView) {
            if (convertView instanceof TextView) {
                name = (TextView) convertView;
            } else {
                name = (TextView) convertView.findViewById(R.id.easyListFilter_ItemDisplayName);
                arrowImage = (ImageView) convertView.findViewById(R.id.easyListFilter_ItemDisplayImage);
            }
        }

        TextView name;
        ImageView arrowImage;
    }

    /**
     * 删除所以的child 位置记录信息
     */
    public void clearAllChildPosion() {
        int count = getCount();
        for (int i = 0; i < count; i++) {
            IEasyItem easyItem = getItem(i);
            easyItem.setChildSelectPosion(0);
            easyItem.setChildSelected(false);
        }
    }
}
