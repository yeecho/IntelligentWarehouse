package com.greatwall.iws.bean;

import java.util.ArrayList;

//����Bean
public class WoBean {
	private String gdh = "";		//������
	private String cgy = "";		//�ֹ�Ա
	private String process = "";	//���Ͻ���
	private ArrayList<String> kw = new ArrayList<String>();			//��λlist
	private ArrayList<String> partNo = new ArrayList<String>();		//�Ϻ�list
	private ArrayList<String> partNoTaked = new ArrayList<String>();//�Ѿ�ȡ�õ��Ϻ�list����δ���ϣ�
	private ArrayList<String> kwTaked = new ArrayList<String>();	//�Ѿ�ȡ�õ��ϺŶ�Ӧ�Ŀ�λ
	
	public ArrayList<String> getPartNo() {
		return partNo;
	}
	public void setPartNo(ArrayList<String> partNo) {
		this.partNo = partNo;
	}
	public ArrayList<String> getPartNoTaked() {
		return partNoTaked;
	}
	public void setPartNoTaked(ArrayList<String> partNoTaked) {
		this.partNoTaked = partNoTaked;
	}
	
	public String getGdh() {
		return gdh;
	}
	public void setGdh(String gdh) {
		this.gdh = gdh;
	}
	public String getCgy() {
		return cgy;
	}
	public void setCgy(String cgy) {
		this.cgy = cgy;
	}
	public String getProcess() {
		return ""+kwTaked.size()+"/"+(kwTaked.size()+kw.size());
//		return ""+kwTaked.size()+"/"+ kw.size();
	}
	public void setProcess(String process) {
		this.process = process;
	}
	public ArrayList<String> getKw() {
		return kw;
	}
	public void setKw(ArrayList<String> kw) {
		this.kw = kw;
	}
	public ArrayList<String> getKwTaked() {
		return kwTaked;
	}
	public void setKwTaked(ArrayList<String> kwTaked) {
		this.kwTaked = kwTaked;
	}
	
	public boolean isEmpty(){
		if (gdh.equals("")) {
			return true;
		}
		return false;
	}
	@Override
	public boolean equals(Object o) {
		WoBean w = (WoBean) o;
		if (gdh.equals(w.getGdh())) {
			return true;
		}
		return super.equals(o);
	}
	
}
