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

public class RelatedFittingAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Fitting> fittingList;

    public RelatedFittingAdapter(Context context, List<Fitting> fittingList) {
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
            convertView = inflater.inflate(R.layout.item_related_fitting, parent, false);
            holder.fitting_img_view = (ImageView) convertView.findViewById(R.id.fitting_img_view);
            holder.fitting_text_view = (TextView) convertView.findViewById(R.id.fitting_text_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Fitting fitting = fittingList.get(position);
        holder.fitting_text_view.setText(fitting.getName());
        ImageLoader.getInstance().displayImage(fitting.getImgUrl(), holder.fitting_img_view);
        return convertView;
    }

    private static class ViewHolder {
        private ImageView fitting_img_view;
        private TextView fitting_text_view;
    }
}