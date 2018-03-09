package com.greatwall.iws.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater.Filter;
import android.widget.Toast;

import com.greatwall.iws.IwsApplication;
import com.greatwall.iws.bean.BoardBean;
import com.greatwall.iws.bean.FDUBean;
import com.greatwall.iws.bean.IDUBean;
import com.greatwall.iws.bean.WoBean;
import com.greatwall.iws.constant.Constant;
import com.greatwall.iws.constant.ProtocolConstant;
import com.greatwall.iws.serial.SerialPort;
import com.greatwall.iws.utils.DataUtil;
import com.greatwall.iws.utils.JsonUtil;
import com.greatwall.iws.utils.WebServiceUtil;

/**
 * 数据更新服务
 * 
 * @author yuanye
 * 
 */
public class UpdateService extends Service {

	private static String tag = "com.greatwall.iws.service.UpdateService";	//tag
	private SharedPreferences sp;	//首选项
	private SerialPort[] mSerialPorts = new SerialPort[3];	//串口数组(有三个串口)
	private FileOutputStream[] mOutputStreams = new FileOutputStream[3];	//输出流
	private FileInputStream[] mInputStreams = new FileInputStream[3];	//输入流
	private WebServiceUtil mWeb;	//服务器访问工具
	private IwsApplication iws;	//本应用实例
	private BoardBean board;	//面板实例
	private String[] results = { "", "", "" };//串口接收的数据（暂未用到，留待扩充）
	private Handler mHandler;	//接收到服务端数据后，该handler进行数据分发处理
	private BroadcastReceiver mBroadcastReceiver; 	//设置界面进行设置时由该广播接收器进行对应处理

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.d(tag, "UpdateService onCreate");
		super.onCreate();
		initSerialPort();	//初始化串口
		initData();		//初始化应用数据
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(tag, "UpdateService onStart");
//		startDataCheck();//开启数据监测
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(mBroadcastReceiver);//注销广播接收器
		super.onDestroy();
	}
	
	// 初始化串口数据
	private void initSerialPort() {
		try {
			mSerialPorts[0] = new SerialPort(new File(Constant.UART0),
					Constant.BAUDRATE);
			mSerialPorts[1] = new SerialPort(new File(Constant.UART1),
					Constant.BAUDRATE);
			mSerialPorts[2] = new SerialPort(new File(Constant.UART2),
					Constant.BAUDRATE);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < 3; i++) {
			if (mSerialPorts[i] == null) {
				Log.d(tag, "mSerialPort " + i + " is null");
			} else {
				mInputStreams[i] = (FileInputStream) mSerialPorts[i]
						.getInputStream();
				mOutputStreams[i] = (FileOutputStream) mSerialPorts[i]
						.getOutputStream();
			}
		}
	}
	
	// 初始化应用数据
	private void initData() {
		
		//初始化各个实例
		mHandler = new MyHandler();
		mWeb = new WebServiceUtil(this, mHandler);
//		sendMessage(0, "02 46 69 54 67 10 38 37 36 35 34 33 32 2d 32 30 31 36 31 32 31 34 55 32 31 31 32 33 32 34 32 31 31 31 32 31 32 31 31 32 34 31 31 32 33 33 31 31 31 34 31 33 31 32 32 33 32 31 31 31 32 32 31 33 32 31 31 33 31 34 31 32 32 31 32 33 31 31 32 33 31 32 32 31 31 32 32 33 32 31 32 32 32 33 31 33 31 31 32 32 32 32 33 33 31 31 31 32 02 cd");
//		sendMessage(1, "02 46 aa 69 54 67 10 38 37 36 35 34 33 32 2d 32 30 31 36 31 32 31 34 55 32 31 31 32 33 32 34 32 31 31 31 32 31 32 31 31 32 34 31 31 32 33 33 31 31 31 34 31 33 31 32 32 33 32 31 31 31 32 32 31 33 32 31 31 33 31 34 31 32 32 31 32 33 31 31 32 33 31 32 32 31 31 32 32 33 32 31 32 32 32 33 31 33 31 31 32 32 32 32 33 33 31 31 31 32 76 da");
		//		sendMessage(2, "02 46 69 54 67 10 38 37 36 35 34 33 32 2d 32 30 31 36 31 32 31 34 55 32 31 31 32 33 32 34 32 31 31 31 32 31 32 31 31 32 34 31 31 32 33 33 31 31 31 34 31 33 31 32 32 33 32 31 31 31 32 32 31 33 32 31 31 33 31 34 31 32 32 31 32 33 31 31 32 33 31 32 32 31 31 32 32 33 32 31 32 32 32 33 31 33 31 31 32 32 32 32 33 33 31 31 31 32 02 cd");
//		initDev("4500F300E075334573237302", "SMT-01-02-02");
//		initDev("A500E300E075334573237302", "SMT-01-02-02");
//		backUp("SMT-01-02-00");
//		lightUp(0, "SMT-01-02-22");
//		updateAmount("SMT-01-01-22", 10345345);
		initBarCode("hello world !", "SMT-01-02-02");
		
		iws = (IwsApplication) getApplication();
		board = iws.getBoard();
		sp = getSharedPreferences(Constant.SHARE_PREF, MODE_PRIVATE);
		boolean isInitDev = sp.getBoolean(Constant.SETTING_INITDEV, false);
		if (isInitDev) {//若开机初始化地址的开关为打开，则初始化所有子机
			mWeb.getInitDev();
		}
		
		//初始化设置选项的广播接收器
		mBroadcastReceiver = new SettingBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.ACTION_INITDEV);
		filter.addAction(Constant.ACTION_BACKUP);
		filter.addAction(Constant.ACTION_LIGHT_UP_ALL);
		filter.addAction(Constant.ACTION_LIGHT_DOWN_ALL);
		filter.addAction(Constant.ACTION_UPDATE_TEMPRATURE);
		filter.addAction(Constant.ACTION_UPDATE_HUMUDITY);
		registerReceiver(mBroadcastReceiver, filter);
	}

	//数据监测
	private void startDataCheck() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {

					// 每5秒查询一次数据库
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					checkData();//访问服务器
				}
			}

		}).start();
	}

	protected void checkData() {
		mWeb.getRecommendKW();	//获取入库数据
		mWeb.getOutHouseDatas();	//获取出库数据
		Log.d("yuanye", "Status:"+iws.getStatus());
	}

	//指令发送函数
	private void sendMessage(int serial_num, String ADU) {
		if (mOutputStreams[serial_num] == null) {
			Log.d(tag, "mOutputStream is null");
			return;
		}
		//清空当前串口接受的缓存数据（暂未用到）
		results[serial_num] = "";
		try {
			byte[] msg = DataUtil.hexStringToBytes(ADU
					+ DataUtil.getCRC16CheckCode(ADU));
			mSerialPorts[serial_num].setRxtx(1);
			mOutputStreams[serial_num].write(msg);
			Log.d(tag, "sendMessage: " + DataUtil.bytesToHexString(msg));
			Thread.sleep(50);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	//串口数据读取线程（待扩展功能，暂未用到）
	private class ReadThread extends Thread {
		@Override
		public void run() {
			super.run();
			while (!isInterrupted()) {
				int size;
				try {
					for (int i = 0; i < 3; i++) {

						byte[] buffer = new byte[128];
						if (mInputStreams[i] != null) {
							size = mInputStreams[i].read(buffer);
							if (size > 0) {
								onDataReceived(buffer, size, i);
							}
						}
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}

	//自定义接口
	void onDataReceived(final byte[] buffer, final int size, int pos) {
		byte[] tempBuf = new byte[size];
		for (int i = 0; i < size; i++) {
			tempBuf[i] = buffer[i];
		}
		results[pos] += DataUtil.bytesToHexString(tempBuf);
		if (DataUtil.isCompleted(results[pos])) {
			Log.d(tag, "pos" + pos + ":msg receive complete");
			if (DataUtil.CRC16Check(results[pos])) {
				Log.d(tag, "pos" + pos + ":CRC check pass");
			} else {
				Log.d(tag, "pos" + pos + ":CRC check failed");
			}
		} else {
			Log.d(tag, "pos" + pos + ":msg receive not complete");
		}
	}

	//服务器JSON数据处理机制
	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			//处理入库信息
			case Constant.WEB_GETENTER_DATA:
				if (msg.obj == null) {
					if (iws.getStatus() == Constant.STATUS_ENTERHOUSE) {
						Log.d(tag, "enter-house success,no message now");
						if (iws.enters.size() > 0) {
							for (String kw : iws.enters) {
								lightDown(0, kw);
							}
							iws.enters.clear();
						}
						iws.setStatus(Constant.STATUS_IDLE);
					}else{
						Log.d(tag, "no message of enter-house");
					}
				} else {
					Log.d(tag, "json:"+msg.obj.toString());
					if (iws.getStatus() == Constant.STATUS_OUTHOUSE) {
						Toast.makeText(UpdateService.this, "正在出库中，无法进行入库操作，请检查数据库数据是否出现问题！", Toast.LENGTH_LONG).show();
					}else{
						JSONArray ja = (JSONArray) msg.obj;
						iws.setStatus(Constant.STATUS_ENTERHOUSE);
						handleRKJA(ja);//符合处理条件，往上提交进行正式处理
					}
				}
				break;
			//处理出库信息
			case Constant.WEB_GETOUT_DATA:
				if (msg.obj == null) {
					Log.d(tag, "no message of out-house");
					if (iws.getStatus() == Constant.STATUS_OUTHOUSE) {
						woBeanClear();
						iws.setStatus(Constant.STATUS_IDLE);
					}
				} else {
					JSONArray ja = (JSONArray) msg.obj;
					if (iws.getStatus() == Constant.STATUS_ENTERHOUSE) {
						Toast.makeText(UpdateService.this, "正在入库中，无法进行出库操作，请检查数据库数据是否出现问题！", Toast.LENGTH_LONG).show();
					}else{
						iws.setStatus(Constant.STATUS_OUTHOUSE);
						handleCKJA(ja);//符合处理条件，往上提交进行正式处理
					}
				}
				break;
			//处理获取库存数量的信息
			case Constant.WEB_GET_MOUNT:
				if (msg.obj == null) {
					Log.d(tag, "WEB_GET_MOUNT:ja is null");
				} else {
					JSONArray ja = (JSONArray) msg.obj;
					try {
						JSONObject jo = ja.getJSONObject(0);
						String kw = jo.getString("KW");
						int mount = Integer.parseInt(jo.getString("Qty"));
						Log.d(kw, "KW:" + kw + " mount:" + mount);
						updateAmount(kw, mount);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				break;
			//处理地址初始化信息
			case Constant.WEB_GET_INITDEV:
				if (msg.obj == null) {
					Log.d(tag, "没有地址的初始化数据");
				} else {
					JSONArray ja = (JSONArray) msg.obj;
					Log.d(tag, "地址的初始化数据："+ja.toString());
					handleInitJA(ja);
				}
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}

	}
	
	// 处理入库json信息
	protected void handleRKJA(JSONArray ja) {
		if (JsonUtil.isChanged(iws, ja)) {// 判断接收到的JSONArray是否有变化
			iws.setJa(ja);// 将当前入库数据保存下来
			ArrayList<String> list = JsonUtil.getRkKw(ja);
			Log.d(tag, "enters：" + iws.enters);
			Log.d(tag, "list：" + list);
			// 是否需要亮灯
			for (int i = 0; i < list.size(); i++) {
				String kw = list.get(i);
				if (!iws.enters.contains(kw)) {
					lightUp(0, kw);
				}
			}
			// 是否需要灭灯
			for (int i = 0; i < iws.enters.size(); i++) {
				String kw = iws.enters.get(i);
				if (!list.contains(kw)) {
					lightDown(0, kw);
					mWeb.getMount(kw);
				}
			}
			iws.enters.clear();
			iws.enters.addAll(list);

		} else {
//			Log.d(tag, "入库数据无变化");
		}
	}
	
	//清除工单信息
	private void woBeanClear() {
		Log.d(tag, "clear");
		if (!board.getRed().isEmpty()) {
			for (String kw : board.getRed().getKw()) {
				lightDown(0, kw);
				mWeb.getMount(kw);
			}
			board.setRed(new WoBean());
		}
		if (!board.getBlue().isEmpty()) {
			for (String kw : board.getBlue().getKw()) {
				lightDown(1, kw);
				mWeb.getMount(kw);
			}
			board.setBlue(new WoBean());
		}
		if (!board.getGreen().isEmpty()) {
			for (String kw : board.getGreen().getKw()) {
				lightDown(2, kw);
				mWeb.getMount(kw);
			}
			board.setGreen(new WoBean());
		}
		Intent intent = new Intent(Constant.ACTION_DATA_UPDATE);
		sendBroadcast(intent);
	}
	// 处理出库json信息
	protected void handleCKJA(JSONArray ja) {
		if (JsonUtil.isChanged(iws, ja)) {
			Log.d("ja", ja.toString());
			iws.setJa(ja);
			//利用JsonUtil解析出服务器中的jsonArray,获取工单list
			ArrayList<WoBean> list = JsonUtil.getWoBeanList(ja);
			Log.d("处理出库json信息", "list.size:"+list.size());
			Intent intent = new Intent(Constant.ACTION_DATA_UPDATE);
			
			if (list.size()>3) {
				Toast.makeText(this, "不支持同时出库超过三个工单，请检查数据库是否出错!", Toast.LENGTH_LONG).show();
				return;
			}
			if (!board.getRed().isEmpty()) {
				if (!list.contains(board.getRed())) {
					for (String kw : board.getRed().getKw()) {
						lightDown(0, kw);
						mWeb.getMount(kw);
					}
					board.setRed(new WoBean());
				}
			}
			if (!board.getBlue().isEmpty()) {
				if (!list.contains(board.getBlue())) {
					for (String kw : board.getBlue().getKw()) {
						lightDown(1, kw);
						mWeb.getMount(kw);
					}
					board.setBlue(new WoBean());
				}
			}
			if (!board.getGreen().isEmpty()) {
				if (!list.contains(board.getGreen())) {
					for (String kw : board.getGreen().getKw()) {
						lightDown(2, kw);
						mWeb.getMount(kw);
					}
					board.setGreen(new WoBean());
				}
			}
			for (int i = 0; i < list.size(); i++) {
				//从工单list中获取工单woBean
				WoBean woBean = list.get(i);
				String gdh = woBean.getGdh();
				ArrayList<String> kws = woBean.getKw();
				ArrayList<String> partNos = woBean.getPartNo();
				int code = board.isContain(gdh);
				Log.d("i="+i+" gdh:", gdh);
				Log.d("i="+i+" kws:", kws.toString());
				Log.d("i="+i+" partNos:", partNos.toString());
				Log.d("i="+i+" code:", ""+code);
				if (code == -1) {//出现了新的工单号
					int result = board.addWoBean(woBean);//添加工单
					if (result == -1) {
						Toast.makeText(this, "添加工单失败，请检查数据库是否出错!", Toast.LENGTH_LONG).show();
						return;
					}
					for (String kw : kws) {
						lightUp(result, kw);
					}
					intent = new Intent(Constant.ACTION_DATA_FIND);
				}else{//已经存在的工单号
					WoBean woBeanExist = board.getWoBean(code);
					ArrayList<String> kwsExist = woBeanExist.getKw();
					ArrayList<String> partNosExist = woBeanExist.getPartNo();
					ArrayList<String> kwTaked = woBeanExist.getKwTaked();
					for (int j = 0; j < partNos.size(); j++) {
						String partNo = partNos.get(j);
						String kw = kws.get(j);
						if (!partNosExist.contains(partNo)) {//出现了新的料号，说明工单数据增加了新的待出库物料
							partNosExist.add(partNo);	//料号list更新
							if (!kwsExist.contains(kw)) {
								lightUp(code, kw);		//若该料号出现在新的库位上，点亮该库位
							}
							kwsExist.add(kw);		//库位list更新
							intent = new Intent(Constant.ACTION_DATA_UPDATE);
						}
					}
					Iterator<String> it = kwsExist.iterator();
					int count = 0;
					while(it.hasNext()){
						String kw = (String) it.next();
						if (!kws.contains(kw)) {
							lightDown(code, kw);
							mWeb.getMount(kw);
							kwTaked.add(kw);
							it.remove();
							count++;
						}
					}
					if (count>0) {
						intent = new Intent(Constant.ACTION_DATA_UPDATE);
					}
				}
				sendBroadcast(intent);
			}
		}else{
//			Log.d(tag, "出库数据无变化");
		}
	}
	
	//处理地址初始化的信息
	private void handleInitJA (JSONArray ja){
		for (int i = 0; i < ja.length(); i++) {
			try {
				JSONObject jo = ja.getJSONObject(i);
				String devId = jo.getString("devId");
				String kw = jo.getString("KW");
				initDev(devId, kw);
				mWeb.getMount(kw);
//				Log.d(tag, "debug:"+DataUtil.bytesToHexString(devId.getBytes()));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	//初始化子机地址函数封装
	private void initDev(String devId, String kw) {
		FDUBean fduBean = new FDUBean();
		fduBean.setFDUCode(ProtocolConstant.FUCTIONCODE_SET);
		IDUBean iduBean = new IDUBean();
		iduBean.setIDUCode(ProtocolConstant.INSTRUCTIONCODE_SET_ADDRESS);
//		iduBean.setIDUContent(DataUtil.addressDecode(kw));
		iduBean.setIDUContent("18"+DataUtil.bytesToHexString(devId.getBytes())+"01"+DataUtil.addressDecode(kw));
		ArrayList<IDUBean> iduBeans = new ArrayList<IDUBean>();
		iduBeans.add(iduBean);
		fduBean.setFDUContent(iduBeans);
		String ADU = DataUtil.buildADU("00", fduBean);
		sendMessage(DataUtil.serialNumDecode(kw), ADU);
	}
	
	//条形码初始化函数封装
	private void initBarCode(String barcode, String kw) {
		int serial_num = DataUtil.serialNumDecode(kw);
		String virtualAddress = DataUtil.addressDecode(kw);
		FDUBean fduBean = new FDUBean();
		fduBean.setFDUCode(ProtocolConstant.FUCTIONCODE_UPDATE);
		IDUBean iduBean = new IDUBean();
		iduBean.setIDUCode(ProtocolConstant.INSTRUCTIONCODE_UPDATE_SN_BARCODE);
//		String iduContent = DataUtil.generate_128C(barcode);
		String iduContent = "";
		String sourceContent = DataUtil.bytesToHexString(barcode.getBytes());
		String sourceContentLen = DataUtil.getHexStringLength(sourceContent);
		String transformCode = "";
		try {
			transformCode = DataUtil.generate_128C(barcode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String transformCodeLen = DataUtil.getHexStringLength(transformCode);
		iduContent = sourceContentLen + sourceContent + transformCodeLen + transformCode;
		iduBean.setIDUContent(iduContent);
		ArrayList<IDUBean> iduBeans = new ArrayList<IDUBean>();
		iduBeans.add(iduBean);
		fduBean.setFDUContent(iduBeans);
		String ADU = DataUtil.buildADU(virtualAddress, fduBean);
		sendMessage(serial_num, ADU);
	}

	// 点灯函数封装
	private void lightUp(int i, String kw) {
		int serial_num = DataUtil.serialNumDecode(kw);
		String virtualAddress = DataUtil.addressDecode(kw);
		FDUBean fduBean = new FDUBean();
		fduBean.setFDUCode(ProtocolConstant.FUCTIONCODE_UPDATE);
		IDUBean iduBean = new IDUBean();
		iduBean.setIDUCode(ProtocolConstant.INSTRUCTIONCODE_UPDATE_LIGHT);
		iduBean.setIDUContent(ProtocolConstant.LIGHT_UP[i]);
		ArrayList<IDUBean> iduBeans = new ArrayList<IDUBean>();
		iduBeans.add(iduBean);
		fduBean.setFDUContent(iduBeans);
		String ADU = DataUtil.buildADU(virtualAddress, fduBean);
		sendMessage(serial_num, ADU);
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		Log.d(tag, "lightUp:" + kw);
	}

	// 灭灯函数封装
	private void lightDown(int i, String kw) {
		int serial_num = DataUtil.serialNumDecode(kw);
		String virtualAddress = DataUtil.addressDecode(kw);
		FDUBean fduBean = new FDUBean();
		fduBean.setFDUCode(ProtocolConstant.FUCTIONCODE_UPDATE);
		IDUBean iduBean = new IDUBean();
		iduBean.setIDUCode(ProtocolConstant.INSTRUCTIONCODE_UPDATE_LIGHT);
		iduBean.setIDUContent(ProtocolConstant.LIGHT_DOWN[i]);
		ArrayList<IDUBean> iduBeans = new ArrayList<IDUBean>();
		iduBeans.add(iduBean);
		fduBean.setFDUContent(iduBeans);
		String ADU = DataUtil.buildADU(virtualAddress, fduBean);
		sendMessage(serial_num, ADU);
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Log.d(tag, "lightDown：" + kw);

	}

	// 更新库存数量函数封装
	private void updateAmount(String kw, int count) {
		int serial_num = DataUtil.serialNumDecode(kw);
		String virtualAddress = DataUtil.addressDecode(kw);
		FDUBean fduBean = new FDUBean();
		fduBean.setFDUCode(ProtocolConstant.FUCTIONCODE_UPDATE);
		IDUBean iduBean = new IDUBean();
		iduBean.setIDUCode(ProtocolConstant.INSTRUCTIONCODE_UPDATE_COUNT);
		iduBean.setIDUContent(DataUtil.getHexCount(count));
		ArrayList<IDUBean> iduBeans = new ArrayList<IDUBean>();
		iduBeans.add(iduBean);
		fduBean.setFDUContent(iduBeans);
		String ADU = DataUtil.buildADU(virtualAddress, fduBean);
		sendMessage(serial_num, ADU);
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d(tag, "updateAmount:" + count +"~"+ kw);
	}
	
	// 更新温度函数封装
		private void updateTemprature(String kw, int temprature) {
			int serial_num = DataUtil.serialNumDecode(kw);
			String virtualAddress = DataUtil.addressDecode(kw);
			FDUBean fduBean = new FDUBean();
			fduBean.setFDUCode(ProtocolConstant.FUCTIONCODE_UPDATE);
			IDUBean iduBean = new IDUBean();
			iduBean.setIDUCode(ProtocolConstant.INSTRUCTIONCODE_UPDATE_TEMPERATURE);
			iduBean.setIDUContent(DataUtil.getHexCount(temprature));
			ArrayList<IDUBean> iduBeans = new ArrayList<IDUBean>();
			iduBeans.add(iduBean);
			fduBean.setFDUContent(iduBeans);
			String ADU = DataUtil.buildADU(virtualAddress, fduBean);
			sendMessage(serial_num, ADU);
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.d(tag, "updateTemprature:" + temprature +"~"+ kw);
		}
		
		// 更新湿度函数封装
		private void updateHumidity(String kw, int humidity) {
			int serial_num = DataUtil.serialNumDecode(kw);
			String virtualAddress = DataUtil.addressDecode(kw);
			FDUBean fduBean = new FDUBean();
			fduBean.setFDUCode(ProtocolConstant.FUCTIONCODE_UPDATE);
			IDUBean iduBean = new IDUBean();
			iduBean.setIDUCode(ProtocolConstant.INSTRUCTIONCODE_UPDATE_HUMIDITY);
			iduBean.setIDUContent(DataUtil.getHexCount(humidity));
			ArrayList<IDUBean> iduBeans = new ArrayList<IDUBean>();
			iduBeans.add(iduBean);
			fduBean.setFDUContent(iduBeans);
			String ADU = DataUtil.buildADU(virtualAddress, fduBean);
			sendMessage(serial_num, ADU);
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.d(tag, "updateHumidity:" + humidity +"~"+ kw);
		}

	// 自己恢复出厂设置
		private void backUp(String kw) {
			int serial_num = DataUtil.serialNumDecode(kw);
			String virtualAddress = DataUtil.addressDecode(kw);
			FDUBean fduBean = new FDUBean();
			fduBean.setFDUCode(ProtocolConstant.FUCTIONCODE_SET);
			IDUBean iduBean = new IDUBean();
			iduBean.setIDUCode(ProtocolConstant.INSTRUCTIONCODE_SET_RESET_REBOOT);
			iduBean.setIDUContent("02");
			ArrayList<IDUBean> iduBeans = new ArrayList<IDUBean>();
			iduBeans.add(iduBean);
			fduBean.setFDUContent(iduBeans);
			String ADU = DataUtil.buildADU(virtualAddress, fduBean);
			sendMessage(serial_num, ADU);
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//设置广播接收器，处理设置变化
		class SettingBroadcastReceiver extends BroadcastReceiver{

			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(Constant.ACTION_INITDEV)) {
					mWeb.getInitDev();
				}else if (intent.getAction().equals(Constant.ACTION_BACKUP)) {
					backUp("SMT-01-01-00");
					backUp("SMT-01-02-00");
					backUp("SMT-01-03-00");
				}else if (intent.getAction().equals(Constant.ACTION_LIGHT_UP_ALL)) {
					for (int i = 0; i < 3; i++) {
						lightUp(i, "SMT-01-01-00");
						lightUp(i, "SMT-01-02-00");
						lightUp(i, "SMT-01-03-00");
					}
				}else if (intent.getAction().equals(Constant.ACTION_LIGHT_DOWN_ALL)) {
					for (int i = 0; i < 3; i++) {
						lightDown(i, "SMT-01-01-00");
						lightDown(i, "SMT-01-02-00");
						lightDown(i, "SMT-01-03-00");
					}
				}else if (intent.getAction().equals(Constant.ACTION_UPDATE_TEMPRATURE)) {
					int temprature = intent.getIntExtra("temprature", 0);
					updateTemprature("SMT-01-01-00", temprature);
					updateTemprature("SMT-01-02-00", temprature);
					updateTemprature("SMT-01-03-00", temprature);
				}else if (intent.getAction().equals(Constant.ACTION_UPDATE_HUMUDITY)) {
					int humidity = intent.getIntExtra("humidity", 0);
					updateHumidity("SMT-01-01-00", humidity);
					updateHumidity("SMT-01-02-00", humidity);
					updateHumidity("SMT-01-03-00", humidity);
				}
			}
			
		}
		

}
