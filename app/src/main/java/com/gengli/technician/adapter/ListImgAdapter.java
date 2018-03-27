package com.gengli.technician.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.gengli.technician.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class ListImgAdapter extends BaseAdapter {

    private Context context;
    private List<String> imgList;

    public ListImgAdapter(Context context, List<String> imgList) {
        this.context = context;
        this.imgList = imgList;
    }

    @Override
    public int getCount() {
        return imgList.size();
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

        View view = LayoutInflater.from(context).inflate(R.layout.image_list_item, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.img_view2);
        ImageLoader.getInstance().displayImage(imgList.get(position), imageView);

        return imageView;
    }
}