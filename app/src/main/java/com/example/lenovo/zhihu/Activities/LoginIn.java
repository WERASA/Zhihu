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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.zhihu.R;
import com.example.lenovo.zhihu.Tools.NetWork;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginIn extends AppCompatActivity {
    EditText mName;
    EditText mPassword;
    String result;
   int resultCode;
    TextView login;
    TextView loginIn;
    TextView loginResult;
    String returnThing;

    Handler setErrorMessage =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            loginResult=(TextView)findViewById(R.id.loginResult);
            if (msg.what==1){
                loginResult.setText(result);
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_in);
        login=(TextView)findViewById(R.id.login);
        loginIn=(TextView)findViewById(R.id.loginIn);
        loginIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(LoginIn.this,Login.class);
                startActivity(i);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mName=(EditText)findViewById(R.id.username);
                mPassword=(EditText)findViewById(R.id.Password);
                if (mPassword.getText().length()<6){
                    Toast.makeText(LoginIn.this,"密码过短",Toast.LENGTH_LONG).show();
                }
                else{
                sendLogin();}
            }
        });
    }

public void sendLogin(){
     new Thread(new Runnable() {
         @Override
         public void run() {
             mName=(EditText)findViewById(R.id.username);
             mPassword=(EditText)findViewById(R.id.Password);
             String data="username="+mName.getText().toString()+"&"+"password="+mPassword.getText().toString();
             returnThing =  NetWork.post("https://api.caoyue.com.cn/bihu/login.php",data);
             JSONObject jsonObject;
             try {
                 jsonObject=new JSONObject(returnThing);
                 result=jsonObject.getString("info");
                 resultCode= Integer.parseInt(jsonObject.getString("status"));
                 checkLoginMessage(resultCode,jsonObject);
             }
             catch (JSONException e) {
                 e.printStackTrace();
             }
         }
     }).start();
}
    private void checkLoginMessage(int statusCode,JSONObject jsonObject ){
        loginResult=(TextView) findViewById(R.id.loginResult);
        if (statusCode==200){
                SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
                try {
                    String data=  jsonObject.getString("data");
                    JSONObject mData=new JSONObject(data);
                    editor.putString("username",mData.getString("username"));
                    editor.putString("mToken",mData.getString("token"));
                    editor.putString("mAvatar",mData.getString("avatar"));
                    editor.apply();
                    Intent i=new Intent(LoginIn.this,Question.class);

                    startActivity(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
        else {
            try {
                result=jsonObject.getString("info");
                Message message=new Message();
                message.what=1;
                setErrorMessage.sendMessage(message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
