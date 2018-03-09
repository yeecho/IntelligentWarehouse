package com.greatwall.iws.constant;

/**
 * 协议常量定义类
 * @author yuanye
 *
 */
public class ProtocolConstant {
	
	public static final String FUCTIONCODE_SET = "41";							//配置设备属性
	public static final String FUCTIONCODE_UPDATE = "46";						//更新设备数据
	public static final String FUCTIONCODE_QUERY = "51";						//查询设备属性
	
	public static final String INSTRUCTIONCODE_SET_ADDRESS = "73";				//分配设备地址
	public static final String INSTRUCTIONCODE_SET_RESET_REBOOT = "74";			//恢复出厂/重启设备
	public static final String INSTRUCTIONCODE_SET_SYNC_TIME = "75";			//同步时间
	public static final String INSTRUCTIONCODE_SET_ID = "76";					//设置设备ID
	public static final String INSTRUCTIONCODE_SET_NAME = "77";					//设置设备名称
	
	public static final String INSTRUCTIONCODE_QUERY_ID = "56";					//查询设备ID
	public static final String INSTRUCTIONCODE_QUERY_PROTOCAL_VERSION = "50";	//查询协议版本
	public static final String INSTRUCTIONCODE_QUERY_NAME = "4e";				//查询设备名称
	public static final String INSTRUCTIONCODE_QUERY_IMAGE_VERSION = "a3";		//查询固件版本
	
	public static final String INSTRUCTIONCODE_UPDATE_SN_QRCODE = "53";			//更新货仓存储产品SN QRcode
	public static final String INSTRUCTIONCODE_UPDATE_SN_BARCODE = "54";		//更新货仓存储产品SN Barcode
	public static final String INSTRUCTIONCODE_UPDATE_COUNT = "55";				//更新货仓存储产品数量
	public static final String INSTRUCTIONCODE_UPDATE_TEMPERATURE = "56";		//更新货仓温度
	public static final String INSTRUCTIONCODE_UPDATE_HUMIDITY = "57";			//更新货仓湿度
	public static final String INSTRUCTIONCODE_UPDATE_LIGHT = "58";				//LED灯操作
	
	public static final String[] LIGHT_DOWN = {"00","20","10"};					//灭灯：红-绿-蓝
//	public static final String[] LIGHT_UP = {"10","11","12"};					//亮灯
	public static final String[] LIGHT_UP = {"01","21","11"};					//亮灯：红-绿-蓝
	
}
