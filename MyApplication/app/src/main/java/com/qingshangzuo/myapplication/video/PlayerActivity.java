package com.qingshangzuo.myapplication.video;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import com.qingshangzuo.myapplication.R;



public class PlayerActivity extends AppCompatActivity {

    private Button play;
    private VideoView video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        video = ((VideoView) findViewById(R.id.video));
        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        //Uri uri = Uri.parse(path);//第一种。把路径转换为uri，然后给videoview设置，
//        video.setVideoURI(uri);
        video.setVideoPath(path);//第二种，直接把本地视频的路径设置给VideoView也可以.
        play = ((Button) findViewById(R.id.btn_play));
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video.start();
            }
        });
    }
}