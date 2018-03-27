package com.gengli.technician.adapter;

import java.util.List;

import com.gengli.technician.R;
import com.gengli.technician.bean.Province;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	String [] provinces;
	public List<Province> list;
	int last_item;
	private int selectedPosition = -1;     
	
	public MyAdapter(Context context, List<Province> list) {
		super();
		this.context = context;
		this.list = list;
		inflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
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
		ViewHolder  holder = null;
		if(convertView==null){
			convertView = inflater.inflate(R.layout.popup_area_item, null);
			holder = new ViewHolder();
			holder.textView =(TextView)convertView.findViewById(R.id.popup_area_textview);
			holder.layout=(RelativeLayout)convertView.findViewById(R.id.popup_area_layout);
			convertView.setTag(holder);
		}
		else{
			holder=(ViewHolder)convertView.getTag();
		}
		// 设置选中效果    
		if(selectedPosition == position)   
		{   
			holder.textView.setTextColor(Color.BLUE);   
			holder.layout.setBackgroundColor(Color.LTGRAY);   
		} else {   
			holder.textView.setTextColor(Color.WHITE);   
			holder.layout.setBackgroundColor(Color.TRANSPARENT);
		}   
		
		Province province = list.get(position);
		holder.textView.setText(province.getProvince());
		holder.textView.setTextColor(Color.BLACK);

		return convertView;
	}

	public static class ViewHolder{
		public TextView textView;
		public RelativeLayout layout;
	}
	
	public void setSelectedPosition(int position) {   
		selectedPosition = position;   
	}   
}
