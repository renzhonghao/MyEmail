package com.example.administrator.myemail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Button send;
    Button receive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        send=(Button)findViewById(R.id.button_send);
        receive=(Button)findViewById(R.id.button_receive);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timer timer=new Timer();
                TimerTask timerTask=new TimerTask() {
                    @Override
                    public void run() {
                        Intent intent1=new Intent(MainActivity.this,MailEditActivity.class);
                        overridePendingTransition(1,3);
                        startActivity(intent1);
                        finish();
                    }
                };
                timer.schedule(timerTask,10);
            }
        });
//        receive.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Timer timer=new Timer();
//                TimerTask timerTask=new TimerTask() {
//                    @Override
//                    public void run() {
//                        Intent intent1=new Intent(MainActivity.this,MailBoxActivity.class);
//                        overridePendingTransition(1,3);
//                        startActivity(intent1);
//                        finish();
//                    }
//                };
//                timer.schedule(timerTask,10);
//            }
//        });
    }
}
