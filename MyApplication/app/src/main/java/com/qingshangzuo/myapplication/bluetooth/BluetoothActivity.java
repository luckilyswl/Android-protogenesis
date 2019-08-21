package com.qingshangzuo.myapplication.bluetooth;


import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.qingshangzuo.myapplication.R;

public class BluetoothActivity extends AppCompatActivity {

    private static final String TAG = "BluetoothActivity";
    private static final int REQUEAT_OPEN_BT = 0x01; //打开蓝牙
    private static final int REQUEST_CANCELED = 0x02; //打开蓝牙
    private Button mBtnOpenBt;
    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        //获取蓝牙适配器
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //判断蓝牙功能是否存在
        if (mBluetoothAdapter == null) {
            showToast("该设备不支持蓝牙....");
            return;
        }

        //获取名字 MAC地址
        String name = mBluetoothAdapter.getName();
        String mac = mBluetoothAdapter.getAddress();
        Log.e(TAG, "名字：" + name + ",mac:" + mac);

        //获取当前蓝牙状态
        int state = mBluetoothAdapter.getState();
        switch (state) {
            case BluetoothAdapter.STATE_ON:  // 蓝牙已打开
                showToast("已打开蓝牙");
                break;
            case BluetoothAdapter.STATE_TURNING_ON:  // 蓝牙正在打开
                showToast("正在打开....");
                break;
            case BluetoothAdapter.STATE_TURNING_OFF:  // 蓝牙正在关闭
                showToast("正在关闭蓝牙....");
                break;
            case BluetoothAdapter.STATE_OFF:  // 蓝牙已关闭
                showToast("已关闭蓝牙....");
                break;
        }

        mBtnOpenBt = findViewById(R.id.btn_open_bt);
        mBtnOpenBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 关闭 -- 打开本地蓝牙设备
                //判断蓝牙是否已经打开
                if (mBluetoothAdapter.isEnabled()) {
                    showToast("蓝牙已经处于打开状态....");
                    boolean isClose = mBluetoothAdapter.disable();
                    Log.e(TAG, "蓝牙是否关闭：" + isClose);
                } else {
                    // 打开蓝牙
                    //boolean isOpen = mBluetoothAdapter.enable();
                    //showToast("蓝牙的状态：" + isOpen);

                    //调用系统API打开
                    Intent open = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(open, REQUEAT_OPEN_BT);

                }
            }
        });
    }

    public void showToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEAT_OPEN_BT == requestCode) {
            if (resultCode == REQUEST_CANCELED) {
                showToast("请求失败");
            } else {
                showToast("请求成功");
            }
        }
    }
}
