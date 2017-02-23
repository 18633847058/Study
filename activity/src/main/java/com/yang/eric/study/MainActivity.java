package com.yang.eric.study;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

	private static final String TAG = "MainActivity";
	private static final int MSG_SUM = 0x110;

	private Button button;
	private TextView textView;
	private LinearLayout linearLayout;

	private Messenger service;
	private boolean isCon;

	private Messenger messenger = new Messenger(new Handler(){
		@Override
		public void handleMessage(Message msgFromServer) {
			switch (msgFromServer.what){
				case MSG_SUM:
					TextView textView = (TextView) linearLayout.findViewById(msgFromServer.arg1);
					textView.setText(textView.getText() + "=>" + msgFromServer.arg2);
					break;
			}
			super.handleMessage(msgFromServer);
		}
	});
	private ServiceConnection connection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
			service = new Messenger(iBinder);
			isCon = true;
			textView.setText("connected!");
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			service = null;
			isCon = false;
			textView.setText("disconnected!");
		}
	};
	private int id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bindServiceInvoked();

		textView = (TextView) findViewById(R.id.id_tv_callback);
		linearLayout = (LinearLayout) findViewById(R.id.id_ll_container);
		button = (Button) findViewById(R.id.id_btn_add);

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				int a = id++;
				int b = (int) (Math.random()*100);

				TextView tv = new TextView(MainActivity.this);
				tv.setText(a + " + " + b + " = caculating ...");
				tv.setId(a);
				linearLayout.addView(tv);

				Message msgFromClient = Message.obtain(null, MSG_SUM, a, b);
				msgFromClient.replyTo = messenger;
				if(isCon){
					try {
						service.send(msgFromClient);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	private void bindServiceInvoked() {
		Intent intent = new Intent();
		intent.setAction("com.yang.eric.service");
		bindService(intent, connection, Context.BIND_AUTO_CREATE);
		Log.e(TAG, "bindService invoked !");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(connection);
	}
}
