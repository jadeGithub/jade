package com.jade.hust;

import java.io.Serializable;
import java.util.List;

import com.jade.hust.entity.Wifi;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * ������ʱû��ʹ��
 * @author jadechen
 *
 */
public class WifiListenerService extends Service {
       private WifiManager wifim;
       private WifiReceiver wr;
       private List<ScanResult> WifiList;
       private List<Wifi> list;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
	//	Log.e("JADE","service onCreate..........");
		 wifim=(WifiManager) getSystemService(Context.WIFI_SERVICE);//���wifi������
		 wr= new WifiReceiver(); //�����㲥������������ϵͳwifi��Ϣ�仯
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(wr);//ע���㲥
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		IntentFilter filter=new IntentFilter();
		filter.addAction(wifim.SCAN_RESULTS_AVAILABLE_ACTION);
		registerReceiver(wr, filter);//ע��㲥
	
		new Thread(){//�½��̣߳�1�����һ��
			public void run(){
	        	while (true) {
					scanWifi();
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
	        }

			
		}.start();
		
		super.onStart(intent, startId);
	}
	
	private void scanWifi() {//ɨ��wifi
		// TODO Auto-generated method stub
		if(!wifim.isWifiEnabled())
			wifim.setWifiEnabled(true);
		wifim.startScan();
	}
	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}
	
   class	WifiReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
			
			WifiList = wifim.getScanResults();
			
			 for(int i=0;i<WifiList.size();i++){
				 Wifi wifi=new Wifi();
				 wifi.setSSID(WifiList.get(i).SSID);
				 wifi.setLEVEL(WifiList.get(i).level);
				 list.add(wifi);
			 }
			 
			Intent intentM=new Intent("android.intent.action.test");
			intentM.putExtra("list", (Serializable)list);
			LocalBroadcastManager.getInstance(context).sendBroadcast(intentM);//��ɨ����µĽ���㲥
			Log.e("JADE","service onReceive..........");
		}
		     
		
	}
	   
   }
}
