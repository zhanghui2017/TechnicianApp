package com.gengli.technician.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gengli.technician.R;
import com.gengli.technician.adapter.MessageAdapter;
import com.gengli.technician.adapter.RecordAdapter;
import com.gengli.technician.bean.Article;
import com.gengli.technician.bean.Message;
import com.gengli.technician.http.ApiClientHttp;
import com.gengli.technician.http.ServicePort;
import com.gengli.technician.slide.ISlide;
import com.gengli.technician.slide.OnClickSlideItemListener;
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

public class RecordActivity extends Activity implements View.OnClickListener {
    private ImageView record_back_img;
    private PullToRefreshListView record_list_view;
    private RecordAdapter adapter;
    private List<Article> articalList;
    private TextView text_record_clear;
    private LinearLayout view_no_data;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
//        initData();
        getArchive(page);
        initView();
    }

    private void initView() {
        articalList = new ArrayList<>();
        view_no_data = (LinearLayout) findViewById(R.id.view_no_data);
        record_back_img = (ImageView) findViewById(R.id.record_back_img);
        record_back_img.setOnClickListener(this);
        text_record_clear = (TextView) findViewById(R.id.text_record_clear);
        text_record_clear.setOnClickListener(this);
        record_list_view = (PullToRefreshListView) findViewById(R.id.record_list_view);
        adapter = new RecordAdapter(articalList);
        record_list_view.setAdapter(adapter);
        adapter.setupListView(record_list_view);
        adapter.setOnClickSlideItemListener(new RecordItemClick());
        record_list_view.setMode(PullToRefreshBase.Mode.BOTH);
//        record_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                int articleid = articalList.get(position).getId();
//                Intent intent = new Intent(RecordActivity.this, ArticleActivity.class);
//                intent.putExtra("articleid", articleid);
//                startActivity(intent);
//            }
//        });
        record_list_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getArchive(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                LogUtils.showLogD("--->recordeList page = " + (page++));
                getArchive(page++);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.record_back_img:
                finish();
                break;
            case R.id.text_record_clear:
                removeArticle("all");
                articalList.clear();
                adapter.notifyDataSetChanged();
                view_no_data.setVisibility(View.VISIBLE);
                record_list_view.setVisibility(View.INVISIBLE);
                break;
        }
    }

    public class RecordItemClick implements OnClickSlideItemListener {
        @Override
        public void onItemClick(ISlide iSlideView, View v, int position) {
            int articleid = articalList.get(position).getId();
            Intent intent = new Intent(RecordActivity.this, ArticleActivity.class);
            intent.putExtra("articleid", articleid);
            startActivity(intent);
        }

        @Override
        public void onClick(ISlide iSlideView, View v, int position) {
            if (v.getId() == R.id.btn_del) {
                LogUtils.showLogD(articalList.get(position).getId() + "");
                removeArticle(articalList.get(position).getId() + "");
                iSlideView.close();
                articalList.remove(position);
                adapter.notifyDataSetChanged();
            }
        }
    }


    private void removeArticle(String id) {
        String url = ServicePort.LOG_ARCHIVE_REMOVE;
        ApiClientHttp api = new ApiClientHttp();
        Map<String, String> map = new HashMap<>();
        map.put("id", id + "");
        AjaxParams params = api.getParam(this, map);
        params.put("id", id + "");
        api.Post(url, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                String responce = o.toString();
                LogUtils.showLogD("----->浏览记录删除返回数据----->" + responce);
                if (!TextUtils.isEmpty(responce)) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(responce);
                        int err_no = jsonObject.getInt("err_no");
                        if (err_no == 0) {
                            LogUtils.showLogD("删除成功");
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

    public void getArchive(final int page) {
        final List<Article> listTemp = new ArrayList<>();
        String url = ServicePort.LOG_ARCHIVE;
        ApiClientHttp api = new ApiClientHttp();
        Map<String, String> map = new HashMap<>();
        map.put("page", page + "");
        map.put("size", 10 + "");
        AjaxParams params = api.getParam(this, map);
        params.put("page", page + "");
        params.put("size", 10 + "");
        api.Post(url, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                String responce = o.toString();
                LogUtils.showLogD("----->浏览记录列表返回数据----->" + responce);
                if (!TextUtils.isEmpty(responce)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responce);
                        int err_no = jsonObject.getInt("err_no");
                        if (err_no == 0) {
                            JSONObject results = jsonObject.getJSONObject("results");
                            if (!TextUtils.isEmpty(results.toString())) {
                                JSONArray articles = results.getJSONArray("lists");
                                if (articles != null && articles.length() > 0) {
                                    for (int i = 0; i < articles.length(); i++) {
                                        JSONObject item = articles.getJSONObject(i);
                                        Article article = new Article();
                                        article.setId(item.getInt("aid"));
                                        article.setTitle(item.getString("title"));
                                        article.setCount(item.getInt("num_click"));
                                        article.setTime(item.getString("create_time"));
                                        article.setImgUrl(item.getString("thumb"));
                                        article.setDes(item.getString("des").toString().trim());
                                        listTemp.add(article);
                                    }
                                    if (page == 1) {
                                        articalList.clear();
                                        if (listTemp.size() == 0) {
                                            LogUtils.showCenterToast(RecordActivity.this, "没有数据");
                                        } else if (listTemp.size() > 0) {
                                            articalList.addAll(listTemp);
                                            adapter.notifyDataSetChanged();

                                        }

                                    } else if (page > 1) {
                                        if (listTemp.size() == 0) {
                                            LogUtils.showCenterToast(RecordActivity.this, "没有更多数据");
                                        } else if (listTemp.size() > 0) {
                                            articalList.addAll(listTemp);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                } else {
                                    LogUtils.showCenterToast(RecordActivity.this, "没有数据");
                                }
                            } else {
                                LogUtils.showCenterToast(RecordActivity.this, "没有数据");
                            }
                        } else {
                            LogUtils.showCenterToast(RecordActivity.this, jsonObject.getInt("err_no") + "" + jsonObject.getString("err_msg"));
                        }
                        record_list_view.onRefreshComplete();
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
}
