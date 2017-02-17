package com.example.lenovo.zhihu.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

public class AskQuestion extends AppCompatActivity {
    String myQuestionTitle;
    String myQusestionContent;
    EditText mQuestion;
    EditText mQuestionContent;
    String mToken;
    String result;
    String returnThing;
    Context context;
    JSONObject jsonObject;
    String questionData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }
public void sendQuestion(final String questionData){
    new Thread(new Runnable() {
        @Override
        public void run() {

            returnThing= NetWork.post("https://api.caoyue.com.cn/bihu/question.php",questionData);
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mQuestion=(EditText)findViewById(R.id.questionTitle);
        mQuestionContent=(EditText)findViewById(R.id.mQuestion);
        myQuestionTitle=mQuestion.getText().toString();
        myQusestionContent=mQuestionContent.getText().toString();
        context=getApplicationContext();
        SharedPreferences sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
        mToken=sharedPreferences.getString("mToken","");
        questionData="token="+mToken+"&"+"title="+myQuestionTitle+"&"+"content="+myQusestionContent;

        switch (item.getItemId()){
            case  R.id.back:
                Intent intent=new Intent(AskQuestion.this,AnswerList.class);
                startActivity(intent);
                break;
            case R.id.finish:
                if (mQuestionContent.getText().toString().equals("")|myQuestionTitle.equals("")){
                    Toast.makeText(this,"标题或内容不能为空",Toast.LENGTH_SHORT).show();

                }
                else
                    sendQuestion(questionData);
                break;
        }
        return true;
    }
    public  void isSuccess(int statusCode)
    {
        if (statusCode==200)
        {
            Intent i=new Intent(AskQuestion.this,Question.class);
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
}
