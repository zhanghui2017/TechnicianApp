package com.gengli.technician.activity;

import java.util.ArrayList;
import java.util.List;

import com.gengli.technician.R;
import com.gengli.technician.adapter.SearchKeyAdapter;
import com.gengli.technician.adapter.SearchWordAdapter;
import com.gengli.technician.bean.HotKey;
import com.gengli.technician.fragment.HelpFragment;
import com.gengli.technician.http.ApiClientHttp;
import com.gengli.technician.http.ServicePort;
import com.gengli.technician.listener.MyDialogListener;
import com.gengli.technician.util.DatasUtil;
import com.gengli.technician.util.LogUtils;
import com.gengli.technician.view.ListViewForScrollView;
import com.gengli.technician.view.MyDialog;
import com.gengli.technician.view.MyGridView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchActivity extends Activity implements OnClickListener {

    private ImageButton search_lbt, search_clear_bt;
    private EditText search_edittext;
    private Button search_bt;
    private ListViewForScrollView search_histroy_listview;
    private LinearLayout search_key_delete_layout, search_key_delete_bt;
    private MyGridView search_gridview;
    private SearchKeyAdapter sKeyAdapter;

    private SearchWordAdapter swAdapter;
    private List<String> list = new ArrayList<String>();
    private List<String> keyList = new ArrayList<String>();

    private MyDialog myDialog;

    private int ADD_RECORD = 1;
    private int DELETE_RECORD = 2;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO 自动生成的方法存根
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    getSearchRecord(true);
                    break;
                case 2:
//				getSearchRecord(false);
                    swAdapter.notifyDataSetChanged();
                    controlClearLayout();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO 自动生成的方法存根
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);
        inSearchView();
        getHotwords();
        getSearchRecord(false);
    }

    private void inSearchView() {
        search_lbt = (ImageButton) findViewById(R.id.search_lbt);
        search_clear_bt = (ImageButton) findViewById(R.id.search_clear_bt);

        search_edittext = (EditText) findViewById(R.id.search_edittext);
        search_edittext.addTextChangedListener(watcher);

        search_bt = (Button) findViewById(R.id.search_bt);

        search_histroy_listview = (ListViewForScrollView) findViewById(R.id.search_histroy_listview);
        swAdapter = new SearchWordAdapter(SearchActivity.this, list);
        search_histroy_listview.setAdapter(swAdapter);
        search_histroy_listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO 自动生成的方法存根
				String word = swAdapter.list.get(position);
				Intent intent = new Intent(SearchActivity.this,SearchResultActivity.class);
				intent.putExtra("SearchKey",word);
				startActivity(intent);

            }
        });

        search_key_delete_layout = (LinearLayout) findViewById(R.id.search_key_delete_layout);
        search_key_delete_bt = (LinearLayout) findViewById(R.id.search_key_delete_bt);
        search_gridview = (MyGridView) findViewById(R.id.search_gridview);
        sKeyAdapter = new SearchKeyAdapter(keyList, this);
        search_gridview.setAdapter(sKeyAdapter);
        search_gridview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO 自动生成的方法存根
                String keyword = sKeyAdapter.list.get(position);
                addSearchRecord(keyword);
                handler.sendEmptyMessage(ADD_RECORD);
                Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                intent.putExtra("SearchKey", keyword);
                startActivity(intent);
            }
        });

        search_lbt.setOnClickListener(this);
        search_clear_bt.setOnClickListener(this);
        search_bt.setOnClickListener(this);
        search_key_delete_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO 点击事件
        switch (v.getId()) {
            case R.id.search_lbt:
                finish();
                break;
            case R.id.search_clear_bt:
                search_edittext.setText("");
                break;
            case R.id.search_bt:
                String searchString = search_edittext.getText().toString();
                if (!TextUtils.isEmpty(searchString)) {
                    addSearchRecord(searchString);
                    handler.sendEmptyMessage(ADD_RECORD);
                    Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                    intent.putExtra("SearchKey", searchString);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "搜索关键字不能为空！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.search_key_delete_bt:
                myDialog = new MyDialog(SearchActivity.this, "确定要清除历史记录吗？", R.style.Mydialog, new MyDialogListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO 删除历史记录
                        switch (view.getId()) {
                            case R.id.dialog_button1:
                                DatasUtil.clearSearchKeyWord(SearchActivity.this);
                                list.clear();
                                handler.sendEmptyMessage(DELETE_RECORD);
                                myDialog.dismiss();
                                break;
                            case R.id.dialog_button2:
                                myDialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                });
                myDialog.show();
                break;
            default:
                break;
        }
    }

    /**
     * 获取热搜词
     */
    private void getHotwords() {
        String url = ServicePort.ARCHIVE_HOTWORDS;
        ApiClientHttp api = new ApiClientHttp();
        api.Post(url, null, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                String responce = o.toString();
                LogUtils.showLogD("----->热词返回数据----->" + responce);
                if (!TextUtils.isEmpty(responce)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responce);
                        int err_no = jsonObject.getInt("err_no");
                        if (err_no == 0) {
                            JSONObject results = jsonObject.getJSONObject("results");
                            if (!TextUtils.isEmpty(results.toString())) {
                                JSONArray hotList = results.getJSONArray("list");
                                if (hotList != null && hotList.length() > 0) {
                                    for (int i = 0; i < hotList.length(); i++) {
                                        keyList.add(hotList.get(i).toString());
                                    }
                                    sKeyAdapter.notifyDataSetChanged();
                                }
                            }
                        } else {
                            LogUtils.showCenterToast(SearchActivity.this, jsonObject.getInt("err_no") + "" + jsonObject.getString("err_msg"));
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
     * 添加记录
     */
    private void addSearchRecord(String str) {
        List<String> list = DatasUtil.getSearchKeyWord(SearchActivity.this);
        if (list.contains(str)) {
            return;
        } else {
            if (list.size() > 15) {
                list.remove(0);
                StringBuffer buffer = new StringBuffer();
                for (int i = 0; i < list.size(); i++) {
                    buffer.append(list.get(i) + ",");
                }
                buffer.append(str);
                String newRecord = buffer.toString();
                DatasUtil.storeSearchKeyWord(SearchActivity.this, newRecord);
            } else {
                if (list.size() > 0) {
                    StringBuffer buffer = new StringBuffer();
                    for (int i = 0; i < list.size(); i++) {
                        buffer.append(list.get(i) + ",");
                    }
                    buffer.append(str);
                    String newRecord = buffer.toString();
                    DatasUtil.storeSearchKeyWord(SearchActivity.this, newRecord);
                } else {
                    DatasUtil.storeSearchKeyWord(SearchActivity.this, str);
                }
            }
        }
    }

    /**
     * 控制删除按钮显示关闭
     */
    private void controlClearLayout() {
        if (list.size() > 0) {
            search_key_delete_layout.setVisibility(View.VISIBLE);
        } else {
            search_key_delete_layout.setVisibility(View.GONE);
        }
    }

    /**
     * 获取搜索记录
     */
    private void getSearchRecord(boolean isFresh) {
        List<String> listTemp = DatasUtil.getSearchKeyWord(SearchActivity.this);
        if (isFresh) {
            if (listTemp == null || (listTemp != null && listTemp.size() <= 0)) {
                if (list != null) {
                }
            } else {
                list.clear();
                swAdapter.notifyDataSetChanged();
            }
        }
        if (listTemp == null || (listTemp != null && listTemp.size() <= 0)) {
            if (isFresh) {
                if (list != null) {
                }
            }
        } else {
            list.addAll(listTemp);
        }
        controlClearLayout();
    }

    /**
     * 编辑框监听
     */
    //字数控制
    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                search_clear_bt.setVisibility(View.VISIBLE);
            } else {
                search_clear_bt.setVisibility(View.GONE);
            }
        }
    };
}
