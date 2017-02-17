package com.example.lenovo.zhihu.adapter;

import android.content.Context;
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

import com.bumptech.glide.Glide;
import com.example.lenovo.zhihu.R;
import com.example.lenovo.zhihu.Tools.NetWork;
import com.example.lenovo.zhihu.data.Answer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lenovo on 2017/2/6.
 */

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerHolder> {
    ArrayList<Answer>answers=new ArrayList<>();
    Context context;
    String mToken;
    String qid;
    Answer answer;

    public  AnswerAdapter(ArrayList<Answer>answers, Context context,String mToken,String qid){
        this.answers=answers;
        this.context=context;
        this.mToken=mToken;
        this.qid=qid;
    }

    @Override
    public AnswerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_item,parent,false);
        AnswerHolder mAnswerViewHolder=new AnswerHolder(view);
        return mAnswerViewHolder;

    }

    @Override
    public void onBindViewHolder(AnswerHolder holder, int position) {
        holder.userName.setText("来自"+answers.get(position).getAuthorName()+"的回答");
        holder.answerData.setText(answers.get(position).getContent());
        holder.agreeNumber.setText(answers.get(position).getExciting()+"赞同");
        holder.naiveCount.setText(answers.get(position).getNaive()+"反对");
        Glide.with(context).load(answers.get(position).getUserHeadUrl()).into(holder.mHead);
        answer=answers.get(position);

    }
    public void refresh(ArrayList<Answer> answers) {
        this.answers.clear();
        addAnswers(answers);
    }
    public void addAnswers(ArrayList<Answer>answers){
        this.answers.addAll(answers);
        Log.d("size", String.valueOf(this.answers.size()));
        notifyDataSetChanged();}
    @Override
    public int getItemCount() {
        return answers.size();
    }


    public class AnswerHolder extends RecyclerView.ViewHolder{
        TextView naiveCount;
        TextView agreeNumber;
        TextView answerData;
        TextView userName;
        ImageView naive;
        ImageView agree;
        CircleImageView mHead;
        TextView accept;
        String resultData;
        public Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){

                    case 0:
                        naive.setImageResource(R.drawable.disagree);
                        answer.isNaive=true;
                        answer.setNaive(answer.getNaive()+1);
                        naiveCount.setText(answer.getNaive()+"反对");
                        break;
                    case 1:
                        agree.setImageResource(R.drawable.after);
                        answer.setExciting(answer.getExciting()+1);
                        agreeNumber.setText(answer.getExciting()+"赞同");
                        answer .isExitied=true;
                        break;
                    case  2:
                        naive.setImageResource(R.drawable.disagre);
                        answer.isNaive=false;
                        answer.setNaive(answer.getNaive()-1);
                        naiveCount.setText(answer.getNaive()+"反对");
                        break;

                    case 3:
                        answer.isExitied=false;
                        agree.setImageResource(R.drawable.before);


                        answer.setExciting(answer.getExciting()-1);
                        agreeNumber.setText(answer.getExciting()+"赞同");
                        break;
                    case 4:
                        answer.setIsBest(1);
                        accept.setText("已采纳");

                        break;

                   case 5:
                        Toast.makeText(context,resultData,Toast.LENGTH_SHORT).show();
                        break;





                }
            }
        };

        public AnswerHolder(View itemView) {
            super(itemView);
            naiveCount=(TextView)itemView.findViewById(R.id.naive);
            agreeNumber=(TextView)itemView.findViewById(R.id.agreeNumber);
            answerData=(TextView)itemView.findViewById(R.id.answerData);
            naive=(ImageView)itemView.findViewById(R.id.disagree);
            agree=(ImageView)itemView.findViewById(R.id.agree);
            userName=(TextView)itemView.findViewById(R.id.username);
            accept=(TextView)itemView.findViewById(R.id.accept) ;
            mHead=(CircleImageView)itemView.findViewById(R.id.userhead);
            agree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (answer.isExitied){
                        concelAgreeOrDisagree("agree",answer.getID());
                    }
                    else
                        agreeOrDisagree("agree",answer.getID());
                }
            });
            naive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (answer.isNaive())
                    {
                        concelAgreeOrDisagree("disagree",answer.getID());
                    }
                    else
                        agreeOrDisagree("disagree",answer.getID());
                }
            });
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    agreeOrDisagree("accept",answer.getID());
                }
            });
        }
        public  void  agreeOrDisagree(final String type, final int ID ){
            final int StatusCode=0;
            Log.d("1","1");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String data="id="+String.valueOf(ID)+"&&token="+mToken+"&&type=2";
                    try {
                        if (type.equals("agree")){

                            String result= NetWork.post("https://api.caoyue.com.cn/bihu/exciting.php",data);
                            JSONObject jsonObject=new JSONObject(result);
                            int   statusCode=jsonObject.getInt("status");
                            Log.d("1","2");
                            if (statusCode==200){
                                Log.d("1","3");
                                resultData=jsonObject.getString("info");
                                Log.d("result",jsonObject.getString("info"));
                                Log.d("token",mToken);
                                Message message=new Message();
                                message.what=1;
                                handler.sendMessage(message);}
                            else {

                                resultData=jsonObject.getString("info");
                                Log.d("result",result);
                                Message message=new Message();
                                message.what=5;
                                handler.sendMessage(message);
                                Log.d("id",String.valueOf(answer.getID()));


                            }

                        }
                        if (type.equals("disagree")){
                            String result= NetWork.post("https://api.caoyue.com.cn/bihu/naive.php",data);
                            JSONObject jsonObject=new JSONObject(result);
                            int   statusCode=jsonObject.getInt("status");
                            if (statusCode==200){
                                resultData=jsonObject.getString("info");
                                Log.d("result",jsonObject.getString("info"));
                                Message message=new Message();
                                message.what=0;
                                handler.sendMessage(message);}
                            else {
                                resultData=jsonObject.getString("info");
                                Message message=new Message();
                                message.what=6;
                                handler.sendMessage(message);
                                Log.d("id",String.valueOf(answer.getID()));
                                Log.d("id",String.valueOf(answer.isNaive));

                            }
                        }
                        if (type.equals("accept")){
                         String postData="aid="+String.valueOf(ID)+"&token="+mToken+"&qid="+qid;
                        String result= NetWork.post("https://api.caoyue.com.cn/bihu/accept.php",postData);
                          JSONObject jsonObject=new JSONObject(result);
                       int   statusCode=jsonObject.getInt("status");
                           if (statusCode==200){
                               resultData=jsonObject.getString("info");
                               Log.d("result",jsonObject.getString("info"));
                               resultData=jsonObject.getString("info");
                               Message message=new Message();
                               message.what=4;
                               handler.sendMessage(message);}
                           else {
                                resultData=jsonObject.getString("info");
                                Message message=new Message();
                                message.what=6;
                                Log.d("result",result);
                               Log.d("status", jsonObject.getString("status"));
                                handler.sendMessage(message);
                               Log.d("id",String.valueOf(answer.getID()));
                               Log.d("id",String.valueOf(answer.isNaive));

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
                    String data="id="+String.valueOf(ID)+"&&token="+mToken+"&&type=2";
                    try {
                        if (type.equals("agree")){
                            String result= NetWork.post("https://api.caoyue.com.cn/bihu/cancelExciting.php",data);
                            JSONObject jsonObject=new JSONObject(result);
                            int   statusCode=jsonObject.getInt("status");
                            Log.d("1","2");
                            if (statusCode==200){
                                resultData=jsonObject.getString("info");
                                Log.d("1","3");
                                Log.d("result",jsonObject.getString("info"));
                                Log.d("token",mToken);
                                Message message=new Message();
                                message.what=3;
                                handler.sendMessage(message);}
                            else {
                                result=jsonObject.getString("info");
                                Message message=new Message();
                                message.what=6;
                                handler.sendMessage(message);


                            }

                        }
                        if (type.equals("disagree")){
                            String result= NetWork.post("https://api.caoyue.com.cn/bihu/cancelNaive.php",data);
                            JSONObject jsonObject=new JSONObject(result);
                            int   statusCode=jsonObject.getInt("status");
                            if (statusCode==200){
                                resultData=jsonObject.getString("info");
                                Log.d("result",jsonObject.getString("info"));
                                Message message=new Message();
                                message.what=2;
                                handler.sendMessage(message);}
                            else {
                                resultData=jsonObject.getString("info");
                                Message message=new Message();
                                message.what=6;
                                handler.sendMessage(message);
                                Log.d("id",String.valueOf(answer.getID()));
                                Log.d("id",String.valueOf(answer.isNaive));
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }
}
