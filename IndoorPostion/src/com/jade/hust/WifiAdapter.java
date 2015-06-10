package com.jade.hust;

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.indoorpostion.R;

public class WifiAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<Wifi> list;
	public WifiAdapter(Context context, List<Wifi> Slist){
		this.inflater = LayoutInflater.from(context);
		//Log.e("JADE","ai.........");
		list=Slist;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int positon) {
		// TODO Auto-generated method stub
		return list.get(positon);
	}

	@Override
	public long getItemId(int positon) {
		// TODO Auto-generated method stub
		return positon;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
	  //  Log.i("JADE","GETview"+list.get(position).get("SSID"));
	  //  Log.i("JADE","GETview"+list.get(position).get("LEVEL"));
	    
	    HoldView hv=null;
		View view=convertView;
		Wifi wifi=list.get(position);
		if(view==null)
		{
			view=inflater.inflate(R.layout.list_item, null);
			hv=new HoldView();
			hv.imageView=(ImageView)view.findViewById(R.id.image);
			hv.wifi_name= (TextView) view.findViewById(R.id.wifi_name);
			hv.wifi_strenth= (TextView) view.findViewById(R.id.singl_wifi_strenth);
			view.setTag(hv);
		}
		else hv=(HoldView) view.getTag();
			
		//Log.e("JADE","wifilist="+list.get(0).getLEVEL());
		hv.wifi_name.setText(wifi.getSSID());
		
		hv.wifi_strenth.setText(String.valueOf(wifi.getLEVEL()+100));	
		
		/*
		 *�õ����ź�ǿ��ֵ��һ��0��-100������ֵ����һ��int�����ݣ�����0��-50��ʾ�ź���ã�
		 *-50��-70��ʾ�ź�ƫ�С��-70��ʾ���п������Ӳ��ϻ��ߵ��ߡ� 
		 */
		int level= Math.abs(wifi.getLEVEL());
		if (level > 100) {
			hv.imageView.setImageResource(R.drawable.wifi0);
		} else if (level > 90) {
			hv.imageView.setImageResource(R.drawable.wifi0);
			} else if (level > 80) {
				hv.imageView.setImageResource(R.drawable.wifi1);
			} else if (level > 70) {
				hv.imageView.setImageResource(R.drawable.wifi1);
			} else if (level > 60) {
				hv.imageView.setImageResource(R.drawable.wifi2);
			} else if (level > 50) {
				hv.imageView.setImageResource(R.drawable.wifi3);
			} else {
				hv.imageView.setImageResource(R.drawable.wifi3);
			}
		return view;
	}
   static class HoldView{
	   TextView wifi_name,wifi_strenth;
	   ImageView imageView;
   }
   
   public void addData(List<Wifi> list){
	   this.list=list;
	   notifyDataSetChanged();
   }
}
