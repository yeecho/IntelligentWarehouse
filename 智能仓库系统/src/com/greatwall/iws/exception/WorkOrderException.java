package com.greatwall.iws.exception;

import java.io.PrintWriter;

@SuppressWarnings("serial")
public class WorkOrderException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7846188419968770976L;

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "添加工单失败！！请检查数据库中工单号是否超过了3个！！这是不合法的！！";
	}

}
