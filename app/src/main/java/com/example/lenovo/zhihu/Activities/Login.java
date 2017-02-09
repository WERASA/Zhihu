package com.example.lenovo.zhihu.Activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.zhihu.R;
import com.example.lenovo.zhihu.Tools.NetWork;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity implements View.OnClickListener {
   EditText memberName;
    EditText memberPassword;
    TextView mResult;
    TextView loginIn;
    String result;
    TextView login;
    int resultCode;
Handler loginResult=new Handler(){
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.what==1){
            mResult.setText(result);
        }
    }
};
    public void sendLoginMessage(){
       new Thread(new Runnable() {
           @Override
           public void run() {
               mResult=(TextView)findViewById(R.id.result);
               memberName=(EditText)findViewById(R.id.memberName);
               memberPassword=(EditText)findViewById(R.id.memberPassword);
               String data="username="+memberName.getText().toString()+"&"+"password="+memberPassword.getText().toString();
               String returnThing= NetWork.post("https://api.caoyue.com.cn/bihu/register.php",data);
               JSONObject jsonObject;
               try {
                   jsonObject=new JSONObject(returnThing);
                    result=jsonObject.getString("info");
                    resultCode= Integer.parseInt(jsonObject.getString("status"));
                  isLogin(resultCode);
                   }
                catch (JSONException e) {
                   e.printStackTrace();
               }

           }
       }).start();
    }
public  void isLogin(int statusCode){
    if (statusCode==200)
    {
        Intent i=new Intent(Login.this,Question.class);
        startActivity(i);
    }
    else {
        Message message=new Message();
        message.what=1;
        loginResult.sendMessage(message);
    }
}




    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.loginIn){
            memberName=(EditText)findViewById(R.id.memberName);
            memberPassword=(EditText)findViewById(R.id.memberPassword);

          if (memberPassword.getText().length()<6){
              Toast.makeText(Login.this,"密码过短",Toast.LENGTH_LONG).show();
          }
            else {
              sendLoginMessage();
          }

        }
        if (v.getId()==R.id.login){
            Intent intent=new Intent(Login.this,LoginIn.class);
            startActivity(intent);
        }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginIn=(TextView)findViewById(R.id.loginIn);
        loginIn.setOnClickListener(this);
        login=(TextView)findViewById(R.id.login);
        login.setOnClickListener(this);
    }

}

