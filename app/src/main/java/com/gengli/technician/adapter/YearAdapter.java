package com.gengli.technician.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gengli.technician.R;

import java.util.List;

public class YearAdapter extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	public List<Integer> list;
	private int selectedPosition = -1;

	public YearAdapter(Context context, List<Integer> list) {
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
			convertView = inflater.inflate(R.layout.item_year, null);
			holder = new ViewHolder();
			holder.popup_year_textview =(TextView)convertView.findViewById(R.id.popup_year_textview);
			holder.popup_year_layout=(RelativeLayout)convertView.findViewById(R.id.popup_year_layout);
			convertView.setTag(holder);
		}
		else{
			holder=(ViewHolder)convertView.getTag();
		}
		// 设置选中效果    
		if(selectedPosition == position)   
		{   
			holder.popup_year_textview.setTextColor(Color.BLUE);
			holder.popup_year_layout.setBackgroundColor(Color.LTGRAY);
		} else {   
			holder.popup_year_textview.setTextColor(Color.WHITE);
			holder.popup_year_layout.setBackgroundColor(Color.TRANSPARENT);
		}   
		
		int i = list.get(position);
		holder.popup_year_textview.setText(i+"");
		holder.popup_year_textview.setTextColor(Color.BLACK);

		return convertView;
	}

	public static class ViewHolder{
		public TextView popup_year_textview;
		public RelativeLayout popup_year_layout;
	}

	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}
}
