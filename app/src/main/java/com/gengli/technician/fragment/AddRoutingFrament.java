package com.gengli.technician.fragment;

import com.gengli.technician.R;
import com.gengli.technician.activity.AddRoutingActivity;
import com.gengli.technician.bean.Routing;
import com.gengli.technician.util.SharePreferencesUtil;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddRoutingFrament extends Fragment implements OnClickListener {
    public static final String ADDRESS = "address";
    public static final String JOBCONTENT = "jobcontent";
    public static final String MACHINE = "machine";
    public static final String MODEL = "model";
    public static final String NAMEPHONE = "namePhone";
    public static final String CHARGENAME = "chargeName";
    public static final String TYPE = "type";

    private View mViewContent; // 缓存视图内容
    private TextView text_fragment_add_address;
    private EditText edit_fragment_add_address;
    private TextView text_fragment_add_jobcontent;
    private EditText edit_fragment_add_jobcontent;
    private TextView text_fragment_add_machine;
    private EditText edit_fragment_add_machine;
    private TextView text_fragment_add_model;
    private EditText edit_fragment_add_model;
    private TextView text_fragment_add_name_phone;
    private EditText edit_fragment_add_name_phone;
    private TextView text_fragment_add_charge_name;
    private EditText edit_fragment_add_charge_name;
    private TextView text_fragment_add_day;
    private String chooseDay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        chooseDay = bundle.getString("test_day");
        if (mViewContent == null) {
            mViewContent = inflater.inflate(R.layout.fragment_add_routing, container, false);
            text_fragment_add_address = (TextView) mViewContent.findViewById(R.id.text_fragment_add_address);
            edit_fragment_add_address = (EditText) mViewContent.findViewById(R.id.edit_fragment_add_address);
            text_fragment_add_jobcontent = (TextView) mViewContent.findViewById(R.id.text_fragment_add_jobcontent);
            edit_fragment_add_jobcontent = (EditText) mViewContent.findViewById(R.id.edit_fragment_add_jobcontent);
            text_fragment_add_machine = (TextView) mViewContent.findViewById(R.id.text_fragment_add_machine);
            edit_fragment_add_machine = (EditText) mViewContent.findViewById(R.id.edit_fragment_add_machine);
            text_fragment_add_model = (TextView) mViewContent.findViewById(R.id.text_fragment_add_model);
            edit_fragment_add_model = (EditText) mViewContent.findViewById(R.id.edit_fragment_add_model);
            text_fragment_add_name_phone = (TextView) mViewContent.findViewById(R.id.text_fragment_add_name_phone);
            edit_fragment_add_name_phone = (EditText) mViewContent.findViewById(R.id.edit_fragment_add_name_phone);
            text_fragment_add_charge_name = (TextView) mViewContent.findViewById(R.id.text_fragment_add_charge_name);
            edit_fragment_add_charge_name = (EditText) mViewContent.findViewById(R.id.edit_fragment_add_charge_name);
            text_fragment_add_day = (TextView) mViewContent.findViewById(R.id.text_fragment_add_day);
            text_fragment_add_day.setText(chooseDay);


            text_fragment_add_address.setOnClickListener(this);
            text_fragment_add_jobcontent.setOnClickListener(this);
            text_fragment_add_machine.setOnClickListener(this);
            text_fragment_add_model.setOnClickListener(this);
            text_fragment_add_name_phone.setOnClickListener(this);
            text_fragment_add_charge_name.setOnClickListener(this);
        }
        // 缓存View判断是否含有parent, 如果有需要从parent删除, 否则发生已有parent的错误.
        ViewGroup parent = (ViewGroup) mViewContent.getParent();
        if (parent != null) {
            parent.removeView(mViewContent);
        }
        return mViewContent;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_fragment_add_address:
                text_fragment_add_address.setVisibility(View.GONE);
                edit_fragment_add_address.setVisibility(View.VISIBLE);
                ((AddRoutingActivity) getActivity()).isEditShow = true;
                break;
            case R.id.text_fragment_add_jobcontent:
                text_fragment_add_jobcontent.setVisibility(View.GONE);
                edit_fragment_add_jobcontent.setVisibility(View.VISIBLE);
                ((AddRoutingActivity) getActivity()).isEditShow = true;
                break;
            case R.id.text_fragment_add_machine:
                text_fragment_add_machine.setVisibility(View.GONE);
                edit_fragment_add_machine.setVisibility(View.VISIBLE);
                ((AddRoutingActivity) getActivity()).isEditShow = true;
                break;
            case R.id.text_fragment_add_model:
                text_fragment_add_model.setVisibility(View.GONE);
                edit_fragment_add_model.setVisibility(View.VISIBLE);
                ((AddRoutingActivity) getActivity()).isEditShow = true;
                break;
            case R.id.text_fragment_add_name_phone:
                text_fragment_add_name_phone.setVisibility(View.GONE);
                edit_fragment_add_name_phone.setVisibility(View.VISIBLE);
                ((AddRoutingActivity) getActivity()).isEditShow = true;
                break;
            case R.id.text_fragment_add_charge_name:
                text_fragment_add_charge_name.setVisibility(View.GONE);
                edit_fragment_add_charge_name.setVisibility(View.VISIBLE);
                ((AddRoutingActivity) getActivity()).isEditShow = true;
                break;

            default:
                break;
        }

    }

    String tag;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tag = getTag();
        if (tag.equals(AddRoutingActivity.types[0])) {
            Log.d("test", "-----0-----");
        } else if (tag.equals(AddRoutingActivity.types[1])) {
            Log.d("test", "-----1-----");
        } else if (tag.equals(AddRoutingActivity.types[2])) {
            Log.d("test", "-----2-----");
        } else if (tag.equals(AddRoutingActivity.types[3])) {
            Log.d("test", "-----3-----");
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        String address = edit_fragment_add_address.getText().toString();
        String jobContent = edit_fragment_add_jobcontent.getText().toString();
        String machine = edit_fragment_add_machine.getText().toString();
        String model = edit_fragment_add_model.getText().toString();
        String namePhone = edit_fragment_add_name_phone.getText().toString();
        String chargeName = edit_fragment_add_charge_name.getText().toString();
//        Log.d("test", "-----address-----" + address);
//        Log.d("test", "-----jobContent-----" + jobContent);
//        Log.d("test", "-----machine-----" + machine);
//        Log.d("test", "-----model-----" + model);
//        Log.d("test", "-----namePhone-----" + namePhone);
//        Log.d("test", "-----chaegeName-----" + chargeName);

//        Log.d("test", "-------->切换之前的tab-------->" + tag);
        if (tag.equals(AddRoutingActivity.types[0])) {
            Log.d("test", "----开始保存培训---->");
            SharedPreferences preferences = getActivity().getSharedPreferences(AddRoutingActivity.ROUTING_PEIXUN, Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(ADDRESS, address);
            editor.putString(JOBCONTENT, jobContent);
            editor.putString(MACHINE, machine);
            editor.putString(MODEL, model);
            editor.putString(NAMEPHONE, namePhone);
            editor.putString(CHARGENAME, chargeName);
            editor.putString(TYPE, "培训");
            editor.commit();
        } else if (tag.equals(AddRoutingActivity.types[1])) {
            Log.d("test", "----开始保存巡检---->");
            SharedPreferences preferences = getActivity().getSharedPreferences(AddRoutingActivity.ROUTING_XUNJIAN, Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(ADDRESS, address);
            editor.putString(JOBCONTENT, jobContent);
            editor.putString(MACHINE, machine);
            editor.putString(MODEL, model);
            editor.putString(NAMEPHONE, namePhone);
            editor.putString(CHARGENAME, chargeName);
            editor.putString(TYPE, "巡检");
            editor.commit();
        } else if (tag.equals(AddRoutingActivity.types[2])) {
            Log.d("test", "----开始保存维修---->");
            SharedPreferences preferences = getActivity().getSharedPreferences(AddRoutingActivity.ROUTING_WEIXIU, Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(ADDRESS, address);
            editor.putString(JOBCONTENT, jobContent);
            editor.putString(MACHINE, machine);
            editor.putString(MODEL, model);
            editor.putString(NAMEPHONE, namePhone);
            editor.putString(CHARGENAME, chargeName);
            editor.putString(TYPE, "维修");
            editor.commit();
        } else if (tag.equals(AddRoutingActivity.types[3])) {
            Log.d("test", "----开始保存调试---->");
            SharedPreferences preferences = getActivity().getSharedPreferences(AddRoutingActivity.ROUTING_TIAOSHI, Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(ADDRESS, address);
            editor.putString(JOBCONTENT, jobContent);
            editor.putString(MACHINE, machine);
            editor.putString(MODEL, model);
            editor.putString(NAMEPHONE, namePhone);
            editor.putString(CHARGENAME, chargeName);
            editor.putString(TYPE, "调试");
            editor.commit();
        }

    }

    public Routing getData() {
        Routing routing = new Routing();
        String address = edit_fragment_add_address.getText().toString();
        String jobContent = edit_fragment_add_jobcontent.getText().toString();
        String machine = edit_fragment_add_machine.getText().toString();
        String model = edit_fragment_add_model.getText().toString();
        String namePhone = edit_fragment_add_name_phone.getText().toString();
        String chargeName = edit_fragment_add_charge_name.getText().toString();
        routing.setAddress(address);
        routing.setJobContent(jobContent);
        routing.setMachine(machine);
        routing.setModel(model);
        routing.setPhone(namePhone);
        routing.setChargeName(chargeName);
        routing.setType(tag);
        return routing;
    }
}