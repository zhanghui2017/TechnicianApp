package com.gengli.technician.fragment;


import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gengli.technician.R;
import com.gengli.technician.activity.AssessDetailActivity;
import com.gengli.technician.activity.LoginActivity;
import com.gengli.technician.adapter.ExcelTwoAdapter;
import com.gengli.technician.adapter.YearAdapter;
import com.gengli.technician.bean.Assess;
import com.gengli.technician.http.ApiClientHttp;
import com.gengli.technician.http.ServicePort;
import com.gengli.technician.util.LogUtils;
import com.google.android.gms.plus.PlusOneButton;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ExcelTwoFragment extends Fragment implements View.OnClickListener {
    private TextView excel_two_all_text, excel_two_year_text;
    private PullToRefreshListView excel_two_list_view;
    private ExcelTwoAdapter adapter;
    private List<Assess> assessList;

    private ListView popup_year_list_view;
    private TextView pop_year_dimiss_bt, pop_year_choose_bt;
    private PopupWindow yearPop;
    private YearAdapter yearAdapter;
    private List<Integer> yearList;
    private String chooseYear = "";
    private int page = 1;

    private LinearLayout excel_two_no_data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_excel_two, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getEvaluate("", 1);
    }

    private void initView(View view) {
        assessList = new ArrayList<>();
        excel_two_no_data = (LinearLayout) view.findViewById(R.id.excel_two_no_data);
        excel_two_all_text = (TextView) view.findViewById(R.id.excel_two_all_text);
        excel_two_year_text = (TextView) view.findViewById(R.id.excel_two_year_text);
        excel_two_all_text.setOnClickListener(this);
        excel_two_year_text.setOnClickListener(this);
        excel_two_list_view = (PullToRefreshListView) view.findViewById(R.id.excel_two_list_view);
        adapter = new ExcelTwoAdapter(getActivity(), assessList);
        excel_two_list_view.setAdapter(adapter);
        excel_two_list_view.setMode(PullToRefreshBase.Mode.BOTH);
        excel_two_list_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                LogUtils.showLogD(" --------- " + chooseYear);
                getEvaluate(chooseYear, 1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                LogUtils.showLogD(" --------- " + chooseYear);
                getEvaluate(chooseYear, page++);
            }
        });
        excel_two_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), AssessDetailActivity.class).putExtra("tmp_id", assessList.get(position - 1).getId()));
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.excel_two_all_text:
                chooseYear = "";
                getEvaluate("", 1);
                break;
            case R.id.excel_two_year_text:
                getYearPopup();
                yearPop.showAtLocation(v, Gravity.CENTER, 0, 0);
                break;
        }
    }


    private void getYearPopup() {
        if (yearPop != null) {
            yearPop.dismiss();
            return;
        } else {
            initYearPop();
        }
    }


    private void initYearPop() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View popupView = layoutInflater.inflate(R.layout.popup_year, null);

        popup_year_list_view = (ListView) popupView.findViewById(R.id.popup_year_list_view);
        pop_year_dimiss_bt = (TextView) popupView.findViewById(R.id.pop_year_dimiss_bt);
        pop_year_choose_bt = (TextView) popupView.findViewById(R.id.pop_year_choose_bt);
        pop_year_dimiss_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yearPop.dismiss();
            }
        });
        pop_year_choose_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                excel_two_year_text.setText(chooseYear + "");
                LogUtils.showLogD(" --------- " + chooseYear);
                getEvaluate(chooseYear + "", 1);
                yearPop.dismiss();
            }
        });

        yearPop = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        yearPop.setFocusable(true);
        yearPop.setTouchable(true);
        yearPop.setBackgroundDrawable(new BitmapDrawable());
        yearList = new ArrayList<>();
        getYear();
        yearAdapter = new YearAdapter(getActivity(), yearList);
        popup_year_list_view.setAdapter(yearAdapter);
        yearAdapter.notifyDataSetChanged();
        popup_year_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                yearAdapter.setSelectedPosition(position);
                yearAdapter.notifyDataSetChanged();
                chooseYear = yearList.get(position) + "";
            }
        });
    }


    private void getYear() {
        for (int i = 2015; i < 2030; i++) {
            yearList.add(i);
        }
    }


    private void getEvaluate(String year, final int page) {
        final List<Assess> listTemp = new ArrayList<>();
        String url = ServicePort.EVALUATE_LISTS;
        ApiClientHttp api = new ApiClientHttp();

        Map<String, String> map = new HashMap<>();
        map.put("year", year + "");
        map.put("page", page + "");
        map.put("size", 10 + "");
        AjaxParams params = api.getParam(getActivity(), map);
        params.put("year", year + "");
        params.put("page", page + "");
        params.put("size", 10 + "");

        api.Post(url, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                String responce = o.toString();
                LogUtils.showLogD("----->获取评定表返回数据----->" + responce);
                if (!TextUtils.isEmpty(responce)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responce);
                        int err_no = jsonObject.getInt("err_no");
                        if (err_no == 0) {
                            JSONObject results = jsonObject.getJSONObject("results");
                            JSONArray lists = results.getJSONArray("lists");
                            if (lists.length() > 0) {
                                for (int i = 0; i < lists.length(); i++) {
                                    JSONObject item = lists.getJSONObject(i);
                                    Assess assess = new Assess();
                                    assess.setId(item.getInt("id"));
                                    assess.setTime(item.getString("day"));
                                    assess.setName(item.getString("name"));
                                    assess.setAddress(item.getString("address"));
                                    assess.setSale_name(item.getString("sale_name"));
                                    assess.setSale_phone(item.getString("sale_phone"));
                                    assess.setLead_name(item.getString("lead_name"));
                                    assess.setLead_phone(item.getString("lead_phone"));
                                    listTemp.add(assess);
                                }
                                if (page == 1) {
                                    assessList.clear();
                                    if (listTemp.size() == 0) {
                                        LogUtils.showCenterToast(getActivity(), "没有数据");
                                    } else if (listTemp.size() > 0) {
                                        assessList.addAll(listTemp);
                                        adapter.notifyDataSetChanged();
                                    }

                                } else if (page > 1) {
                                    if (listTemp.size() == 0) {
                                        LogUtils.showCenterToast(getActivity(), "没有更多数据");
                                    } else if (listTemp.size() > 0) {
                                        assessList.addAll(listTemp);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            } else {
                                if (page > 1) {
                                    LogUtils.showCenterToast(getActivity(), "没有更多数据");
                                } else {
                                    handle.sendEmptyMessage(1);
                                    LogUtils.showCenterToast(getActivity(), "没有数据");
                                }
                            }
                            excel_two_list_view.onRefreshComplete();
                        } else if (err_no == 2100) {
                            handle.sendEmptyMessage(3);
                            LogUtils.showCenterToast(getActivity(), jsonObject.getInt("err_no") + "" + jsonObject.getString("err_msg"));
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

            }
        });
    }

    private Handler handle = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                excel_two_no_data.setVisibility(View.VISIBLE);
                excel_two_list_view.setVisibility(View.INVISIBLE);
            } else if (msg.what == 3) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        }
    };
}
