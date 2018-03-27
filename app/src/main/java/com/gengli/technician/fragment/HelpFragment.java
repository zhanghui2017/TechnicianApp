package com.gengli.technician.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gengli.technician.R;
import com.gengli.technician.activity.ArticleActivity;
import com.gengli.technician.activity.FavourActivity;
import com.gengli.technician.activity.SearchActivity;
import com.gengli.technician.adapter.HelpAdapter;
import com.gengli.technician.adapter.PopProductAdapter;
import com.gengli.technician.bean.Article;
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

public class HelpFragment extends Fragment implements View.OnClickListener {
    private String[] type = {"全部", "操作技能", "保养指南", "常见故障"};
    private String[] productType = {"全部", "湿喷台车系列", "湿喷机系列", "干喷机系列", "联合上料机", "注浆机系列", "凿岩机系列", "型钢冷弯机", "钢筋机械"};
    private PullToRefreshListView help_list_view;
    private HelpAdapter adapter;
    private List<Article> articalList;
    private LinearLayout view_help_no_data;
    private LinearLayout view_help_product;
    private LinearLayout view_help_classify;
    private TextView text_help_classify;
    private TextView text_help_product;
    private ImageView img_help_search;

    private PopupWindow productPop;
    private ListView pop_help_product_list;
    private TextView product_dimiss_bt;
    private TextView product_choose_bt;
    private PopProductAdapter proAdapter;


    private PopupWindow classifyPop;
    private TextView classify_help_dimiss_bt;
    private TextView classify_help_choose_bt;
    private TextView pop_help_classify_1;
    private TextView pop_help_classify_2;
    private TextView pop_help_classify_3;
    private TextView pop_help_classify_4;
    private String chooseclassify;
    private String chooseProduct;

    private int category_id = 0;
    private String keyword = "";
    private int page = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        initView(view);
        getArticles(1, category_id, keyword);
        return view;
    }

    private void initView(View view) {
        articalList = new ArrayList<>();
        help_list_view = (PullToRefreshListView) view.findViewById(R.id.help_list_view);
        adapter = new HelpAdapter(articalList);
        help_list_view.setAdapter(adapter);
        view_help_no_data = (LinearLayout) view.findViewById(R.id.view_help_no_data);
        view_help_product = (LinearLayout) view.findViewById(R.id.view_help_product);
        view_help_classify = (LinearLayout) view.findViewById(R.id.view_help_classify);
        text_help_product = (TextView) view.findViewById(R.id.text_help_product);
        text_help_classify = (TextView) view.findViewById(R.id.text_help_classify);
        view_help_classify.setOnClickListener(this);
        view_help_product.setOnClickListener(this);
        img_help_search = (ImageView) view.findViewById(R.id.img_help_search);
        img_help_search.setOnClickListener(this);
        help_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int articleid = articalList.get(position - 1).getId();
                Intent intent = new Intent(getActivity(), ArticleActivity.class);
                intent.putExtra("articleid", articleid);
                startActivity(intent);
            }
        });
        help_list_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getArticles(1, category_id, keyword);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                LogUtils.showLogD("--->favourlist page = " + (page++));
                getArticles(page++, category_id, keyword);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_help_product:
                getProductPop();
                productPop.showAtLocation(v, Gravity.CENTER, 0, 0);
                break;
            case R.id.view_help_classify:
                getClassifyPop();
                classifyPop.showAtLocation(v, Gravity.CENTER, 0, 0);
                break;
            case R.id.classify_help_dimiss_bt:
                classifyPop.dismiss();
                break;
            case R.id.classify_help_choose_bt:
                getArticles(1, category_id, keyword);
                text_help_classify.setText(chooseclassify);
                classifyPop.dismiss();
                break;
            case R.id.pop_help_classify_1:
                chooseclassify = type[0];
                category_id = 0;
                pop_help_classify_1.setBackgroundColor(Color.LTGRAY);
                pop_help_classify_2.setBackgroundColor(Color.WHITE);
                pop_help_classify_3.setBackgroundColor(Color.WHITE);
                pop_help_classify_4.setBackgroundColor(Color.WHITE);
                break;
            case R.id.pop_help_classify_2:
                chooseclassify = type[1];
                category_id = 6;
                pop_help_classify_1.setBackgroundColor(Color.WHITE);
                pop_help_classify_2.setBackgroundColor(Color.LTGRAY);
                pop_help_classify_3.setBackgroundColor(Color.WHITE);
                pop_help_classify_4.setBackgroundColor(Color.WHITE);
                break;
            case R.id.pop_help_classify_3:
                chooseclassify = type[2];
                category_id = 8;
                pop_help_classify_1.setBackgroundColor(Color.WHITE);
                pop_help_classify_2.setBackgroundColor(Color.WHITE);
                pop_help_classify_3.setBackgroundColor(Color.LTGRAY);
                pop_help_classify_4.setBackgroundColor(Color.WHITE);
                break;
            case R.id.pop_help_classify_4:
                chooseclassify = type[3];
                category_id = 7;
                pop_help_classify_1.setBackgroundColor(Color.WHITE);
                pop_help_classify_2.setBackgroundColor(Color.WHITE);
                pop_help_classify_3.setBackgroundColor(Color.WHITE);
                pop_help_classify_4.setBackgroundColor(Color.LTGRAY);
                break;

            case R.id.product_dimiss_bt:
                productPop.dismiss();
                break;
            case R.id.product_choose_bt:
                getArticles(1, category_id, keyword);
                text_help_product.setText(chooseProduct);
                productPop.dismiss();
                break;
            case R.id.img_help_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
        }
    }

    private void getProductPop() {
        if (productPop != null) {
            productPop.dismiss();
            return;
        } else {
            initProductPop();
        }
    }

    private void initProductPop() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View popupView = layoutInflater.inflate(R.layout.popup_help_product, null);

        pop_help_product_list = (ListView) popupView.findViewById(R.id.pop_help_product_list);

        product_dimiss_bt = (TextView) popupView.findViewById(R.id.product_dimiss_bt);
        product_choose_bt = (TextView) popupView.findViewById(R.id.product_choose_bt);
        product_dimiss_bt.setOnClickListener(this);
        product_choose_bt.setOnClickListener(this);

        productPop = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        productPop.setFocusable(true);
        productPop.setTouchable(true);
        productPop.setBackgroundDrawable(new BitmapDrawable());


        proAdapter = new PopProductAdapter(getActivity(), productType);
        pop_help_product_list.setAdapter(proAdapter);

        pop_help_product_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                proAdapter.setSelectedPosition(position);
                proAdapter.notifyDataSetInvalidated();
                chooseProduct = productType[position];
                if (position == 0) {
                    keyword = "";
                } else {
                    keyword = productType[position];
                }

            }
        });
    }


    private void getClassifyPop() {
        if (classifyPop != null) {
            classifyPop.dismiss();
            return;
        } else {
            initClassifyPop();
        }
    }

    /**
     * 分类窗口
     */
    private void initClassifyPop() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View popupView = layoutInflater.inflate(R.layout.popup_help_classify, null);

        classify_help_dimiss_bt = (TextView) popupView.findViewById(R.id.classify_help_dimiss_bt);
        classify_help_choose_bt = (TextView) popupView.findViewById(R.id.classify_help_choose_bt);
        pop_help_classify_1 = (TextView) popupView.findViewById(R.id.pop_help_classify_1);
        pop_help_classify_2 = (TextView) popupView.findViewById(R.id.pop_help_classify_2);
        pop_help_classify_3 = (TextView) popupView.findViewById(R.id.pop_help_classify_3);
        pop_help_classify_4 = (TextView) popupView.findViewById(R.id.pop_help_classify_4);

        classify_help_dimiss_bt.setOnClickListener(this);
        classify_help_choose_bt.setOnClickListener(this);
        pop_help_classify_2.setOnClickListener(this);
        pop_help_classify_3.setOnClickListener(this);
        pop_help_classify_4.setOnClickListener(this);
        pop_help_classify_1.setOnClickListener(this);

        classifyPop = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        classifyPop.setFocusable(true);
        classifyPop.setTouchable(true);
        classifyPop.setBackgroundDrawable(new BitmapDrawable());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void getArticles(final int page, int category_id, String keyword) {
        final List<Article> listTemp = new ArrayList<>();
        String url = ServicePort.ARCHIVE_LISTS;
        ApiClientHttp api = new ApiClientHttp();
        Map<String, String> map = new HashMap<>();
        if (category_id != 0) {
            map.put("category_id", category_id + "");
        }
        if (keyword.length() > 0) {
            map.put("keyword", keyword);
        }
        map.put("page", page + "");
        map.put("size", 10 + "");
        AjaxParams params = api.getParam(getActivity(), map);
        if (category_id != 0) {
            params.put("category_id", category_id + "");
        }
        if (keyword.length() > 0) {
            params.put("keyword", keyword);
        }
        params.put("page", page + "");
        params.put("size", 10 + "");
        api.Post(url, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                String responce = o.toString();
                LogUtils.showLogD("----->指南列表返回数据----->" + responce);
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
                                        article.setInfo_id(item.getInt("category_id"));
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
                                            LogUtils.showCenterToast(getActivity(), "没有数据");
                                        } else if (listTemp.size() > 0) {
                                            articalList.addAll(listTemp);
                                            adapter.notifyDataSetChanged();
                                        }

                                    } else if (page > 1) {
                                        if (listTemp.size() == 0) {
                                            LogUtils.showCenterToast(getActivity(), "没有更多数据");
                                        } else if (listTemp.size() > 0) {
                                            articalList.addAll(listTemp);
                                            adapter.notifyDataSetChanged();
                                            help_list_view.onRefreshComplete();
                                        }
                                    }
                                    help_list_view.onRefreshComplete();
                                } else {
                                    LogUtils.showCenterToast(getActivity(), "没有数据");
                                }
                            } else {
                                LogUtils.showCenterToast(getActivity(), "没有数据");
                            }
                        } else {
                            LogUtils.showCenterToast(getActivity(), jsonObject.getInt("err_no") + "" + jsonObject.getString("err_msg"));
                        }
                        help_list_view.onRefreshComplete();
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
//    public void initData() {
//        articalList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            Article m = new Article();
//            m.setImgUrl("http://pic3.zhimg.com/b5c5fc8e9141cb785ca3b0a1d037a9a2.jpg");
//            m.setTitle("GYP90液压湿喷机的特点和工作原理" + i);
//            m.setCount(50 + i);
//            m.setTime("2018-01-0" + i);
//            articalList.add(m);
//        }
//    }
}
