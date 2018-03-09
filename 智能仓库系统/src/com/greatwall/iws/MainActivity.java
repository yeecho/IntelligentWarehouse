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
	private boolean debug = false; // 是否弃用调试模式
	private IwsApplication iwsApp; // 本应用的实例
	private BoardBean board; // 面板实例
	private boolean red, blue, green; // 红，绿，蓝三个工单信息块的实例
	private TextView tvWonRed, tvWhkRed, tvProcessRed, tvTimeCostRed; // 工单号，仓管员，进度，配料时长
																		// （红）
	private TextView tvWonBlue, tvWhkBlue, tvProcessBlue, tvTimeCostBlue; // 工单号，仓管员，进度，配料时长
																			// （蓝）
	private TextView tvWonGreen, tvWhkGreen, tvProcessGreen, tvTimeCostGreen; // 工单号，仓管员，进度，配料时长
																				// （绿）
	private TextView tvTempHumi; // （温度&湿度）
	private TvHandler tvHandler = new TvHandler(); // 信息显示处理handler
	private int timecostRed, timecostBlue, timecostGreen; // 配料时长值
	private SensorManager sm; // 传感器管理
	private Sensor temprature; // 温度sensor
	private Sensor humidity; // 湿度sensor
	private MySensorEventListener listener; // 传感器监听器
	private String strTemprature = "温度："; // 温度
	private String strHumidity = "湿度："; // 湿度
	private int tmpTemprature = 0; // 温度值
	private int tmpHumidity = 0; // 湿度值
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

	// 初始化数据
	private void initData() {
		iwsApp = (IwsApplication) getApplication();
		board = iwsApp.getBoard();
		sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		boardBroadcastReceiver = new UpdateBroadCastReceiver();
		temprature = sm.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
		humidity = sm.getDefaultSensor(Sensor.TYPE_LIGHT);
		listener = new MySensorEventListener();
		if (temprature == null) {
			Toast.makeText(this, "温感不存在", Toast.LENGTH_SHORT).show();
		} else {
			sm.registerListener(listener, temprature,
					SensorManager.SENSOR_DELAY_NORMAL);
		}
		if (humidity == null) {
			Toast.makeText(this, "湿度传感不存在", Toast.LENGTH_SHORT).show();
		} else {
			sm.registerListener(listener, humidity,
					SensorManager.SENSOR_DELAY_NORMAL);
		}
	}

	// 初始化UI
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

		// 开启访问服务
		startService(new Intent(this, UpdateService.class));

		// 注册广播接收器
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.ACTION_DATA_FIND);
		filter.addAction(Constant.ACTION_DATA_UPDATE);
		filter.addAction(Intent.ACTION_TIME_TICK);
		registerReceiver(boardBroadcastReceiver, filter);
	}

	@Override
	protected void onDestroy() {
		stopService(new Intent(this, UpdateService.class));// 停止服务
		unregisterReceiver(boardBroadcastReceiver);// 注销面板广播接收器
		sm.unregisterListener(listener);// 注销传感器监听
		super.onDestroy();
	}

	/**
	 * 
	 * @author yuanye 动态广播接收器：处理服务程序发送过来的广播
	 */
	class UpdateBroadCastReceiver extends BroadcastReceiver {

		@SuppressWarnings("unchecked")
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Constant.ACTION_DATA_FIND)) {
				// 更新看板UI
				tvHandler.sendEmptyMessage(0);
			} else if (intent.getAction().equals(Constant.ACTION_DATA_UPDATE)) {
				tvHandler.sendEmptyMessage(1);
			} else if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
				count++;
				Log.e(tag, "count:"+count);
				if (count > 10) {
					Log.e(tag, "温湿度更新开关打开");
					flagTemprature = true;
					flagHumidity = true;
					count = 0;
				}
				if (red) {
					timecostRed++;
					tvTimeCostRed.setText("" + timecostRed + "分钟");
				}
				if (blue) {
					timecostBlue++;
					tvTimeCostBlue.setText("" + timecostBlue + "分钟");
				}
				if (green) {
					timecostGreen++;
					tvTimeCostGreen.setText("" + timecostGreen + "分钟");
				}
			}
		}

	}

	// 更新面板工单信息
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

	// 传感器监听器
	public class MySensorEventListener implements SensorEventListener {

		@Override
		public void onSensorChanged(SensorEvent event) {
			int type = event.sensor.getType();
			if (type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
				float value = event.values[0];
				DecimalFormat df = new DecimalFormat("#.0");
				if (value <= 100) {
					int temp = (int) value;
					// 正负温差大于1度
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
					strTemprature = "温度：" + df.format(value) + "℃";
				} else if (value > 100) {
					int temp = (int) (value / 100);
					// 正负温差大于1度
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
					strHumidity = "湿度：" + df.format(value / 100) + "%";
				}
				tvTempHumi.setText(strTemprature + "\n" + strHumidity);
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}

	}

}
