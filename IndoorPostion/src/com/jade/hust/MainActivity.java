package com.jade.hust;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;

import com.example.indoorpostion.R;

public class MainActivity extends Activity {
    private static final String Jade = "JADE";
	private WifiManager wifiManager;
	private List<ScanResult> WifiList;
	private List<Wifi> list;
	private  ListView listView;
	private  WifiAdapter wa;
	private final int SCAN_SUCCESS=1;
	private Thread tr;	
	private Handler myHandler; 
	private Boolean alive=true;
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.mian);
		myHandler = new Handler() {  
	        public void handleMessage(Message msg) {   
	             switch (msg.what) {   
	                  case SCAN_SUCCESS:
	                	 Log.i(Jade,"handle");           
	                	  WifiList=wifiManager.getScanResults();//����ɨ����	             		 
	             		 list=new ArrayList<Wifi>();//��list�洢��Ҫ��wifi��Ϣ
	             		 for(int i=0;i<WifiList.size();i++){
	             			 Wifi wifi=new Wifi();
	             			 wifi.setSSID(WifiList.get(i).SSID);
	             			 wifi.setLEVEL(WifiList.get(i).level);			 
	             			 list.add(wifi);
	             		 }
	             		 Collections.sort(list, new Comparator<Wifi>(){
	             			public int compare(Wifi arg0, Wifi arg1) {
	                            return arg0.getLEVEL()>arg1.getLEVEL()?-1:1;
	             			}
	             		 });
	                            	                            	                            
	             		 wa.addData(list);//�������list���ݣ����½���
	                       break;   
	             }   
	             super.handleMessage(msg);   
	        }   
	   };
	//	 checkWifiGps();
		 getWiFiInfo();

	}
	private void getWiFiInfo() {
		// TODO Auto-generated method stub
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);//���wifi������
		if(!wifiManager.isWifiEnabled()){
			wifiManager.setWifiEnabled(true);
		}
		//���listview�ؼ�
		listView=new ListView(this);
		listView = (ListView) findViewById(R.id.wifi_list);
		// wifiManager.startScan();//��ʼɨ��wifi
		
		 WifiList=wifiManager.getScanResults();//����ɨ����
		 
		 list=new ArrayList<Wifi>();//��list�洢��Ҫ��wifi��Ϣ
		 for(int i=0;i<WifiList.size();i++){
			 Wifi wifi=new Wifi();
			 wifi.setSSID(WifiList.get(i).SSID);
			 wifi.setLEVEL(WifiList.get(i).level);			 
			 list.add(wifi);
		 }

		 
		 wa=new WifiAdapter(this,list);//��ʼ��������
		listView.setAdapter(wa);//����������

	}
	
	
	class Run implements Runnable {
		public void run(){
			while (alive) {
				 wifiManager.startScan();//��ʼɨ��wifi
				 MainActivity.this.myHandler.sendEmptyMessage(SCAN_SUCCESS);//ɨ��ɹ���֪ͨ���̸߳���UI
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		 Log.e(Jade,"onResume"); 
		tr=new Thread(new Run());//�½��߳�ÿ��һ��ɨ��һ��wifi�ź�
		 tr.start();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		alive=false;//���ñ�־�ر��߳�
		 Log.e(Jade,"onPause");  
		//LocalBroadcastManager.getInstance(this).unregisterReceiver(mr);//ע���㲥
	}

}
