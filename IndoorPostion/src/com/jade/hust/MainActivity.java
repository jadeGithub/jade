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
	             		 wa.addData(list);//�������list���ݣ����½���
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
		
		list=new ArrayList<Wifi>();//��list�洢��Ҫ��wifi��Ϣ
		ws=new WifiScan(this);//ɨ��wifi
		list=ws.getWiFiInfo();//��ȡwifi��Ϣ
		
		 wa=new WifiAdapter(this,list);//��ʼ��������
		listView.setAdapter(wa);//����������

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
		HashMap map_score = new HashMap();//�����Ӧ��λ�ô��
		for(int i=0;i<5;i++){//ȡ��ǰǰ5��wifi������SSID�ҵ���Ӧ��λ�ã�Ȼ��Ϊÿ��λ�ô�֡�
			HashMap map_result = new HashMap();//�����Ӧ��λ��
			Wifi wifi=list.get(i);		
			map_result=WifiDB.getInstance(this).search(wifi.getSSID());//���ҳ����а������wifi�źŵ�λ��
			
			//����map_result��Ϊÿ��λ�ô��
			Iterator iter = map_result.entrySet().iterator();
			while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key=(String) entry.getKey();
			int val = (Integer) entry.getValue();
			int score=-Math.abs(val-wifi.getLEVEL());//���Ժ�map�е�LEVEL����벢ȡ��
			if(map_score.get(key)==null){//�����ǰλ��û�д�֣���ѵ�ǰwifiΪ��ǰλ�õĴ�ִ���
				map_score.put(key, score);
			}
			else{ //�����ۼӵ�ǰwifiΪ��ǰλ�õĴ��
				int total=(Integer) map_score.get(key);
				map_score.put(key, score+total);		
			}
			}		
		}
		           //����map_score,�ҳ������ߵ�λ��
		        int max_score=-100; 
		        String key = null;
				Iterator iter = map_score.entrySet().iterator();
				while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();				
				int val = (Integer) entry.getValue();			
				if(max_score<val)
					{
					max_score=val;//�ҳ���߷�
				    key =(String) entry.getKey();//�ҳ��÷���ߵ�λ��
				    Log.i(Jade, key+"..."+val);
					}
				}
				
				new AlertDialog.Builder(this).setMessage("your position is:"+key)
				.setPositiveButton("sure", null).show();
		
	}

	class Run implements Runnable {
		public void run(){
			while (alive) {
				// wifiManager.startScan();//��ʼɨ��wifi
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
		 alive=true;
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
