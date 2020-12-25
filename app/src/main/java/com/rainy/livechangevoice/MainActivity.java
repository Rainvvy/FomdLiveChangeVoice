package com.rainy.livechangevoice;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.FileUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.fmod.FMOD;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import static com.rainy.livechangevoice.VoiceUtil.MODE_DASHU;
import static com.rainy.livechangevoice.VoiceUtil.MODE_GAOGUAI;
import static com.rainy.livechangevoice.VoiceUtil.MODE_JINGSONG;
import static com.rainy.livechangevoice.VoiceUtil.MODE_KONGLING;
import static com.rainy.livechangevoice.VoiceUtil.MODE_LUOLI;
import static com.rainy.livechangevoice.VoiceUtil.MODE_YUANSHENG;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    private static final int GET_RECODE_AUDIO = 1;
    private static String[] PERMISSION_AUDIO = {
            Manifest.permission.RECORD_AUDIO
    };

    private String path;


    MediaRecorder mMediaRecorder;

    String filePath;
    String audioSaveDir ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.yuansheng).setOnClickListener(this);
        findViewById(R.id.luoli).setOnClickListener(this);
        findViewById(R.id.dashu).setOnClickListener(this);
        findViewById(R.id.jingsong).setOnClickListener(this);
        findViewById(R.id.gaoguai).setOnClickListener(this);
        findViewById(R.id.kongling).setOnClickListener(this);
        findViewById(R.id.record).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                TextView text = (TextView) v;
                switch (event.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        int permission = ActivityCompat.checkSelfPermission(MainActivity.this,
                                Manifest.permission.RECORD_AUDIO);
                            if (permission != PackageManager.PERMISSION_GRANTED) {
                               verifyAudioPermissions(MainActivity.this);
                               return false;
                            }
                            text.setText("正在录制中,松手可停止录制。");
                            startRecord();
                            return  true;
                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_UP:
                            text.setText("点我录音");
                            //并且播放，，点击下面的变声 也可播放
                            path = filePath;
                            stopRecord();
                            break;



                }
                return false;
            }
        });
//        path = "file:///android_asset/hh.mp3";
        audioSaveDir = getExternalFilesDir(null).getParent() +  File.separator + "cache/audio/";
        FMOD.init(this);
        verifyAudioPermissions(this);
    }

    @Override
    public void onClick(View v) {
        if (path == null){
            Toast.makeText(MainActivity.this,"请录制完成后点击播放！",Toast.LENGTH_SHORT).show();
            return;
        }

        switch (v.getId()){

            case R.id.yuansheng:
                VoiceUtil.voiceChange(path,MODE_YUANSHENG);
                break;
            case R.id.luoli:{
                VoiceUtil.voiceChange(path,MODE_LUOLI);
                break;
            }
            case R.id.dashu: {
                VoiceUtil.voiceChange(path,MODE_DASHU);
                break;
            }
            case R.id.jingsong:{
                VoiceUtil.voiceChange(path,MODE_JINGSONG);
                break;
            }
            case R.id.gaoguai:{
                VoiceUtil.voiceChange(path,MODE_GAOGUAI);
                break;
            }
            case R.id.kongling:{
                VoiceUtil.voiceChange(path,MODE_KONGLING);
                break;
            }

            default:break;

        }
    }

    /*
     * 申请录音权限*/
    public  void verifyAudioPermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.RECORD_AUDIO);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSION_AUDIO,
                    GET_RECODE_AUDIO);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GET_RECODE_AUDIO){
            int permission =  ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.RECORD_AUDIO);
            if (permission != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(MainActivity.this,"小伙子你不讲武德啊，竟然拒绝我的权限！",Toast.LENGTH_SHORT).show();
                return;
            }

        }
    }


    public void startRecord() {
        // 开始录音
        /* ①Initial：实例化MediaRecorder对象 */
        if (mMediaRecorder == null)
            mMediaRecorder = new MediaRecorder();
        try {
            /* ②setAudioSource/setVedioSource */
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置麦克风
            /*
             * ②设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
             * ，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
             */
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            /* ②设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样 */
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
           String fileName = DateFormat.format("yyyyMMdd_HHmmss", Calendar.getInstance(Locale.CHINA)) + ".m4a";
           if (!new File(audioSaveDir).exists()){
               new File(audioSaveDir).mkdirs();
           }

            filePath = audioSaveDir + fileName;
            /* ③准备 */
            mMediaRecorder.setOutputFile(filePath);
            mMediaRecorder.prepare();
            /* ④开始 */
            mMediaRecorder.start();
        } catch (IllegalStateException e) {
            Log.i("MainActivity","call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        } catch (IOException e) {
            Log.i("MainActivity","call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        }
    }

    public void stopRecord() {
        try {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
            filePath = "";
        } catch (RuntimeException e) {
            Log.e("MainActivity",e.toString());
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;

            File file = new File(filePath);
            if (file.exists())
                file.delete();

            filePath = null;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        FMOD.close();
        stopRecord();
    }
}
