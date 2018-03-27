package com.gengli.technician.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gengli.technician.R;
import com.gengli.technician.adapter.MyAdapter;
import com.gengli.technician.adapter.StockAdapter;
import com.gengli.technician.adapter.SubAdapter;
import com.gengli.technician.bean.Article;
import com.gengli.technician.bean.City;
import com.gengli.technician.bean.Fitting;
import com.gengli.technician.bean.Province;
import com.gengli.technician.http.ApiClientHttp;
import com.gengli.technician.http.ServicePort;
import com.gengli.technician.util.GetProvinceData;
import com.gengli.technician.util.LogUtils;
import com.gengli.technician.view.MyListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private String[] type = {"液压", "电气", "机械", "常用易损件"};
    private ImageView stock_back_img;
    private EditText edit_stock_fitting;
    private LinearLayout stock_classify_view;
    private LinearLayout stock_area_view;
    private TextView text_stock_classify;
    private TextView text_stock_area;
    private TextView stock_search_bt;
    private PullToRefreshGridView stock_grid_view;
    private StockAdapter stockAdapter;
    private List<Fitting> fittingList;
    private LinearLayout stock_search_view;
    private boolean scrollFlag = false;// 标记是否滑动
    private int lastVisibleItemPosition;// 标记上次滑动位置

    private PopupWindow cityPop;
    private MyListView listView;
    private MyListView subListView;
    private TextView area_dimiss_bt;
    private TextView area_choose_bt;
    private MyAdapter myAdapter;
    private String area_url = "area2.json";
    private List<City> cities;
    private SubAdapter subAdapter;
    private String chooseCity;

    private PopupWindow classifyPop;
    private TextView classify_dimiss_bt;
    private TextView classify_choose_bt;
    private TextView pop_classify_1;
    private TextView pop_classify_2;
    private TextView pop_classify_3;
    private TextView pop_classify_4;
    private LinearLayout stock_no_data;

    private String chooseType;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        fittingList = new ArrayList<>();
        getStock("", "", "", page);
//        for (int i = 0; i < 10; i++) {
//            Fitting f = new Fitting();
//            f.setName("隧道钢筋网焊网机" + i);
//            f.setImgUrl("http://pic3.zhimg.com/b5c5fc8e9141cb785ca3b0a1d037a9a2.jpg");
//            f.setLastCount(100);
//            fittingList.add(f);
//        }
        initView();
    }

    private void initView() {
        stock_no_data = (LinearLayout) findViewById(R.id.stock_no_data);
        stock_search_view = (LinearLayout) findViewById(R.id.stock_search_view);
        stock_back_img = (ImageView) findViewById(R.id.stock_back_img);
        stock_back_img.setOnClickListener(this);
        edit_stock_fitting = (EditText) findViewById(R.id.edit_stock_fitting);
        stock_classify_view = (LinearLayout) findViewById(R.id.stock_classify_view);
        stock_area_view = (LinearLayout) findViewById(R.id.stock_area_view);
        text_stock_classify = (TextView) findViewById(R.id.text_stock_classify);
        text_stock_area = (TextView) findViewById(R.id.text_stock_area);
        stock_search_bt = (TextView) findViewById(R.id.stock_search_bt);
        stock_classify_view.setOnClickListener(this);
        stock_area_view.setOnClickListener(this);
        stock_search_bt.setOnClickListener(this);
        stock_back_img.setOnClickListener(this);
        stock_grid_view = (PullToRefreshGridView) findViewById(R.id.stock_grid_view);
        stockAdapter = new StockAdapter(this, fittingList);
        stock_grid_view.setAdapter(stockAdapter);
        stock_grid_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                getStock("", "", "", page);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                getStock("", "", "", page++);
            }
        });
        stock_grid_view.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_TOUCH_SCROLL:
                        scrollFlag = false;
                        break;
                    case SCROLL_STATE_FLING:
                        scrollFlag = true;
                        break;
                    case SCROLL_STATE_IDLE:
                        scrollFlag = false;
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (scrollFlag) {
                    if (firstVisibleItem > lastVisibleItemPosition) {//上滑
                        stock_search_view.setVisibility(View.GONE);
                    }
                    if (firstVisibleItem < lastVisibleItemPosition) {//下滑
                        stock_search_view.setVisibility(View.GONE);
                    }
                    if (firstVisibleItem == 0) {
                        stock_search_view.setVisibility(View.VISIBLE);

                    }
//                    if (firstVisibleItem == lastVisibleItemPosition) {
//                        return;
//                    }
//                    lastVisibleItemPosition = firstVisibleItem;

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stock_back_img:
                finish();
                break;
            case R.id.stock_classify_view:
                getClassifyPop();
                classifyPop.showAtLocation(v, Gravity.CENTER, 0, 0);
                break;
            case R.id.stock_area_view:
                getCityPopup();
                cityPop.showAtLocation(v, Gravity.CENTER, 0, 0);
                break;
            case R.id.stock_search_bt:
                String keyWord = edit_stock_fitting.getText().toString();
                getStock(keyWord, chooseType, chooseCity, 1);
                break;
            case R.id.area_dimiss_bt:
                cityPop.dismiss();
                break;
            case R.id.area_choose_bt:
                text_stock_area.setText(chooseCity);
                cityPop.dismiss();
                break;
            case R.id.classify_dimiss_bt:
                classifyPop.dismiss();
                break;
            case R.id.classify_choose_bt:
                text_stock_classify.setText(chooseType);
                classifyPop.dismiss();
                break;
            case R.id.pop_classify_1:
                chooseType = type[0];
                pop_classify_1.setBackgroundColor(Color.LTGRAY);
                pop_classify_2.setBackgroundColor(Color.WHITE);
                pop_classify_3.setBackgroundColor(Color.WHITE);
                pop_classify_4.setBackgroundColor(Color.WHITE);
                break;
            case R.id.pop_classify_2:
                chooseType = type[1];
                pop_classify_1.setBackgroundColor(Color.WHITE);
                pop_classify_2.setBackgroundColor(Color.LTGRAY);
                pop_classify_3.setBackgroundColor(Color.WHITE);
                pop_classify_4.setBackgroundColor(Color.WHITE);
                break;
            case R.id.pop_classify_3:
                chooseType = type[2];
                pop_classify_1.setBackgroundColor(Color.WHITE);
                pop_classify_2.setBackgroundColor(Color.WHITE);
                pop_classify_3.setBackgroundColor(Color.LTGRAY);
                pop_classify_4.setBackgroundColor(Color.WHITE);
                break;
            case R.id.pop_classify_4:
                chooseType = type[3];
                pop_classify_1.setBackgroundColor(Color.WHITE);
                pop_classify_2.setBackgroundColor(Color.WHITE);
                pop_classify_3.setBackgroundColor(Color.WHITE);
                pop_classify_4.setBackgroundColor(Color.LTGRAY);
                break;
        }
    }

    private void getClassifyPop() {
        if (classifyPop != null) {
            classifyPop.dismiss();
            return;
        } else {
            initClassifyPop();
        }
    }

    private void getCityPopup() {
        if (cityPop != null) {
            cityPop.dismiss();
            return;
        } else {
            initCityPop();
        }
    }

    /**
     * 分类窗口
     */
    private void initClassifyPop() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View popupView = layoutInflater.inflate(R.layout.popup_classify_choice, null);

        classify_dimiss_bt = (TextView) popupView.findViewById(R.id.classify_dimiss_bt);
        classify_choose_bt = (TextView) popupView.findViewById(R.id.classify_choose_bt);
        pop_classify_1 = (TextView) popupView.findViewById(R.id.pop_classify_1);
        pop_classify_2 = (TextView) popupView.findViewById(R.id.pop_classify_2);
        pop_classify_3 = (TextView) popupView.findViewById(R.id.pop_classify_3);
        pop_classify_4 = (TextView) popupView.findViewById(R.id.pop_classify_4);

        classify_dimiss_bt.setOnClickListener(this);
        classify_choose_bt.setOnClickListener(this);
        pop_classify_1.setOnClickListener(this);
        pop_classify_2.setOnClickListener(this);
        pop_classify_3.setOnClickListener(this);
        pop_classify_4.setOnClickListener(this);

        classifyPop = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        classifyPop.setFocusable(true);
        classifyPop.setTouchable(true);
        classifyPop.setBackgroundDrawable(new BitmapDrawable());
    }

    private void initCityPop() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View popupView = layoutInflater.inflate(R.layout.popup_area_choice, null);

        listView = (MyListView) popupView.findViewById(R.id.popup_area_listView);
        subListView = (MyListView) popupView.findViewById(R.id.popup_subListView);

        area_dimiss_bt = (TextView) popupView.findViewById(R.id.area_dimiss_bt);
        area_choose_bt = (TextView) popupView.findViewById(R.id.area_choose_bt);
        area_dimiss_bt.setOnClickListener(this);
        area_choose_bt.setOnClickListener(this);

        cityPop = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        cityPop.setFocusable(true);
        cityPop.setTouchable(true);
        cityPop.setBackgroundDrawable(new BitmapDrawable());

        try {
            myAdapter = new MyAdapter(this, GetProvinceData.getProvincesList(
                    GetProvinceData.getData(GetProvinceData.getJsonObject(area_url, this))));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        listView.setAdapter(myAdapter);

        cities = new ArrayList<>();
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == listView) {
            final int location = position;        //存储省份position

            myAdapter.setSelectedPosition(position);
            myAdapter.notifyDataSetInvalidated();

            try {
                List<Province> pro = GetProvinceData.getProvincesList(GetProvinceData.getData(GetProvinceData.getJsonObject(area_url, this)));

            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            try {
                cities = GetProvinceData.getCityList(GetProvinceData.getData(GetProvinceData.getJsonObject(area_url, this)), position);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            subAdapter = new SubAdapter(this, cities);
            subListView.setAdapter(subAdapter);
            subListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                    subAdapter.setSelectedPosition(position);
                    subAdapter.notifyDataSetChanged();
                    if (cities.get(position).getCity().equals("全部") || cities.get(position).getCity().equals("区")
                            || cities.get(position).getCity().equals("县")) {
                        //获取省份
                        String province_name = "";
                        try {
                            List<Province> provinces = GetProvinceData.getProvincesList(GetProvinceData.getData(GetProvinceData.getJsonObject(area_url, StockActivity.this)));
                            province_name = provinces.get(location).getProvince();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.i("PersonInfo", "城市---id：" + cities.get(position));
                        chooseCity = province_name;
                    } else {
                        Log.i("PersonInfo", "城市   id：" + cities.get(position));
                        chooseCity = cities.get(position).getCity().toString();

                    }
                }
            });
        }
    }


    private void getStock(String keyWord, String category_id, String city, final int page) {
        final List<Fitting> listTemp = new ArrayList<>();
        String url = ServicePort.PART_LISTS;
        ApiClientHttp api = new ApiClientHttp();
        Map<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(category_id)) {
            map.put("category_id", category_id);
        }
        if (!TextUtils.isEmpty(keyWord)) {
            map.put("keyword", keyWord);
        }
        if (!TextUtils.isEmpty(city)) {
            map.put("city", city);
        }

        map.put("page", page + "");
        AjaxParams params = api.getParam(this, map);
        if (!TextUtils.isEmpty(category_id)) {
            params.put("category_id", category_id);
        }
        if (!TextUtils.isEmpty(keyWord)) {
            params.put("keyword", keyWord);
        }
        if (!TextUtils.isEmpty(city)) {
            params.put("city", city);
        }
        params.put("page", page + "");
        api.Post(url, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                LogUtils.showLogD("--------配件库存返回数据:" + o.toString());
                String responce = o.toString();
                if (!TextUtils.isEmpty(responce)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responce);
                        int err_no = jsonObject.getInt("err_no");
                        if (err_no == 0) {
                            JSONObject results = jsonObject.getJSONObject("results");
                            JSONArray lists = results.getJSONArray("lists");
                            if (lists != null && lists.length() > 0) {
                                handle.sendEmptyMessage(1);
                                for (int i = 0; i < lists.length(); i++) {
                                    JSONObject item = lists.getJSONObject(i);
                                    Fitting f = new Fitting();
                                    f.setName(item.getString("title"));
                                    f.setImgUrl(item.getString("thumb"));
                                    f.setLastCount(item.getInt("stock"));
                                    listTemp.add(f);
                                }
                                if (page == 1) {
                                    fittingList.clear();
                                    if (listTemp.size() == 0) {
                                        LogUtils.showCenterToast(StockActivity.this, "没有数据");
                                    } else if (listTemp.size() > 0) {
                                        fittingList.addAll(listTemp);
                                        stockAdapter.notifyDataSetChanged();
                                    }

                                } else if (page > 1) {
                                    if (listTemp.size() == 0) {
                                        LogUtils.showCenterToast(StockActivity.this, "没有更多数据");
                                    } else if (listTemp.size() > 0) {
                                        fittingList.addAll(listTemp);
                                        stockAdapter.notifyDataSetChanged();
                                    }
                                }
                            } else {
                                handle.sendEmptyMessage(0);
                                LogUtils.showCenterToast(StockActivity.this, "没有数据");
                            }
                        } else if (err_no == 2100) {
                            handle.sendEmptyMessage(2);
                        } else {
                            LogUtils.showCenterToast(StockActivity.this, jsonObject.getInt("err_no") + "" + jsonObject.getString("err_msg"));
                        }
                        stock_grid_view.onRefreshComplete();
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
                stock_no_data.setVisibility(View.VISIBLE);
                stock_grid_view.setVisibility(View.INVISIBLE);
            } else if (msg.what == 1) {
                stock_no_data.setVisibility(View.INVISIBLE);
                stock_grid_view.setVisibility(View.VISIBLE);
            } else if (msg.what == 2) {
                startActivity(new Intent(StockActivity.this, LoginActivity.class));
                finish();
            }
        }
    };
}
