package com.qingshangzuo.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PhoneActivity extends AppCompatActivity {

    private Button btnPhone;
    private EditText edtNumber;

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        edtNumber = findViewById(R.id.edt_number);
        btnPhone = findViewById(R.id.btn_phone);

        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //第一种：打电话，因为权限问题，所以要自己进入到应用里面，打开电话权限
                /*String number = edtNumber.getText().toString();   // 从edtNumber中取得数据并赋值到number
                if(TextUtils.isEmpty(number)){
                    Toast.makeText(PhoneActivity.this, "号码不能为空！！", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_CALL);
                    Uri data = Uri.parse("tel:" + number);
                    intent.setData(data);
                    startActivity(intent);
                }*/

                // 第二种：跳转到授权界面（手机的应用权限），自己手动添加权限

                // 检查是否获取权限
                if (ContextCompat.checkSelfPermission(PhoneActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    // 没有获取授权，申请授权
                    if(ActivityCompat.shouldShowRequestPermissionRationale(PhoneActivity.this,Manifest.permission.CALL_PHONE)){
                        /**
                         *  返回值：
                         *  如果app之前请求过该权限，被用户拒绝，这个方法就会返回true
                         *  如果用户之前拒绝权限的时候勾选了对话框中“Don't ask again”的选项，那么这个方法会返回false
                         *  如果设备策略禁止应用拥有这条权限，这个方法也会返回false
                         *  弹窗需要解释为何需要该权限，再次请求授权
                         */
                        Toast.makeText(PhoneActivity.this, "请授权！！", Toast.LENGTH_SHORT).show();
                        //跳转到授权页，让用户手动授权
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package",getPackageName(),null);
                        intent.setData(uri);
                        startActivity(intent);
                    }else {
                        // 不需要解释为何需要该授权，直接请求授权
                        ActivityCompat.requestPermissions(PhoneActivity.this,new String[] {Manifest.permission.CALL_PHONE},MY_PERMISSIONS_REQUEST_CALL_PHONE);
                    }
                }else {
                    // 已获得授权，可以直接打电话
                    CallPhone();
                }
            }
        });
    }

    private void CallPhone() {
        String number = edtNumber.getText().toString();   // 从edtNumber中取得数据并赋值到number
        if(TextUtils.isEmpty(number)){
            /**
             *  提醒用户
             *  注意：在这个匿名内部类中，如果用this则表示是View.OnClickListener类的对象
             *  所以必须用PhoneActivity.this来指定上下文环境
             */
            Toast.makeText(PhoneActivity.this, "号码不能为空！！", Toast.LENGTH_SHORT).show();
        }else{
            //拨号：激活系统的拨号组件
            Intent intent = new Intent();   // 意图对象：动作 +  数据
            intent.setAction(Intent.ACTION_CALL);   // 设置动作
            Uri data = Uri.parse("tel:" + number);  // 设置数据
            intent.setData(data);
            startActivity(intent);  // 激活Activity组件
        }
    }

    //处理权限申请的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_CALL_PHONE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //授权成功
                    CallPhone();
                }else {
                    Toast.makeText(this, "授权失败！！", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
}
