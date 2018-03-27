package com.gengli.technician.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gengli.technician.R;
import com.gengli.technician.bean.Fitting;
import com.gengli.technician.bean.Order;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class FittingInfoAdapter extends BaseAdapter {

    private Context context;
    private List<Fitting> fittingList;
    private LayoutInflater inflater;

    public FittingInfoAdapter(Context context, List<Fitting> fittingList) {
        super();
        this.context = context;
        this.fittingList = fittingList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return fittingList.size();
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
            convertView = inflater.inflate(R.layout.item_order_ing_fitting_info, parent, false);
            holder.img_ing_fitting_info = (ImageView) convertView.findViewById(R.id.img_ing_fitting_info);
            holder.text_ing_fitting_info_name = (TextView) convertView.findViewById(R.id.text_ing_fitting_info_name);
            holder.text_ing_fitting_info_count = (TextView) convertView.findViewById(R.id.text_ing_fitting_info_count);
            holder.text_ing_fitting_info_price = (TextView) convertView.findViewById(R.id.text_ing_fitting_info_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Fitting fitting = fittingList.get(position);
        holder.text_ing_fitting_info_name.setText(fitting.getName());
        holder.text_ing_fitting_info_count.setText(fitting.getCount()+"件");
        holder.text_ing_fitting_info_price.setText(fitting.getPrice()+"元");
        ImageLoader.getInstance().displayImage(fitting.getImgUrl(), holder.img_ing_fitting_info);
        return convertView;
    }


    private static class ViewHolder {
        private ImageView img_ing_fitting_info;
        private TextView text_ing_fitting_info_name;
        private TextView text_ing_fitting_info_count;
        private TextView text_ing_fitting_info_price;
    }
}