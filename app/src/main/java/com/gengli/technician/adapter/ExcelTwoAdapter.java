package com.gengli.technician.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gengli.technician.R;
import com.gengli.technician.bean.Assess;
import com.gengli.technician.bean.CarInfo;
import com.gengli.technician.util.LogUtils;

import java.util.List;

public class ExcelTwoAdapter extends BaseAdapter {

    private Context context;
    private List<Assess> assessList;
    private LayoutInflater inflater;

    public ExcelTwoAdapter(Context context, List<Assess> assessList) {
        this.context = context;
        this.assessList = assessList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return assessList.size();
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
            convertView = inflater.inflate(R.layout.item_excel_two, null);
            holder.excel_two_time_text = (TextView) convertView.findViewById(R.id.excel_two_time_text);
            holder.excel_two_unit_text = (TextView) convertView.findViewById(R.id.excel_two_unit_text);
            holder.excel_two_address_text = (TextView) convertView.findViewById(R.id.excel_two_address_text);
            holder.excel_two_ywy_text = (TextView) convertView.findViewById(R.id.excel_two_ywy_text);
            holder.excel_two_ywy_phone_text = (TextView) convertView.findViewById(R.id.excel_two_ywy_phone_text);
            holder.excel_two_fzr_text = (TextView) convertView.findViewById(R.id.excel_two_fzr_text);
            holder.excel_two_fzr_phone_text = (TextView) convertView.findViewById(R.id.excel_two_fzr_phone_text);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Assess assess = assessList.get(position);

        holder.excel_two_time_text.setText(assess.getTime());
        holder.excel_two_unit_text.setText(assess.getName());
        holder.excel_two_address_text.setText(assess.getAddress());
        holder.excel_two_ywy_text.setText(assess.getSale_name());
        holder.excel_two_ywy_phone_text.setText(assess.getSale_phone());
        holder.excel_two_fzr_text.setText(assess.getLead_name());
        holder.excel_two_fzr_phone_text.setText(assess.getSale_phone());
        return convertView;
    }


    private class ViewHolder {
        private TextView excel_two_time_text;
        private TextView excel_two_unit_text;
        private TextView excel_two_address_text;
        private TextView excel_two_ywy_text;
        private TextView excel_two_ywy_phone_text;
        private TextView excel_two_fzr_text;
        private TextView excel_two_fzr_phone_text;
    }

}