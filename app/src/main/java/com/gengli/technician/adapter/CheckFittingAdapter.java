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

public class CheckFittingAdapter extends BaseAdapter{

    private Context context;
    private List<Fitting> fittingList;
    private LayoutInflater inflater;

    public CheckFittingAdapter(Context context, List<Fitting> fittingList) {
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
            convertView = inflater.inflate(R.layout.item_check_fitting, null);
            holder.text_check_fitting_name = (TextView) convertView.findViewById(R.id.text_check_fitting_name);
            holder.text_check_fitting_count = (TextView) convertView.findViewById(R.id.text_check_fitting_count);
            holder.img_check_fitting = (ImageView) convertView.findViewById(R.id.img_check_fitting);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Fitting fitting = fittingList.get(position);
        holder.text_check_fitting_name.setText(fitting.getName());
        holder.text_check_fitting_count.setText(fitting.getCount()+"件");
        ImageLoader.getInstance().displayImage(fitting.getImgUrl(), holder.img_check_fitting);
        return convertView;
    }



    private class ViewHolder {
        private ImageView img_check_fitting;
        private TextView text_check_fitting_name;
        private TextView text_check_fitting_count;
    }

}