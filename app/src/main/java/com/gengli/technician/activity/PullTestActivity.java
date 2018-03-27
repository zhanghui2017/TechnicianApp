package com.gengli.technician.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gengli.technician.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.jpush.android.api.JPushInterface;

public class PullTestActivity extends Activity {
    private ArrayList<String> mListItems;
    /**
     * 上拉刷新的控件
     */
    private PullToRefreshListView mPullRefreshListView;

    private ArrayAdapter<String> mAdapter;

    private int mItemCount = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_test);

//        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
//        JPushInterface.init(getApplicationContext());     		// 初始化 JPush


        // 得到控件
        initDatas();
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_to_refresh_listview);

        //设置适配器
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mListItems);
        mPullRefreshListView.setAdapter(mAdapter);
        // 设置监听事件
        mPullRefreshListView
                .setOnRefreshListener(new OnRefreshListener<ListView>() {
                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        String label = DateUtils.formatDateTime(
                                getApplicationContext(),
                                System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME
                                        | DateUtils.FORMAT_SHOW_DATE
                                        | DateUtils.FORMAT_ABBREV_ALL);
                        // 显示最后更新的时间
                        refreshView.getLoadingLayoutProxy()
                                .setLastUpdatedLabel(label);

                        // 模拟加载任务
                        new GetDataTask().execute();
                    }
                });
        mPullRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(PullTestActivity.this, LocationActivity.class));
            }
        });
    }

    private void initDatas() {
        // 初始化数据和数据源
        mListItems = new ArrayList<String>();

        for (int i = 0; i < mItemCount; i++) {
            mListItems.add("" + i);
        }
    }

    private class GetDataTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            return "" + (mItemCount++);
        }

        @Override
        protected void onPostExecute(String result) {
            mListItems.add(result);
            mAdapter.notifyDataSetChanged();
            // Call onRefreshComplete when the list has been refreshed.
            mPullRefreshListView.onRefreshComplete();
        }
    }

}
