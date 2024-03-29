package com.qingshangzuo.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.qingshangzuo.myapplication.flashlight.FlashLightActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnButton1,btnButton2,btnButton3,btnButton11,btnButton22,btnButton33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 打电话
        btnButton1 = findViewById(R.id.btn_button1);
        btnButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,PhoneActivity.class);   // 意图对象：动作、数据
                startActivity(intent);  // 激活Activity组件
            }
        });

        // 进入本地图片
        btnButton2 = findViewById(R.id.btn_button2);
        btnButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,PicActivity.class);   // 意图对象：动作、数据
                startActivity(intent);  // 激活Activity组件
            }
        });

        //代开相机
        btnButton3 = findViewById(R.id.btn_button3);
        btnButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);   // 意图对象：动作、数据
                startActivity(intent);  // 激活Activity组件
            }
        });

        //代开相机
        btnButton11 = findViewById(R.id.btn_button11);
        btnButton11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FlashLightActivity.class);   // 意图对象：动作、数据
                startActivity(intent);  // 激活Activity组件
            }
        });
    }
}
