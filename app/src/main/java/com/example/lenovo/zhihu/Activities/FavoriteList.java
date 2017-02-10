package com.example.lenovo.zhihu.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.lenovo.zhihu.R;
import com.example.lenovo.zhihu.Tools.NetWork;
import com.example.lenovo.zhihu.Tools.QuestionParaser;
import com.example.lenovo.zhihu.adapter.FavouriteListAdapter;
import com.example.lenovo.zhihu.adapter.RvAdapter;
import com.example.lenovo.zhihu.data.MyQuestion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FavoriteList extends AppCompatActivity {
    private  DrawerLayout drawerLayout;
    private NavigationView mNavigationView;
    JSONArray  returnQuestions;
    Context context;
    int statusCode;
    String getQuestion;
    String result;
    public SwipeRefreshLayout refresh;
    RecyclerView recyclerView;
    int page=0;
    private boolean isLoading;
    public  ArrayList<MyQuestion>myQuestions;
    LinearLayoutManager linearLayoutManager;
    private  boolean isRefresh=false;
   FavouriteListAdapter favouriteListAdapter;
    ArrayList<MyQuestion>reQuestions;
    String mToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);
        mNavigationView=(NavigationView)findViewById(R.id.navigation) ;
        refresh=(SwipeRefreshLayout)findViewById(R.id.refreshData);
        recyclerView=(RecyclerView)findViewById(R.id.questions);
        context=this;
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar) ;
        setSupportActionBar(toolbar);
        linearLayoutManager=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        getData(0,1);
        setBoottom();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favourite_bar,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {    case  R.id.back:
            Intent intent=new Intent(FavoriteList.this,Question.class);
            startActivity(intent);
            break;

        }
        return super.onOptionsItemSelected(item);

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
                String postData="token="+mToken+"&"+"page=0"+"&count=20";
                String respond= NetWork.post("https://api.caoyue.com.cn/bihu/getFavoriteList.php",postData);
                try {
                    JSONObject jsonObject=new JSONObject(respond);

                         Log.d("jsonobject",respond);
                        String data=jsonObject.getString("data");
                        JSONObject mData=new JSONObject(data);
                        JSONArray questions=mData.getJSONArray("questions");
                        reQuestions =QuestionParaser.myQuestions(questions);
                        Message message=new Message();
                        message.what=0;
                        handler.sendMessage(message);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();


    }







    public  void getData(int mPage, final int type){

        final int getPage=mPage;
        new   Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
                mToken=sharedPreferences.getString("mToken","");
                String page=String.valueOf(getPage);
                String postData="token="+mToken+"&"+"page="+page+"&count=20";
                getQuestion= NetWork.post("https://api.caoyue.com.cn/bihu/getFavoriteList.php",postData);
                try {
                    JSONObject jsonObject=new JSONObject(getQuestion);
                    statusCode=Integer.parseInt(jsonObject.getString("status"));
                    if (statusCode==200) {
                        String returnData = null;
                        if (jsonObject.getString("info").equals("success")) {
                            returnData = jsonObject.getString("data");
                            JSONObject mData = new JSONObject(returnData);
                            returnQuestions = mData.getJSONArray("questions");
                            myQuestions = QuestionParaser.myQuestions(returnQuestions);
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
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    favouriteListAdapter.refresh(reQuestions);
                    Log.d("data", String.valueOf(reQuestions.size()));
                    isRefresh=false;
                    break;
                case 1:
                    favouriteListAdapter = new FavouriteListAdapter(myQuestions,mToken,context);
                    recyclerView.setAdapter(favouriteListAdapter);
                    break;
                case 2:
                    Toast.makeText(context,result,Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    favouriteListAdapter.addQuestion(myQuestions);
                    break;
            }
        }
    };
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




}
