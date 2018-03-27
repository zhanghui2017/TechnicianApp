package com.gengli.technician.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.gengli.technician.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PictureActivity extends Activity {

    private ImageView pic_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        String url = getIntent().getStringExtra("img_url");
        pic_view = (ImageView) findViewById(R.id.pic_view);
        ImageLoader.getInstance().displayImage(url,pic_view);
    }
}
