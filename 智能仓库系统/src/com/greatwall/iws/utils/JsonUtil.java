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

	//�жϽ��յ���JSONArray��ǰһ�����յ��������Ƿ��б仯
	public static boolean isChanged(IwsApplication iws, JSONArray ja) {
		if (iws.getJa().toString().equals(ja.toString())) {
			return false;
		}
		return true;
	}

	//��JSONArray�н�������λ�ŵ�list
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

	//�ӽ��յ��ĳ���jsonarray���������������
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
				if (index >= 0) {//�������Ѿ��ڹ����б��У������λ�ź��Ϻ�
					list.get(index).getKw().add(kw);
					list.get(index).getPartNo().add(partNo);
				}else{//�������µĹ����ţ�����woBean
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

	//�жϹ����б��Ƿ�����ù����ţ������������ع����ŵ�λ�ã�����������-1
	public static int isContains(ArrayList<WoBean> list, String gdh) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getGdh().equals(gdh)) {
				return i;
			}
		}
		return -1;
	}
}
