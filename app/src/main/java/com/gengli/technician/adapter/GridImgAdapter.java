package com.gengli.technician.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.gengli.technician.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class GridImgAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<String> fittingList;

    public GridImgAdapter(Context context, List<String> fittingList) {
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
            convertView = inflater.inflate(R.layout.image_grid_item, parent, false);
            holder.begin_fitting_img_view = (ImageView) convertView.findViewById(R.id.begin_img_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String fitting = fittingList.get(position);
        Log.d("test", " ------------------>"+fitting);
        ImageLoader.getInstance().displayImage(fitting, holder.begin_fitting_img_view);
        return convertView;
    }

    private static class ViewHolder {
        private ImageView begin_fitting_img_view;
    }
}