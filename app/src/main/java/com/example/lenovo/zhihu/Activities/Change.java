package com.example.lenovo.zhihu.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.zhihu.R;
import com.example.lenovo.zhihu.Tools.NetWork;

import org.json.JSONException;
import org.json.JSONObject;

public class Change extends AppCompatActivity {
    String mToken;
    EditText mNewPassword;
    TextView finsh;
    TextView back;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        mNewPassword=(EditText)findViewById(R.id.mNewPassword);
        finsh=(TextView)findViewById(R.id.finish);
        back=(TextView)findViewById(R.id.back);
        SharedPreferences sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
        mToken=sharedPreferences.getString("mToken","");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Change.this,Question.class);
                startActivity(i);
            }
        });
        finsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putNewPassword();

            }
        });
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {case 0:
                Intent i=new Intent(Change.this,Question.class);
                startActivity(i);
                case 1:
                    Toast.makeText(Change.this,result,Toast.LENGTH_SHORT).show();

            }
        }
    };

    private void putNewPassword(){
        final String data="token="+mToken+"&password="+mNewPassword.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String returnData= NetWork.post("https://api.caoyue.com.cn/bihu/changePassword.php",data);
                try {
                    JSONObject getData=new JSONObject(returnData);
                    if (getData.getInt("status")==200){
                        String returnToken=getData.getString("data");
                        JSONObject jsonObject=new JSONObject(returnToken);
                       SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();

                        editor.putString("mToken",jsonObject.getString("token"));
                        editor.apply();

                        Message message=new Message();
                        message.what=0;
                        handler.sendMessage(message);

                    }
                    else
                    {
                        Log.d("1",getData.getString("status"));
                        result=getData.getString("info");
                        Message message=new Message();
                        message.what=1;
                        handler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();



    }
}
