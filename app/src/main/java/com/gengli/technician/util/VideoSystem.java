package com.gengli.technician.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gengli.technician.R;

import java.io.File;
import java.io.IOException;

/**
 * 拍照选择
 * <p/>
 * Created by zhuwentao on 2016-09-08.
 */
public abstract class VideoSystem {

    /**
     * 打开相册
     */
    public static final int OPEN_IMAGE = 101;

//    /**
//     * 打开相机
//     */
//    public static final int OPEN_CAMERA = 102;
//
//    /**
//     * 裁剪图片
//     */
//    public static final int CUT_PHOTO = 103;

    /**
     * 系统图片
     */
    private TextView mSystemPhotoTv;

    /**
     * 手机拍照
     */
    private TextView mShootPhotoTv;

    /**
     * 取消
     */
    private TextView mCancelPhotoTv;

    /**
     * 选择界面
     */
    private LayoutInflater inflater;

    /**
     * 泡泡窗口
     */
    private PopupWindow mPopupWindow;

    private Context mContext;

    /**
     * 对话框布局
     */
    private View mMenuView;

    private String photoPath;

    public VideoSystem(Context context) {
        this.mContext = context;
        initUI(mContext);
        initListener();
    }

    /**
     * 设置监听事件
     */
    private void initListener() {
//        mShootPhotoTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                addBitmapShoots();
//                closePopupSelect();
//            }
//        });

        mSystemPhotoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBitmapSystem();
                closePopupSelect();
            }
        });

        mCancelPhotoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closePopupSelect();
            }
        });
    }

    /**
     * 显示图片选择窗口
     */
    public void showPopupSelect(View mView) {
        mPopupWindow.showAtLocation(mView, Gravity.BOTTOM, 0, 0);
    }

    public void closePopupSelect() {
        mPopupWindow.dismiss();
    }

    private void initUI(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popup_select_way_photo, null);
        mSystemPhotoTv = (TextView) mMenuView.findViewById(R.id.tv_bitmap_system);
        mShootPhotoTv = (TextView) mMenuView.findViewById(R.id.tv_bitmap_shoot);
        mShootPhotoTv.setVisibility(View.GONE);
        mCancelPhotoTv = (TextView) mMenuView.findViewById(R.id.tv_bitmap_cancel);
        mPopupWindow = new PopupWindow(context);

        // 背景图片透明化
        ColorDrawable dw = new ColorDrawable(0x00000000);
        mPopupWindow.setBackgroundDrawable(dw);
        mPopupWindow.setContentView(mMenuView);
        mPopupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setAnimationStyle(R.style.popup_select_way);
        mPopupWindow.setFocusable(true);

    }

    /**
     * 抽象的回调方法
     *
     * @param intent
     * @param requestCode
     */
    public abstract void onStartActivityForResult(Intent intent, int requestCode);

//    /**
//     * 相机拍照
//     */
//    private void addBitmapShoots() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // 设置图片要保存的 根路径+文件名
//        photoPath = PhotoBitmapUtil.getPhotoFileName(mContext);
//        File file = new File(photoPath);
//        if (!file.exists()) {
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//        onStartActivityForResult(intent, OPEN_CAMERA);
//    }

    /**
     * 本地图片
     */
    private void addBitmapSystem() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        onStartActivityForResult(intent, OPEN_IMAGE);

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("video/*");
        onStartActivityForResult(intent, OPEN_IMAGE);
    }

    /**
     * 获取图片的地址
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public String getPhotoResultPath(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            return null;
        }

//        // 相机返回
//        if (requestCode == OPEN_CAMERA) {
//            String filepath = PhotoBitmapUtil.amendRotatePhoto(photoPath, mContext);
//            CutPhoto(Uri.fromFile(new File(filepath)));
//        } else
        if (requestCode == OPEN_IMAGE) { // 相册返回
//            CutPhoto(data.getData());
            Uri uri = data.getData();
            Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
//            String number= cursor.getString(0); // 视频编号
            String path = cursor.getString(1); // 视频文件路径
//            String size = cursor.getString(2); // 视频大小
//            String name = cursor.getString(3); // 视频文件名
//            Log.e("-----","number="+number);
//            Log.e("-----","v_size="+size);
//            Log.e("-----","v_name="+name);
            return path;

        }
//        else if (requestCode == CUT_PHOTO) {  // 裁剪图片
//            if (data != null) {
//                Bundle bundle = data.getExtras();
//                Bitmap bitmap = (Bitmap) bundle.get("data");
//                String path = PhotoBitmapUtil.savePhotoToSD(bitmap, mContext);
//                if (path != null) {
//                    return path;
//                } else {
//                    return null;
//                }
//            }
//        }

        return null;
    }

//    /**
//     * 剪切图片
//     *
//     * @param uri 地址
//     */
//    public void CutPhoto(Uri uri) {
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        intent.putExtra("crop", "true");
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("outputX", 200);
//        intent.putExtra("outputY", 200);
//        intent.putExtra("outputFormat", "JPEG");
//        intent.putExtra("noFaceDetection", true);
//        intent.putExtra("return-data", true);
//        onStartActivityForResult(intent, CUT_PHOTO);
//    }


}
