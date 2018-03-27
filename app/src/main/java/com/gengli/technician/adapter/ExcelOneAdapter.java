package com.gengli.technician.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gengli.technician.R;
import com.gengli.technician.bean.CarInfo;

import java.util.List;

public class ExcelOneAdapter extends BaseAdapter {

    private Context context;
    private List<CarInfo> carInfoList;
    private LayoutInflater inflater;

    public ExcelOneAdapter(Context context, List<CarInfo> carInfoList) {
        this.context = context;
        this.carInfoList = carInfoList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return carInfoList.size();
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
            convertView = inflater.inflate(R.layout.item_excel_one, null);
            holder.item_one_title = (TextView) convertView.findViewById(R.id.item_one_title);
            holder.item_one_start_time = (TextView) convertView.findViewById(R.id.item_one_start_time);
            holder.item_one_end_time = (TextView) convertView.findViewById(R.id.item_one_end_time);
            holder.item_one_weight = (TextView) convertView.findViewById(R.id.item_one_weight);
            holder.item_one_desc = (TextView) convertView.findViewById(R.id.item_one_desc);
            holder.item_one_other = (TextView) convertView.findViewById(R.id.item_one_other);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CarInfo carInfo = carInfoList.get(position);
        holder.item_one_title.setText((position + 1) + "车信息");
        holder.item_one_start_time.setText("开始时间：" + carInfo.getStartTime());
        holder.item_one_end_time.setText("结束时间：" + carInfo.getEndTime());
        holder.item_one_weight.setText("喷浆方量：" + carInfo.getWeight());
        holder.item_one_desc.setText("运行情况：" + carInfo.getDesc());
        holder.item_one_other.setText("其他因素：" + carInfo.getOther());
        return convertView;
    }


    private class ViewHolder {
        private TextView item_one_title;
        private TextView item_one_start_time;
        private TextView item_one_end_time;
        private TextView item_one_weight;
        private TextView item_one_desc;
        private TextView item_one_other;

    }

}