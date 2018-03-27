package com.gengli.technician.activity;

import android.app.Application;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;

import cn.jpush.android.api.JPushInterface;

import com.gengli.technician.broadcast.NetCheckReceiver;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * @ClassName: MyApplication
 * @Description: TODO(这里用一句话描述这个类的作用)
 */
public class GLApplication extends Application {

    private NetCheckReceiver mReceiver;
//    public final static String DEFAULT_SAVE_IMAGE_PATH = FileUtils.getSDCardPath() + "gengli" + File.separator + "Images" + File.separator;

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader();
        //极光推送
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(getApplicationContext());     		// 初始化 JPush


        //注册广播接受者用于监听网络状态
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mReceiver = new NetCheckReceiver();
        registerReceiver(mReceiver, mFilter);
    }

    /**
     * 初始化imageLoader
     */
    private void initImageLoader() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)             //设置图片的解码类型
                .build();

//        File cacheDir = new File(DEFAULT_SAVE_IMAGE_PATH);
        ImageLoaderConfiguration imageconfig = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(480, 800)
                .diskCacheExtraOptions(480, 800, null)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(200)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .defaultDisplayImageOptions(options)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();

        ImageLoader.getInstance().init(imageconfig);
    }
}
