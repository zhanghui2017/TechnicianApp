package com.gengli.technician.fragment;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gengli.technician.R;
import com.gengli.technician.activity.FavourActivity;
import com.gengli.technician.activity.LoginActivity;
import com.gengli.technician.adapter.ExcelOneAdapter;
import com.gengli.technician.bean.Article;
import com.gengli.technician.bean.CarInfo;
import com.gengli.technician.bean.DateBean;
import com.gengli.technician.http.ApiClientHttp;
import com.gengli.technician.http.ServicePort;
import com.gengli.technician.listener.OnPagerChangeListener;
import com.gengli.technician.listener.OnSingleChooseListener;
import com.gengli.technician.util.CalendarUtil;
import com.gengli.technician.util.LogUtils;
import com.gengli.technician.view.CalendarView;
import com.gengli.technician.view.MyListView;
import com.gengli.technician.view.RepairListView;
import com.google.android.gms.plus.PlusOneButton;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A fragment with a Google +1 button.
 */
public class ExcelOneFragment extends Fragment implements View.OnClickListener {

    private CalendarView calendarView;
    private int[] cDate = CalendarUtil.getCurrentDate();
    private String chooseDay;
    private ImageView lastMonth1, nextMonth1;

    private TextView excel_one_text_1, excel_one_text_2, excel_one_text_3, excel_one_text_4, excel_one_text_5, excel_one_more_bt;
    private RepairListView excel_one_list_view;
    private ExcelOneAdapter adapter;
    private List<CarInfo> carInfoList;

    private View tmp_line_6;
    private String address;
    private String product_model;
    private String order_id;
    private String enter;
    private String prepare;
    private LinearLayout excel_one_data_view, excel_one_no_data;

    private FragmentInteraction listener;

    public interface FragmentInteraction {
        void process(String str);
    }

    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInteraction) {
            listener = (FragmentInteraction) context;
        } else {
            throw new IllegalArgumentException("activity must implements FragmentInteraction");
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (activity instanceof FragmentInteraction) {
                listener = (FragmentInteraction) activity;
            } else {
                throw new IllegalArgumentException("activity must implements FragmentInteraction");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_excel_one, container, false);
        initView(view);
        initCalendar(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getOperator();
    }


    private void initView(View view) {
        carInfoList = new ArrayList<>();
        tmp_line_6 = view.findViewById(R.id.tmp_line_6);
        excel_one_data_view = (LinearLayout) view.findViewById(R.id.excel_one_data_view);
        excel_one_no_data = (LinearLayout) view.findViewById(R.id.excel_one_no_data);
        excel_one_text_1 = (TextView) view.findViewById(R.id.excel_one_text_1);
        excel_one_text_2 = (TextView) view.findViewById(R.id.excel_one_text_2);
        excel_one_text_3 = (TextView) view.findViewById(R.id.excel_one_text_3);
        excel_one_text_4 = (TextView) view.findViewById(R.id.excel_one_text_4);
        excel_one_text_5 = (TextView) view.findViewById(R.id.excel_one_text_5);
        excel_one_more_bt = (TextView) view.findViewById(R.id.excel_one_more_bt);
        excel_one_list_view = (RepairListView) view.findViewById(R.id.excel_one_list_view);
        adapter = new ExcelOneAdapter(getActivity(), carInfoList);
        excel_one_list_view.setAdapter(adapter);
        excel_one_more_bt.setOnClickListener(this);
    }

    /**
     * 日历初始化
     */
    public void initCalendar(View view) {
        lastMonth1 = (ImageView) view.findViewById(R.id.lastMonth1);
        nextMonth1 = (ImageView) view.findViewById(R.id.nextMonth1);
        lastMonth1.setOnClickListener(this);
        nextMonth1.setOnClickListener(this);
        final TextView title = (TextView) view.findViewById(R.id.excel_one_title);
        calendarView = (CalendarView) view.findViewById(R.id.excel_one_calendar);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("2017.10.30", "qaz");
        map.put("2017.10.1", "wsx");
        map.put("2017.11.12", "yhn");
        map.put("2017.9.15", "edc");
        map.put("2017.11.6", "rfv");
        map.put("2017.11.11", "tgb");
        calendarView.setStartEndDate("2016.1", "2028.12").setDisableStartEndDate("2016.10.10", "2028.10.10")
                .setInitDate(cDate[0] + "." + cDate[1]).setSingleDate(cDate[0] + "." + cDate[1] + "." + cDate[2])
                .init();

        title.setText(cDate[0] + "." + cDate[1]);
        chooseDay = cDate[0] + "-" + cDate[1] + "-" + cDate[2];
        listener.process(chooseDay);
        LogUtils.showLogD("当前选中的日期：" + chooseDay);
        calendarView.setOnPagerChangeListener(new OnPagerChangeListener() {
            @Override
            public void onPagerChanged(int[] date) {
                title.setText(date[0] + "." + date[1]);
            }
        });

        calendarView.setOnSingleChooseListener(new OnSingleChooseListener() {
            @Override
            public void onSingleChoose(View view, DateBean date) {
                title.setText(date.getSolar()[0] + "." + date.getSolar()[1]);
                if (date.getType() == 1) {
                    title.setText(date.getSolar()[0] + "." + date.getSolar()[1]);
                    chooseDay = date.getSolar()[0] + "-" + date.getSolar()[1] + "-" + date.getSolar()[2];
                    listener.process(chooseDay);
                    getOperator();
                    LogUtils.showLogD("当前选中的日期：" + chooseDay);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lastMonth1:
                calendarView.lastMonth();
                break;
            case R.id.nextMonth1:
                calendarView.nextMonth();
                break;
            case R.id.excel_one_more_bt:
                tmp_line_6.setVisibility(View.VISIBLE);
                excel_one_more_bt.setVisibility(View.INVISIBLE);
                excel_one_list_view.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void getOperator() {
        if (carInfoList != null)
            carInfoList.clear();
        else
            carInfoList = new ArrayList<>();

        String url = ServicePort.OPERATOR_LISTS;
        ApiClientHttp api = new ApiClientHttp();
        Map<String, String> map = new HashMap<>();
        map.put("day", chooseDay);
        AjaxParams params = api.getParam(getActivity(), map);
        params.put("day", chooseDay);

        api.Post(url, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                String responce = o.toString();
                LogUtils.showLogD("--->获取记录表返回数据：" + responce);
                if (!TextUtils.isEmpty(responce)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responce);
                        int err_no = jsonObject.getInt("err_no");
                        if (err_no == 0) {
                            handle.sendEmptyMessage(0);
                            JSONObject results = jsonObject.getJSONObject("results");

                            address = results.getString("address");
                            product_model = results.getString("product_model");
                            order_id = results.getString("order_id");
                            enter = results.getString("enter");
                            prepare = results.getString("prepare");
                            JSONArray lists = results.getJSONArray("lists");
                            if (lists != null && lists.length() > 0) {
                                for (int i = 0; i < lists.length(); i++) {
                                    JSONObject item = lists.getJSONObject(i);
                                    CarInfo carInfo = new CarInfo();
                                    carInfo.setStartTime(item.getString("start"));
                                    carInfo.setEndTime(item.getString("end"));
                                    carInfo.setWeight(item.getString("pjfl"));
                                    carInfo.setDesc(item.getString("yxqk"));
                                    carInfo.setOther(item.getString("qtys"));
                                    carInfoList.add(carInfo);
                                }
                                adapter.notifyDataSetChanged();
                            } else {
                                handle.sendEmptyMessage(1);
                            }
                        } else if (err_no == 2100) {
                            handle.sendEmptyMessage(2);
                        } else if (err_no == 3000) {
                            handle.sendEmptyMessage(1);
                        } else {
                            LogUtils.showCenterToast(getActivity(), jsonObject.getInt("err_no") + "" + jsonObject.getString("err_msg"));
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
                excel_one_data_view.setVisibility(View.VISIBLE);
                excel_one_no_data.setVisibility(View.INVISIBLE);
                excel_one_text_1.setText(address);
                excel_one_text_2.setText(product_model);
                excel_one_text_3.setText(order_id);
                excel_one_text_4.setText(enter);
                excel_one_text_5.setText(prepare);
            } else if (msg.what == 1) {
                excel_one_data_view.setVisibility(View.INVISIBLE);
                excel_one_no_data.setVisibility(View.VISIBLE);
//                LogUtils.showCenterToast(getActivity(), "无车辆信息");
            } else if (msg.what == 2) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        }
    };
}
