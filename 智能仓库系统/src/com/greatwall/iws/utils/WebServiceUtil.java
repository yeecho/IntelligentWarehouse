package com.greatwall.iws.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.greatwall.iws.constant.Constant;
import com.greatwall.iws.model.DataModelInterface;

public class WebServiceUtil {
	Context mContxt;
	Handler mHandler;
	public WebServiceUtil(Context mContxt,Handler mHandler){
		this.mContxt = mContxt;
		this.mHandler = mHandler;
	}
	
    String[] propertyOhUpdate = {"ckgdh", "ccpn", "ckms", "cksl", "ckczy"};
	public void ohUpdate(String ckgdh, String ccpn, String ckms, String cksl, String ckczy){
		List<Object> mList = new ArrayList<Object>();
		mList.add(ckgdh);
		mList.add(ccpn);
		mList.add(ckms);
		mList.add(cksl);
		mList.add(ckczy);
		UserGetDataTask mUserGetDataTask = new UserGetDataTask(Constant.WEB_OUT_UPDATE, Constant.OhUpdate, propertyOhUpdate, mList, null);
		mUserGetDataTask.execute((Void) null);
	}

    String[] propertyOutHouse = {"ckgdh", "ccpn", "ckczy"};
	public void outHouse(String ckgdh, String ccpn, String ckczy){
		List<Object> mList = new ArrayList<Object>();
		mList.add(ckgdh);
		mList.add(ccpn);
		mList.add(ckczy);
		UserGetDataTask mUserGetDataTask = new UserGetDataTask(Constant.WEB_OUT, Constant.OutHouse, propertyOutHouse, mList, null);
		mUserGetDataTask.execute((Void) null);
	}

    String[] propertyEhUpdate = {"gysid", "ccpn", "scsj", "xlh", "drksl", "rkczy"};
	public void ehUpdate(String gysid, String ccpn, String scsj, String xlh, String drksl, String rkczy){
		List<Object> mList = new ArrayList<Object>();
		mList.add(gysid);
		mList.add(ccpn);
		mList.add(scsj);
		mList.add(xlh);
		mList.add(drksl);
		mList.add(rkczy);
		UserGetDataTask mUserGetDataTask = new UserGetDataTask(Constant.WEB_ENTER_UPDATE, Constant.EhUpdate, propertyEhUpdate, mList, null);
		mUserGetDataTask.execute((Void) null);
	}
	
    String[] propertyEnterHouse = {"ccpn", "rkczy"};
	public void enterHouse(String ccpn, String rkczy){
		List<Object> mList = new ArrayList<Object>();
		mList.add(ccpn);
		mList.add(rkczy);
		UserGetDataTask mUserGetDataTask = new UserGetDataTask(Constant.WEB_ENTER, Constant.EnterHouse, propertyEnterHouse, mList, null);
		mUserGetDataTask.execute((Void) null);
	}
	
	//获取库位的库存数量
	String[] propertyGetMount = {"kw"};
	public void getMount(String kw){
		List<Object> mList = new ArrayList<Object>();
		mList.add(kw);
		UserGetDataTask mUserGetDataTask = new UserGetDataTask(Constant.WEB_GET_MOUNT, Constant.getTableKwDataNum, propertyGetMount, mList, null);
		mUserGetDataTask.execute((Void) null);
	}
	
	//获取出库数据
	public void getOutHouseDatas(){
		UserGetDataTask mUserGetDataTask = new UserGetDataTask(Constant.WEB_GETOUT_DATA, Constant.GetOutHouseDataPre, null, null, null);
		mUserGetDataTask.execute((Void) null);
	}
	
	//获取入库的推荐库位
	public void getRecommendKW(){
		UserGetDataTask mUserGetDataTask = new UserGetDataTask(Constant.WEB_GETENTER_DATA, Constant.GetRecommendKW, null, null, null);
		mUserGetDataTask.execute((Void) null);
	}
	
	//获取子机逻辑地址初始化数据
	public void getInitDev(){
		UserGetDataTask mUserGetDataTask = new UserGetDataTask(Constant.WEB_GET_INITDEV, Constant.getInitDev, null, null, null);
		mUserGetDataTask.execute((Void) null);
	}
	
	public class UserGetDataTask extends AsyncTask<Void, Void, Boolean> {

		private String[] addProperty;
        private List<Object> addPropertyValue;
        private String fun;
        private int whatType;
        DataModelInterface mDataModelInterface;

		UserGetDataTask(int what, String fun, String[] addProperty, List<Object> addPropertyValue, DataModelInterface mDataModelInterface) {
			this.fun = fun;
			this.addPropertyValue = addPropertyValue;
			this.mDataModelInterface = mDataModelInterface;
			this.addProperty = addProperty;
			this.whatType = what;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			SoapObject soapObject = new SoapObject(Constant.nameSpace,
					fun);
			if(addProperty!=null){
			    for (int i = 0; i < addProperty.length; i++) {
				    soapObject.addProperty(addProperty[i], addPropertyValue.get(i));
			    }
			}
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(soapObject);// envelope.bodyOut=request;
			HttpTransportSE httpTranstation = new HttpTransportSE(Constant.serviceURL);
			try {

				httpTranstation
						.call(Constant.nameSpace + "/" + fun, envelope);
				if(envelope.getResponse() == null){
					sendHandler(false, null, "获取数据失败");
				    Log.i("yuanye","result == null");
		            return false;
				}
				
				String response = ""+envelope.getResponse();
//				Log.d("yuanye", "response:"+response);
				String status = "";
				String resultcode = "";
				JSONArray mJsonArray = null;
				JSONObject mJson = null;
				try {
					mJson = new JSONObject(response);
					if(mJson.has("status")) {
					    status = mJson.getString("status");
					}
					if(mJson.has("resultcode")) {
						resultcode = ""+mJson.getString("resultcode");
					}
					if(mJson.has("resultData")) {
					    mJsonArray = mJson.getJSONArray("resultData");
					    int i = mJsonArray.length();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					sendHandler(false, null, "获取数据失败");
		            return false;
				}
				if(status.equals("")){
					sendHandler(false, null, "获取数据失败");
		            return false;
				}else{
					if(mJsonArray!=null){
						if (mJsonArray.toString().equals("[]")) {
							sendHandler(false, null, resultcode);
						}else{
							sendHandler(true, mJsonArray, resultcode);
						}
					} else{
						sendHandler(false, null, resultcode);
					}
					return true;
				}

			} catch (IOException e) {
				e.printStackTrace();
				sendHandler(false, null, ""+e.toString());
			} catch (XmlPullParserException e) {
				e.printStackTrace();
				sendHandler(false, null, ""+e.toString());
			}
			return false;
		}
		private void sendHandler(boolean success, Object getStr, String mesg){
			Message msg = Message.obtain();
			msg.what = whatType;
			if (success) {
				msg.obj = getStr;
			}
			mHandler.sendMessage(msg);
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			
		}

		@Override
		protected void onCancelled() {
			
		}
	}
}
