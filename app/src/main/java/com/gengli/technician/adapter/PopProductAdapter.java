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
import com.gengli.technician.bean.Province;

import java.util.List;

public class PopProductAdapter extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	public String[] productList;
	private int selectedPosition = -1;

	public PopProductAdapter(Context context, String[] productList) {
		super();
		this.context = context;
		this.productList = productList;
		inflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return productList.length;
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
			convertView = inflater.inflate(R.layout.popup_product_item, null);
			holder = new ViewHolder();
			holder.textView =(TextView)convertView.findViewById(R.id.popup_product_textview);
			holder.layout=(RelativeLayout)convertView.findViewById(R.id.popup_product_layout);
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
		

		holder.textView.setText(productList[position]);
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
