package com.gengli.technician.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gengli.technician.R;
import com.gengli.technician.bean.Order;

import java.util.List;

public class OrderAdapter extends BaseAdapter {

    private Context context;
    private List<Order> orderList;
    private LayoutInflater inflater;

    public OrderAdapter(Context context, List<Order> orderList) {
        super();
        this.context = context;
        this.orderList = orderList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.order_item, null);
            holder.order_type_img = (ImageView) convertView.findViewById(R.id.order_type_img);
            holder.order_id_text = (TextView) convertView.findViewById(R.id.order_id_text);
            holder.order_time_text = (TextView) convertView.findViewById(R.id.order_time_text);
            holder.order_lever_text = (TextView) convertView.findViewById(R.id.order_lever_text);
            holder.order_model_text = (TextView) convertView.findViewById(R.id.order_model_text);
            holder.order_address_text = (TextView) convertView.findViewById(R.id.order_address_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Order order = orderList.get(position);
        holder.order_id_text.setText(order.getId());
        holder.order_time_text.setText(order.getTime());
        holder.order_lever_text.setText(order.getLevel());
        holder.order_model_text.setText(order.getMachine());
        holder.order_address_text.setText(order.getExpressAddress());
        if (order.getType() == 2) {
            holder.order_type_img.setVisibility(View.VISIBLE);
            holder.order_type_img.setImageResource(R.drawable.order_begin_img);
        } else if (order.getType() == 3) {
            holder.order_type_img.setVisibility(View.VISIBLE);
            holder.order_type_img.setImageResource(R.drawable.order_ing_img);
        } else if (order.getType() == 4) {
            holder.order_type_img.setVisibility(View.VISIBLE);
            holder.order_type_img.setImageResource(R.drawable.order_ok_img);
        } else {
            holder.order_type_img.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }


    private static class ViewHolder {
        private TextView order_id_text;
        private TextView order_time_text;
        private TextView order_lever_text;
        private TextView order_model_text;
        private TextView order_address_text;
        private ImageView order_type_img;
    }
}