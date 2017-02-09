package com.example.lenovo.zhihu.Activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lenovo.zhihu.R;

public class Change extends AppCompatActivity {
    String mToken;
    EditText mNewPassword;
    TextView finsh;
    TextView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        mNewPassword=(EditText)findViewById(R.id.mNewPassword);
        finsh=(TextView)findViewById(R.id.finish);
        back=(TextView)findViewById(R.id.back);
        SharedPreferences sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
        mToken=sharedPreferences.getString("mToken","");
    }

    private void putNewPassword(){
        
    }
}
