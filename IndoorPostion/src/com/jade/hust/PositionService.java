package com.jade.hust;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class PositionService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(this).setMessage("service Start").show();
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
   
}
