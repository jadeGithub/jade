package com.jade.hust;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.indoorpostion.R;
import com.jade.hust.AddPositionActivity.SaveListener;
import com.jade.hust.db.WifiDB;

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
	private WifiScan ws;
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.mian);
		myHandler = new Handler() {  
	        public void handleMessage(Message msg) {   
	             switch (msg.what) {   
	                  case SCAN_SUCCESS:
	                	 // Log.i(Jade,"handle");
	                	  ws=new WifiScan(MainActivity.this);
	              		list=ws.getWiFiInfo();          	                            	                            
	             		 wa.addData(list);//获得最新list数据，更新界面
	                       break;   
	             }   
	             super.handleMessage(msg);   
	        }   
	   };
	
		 getWiFiInfo();

	}
	private void getWiFiInfo() {
		listView=new ListView(this);
		listView =(ListView) findViewById(R.id.wifi_list);
		
		list=new ArrayList<Wifi>();//用list存储需要的wifi信息
		ws=new WifiScan(this);//扫描wifi
		list=ws.getWiFiInfo();//获取wifi信息
		
		 wa=new WifiAdapter(this,list);//初始化适配器
		listView.setAdapter(wa);//设置适配器

	}
	
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_add) {
        	startActivity(new Intent(MainActivity.this,AddPositionActivity.class));
            return true;
        }
        else if(id == R.id.action_positioning){
        //	startService(new Intent(this,PositionService.class));
        	getPosition();
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }
	
	private void getPosition() {
		// TODO Auto-generated method stub
		HashMap map_score = new HashMap();//存放相应的位置打分
		for(int i=0;i<5;i++){//取当前前5个wifi，根据SSID找到相应的位置，然后为每个位置打分。
			HashMap map_result = new HashMap();//存放相应的位置
			Wifi wifi=list.get(i);		
			map_result=WifiDB.getInstance(this).search(wifi.getSSID());//查找出所有包含这个wifi信号的位置
			
			//遍历map_result，为每个位置打分
			Iterator iter = map_result.entrySet().iterator();
			while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key=(String) entry.getKey();
			int val = (Integer) entry.getValue();
			int score=-Math.abs(val-wifi.getLEVEL());//测试和map中的LEVEL求距离并取反
			if(map_score.get(key)==null){//如果当前位置没有打分，则把当前wifi为当前位置的打分存入
				map_score.put(key, score);
			}
			else{ //否则，累加当前wifi为当前位置的打分
				int total=(Integer) map_score.get(key);
				map_score.put(key, score+total);		
			}
			}		
		}
		           //遍历map_score,找出打分最高的位置
		        int max_score=-100; 
		        String key = null;
				Iterator iter = map_score.entrySet().iterator();
				while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();				
				int val = (Integer) entry.getValue();			
				if(max_score<val)
					{
					max_score=val;//找出最高分
				    key =(String) entry.getKey();//找出得分最高的位置
				    Log.i(Jade, key+"..."+val);
					}
				}
				
				new AlertDialog.Builder(this).setMessage("your position is:"+key)
				.setPositiveButton("sure", null).show();
		
	}

	class Run implements Runnable {
		public void run(){
			while (alive) {
				// wifiManager.startScan();//开始扫描wifi
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
		 alive=true;
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
