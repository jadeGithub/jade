package com.jade.hust.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jade.hust.StoreWifi;

public class WifiDB extends SQLiteOpenHelper{
	public static final String DB_NAME="IndoorPositioning";
	private final boolean WRITABLE = true;
	private final boolean READABLE = false;
	public static  WifiDB wifiDb;
	private SQLiteDatabase db;
	public final static int DB_VERSION = 1;
	
	private WifiDB(Context context){
		super(context, DB_NAME, null, DB_VERSION);
	}

	public static WifiDB getInstance(Context context){
		if(wifiDb==null)
			wifiDb=new WifiDB(context);
		return wifiDb;
	}
	
	private void openDatabase(boolean boo){
		if(db == null && boo == WRITABLE){
			db = this.getWritableDatabase();
		}else if(db == null && boo == READABLE) {
			db = this.getReadableDatabase();
		}
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		StringBuffer positionTable=new StringBuffer();
		positionTable.append("create table position(_id integer primary key autoincrement,")
		.append("position text,").append("wifi_name text,").append("wifi_strengthM int);");
		db.execSQL(positionTable.toString()); 
	}

	public long save(StoreWifi swf) {
		//�����ݿ�
		openDatabase(WRITABLE);
		/**
		 * �����ݿ��в�������
		 * 1���ȴ���ContentValues����ͨ����ֵ�������ݺͶ�Ӧ�ֶ�ƥ��
		 * 2���ٰ�ContentValues������db.insert()������
		 */
		//�ȴ���һ��ContenValues�����Լ�ֵ����ʽ��������
		ContentValues cv = new ContentValues();
		//cv.put("_id", "");
		cv.put("position", swf.getPositon()); 
		cv.put("wifi_name", swf.getName());
		cv.put("wifi_strengthM", swf.getMid());
		//�ٽ�ContentValues������Database��.insert()�����У��Ӷ���ʽ�������ݿ�
		return db.insert("position", null, cv);
		
	}
	
	public HashMap search(String searchCondition){
		Cursor cursor = db.query("position", null, "wifi_name=?", new String[]{searchCondition}, null, null, null);  
		HashMap map = new HashMap();//����λ�ú��źŵ�ƽ��ǿ��
		while(cursor.moveToNext()){			
			map.put(cursor.getString(cursor.getColumnIndex("position")), cursor.getInt(cursor.getColumnIndex("wifi_strengthM")));		    
		}
		
		return map;
	}
	
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
