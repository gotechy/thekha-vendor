package com.thekha.vendor.dao;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.thekha.vendor.bean.Prices;
import com.thekha.vendor.util.ServiceHandler;

public class PricesDAO {
	Prices dp;
	String jsonResp;

	private static final String TAG_REGULAR = "regular";
	private static final String TAG_SEPCIAL = "special";
	private static final String TAG_TOPLISTING = "topListing";
	private static final String TAG_HOMEPAGEBANNER = "homePageBanner";
	private static final String TAG_CATEGORYBANNER = "categoryBanner";
	private static final String TAG_SMSCOUNT = "smscount";
	private static final String TAG_EMAILCOUNT = "emailcount";

	public Prices read() throws JSONException, ClientProtocolException, IOException{
		
		ServiceHandler sh = new ServiceHandler();
		jsonResp = sh.makeServiceCall(ServiceHandler.GET_PRICES_SERVICE, ServiceHandler.GET);
		inflatePricesObject();
		return dp;
	}

	private void inflatePricesObject() throws JSONException {
		JSONTokener tokener = new JSONTokener(jsonResp);
		JSONArray jsonArr = new JSONArray(tokener);
		JSONObject jsonObj = jsonArr.getJSONObject(0);
					
		dp = new Prices(
				jsonObj.getString(TAG_REGULAR), 
				jsonObj.getString(TAG_SEPCIAL),
				jsonObj.getString(TAG_TOPLISTING),
				jsonObj.getString(TAG_HOMEPAGEBANNER),
				jsonObj.getString(TAG_CATEGORYBANNER),
				jsonObj.getString(TAG_SMSCOUNT),
				jsonObj.getString(TAG_EMAILCOUNT));
	}
}
