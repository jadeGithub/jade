package com.jade.hust;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.indoorpostion.R;
import com.jade.hust.entity.Wifi;

public class AddWifiAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<HashMap> list;
	public AddWifiAdapter(Context context, List<HashMap> Slist){
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
	  // Log.i("JADE","GETview............");
	  //  Log.i("JADE","GETview"+list.get(position).get("LEVEL"));
	    
	    HoldView hv=null;
		View view=convertView;
		HashMap map=list.get(position);
		if(view==null)
		{
			view=inflater.inflate(R.layout.list_item_add, null);
			hv=new HoldView();
			hv.wifi1= (TextView) view.findViewById(R.id.wifi1);
			hv.wifi2= (TextView) view.findViewById(R.id.wifi2);
			hv.wifi3= (TextView) view.findViewById(R.id.wifi3);
			hv.wifi4= (TextView) view.findViewById(R.id.wifi4);
			hv.wifi5= (TextView) view.findViewById(R.id.wifi5);
			
			hv.wifi_strength_1= (TextView) view.findViewById(R.id.wifi_strength_1);
			hv.wifi_strength_2= (TextView) view.findViewById(R.id.wifi_strength_2);
			hv.wifi_strength_3= (TextView) view.findViewById(R.id.wifi_strength_3);
			hv.wifi_strength_4= (TextView) view.findViewById(R.id.wifi_strength_4);
			hv.wifi_strength_5= (TextView) view.findViewById(R.id.wifi_strength_5);
			
			view.setTag(hv);
		}
		else hv=(HoldView) view.getTag();
			
		
		hv.wifi_strength_1.setText((String.valueOf(((Wifi) map.get("WIFI1")).getLEVEL())));
		hv.wifi_strength_2.setText((String.valueOf(((Wifi) map.get("WIFI2")).getLEVEL())));
		hv.wifi_strength_3.setText((String.valueOf(((Wifi) map.get("WIFI3")).getLEVEL())));
		hv.wifi_strength_4.setText((String.valueOf(((Wifi) map.get("WIFI4")).getLEVEL())));
		hv.wifi_strength_5.setText((String.valueOf(((Wifi) map.get("WIFI5")).getLEVEL())));
			
		hv.wifi1.setText(String.valueOf(((Wifi) map.get("WIFI1")).getSSID()));
		hv.wifi2.setText(String.valueOf(((Wifi) map.get("WIFI2")).getSSID()));
		hv.wifi3.setText(String.valueOf(((Wifi) map.get("WIFI3")).getSSID()));
		hv.wifi4.setText(String.valueOf(((Wifi) map.get("WIFI4")).getSSID()));
		hv.wifi5.setText(String.valueOf(((Wifi) map.get("WIFI5")).getSSID()));
		
		//Log.e("JADE","wifilist=,.............."+String.valueOf(((Wifi) map.get("WIFI1")).getSSID()));
		return view;
	}
   static class HoldView{
	   TextView wifi1,wifi2,wifi3,wifi4,wifi5;
	   TextView wifi_strength_2,wifi_strength_3,wifi_strength_4,wifi_strength_1,wifi_strength_5;
	   
   }
   
   public void addData(List<HashMap> list){
	   this.list=list;
	   notifyDataSetChanged();
   }
}
