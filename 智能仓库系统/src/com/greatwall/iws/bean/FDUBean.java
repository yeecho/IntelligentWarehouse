package com.greatwall.iws.bean;

import java.util.List;

import com.greatwall.iws.utils.DataUtil;

/**
 * �������ݵ�Ԫ
 * @author yuanye
 *
 */
public class FDUBean {
	
	private String FDUCode;				//������
	private List<IDUBean> FDUContent;	//��������
	
	public String getFDUCode() {
		return FDUCode;
	}
	public void setFDUCode(String fDUCode) {
		FDUCode = fDUCode;
	}
	public List<IDUBean> getFDUContent() {
		return FDUContent;
	}
	public void setFDUContent(List<IDUBean> fDUContent) {
		FDUContent = fDUContent;
	}
	public String getFDULen() {
		
		return DataUtil.getHexStringLength(getFDU());
	}
	public String getFDU() {
		String FDU = "";
		for (IDUBean iduBean : FDUContent) {
			String strLen = DataUtil.getHexStringLength(iduBean.getIDUContent());
			FDU += iduBean.getIDUCode() + strLen + iduBean.getIDUContent();
		}
		return FDU;
	}
	
}
