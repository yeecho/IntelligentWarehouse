package com.greatwall.iws;

import java.util.ArrayList;
import java.util.HashSet;

import org.json.JSONArray;

import android.app.Application;

import com.greatwall.iws.bean.BoardBean;
import com.greatwall.iws.bean.WoBean;
import com.greatwall.iws.constant.Constant;

public class IwsApplication extends Application{
	
	//应用工作状态（默认闲置状态）
	private int status = Constant.STATUS_IDLE;
	//应用当前临时的JSONArray数据
	private JSONArray ja = new JSONArray();
	
	//入库数据list
	public ArrayList<String> enters = new ArrayList<>();
	//面板
	private BoardBean board = new BoardBean();
	//工单list（不超过三条）
	private ArrayList<WoBean> woBeanList = new ArrayList<>();
	
	//获取面板
	public BoardBean getBoard() {
		return board;
	}
	//获取工单list
	public ArrayList<WoBean> getWoBeanList() {
		return woBeanList;
	}

	//暂不公开设置功能
	private void setBoard(BoardBean board) {
		this.board = board;
	}
	//暂不公开设置功能
	private void setWoBeanList(ArrayList<WoBean> woBeanList) {
		this.woBeanList = woBeanList;
	}

	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public ArrayList<String> getEnters() {
		return enters;
	}

	public void setEnters(ArrayList<String> enters) {
		this.enters = enters;
	}

	public JSONArray getJa() {
		return ja;
	}
	
	public void setJa(JSONArray ja) {
		this.ja = ja;
	}

}
