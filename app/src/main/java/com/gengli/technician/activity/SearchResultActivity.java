package com.gengli.technician.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gengli.technician.R;
import com.gengli.technician.adapter.HelpAdapter;
import com.gengli.technician.adapter.RecordAdapter;
import com.gengli.technician.bean.Article;
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

public class SearchResultActivity extends Activity implements View.OnClickListener {
    private ImageView search_result_back_img;
    private PullToRefreshListView search_result_list_view;
    private HelpAdapter adapter;
    private List<Article> articalList;
    private LinearLayout view_search_result_no_data;

    private int page = 1;
    private String searchString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        searchString = getIntent().getStringExtra("SearchKey");
        LogUtils.showLogD("----------------------------" + searchString);
        getSearchArchive(page);
        initView();
    }

    private void initView() {
        articalList = new ArrayList<>();
        view_search_result_no_data = (LinearLayout) findViewById(R.id.view_search_result_no_data);
        search_result_back_img = (ImageView) findViewById(R.id.search_result_back_img);
        search_result_back_img.setOnClickListener(this);
        search_result_list_view = (PullToRefreshListView) findViewById(R.id.search_result_list_view);
        adapter = new HelpAdapter(articalList);
        search_result_list_view.setAdapter(adapter);
        search_result_list_view.setMode(PullToRefreshBase.Mode.BOTH);
        search_result_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int articleid = articalList.get(position).getId();
                Intent intent = new Intent(SearchResultActivity.this, ArticleActivity.class);
                intent.putExtra("articleid", articleid);
                startActivity(intent);
            }
        });
        search_result_list_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getSearchArchive(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                LogUtils.showLogD("--->recordeList page = " + (page++));
                getSearchArchive(page++);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.record_back_img:
                finish();
                break;
        }
    }


    public void getSearchArchive(final int page) {
        final List<Article> listTemp = new ArrayList<>();
        String url = ServicePort.ARCHIVE_LISTS;
        ApiClientHttp api = new ApiClientHttp();
        Map<String, String> map = new HashMap<>();
        map.put("keyword", searchString);
        map.put("page", page + "");
        map.put("size", 10 + "");
        AjaxParams params = api.getParam(this, map);
        params.put("keyword", searchString);
        params.put("page", page + "");
        params.put("size", 10 + "");
        api.Post(url, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                String responce = o.toString();
                LogUtils.showLogD("----->搜索列表返回数据----->" + responce);
                if (!TextUtils.isEmpty(responce)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responce);
                        int err_no = jsonObject.getInt("err_no");
                        if (err_no == 0) {
                            JSONObject results = jsonObject.getJSONObject("results");
                            if (!TextUtils.isEmpty(results.toString())) {
                                JSONArray articles = results.getJSONArray("list");
                                if (articles != null && articles.length() > 0) {
                                    for (int i = 0; i < articles.length(); i++) {
                                        JSONObject item = articles.getJSONObject(i);
                                        Article article = new Article();
                                        article.setId(item.getInt("id"));
                                        article.setTitle(item.getString("title"));
                                        article.setCount(item.getInt("num_click"));
                                        article.setTime(item.getString("create_time"));
                                        article.setImgUrl(item.getString("thumb"));
                                        listTemp.add(article);
                                    }
                                    if (page == 1) {
                                        articalList.clear();
                                        if (listTemp.size() == 0) {
                                            LogUtils.showCenterToast(SearchResultActivity.this, "没有数据");
                                        } else if (listTemp.size() > 0) {
                                            articalList.addAll(listTemp);
                                            adapter.notifyDataSetChanged();

                                        }

                                    } else if (page > 1) {
                                        if (listTemp.size() == 0) {
                                            LogUtils.showCenterToast(SearchResultActivity.this, "没有更多数据");
                                        } else if (listTemp.size() > 0) {
                                            articalList.addAll(listTemp);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                } else {
                                    if (page > 1) {
                                        LogUtils.showCenterToast(SearchResultActivity.this, "没有更多数据");
                                    } else {
                                        handler.sendEmptyMessage(1);
                                        LogUtils.showCenterToast(SearchResultActivity.this, "没有数据");
                                    }

                                }
                            } else {

                                LogUtils.showCenterToast(SearchResultActivity.this, "没有数据");
                            }
                        } else {
                            LogUtils.showCenterToast(SearchResultActivity.this, jsonObject.getInt("err_no") + "" + jsonObject.getString("err_msg"));
                        }
                        search_result_list_view.onRefreshComplete();
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

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO 自动生成的方法存根
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    view_search_result_no_data.setVisibility(View.VISIBLE);
                    search_result_list_view.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    break;
                default:
                    break;
            }
        }
    };

}
