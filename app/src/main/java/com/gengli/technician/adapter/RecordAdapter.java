package com.gengli.technician.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gengli.technician.R;
import com.gengli.technician.bean.Article;
import com.gengli.technician.bean.Message;
import com.gengli.technician.slide.SlideBaseAdapter;
import com.gengli.technician.slide.SlideTouchView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class RecordAdapter extends SlideBaseAdapter {

    private List<Article> articleList;

    public RecordAdapter(List<Article> articleList) {
        this.articleList = articleList;
    }

    @Override
    public int[] getBindOnClickViewsIds() {
        return new int[]{R.id.btn_del};
    }

    @Override
    public int getCount() {
        return articleList.size();
    }

    @Override
    public Object getItem(int position) {
        return articleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, null);
            holder.text_record_title = (TextView) convertView.findViewById(R.id.text_record_title);
            holder.text_record_count = (TextView) convertView.findViewById(R.id.text_record_count);
            holder.text_record_time = (TextView) convertView.findViewById(R.id.text_record_time);
            holder.img_record = (ImageView) convertView.findViewById(R.id.img_record);
            holder.mSlideTouchView = (SlideTouchView) convertView.findViewById(R.id.mSlideTouchView);
            convertView.setTag(holder);
            bindSlideState(holder.mSlideTouchView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        bindSlidePosition(holder.mSlideTouchView, position);
        Article article = articleList.get(position);
        holder.text_record_title.setText(article.getTitle());
        holder.text_record_time.setText(article.getTime());
        holder.text_record_count.setText(article.getCount() + "人阅读");
        ImageLoader.getInstance().displayImage(article.getImgUrl(), holder.img_record);


        return convertView;
    }


    private class ViewHolder {
        private ImageView img_record;
        private TextView text_record_title;
        private TextView text_record_count;
        private TextView text_record_time;
        SlideTouchView mSlideTouchView;
    }

}