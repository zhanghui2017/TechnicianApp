package com.gengli.technician.adapter;

import java.util.HashMap;
import java.util.Map;

import com.gengli.technician.R;
import com.gengli.technician.util.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OrderDescAdapter extends BaseAdapter {
    private Context context;
    public String[] types;
    private LayoutInflater inflater;

    public Map<Integer, Boolean> selectedMap;

    public OrderDescAdapter(Context context, String[] types) {
        super();
        this.context = context;
        this.types = types;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //初始化选中记录集合
        if (selectedMap == null) {
            selectedMap = new HashMap<Integer, Boolean>();
        }
        changeSelected(0);
    }

    @Override
    public int getCount() {
        // TODO 自动生成的方法存根
        return types.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO 自动生成的方法存根
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO 自动生成的方法存根
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_order_desc_type, parent, false);
            holder.text_order_desc_type = (TextView) convertView.findViewById(R.id.text_order_desc_type);
            holder.item_order_desc_line = convertView.findViewById(R.id.item_order_desc_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.text_order_desc_type.setText(types[position]);
        if (selectedMap.get(position)) {
            holder.text_order_desc_type.setTextColor(Util.setColor(context,R.color.color_order_bg_4373f1));
            holder.item_order_desc_line.setVisibility(View.VISIBLE);
        } else {
            holder.text_order_desc_type.setTextColor(Util.setColor(context,R.color.color_main_fun_text));
            holder.item_order_desc_line.setVisibility(View.GONE);
        }
        return convertView;
    }

    private static class ViewHolder {
        public TextView text_order_desc_type;
        public View item_order_desc_line;
    }

    //设置默认值
    public void changeSelected(int position) {
        for (int i = 0; i < types.length; i++) {
            if (position == i) {
                selectedMap.put(i, true);
            } else {
                selectedMap.put(i, false);
            }
        }
    }
}
