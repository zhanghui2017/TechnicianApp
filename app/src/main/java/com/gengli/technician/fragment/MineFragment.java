package com.gengli.technician.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gengli.technician.R;
import com.gengli.technician.activity.ChangePhoneActivity;
import com.gengli.technician.activity.ChangePwdActivity;
import com.gengli.technician.activity.MessageActivity;
import com.gengli.technician.activity.OrderBeginActivity;
import com.gengli.technician.activity.RecordActivity;
import com.gengli.technician.http.ApiClientHttp;
import com.gengli.technician.http.ServicePort;
import com.gengli.technician.util.CommandPhotoUtil;
import com.gengli.technician.util.DatasUtil;
import com.gengli.technician.util.FileUtils;
import com.gengli.technician.util.LogUtils;
import com.gengli.technician.util.PhotoBitmapUtil;
import com.gengli.technician.util.PhotoSystemOrShoot;
import com.gengli.technician.view.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class MineFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private RelativeLayout bt_mine_message;
    private RelativeLayout bt_mine_record;
    private RelativeLayout bt_mine_change_phone;
    private RelativeLayout bt_mine_change_pwd;
    private RelativeLayout bt_mine_clear;
    private TextView text_mine_phone;
    private RoundImageView mine_fragemnt_change_header;
    private ImageView mine_fragemnt_change_tip;
    private PopupWindow headerPopupWindow;
    private Button take_photos_bt, getpic_from_sd, popup_cancle_bt;
    private LinearLayout popup_dimiss;
    private static final String PHOTO_FILE_NAME = "filter_qrCode_img.png";
    private File tempFile;
    private static final int PHOTO_REQUEST_CAREMA = 1;
    private static final int PHOTO_REQUEST_GALLERY = 2;
    private static final int PHOTO_REQUEST_CUT = 3;
    private TextView mine_fragemnt_username;
    private TextView mine_fragemnt_jobnumber;
    private Bitmap bitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        String headUrl = DatasUtil.getUserInfo(getActivity(), "avatar");
        if (!TextUtils.isEmpty(headUrl)) {
            String str = headUrl.substring(headUrl.length() - 3, headUrl.length());
            if (str.equals("jpg")) {
                ImageLoader.getInstance().displayImage(headUrl, mine_fragemnt_change_header);
                mine_fragemnt_change_tip.setImageResource(R.drawable.icon_authentication_pre);
            }
        }
        String mobile = DatasUtil.getUserInfo(getActivity(), "mobile");
        String realname = DatasUtil.getUserInfo(getActivity(), "realname");
        String company_id = DatasUtil.getUserInfo(getActivity(), "company_id");

        mine_fragemnt_jobnumber.setText("工号：" + company_id);
        if (TextUtils.isEmpty(realname)) {
            mine_fragemnt_username.setText("姓名");
        } else {
            mine_fragemnt_username.setText(realname);
        }
        if (TextUtils.isEmpty(mobile)) {
            text_mine_phone.setText("");
        } else {
            text_mine_phone.setText("(" + mobile + ")");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mine_fragemnt_username = (TextView) view.findViewById(R.id.mine_fragemnt_username);
        mine_fragemnt_jobnumber = (TextView) view.findViewById(R.id.mine_fragemnt_jobnumber);
        mine_fragemnt_change_header = (RoundImageView) view.findViewById(R.id.mine_fragemnt_change_header);
        mine_fragemnt_change_header.setOnClickListener(this);
        mine_fragemnt_change_tip = (ImageView) view.findViewById(R.id.mine_fragemnt_change_tip);
        bt_mine_message = (RelativeLayout) view.findViewById(R.id.bt_mine_message);
        bt_mine_record = (RelativeLayout) view.findViewById(R.id.bt_mine_record);
        bt_mine_change_phone = (RelativeLayout) view.findViewById(R.id.bt_mine_change_phone);
        bt_mine_change_pwd = (RelativeLayout) view.findViewById(R.id.bt_mine_change_pwd);
        bt_mine_clear = (RelativeLayout) view.findViewById(R.id.bt_mine_clear);
        text_mine_phone = (TextView) view.findViewById(R.id.text_mine_phone);

        bt_mine_message.setOnClickListener(this);
        bt_mine_record.setOnClickListener(this);
        bt_mine_change_phone.setOnClickListener(this);
        bt_mine_change_pwd.setOnClickListener(this);
        bt_mine_clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_fragemnt_change_header:
                getHeadPopup();
                headerPopupWindow.setAnimationStyle(R.style.popup_photo_view_style);
                headerPopupWindow.showAtLocation(mine_fragemnt_change_header, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.bt_mine_message:
                LogUtils.showCenterToast(getActivity(), "消息功能暂未开放");
//                startActivity(new Intent(getActivity(), MessageActivity.class));
                break;
            case R.id.bt_mine_record:
                startActivity(new Intent(getActivity(), RecordActivity.class));
                break;
            case R.id.bt_mine_change_phone:
                startActivity(new Intent(getActivity(), ChangePhoneActivity.class));
                break;
            case R.id.bt_mine_change_pwd:
                startActivity(new Intent(getActivity(), ChangePwdActivity.class));
                break;
            case R.id.bt_mine_clear:
                ImageLoader.getInstance().clearDiskCache();
                ImageLoader.getInstance().clearMemoryCache();
                PhotoBitmapUtil.DelFilePhoto(getActivity());
                LogUtils.showCenterToast(getActivity(), "缓存清除成功");
                break;
            case R.id.take_photos_bt:
                takePhotos();
                headerPopupWindow.dismiss();
                break;
            case R.id.getpic_from_sd:
                getFromCd();
                headerPopupWindow.dismiss();
                break;
            case R.id.popup_cancle_bt:
                headerPopupWindow.dismiss();
                break;
            case R.id.popup_dimiss:
                headerPopupWindow.dismiss();
                break;
        }

    }

    private void getHeadPopup() {
        if (headerPopupWindow != null) {
            headerPopupWindow.dismiss();
            return;
        } else {
            HeanderPopupWindow();
        }
    }


    public void HeanderPopupWindow() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View headPopup = layoutInflater.inflate(R.layout.popup_photo_files, null);

        take_photos_bt = (Button) headPopup.findViewById(R.id.take_photos_bt);
        getpic_from_sd = (Button) headPopup.findViewById(R.id.getpic_from_sd);
        popup_cancle_bt = (Button) headPopup.findViewById(R.id.popup_cancle_bt);
        popup_dimiss = (LinearLayout) headPopup.findViewById(R.id.popup_dimiss);

        take_photos_bt.setOnClickListener(this);
        getpic_from_sd.setOnClickListener(this);
        popup_cancle_bt.setOnClickListener(this);
        popup_dimiss.setOnClickListener(this);

        headerPopupWindow = new PopupWindow(headPopup, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        headerPopupWindow.setFocusable(true);
        headerPopupWindow.setBackgroundDrawable(new BitmapDrawable());
    }

    /**
     * 拍照
     */
    private void takePhotos() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME);
            Uri uri = Uri.fromFile(tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
        } else {
            Toast.makeText(getActivity(), "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 文件获取
     */
    private void getFromCd() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    /**
     * 回调
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST_GALLERY) {        //本地获取图片
            if (data != null) {
                Uri uri = data.getData();
                crop(uri);
            }

        } else if (requestCode == PHOTO_REQUEST_CAREMA) {//拍照
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                crop(Uri.fromFile(tempFile));
            } else {
                Toast.makeText(getActivity(), "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PHOTO_REQUEST_CUT) {    //剪切图片
            if (data != null) {
                bitmap = data.getParcelableExtra("data");
                uploadHeader();
//                mine_fragemnt_change_header.setImageBitmap(bitmap);
//                upload(PictureManageUtil.resizeBitmap(bitmap,160,160),headUrl());
            }
            try {
                if (tempFile != null && tempFile.exists())
                    tempFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void crop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    private void uploadHeader() {
        String url = ServicePort.ACCOUNT_MODIFY;
        ApiClientHttp api = new ApiClientHttp();
        Map<String, String> map = new HashMap<>();

        AjaxParams params = api.getParam(getActivity(), map);
        try {
            params.put("avatar", FileUtils.BitmapToFile(getActivity(), bitmap));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        api.Post(url, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                String responce = o.toString();
                LogUtils.showLogD("----->上传头像返回数据----->" + responce);
                if (!TextUtils.isEmpty(responce)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responce);
                        int err_no = jsonObject.getInt("err_no");
                        if (err_no == 0) {
                            JSONObject results = jsonObject.getJSONObject("results");
                            if (!TextUtils.isEmpty(results.toString())) {
                                handle.sendEmptyMessage(1);
                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("User", getActivity().MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("avatar", results.getString("avatar"));
//                                editor.putBoolean("is_avatar", true);
                                editor.commit();
                            }

                        } else {
                            LogUtils.showCenterToast(getActivity(), jsonObject.getInt("err_no") + "" + jsonObject.getString("err_msg"));
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String headUrl = DatasUtil.getUserInfo(getActivity(), "avatar");
                ImageLoader.getInstance().displayImage(headUrl, mine_fragemnt_change_header);
                mine_fragemnt_change_tip.setImageResource(R.drawable.icon_authentication_pre);
            }
        }
    };
}
