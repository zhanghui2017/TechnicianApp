package com.gengli.technician.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gengli.technician.R;
import com.gengli.technician.bean.Article;
import com.gengli.technician.slide.SlideBaseAdapter;
import com.gengli.technician.slide.SlideTouchView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class HelpAdapter extends BaseAdapter {

    private List<Article> articleList;

    public HelpAdapter(List<Article> articleList) {
        this.articleList = articleList;
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_help, null);
            holder.text_help_title = (TextView) convertView.findViewById(R.id.text_help_title);
            holder.text_help_count = (TextView) convertView.findViewById(R.id.text_help_count);
            holder.text_help_time = (TextView) convertView.findViewById(R.id.text_help_time);
            holder.img_help = (ImageView) convertView.findViewById(R.id.img_help);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Article article = articleList.get(position);
        holder.text_help_title.setText(article.getTitle());
        holder.text_help_time.setText(article.getTime());
        holder.text_help_count.setText(article.getCount() + "人阅读");
        ImageLoader.getInstance().displayImage(article.getImgUrl(), holder.img_help);
//        holder.img_help.setImageResource(R.drawable.img_zhinan_tmp);


        return convertView;
    }


    private class ViewHolder {
        private ImageView img_help;
        private TextView text_help_title;
        private TextView text_help_count;
        private TextView text_help_time;
    }

}