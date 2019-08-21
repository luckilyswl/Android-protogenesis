package com.qingshangzuo.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Path;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class PicActivity extends AppCompatActivity {

    private Button btnChoose;
    private ImageView imageView;

    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 1;
    private static final int REQUEST_CODE_PICK_IMAGE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);

        btnChoose = findViewById(R.id.btn_choose);
        imageView = findViewById(R.id.imageview);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(ContextCompat.checkSelfPermission(PicActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                   ActivityCompat.requestPermissions(PicActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSIONS_REQUEST_CODE);
               }else {
                   openPic();
               }
            }
        });

    }

    private void openPic() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

       // super.onActivityResult(requestCode,resultCode,data);  注不注释都没影响
        if(requestCode == 2){
            if(data != null){
                Uri uri = data.getData();
                imageView.setImageURI(uri);
            }
        }
    }
}
