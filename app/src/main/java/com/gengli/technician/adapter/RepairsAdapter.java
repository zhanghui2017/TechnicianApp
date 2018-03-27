package com.gengli.technician.adapter;

import android.content.Context;
import android.content.Intent;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gengli.technician.R;
import com.gengli.technician.activity.RepairDetailActivity;
import com.gengli.technician.bean.Order;

import java.util.List;

public class RepairsAdapter extends BaseAdapter{

    private Context context;
    private List<Order> repairList;
    private LayoutInflater inflater;

    public RepairsAdapter(Context context, List<Order> repairList) {
        super();
        this.context = context;
        this.repairList = repairList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return repairList.size();
    }

    @Override
    public Object getItem(int position) {
        return repairList.get(position);
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
            convertView = inflater.inflate(R.layout.repair_item, null);
            holder.repair_name_phone_text = (TextView) convertView.findViewById(R.id.repairs_name_phone_text);
            holder.repair_start_time_text = (TextView) convertView.findViewById(R.id.repairs_start_time_text);
            holder.repair_end_time_text = (TextView) convertView.findViewById(R.id.repairs_end_time_text);
            holder.repair_details_img = (ImageButton) convertView.findViewById(R.id.repair_details_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Order order = repairList.get(position);
        holder.repair_name_phone_text.setText(order.getChargeName() + "  " + order.getChargePhone());
        holder.repair_start_time_text.setText(order.getStartTime());
        holder.repair_end_time_text.setText(order.getEndTime());
        holder.repair_details_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RepairDetailActivity.class);
                intent.putExtra("order_details", order);
                context.startActivity(intent);
            }
        });
        return convertView;
    }


    private class ViewHolder {
        private TextView repair_name_phone_text;
        private TextView repair_start_time_text;
        private TextView repair_end_time_text;
        private ImageButton repair_details_img;
    }
}