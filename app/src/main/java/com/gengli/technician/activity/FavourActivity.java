package com.gengli.technician.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gengli.technician.R;
import com.gengli.technician.adapter.HelpAdapter;
import com.gengli.technician.bean.Article;
import com.gengli.technician.http.ApiClientHttp;
import com.gengli.technician.http.ServicePort;
import com.gengli.technician.util.LogUtils;
import com.gengli.technician.util.SystemMsgUtil;
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

public class FavourActivity extends Activity implements View.OnClickListener {

    private ImageView img_favour_back;
    private PullToRefreshListView favour_list_view;
    private HelpAdapter adapter;
    private List<Article> articalList;
    private LinearLayout favour_no_data;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        getFavours(page);
        initView();
    }

    private void initView() {
        articalList = new ArrayList<>();
        favour_no_data = (LinearLayout) findViewById(R.id.favour_no_data);
        img_favour_back = (ImageView) findViewById(R.id.img_favour_back);
        img_favour_back.setOnClickListener(this);
        favour_list_view = (PullToRefreshListView) findViewById(R.id.favour_list_view);
        adapter = new HelpAdapter(articalList);
        favour_list_view.setAdapter(adapter);
        favour_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int articleid = articalList.get(position - 1).getId();
                Intent intent = new Intent(FavourActivity.this, ArticleActivity.class);
                intent.putExtra("articleid", articleid);
                startActivity(intent);
            }
        });
        favour_list_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getFavours(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                LogUtils.showLogD("--->favourlist page = " + (page++));
                getFavours(page++);

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == img_favour_back) {
            finish();
        }
    }

    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                favour_no_data.setVisibility(View.VISIBLE);
                favour_list_view.setVisibility(View.INVISIBLE);
            } else if (msg.what == 1) {
                favour_no_data.setVisibility(View.INVISIBLE);
                favour_list_view.setVisibility(View.VISIBLE);
            } else if (msg.what == 2) {
                startActivity(new Intent(FavourActivity.this, LoginActivity.class));
                finish();
            }
        }
    };

    private void getFavours(final int page) {
        final List<Article> listTemp = new ArrayList<>();
        String url = ServicePort.FAV_LISTS;
        ApiClientHttp api = new ApiClientHttp();
        Map<String, String> map = new HashMap<>();
        map.put("type", "archive");
        map.put("page", page + "");
        AjaxParams params = api.getParam(this, map);
        params.put("type", "archive");
        params.put("page", page + "");
        api.Post(url, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                String responce = o.toString();
                LogUtils.showLogD("----->收藏列表返回数据----->" + responce);
                if (!TextUtils.isEmpty(responce)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responce);
                        int err_no = jsonObject.getInt("err_no");
                        if (err_no == 0) {
                            JSONObject results = jsonObject.getJSONObject("results");
                            if (!TextUtils.isEmpty(results.toString())) {
                                JSONArray articles = results.getJSONArray("lists");
                                if (articles != null && articles.length() > 0) {
                                    handle.sendEmptyMessage(1);
                                    for (int i = 0; i < articles.length(); i++) {
                                        JSONObject item = articles.getJSONObject(i);
                                        Article article = new Article();
                                        article.setId(item.getInt("info_id"));
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
                                            LogUtils.showCenterToast(FavourActivity.this, "没有数据");
                                        } else if (listTemp.size() > 0) {
                                            articalList.addAll(listTemp);
                                            adapter.notifyDataSetChanged();
                                            favour_list_view.onRefreshComplete();

                                        }
                                    } else if (page > 1) {
                                        if (listTemp.size() == 0) {
                                            LogUtils.showCenterToast(FavourActivity.this, "没有更多数据");
                                        } else if (listTemp.size() > 0) {
                                            articalList.addAll(listTemp);
                                            adapter.notifyDataSetChanged();
                                            favour_list_view.onRefreshComplete();
                                        }
                                    }
                                    favour_list_view.onRefreshComplete();
                                } else {
                                    handle.sendEmptyMessage(0);
                                    LogUtils.showCenterToast(FavourActivity.this, "没有数据");
                                }
                            } else {
                                handle.sendEmptyMessage(0);
                                LogUtils.showCenterToast(FavourActivity.this, "没有数据");
                            }
                        } else if (err_no == 2100) {
                            handle.sendEmptyMessage(2);
                        } else {
                            LogUtils.showCenterToast(FavourActivity.this, jsonObject.getInt("err_no") + "" + jsonObject.getString("err_msg"));
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


}
