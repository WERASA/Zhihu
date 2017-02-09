package com.example.lenovo.zhihu.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lenovo.zhihu.R;
import com.example.lenovo.zhihu.Tools.NetWork;

import org.json.JSONException;
import org.json.JSONObject;

public class PutAnswer extends AppCompatActivity {
    String myQuestionTitle;
    String myAnswerContent;

    EditText mAnswerContent;
    String mToken;
    String result;
    String returnThing;
    Context context;
    JSONObject jsonObject;
    String questionData;
    String qid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_answer);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();
        qid=intent.getStringExtra("qid");


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mAnswerContent=(EditText)findViewById(R.id.answerContent);
        myAnswerContent=mAnswerContent.getText().toString();
        context=getApplicationContext();
        SharedPreferences sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
        mToken=sharedPreferences.getString("mToken","");
        questionData="token="+mToken+"&"+"qid="+qid+"&"+"content="+myAnswerContent;

        switch (item.getItemId()){
            case  R.id.back:
                Intent intent=new Intent(PutAnswer.this,Question.class);
                startActivity(intent);
                break;
            case R.id.finish:
                sendQuestion(questionData);
                break;
        }
        return true;
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 0:
                    try {
                        Toast.makeText(context,jsonObject.getString("info"),Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }
    public  void isSuccess(int statusCode)
    {
        if (statusCode==200)
        {
            Intent i=new Intent(PutAnswer.this,AnswerList.class);
            startActivity(i);
            Message msg=new Message();
            msg.what=0;
            handler.sendMessage(msg);
        }
        else {
            Message msg=new Message();
            msg.what=0;
            handler.sendMessage(msg);
        }
    }
    public void sendQuestion(final String answerData){
        new Thread(new Runnable() {
            @Override
            public void run() {

                returnThing= NetWork.post("https://api.caoyue.com.cn/bihu/answer.php",answerData);
                try {
                    jsonObject=new JSONObject(returnThing);
                    result=jsonObject.getString("info");
                    isSuccess(jsonObject.getInt("status"));


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }).start();

    }
}
