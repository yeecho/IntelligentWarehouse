package com.greatwall.iws;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.greatwall.iws.bean.BoardBean;
import com.greatwall.iws.constant.Constant;
import com.greatwall.iws.service.UpdateService;

public class MainActivity extends Activity {

	private String tag = "com.greatwall.iws.MainActivity"; // tag
	private boolean debug = false; // �Ƿ����õ���ģʽ
	private IwsApplication iwsApp; // ��Ӧ�õ�ʵ��
	private BoardBean board; // ���ʵ��
	private boolean red, blue, green; // �죬�̣�������������Ϣ���ʵ��
	private TextView tvWonRed, tvWhkRed, tvProcessRed, tvTimeCostRed; // �����ţ��ֹ�Ա�����ȣ�����ʱ��
																		// ���죩
	private TextView tvWonBlue, tvWhkBlue, tvProcessBlue, tvTimeCostBlue; // �����ţ��ֹ�Ա�����ȣ�����ʱ��
																			// ������
	private TextView tvWonGreen, tvWhkGreen, tvProcessGreen, tvTimeCostGreen; // �����ţ��ֹ�Ա�����ȣ�����ʱ��
																				// ���̣�
	private TextView tvTempHumi; // ���¶�&ʪ�ȣ�
	private TvHandler tvHandler = new TvHandler(); // ��Ϣ��ʾ����handler
	private int timecostRed, timecostBlue, timecostGreen; // ����ʱ��ֵ
	private SensorManager sm; // ����������
	private Sensor temprature; // �¶�sensor
	private Sensor humidity; // ʪ��sensor
	private MySensorEventListener listener; // ������������
	private String strTemprature = "�¶ȣ�"; // �¶�
	private String strHumidity = "ʪ�ȣ�"; // ʪ��
	private int tmpTemprature = 0; // �¶�ֵ
	private int tmpHumidity = 0; // ʪ��ֵ
	private boolean flagTemprature = true;
	private boolean flagHumidity = true;
	private int count = 0;
	private UpdateBroadCastReceiver boardBroadcastReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		View decorView = getWindow().getDecorView();
		int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_FULLSCREEN;
		decorView.setSystemUiVisibility(uiOptions);
		findUI();
		initData();
	}

	// ��ʼ������
	private void initData() {
		iwsApp = (IwsApplication) getApplication();
		board = iwsApp.getBoard();
		sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		boardBroadcastReceiver = new UpdateBroadCastReceiver();
		temprature = sm.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
		humidity = sm.getDefaultSensor(Sensor.TYPE_LIGHT);
		listener = new MySensorEventListener();
		if (temprature == null) {
			Toast.makeText(this, "�¸в�����", Toast.LENGTH_SHORT).show();
		} else {
			sm.registerListener(listener, temprature,
					SensorManager.SENSOR_DELAY_NORMAL);
		}
		if (humidity == null) {
			Toast.makeText(this, "ʪ�ȴ��в�����", Toast.LENGTH_SHORT).show();
		} else {
			sm.registerListener(listener, humidity,
					SensorManager.SENSOR_DELAY_NORMAL);
		}
	}

	// ��ʼ��UI
	private void findUI() {
		setContentView(R.layout.activity_main);
		tvWonRed = (TextView) findViewById(R.id.bill_wo_number_red);
		tvWhkRed = (TextView) findViewById(R.id.bill_wh_keeper_red);
		tvProcessRed = (TextView) findViewById(R.id.bill_process_red);
		tvTimeCostRed = (TextView) findViewById(R.id.bill_time_cost_red);
		tvWonBlue = (TextView) findViewById(R.id.bill_wo_number_blue);
		tvWhkBlue = (TextView) findViewById(R.id.bill_wh_keeper_blue);
		tvProcessBlue = (TextView) findViewById(R.id.bill_process_blue);
		tvTimeCostBlue = (TextView) findViewById(R.id.bill_time_cost_blue);
		tvWonGreen = (TextView) findViewById(R.id.bill_wo_number_green);
		tvWhkGreen = (TextView) findViewById(R.id.bill_wh_keeper_green);
		tvProcessGreen = (TextView) findViewById(R.id.bill_process_green);
		tvTimeCostGreen = (TextView) findViewById(R.id.bill_time_cost_green);

		tvTempHumi = (TextView) findViewById(R.id.temperature_humidity);
		tvTempHumi.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						SettingActivity.class);
				MainActivity.this.startActivityForResult(intent, 100);
				return false;
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		// �������ʷ���
		startService(new Intent(this, UpdateService.class));

		// ע��㲥������
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.ACTION_DATA_FIND);
		filter.addAction(Constant.ACTION_DATA_UPDATE);
		filter.addAction(Intent.ACTION_TIME_TICK);
		registerReceiver(boardBroadcastReceiver, filter);
	}

	@Override
	protected void onDestroy() {
		stopService(new Intent(this, UpdateService.class));// ֹͣ����
		unregisterReceiver(boardBroadcastReceiver);// ע�����㲥������
		sm.unregisterListener(listener);// ע������������
		super.onDestroy();
	}

	/**
	 * 
	 * @author yuanye ��̬�㲥�������������������͹����Ĺ㲥
	 */
	class UpdateBroadCastReceiver extends BroadcastReceiver {

		@SuppressWarnings("unchecked")
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Constant.ACTION_DATA_FIND)) {
				// ���¿���UI
				tvHandler.sendEmptyMessage(0);
			} else if (intent.getAction().equals(Constant.ACTION_DATA_UPDATE)) {
				tvHandler.sendEmptyMessage(1);
			} else if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
				count++;
				Log.e(tag, "count:"+count);
				if (count > 10) {
					Log.e(tag, "��ʪ�ȸ��¿��ش�");
					flagTemprature = true;
					flagHumidity = true;
					count = 0;
				}
				if (red) {
					timecostRed++;
					tvTimeCostRed.setText("" + timecostRed + "����");
				}
				if (blue) {
					timecostBlue++;
					tvTimeCostBlue.setText("" + timecostBlue + "����");
				}
				if (green) {
					timecostGreen++;
					tvTimeCostGreen.setText("" + timecostGreen + "����");
				}
			}
		}

	}

	// ������幤����Ϣ
	class TvHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (!board.getRed().getGdh().equals("")) {
					red = true;
					tvWonRed.setText(board.getRed().getGdh());
					tvWhkRed.setText(board.getRed().getCgy());
					tvProcessRed.setText(board.getRed().getProcess());
				}
				if (!board.getBlue().getGdh().equals("")) {
					blue = true;
					tvWonBlue.setText(board.getBlue().getGdh());
					tvWhkBlue.setText(board.getBlue().getCgy());
					tvProcessBlue.setText(board.getBlue().getProcess());
				}
				if (!board.getGreen().getGdh().equals("")) {
					green = true;
					tvWonGreen.setText(board.getGreen().getGdh());
					tvWhkGreen.setText(board.getGreen().getCgy());
					tvProcessGreen.setText(board.getGreen().getProcess());
				}
				break;
			case 1:
				if (red) {
					if (!board.getRed().getGdh().equals("")) {
						tvWonRed.setText(board.getRed().getGdh());
						tvWhkRed.setText(board.getRed().getCgy());
						tvProcessRed.setText(board.getRed().getProcess());
					} else {
						tvProcessRed.setText("100%");
						timecostRed = 0;
						red = false;
					}
				}
				if (blue) {
					if (!board.getBlue().getGdh().equals("")) {
						tvWonBlue.setText(board.getBlue().getGdh());
						tvWhkBlue.setText(board.getBlue().getCgy());
						tvProcessBlue.setText(board.getBlue().getProcess());
					} else {
						tvProcessBlue.setText("100%");
						timecostBlue = 0;
						blue = false;
					}
				}
				if (green) {
					if (!board.getGreen().getGdh().equals("")) {
						tvWonGreen.setText(board.getGreen().getGdh());
						tvWhkGreen.setText(board.getGreen().getCgy());
						tvProcessGreen.setText(board.getGreen().getProcess());
					} else {
						tvProcessGreen.setText("100%");
						timecostGreen = 0;
						green = false;
					}
				}
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}

	}

	// ������������
	public class MySensorEventListener implements SensorEventListener {

		@Override
		public void onSensorChanged(SensorEvent event) {
			int type = event.sensor.getType();
			if (type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
				float value = event.values[0];
				DecimalFormat df = new DecimalFormat("#.0");
				if (value <= 100) {
					int temp = (int) value;
					// �����²����1��
					if ((temp - tmpTemprature) >= 1
							|| (tmpTemprature - temp) >= 1) {
						tmpTemprature = temp;
						if (flagTemprature) {
							Intent intent = new Intent(
									Constant.ACTION_UPDATE_TEMPRATURE);
							intent.putExtra("temprature", tmpTemprature);
							Log.e(tag, "sendBroadcast:temp");
							sendBroadcast(intent);
							flagTemprature = false;
						}
					}
					strTemprature = "�¶ȣ�" + df.format(value) + "��";
				} else if (value > 100) {
					int temp = (int) (value / 100);
					// �����²����1��
					if ((temp - tmpHumidity) >= 1 || (tmpHumidity - temp) >= 1) {
						tmpHumidity = temp;
						if (flagHumidity) {
							Intent intent = new Intent(
									Constant.ACTION_UPDATE_HUMUDITY);
							intent.putExtra("humidity", tmpHumidity);
							Log.e(tag, "sendBroadcast:Humi");
							sendBroadcast(intent);
							flagHumidity = false;
						}
					}
					strHumidity = "ʪ�ȣ�" + df.format(value / 100) + "%";
				}
				tvTempHumi.setText(strTemprature + "\n" + strHumidity);
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}

	}

}
