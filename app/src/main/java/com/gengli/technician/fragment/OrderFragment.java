package com.gengli.technician.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gengli.technician.R;
import com.gengli.technician.activity.AddRoutingActivity;
import com.gengli.technician.activity.CommitActivity;
import com.gengli.technician.activity.LoginActivity;
import com.gengli.technician.activity.OrderBeginActivity;
import com.gengli.technician.activity.OrderIngActivity;
import com.gengli.technician.activity.OrderOKActivity;
import com.gengli.technician.activity.RecordActivity;
import com.gengli.technician.adapter.OrderAdapter;
import com.gengli.technician.bean.Article;
import com.gengli.technician.bean.Order;
import com.gengli.technician.http.ApiClientHttp;
import com.gengli.technician.http.ServicePort;
import com.gengli.technician.util.LogUtils;
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
import java.util.Random;

public class OrderFragment extends Fragment implements View.OnClickListener {

    private RelativeLayout order_ing_view;
    private RelativeLayout order_ok_view;
    private RelativeLayout order_all_view;
    private PullToRefreshListView order_list_view;
    private OrderAdapter orderAdapter;
    private List<Order> allList;
    private List<Order> chooseList;
    private List<Order> orderList;
    private PopupWindow popupWindow;
    private View img_order_tab_ing;
    private View img_order_tab_ok;
    private View img_order_tab_all;
    private ImageView pop_order_begin_sel;
    private ImageView pop_order_ing_sel;
    //    private ImageView pop_order_after_sel;
    private int clickType = 1;
    private TextView begin_count_text;
    //    private TextView after_count_text;
    private TextView ok_count_text;
    private TextView all_count_text;
    private TextView ing_count_text;

    private int beginCount = 0;
    private int ingCount = 0;
    //    private int afterCount = 0;
//    private int okCount = 0;
//    private int allCount = 0;

    private TextView order_ing_text;
    private ImageView order_ing_img;

    private TextView order_ok_text;
    private TextView order_ok_count_text;

    private TextView order_all_text;
    private TextView order_all_count_text;
    private LinearLayout order_no_data;

    private int beginNum = 0;
    private int ingNum = 0;
    private int okNum = 0;
    private int allNum = 0;

    private int currentTab = 2;
    private int page = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        allList = new ArrayList<>();

//        getCount();
//        initData();
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getOrderData(1);
    }

    public void initView(View view) {
        order_no_data = (LinearLayout) view.findViewById(R.id.order_no_data);
        order_ing_view = (RelativeLayout) view.findViewById(R.id.order_ing_view);
        order_ok_view = (RelativeLayout) view.findViewById(R.id.order_ok_view);
        order_all_view = (RelativeLayout) view.findViewById(R.id.order_all_view);
        img_order_tab_ing = view.findViewById(R.id.img_order_tab_ing);
        img_order_tab_ok = view.findViewById(R.id.img_order_tab_ok);
        img_order_tab_all = view.findViewById(R.id.img_order_tab_all);
        order_list_view = (PullToRefreshListView) view.findViewById(R.id.order_list_view);
        ok_count_text = (TextView) view.findViewById(R.id.order_ok_count_text);
        all_count_text = (TextView) view.findViewById(R.id.order_all_count_text);

//        ok_count_text.setText("(" + okCount + ")");
//        all_count_text.setText("(" + allList.size() + ")");

        order_ing_text = (TextView) view.findViewById(R.id.order_ing_text);
        order_ing_img = (ImageView) view.findViewById(R.id.order_ing_img);
        order_ok_text = (TextView) view.findViewById(R.id.order_ok_text);
        order_ok_count_text = (TextView) view.findViewById(R.id.order_ok_count_text);
        order_all_text = (TextView) view.findViewById(R.id.order_all_text);
        order_all_count_text = (TextView) view.findViewById(R.id.order_all_count_text);
        order_ing_text.setTextColor(ContextCompat.getColor(getActivity(), R.color.color_order_bg_4373f1));
        order_ing_img.setImageResource(R.drawable.triangle_blue);
        order_ok_text.setTextColor(setColor(R.color.color_order_text_242424));
        order_ok_count_text.setTextColor(setColor(R.color.color_order_text_242424));
        order_all_text.setTextColor(setColor(R.color.color_order_text_242424));
        order_all_count_text.setTextColor(setColor(R.color.color_order_text_242424));


        order_ing_view.setOnClickListener(this);
        order_ok_view.setOnClickListener(this);
        order_all_view.setOnClickListener(this);

        orderList = new ArrayList<>();
        chooseList = new ArrayList<>();
//        getChooseList(1);
        orderAdapter = new OrderAdapter(getActivity(), orderList);
        order_list_view.setAdapter(orderAdapter);
        order_list_view.setMode(PullToRefreshBase.Mode.BOTH);
//        order_list_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
//            @Override
//            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//                String label = DateUtils.formatDateTime(
//                        getActivity(),
//                        System.currentTimeMillis(),
//                        DateUtils.FORMAT_SHOW_TIME
//                                | DateUtils.FORMAT_SHOW_DATE
//                                | DateUtils.FORMAT_ABBREV_ALL);
//                // 显示最后更新的时间
//                refreshView.getLoadingLayoutProxy()
//                        .setLastUpdatedLabel(label);
//                new FinishRefresh().execute();
//            }
//        });
        order_list_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getOrderData(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                LogUtils.showLogD("cur page --------->" + page++);
                getOrderData(page++);
            }
        });

        order_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order order = orderList.get(position - 1);
                if (order.getType() == 2) {
                    Intent intent = new Intent(getActivity(), OrderBeginActivity.class);
                    intent.putExtra("BEGIN_ORDER", order);
                    startActivity(intent);
                } else if (order.getType() == 3) {
                    Intent intent = new Intent(getActivity(), OrderIngActivity.class);
                    intent.putExtra("ING_ORDER", order);
                    startActivity(intent);
//                } else if (order.getType() == 3) {
//                    Intent intent = new Intent(getActivity(), CommitActivity.class);
//                    intent.putExtra("AFTER_ORDER", order);
//                    startActivity(intent);
                } else if (order.getType() == 4) {
                    Intent intent = new Intent(getActivity(), OrderOKActivity.class);
                    intent.putExtra("OK_ORDER", order);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_ing_view:
                img_order_tab_ing.setVisibility(View.VISIBLE);
                img_order_tab_ok.setVisibility(View.INVISIBLE);
                img_order_tab_all.setVisibility(View.INVISIBLE);

                order_ing_text.setTextColor(setColor(R.color.color_order_bg_4373f1));
                order_ing_img.setImageResource(R.drawable.triangle_blue);
                order_ok_text.setTextColor(setColor(R.color.color_order_text_242424));
                order_ok_count_text.setTextColor(setColor(R.color.color_order_text_242424));
                order_all_text.setTextColor(setColor(R.color.color_order_text_242424));
                order_all_count_text.setTextColor(setColor(R.color.color_order_text_242424));
                showPopupWindow();
                popupWindow.showAsDropDown(img_order_tab_ing);
                break;
            case R.id.order_ok_view:
                currentTab = 4;
                img_order_tab_ing.setVisibility(View.INVISIBLE);
                img_order_tab_ok.setVisibility(View.VISIBLE);
                img_order_tab_all.setVisibility(View.INVISIBLE);

                order_ing_text.setTextColor(setColor(R.color.color_order_text_242424));
                order_ing_img.setImageResource(R.drawable.triangle);
                order_ok_text.setTextColor(setColor(R.color.color_order_bg_4373f1));
                order_ok_count_text.setTextColor(setColor(R.color.color_order_bg_4373f1));
                order_all_text.setTextColor(setColor(R.color.color_order_text_242424));
                order_all_count_text.setTextColor(setColor(R.color.color_order_text_242424));
                getChooseList(4);
                orderAdapter.notifyDataSetChanged();
                break;
            case R.id.order_all_view:
                currentTab = 0;
                img_order_tab_ing.setVisibility(View.INVISIBLE);
                img_order_tab_ok.setVisibility(View.INVISIBLE);
                img_order_tab_all.setVisibility(View.VISIBLE);

                order_ing_text.setTextColor(setColor(R.color.color_order_text_242424));
                order_ing_img.setImageResource(R.drawable.triangle);
                order_ok_text.setTextColor(setColor(R.color.color_order_text_242424));
                order_ok_count_text.setTextColor(setColor(R.color.color_order_text_242424));
                order_all_text.setTextColor(setColor(R.color.color_order_bg_4373f1));
                order_all_count_text.setTextColor(setColor(R.color.color_order_bg_4373f1));
                getChooseList(0);
                orderAdapter.notifyDataSetChanged();
                break;
            case R.id.pop_begin_bt:
                currentTab = 2;
                order_ing_text.setText("待接单");
                getChooseList(2);
                orderAdapter.notifyDataSetChanged();
                clickType = 1;
                popupWindow.dismiss();
                break;
            case R.id.pop_ing_bt:
                currentTab = 3;
                order_ing_text.setText("工作中");
                getChooseList(3);
                orderAdapter.notifyDataSetChanged();
                clickType = 2;
                popupWindow.dismiss();
                break;

            default:
                break;
        }

    }

    public void showPopupWindow() {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_order_choose, null);
        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setContentView(contentView);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        //设置各个控件的点击响应
        RelativeLayout beginBt = (RelativeLayout) contentView.findViewById(R.id.pop_begin_bt);
        RelativeLayout ingBt = (RelativeLayout) contentView.findViewById(R.id.pop_ing_bt);
//        RelativeLayout afterBt = (RelativeLayout) contentView.findViewById(R.id.pop_after_bt);
        pop_order_begin_sel = (ImageView) contentView.findViewById(R.id.pop_order_begin_sel);
        pop_order_ing_sel = (ImageView) contentView.findViewById(R.id.pop_order_ing_sel);
//        pop_order_after_sel = (ImageView) contentView.findViewById(R.id.pop_order_after_sel);
        begin_count_text = (TextView) contentView.findViewById(R.id.pop_order_begin_count_text);
        ing_count_text = (TextView) contentView.findViewById(R.id.pop_order_ing_count_text);
        begin_count_text.setText("(" + beginNum + ")");
        ing_count_text.setText("(" + ingNum + ")");
        beginBt.setOnClickListener(this);
        ingBt.setOnClickListener(this);
//        afterBt.setOnClickListener(this);
        if (clickType == 1) {
            pop_order_begin_sel.setVisibility(View.VISIBLE);
            pop_order_ing_sel.setVisibility(View.INVISIBLE);
//            pop_order_after_sel.setVisibility(View.INVISIBLE);
        } else if (clickType == 2) {
            pop_order_begin_sel.setVisibility(View.INVISIBLE);
            pop_order_ing_sel.setVisibility(View.VISIBLE);
//            pop_order_after_sel.setVisibility(View.INVISIBLE);
//        } else if (clickType == 3) {
//            pop_order_begin_sel.setVisibility(View.INVISIBLE);
//            pop_order_ing_sel.setVisibility(View.INVISIBLE);
//            pop_order_after_sel.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 获取选择框中选择的列表
     *
     * @param type
     */
    public void getChooseList(int type) {
        chooseList.clear();
        if (type == 0) {
            if (allList.size() <= 0) {
                order_no_data.setVisibility(View.VISIBLE);
                order_list_view.setVisibility(View.INVISIBLE);
            } else {
                order_no_data.setVisibility(View.INVISIBLE);
                order_list_view.setVisibility(View.VISIBLE);
                if (orderList.size() > 0) {
                    orderList.clear();
                    orderList.addAll(allList);
                } else if (orderList.size() == 0) {
                    orderList.addAll(allList);
                } else {
                    Log.d("test", "-----------------这什么情况-----------------");
                }
            }

        } else {
            if (allList != null) {
                for (int i = 0; i < allList.size(); i++) {
                    Order order = allList.get(i);
                    if (type == order.getType()) {
                        chooseList.add(order);
                    }
                }
                if (chooseList.size() > 0) {
                    order_no_data.setVisibility(View.INVISIBLE);
                    order_list_view.setVisibility(View.VISIBLE);
                    if (orderList.size() > 0) {
                        orderList.clear();
                        orderList.addAll(chooseList);
                    } else if (orderList.size() == 0) {
                        orderList.addAll(chooseList);
                    } else {
                        Log.d("test", "-----------------这特么又是什么情况-----------------");
                    }
                } else {
                    order_no_data.setVisibility(View.VISIBLE);
                    order_list_view.setVisibility(View.INVISIBLE);
                }
            } else {
                Toast.makeText(getActivity(), "暂没有新数据", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
//                begin_count_text.setText("(" + beginNum + ")");
//                ing_count_text.setText("(" + ingNum + ")");
                ok_count_text.setText("(" + okNum + ")");
                allNum = beginNum + ingNum + okNum;
                all_count_text.setText("(" + allNum + ")");
            } else if (msg.what == 1) {
                order_no_data.setVisibility(View.VISIBLE);
                order_list_view.setVisibility(View.INVISIBLE);
            } else if (msg.what == 2) {
                LogUtils.showLogD("当前显示页面 == " + currentTab);
                getChooseList(currentTab);
                orderAdapter.notifyDataSetChanged();
            } else if (msg.what == 3) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        }
    };

    private void getOrderData(final int page) {
        final List<Order> listTemp = new ArrayList<>();
        String url = ServicePort.REPAIR_LISTS;
        ApiClientHttp api = new ApiClientHttp();
        Map<String, String> map = new HashMap<>();
        map.put("status", 0 + "");
        map.put("page", page + "");
        AjaxParams params = api.getParam(getActivity(), map);
        params.put("status", 0 + "");
        params.put("page", page + "");
        api.Post(url, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                String responce = o.toString();
//                LogUtils.showLogI("-----订单列表返回数据----->" + responce);
                if (!TextUtils.isEmpty(responce)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responce);
                        int err_no = jsonObject.getInt("err_no");
                        if (err_no == 0) {
                            JSONObject results = jsonObject.getJSONObject("results");
                            if (!TextUtils.isEmpty(results.toString())) {
                                JSONArray status = results.getJSONArray("status");
                                LogUtils.showLogD("..............." + status.toString());
                                if (status.length() > 0) {
                                    for (int i = 0; i < status.length(); i++) {
                                        JSONObject item = status.getJSONObject(i);
                                        if (item.getString("name").equals("工作中")) {
                                            ingNum = item.getInt("num");
                                            LogUtils.showLogD("-1------" + ingNum);
                                        }
                                        if (item.getString("name").equals("已完成")) {
                                            okNum = item.getInt("num");
                                            LogUtils.showLogD("-2------" + okNum);
                                        }
                                        if (item.getString("name").equals("待接单")) {
                                            beginNum = item.getInt("num");
                                            LogUtils.showLogD("-3------" + beginNum);
                                        }
                                    }
                                    handle.sendEmptyMessage(0);
                                } else {
                                    LogUtils.showCenterToast(getActivity(), "订单数量获取失败");
                                }
                                JSONArray lists = results.getJSONArray("lists");
                                LogUtils.showLogI("-----订单列表返回数据----->" + lists.toString());
                                if (lists.length() > 0) {

                                    for (int i = 0; i < lists.length(); i++) {
                                        JSONObject item = lists.getJSONObject(i);
                                        Order order = new Order();
                                        int type = item.getInt("status");
                                        if (type != 1) {
                                            order.setType(type);
                                            order.setId(item.getString("rid"));
                                            order.setModel(item.getString("product_id"));
                                            order.setMachine(item.getString("product_model"));
                                            order.setName(item.getString("realname"));
                                            order.setLevel(item.getString("category_name"));
                                            order.setPhone(item.getString("tel"));
                                            order.setCompany(item.getString("unit"));
                                            order.setAddress(item.getString("address"));
                                            order.setChargeName(item.getString("handle_name"));
                                            order.setChargePhone(item.getString("handle_phone"));
                                            order.setTime(item.getString("create_time"));
                                            order.setLevel(item.getString("category_name"));
                                            order.setTrouble(item.getString("des"));
                                            order.setExpressAddress(item.getString("express"));
                                            listTemp.add(order);
                                        }
                                    }
                                    if (page == 1) {
                                        allList.clear();
                                        if (listTemp.size() == 0) {
                                            LogUtils.showCenterToast(getActivity(), "没有数据");
                                        } else if (listTemp.size() > 0) {
                                            allList.addAll(listTemp);
                                            orderAdapter.notifyDataSetChanged();
                                        }

                                    } else if (page > 1) {
                                        if (listTemp.size() == 0) {
                                            LogUtils.showCenterToast(getActivity(), "没有更多订单");
                                        } else if (listTemp.size() > 0) {
                                            allList.addAll(listTemp);
                                            orderAdapter.notifyDataSetChanged();
                                        }
                                    }
                                    handle.sendEmptyMessage(2);
                                } else {
                                    if (page > 1) {
                                        LogUtils.showCenterToast(getActivity(), "没有更多订单");
                                    } else {
                                        handle.sendEmptyMessage(1);
                                        LogUtils.showCenterToast(getActivity(), "没有订单");
                                    }
                                }
                            }
                            order_list_view.onRefreshComplete();
                        } else if (err_no == 2100) {
                            handle.sendEmptyMessage(3);
                        }

                    } catch (JSONException e) {

                    }
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {

            }
        });

    }


    private int setColor(int color) {
        return ContextCompat.getColor(getActivity(), color);
    }
}
