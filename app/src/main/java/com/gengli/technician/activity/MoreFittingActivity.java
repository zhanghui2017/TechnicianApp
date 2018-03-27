package com.gengli.technician.activity;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gengli.technician.R;
import com.gengli.technician.adapter.GetFittingAdapter;
import com.gengli.technician.adapter.MoreFittingAdapter;
import com.gengli.technician.bean.Fitting;
import com.gengli.technician.http.ApiClientHttp;
import com.gengli.technician.http.ServicePort;
import com.gengli.technician.util.LogUtils;
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

public class MoreFittingActivity extends Activity implements MoreFittingAdapter.MoreFittingCountListener, View.OnClickListener {
    private List<Fitting> fittings;
    private PullToRefreshGridView more_fitting_grid_view;
    private TextView text_more_fitting_total_count;
    private LinearLayout bt_more_fitting_commit;
    private ImageView more_fitting_back_img;
    private int totalCount = 0;
    private MoreFittingAdapter adapter;
    private int page = 1;
    private String rid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_fitting);
//        initData();
        fittings = new ArrayList<>();
        rid = getIntent().getStringExtra("order_rid");
        getStock("", "", "", page);
        init();
    }

    private void init() {
        bt_more_fitting_commit = (LinearLayout) findViewById(R.id.bt_more_fitting_commit);
        bt_more_fitting_commit.setOnClickListener(this);
        text_more_fitting_total_count = (TextView) findViewById(R.id.text_more_fitting_total_count);
        text_more_fitting_total_count.setText(totalCount + "");
        more_fitting_back_img = (ImageView) findViewById(R.id.more_fitting_back_img);
        more_fitting_back_img.setOnClickListener(this);

        more_fitting_grid_view = (PullToRefreshGridView) findViewById(R.id.more_fitting_grid_view);
        adapter = new MoreFittingAdapter(this, fittings);
        adapter.setFittingCountListener(this);
        more_fitting_grid_view.setAdapter(adapter);

        more_fitting_grid_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                getStock("", "", "", page);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                getStock("", "", "", page++);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_fitting_back_img:
                finish();
                break;
            case R.id.bt_more_fitting_commit:
                if (totalCount > 0) {
                    getPartAdd();
                } else {
                    LogUtils.showCenterToast(this, "请添加要申请的配件");
                }

                break;
        }
    }

    @Override
    public void addCount(View v) {
//        Log.d("test", "----addClick------->" + (Integer) v.getTag());
        totalCount++;
        fittings.get((Integer) v.getTag()).setCount(fittings.get((Integer) v.getTag()).getCount() + 1);
        text_more_fitting_total_count.setText(totalCount + "");
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
        text_more_fitting_total_count.setText(totalCount + "");
        adapter.notifyDataSetChanged();
    }


    private void getStock(String keyWord, String category_id, String city, final int page) {
        final List<Fitting> listTemp = new ArrayList<>();
        String url = ServicePort.PART_LISTS;
        ApiClientHttp api = new ApiClientHttp();
        Map<String, String> map = new HashMap<>();
        map.put("keyWord", keyWord);
        map.put("category_id", category_id);
        map.put("city", city);
        map.put("page", page + "");
        AjaxParams params = api.getParam(this, map);
        params.put("keyWord", keyWord);
        params.put("category_id", category_id);
        params.put("city", city);
        params.put("page", page + "");
        api.Post(url, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                LogUtils.showLogD("--------更多配件返回数据:" + o.toString());
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
                                    f.setId(item.getInt("id") + "");
                                    f.setName(item.getString("title"));
                                    f.setImgUrl(item.getString("thumb"));
                                    f.setLastCount(item.getInt("stock"));
                                    listTemp.add(f);
                                }
                                if (page == 1) {
                                    fittings.clear();
                                    if (listTemp.size() == 0) {
                                        LogUtils.showCenterToast(MoreFittingActivity.this, "没有数据");
                                    } else if (listTemp.size() > 0) {
                                        fittings.addAll(listTemp);
                                        adapter.notifyDataSetChanged();
                                    }

                                } else if (page > 1) {
                                    if (listTemp.size() == 0) {
                                        LogUtils.showCenterToast(MoreFittingActivity.this, "没有更多数据");
                                    } else if (listTemp.size() > 0) {
                                        fittings.addAll(listTemp);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            } else {
//                                handle.sendEmptyMessage(0);
                                LogUtils.showCenterToast(MoreFittingActivity.this, "没有数据");
                            }
                        } else {
                            LogUtils.showCenterToast(MoreFittingActivity.this, jsonObject.getInt("err_no") + "" + jsonObject.getString("err_msg"));
                        }
                        more_fitting_grid_view.onRefreshComplete();
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
                    obj.put(fittings.get(i).getId(), fittings.get(i).getCount());
                } catch (JSONException e) {

                }
            }
        }
        Map<String, String> map = new HashMap<>();
        map.put("rid", rid);
        map.put("parts", obj.toString());
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
                            LogUtils.showCenterToast(MoreFittingActivity.this, jsonObject.getInt("err_no") + "" + jsonObject.getString("text"));
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
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                finish();
            }
        }
    };


}
