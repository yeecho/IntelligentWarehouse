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
 * ���ݸ��·���
 * 
 * @author yuanye
 * 
 */
public class UpdateService extends Service {

	private static String tag = "com.greatwall.iws.service.UpdateService";	//tag
	private SharedPreferences sp;	//��ѡ��
	private SerialPort[] mSerialPorts = new SerialPort[3];	//��������(����������)
	private FileOutputStream[] mOutputStreams = new FileOutputStream[3];	//�����
	private FileInputStream[] mInputStreams = new FileInputStream[3];	//������
	private WebServiceUtil mWeb;	//���������ʹ���
	private IwsApplication iws;	//��Ӧ��ʵ��
	private BoardBean board;	//���ʵ��
	private String[] results = { "", "", "" };//���ڽ��յ����ݣ���δ�õ����������䣩
	private Handler mHandler;	//���յ���������ݺ󣬸�handler�������ݷַ�����
	private BroadcastReceiver mBroadcastReceiver; 	//���ý����������ʱ�ɸù㲥���������ж�Ӧ����

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.d(tag, "UpdateService onCreate");
		super.onCreate();
		initSerialPort();	//��ʼ������
		initData();		//��ʼ��Ӧ������
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(tag, "UpdateService onStart");
//		startDataCheck();//�������ݼ��
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(mBroadcastReceiver);//ע���㲥������
		super.onDestroy();
	}
	
	// ��ʼ����������
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
	
	// ��ʼ��Ӧ������
	private void initData() {
		
		//��ʼ������ʵ��
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
		if (isInitDev) {//��������ʼ����ַ�Ŀ���Ϊ�򿪣����ʼ�������ӻ�
			mWeb.getInitDev();
		}
		
		//��ʼ������ѡ��Ĺ㲥������
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

	//���ݼ��
	private void startDataCheck() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {

					// ÿ5���ѯһ�����ݿ�
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					checkData();//���ʷ�����
				}
			}

		}).start();
	}

	protected void checkData() {
		mWeb.getRecommendKW();	//��ȡ�������
		mWeb.getOutHouseDatas();	//��ȡ��������
		Log.d("yuanye", "Status:"+iws.getStatus());
	}

	//ָ��ͺ���
	private void sendMessage(int serial_num, String ADU) {
		if (mOutputStreams[serial_num] == null) {
			Log.d(tag, "mOutputStream is null");
			return;
		}
		//��յ�ǰ���ڽ��ܵĻ������ݣ���δ�õ���
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

	//�������ݶ�ȡ�̣߳�����չ���ܣ���δ�õ���
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

	//�Զ���ӿ�
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

	//������JSON���ݴ������
	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			//���������Ϣ
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
						Toast.makeText(UpdateService.this, "���ڳ����У��޷��������������������ݿ������Ƿ�������⣡", Toast.LENGTH_LONG).show();
					}else{
						JSONArray ja = (JSONArray) msg.obj;
						iws.setStatus(Constant.STATUS_ENTERHOUSE);
						handleRKJA(ja);//���ϴ��������������ύ������ʽ����
					}
				}
				break;
			//���������Ϣ
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
						Toast.makeText(UpdateService.this, "��������У��޷����г���������������ݿ������Ƿ�������⣡", Toast.LENGTH_LONG).show();
					}else{
						iws.setStatus(Constant.STATUS_OUTHOUSE);
						handleCKJA(ja);//���ϴ��������������ύ������ʽ����
					}
				}
				break;
			//�����ȡ�����������Ϣ
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
			//�����ַ��ʼ����Ϣ
			case Constant.WEB_GET_INITDEV:
				if (msg.obj == null) {
					Log.d(tag, "û�е�ַ�ĳ�ʼ������");
				} else {
					JSONArray ja = (JSONArray) msg.obj;
					Log.d(tag, "��ַ�ĳ�ʼ�����ݣ�"+ja.toString());
					handleInitJA(ja);
				}
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}

	}
	
	// �������json��Ϣ
	protected void handleRKJA(JSONArray ja) {
		if (JsonUtil.isChanged(iws, ja)) {// �жϽ��յ���JSONArray�Ƿ��б仯
			iws.setJa(ja);// ����ǰ������ݱ�������
			ArrayList<String> list = JsonUtil.getRkKw(ja);
			Log.d(tag, "enters��" + iws.enters);
			Log.d(tag, "list��" + list);
			// �Ƿ���Ҫ����
			for (int i = 0; i < list.size(); i++) {
				String kw = list.get(i);
				if (!iws.enters.contains(kw)) {
					lightUp(0, kw);
				}
			}
			// �Ƿ���Ҫ���
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
//			Log.d(tag, "��������ޱ仯");
		}
	}
	
	//���������Ϣ
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
	// �������json��Ϣ
	protected void handleCKJA(JSONArray ja) {
		if (JsonUtil.isChanged(iws, ja)) {
			Log.d("ja", ja.toString());
			iws.setJa(ja);
			//����JsonUtil�������������е�jsonArray,��ȡ����list
			ArrayList<WoBean> list = JsonUtil.getWoBeanList(ja);
			Log.d("�������json��Ϣ", "list.size:"+list.size());
			Intent intent = new Intent(Constant.ACTION_DATA_UPDATE);
			
			if (list.size()>3) {
				Toast.makeText(this, "��֧��ͬʱ���ⳬ�������������������ݿ��Ƿ����!", Toast.LENGTH_LONG).show();
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
				//�ӹ���list�л�ȡ����woBean
				WoBean woBean = list.get(i);
				String gdh = woBean.getGdh();
				ArrayList<String> kws = woBean.getKw();
				ArrayList<String> partNos = woBean.getPartNo();
				int code = board.isContain(gdh);
				Log.d("i="+i+" gdh:", gdh);
				Log.d("i="+i+" kws:", kws.toString());
				Log.d("i="+i+" partNos:", partNos.toString());
				Log.d("i="+i+" code:", ""+code);
				if (code == -1) {//�������µĹ�����
					int result = board.addWoBean(woBean);//��ӹ���
					if (result == -1) {
						Toast.makeText(this, "��ӹ���ʧ�ܣ��������ݿ��Ƿ����!", Toast.LENGTH_LONG).show();
						return;
					}
					for (String kw : kws) {
						lightUp(result, kw);
					}
					intent = new Intent(Constant.ACTION_DATA_FIND);
				}else{//�Ѿ����ڵĹ�����
					WoBean woBeanExist = board.getWoBean(code);
					ArrayList<String> kwsExist = woBeanExist.getKw();
					ArrayList<String> partNosExist = woBeanExist.getPartNo();
					ArrayList<String> kwTaked = woBeanExist.getKwTaked();
					for (int j = 0; j < partNos.size(); j++) {
						String partNo = partNos.get(j);
						String kw = kws.get(j);
						if (!partNosExist.contains(partNo)) {//�������µ��Ϻţ�˵�����������������µĴ���������
							partNosExist.add(partNo);	//�Ϻ�list����
							if (!kwsExist.contains(kw)) {
								lightUp(code, kw);		//�����Ϻų������µĿ�λ�ϣ������ÿ�λ
							}
							kwsExist.add(kw);		//��λlist����
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
//			Log.d(tag, "���������ޱ仯");
		}
	}
	
	//�����ַ��ʼ������Ϣ
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

	//��ʼ���ӻ���ַ������װ
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
	
	//�������ʼ��������װ
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

	// ��ƺ�����װ
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

	// ��ƺ�����װ
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
		Log.d(tag, "lightDown��" + kw);

	}

	// ���¿������������װ
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
	
	// �����¶Ⱥ�����װ
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
		
		// ����ʪ�Ⱥ�����װ
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

	// �Լ��ָ���������
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
		
		//���ù㲥���������������ñ仯
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
