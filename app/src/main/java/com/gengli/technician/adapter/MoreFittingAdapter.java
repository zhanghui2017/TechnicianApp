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

public class MoreFittingAdapter extends BaseAdapter implements View.OnClickListener {

    private Context context;
    private List<Fitting> fittingList;
    private LayoutInflater inflater;
    private MoreFittingCountListener listener;

    public MoreFittingAdapter(Context context, List<Fitting> fittingList) {
        this.context = context;
        this.fittingList = fittingList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setFittingCountListener(MoreFittingCountListener listener) {
        this.listener = listener;
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
            convertView = inflater.inflate(R.layout.item_more_fitting, null);
            holder.text_more_fitting_name = (TextView) convertView.findViewById(R.id.text_more_fitting_name);
            holder.text_more_fitting_last_count = (TextView) convertView.findViewById(R.id.text_more_fitting_last_count);
            holder.text_more_fitting_cho_count = (TextView) convertView.findViewById(R.id.text_more_fitting_cho_count);
            holder.img_more_fitting = (ImageView) convertView.findViewById(R.id.img_more_fitting);
            holder.img_more_minus_bt = (ImageView) convertView.findViewById(R.id.img_more_minus_bt);
            holder.img_more_add_bt = (ImageView) convertView.findViewById(R.id.img_more_add_bt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Fitting fitting = fittingList.get(position);
        holder.text_more_fitting_name.setText(fitting.getName());
        holder.text_more_fitting_last_count.setText("" + fitting.getLastCount());
        ImageLoader.getInstance().displayImage(fitting.getImgUrl(), holder.img_more_fitting);
        holder.text_more_fitting_cho_count.setText(fitting.getCount() + "件");
        holder.img_more_minus_bt.setOnClickListener(this);
        holder.img_more_add_bt.setOnClickListener(this);
        holder.img_more_minus_bt.setTag(position);
        holder.img_more_add_bt.setTag(position);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        if (null == listener) return;
        switch (v.getId()) {
            case R.id.img_more_add_bt:
                listener.addCount(v);
                break;
            case R.id.img_more_minus_bt:
                listener.minusCount(v);
                break;
        }
    }

    private class ViewHolder {
        private ImageView img_more_fitting;
        private TextView text_more_fitting_name;
        private TextView text_more_fitting_last_count;
        private ImageView img_more_minus_bt;
        private ImageView img_more_add_bt;
        private TextView text_more_fitting_cho_count;
    }

    public interface MoreFittingCountListener {
        void addCount(View v);
        void minusCount(View v);
    }
}