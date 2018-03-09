package com.greatwall.iws.bean;

import android.util.Log;

public class BoardBean {
	
	private WoBean red = new WoBean();
	private WoBean blue = new WoBean();
	private WoBean green = new WoBean();
	private String white;
	
	public WoBean getRed() {
		return red;
	}
	public void setRed(WoBean red) {
		this.red = red;
	}
	public WoBean getBlue() {
		return blue;
	}
	public void setBlue(WoBean blue) {
		this.blue = blue;
	}
	public WoBean getGreen() {
		return green;
	}
	public void setGreen(WoBean green) {
		this.green = green;
	}
	public String getWhite() {
		return white;
	}
	public void setWhite(String white) {
		this.white = white;
	}
	
	public int isContain (String gdh){
		Log.d("BoardBean", "contains");
		if (red.getGdh().equals(gdh)) {
			return 0;
		}else if(blue.getGdh().equals(gdh)) {
			return 1;
		}else if(green.getGdh().equals(gdh)) {
			return 2;
		}
		return -1;
	}
	public int addWoBean(WoBean woBean) {
		if (red.isEmpty()) {
			red = woBean;
			return 0;
		}else if (blue.isEmpty()) {
			blue = woBean;
			return 1;
		}else if (green.isEmpty()){
			green = woBean;
			return 2;
		}
		return -1;
	}
	public WoBean getWoBean(int index){
		if (index == 0) {
			return red;
		}else if (index == 1) {
			return blue;
		}else if (index == 2) {
			return green;
		}
		return null;
	}
	public WoBean setWoBean(int index, WoBean woBean){
		if (index == 0) {
			red = woBean;
		}else if (index == 1) {
			blue = woBean;
		}else if (index == 2) {
			green = woBean;
		}
		return null;
	}
}
