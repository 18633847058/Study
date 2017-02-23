package com.yang.eric.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class AIDLService extends Service {

	private ICalcAidlInterface.Stub calcAidlInterface = new ICalcAidlInterface.Stub() {

		@Override
		public int Calc(int a, int b) throws RemoteException {
			return a + b;
		}
	};
	public AIDLService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		return calcAidlInterface;
	}
}
