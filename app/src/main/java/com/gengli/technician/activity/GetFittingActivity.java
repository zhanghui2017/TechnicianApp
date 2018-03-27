package com.gengli.technician.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gengli.technician.R;
import com.gengli.technician.adapter.GetFittingAdapter;
import com.gengli.technician.adapter.MyAdapter;
import com.gengli.technician.adapter.SubAdapter;
import com.gengli.technician.bean.City;
import com.gengli.technician.bean.Fitting;
import com.gengli.technician.bean.Province;
import com.gengli.technician.http.ApiClientHttp;
import com.gengli.technician.http.ServicePort;
import com.gengli.technician.util.GetProvinceData;
import com.gengli.technician.util.LogUtils;
import com.gengli.technician.view.ImageSlideView;
import com.gengli.technician.view.MyListView;
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

/**
 * 申请配件
 */
public class GetFittingActivity extends Activity implements GetFittingAdapter.FittingCountListener, View.OnClickListener, AdapterView.OnItemClickListener {

    private GetFittingAdapter adapter;
    private List<Fitting> fittings;
    private PullToRefreshListView get_fitting_list;
    private TextView text_get_fitting_total_count;
    private LinearLayout bt_get_fitting_commit;
    private ImageView get_fitting_back_img;
    private int totalCount = 0;
    private LinearLayout get_fitting_classify_view;
    private LinearLayout get_fitting_area_view;
    private EditText edit_get_fitting;
    private TextView text_get_fitting_area;
    private TextView text_get_fitting_classify;
    //    private ImageView get_fitting_classify_img;
//    private ImageView get_fitting_area_img;
    private LinearLayout get_fitting_search_view;
    private boolean scrollFlag = false;// 标记是否滑动
    private int lastVisibleItemPosition;// 标记上次滑动位置
    private TextView get_fitting_search_bt;


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

    private String chooseType = "";
    private String rid = "";

    int page = 1;
    private String[] type = {"液压", "电气", "机械", "常用易损件"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_fitting);
        rid = getIntent().getStringExtra("order_rid");
        fittings = new ArrayList<>();
        getStock("", "", "", page);
//        initData();
        init();

    }

    public void init() {
        get_fitting_search_view = (LinearLayout) findViewById(R.id.get_fitting_search_view);
        edit_get_fitting = (EditText) findViewById(R.id.edit_get_fitting);
        text_get_fitting_area = (TextView) findViewById(R.id.text_get_fitting_area);
        text_get_fitting_classify = (TextView) findViewById(R.id.text_get_fitting_classify);
        text_get_fitting_classify.setOnClickListener(this);
        text_get_fitting_area.setOnClickListener(this);
        bt_get_fitting_commit = (LinearLayout) findViewById(R.id.bt_get_fitting_commit);
        get_fitting_classify_view = (LinearLayout) findViewById(R.id.get_fitting_classify_view);
        get_fitting_area_view = (LinearLayout) findViewById(R.id.get_fitting_area_view);
//        get_fitting_classify_img = (ImageView) findViewById(R.id.get_fitting_classify_img);
//        get_fitting_area_img = (ImageView) findViewById(R.id.get_fitting_area_img);
        text_get_fitting_total_count = (TextView) findViewById(R.id.text_get_fitting_total_count);
        get_fitting_back_img = (ImageView) findViewById(R.id.get_fitting_back_img);
        get_fitting_list = (PullToRefreshListView) findViewById(R.id.get_fitting_list);
        get_fitting_search_bt = (TextView) findViewById(R.id.get_fitting_search_bt);
        get_fitting_search_bt.setOnClickListener(this);

        get_fitting_area_view.setOnClickListener(this);
        get_fitting_classify_view.setOnClickListener(this);
        bt_get_fitting_commit.setOnClickListener(this);
        text_get_fitting_total_count.setText(totalCount + "");
        get_fitting_back_img.setOnClickListener(this);
        adapter = new GetFittingAdapter(this, fittings);
        adapter.setFittingCountListener(this);
        get_fitting_list.setAdapter(adapter);

        get_fitting_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getStock("", "", "", page);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getStock("", "", "", page++);
            }
        });

        get_fitting_list.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                        get_fitting_search_view.setVisibility(View.GONE);
                    }
                    if (firstVisibleItem < lastVisibleItemPosition) {//下滑
                        get_fitting_search_view.setVisibility(View.GONE);
                    }
                    if (firstVisibleItem == 0) {
                        get_fitting_search_view.setVisibility(View.VISIBLE);

                    }

                }
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_fitting_back_img:
                finish();
                break;
            case R.id.bt_get_fitting_commit:
                if (totalCount <= 0) {
                    LogUtils.showCenterToast(this, "请选择要申请的配件");
                }
                getPartAdd();
                break;
            case R.id.get_fitting_classify_view:
                getClassifyPop();
                classifyPop.showAtLocation(v, Gravity.CENTER, 0, 0);
                break;
            case R.id.text_get_fitting_area:
                getCityPopup();
                cityPop.showAtLocation(v, Gravity.CENTER, 0, 0);
                break;
//            case R.id.edit_get_fitting:
//
//                break;
            case R.id.area_dimiss_bt:
                cityPop.dismiss();
                break;
            case R.id.area_choose_bt:
                text_get_fitting_area.setText(chooseCity);
                cityPop.dismiss();
                break;
            case R.id.classify_dimiss_bt:
                classifyPop.dismiss();
                break;
            case R.id.classify_choose_bt:
                text_get_fitting_classify.setText(chooseType);
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

            case R.id.get_fitting_search_bt:
                String keyWord = edit_get_fitting.getText().toString();
                getStock(keyWord, chooseType, chooseCity, 1);
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
                            List<Province> provinces = GetProvinceData.getProvincesList(GetProvinceData.getData(GetProvinceData.getJsonObject(area_url, GetFittingActivity.this)));
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

    //    Map<String, Integer >chooseFitting = new HashMap<>();
    @Override
    public void addCount(View v) {
//        Log.d("test", "----addClick------->" + (Integer) v.getTag());
        totalCount++;
        fittings.get((Integer) v.getTag()).setCount(fittings.get((Integer) v.getTag()).getCount() + 1);
        text_get_fitting_total_count.setText(totalCount + "");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void minusCount(View v) {
//        Log.d("test", "----minusClick------->" + (Integer) v.getTag());
        int count = fittings.get((Integer) v.getTag()).getCount();
        if (count <= 0 || totalCount <= 0)
            return;
        totalCount--;
        fittings.get((Integer) v.getTag()).setCount(count - 1);
        text_get_fitting_total_count.setText(totalCount + "");
        adapter.notifyDataSetChanged();
    }

    private void getStock(String keyWord, String category_id, String city, final int page) {
        final List<Fitting> listTemp = new ArrayList<>();
        String url = ServicePort.PART_LISTS;
        ApiClientHttp api = new ApiClientHttp();
        Map<String, String> map = new HashMap<>();
        map.put("keyword", keyWord);
        map.put("category_id", category_id);
        map.put("city", city);
        map.put("page", page + "");
        AjaxParams params = api.getParam(this, map);
        params.put("keyword", keyWord);
        params.put("category_id", category_id);
        params.put("city", city);
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
//                                handle.sendEmptyMessage(1);
                                for (int i = 0; i < lists.length(); i++) {
                                    JSONObject item = lists.getJSONObject(i);
                                    Fitting f = new Fitting();
                                    f.setId(item.getInt("id") + "");
                                    f.setName(item.getString("title"));
                                    f.setImgUrl(item.getString("thumb"));
                                    f.setLastCount(item.getInt("stock"));
                                    listTemp.add(f);
                                }
                                if (page == 1) {
                                    fittings.clear();
                                    if (listTemp.size() == 0) {
                                        LogUtils.showCenterToast(GetFittingActivity.this, "没有数据");
                                    } else if (listTemp.size() > 0) {
                                        fittings.addAll(listTemp);
                                        adapter.notifyDataSetChanged();
                                    }

                                } else if (page > 1) {
                                    if (listTemp.size() == 0) {
                                        LogUtils.showCenterToast(GetFittingActivity.this, "没有更多数据");
                                    } else if (listTemp.size() > 0) {
                                        fittings.addAll(listTemp);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            } else {
//                                handle.sendEmptyMessage(0);
                                LogUtils.showCenterToast(GetFittingActivity.this, "没有数据");
                            }
                        } else {
                            LogUtils.showCenterToast(GetFittingActivity.this, jsonObject.getInt("err_no") + "" + jsonObject.getString("err_msg"));
                        }
                        get_fitting_list.onRefreshComplete();
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

    /**
     * 申请配件
     */
    public void getPartAdd() {
        String url = ServicePort.REPAIR_PART_ADD;
        ApiClientHttp api = new ApiClientHttp();
        JSONObject obj = new JSONObject();
        for (int i = 0; i < fittings.size(); i++) {
            if (fittings.get(i).getCount() != 0) {
                try {
                    obj.put(fittings.get(i).getId(), fittings.get(i).getCount() + "");
                } catch (JSONException e) {

                }
            }
        }
        Map<String, String> map = new HashMap<>();
        map.put("rid", rid);
        map.put("parts", obj.toString());
        LogUtils.showLogD(" .....1..... " + rid);
        LogUtils.showLogD(" ......2.... " + obj.toString());
        AjaxParams params = api.getParam(this, map);
        params.put("rid", rid);
        params.put("parts", obj.toString());

        api.Post(url, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                String responce = o.toString();
                LogUtils.showLogD("----->申请配件返回数据----->" + responce);
                if (!TextUtils.isEmpty(responce)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responce);
                        int err_no = jsonObject.getInt("err_no");
                        if (err_no == 0) {
                            handle.sendEmptyMessage(0);
                        } else {
                            LogUtils.showLogD(jsonObject.getInt("err_no") + "" + jsonObject.getString("text"));
                            LogUtils.showCenterToast(GetFittingActivity.this, jsonObject.getInt("err_no") + "" + jsonObject.getString("text"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                LogUtils.showLogD(" ---------------- " + strMsg);
            }
        });
    }

    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                finish();
            }
        }
    };

//    public void initData() {
//        fittings = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            Fitting f = new Fitting();
//            f.setName("隧道钢筋网焊网机" + i);
//            f.setImgUrl("http://pic3.zhimg.com/b5c5fc8e9141cb785ca3b0a1d037a9a2.jpg");
//            f.setLastCount(100);
//            fittings.add(f);
//        }
//    }
}
