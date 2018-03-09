package com.greatwall.iws.utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.array;
import android.util.Log;
import android.widget.Toast;

import com.greatwall.iws.IwsApplication;
import com.greatwall.iws.bean.WoBean;

public class JsonUtil {
	
	private static final String tag = "JsonUtil";

	//判断接收到的JSONArray跟前一条接收到的数据是否有变化
	public static boolean isChanged(IwsApplication iws, JSONArray ja) {
		if (iws.getJa().toString().equals(ja.toString())) {
			return false;
		}
		return true;
	}

	//从JSONArray中解析出库位号的list
	public static ArrayList<String> getRkKw(JSONArray ja) {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < ja.length(); i++) {
			JSONObject jo;
			try {
				jo = ja.getJSONObject(i);
				list.add(jo.getString("KW"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	//从接收到的出库jsonarray数据里解析出工单
	public static ArrayList<WoBean> getWoBeanList(JSONArray ja) {
		ArrayList<WoBean> list = new ArrayList<>();
		for (int i = 0; i < ja.length(); i++) {
			JSONObject jo;
			WoBean woBean = new WoBean();
			try {
				jo = ja.getJSONObject(i);
				String gdh = jo.getString("GDH");
				String cgy = jo.getString("userName");
				String kw = jo.getString("KW");
				String partNo = jo.getString("PartNo");
				int index = isContains(list, gdh);
				if (index >= 0) {//工单号已经在工单列表中，补充库位号和料号
					list.get(index).getKw().add(kw);
					list.get(index).getPartNo().add(partNo);
				}else{//出现了新的工单号，新增woBean
					woBean.setGdh(gdh);
					woBean.setCgy(cgy);
					woBean.getKw().add(kw);
					woBean.getPartNo().add(partNo);
					list.add(woBean);
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	//判断工单列表是否包含该工单号，若包含，返回工单号的位置，若不，返回-1
	public static int isContains(ArrayList<WoBean> list, String gdh) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getGdh().equals(gdh)) {
				return i;
			}
		}
		return -1;
	}
}
