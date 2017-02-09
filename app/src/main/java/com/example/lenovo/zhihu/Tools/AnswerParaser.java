package com.example.lenovo.zhihu.Tools;

import android.util.Log;

import com.example.lenovo.zhihu.data.Answer;
import com.example.lenovo.zhihu.data.MyQuestion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/2/6.
 */

public class AnswerParaser {
    private JSONArray getData;
    public  JSONArray dataToString(JSONObject jsonObject, String myData){
        try {
            getData=jsonObject.getJSONArray(myData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getData;

    }
    public static ArrayList<Answer> myAnswer(JSONArray jsonArray) {
        ArrayList<Answer>answers=new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                Answer answer = new Answer();

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                answer.setIsBest(jsonObject.getInt("best"));
                answer.setContent(jsonObject.getString("content"));
                answer.setExciting(jsonObject.getInt("exciting"));
                answer.setNaive(jsonObject.getInt("naive"));
                answer.setID(jsonObject.getInt("id"));
                answer.isNaive=jsonObject.getBoolean("is_naive");
                answer.isExitied=jsonObject.getBoolean("is_exciting");
                answer.setAuthorName(jsonObject.getString("authorName"));
                answers.add(answer);
                Log.d("1","ad");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return answers;
    }
}
