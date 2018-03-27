package com.gengli.technician.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.gengli.technician.R;
import com.gengli.technician.activity.RepairDetailActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class ListVideoAdapter extends BaseAdapter {

    private Context context;
    private List<String> imgList;

    public ListVideoAdapter(Context context, List<String> imgList) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.video_list_item, null);
//        ImageView imageView = (ImageView) view.findViewById(R.id.img_video_bt);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, RepairDetailActivity.class);
//                intent.putExtra("video_url", imgList.get(position));
//                context.startActivity(intent);
//            }
//        });
        return view;
    }
}