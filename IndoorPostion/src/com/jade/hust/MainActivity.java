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
	                	  WifiList=wifiManager.getScanResults();//返回扫描结果	             		 
	             		 list=new ArrayList<Wifi>();//用list存储需要的wifi信息
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
	                            	                            	                            
	             		 wa.addData(list);//获得最新list数据，更新界面
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
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);//获得wifi管理器
		if(!wifiManager.isWifiEnabled()){
			wifiManager.setWifiEnabled(true);
		}
		//获得listview控件
		listView=new ListView(this);
		listView = (ListView) findViewById(R.id.wifi_list);
		// wifiManager.startScan();//开始扫描wifi
		
		 WifiList=wifiManager.getScanResults();//返回扫描结果
		 
		 list=new ArrayList<Wifi>();//用list存储需要的wifi信息
		 for(int i=0;i<WifiList.size();i++){
			 Wifi wifi=new Wifi();
			 wifi.setSSID(WifiList.get(i).SSID);
			 wifi.setLEVEL(WifiList.get(i).level);			 
			 list.add(wifi);
		 }

		 
		 wa=new WifiAdapter(this,list);//初始化适配器
		listView.setAdapter(wa);//设置适配器

	}
	
	
	class Run implements Runnable {
		public void run(){
			while (alive) {
				 wifiManager.startScan();//开始扫描wifi
				 MainActivity.this.myHandler.sendEmptyMessage(SCAN_SUCCESS);//扫描成功，通知主线程更新UI
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
		tr=new Thread(new Run());//新建线程每隔一秒扫描一次wifi信号
		 tr.start();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		alive=false;//利用标志关闭线程
		 Log.e(Jade,"onPause");  
		//LocalBroadcastManager.getInstance(this).unregisterReceiver(mr);//注销广播
	}

}
