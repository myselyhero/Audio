package com.yongyongwang.audio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yongyongwang.audio.record.AudioKit;
import com.yongyongwang.audio.record.AudioRecordManager;
import com.yongyongwang.audio.record.model.OnAudioRecordListener;
import com.yongyongwang.audio.record.util.DateTimeUtil;
import com.yongyongwang.audio.record.util.FileUtils;
import com.yongyongwang.audio.record.util.LogUtil;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private static final int REQUEST_CODE = 10;
    private String[] permission;

    private AlertDialog alertDialog;

    private TextView recordIngTextView,recordFileTextView;
    private TextView stopTextView,startTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recordIngTextView = findViewById(R.id.main_record_ing);
        recordFileTextView = findViewById(R.id.main_file_size);
        stopTextView = findViewById(R.id.main_stop_btn);
        startTextView = findViewById(R.id.main_start_btn);

        stopTextView.setOnClickListener(this);
        startTextView.setOnClickListener(this);
        //getFile();
    }

    private void getFile(){
        File file = new File(FileUtils.AUDIO_PATH);
        if (!file.exists() || !file.isDirectory())
            return;
        File[] listFiles = file.listFiles();
        for (File f : listFiles) {
            if (f.isFile()){
                LogUtil.e(f.getName());
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.main_start_btn){
            if (checkPermission()){
                requestPermissions();
            }else {
                start();
            }
        }else if (view.getId() == R.id.main_stop_btn){
            stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE){
            int i = 0;
            for (int j : grantResults) {
                if (j == PackageManager.PERMISSION_DENIED){
                    i++;
                }
            }
            if (i > 0){
                Toast.makeText(this,String.format("有%d项权限已被拒绝，部分功能将不可用！",i),Toast.LENGTH_LONG).show();
            }else {
                start();
            }
        }
    }

    /**
     *
     * @return
     */
    private boolean checkPermission() {
        boolean flag = false;
        if (AudioKit.isAndroidQ()){
            permission = new String[]{Manifest.permission.RECORD_AUDIO};
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                flag = true;
            }
        }else {
            permission = new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                flag = true;
            }
        }

        return flag;
    }

    /**
     *
     */
    private void requestPermissions(){
        if (alertDialog == null){
            alertDialog = new AlertDialog.Builder(this, R.style.dialogStyle).create();
            alertDialog.show();
            alertDialog.setContentView(R.layout.main_permission_window);
            alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView desc = alertDialog.findViewById(R.id.main_permission_window_desc);
            TextView cancel = alertDialog.findViewById(R.id.main_permission_window_cancel);
            TextView agree = alertDialog.findViewById(R.id.main_permission_window_agree);

            desc.setText(AudioKit.isAndroidQ() ? "我们将申请您的录音，以便管理我司人员及保障您的安全！" : "我们将申请您的录音权限及读写权限，以便在录音后保存到本地！");

            assert cancel != null;
            cancel.setOnClickListener(view -> {
                alertDialog.dismiss();
            });
            assert agree != null;
            agree.setOnClickListener(view -> {
                alertDialog.dismiss();
                ActivityCompat.requestPermissions(this, permission, REQUEST_CODE);
            });
        }
        if (alertDialog.isShowing())
            return;
        alertDialog.show();
    }

    private void start(){
        startTextView.setEnabled(false);
        stopTextView.setEnabled(true);
        AudioRecordManager.getInstance().startRecord(null, new OnAudioRecordListener() {
            @Override
            public void start(String path) {
                /*List<String> list = new ArrayList<>();
                list.add(path);
                uploaderFile(list);*/
            }

            @Override
            public void complete(String path, long duration) {
                Log.e(TAG, "complete: "+duration);
            }

            @Override
            public void recordContinue(long duration) {
                Log.e(TAG, "recordContinue: "+duration);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recordIngTextView.setText(String.format("已录制时间：%s",DateTimeUtil.getTime(duration)));
                        recordFileTextView.setText(String.format("已录制文件大小：%s",FileUtils.formtFileSize(FileUtils.getAutoFileSize(AudioRecordManager.getInstance().getFilePath()))));
                    }
                });
            }
        });
    }

    /**
     *
     */
    public void stop(){
        stopTextView.setEnabled(false);
        startTextView.setEnabled(true);
        AudioRecordManager.getInstance().stopRecord();
    }
}