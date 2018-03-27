package com.gengli.technician.adapter;

import java.util.List;

import com.gengli.technician.R;
import com.gengli.technician.bean.HotKey;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchKeyAdapter extends BaseAdapter {
	
	public List<String> list;
	private LayoutInflater inflater;
	private Context context;
	
	public SearchKeyAdapter(List<String> list, Context context) {
		super();
		this.list = list;
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		
		return list.size() > 9 ? 9 : list.size();
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
			convertView = inflater.inflate(R.layout.item_search_key, null);
			holder.item_search_key_t = (TextView) convertView.findViewById(R.id.item_search_key_t);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.item_search_key_t.setText(list.get(position));
		
		return convertView;
	}
	private static class ViewHolder{
		private TextView item_search_key_t;
	}
}
