package com.yang.eric.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

	private boolean isCon;
	private ICalcAidlInterface iCalcAidlInterface;
	private final String TAG = getClass().getSimpleName();

	private Button button;
	private ServiceConnection connection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
			iCalcAidlInterface = ICalcAidlInterface.Stub.asInterface(iBinder);
			isCon = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			iCalcAidlInterface = null;
			isCon = false;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bindServiceInvoked();
		button = (Button) findViewById(R.id.btn_add);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(isCon){
					try {
						Toast.makeText(MainActivity.this, iCalcAidlInterface.Calc(1,2) + "",Toast.LENGTH_LONG).show();
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	private void bindServiceInvoked() {
		Intent intent = new Intent();
		intent.setAction("com.yang.eric.aidl");
		bindService(intent, connection, Context.BIND_AUTO_CREATE);
		Log.e(TAG, "bindService invoked !");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(connection);
	}
}
