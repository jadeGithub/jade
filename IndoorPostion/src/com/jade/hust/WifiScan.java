package com.jade.hust;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.jade.hust.entity.Wifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;


public class WifiScan {
    private static final String Jade = "JADE";
	private WifiManager wifiManager;
	private List<ScanResult> WifiList;
	private List<Wifi> list;
	private Context context;
	
	public WifiScan(Context context){
		this.context=context;
	}
	public List<Wifi> getWiFiInfo() {
		// TODO Auto-generated method stub
		wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);//获得wifi管理器
		if(!wifiManager.isWifiEnabled()){
			wifiManager.setWifiEnabled(true);
		}	
		 wifiManager.startScan();//开始扫描wifi
		
		 WifiList=wifiManager.getScanResults();//返回扫描结果
		 
		 list=new ArrayList<Wifi>();//用list存储需要的wifi信息
		 for(int i=0;i<WifiList.size();i++){
			 Wifi wifi=new Wifi();
			 wifi.setSSID(WifiList.get(i).SSID);
			 wifi.setLEVEL(WifiList.get(i).level);	
			 wifi.setBSSID(WifiList.get(i).BSSID);
			 list.add(wifi);
		 }
		 Collections.sort(list, new Comparator<Wifi>(){
  			public int compare(Wifi arg0, Wifi arg1) {
                 return arg0.getLEVEL()>arg1.getLEVEL()?-1:1;
  			}
  		 });
		return list;

	}

}
