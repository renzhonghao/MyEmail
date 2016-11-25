package com.example.administrator.myemail;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MailEditActivity extends Activity implements OnClickListener {
	private EditText mail_to;
	private EditText mail_from;
	private EditText mail_topic;
	private EditText mail_content;

	private Button send;
	private ImageButton attachment;
	private GridView gridView;
	private GridViewAdapter<Attachment> adapter = null;

	 private static final int SUCCESS = 1;
	 private static final int FAILED = -1;
	 private ProgressDialog dialog;
	 HttpUtil util=new HttpUtil();
		private Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case SUCCESS:
					dialog.cancel();
						Toast.makeText(getApplicationContext(), "邮件发送成功", Toast.LENGTH_SHORT).show();
						//清空之前填写的数据
						mail_to.getText().clear();
						mail_topic.getText().clear();
						mail_content.getText().clear();
						//adapter=new GridViewAdapter<Attachment>(MailEditActivity.this);
//					}

					break;
				case FAILED:
					dialog.cancel();
					Toast.makeText(getApplicationContext(), "邮件发送失败", Toast.LENGTH_SHORT).show();
					break;
				}
				super.handleMessage(msg);
			}

	    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.email_writer);
		init();
	}

	/**
	 * 初始化
	 */
	private void init(){
		mail_to=(EditText) findViewById(R.id.mail_to);
		mail_from=(EditText) findViewById(R.id.mail_from);
		mail_topic=(EditText) findViewById(R.id.mail_topic);
		mail_content=(EditText) findViewById(R.id.content);
		send=(Button) findViewById(R.id.send);
		attachment=(ImageButton) findViewById(R.id.add_att);
		gridView=(GridView) findViewById(R.id.pre_view);

		mail_from.setText(MyApplication.info.getUserName());
		send.setOnClickListener(this);
		attachment.setOnClickListener(this);
//
		adapter = new GridViewAdapter<Attachment>(this);
		gridView.setAdapter(adapter);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.send:
			sendMail();
			break;
		}

	};

	/**
	 * 添加附件
	 */
	private void addAttachment() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("file/");
		startActivityForResult(intent, 1);

	}



	/**
	 * 设置邮件数据
	 */
	private void sendMail(){
		MyApplication.info.setAttachmentInfos(adapter.getList());
		MyApplication.info.setFromAddress(mail_from.getText().toString().trim());
		MyApplication.info.setSubject(mail_topic.getText().toString().trim());
		MyApplication.info.setContent(mail_content.getText().toString().trim());
		//收件人
		String str=mail_to.getText().toString().trim();
		String []recevers=str.split(",");
		for(int i=0;i<recevers.length;i++){
			if(recevers[i].startsWith("<")&&recevers[i].endsWith(">")){
				recevers[i]=recevers[i].substring(recevers[i].lastIndexOf("<")+1, recevers[i].lastIndexOf(">"));
			}
		}
		MyApplication.info.setReceivers(recevers);


		//发送邮件
		dialog=new ProgressDialog(this);
		dialog.setMessage("正在发送");
		dialog.show();

		/**
		 * 发送
		 */
		new Thread(){
			@Override
			public void run() {
				boolean flag=util.sendTextMail(MyApplication.info, MyApplication.session);
				Message msg=new Message();
				if(flag){
					msg.what=SUCCESS;
					handler.sendMessage(msg);
				}else{
					msg.what=FAILED;
					handler.sendMessage(msg);
				}
			}

		}.start();


	}

	/**
	 * 点击事件
	 * @author Administrator
	 *
	 */

	/**
	 * 返回
	 * @param v
	 */
	public void back(View v){
		Timer timer=new Timer();
		TimerTask timerTask=new TimerTask() {
			@Override
			public void run() {
				Intent intent1=new Intent(MailEditActivity.this,MainActivity.class);
				overridePendingTransition(1,3);
				startActivity(intent1);
				finish();
			}
		};
		timer.schedule(timerTask,10);
	}

	/**
	 * 返回按钮
	 */


}
