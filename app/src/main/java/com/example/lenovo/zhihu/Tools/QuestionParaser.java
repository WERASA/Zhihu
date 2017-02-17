package com.example.lenovo.zhihu.Tools;

import com.example.lenovo.zhihu.Activities.Question;
import com.example.lenovo.zhihu.R;
import com.example.lenovo.zhihu.data.MyQuestion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/1/31.
 */

public class QuestionParaser {

    private JSONArray getData;
public  JSONArray dataToString(JSONObject jsonObject,String myData){
    try {
        getData=jsonObject.getJSONArray(myData);
    } catch (JSONException e) {
        e.printStackTrace();
    }
     return getData;

}
    public static ArrayList<MyQuestion> myQuestions(JSONArray jsonArray) {
        ArrayList<MyQuestion>myQuestions=new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                MyQuestion question = new MyQuestion();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                question.setTitle(jsonObject.getString("title"));
                question.setAuthorName(jsonObject.getString("authorName"));
                question.setAnswerCount(Integer.parseInt(jsonObject.getString("answerCount")));
                question.setContent(jsonObject.getString("content"));
                question.setExcitingCount(Integer.parseInt(jsonObject.getString("exciting")));
                question.setNaiveCount(Integer.parseInt(jsonObject.getString("naive")));
                question.setId(jsonObject.getInt("id"));
                question.isExciting=(jsonObject.getBoolean("is_exciting"));
                question.isNaive=(jsonObject.getBoolean("is_naive"));
                 question.setUserHeadUrl(jsonObject.getString("authorAvatar"));
                myQuestions.add(question);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return myQuestions;
    }
}
