package com.example.lenovo.zhihu.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.zhihu.Activities.AnswerList;
import com.example.lenovo.zhihu.Activities.Question;
import com.example.lenovo.zhihu.R;
import com.example.lenovo.zhihu.Tools.MyItemListener;
import com.example.lenovo.zhihu.Tools.NetWork;
import com.example.lenovo.zhihu.data.MyQuestion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/1/20.
 */

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.questionViewHolder>implements  MyItemListener {
    private ArrayList<MyQuestion>questions;
    public   int position;
    public  String user;
    public MyItemListener mItemClickListener;
    public MyQuestion question;
    Context context;
    MyItemListener myItemListener;
    public   RvAdapter(ArrayList<MyQuestion>questions,String user,Context context){
        this.user=user;
        this.questions=questions;
        this.context=context;}

    public void refresh(ArrayList<MyQuestion> questions) {
       this.questions.clear();
        addQuestion(questions);
    }
public void addQuestion(ArrayList<MyQuestion>questions){
    this.questions.addAll(questions);
    Log.d("size", String.valueOf(this.questions.size()));
    notifyDataSetChanged();}





    @Override
    public int getItemCount() {
        return  questions.size();
    }
    @Override
    public questionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item,parent,false);
        questionViewHolder mQuestionViewHolder=new questionViewHolder(view);
        return mQuestionViewHolder;
    }
    public void setOnItemClickListener(MyItemListener listener){
        this.mItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(questionViewHolder holder, int position) {
        holder.mQuestionTitle.setText(questions.get(position).getTitle());
        holder.userName.setText("来自"+questions.get(position).getAuthorName()+"的问题");
        holder.mQuestionData.setText(questions.get(position).getContent());
        holder.agreeNumber.setText(questions.get(position).getExcitingCount()+"赞同");
        holder.answerNumber.setText(questions.get(position).getAnswerCount()+"回答");
        holder.mHead.setImageResource(R.drawable.account);
        holder.naiveNumber.setText(questions.get(position).getNaiveCount()+"反对");
        question=questions.get(position);
        if (question.isFavourite){
            holder.favourite.setText("已收藏该问题");
        }
        if (question.isExciting())
        {
            holder.isExctied.setImageResource(R.drawable.after);
        }
        if (question.isNaive()){
            holder.isNaive.setImageResource(R.drawable.disagree);
        }

        this.position=position;
    }
public void setmItemClickListener(MyItemListener mItemClickListener){
    this.mItemClickListener=mItemClickListener;
}
    @Override
    public void onItemClick(int position, View view) {

    }


    public class questionViewHolder extends  RecyclerView.ViewHolder {
       TextView userName;
        TextView agreeNumber;
        TextView answerNumber;
        TextView mQuestionTitle;
        TextView mQuestionData;
        ImageView mHead;
        TextView naiveNumber;
        ImageView isExctied;
        ImageView isNaive;
        TextView favourite;
        String result;
        public Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){

                    case 0:
                        isNaive.setImageResource(R.drawable.disagree);
                        question.isNaive=true;
                        question.setNaiveCount(question.getNaiveCount()+1);
                       naiveNumber.setText((question.getNaiveCount())+"反对");
                        break;
                    case 1:
                        isExctied.setImageResource(R.drawable.after);
                        question.setExcitingCount(question.getExcitingCount()+1);
                        agreeNumber.setText(question.getExcitingCount()+"赞同");
                        question.isExciting=true;
                        break;
                    case  2:
                        isNaive.setImageResource(R.drawable.disagre);
                        question.isNaive=false;
                        question.setNaiveCount(question.getNaiveCount()-1);
                        naiveNumber.setText(question.getNaiveCount()+"反对");
                        break;

                    case 3:
                        question.isExciting=false;
                        isExctied.setImageResource(R.drawable.before);
                        favourite.setText("已收藏问题");

                        question.setExcitingCount(question.getExcitingCount()-1);
                        agreeNumber.setText(question.getExcitingCount()+"赞同");
                        break;
                    case 4:
                        question.isFavourite=true;
                        break;
                    case 5:
                        question.isFavourite=false;
                        favourite.setText("收藏问题");
                        break;
                    case 6:
                        Toast.makeText(context,result,Toast.LENGTH_SHORT).show();
                        break;





                }
            }
        };
        public questionViewHolder(View itemView) {
            super(itemView);
            naiveNumber=(TextView)itemView.findViewById(R.id.naive) ;
            mHead=(ImageView) itemView.findViewById(R.id.userhead);
            mQuestionData=(TextView)itemView.findViewById(R.id.questionData);
            mQuestionTitle=(TextView)itemView.findViewById(R.id.questionTitle) ;
            userName=(TextView) itemView.findViewById(R.id.username);
            agreeNumber=(TextView)itemView.findViewById(R.id.agreeNumber);
            answerNumber=(TextView)itemView.findViewById(R.id.answerNumber);
            isExctied=(ImageView)itemView.findViewById(R.id.agree);
            isNaive=(ImageView)itemView.findViewById(R.id.disagree);
            favourite=(TextView)itemView.findViewById(R.id.favourite) ;


            mQuestionData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(v.getContext(),AnswerList.class);
                    SharedPreferences.Editor editor=context.getSharedPreferences("data",context.MODE_PRIVATE).edit();
                    editor.putString("qid", String.valueOf(question.getId()));
                    editor.putString("Title",mQuestionTitle.getText().toString());
                    editor.apply();
                    v.getContext().startActivity(i);
                }
            });
               favourite.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       if (question.isFavourite){
                           concelAgreeOrDisagree("favourite",question.getId());
                       }
                       else
                           agreeOrDisagree("favourite",question.getId());
                   }
               });
            isExctied.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("1","点击");
                   if (question.isExciting()){
                   concelAgreeOrDisagree("agree",question.getId());}
                    else
                       agreeOrDisagree("agree",question.getId());
                }



                });

            isNaive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("1","点击");
                    if (question.isNaive()){
                        concelAgreeOrDisagree("disagree",question.getId());
                    }
                    else
                        agreeOrDisagree("disagree",question.getId());
                }


                });


        }


        public  void  agreeOrDisagree(final String type, final int ID ){
            final int StatusCode=0;
            Log.d("1","1");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String data="id="+String.valueOf(ID)+"&&token="+user+"&&type=1";
                    try {
                        if (type.equals("agree")){
                        String result= NetWork.post("https://api.caoyue.com.cn/bihu/exciting.php",data);
                        JSONObject jsonObject=new JSONObject(result);
                        int   statusCode=jsonObject.getInt("status");
                            Log.d("1","2");
                        if (statusCode==200){
                            Log.d("1","3");
                            Log.d("result",jsonObject.getString("info"));
                            Log.d("token",user);
                            Message message=new Message();
                            message.what=1;
                            handler.sendMessage(message);}
                        else {

                            result=jsonObject.getString("info");
                            Log.d("result",result);
                            Message message=new Message();
                            message.what=6;
                            handler.sendMessage(message);
                            Log.d("position",String.valueOf(position));
                            Log.d("id",String.valueOf(question.getId()));


                        }

                        }
                        if (type.equals("disagree")){
                            String result= NetWork.post("https://api.caoyue.com.cn/bihu/naive.php",data);
                            JSONObject jsonObject=new JSONObject(result);
                            int   statusCode=jsonObject.getInt("status");
                            if (statusCode==200){
                                Log.d("result",jsonObject.getString("info"));
                                Message message=new Message();
                                message.what=0;
                                handler.sendMessage(message);}
                            else {
                                result=jsonObject.getString("info");
                                Message message=new Message();
                                message.what=6;
                                handler.sendMessage(message);
                                Log.d("position",String.valueOf(position));
                                Log.d("id",String.valueOf(question.getId()));
                                Log.d("id",String.valueOf(question.isNaive));

                            }
                        }
                        if (type.equals("favourite")){
                            String postData="qid="+String.valueOf(ID)+"&&token="+user;
                            String result= NetWork.post("https://api.caoyue.com.cn/bihu/favorite.php",postData);
                            JSONObject jsonObject=new JSONObject(result);
                            int   statusCode=jsonObject.getInt("status");
                            if (statusCode==200){
                                Log.d("result",jsonObject.getString("info"));
                                Message message=new Message();
                                message.what=4;
                                handler.sendMessage(message);}
                            else {
                               result=jsonObject.getString("info");
                                Message message=new Message();
                                message.what=6;
                                Log.d("result",result);
                                Log.d("status", jsonObject.getString("status"));
                                handler.sendMessage(message);
                                Log.d("position",String.valueOf(position));
                                Log.d("id",String.valueOf(question.getId()));
                                Log.d("id",String.valueOf(question.isNaive));

                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();


        }
        public  void  concelAgreeOrDisagree(final String type, final int ID ){
            final int StatusCode=0;
            Log.d("1","1");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String data="id="+String.valueOf(ID)+"&&token="+user+"&&type=1";
                    try {
                        if (type.equals("agree")){
                            String result= NetWork.post("https://api.caoyue.com.cn/bihu/cancelExciting.php",data);
                            JSONObject jsonObject=new JSONObject(result);
                            int   statusCode=jsonObject.getInt("status");
                            Log.d("1","2");
                            if (statusCode==200){
                                Log.d("1","3");
                                Log.d("result",jsonObject.getString("info"));
                                Log.d("token",user);
                                Message message=new Message();
                                message.what=3;
                                handler.sendMessage(message);}
                            else {
                                result=jsonObject.getString("info");
                                Message message=new Message();
                                message.what=6;
                                handler.sendMessage(message);
                                Log.d("position",String.valueOf(position));
                                Log.d("id",String.valueOf(question.getId()));


                            }

                        }
                        if (type.equals("disagree")){
                            String result= NetWork.post("https://api.caoyue.com.cn/bihu/cancelNaive.php",data);
                            JSONObject jsonObject=new JSONObject(result);
                            int   statusCode=jsonObject.getInt("status");
                            if (statusCode==200){
                                Log.d("result",jsonObject.getString("info"));
                                Message message=new Message();
                                message.what=2;
                                handler.sendMessage(message);}
                            else {
                                result=jsonObject.getString("info");
                                Message message=new Message();
                                message.what=6;
                                handler.sendMessage(message);
                                Log.d("position",String.valueOf(position));
                                Log.d("id",String.valueOf(question.getId()));
                                Log.d("id",String.valueOf(question.isNaive));

                            }
                        }
                        if (type.equals("favourite")){
                            String postData="qid="+String.valueOf(ID)+"&&token="+user;
                            String result= NetWork.post("https://api.caoyue.com.cn/bihu/cancelFavorite.php",postData);
                            JSONObject jsonObject=new JSONObject(result);
                            int   statusCode=jsonObject.getInt("status");
                            if (statusCode==200){
                                Log.d("result",jsonObject.getString("info"));
                                Message message=new Message();
                                message.what=5;
                                handler.sendMessage(message);}
                            else {
                             Log.d("info",jsonObject.getString("info"));
                                Message message=new Message();
                                message.what=6;
                                handler.sendMessage(message);
                                Log.d("position",String.valueOf(position));
                                Log.d("id",String.valueOf(question.getId()));
                                Log.d("id",String.valueOf(question.isNaive));

                            }
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();



    }


}}
