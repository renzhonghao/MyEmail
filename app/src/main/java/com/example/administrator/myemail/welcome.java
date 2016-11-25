package com.example.administrator.myemail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/11/14.
 */

public class welcome extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        Timer timer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                Intent intent1=new Intent(welcome.this,LoginActivity.class);
                overridePendingTransition(1,3);
                startActivity(intent1);
                finish();
            }
        };
        timer.schedule(timerTask,1000*2);//等待三秒


    }
}

