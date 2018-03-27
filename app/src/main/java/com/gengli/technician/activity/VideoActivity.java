package com.gengli.technician.activity;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.gengli.technician.R;

public class VideoActivity extends Activity {

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        videoView = (VideoView) findViewById(R.id.video_play_view);
        String url = getIntent().getStringExtra("video_url");

        Uri uri = Uri.parse(url);
        videoView.setMediaController(new MediaController(this));
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
    }
}
