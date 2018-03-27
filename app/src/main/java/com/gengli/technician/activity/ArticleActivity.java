package com.gengli.technician.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.gengli.technician.R;
import com.gengli.technician.http.ApiClientHttp;
import com.gengli.technician.http.ServicePort;
import com.gengli.technician.util.LogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ArticleActivity extends Activity implements View.OnClickListener {
    private ImageView img_article_back;
    private ImageView img_article_collect;
    private WebView webview_article;
    private int articleid;
    private String loadUrl;
    private boolean isFav = false;
    private int id;
    private TextView article_title_text;
    private TextView article_category_name_text;
    private TextView article_create_time_text;
    private String title;
    private String category_name;
    private String create_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Intent intent = getIntent();
        articleid = intent.getIntExtra("articleid", -1);
        initView();
        getArticleDetile();
    }

    private void initView() {
        img_article_back = (ImageView) findViewById(R.id.img_article_back);
        img_article_collect = (ImageView) findViewById(R.id.img_article_collect);
        article_title_text = (TextView) findViewById(R.id.article_title_text);
        article_category_name_text = (TextView) findViewById(R.id.article_category_name_text);
        article_create_time_text = (TextView) findViewById(R.id.article_create_time_text);
        img_article_back.setOnClickListener(this);
        img_article_collect.setOnClickListener(this);
        webview_article = (WebView) findViewById(R.id.webview_article);

        WebSettings settings = webview_article.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setJavaScriptEnabled(true);
        webview_article.setWebViewClient(new MyWebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            imgReset();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private void imgReset() {
        webview_article.loadUrl("javascript:(function(){"
                + "var objs = document.getElementsByTagName('img'); "
                + "for(var i=0;i<objs.length;i++)  " + "{"
                + "var img = objs[i];   "
                + "    img.style.width = '100%';   "
                + "    img.style.height = 'auto';   "
                + "}" + "})()");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_article_back:
                finish();
                break;
            case R.id.img_article_collect:
                if (isFav) {
                    removeFav();
                } else {
                    addFavour();
                }

                break;
        }
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 101:
                    article_title_text.setText(title);
                    article_category_name_text.setText(category_name);
                    article_create_time_text.setText(create_time);
                    webview_article.loadUrl(loadUrl);
                    if (isFav) {
                        img_article_collect.setImageResource(R.drawable.img_collect_sele);
                    } else {
                        img_article_collect.setImageResource(R.drawable.img_collect);
                    }
                    break;
                case 102:
                    img_article_collect.setImageResource(R.drawable.img_collect_sele);
                    isFav = true;
                    break;
                case 103:
                    img_article_collect.setImageResource(R.drawable.img_collect);
                    isFav = false;
                    break;
            }
        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview_article.canGoBack()) {
            webview_article.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 获取文章详情
     */
    private void getArticleDetile() {
        String url = ServicePort.ARCHIVE_DETAIL;
        ApiClientHttp api = new ApiClientHttp();
        Map<String, String> map = new HashMap<>();
        map.put("id", articleid + "");

        AjaxParams params = api.getParam(this, map);
        params.put("id", articleid + "");

        api.Post(url, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                String responce = o.toString();
                LogUtils.showLogD("----->文章详情返回数据----->" + responce);
                if (!TextUtils.isEmpty(responce)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responce);
                        int err_no = jsonObject.getInt("err_no");
                        if (err_no == 0) {
                            JSONObject results = jsonObject.getJSONObject("results");
                            if (!TextUtils.isEmpty(results.toString())) {
                                loadUrl = results.getString("h5_url");
                                isFav = results.getBoolean("is_fav");
                                id = results.getInt("id");
                                title = results.getString("title");
                                category_name = results.getString("category_name");
                                create_time = results.getString("create_time");
                                handler.sendEmptyMessage(101);
                            }
                        } else {
                            LogUtils.showCenterToast(ArticleActivity.this, jsonObject.getInt("err_no") + "" + jsonObject.getString("err_msg"));
                        }
                    } catch (JSONException e) {

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
     * 取消收藏
     */
    public void removeFav() {
        String url = ServicePort.FAV_REMOVE;
        ApiClientHttp api = new ApiClientHttp();
        Map<String, String> map = new HashMap<>();
        map.put("id", articleid + "");
        AjaxParams params = api.getParam(this, map);
        params.put("id", articleid + "");
        api.Post(url, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                String responce = o.toString();
                LogUtils.showLogD("----->删除收藏返回数据----->" + responce);
                if (!TextUtils.isEmpty(responce)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responce);
                        int err_no = jsonObject.getInt("err_no");
                        if (err_no == 0) {
                            JSONObject results = jsonObject.getJSONObject("results");
                            if (!TextUtils.isEmpty(results.toString())) {
                                handler.sendEmptyMessage(103);
                                LogUtils.showCenterToast(ArticleActivity.this, "取消收藏成功");
                            }
                        } else {
                            LogUtils.showCenterToast(ArticleActivity.this, jsonObject.getInt("err_no") + "" + jsonObject.getString("err_msg"));
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

    /**
     * 添加收藏
     */
    public void addFavour() {
        String url = ServicePort.FAV_ADD;
        ApiClientHttp api = new ApiClientHttp();
        Map<String, String> map = new HashMap<>();
        map.put("type", "archive");
        map.put("info_id", articleid + "");
        AjaxParams params = api.getParam(this, map);
        params.put("type", "archive");
        params.put("info_id", articleid + "");

        api.Post(url, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                String responce = o.toString();
                LogUtils.showLogD("----->添加收藏返回数据----->" + responce);
                if (!TextUtils.isEmpty(responce)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responce);
                        int err_no = jsonObject.getInt("err_no");
                        if (err_no == 0) {
                            LogUtils.showCenterToast(ArticleActivity.this, "收藏成功");
                            handler.sendEmptyMessage(102);
                        } else {
                            LogUtils.showCenterToast(ArticleActivity.this, jsonObject.getInt("err_no") + "" + jsonObject.getString("err_msg"));
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
}
