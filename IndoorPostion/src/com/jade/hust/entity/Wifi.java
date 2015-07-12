package com.jade.hust.entity;

public class Wifi {
  private String SSID;//wifi名称
  private String BSSID;//wifi名称
  private int LEVEL;//wifi强度
  
public String getSSID() {
	return SSID;
}
public void setSSID(String sSID) {
	SSID = sSID;
}
public int getLEVEL() {
	return LEVEL;
}
public void setLEVEL(int lEVEL) {
	LEVEL = lEVEL;
}
public String getBSSID() {
	return BSSID;
}
public void setBSSID(String bSSID) {
	BSSID = bSSID;
}
  

}
