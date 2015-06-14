package com.jade.hust;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.indoorpostion.R;
import com.jade.hust.db.WifiDB;

public class AddPositionActivity extends Activity {
    private static final String Jade = "JADE";
    private List<Wifi> list1;
    private List<HashMap> list2;
    private WifiScan ws;
    private ListView showlist;
    private AddWifiAdapter awa;
    private Handler myHandler;
    private final int SCAN_SUCCESS=1;
    private Boolean alive=true;
    private Thread tr;
    private int count=1;
    private Button bt;
    private EditText et;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.add_position);
		myHandler = new Handler() {  
	        public void handleMessage(Message msg) {   
	             switch (msg.what) {   
	                  case SCAN_SUCCESS:
	                	 // Log.i(Jade,"handle"+list1.get(0).getSSID());
	                	  HashMap hap=new HashMap();
	              		list1=ws.getWiFiInfo();//获取wifi信号
	              		for(int i=1;i<6;i++){			
	              			hap.put("WIFI"+i, list1.get(i-1));//从第0个开始		
	              		}
	              		list2.set(list2.size()-1, hap);	              			              		           	                            
	             		 awa.addData(list2);//获得最新list数据，更新界面
	                       break;   
	             }   
	             super.handleMessage(msg);   
	        }   
	   };
	   
		list1=new ArrayList<Wifi>();
		list2=new ArrayList<HashMap>();
		ws= new WifiScan(this);
		et=(EditText) findViewById(R.id.position_edit);
		 bt=(Button) findViewById(R.id.save);
		bt.setText("save(0)");
		showlist=new ListView(this);
		showlist=(ListView) findViewById(R.id.wifi_list_add);
		
		
		HashMap hap=new HashMap();
		list1=ws.getWiFiInfo();
		for(int i=1;i<6;i++){	//将信号前度好的前5个放到map中		
			hap.put("WIFI"+i, list1.get(i-1));		
		}
		
		list2.add(hap);//将map填入list
		awa=new AddWifiAdapter(this,list2);
		showlist.setAdapter(awa);//适配
		
		bt.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(TextUtils.isEmpty(et.getText()))
					new AlertDialog.Builder(AddPositionActivity.this).setMessage("input position first!")
					.setPositiveButton("sure", null).show();
				else{
				
				save();
				}
				
			}
			
		});
	}
	
	
	protected void save() {
		// TODO Auto-generated method stub
		
		if(count<5){
			show();		
			 bt.setText("save("+count+")");
			 count++;
		}	     
		else {
			bt.setClickable(false);
			new AlertDialog.Builder(this).setMessage("save successfully")
			.setPositiveButton("sure", new SaveListener()).show();
		}
	}
	
   class SaveListener implements android.content.DialogInterface.OnClickListener{

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub\
		HashMap map_put=new HashMap();//将要存储的StroeWifi放在map_put中
		 Log.i(Jade,"00000000000"+list2.size());
		for(int i=0;i<list2.size();i++){
			HashMap map_get=new HashMap();
			map_get=list2.get(i);//从list中取出含有5个wifi的map					 
			for(int j=1;j<6;j++){
				StoreWifi swf=new StoreWifi();
				swf.setPositon(et.getText().toString());
			Wifi wifi1=(Wifi) map_get.get("WIFI"+j);//循环从map中取出wifi来
					
			if(map_put.get(wifi1.getSSID())==null){//判断这个wifi是否保存，如果没有保存这个wifi，则保存				
				swf.setName(wifi1.getSSID());
			    swf.setMax(wifi1.getLEVEL());			   
			    swf.setMin(wifi1.getLEVEL());
			}
			else {//如果有，则更新map_put中的数据				
				swf=(StoreWifi) map_put.get(wifi1.getSSID());//获取原来保存的swf数据
				swf.setName(wifi1.getSSID());
				if(wifi1.getLEVEL()>swf.getMax())
					swf.setMax(wifi1.getLEVEL());
				else if(wifi1.getLEVEL()<swf.getMin())
				      swf.setMin(wifi1.getLEVEL());
				swf.setMid((swf.getMax()+swf.getMin())/2);
			}
			map_put.put(swf.getName(), swf);	//将最新的数据保存		
			}
			Log.i(Jade,"jies......");
		}
		//遍历map
		Iterator iter = map_put.entrySet().iterator();
		while (iter.hasNext()) {
		Map.Entry entry = (Map.Entry) iter.next();
		StoreWifi val = (StoreWifi) entry.getValue();
		WifiDB.getInstance(AddPositionActivity.this).save(val);
		Log.i(Jade, val.getPositon()+val.getName()+";"+val.getMax()+";"+val.getMin());
		}
		
		finish();
	}
	   
   }
	protected void show() {
		// TODO Auto-generated method stub
		HashMap hap=new HashMap();
		list1=ws.getWiFiInfo();
		for(int i=0;i<5;i++){			
			hap.put("WIFI"+i, list1.get(i));		
			
		}
		list2.add(hap);
	}
	
	class Run implements Runnable {
		public void run(){
			while (alive) {
				// wifiManager.startScan();//开始扫描wifi
				AddPositionActivity.this.myHandler.sendEmptyMessage(SCAN_SUCCESS);//扫描成功，通知主线程更新UI
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
		// Log.e(Jade,"onResume"); 
		 alive=true;
		tr=new Thread(new Run());//新建线程每隔一秒扫描一次wifi信号
		 tr.start();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		alive=false;//利用标志关闭线程
		// Log.e(Jade,"onPause");  
		//LocalBroadcastManager.getInstance(this).unregisterReceiver(mr);//注销广播
	}
}
