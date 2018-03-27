package com.gengli.technician.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gengli.technician.R;
import com.gengli.technician.activity.AddRoutingActivity;
import com.gengli.technician.activity.AssessActivity;
import com.gengli.technician.activity.CarCountActivity;
import com.gengli.technician.bean.Assess;
import com.gengli.technician.http.ApiClientHttp;
import com.gengli.technician.http.ServicePort;
import com.gengli.technician.util.LogUtils;
import com.gengli.technician.util.SharePreferencesUtil;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StepThreeFragment extends Fragment {

    private RadioGroup radio_group_1, radio_group_2, radio_group_3, radio_group_4, radio_group_5, radio_group_6;
    private RadioButton radio_button_1_y, radio_button_1_n;
    private RadioButton radio_button_2_y, radio_button_2_n;
    private RadioButton radio_button_3_y, radio_button_3_n;
    private RadioButton radio_button_4_y, radio_button_4_n;
    private RadioButton radio_button_5_y, radio_button_5_n;
    private RadioButton radio_button_6_y, radio_button_6_n;

    private TextView step_three_commit_bt;
    private EditText step_three_beizhu_edit;

    private String radioText1 = "满意";
    private String radioText2 = "满意";
    private String radioText3 = "满意";
    private String radioText4 = "满意";
    private String radioText5 = "满意";
    private String radioText6 = "满意";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_three, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        step_three_beizhu_edit = (EditText) view.findViewById(R.id.step_three_beizhu_edit);
        step_three_commit_bt = (TextView) view.findViewById(R.id.step_three_commit_bt);

        radio_group_1 = (RadioGroup) view.findViewById(R.id.radio_group_1);
        radio_button_1_y = (RadioButton) view.findViewById(R.id.radio_button_1_y);
        radio_button_1_n = (RadioButton) view.findViewById(R.id.radio_button_1_n);

        radio_group_2 = (RadioGroup) view.findViewById(R.id.radio_group_2);
        radio_button_2_y = (RadioButton) view.findViewById(R.id.radio_button_2_y);
        radio_button_2_n = (RadioButton) view.findViewById(R.id.radio_button_2_n);

        radio_group_3 = (RadioGroup) view.findViewById(R.id.radio_group_3);
        radio_button_3_y = (RadioButton) view.findViewById(R.id.radio_button_3_y);
        radio_button_3_n = (RadioButton) view.findViewById(R.id.radio_button_3_n);

        radio_group_4 = (RadioGroup) view.findViewById(R.id.radio_group_4);
        radio_button_4_y = (RadioButton) view.findViewById(R.id.radio_button_4_y);
        radio_button_4_n = (RadioButton) view.findViewById(R.id.radio_button_4_n);

        radio_group_5 = (RadioGroup) view.findViewById(R.id.radio_group_5);
        radio_button_5_y = (RadioButton) view.findViewById(R.id.radio_button_5_y);
        radio_button_5_n = (RadioButton) view.findViewById(R.id.radio_button_5_n);

        radio_group_6 = (RadioGroup) view.findViewById(R.id.radio_group_6);
        radio_button_6_y = (RadioButton) view.findViewById(R.id.radio_button_6_y);
        radio_button_6_n = (RadioButton) view.findViewById(R.id.radio_button_6_n);

        radio_group_1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (radio_button_1_y.getId() == checkedId) {
                    radioText1 = radio_button_1_y.getText().toString();
                }
                if (radio_button_1_n.getId() == checkedId) {
                    radioText1 = radio_button_1_n.getText().toString();
                }
            }
        });

        radio_group_2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (radio_button_2_y.getId() == checkedId) {
                    radioText2 = radio_button_2_y.getText().toString();
                }
                if (radio_button_2_n.getId() == checkedId) {
                    radioText2 = radio_button_2_n.getText().toString();
                }
            }
        });

        radio_group_3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (radio_button_3_y.getId() == checkedId) {
                    radioText3 = radio_button_3_y.getText().toString();
                }
                if (radio_button_3_n.getId() == checkedId) {
                    radioText3 = radio_button_3_n.getText().toString();
                }
            }
        });

        radio_group_4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (radio_button_4_y.getId() == checkedId) {
                    radioText4 = radio_button_4_y.getText().toString();
                }
                if (radio_button_4_n.getId() == checkedId) {
                    radioText4 = radio_button_4_n.getText().toString();
                }
            }
        });

        radio_group_5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (radio_button_5_y.getId() == checkedId) {
                    radioText5 = radio_button_5_y.getText().toString();
                }
                if (radio_button_5_n.getId() == checkedId) {
                    radioText5 = radio_button_5_n.getText().toString();
                }
            }
        });

        radio_group_6.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (radio_button_6_y.getId() == checkedId) {
                    radioText6 = radio_button_6_y.getText().toString();
                }
                if (radio_button_6_n.getId() == checkedId) {
                    radioText6 = radio_button_6_n.getText().toString();
                }
            }
        });


        step_three_commit_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitEvaluate();
            }
        });
    }


    private void commitEvaluate() {
        String day = ((AssessActivity) getActivity()).getDay();
        String beizhu = step_three_beizhu_edit.getText().toString();

        SharedPreferences preferences = getActivity().getSharedPreferences("edit_assess", Activity.MODE_PRIVATE);
        String name = preferences.getString("name", "");
        String address = preferences.getString("address", "");
        String sale_name = preferences.getString("sale_name", "");
        String sale_phone = preferences.getString("sale_phone", "");
        String lead_name = preferences.getString("lead_name", "");
        String lead_phone = preferences.getString("lead_phone", "");
        String product_model = preferences.getString("product_model", "");
        String order_id = preferences.getString("order_id", "");
        String time_arrive = preferences.getString("time_arrive", "");
        String time_service = preferences.getString("time_service", "");
        String data_kcgy = preferences.getString("data_kcgy", "");
        String data_ycjb = preferences.getString("data_ycjb", "");
        String data_gddy = preferences.getString("data_gddy", "");
        String data_gfkyj = preferences.getString("data_gfkyj", "");
        String data_jindong = preferences.getString("data_jindong", "");
        String data_shuini = preferences.getString("data_shuini", "");
        String data_sha = preferences.getString("data_sha", "");
        String data_leibie = preferences.getString("data_leibie", "");
        String data_shizi = preferences.getString("data_shizi", "");
        String data_shui = preferences.getString("data_shui", "");
        String data_suningji = preferences.getString("data_suningji", "");
        String data_jianshuini = preferences.getString("data_jianshuini", "");

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address)
                || TextUtils.isEmpty(sale_name) || TextUtils.isEmpty(sale_phone)
                || TextUtils.isEmpty(lead_name) || TextUtils.isEmpty(lead_phone)
                || TextUtils.isEmpty(product_model) || TextUtils.isEmpty(order_id)
                || TextUtils.isEmpty(time_arrive) || TextUtils.isEmpty(time_service)
                || TextUtils.isEmpty(data_kcgy) || TextUtils.isEmpty(data_ycjb)
                || TextUtils.isEmpty(data_gddy) || TextUtils.isEmpty(data_gfkyj)
                || TextUtils.isEmpty(data_jindong) || TextUtils.isEmpty(data_shuini)
                || TextUtils.isEmpty(data_sha) || TextUtils.isEmpty(data_leibie)
                || TextUtils.isEmpty(data_shizi) || TextUtils.isEmpty(data_shui)
                || TextUtils.isEmpty(data_suningji) || TextUtils.isEmpty(data_jianshuini)
                || TextUtils.isEmpty(beizhu)) {
            LogUtils.showCenterToast(getActivity(), "请输入完整信息");
            return;
        }

        String url = ServicePort.EVALUATE_ADD;
        ApiClientHttp api = new ApiClientHttp();
        Map<String, String> map = new HashMap<>();
        map.put("day", day);
        map.put("name", name);
        map.put("address", address);
        map.put("sale_name", sale_name);
        map.put("sale_phone", sale_phone);
        map.put("lead_name", lead_name);
        map.put("lead_phone", lead_phone);
        map.put("product_model", product_model);
        map.put("order_id", order_id);
        map.put("time_arrive", time_arrive);
        map.put("time_service", time_service);
        map.put("data_kcgy", data_kcgy);
        map.put("data_ycjb", data_ycjb);
        map.put("data_gddy", data_gddy);
        map.put("data_gfkyj", data_gfkyj);
        map.put("data_jindong", data_jindong);
        map.put("data_shuini", data_shuini);
        map.put("data_sha", data_sha);
        map.put("data_leibie", data_leibie);
        map.put("data_shizi", data_shizi);
        map.put("data_shui", data_shui);
        map.put("data_suningji", data_suningji);
        map.put("data_jianshuini", data_jianshuini);
        map.put("kaohe_gztd", radioText1);
        map.put("kaohe_szgt", radioText2);
        map.put("kaohe_jssp", radioText3);
        map.put("kaohe_pjsp", radioText4);
        map.put("kaohe_yssb", radioText5);
        map.put("kaohe_rcwh", radioText6);
        map.put("note", beizhu);

        AjaxParams params = api.getParam(getActivity(), map);
        params.put("day", day);
        params.put("name", name);
        params.put("address", address);
        params.put("sale_name", sale_name);
        params.put("sale_phone", sale_phone);
        params.put("lead_name", lead_name);
        params.put("lead_phone", lead_phone);
        params.put("product_model", product_model);
        params.put("order_id", order_id);
        params.put("time_arrive", time_arrive);
        params.put("time_service", time_service);
        params.put("data_kcgy", data_kcgy);
        params.put("data_ycjb", data_ycjb);
        params.put("data_gddy", data_gddy);
        params.put("data_gfkyj", data_gfkyj);
        params.put("data_jindong", data_jindong);
        params.put("data_shuini", data_shuini);
        params.put("data_sha", data_sha);
        params.put("data_leibie", data_leibie);
        params.put("data_shizi", data_shizi);
        params.put("data_shui", data_shui);
        params.put("data_suningji", data_suningji);
        params.put("data_jianshuini", data_jianshuini);
        params.put("kaohe_gztd", radioText1);
        params.put("kaohe_szgt", radioText2);
        params.put("kaohe_jssp", radioText3);
        params.put("kaohe_pjsp", radioText4);
        params.put("kaohe_yssb", radioText5);
        params.put("kaohe_rcwh", radioText6);
        params.put("note", beizhu);

        api.Post(url, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                String responce = o.toString();
                LogUtils.showLogD("--->添加评定表返回数据：" + responce);
                if (!TextUtils.isEmpty(responce)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responce);
                        int err_no = jsonObject.getInt("err_no");
                        if (err_no == 0) {
                            handle.sendEmptyMessage(0);
                        } else {
                            handle.sendEmptyMessage(1);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                getActivity().finish();
                removeCache();
            } else if (msg.what == 1) {
                LogUtils.showCenterToast(getActivity(), "上传失败，请稍后再试");
            }
        }
    };


    private void removeCache() {
        SharePreferencesUtil.getInstance(getActivity(), "edit_assess").removeItem("name");
        SharePreferencesUtil.getInstance(getActivity(), "edit_assess").removeItem("address");
        SharePreferencesUtil.getInstance(getActivity(), "edit_assess").removeItem("sale_name");
        SharePreferencesUtil.getInstance(getActivity(), "edit_assess").removeItem("sale_phone");
        SharePreferencesUtil.getInstance(getActivity(), "edit_assess").removeItem("lead_name");
        SharePreferencesUtil.getInstance(getActivity(), "edit_assess").removeItem("lead_phone");
        SharePreferencesUtil.getInstance(getActivity(), "edit_assess").removeItem("product_model");
        SharePreferencesUtil.getInstance(getActivity(), "edit_assess").removeItem("order_id");
        SharePreferencesUtil.getInstance(getActivity(), "edit_assess").removeItem("time_arrive");
        SharePreferencesUtil.getInstance(getActivity(), "edit_assess").removeItem("time_service");
        SharePreferencesUtil.getInstance(getActivity(), "edit_assess").removeItem("data_kcgy");
        SharePreferencesUtil.getInstance(getActivity(), "edit_assess").removeItem("data_ycjb");
        SharePreferencesUtil.getInstance(getActivity(), "edit_assess").removeItem("data_gddy");
        SharePreferencesUtil.getInstance(getActivity(), "edit_assess").removeItem("data_gfkyj");
        SharePreferencesUtil.getInstance(getActivity(), "edit_assess").removeItem("data_jindong");
        SharePreferencesUtil.getInstance(getActivity(), "edit_assess").removeItem("data_shuini");
        SharePreferencesUtil.getInstance(getActivity(), "edit_assess").removeItem("data_sha");
        SharePreferencesUtil.getInstance(getActivity(), "edit_assess").removeItem("data_leibie");
        SharePreferencesUtil.getInstance(getActivity(), "edit_assess").removeItem("data_shizi");
        SharePreferencesUtil.getInstance(getActivity(), "edit_assess").removeItem("data_shui");
        SharePreferencesUtil.getInstance(getActivity(), "edit_assess").removeItem("data_suningji");
        SharePreferencesUtil.getInstance(getActivity(), "edit_assess").removeItem("data_jianshuini");
    }
}
