package com.thekha.vendor.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;

import com.thekha.vendor.bean.Address;
import com.thekha.vendor.bean.Business;
import com.thekha.vendor.bean.Facilities;
import com.thekha.vendor.util.ServiceHandler;

public class BusinessDAO {
	Business b;
	String jsonResp;
	private final String cacheFileName = "business";
		
	// ID tags
	public static final String TAG_BID = "business_id";
	public static final String TAG_AID = "address_id";
	public static final String TAG_FID = "facilities_id";
	
	// Business Tags
	private static final String TAG_NAME = "name";
	private static final String TAG_TYPE = "type";
	private static final String TAG_IMAGEURL = "imageURL";
	private static final String TAG_PHONE1 = "phone1";
	private static final String TAG_PHONE2 = "phone2";
	private static final String TAG_EMAIL = "business_email";
	private static final String TAG_WEBSITE = "website";
	private static final String TAG_FACEBOOK = "facebook";
	
	// Address Tags
	private static final String TAG_LINE1 = "line1";
	private static final String TAG_LINE2 = "line2";
	private static final String TAG_LOCALITY = "locality";
	private static final String TAG_CITY = "city";
	private static final String TAG_STATE = "state";
	private static final String TAG_COUNTRY = "country";
	private static final String TAG_PIN = "pin";
	
	// Facilities tags
	private static final String TAG_AC = "ac";
	private static final String TAG_SA = "sa";
	private static final String TAG_VP = "vp";
	private static final String TAG_CC = "cc";
	private static final String TAG_VEG = "veg";
	private static final String TAG_NONVEG = "nonveg";
	
	public Business read(String uid) throws JSONException, ClientProtocolException, IOException{
		List<NameValuePair> reqParams = new ArrayList<NameValuePair>();
		reqParams.add(new BasicNameValuePair(LoginDAO.TAG_USERID, uid));
		
		ServiceHandler sh = new ServiceHandler();
		jsonResp = sh.makeServiceCall(ServiceHandler.GET_PROFILE_SERVICE, ServiceHandler.GET, reqParams);
		inflateBusinessObject();
		return b;
	}
	
	public boolean update(Business business) throws ClientProtocolException, IOException  {
		List<NameValuePair> reqParams = new ArrayList<NameValuePair>();
		
		reqParams.add(new BasicNameValuePair(TAG_FID, String.valueOf(business.getFacilities().getId())));
		reqParams.add(new BasicNameValuePair(TAG_AC, business.getFacilities().isAc()?"1":"0"));
		reqParams.add(new BasicNameValuePair(TAG_SA, business.getFacilities().isSa()?"1":"0"));
		reqParams.add(new BasicNameValuePair(TAG_CC, business.getFacilities().isCc()?"1":"0"));
		reqParams.add(new BasicNameValuePair(TAG_VP, business.getFacilities().isVp()?"1":"0"));
		reqParams.add(new BasicNameValuePair(TAG_VEG, business.getFacilities().isVeg()?"1":"0"));
		reqParams.add(new BasicNameValuePair(TAG_NONVEG, business.getFacilities().isNonVeg()?"1":"0"));
		
		reqParams.add(new BasicNameValuePair(TAG_AID, String.valueOf(business.getAddress().getId())));
		reqParams.add(new BasicNameValuePair(TAG_LINE1, business.getAddress().getLine1()));
		reqParams.add(new BasicNameValuePair(TAG_LINE2, business.getAddress().getLine2()));
		reqParams.add(new BasicNameValuePair(TAG_LOCALITY, business.getAddress().getLocality()));
		reqParams.add(new BasicNameValuePair(TAG_CITY, business.getAddress().getCity()));
		reqParams.add(new BasicNameValuePair(TAG_STATE, business.getAddress().getState()));
		reqParams.add(new BasicNameValuePair(TAG_COUNTRY, business.getAddress().getCountry()));
		reqParams.add(new BasicNameValuePair(TAG_PIN, business.getAddress().getPin()));
		
		reqParams.add(new BasicNameValuePair(TAG_BID, String.valueOf(business.getId())));
		reqParams.add(new BasicNameValuePair(TAG_NAME, business.getName()));
		reqParams.add(new BasicNameValuePair(TAG_TYPE, business.getType()));
		reqParams.add(new BasicNameValuePair(TAG_IMAGEURL, business.getImageURL()));
		reqParams.add(new BasicNameValuePair(TAG_PHONE1, business.getPhone1()));
		reqParams.add(new BasicNameValuePair(TAG_PHONE2, business.getPhone2()));
		reqParams.add(new BasicNameValuePair(TAG_EMAIL, business.getEmail()));
		reqParams.add(new BasicNameValuePair(TAG_WEBSITE, business.getWebsite()));
		reqParams.add(new BasicNameValuePair(TAG_FACEBOOK, business.getFacebook()));
		
		ServiceHandler sh = new ServiceHandler();
		jsonResp = sh.makeServiceCall(ServiceHandler.UPDATE_PROFILE_SERVICE, ServiceHandler.POST, reqParams);
		if (jsonResp != null) {
			return true;
		}
		return false;
	}
	
	public void cache(Context c) throws IOException, NullPointerException{
		File cacheFile = new File(c.getCacheDir()+File.separator+cacheFileName);

		if(jsonResp != null){
			FileWriter fw = new FileWriter(cacheFile);
			fw.write(jsonResp.toString()); fw.flush(); fw.close();
		}else
			throw new NullPointerException();
	}
	
	public Business readFromCache(Context c) throws IOException, JSONException{
		File cacheFile = new File(c.getCacheDir()+File.separator+cacheFileName);

		FileReader fr = new FileReader(cacheFile);
		BufferedReader br = new BufferedReader(fr);
		if(br.ready()){
			jsonResp = br.readLine();
		}
		br.close();
		fr.close();
		inflateBusinessObject();
		return b;
	}
	
	private void inflateBusinessObject() throws JSONException{
		JSONTokener tokener = new JSONTokener(jsonResp);
		JSONArray jsonArr = new JSONArray(tokener);
		JSONObject jsonObj = jsonArr.getJSONObject(0);
					
		Facilities f = new Facilities(
				Integer.valueOf(jsonObj.getString(TAG_FID)),
				jsonObj.getString(TAG_AC).equals("1"),
				jsonObj.getString(TAG_SA).equals("1"),
				jsonObj.getString(TAG_VP).equals("1"),
				jsonObj.getString(TAG_CC).equals("1"),
				jsonObj.getString(TAG_VEG).equals("1"),
				jsonObj.getString(TAG_NONVEG).equals("1")
				);
		
		Address a = new Address(
				Integer.valueOf(jsonObj.getString(TAG_AID)),
				jsonObj.getString(TAG_LINE1),
				jsonObj.getString(TAG_LINE2),
				jsonObj.getString(TAG_LOCALITY),
				jsonObj.getString(TAG_CITY),
				jsonObj.getString(TAG_STATE),
				jsonObj.getString(TAG_COUNTRY),
				jsonObj.getString(TAG_PIN)
				);
		
		b = new Business(
				Integer.valueOf(jsonObj.getString(TAG_BID)), 
				jsonObj.getString(TAG_NAME),
				jsonObj.getString(TAG_TYPE),
				jsonObj.getString(TAG_IMAGEURL),
				a,
				jsonObj.getString(TAG_PHONE1),
				jsonObj.getString(TAG_PHONE2),
				jsonObj.getString(TAG_EMAIL),
				jsonObj.getString(TAG_WEBSITE),
				jsonObj.getString(TAG_FACEBOOK),
				f
		);		
	}

}
