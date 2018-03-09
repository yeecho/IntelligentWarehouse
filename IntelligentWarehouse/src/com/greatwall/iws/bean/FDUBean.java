package com.greatwall.iws.bean;

import java.util.List;

import com.greatwall.iws.utils.DataUtil;

/**
 * 功能数据单元
 * @author yuanye
 *
 */
public class FDUBean {
	
	private String FDUCode;				//功能码
	private List<IDUBean> FDUContent;	//功能内容
	
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
