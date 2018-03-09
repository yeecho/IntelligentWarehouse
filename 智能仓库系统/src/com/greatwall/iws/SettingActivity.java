package com.greatwall.iws;

import com.greatwall.iws.constant.Constant;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

public class SettingActivity extends Activity {
	
	private SharedPreferences sp;
	private Switch switchInitdev,switchCheckup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_setting);
		
		sp = getSharedPreferences(Constant.SHARE_PREF, MODE_PRIVATE);
		boolean isChecked = sp.getBoolean(Constant.SETTING_INITDEV, false);
		switchInitdev = (Switch) findViewById(R.id.switch_initdev);
		switchCheckup = (Switch) findViewById(R.id.switch_checkup);
		switchInitdev.setChecked(isChecked);
		switchInitdev.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					Intent intent = new Intent(Constant.ACTION_INITDEV);
					sendBroadcast(intent);
				}
				Editor et = sp.edit();
				et.putBoolean(Constant.SETTING_INITDEV, isChecked);
				et.commit();
			}
		});
		switchCheckup.setChecked(false);
		switchCheckup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Intent intent = new Intent();
				if (isChecked) {
					intent.setAction(Constant.ACTION_LIGHT_UP_ALL);
				}else{
					intent.setAction(Constant.ACTION_LIGHT_DOWN_ALL);
				}
				sendBroadcast(intent);
			}
		});
	}
	
	public void reset(View view){
		Intent intent = new Intent(Constant.ACTION_BACKUP);
		sendBroadcast(intent);
	}
	
	public void lightall(View view){
		Intent intent = new Intent(Constant.ACTION_LIGHT_UP_ALL);
		sendBroadcast(intent);
	}
}
