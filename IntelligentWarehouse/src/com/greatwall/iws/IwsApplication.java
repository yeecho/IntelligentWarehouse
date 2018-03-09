package com.greatwall.iws;

import java.util.ArrayList;
import java.util.HashSet;

import org.json.JSONArray;

import android.app.Application;

import com.greatwall.iws.bean.BoardBean;
import com.greatwall.iws.bean.WoBean;
import com.greatwall.iws.constant.Constant;

public class IwsApplication extends Application{
	
	//Ӧ�ù���״̬��Ĭ������״̬��
	private int status = Constant.STATUS_IDLE;
	//Ӧ�õ�ǰ��ʱ��JSONArray����
	private JSONArray ja = new JSONArray();
	
	//�������list
	public ArrayList<String> enters = new ArrayList<>();
	//���
	private BoardBean board = new BoardBean();
	//����list��������������
	private ArrayList<WoBean> woBeanList = new ArrayList<>();
	
	//��ȡ���
	public BoardBean getBoard() {
		return board;
	}
	//��ȡ����list
	public ArrayList<WoBean> getWoBeanList() {
		return woBeanList;
	}

	//�ݲ��������ù���
	private void setBoard(BoardBean board) {
		this.board = board;
	}
	//�ݲ��������ù���
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
