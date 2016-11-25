package com.example.administrator.myemail;

//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
///**
// * Created by Administrator on 2016/11/17.
// */
//
//public class LoginActivity extends AppCompatActivity {
//    Button b;
//    sendMessage s=new sendMessage();
//    Thread t1=new Thread(s);
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.login);
//        b=(Button) findViewById(R.id.button);
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                s.start();
//
//            }
//        });
//    }
//
//    class
//
//    sendMessage extends Thread {
//        @Override
//        public void run() {
//            //super.run();
//            try {
//                MailSenderInfo mailInfo = new MailSenderInfo();
//                mailInfo.setMailServerHost("smtp.163.com");
//                mailInfo.setMailServerPort("25");
//                mailInfo.setValidate(true);
//                mailInfo.setUserName("m18323251917@163.com");
//                mailInfo.setPassword("ren960316");//您的邮箱密码
////                mailInfo.setUserName("3100584410@qq.com");
////                mailInfo.setPassword("cvybmanizkpqdcgf");//您的邮箱密码
//                mailInfo.setFromAddress("m18323251917@163.com");
////                mailInfo.setFromAddress("3100584410@qq.com");
//                mailInfo.setToAddress("1377756192@qq.com");
//                mailInfo.setSubject("Android首次");
//                mailInfo.setContent("没有内容");
//                //这个类主要来发送邮件
//                SimpleMailSender sms = new SimpleMailSender();
//                sms.sendTextMail(mailInfo);//发送文体格式
//                sms.sendHtmlMail(mailInfo);//发送html格式
//            } catch (Exception e) {
//                Log.d("",e.toString());
//                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG);
//            }
//        }
//    }
//}


import com.example.administrator.myemail.EmailFormatUtil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
public class LoginActivity extends Activity implements TextWatcher, OnClickListener{
    private EditText emailAddress;
    private EditText password;
    private Button clearAddress;
    private Button clearPassword;
    private Button emailLogin;
    private ProgressDialog dialog;
    private SharedPreferences sp;
    private CheckBox cb_remenber;
    private CheckBox cb_autologin;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(MyApplication.session==null){
                dialog.dismiss();
                Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
            }else{
                dialog.dismiss();
                Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            super.handleMessage(msg);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        sp=getSharedPreferences("config", Context.MODE_APPEND);
        initView();
        isRemenberPwd();
    }

    /**
     * 初始化数据
     */
    private void initView(){
        emailAddress=(EditText) findViewById(R.id.emailAddress);
        password=(EditText) findViewById(R.id.password);
        clearAddress=(Button) findViewById(R.id.clear_address);
        clearPassword=(Button) findViewById(R.id.clear_password);
        emailLogin=(Button) findViewById(R.id.login_btn);
        cb_remenber=(CheckBox) findViewById(R.id.remenberPassword);
        cb_autologin=(CheckBox) findViewById(R.id.autoLogin);

        clearAddress.setOnClickListener(this);
        clearPassword.setOnClickListener(this);
        emailAddress.addTextChangedListener(this);
        emailLogin.setOnClickListener(this);

        cb_remenber.setOnClickListener(this);
        cb_autologin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear_address:
                emailAddress.setText("");
                break;
            case R.id.clear_password:
                password.setText("");
                break;
            case R.id.login_btn:
                loginEmail();
                break;
            case R.id.remenberPassword:
                remenberPwd();
                break;
            case R.id.autoLogin:
                break;
        }
    }

    /**
     * 是否记住密码
     */
    private void isRemenberPwd(){
        boolean isRbPwd=sp.getBoolean("isRbPwd", false);
        if(isRbPwd){
            String addr=sp.getString("address", "");
            String pwd=sp.getString("password", "");
            emailAddress.setText(addr);
            password.setText(pwd);
            cb_remenber.setChecked(true);
        }
    }

    /**
     * 记住密码
     */
    private void remenberPwd(){
        boolean isRbPwd=sp.getBoolean("isRbPwd", false);
        if(isRbPwd){
            sp.edit().putBoolean("isRbPwd", false).commit();
            cb_remenber.setChecked(false);
        }else{
            sp.edit().putBoolean("isRbPwd", true).commit();
            sp.edit().putString("address", emailAddress.getText().toString().trim()).commit();
            sp.edit().putString("password", password.getText().toString().trim()).commit();
            cb_remenber.setChecked(true);

        }
    }

    /**
     * 登入邮箱
     */
    private void loginEmail(){
        String address=emailAddress.getText().toString().trim();
        String pwd=password.getText().toString().trim();
        if(TextUtils.isEmpty(address)){
            Toast.makeText(LoginActivity.this, "地址不能为空", Toast.LENGTH_SHORT).show();
            return;
        }else{
            if(TextUtils.isEmpty(pwd)){
                Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        /**
         * 校验邮箱格式
         */
        if(!EmailFormatUtil.emailFormat(address)){
            Toast.makeText(LoginActivity.this, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
        }else{
            String host="smtp."+address.substring(address.lastIndexOf("@")+1);
            MyApplication.info.setMailServerHost(host);
            MyApplication.info.setMailServerPort("25");
            MyApplication.info.setUserName(address);
            MyApplication.info.setPassword(pwd);
            MyApplication.info.setValidate(true);
            /**
             * 进度条
             */
            dialog=new ProgressDialog(LoginActivity.this);
            dialog.setMessage("正在登入，请稍后");
            dialog.show();
            /**
             * 访问网络
             */
            new Thread(){
                @Override
                public void run() {
                    //登入操作
                    HttpUtil util=new HttpUtil();
                    MyApplication.session=util.login();
                    Message message=handler.obtainMessage();
                    message.sendToTarget();
                }

            }.start();
        }
    }


    /**
     * 文本监听事件
     *
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(!TextUtils.isEmpty(s)){
            clearAddress.setVisibility(View.VISIBLE);
        }else{
            clearAddress.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }


}
