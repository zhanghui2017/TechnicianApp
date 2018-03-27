package com.gengli.technician.adapter;

import java.util.List;

import com.gengli.technician.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchWordAdapter extends BaseAdapter {
	
	private Context context;
	public List<String> list;
	private LayoutInflater inflater;
	
	public SearchWordAdapter(Context context, List<String> list) {
		super();
		this.context = context;
		this.list = list;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_search_key_word,null);
			
			holder.item_search_key_word = (TextView) convertView.findViewById(R.id.item_search_key_word);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.item_search_key_word.setText(list.get(position));
		
		return convertView;
	}
	private static class ViewHolder{
		public TextView item_search_key_word;
	}
	
}
