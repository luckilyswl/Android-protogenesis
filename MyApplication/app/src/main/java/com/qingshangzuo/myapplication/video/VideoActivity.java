package com.qingshangzuo.myapplication.video;

import android.content.Intent;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.qingshangzuo.myapplication.R;
import java.io.File;
import java.util.Calendar;



public class VideoActivity extends AppCompatActivity  implements SurfaceHolder.Callback{

    private static final String TAG = "MainActivity";
    private SurfaceView mSurfaceview;
    private Button btn_start_or_stop;
    private Button btn_play;
    private Button btn_turnto;
    private boolean isRecording = false;//是否正在录像
    private boolean isPlay = false;//是否正在播放录像
    private MediaRecorder mRecorder;
    private SurfaceHolder mSurfaceHolder;
    private ImageView mImageView;
    private Camera camera;
    private MediaPlayer mediaPlayer;
    private String path;
    private Chronometer time;
    private int text = 0;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            text++;
            time.setText("录制"+text+"秒");
            handler.postDelayed(this,1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video);

        mSurfaceview = findViewById(R.id.surfaceview);
        mImageView = findViewById(R.id.imageview);
        btn_start_or_stop = findViewById(R.id.btn_start_or_stop);
        btn_turnto = findViewById(R.id.btn_turnto);
        btn_play = findViewById(R.id.btn_play);
        time = findViewById(R.id.time);
        btn_turnto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (path != null){
                    File file = new File(path);
                    if(file.exists()){
                        Intent intent = new Intent(VideoActivity.this,PlayerActivity.class);
                        intent.putExtra("path",path);
                        startActivity(intent);
                    }else {
                        Toast.makeText(VideoActivity.this,"视频文件不存在",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(VideoActivity.this,"文件路径不存在",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_start_or_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlay){
                    if (mediaPlayer != null){
                        isPlay = false;
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                }
                if (!isRecording){
                    handler.postDelayed(runnable,1000);
                    mImageView.setVisibility(View.GONE);
                    if (mRecorder == null){
                        mRecorder = new MediaRecorder();// 创建mediarecorder对象
                        text = 0;//当点击停止之后，每一次进到这里都要重置录制的时间数
                    }
                    camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                    if (camera != null){
                        camera.setDisplayOrientation(90);//摄像图旋转90度
                        camera.unlock();
                        mRecorder.setCamera(camera);// 设置录制视频源为Camera(相机)
                    }
                    try{
                        mRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER); // 这两项需要放在setOutputFormat之前
                        mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA); // 这两项需要放在setOutputFormat之前
                        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);//设置录制视频的输出格式
                        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);//设置音频编码格式
                        mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);//设置视频编码格式// 设置录制的视频编码h263 h264
                        mRecorder.setVideoSize(640,480);//设置视频的分辨率,必须放在设置编码和格式的后面，否则报错
                        mRecorder.setVideoFrameRate(30);//这是设置视频录制的帧率，即1秒钟30帧。。必须放在设置编码和格式的后面，否则报错
                        mRecorder.setVideoEncodingBitRate(4 * 1024 * 1024);//这个属性很重要，这个也直接影响到视频录制的大小，这个设置的越大，视频越清晰
                        mRecorder.setOrientationHint(90);//视频旋转90度
                        mRecorder.setMaxDuration(30 * 1000);//设置录制最长时间为30秒
                        mRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());//设置录制视频时的预览画面
                        path = getSdPath();
                        if (path != null){
                            File dir = new File(path + "/recordtest");
                            if (!dir.exists()){
                                dir.mkdir();
                            }
                            path = dir + "/" + getDate() + ".mp4";
                            mRecorder.setOutputFile(path);// 设置视频文件输出的路径
                            mRecorder.prepare();// 准备录制
                            mRecorder.start();// 开始录制
                            isRecording = true;
                            btn_start_or_stop.setText("停止");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    if (isRecording){
                        try {
                            handler.removeCallbacks(runnable);
                            mRecorder.stop();// 停止录制
                            mRecorder.reset();// 恢复到未初始化的状态
                            mRecorder.release();// 释放资源
                            mRecorder = null;
                            btn_start_or_stop.setText("开始");
                            if (camera != null){
                                camera.release();
                                camera = null;
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        isRecording = false;
                    }
                }
            }
        });
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlay = true;
                mImageView.setVisibility(View.GONE);
                if (mediaPlayer == null){
                    mediaPlayer = new MediaPlayer();
                }
                mediaPlayer.reset();
                Uri uri = Uri.parse(path);
                if (uri == null){
                    Toast.makeText(VideoActivity.this,"请先录制视频",Toast.LENGTH_SHORT).show();
                    return;
                }
                mediaPlayer = MediaPlayer.create(VideoActivity.this,uri);//使用mediaplayer播放uri视频
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//设置播放流媒体类型。
                mediaPlayer.setDisplay(mSurfaceHolder);// 设置屏幕
                try{
                    mediaPlayer.prepare();
                }catch (Exception e){
                    e.printStackTrace();
                }
                mediaPlayer.start();
            }
        });
        SurfaceHolder holder = mSurfaceview.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isRecording){
            mImageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mSurfaceHolder = holder;
    }
    /**
     * 获取系统时间
     *
     * @return
     */
    private static String getDate(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        int minute = calendar.get(Calendar.MINUTE);
        int hour = calendar.get(Calendar.HOUR);
        int second = calendar.get(Calendar.SECOND);
        String date = "" + year + (month + 1) + day + hour + minute + second;
        return date;
    }
    /**
     * 获取SD path
     *
     * @return
     */
    public String getSdPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist){
            sdDir = Environment.getExternalStorageDirectory();//获取根目录
            return sdDir.toString();
        }
        return null;
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // 将holder，这个holder为开始在onCreate里面取得的holder，将它赋给mSurfaceHolder
        mSurfaceHolder = holder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // surfaceDestroyed的时候同时对象设置为null
        mSurfaceview = null;
        mSurfaceHolder = null;
        handler.removeCallbacks(runnable);
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
        if (camera != null) {
            camera.release();
            camera = null;
        }
        if (mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
