package com.example.lenovo.zhihu.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.zhihu.adapter.AnswerAdapter;
import com.example.lenovo.zhihu.R;
import com.example.lenovo.zhihu.Tools.AnswerParaser;
import com.example.lenovo.zhihu.Tools.NetWork;
import com.example.lenovo.zhihu.data.Answer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AnswerList extends AppCompatActivity {
    String result;
    String mToken;
    String getAnswer;
    int statusCode;
    JSONArray returnAnswer;
    Context context;
    String qid;
    RecyclerView recyclerView;
    SwipeRefreshLayout refresh;
    boolean isRefresh;
    int page;
    ArrayList<Answer>reAnswers;
    LinearLayoutManager linearLayoutManager;
    AnswerAdapter answerAdapter;

    ArrayList<Answer>answers=new ArrayList<>();
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    Toast.makeText(context,"已到最底层",Toast.LENGTH_SHORT).show();
                case 1:
                    Log.d("1","1");
                    answerAdapter = new AnswerAdapter(answers,context,mToken,qid);
                    recyclerView.setAdapter(answerAdapter);
                    break;
                case 3:
                    answerAdapter.addAnswers(answers);
                    break;
                case 4:
                    answerAdapter.refresh(reAnswers);
                    Log.d("1", String.valueOf(reAnswers.size()));
                    isRefresh=false;

            }

        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {    case  R.id.back:
            Intent intent=new Intent(AnswerList.this,Question.class);
            startActivity(intent);
            break;
            case R.id.plus:
                Intent i=new Intent(context,PutAnswer.class);
                i.putExtra("qid",qid);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.answer_bar,menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_list);

        TextView question=(TextView)findViewById(R.id.questionTitle) ;
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=AnswerList.this;
        recyclerView=(RecyclerView)findViewById(R.id.answers);
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        SharedPreferences sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
        mToken=sharedPreferences.getString("mToken","");
        qid=sharedPreferences.getString("qid","");
        String qusestionTitle=sharedPreferences.getString("Title","");
        question.setText(qusestionTitle);
        getData(0,1);
       refresh=(SwipeRefreshLayout)findViewById(R.id.swipe);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        setBoottom();


    }
    public  void getData(int mPage, final int type){

        final int getPage=mPage;
        new   Thread(new Runnable() {
            @Override
            public void run() {


                String page=String.valueOf(getPage);
                String postData="token="+mToken+"&"+"page="+page+"&count=20"+"&qid="+qid;
                Log.d("data",postData);
                getAnswer= NetWork.post("https://api.caoyue.com.cn/bihu/getAnswerList.php",postData);
                try {
                    JSONObject jsonObject=new JSONObject(getAnswer);
                    statusCode=Integer.parseInt(jsonObject.getString("status"));
                    Log.d("1","1");
                    if (statusCode==200) {
                        String returnData = null;
                        Log.d("1","1");
                        if (jsonObject.getString("info").equals("success")) {
                            returnData = jsonObject.getString("data");
                            JSONObject mData = new JSONObject(returnData);
                             Log.d("id",qid);
                            returnAnswer = mData.getJSONArray("answers");
                            String answer = mData.getString("answers");
                            Log.d("data",answer);
                            answers = AnswerParaser.myAnswer(returnAnswer);

                            Message message = new Message();
                            message.what = type;
                            handler.sendMessage(message);
                        }
                        else if (jsonObject.getString("info").equals("null")) {
                            Message message = new Message();
                            message.what = 0;
                            handler.sendMessage(message);
                            Log.d("1",jsonObject.getString("info"));
                        }
                    }
                    else
                    {
                        Log.d("1","2");
                        result=jsonObject.getString("info");
                        Message message=new Message();
                        message.what=2;
                        handler.sendMessage(message);}

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
    public void setBoottom(){
        if (isRefresh){
            return;
        }
        isRefresh=true;
        int lastItemId=linearLayoutManager.findLastVisibleItemPosition();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState==RecyclerView.SCROLL_STATE_IDLE)
                {
                    int lastVisibleItem=linearLayoutManager.findLastVisibleItemPosition();
                    if (lastVisibleItem>=linearLayoutManager.getItemCount()-1){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                page++;
                                getData(page,3);

                            }
                        }).start();
                        isRefresh=false;
                    }
                }
            }
        });
    }
    public void refreshData(){
        if (isRefresh){
            refresh.setRefreshing(false);
            return;
        }
        isRefresh=true;
        refresh.setRefreshing(false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
                mToken=sharedPreferences.getString("mToken","");
                String postData="token="+mToken+"&"+"page=0"+"&count=20"+"&qid="+qid;
                String respond= NetWork.post("https://api.caoyue.com.cn/bihu/getAnswerList.php",postData);
                try {
                    JSONObject jsonObject=new JSONObject(respond);
                    if (NetWork.isSuccess(jsonObject)){
                        String data=jsonObject.getString("data");
                        JSONObject mData=new JSONObject(data);
                        Log.d("data",data);
                        JSONArray jsonArray=mData.getJSONArray("answers");
                        reAnswers =AnswerParaser.myAnswer(jsonArray);
                        Message message=new Message();
                        message.what=4;
                        handler.sendMessage(message);
                        Log.d("size", String.valueOf(answers.size()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();


    }
}
