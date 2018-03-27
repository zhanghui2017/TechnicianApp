package com.gengli.technician.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gengli.technician.R;
import com.gengli.technician.bean.Routing;
import com.gengli.technician.util.LogUtils;

public class GetRoutingFrament extends Fragment {

    private TextView text_routing_time;
    private TextView text_routing_address;
    private TextView text_routing_content;
    private TextView text_routing_machine;
    private TextView text_routing_model;
    private TextView text_routing_phone;
    private TextView text_routing_charge_name;
    //	private View mViewContent; // 缓存视图内容
    ArrayList<Routing> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        list = (ArrayList<Routing>) bundle.getSerializable("test_bundle");

//		if (mViewContent == null) {
        View mViewContent = inflater.inflate(R.layout.fragment_get_routing, container, false);
        text_routing_time = (TextView) mViewContent.findViewById(R.id.text_routing_time);
        text_routing_address = (TextView) mViewContent.findViewById(R.id.text_routing_address);
        text_routing_content = (TextView) mViewContent.findViewById(R.id.text_routing_content);
        text_routing_machine = (TextView) mViewContent.findViewById(R.id.text_routing_machine);
        text_routing_model = (TextView) mViewContent.findViewById(R.id.text_routing_model);
        text_routing_phone = (TextView) mViewContent.findViewById(R.id.text_routing_phone);
        text_routing_charge_name = (TextView) mViewContent.findViewById(R.id.text_routing_charge_name);

        String tag = getTag();
        for (int i = 0; i < list.size(); i++) {
            String type = list.get(i).getType();
            if (tag.equals(type)) {
                LogUtils.showLogD("------2-------->   " + list.get(i).toString());
                text_routing_time.setText(list.get(i).getTime());
                text_routing_address.setText(list.get(i).getAddress());
                text_routing_content.setText(list.get(i).getJobContent());
                text_routing_machine.setText(list.get(i).getMachine());
                text_routing_model.setText(list.get(i).getModel());
                text_routing_phone.setText(list.get(i).getPhone());
                text_routing_charge_name.setText(list.get(i).getChargeName());

            }
        }
//		}


//		// 缓存View判断是否含有parent, 如果有需要从parent删除, 否则发生已有parent的错误.
//		ViewGroup parent = (ViewGroup) mViewContent.getParent();
//		if (parent != null) {
//			parent.removeView(mViewContent);
//		}
        return mViewContent;
    }

//	@Override
//	public void onViewCreated(View view, Bundle savedInstanceState) {
//		super.onViewCreated(view, savedInstanceState);
//
//	}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}