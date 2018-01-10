package com.example.myapplication;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.myapplication.base.BaseActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MediaActivity extends BaseActivity implements View.OnClickListener {
    private static final int CHOOSE_PHOTO = 2;
    private Button chooseFromAlbum;
    private ImageView picture;
    private TextView pathTv;
    private static final String TAG = "MediaActivity";
    //音频相关
    private Button play, pause, stop;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    //视频相关
    private Button playVideo, pauseVideo, replayVideo;
    private VideoView videoView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_media;

    }

    @Override
    public void initData() {
        initPermission();
        initMediaPlayer();
    }


    @Override
    public void initView() {
        //照片选择
        chooseFromAlbum = (Button) findViewById(R.id.choose_from_album);
        picture = (ImageView) findViewById(R.id.picture);
        chooseFromAlbum.setOnClickListener(this);
        pathTv = (TextView) findViewById(R.id.path_Tv);
        //音频播放
        play = (Button) findViewById(R.id.play);
        pause = (Button) findViewById(R.id.pause);
        stop = (Button) findViewById(R.id.stop);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);
        //视频播放
        playVideo = (Button) findViewById(R.id.play_video);
        pauseVideo = (Button) findViewById(R.id.pause_video);
        replayVideo = (Button) findViewById(R.id.replay_video);
        videoView=(VideoView)findViewById(R.id.video);
        playVideo.setOnClickListener(this);
        pauseVideo.setOnClickListener(this);
        replayVideo.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choose_from_album:
                openAlbum();
                break;
            case R.id.play:
                if(!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                }
                break;
            case R.id.pause:
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
                break;
            case R.id.stop:
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.reset();
                    initMediaPlayer();
                }
                break;
            case R.id.play_video:
                initVideoPath();
                if(!videoView.isPlaying()){
                    videoView.start();
                }
                break;
            case R.id.pause_video:
                if(videoView.isPlaying()){
                    videoView.pause();
                }
                break;
            case R.id.replay_video:
                if(videoView.isPlaying()){
                    videoView.resume();
                }
                break;
            default:
                break;
        }
    }
    //播放器初始化
    private void initMediaPlayer() {
        try {
            File file=new File(Environment.getExternalStorageDirectory(),"1511339827807.wav");
            mediaPlayer.setDataSource(file.getPath());//加载音频资源
            mediaPlayer.prepare();//准备
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //视频播放初始化
    private void initVideoPath() {
        File file=new File(Environment.getExternalStorageDirectory()+"/XHS","WelcomeVideo.mp4");
        videoView.setVideoPath(file.getPath());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
       if(mediaPlayer!=null){
           mediaPlayer.stop();
           mediaPlayer.release();
       }
       if(videoView!=null){
           videoView.suspend();
       }
    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String permissions[] = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                //进入到这里代表没有权限.
            }
        }
        String tmpList[] = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 1);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "已授予权限", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "没有权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //打开照片选择器
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4及以上
                        handleImageOnKitKat(data);
                    } else {
                        //4.4以下
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
        }
    }

    //4.4以下
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    //4.4及以上
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的Uir͵则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的Uri
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的Uri
            imagePath = uri.getPath();
        }
        Log.i(TAG, "handleImageOnKitKat: " + imagePath);
        displayImage(imagePath);//显示
    }

    private String getImagePath(Uri contentUri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(contentUri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            pathTv.setText(imagePath);
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "无显示", Toast.LENGTH_SHORT).show();
        }
    }
}
