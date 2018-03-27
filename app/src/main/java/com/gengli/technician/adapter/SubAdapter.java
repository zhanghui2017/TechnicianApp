package com.gengli.technician.adapter;

import java.util.List;

import com.gengli.technician.R;
import com.gengli.technician.bean.City;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SubAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    List<City> cities;
    private int selectedPosition = -1;

    public SubAdapter(Context context, List<City> cities) {
        super();
        this.context = context;
        this.cities = cities;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return cities.size();
    }

    @Override
    public Object getItem(int position) {
        return getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.popup_area_subitem, null);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.poput_sub_textview);
            viewHolder.popup_city_layout = (LinearLayout) convertView.findViewById(R.id.popup_city_layout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(selectedPosition == position)
        {
            viewHolder.textView.setTextColor(Color.BLUE);
            viewHolder.popup_city_layout.setBackgroundColor(Color.LTGRAY);
        } else {
            viewHolder.textView.setTextColor(Color.WHITE);
            viewHolder.popup_city_layout.setBackgroundColor(Color.TRANSPARENT);
        }

        City city = cities.get(position);
        viewHolder.textView.setText(city.getCity());
        viewHolder.textView.setTextColor(Color.BLACK);
        return convertView;
    }

    public static class ViewHolder {
        public TextView textView;
        public LinearLayout popup_city_layout;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }
}
