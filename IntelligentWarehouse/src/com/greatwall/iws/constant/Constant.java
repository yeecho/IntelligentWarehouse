package com.greatwall.iws.constant;

/**
 * 常量定义类
 * @author yuanye
 *
 */
public class Constant {
	
	//SharedPreferences
	public static final String SHARE_PREF = "sharePref";
	public static final String SETTING_INITDEV = "setting_initdev";
	
	//应用工作状态
	public static final int STATUS_IDLE = 0;		//闲置
	public static final int STATUS_ENTERHOUSE = 1;	//入库
	public static final int STATUS_OUTHOUSE = 2;	//出库
	
	//串口地址
	public static final String UART0 = "/dev/ttyS0";		
	public static final String UART1 = "/dev/ttyS1";		
	public static final String UART2 = "/dev/ttyS2";
	
	//波特率
	public static final int BAUDRATE = 115200;	
	
	//广播
	public static String ACTION_DATA_UPDATE = "com.greatwall.iws.action.data_update";
	public static String ACTION_DATA_FIND = "com.greatwall.iws.action.data_find";
	public static String ACTION_INITDEV = "com.greatwall.iws.action.setting_initdev";
	public static String ACTION_BACKUP = "com.greatwall.iws.action.setting_backup";
	public static String ACTION_LIGHT_UP_ALL = "com.greatwall.iws.action.setting_lightupall";
	public static String ACTION_LIGHT_DOWN_ALL = "com.greatwall.iws.action.setting_lightdownall";
	public static String ACTION_UPDATE_TEMPRATURE = "com.greatwall.iws.action.update_temprature";
	public static String ACTION_UPDATE_HUMUDITY = "com.greatwall.iws.action.update_humidity";
	
	/**
	 * 从服务器获取的数据类型
	 */
	public static final int SHOW_TOAST = 0x00;
	public static final int WEB_OUT_UPDATE = 0x01;
	public static final int WEB_OUT = 0x02;
	public static final int WEB_ENTER_UPDATE = 0x03;
	public static final int WEB_ENTER = 0x04;
	public static final int WEB_GETOUT_DATA = 0x05;
	public static final int WEB_GETENTER_DATA = 0x06;
	public static final int WEB_HANDLER_SCAN = 0x07;
	public static final int SHOW_WAITING_DIALOG = 0x08;
	public static final int WEB_GET_MOUNT = 0x09;
	public static final int WEB_GET_INITDEV = 0x10;
	
	public static final String nameSpace = "http://web.gw";
	public static final String serviceURL = "http://10.12.59.215:8080/axis2/services/A29Webservice";
	
	/******方法*******/
	/*
	 *  待入库扫描
	 *  String gysid, String ccpn, String scsj, String xlh, String drksl, String rkczy
	 */
	public static final String EhUpdate = "ehUpdate";//待入库扫描 
	
	
	/*
	 * 待出库扫描
	 * String ckgdh, String ccpn, String ckms, String cksl, String ckczy
    */
	public static final String OhUpdate = "ohUpdate";
	
	
	/* 
	 * 入库 
	 * enterHouse(String ccpn, String rkczy)
	 */
	public static final String EnterHouse = "enterHouse";
	
	/* 
	 * 正式出库
	 * outHouse(String ckgdh, String ccpn,  String ckczy)
	 */
	public static final String OutHouse = "outHouse";
	
	/* 
	 * 获取出库数据
	 */
	public static final String GetOutHouseDataPre = "getKwDataPre";
	
	/* 
	 * 获取入库数据
	 */
	public static final String GetEnterHouseDatas = "getEnterHouseDatas";
	
	/* 
	 * 获取推荐库位
	 */
	public static final String GetRecommendKW = "recommendKW";
	
	/*****************/
	/* 
	 * 获取物料数量
	 */
	public static final String getTableKwDataNum = "getTableKwDataNum";
	
	/*****************/
	/* 
	 * 获取物料数量
	 */
	public static final String getInitDev = "getInitDev";
	
	/*****************/
	
}
