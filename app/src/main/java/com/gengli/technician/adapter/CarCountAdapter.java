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
import com.gengli.technician.bean.Fitting;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class CarCountAdapter extends BaseAdapter implements View.OnClickListener {

    private Context context;
    private List<CarInfo> carInfoList;
    private LayoutInflater inflater;
    private CarCountListener listener;

    public void setListener(CarCountListener listener) {
        this.listener = listener;
    }

    public CarCountAdapter(Context context, List<CarInfo> carInfoList) {
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
            convertView = inflater.inflate(R.layout.item_car_count_info, null);
            holder.item_car_count_num_text = (TextView) convertView.findViewById(R.id.item_car_count_num_text);
            holder.item_car_count_time_text = (TextView) convertView.findViewById(R.id.item_car_count_time_text);
            holder.item_car_count_del_bt = (ImageView) convertView.findViewById(R.id.item_car_count_del_bt);
            holder.item_car_count_modify_bt = (ImageView) convertView.findViewById(R.id.item_car_count_modify_bt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CarInfo carInfo = carInfoList.get(position);
        holder.item_car_count_num_text.setText("第" + (position + 1) + "车");
        holder.item_car_count_time_text.setText(carInfo.getStartTime() + "-" + carInfo.getEndTime());
        holder.item_car_count_del_bt.setOnClickListener(this);
        holder.item_car_count_modify_bt.setOnClickListener(this);
        holder.item_car_count_del_bt.setTag(position);
        holder.item_car_count_modify_bt.setTag(position);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_car_count_del_bt:
                listener.delCount(v);
                break;
            case R.id.item_car_count_modify_bt:
                listener.modifyCount(v);
                break;
        }
    }

    private class ViewHolder {
        private TextView item_car_count_num_text;
        private TextView item_car_count_time_text;
        private ImageView item_car_count_del_bt;
        private ImageView item_car_count_modify_bt;
    }


    public interface CarCountListener {
        void delCount(View v);

        void modifyCount(View v);
    }
}