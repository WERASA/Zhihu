package com.example.lenovo.zhihu.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lenovo.zhihu.R;
import com.example.lenovo.zhihu.Tools.BitmapUtil;
import com.example.lenovo.zhihu.Tools.NetWork;
import com.example.lenovo.zhihu.Tools.QiniuUpload;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.util.Auth;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;

public class UploadAvatar extends AppCompatActivity implements View.OnClickListener {
TextView choseAvatar;

    ImageView mAvatar;
    String result;
    String path;
    String mToken;
    String secretKey;
    String accessKey;
    Bitmap bitmap;
    TextView back;


    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    Toast.makeText(UploadAvatar.this,result,Toast.LENGTH_SHORT).show();

            }
        }
    };


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_avatar);
        choseAvatar=(TextView)findViewById(R.id.chosePhoto);
        back=(TextView)findViewById(R.id.back) ;
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(UploadAvatar.this,Question.class);
                startActivity(i);
            }
        });
        mAvatar=(ImageView)findViewById(R.id.avatar);
        SharedPreferences sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
        mToken=sharedPreferences.getString("mToken","");
        choseAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(UploadAvatar.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){
                         ActivityCompat.requestPermissions(UploadAvatar.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

                    } else
                    openAlbum();
            }
        });


}

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chosePhoto:
                openAlbum();
        }
    }

    private void openAlbum(){
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,1 );


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data==null){
            result="未选择头像";
            Message message=new Message();
            message.what=0;
            handler.sendMessage(message);

        }
        else {
            path = BitmapUtil.parseImagePathString(data, UploadAvatar.this);
            Log.d("log", path);
            uploadmAvatar(path, mToken);

            Glide.with(UploadAvatar.this).load(path).into(mAvatar);
        }

    }
    public void uploadmAvatar(String path, String key){


            UploadManager uploadManager=new UploadManager();
            Auth auth=Auth.create("EkgHnK6hay4eCw73StmWB6Dz-r9-l8bHGG7df7b8","l7hicsICYNt7Q3foskLmhSBnAtLICb_rwa8uE3bI");
            String token=auth.uploadToken("zhihu");
            uploadManager.put(path, key, token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if (info.isOK()){
                  new Thread(new Runnable() {
                      @Override
                      public void run() {
                          String mAvatar="avatar="+"http://olekc4jwu.bkt.clouddn.com/"+mToken+"&token="+mToken;
                          String resultCode=  NetWork.post("https://api.caoyue.com.cn/bihu/modifyAvatar.php",mAvatar);
                          try {
                              JSONObject jsonObject=new JSONObject(resultCode);
                              if (jsonObject.getInt("status")==200)
                              {
                                  Intent i=new Intent(UploadAvatar.this,Question.class);
                                  startActivity(i);
                              }
                              else
                              {
                                  result=jsonObject.getString("info");
                                  Message message=new Message();
                                  message.what=0;
                                  handler.sendMessage(message);
                              }

                          } catch (JSONException e) {
                              e.printStackTrace();
                          }


                      }
                  }).start();

                }
                else
                    Log.d("message", String.valueOf(info));

            }
        },null);


        }


    }

