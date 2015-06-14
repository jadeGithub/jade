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
	              		list1=ws.getWiFiInfo();//��ȡwifi�ź�
	              		for(int i=1;i<6;i++){			
	              			hap.put("WIFI"+i, list1.get(i-1));//�ӵ�0����ʼ		
	              		}
	              		list2.set(list2.size()-1, hap);	              			              		           	                            
	             		 awa.addData(list2);//�������list���ݣ����½���
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
		for(int i=1;i<6;i++){	//���ź�ǰ�Ⱥõ�ǰ5���ŵ�map��		
			hap.put("WIFI"+i, list1.get(i-1));		
		}
		
		list2.add(hap);//��map����list
		awa=new AddWifiAdapter(this,list2);
		showlist.setAdapter(awa);//����
		
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
		HashMap map_put=new HashMap();//��Ҫ�洢��StroeWifi����map_put��
		 Log.i(Jade,"00000000000"+list2.size());
		for(int i=0;i<list2.size();i++){
			HashMap map_get=new HashMap();
			map_get=list2.get(i);//��list��ȡ������5��wifi��map					 
			for(int j=1;j<6;j++){
				StoreWifi swf=new StoreWifi();
				swf.setPositon(et.getText().toString());
			Wifi wifi1=(Wifi) map_get.get("WIFI"+j);//ѭ����map��ȡ��wifi��
					
			if(map_put.get(wifi1.getSSID())==null){//�ж����wifi�Ƿ񱣴棬���û�б������wifi���򱣴�				
				swf.setName(wifi1.getSSID());
			    swf.setMax(wifi1.getLEVEL());			   
			    swf.setMin(wifi1.getLEVEL());
			}
			else {//����У������map_put�е�����				
				swf=(StoreWifi) map_put.get(wifi1.getSSID());//��ȡԭ�������swf����
				swf.setName(wifi1.getSSID());
				if(wifi1.getLEVEL()>swf.getMax())
					swf.setMax(wifi1.getLEVEL());
				else if(wifi1.getLEVEL()<swf.getMin())
				      swf.setMin(wifi1.getLEVEL());
				swf.setMid((swf.getMax()+swf.getMin())/2);
			}
			map_put.put(swf.getName(), swf);	//�����µ����ݱ���		
			}
			Log.i(Jade,"jies......");
		}
		//����map
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
				// wifiManager.startScan();//��ʼɨ��wifi
				AddPositionActivity.this.myHandler.sendEmptyMessage(SCAN_SUCCESS);//ɨ��ɹ���֪ͨ���̸߳���UI
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
		tr=new Thread(new Run());//�½��߳�ÿ��һ��ɨ��һ��wifi�ź�
		 tr.start();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		alive=false;//���ñ�־�ر��߳�
		// Log.e(Jade,"onPause");  
		//LocalBroadcastManager.getInstance(this).unregisterReceiver(mr);//ע���㲥
	}
}
