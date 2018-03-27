package com.gengli.technician.fragment;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import com.gengli.technician.R;
import com.gengli.technician.activity.FavourActivity;
import com.gengli.technician.activity.GetRouting2Activity;
import com.gengli.technician.activity.GetRoutingActivity;
import com.gengli.technician.activity.OrderBeginActivity;
import com.gengli.technician.activity.RepairsActivity;
import com.gengli.technician.activity.StockActivity;
import com.gengli.technician.adapter.MyAdapter;
import com.gengli.technician.adapter.SubAdapter;
import com.gengli.technician.bean.City;
import com.gengli.technician.bean.Notice;
import com.gengli.technician.bean.Order;
import com.gengli.technician.bean.Province;
import com.gengli.technician.http.ApiClientHttp;
import com.gengli.technician.http.ServicePort;
import com.gengli.technician.util.DatasUtil;
import com.gengli.technician.util.GetProvinceData;
import com.gengli.technician.util.ImageUtil;
import com.gengli.technician.util.LogUtils;
import com.gengli.technician.util.SystemMsgUtil;
import com.gengli.technician.view.ImageSlideView;
import com.gengli.technician.view.MyListView;
import com.gengli.technician.view.OrderView;
import com.gengli.technician.view.TextSlideView;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private int CLIENT_RUN_BANNERS = 1001;
    private int CLIENT_RUN_NOTICES = 1003;
    private int CLIENT_RUN_NOTICES_FAIL = 1004;
    private int GET_ORDER_DATA = 1002;

    //    private SliderLayout sliderShow;
//    private PagerIndicator indicator;
    private ImageSlideView imageSlideView;
    private TextSlideView textSlideView;
    private TextView location_text;
    private ImageView kucunBt;
    private ImageView shoucangBt;
    private ImageView jiluBt;
    private ImageView xingchengBt;
    private boolean flag;
    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;

    private MyListView listView;
    private MyListView subListView;
    private TextView area_dimiss_bt;
    private TextView area_choose_bt;

    private MyAdapter myAdapter;
    private String area_url = "area2.json";
    private List<City> cities;
    private SubAdapter subAdapter;
    private PopupWindow popupWindow;
    private ImageView tip_jiedan_img;
    private TextView main_jiedan_bt;
    private ImageView tip_weixiu_img;
    private TextView main_weixiu_bt;
    private OrderView missionView;
    private Order orderWaiting;
    private Order orderDoing;
    private Order weixiuOrder;
    private String chooseCity;
    private List<String> imageUrls;
    private List<Notice> noticeList;
    private View view;
    private LinearLayout img_main_fragment_jiedan_bt;
    private ImageView main_fragment_message_bt;
    private RelativeLayout main_fragment_message_view;
    private TextView main_fragment_message_count;
    private int messageCount = 0;
    private LinearLayout order_no_data;

    private int status = 2;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.arg1 == CLIENT_RUN_BANNERS) {
                imageSlideView = (ImageSlideView) view.findViewById(R.id.img_slider_view);
//                sliderShow = (SliderLayout) view.findViewById(R.id.img_slider_view);
//                indicator = (PagerIndicator) view.findViewById(R.id.custom_indicator);
                for (int i = 0; i < imageUrls.size(); i++) {
                    imageSlideView.addImageUrl(imageUrls.get(i));
//                    sliderShow.addSlider(new TextSliderView(getActivity()).image(imageUrls.get(i)));
                }
//
//                sliderShow.setCustomIndicator(indicator);
//                sliderShow.setPresetTransformer(SliderLayout.Transformer.Default);
//                sliderShow.setDuration(3000);
                imageSlideView.setDelay(2000);
                imageSlideView.commit();

//                if (messageCount > 0) {
//                    main_fragment_message_view.setVisibility(View.VISIBLE);
//                    main_fragment_message_count.setText(messageCount + "");
//                } else {
//                    main_fragment_message_view.setVisibility(View.INVISIBLE);
//                }
            } else if (msg.what == CLIENT_RUN_NOTICES) {
                textSlideView = (TextSlideView) view.findViewById(R.id.text_slider_view);
                for (int j = 0; j < noticeList.size(); j++) {
                    LogUtils.showLogD("noticeList.get(j).getTitle():" + noticeList.get(j).getTitle());
                    textSlideView.addTextTitle(noticeList.get(j).getTitle());
                }
                textSlideView.setOnItemClickListener(new TextSlideView.OnItemClickListener() {
                    @Override
                    public void onTextItemClick(View view, int position) {
//                        LogUtils.showCenterToast(getActivity(), noticeList.get(position).getContent());
                        final AlertDialog.Builder noticeDialog = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                        String content = noticeList.get(position).getContent();
                        noticeDialog.setTitle("公告");
                        noticeDialog.setMessage(content);
                        noticeDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        noticeDialog.create().show();
                    }
                });
                textSlideView.setDelay(2000);
                textSlideView.commit();
            } else if (msg.what == 1100) {
                missionView.setVisibility(View.VISIBLE);
                order_no_data.setVisibility(View.INVISIBLE);
                missionView.setData(orderWaiting);
            } else if (msg.what == 1101) {
                img_main_fragment_jiedan_bt.setVisibility(View.INVISIBLE);
                missionView.setVisibility(View.INVISIBLE);
                order_no_data.setVisibility(View.VISIBLE);
            }
        }
    };

    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    location_text.setText(aMapLocation.getCity());
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("test", "location "
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            } else {
                Log.e("test", "aMapLocation is null");
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        this.view = view;
        init(view);
        runClient();
        if (DatasUtil.isTechLogin(getActivity())) {
            getWittingOrderData(1);
        } else {
            img_main_fragment_jiedan_bt.setVisibility(View.INVISIBLE);
            missionView.setVisibility(View.INVISIBLE);
            order_no_data.setVisibility(View.VISIBLE);
        }
        initLocation();
        return view;
    }

    public void init(View view) {
        order_no_data = (LinearLayout) view.findViewById(R.id.order_no_data);
        main_fragment_message_bt = (ImageView) view.findViewById(R.id.main_fragment_message_bt);
        main_fragment_message_bt.setOnClickListener(this);
        img_main_fragment_jiedan_bt = (LinearLayout) view.findViewById(R.id.img_main_fragment_jiedan_bt);
        location_text = (TextView) view.findViewById(R.id.main_fragment_location_text);
        kucunBt = (ImageView) view.findViewById(R.id.main_kucun_bt);
        kucunBt.setOnClickListener(this);
        shoucangBt = (ImageView) view.findViewById(R.id.main_shoucang_bt);
        shoucangBt.setOnClickListener(this);
        jiluBt = (ImageView) view.findViewById(R.id.main_jilu_bt);
        jiluBt.setOnClickListener(this);
        xingchengBt = (ImageView) view.findViewById(R.id.main_xingcheng_bt);
        xingchengBt.setOnClickListener(this);
        location_text.setOnClickListener(this);
        img_main_fragment_jiedan_bt.setOnClickListener(this);

        main_jiedan_bt = (TextView) view.findViewById(R.id.main_jiedan_bt);
        main_weixiu_bt = (TextView) view.findViewById(R.id.main_weixiu_bt);
        main_jiedan_bt.setOnClickListener(this);
        main_weixiu_bt.setOnClickListener(this);
        tip_jiedan_img = (ImageView) view.findViewById(R.id.tip_jiedan_img);
        tip_weixiu_img = (ImageView) view.findViewById(R.id.tip_weixiu_img);
        missionView = (OrderView) view.findViewById(R.id.main_mission_view);
        main_fragment_message_view = (RelativeLayout) view.findViewById(R.id.main_fragment_message_view);
        main_fragment_message_count = (TextView) view.findViewById(R.id.main_fragment_message_count);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_fragment_location_text:
                getPopupInstance();
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
                break;
            case R.id.main_kucun_bt:
                if (DatasUtil.isTechLogin(getActivity())) {
                    startActivity(new Intent(getActivity(), StockActivity.class));
                } else {
                    LogUtils.showCenterToast(getActivity(), "功能未开放");
                }
                break;
            case R.id.main_shoucang_bt:
                startActivity(new Intent(getActivity(), FavourActivity.class));
                break;
            case R.id.main_jilu_bt:
                if (DatasUtil.isTechLogin(getActivity())) {
                    startActivity(new Intent(getActivity(), RepairsActivity.class));
                } else {
                    LogUtils.showCenterToast(getActivity(), "功能未开放");
                }
                break;
            case R.id.main_xingcheng_bt:
                if (DatasUtil.isLogin(getActivity())) {
                    String companyId = DatasUtil.getUserInfo(getActivity(), "company_id");
                    if (TextUtils.isEmpty(companyId)) {
                        return;
                    } else {
                        String str = companyId.substring(0, 1);
                        if (str.equals("8")) {
                            startActivity(new Intent(getActivity(), GetRouting2Activity.class));
                        } else {
                            startActivity(new Intent(getActivity(), GetRoutingActivity.class));
                        }
                    }
                }
                break;
            case R.id.main_jiedan_bt:
                if (DatasUtil.isTechLogin(getActivity())) {
                    status = 2;
                    img_main_fragment_jiedan_bt.setVisibility(View.VISIBLE);
                    tip_jiedan_img.setVisibility(View.VISIBLE);
                    tip_weixiu_img.setVisibility(View.INVISIBLE);
                    getWittingOrderData(1);
                } else {
                    img_main_fragment_jiedan_bt.setVisibility(View.INVISIBLE);
                    missionView.setVisibility(View.INVISIBLE);
                    order_no_data.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.main_weixiu_bt:
                if (DatasUtil.isTechLogin(getActivity())) {
                    status = 3;
                    img_main_fragment_jiedan_bt.setVisibility(View.INVISIBLE);
                    tip_jiedan_img.setVisibility(View.INVISIBLE);
                    tip_weixiu_img.setVisibility(View.VISIBLE);
                    getWittingOrderData(1);
                } else {
                    img_main_fragment_jiedan_bt.setVisibility(View.INVISIBLE);
                    missionView.setVisibility(View.INVISIBLE);
                    order_no_data.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.area_dimiss_bt:
                popupWindow.dismiss();
                break;
            case R.id.area_choose_bt:
                location_text.setText(chooseCity);
                popupWindow.dismiss();
                break;
            case R.id.img_main_fragment_jiedan_bt:
                Intent intent = new Intent(getActivity(), OrderBeginActivity.class);
                intent.putExtra("BEGIN_ORDER", orderWaiting);
                startActivity(intent);
                break;
            case R.id.main_fragment_message_bt:
//                startActivity(new Intent(getActivity(), GetRouting2Activity.class));
                LogUtils.showCenterToast(getActivity(), "消息功能暂未开放");
//                showTimePickerDialog();
//                startActivity(new Intent(getActivity(), MessageActivity.class));
                break;
            default:
                break;
        }
    }

    private void getPopupInstance() {
        if (popupWindow != null) {
            popupWindow.dismiss();
            return;
        } else {
            initPopupWindow();
        }
    }


    private void initPopupWindow() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View popupView = layoutInflater.inflate(R.layout.popup_area_choice, null);

        listView = (MyListView) popupView.findViewById(R.id.popup_area_listView);
        subListView = (MyListView) popupView.findViewById(R.id.popup_subListView);

        area_dimiss_bt = (TextView) popupView.findViewById(R.id.area_dimiss_bt);
        area_choose_bt = (TextView) popupView.findViewById(R.id.area_choose_bt);
        area_dimiss_bt.setOnClickListener(this);
        area_choose_bt.setOnClickListener(this);

        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        try {
            myAdapter = new MyAdapter(getActivity(), GetProvinceData.getProvincesList(
                    GetProvinceData.getData(GetProvinceData.getJsonObject(area_url, getActivity()))));
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
                List<Province> pro = GetProvinceData.getProvincesList(GetProvinceData.getData(GetProvinceData.getJsonObject(area_url, getActivity())));

            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            try {
                cities = GetProvinceData.getCityList(GetProvinceData.getData(GetProvinceData.getJsonObject(area_url, getActivity())), position);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            subAdapter = new SubAdapter(getActivity(), cities);
            subListView.setAdapter(subAdapter);
            subListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                    subAdapter.setSelectedPosition(position);
                    subAdapter.notifyDataSetChanged();
                    if (cities.get(position).getCity().equals("全部") || cities.get(position).getCity().equals("区") || cities.get(position).getCity().equals("县")) {
                        //获取省份
                        String province_name = "";
                        try {
                            List<Province> provinces = GetProvinceData.getProvincesList(GetProvinceData.getData(GetProvinceData.getJsonObject(area_url, getActivity())));
                            province_name = provinces.get(location).getProvince();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                        Log.i("PersonInfo", "城市---id：" + cities.get(position));
                        chooseCity = province_name;
                    } else {
//                        Log.i("PersonInfo", "城市   id：" + cities.get(position));
                        chooseCity = cities.get(position).getCity().toString();

                    }
//                    Log.i("PersonInfo", "城市id：" + cities.get(position));
//                        setInfoToCash("City", cities.get(position).getCity().toString());
//                        setInfoToCash("City_Id", cities.get(position).getCity_code().toString());
//                        Log.i("PersonInfo", "城市信息已存入缓存中"+cities.get(position).getCity().toString()+"--id--"+
//                                cities.get(position).getCity_code().toString());
//                    closePopupArea();
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        textSlideView.releaseResource();
        imageSlideView.releaseResource();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void initLocation() {
        mLocationClient = new AMapLocationClient(getActivity());
        mLocationClient.setApiKey("690bd4df16cf5b81f297a518d696e05e");
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(true);
        mLocationOption.setOnceLocationLatest(true);
        mLocationOption.setHttpTimeOut(20000);
        mLocationOption.setLocationCacheEnable(false);
        if (null != mLocationClient) {
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        } else {
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.startLocation();
        }
    }


    public void runClient() {
        String deviceid = SystemMsgUtil.getSingleKey(getActivity());
        String screen = ImageUtil.getScreenWidth(getActivity()) + "*" + ImageUtil.getScreenHeight(getActivity());
        String brand = android.os.Build.MODEL;
        String pushtoken = "123";
        String url = ServicePort.GET_CLIENT_RUN;
        ApiClientHttp api = new ApiClientHttp();
        Map<String, String> map = new HashMap<>();
        map.put("deviceid", deviceid);
        map.put("pushtoken", pushtoken);
        map.put("brand", brand);
        map.put("screen", screen);
        AjaxParams params = api.getParam(getActivity(), map);
        params.put("deviceid", deviceid);
        params.put("pushtoken", pushtoken);
        params.put("brand", brand);
        params.put("screen", screen);
        api.Post(url, params, new AjaxCallBack<Object>() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                Log.e("MainFragment", "" + strMsg);
            }

            @Override
            public void onSuccess(Object o) {
                LogUtils.showLogD("--------客户端启动返回数据:" + o.toString());
                String responce = o.toString();
                if (!TextUtils.isEmpty(responce)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responce);
                        int err_no = jsonObject.getInt("err_no");
                        if (err_no == 0) {
                            JSONObject results = jsonObject.getJSONObject("results");
                            messageCount = results.getInt("num_msg");
                            final JSONArray banners = results.getJSONArray("banners");
                            final JSONArray notices = results.getJSONArray("notices");


                            new Thread() {
                                public void run() {
                                    Message msg = new Message();
                                    if (banners != null && banners.length() > 0) {
                                        imageUrls = new ArrayList<>();
                                        JSONObject object = new JSONObject();
                                        for (int i = 0; i < banners.length(); i++) {
                                            try {
                                                object = banners.getJSONObject(i);
                                                imageUrls.add(object.getString("thumb"));
                                                LogUtils.showLogD("banner urls : " + imageUrls.get(i));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        Log.d("MainFragment", "--------banners:" + banners);

                                        msg.arg1 = CLIENT_RUN_BANNERS;
//                                        msg.obj = banners;
                                        handler.sendMessage(msg);
                                    }
                                    if (notices != null && notices.length() > 0) {
                                        noticeList = new ArrayList<Notice>();
                                        JSONObject object = new JSONObject();
                                        for (int i = 0; i < notices.length(); i++) {
                                            try {
                                                object = notices.getJSONObject(i);
                                                if (notices.length() > 0 && notices != null) {
                                                    Notice notice = new Notice();
                                                    notice.setTitle(object.getString("title"));
                                                    notice.setContent(object.getString("content"));
                                                    noticeList.add(notice);
                                                }
                                                LogUtils.showLogD("notice contents : ");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        if (noticeList.size() > 0) {
                                            Log.d("MainFragment", "--------notices:" + notices.toString());
                                            handler.sendEmptyMessage(CLIENT_RUN_NOTICES);
                                        } else
                                            handler.sendEmptyMessage(CLIENT_RUN_NOTICES_FAIL);
                                    }

                                }
                            }.start();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    private void getWittingOrderData(final int page) {
        String url = ServicePort.REPAIR_LISTS;
        ApiClientHttp api = new ApiClientHttp();
        Map<String, String> map = new HashMap<>();
        map.put("status", status + "");
        map.put("page", page + "");
        AjaxParams params = api.getParam(getActivity(), map);
        params.put("status", status + "");
        params.put("page", page + "");
        api.Post(url, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                String responce = o.toString();
//                LogUtils.showLogI("-----订单列表返回数据----->" + responce);
                if (!TextUtils.isEmpty(responce)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responce);
                        int err_no = jsonObject.getInt("err_no");
                        if (err_no == 0) {
                            JSONObject results = jsonObject.getJSONObject("results");
                            if (!TextUtils.isEmpty(results.toString())) {
                                JSONArray lists = results.getJSONArray("lists");
                                LogUtils.showLogI("-----订单列表返回数据----->" + lists.toString());
                                if (lists.length() > 0) {
                                    JSONObject item = lists.getJSONObject(0);
                                    orderWaiting = new Order();
                                    orderWaiting.setId(item.getString("rid"));
                                    orderWaiting.setMachine(item.getString("product_name"));
                                    orderWaiting.setName(item.getString("realname"));
                                    orderWaiting.setPhone(item.getString("tel"));
                                    orderWaiting.setCompany(item.getString("unit"));
                                    orderWaiting.setAddress(item.getString("address"));
                                    orderWaiting.setTrouble(item.getString("des"));
                                    handler.sendEmptyMessage(1100);
                                } else {
                                    handler.sendEmptyMessage(1101);
                                }
                            }
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
