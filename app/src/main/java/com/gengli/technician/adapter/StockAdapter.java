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
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class StockAdapter extends BaseAdapter {

    private Context context;
    private List<Fitting> fittingList;
    private LayoutInflater inflater;

    public StockAdapter(Context context, List<Fitting> fittingList) {
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
            convertView = inflater.inflate(R.layout.item_stock, null);
            holder.img_stock_count = (TextView) convertView.findViewById(R.id.img_stock_count);
            holder.img_stock_name = (TextView) convertView.findViewById(R.id.img_stock_name);
            holder.img_stock = (ImageView) convertView.findViewById(R.id.img_stock);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Fitting fitting = fittingList.get(position);
        holder.img_stock_name.setText(fitting.getName());
        holder.img_stock_count.setText("剩余" + fitting.getLastCount() + "件");
        ImageLoader.getInstance().displayImage(fitting.getImgUrl(), holder.img_stock);
        return convertView;
    }


    private class ViewHolder {
        private ImageView img_stock;
        private TextView img_stock_name;
        private TextView img_stock_count;
    }

}