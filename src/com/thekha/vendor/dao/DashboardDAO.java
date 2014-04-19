package com.thekha.vendor.dao;

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

import com.thekha.vendor.bean.Dashboard;
import com.thekha.vendor.util.ServiceHandler;

public class DashboardDAO {
	
	String TAG_STATUS = "status";
	String TAG_BUSINESSNAME = "businessName";
	String TAG_PROFILECOMPLETENESS = "profileCompleteness";
	String TAG_PROFILEVIEWS = "profileViews";
	String TAG_NOOFDEALSACTIVE = "noOfDealsActive";
	String TAG_NOOFDEALS = "noOfDeals";
	String TAG_CREDITSBALANCE = "creditsBalance";
	String TAG_ADVERTSIING = "advertising";
	String TAG_BANNER = "banner";
	String TAG_CLICKS = "clicks";

	public Dashboard updateDashboard(String uid) throws ClientProtocolException,
			IOException, JSONException {
		/*ServiceHandler sh = new ServiceHandler();

		List<NameValuePair> reqParams = new ArrayList<NameValuePair>();
		reqParams.add(new BasicNameValuePair(LoginDAO.TAG_USERID, uid));

		String jsonResp = sh.makeServiceCall(ServiceHandler.LOGIN_SERVICE,
				ServiceHandler.POST, reqParams);
		if (jsonResp.isEmpty()) {
			return null;
		}
		JSONTokener tokener = new JSONTokener(jsonResp);
		JSONArray jsonArr = new JSONArray(tokener);
		JSONObject jsonObj = jsonArr.getJSONObject(0);
		
		return new Dashboard(jsonObj.getInt(TAG_STATUS),
				jsonObj.getString(TAG_BUSINESSNAME),
				jsonObj.getInt(TAG_PROFILECOMPLETENESS),
				jsonObj.getInt(TAG_PROFILEVIEWS),
				jsonObj.getInt(TAG_NOOFDEALSACTIVE),
				jsonObj.getInt(TAG_NOOFDEALS),
				jsonObj.getInt(TAG_CREDITSBALANCE),
				jsonObj.getInt(TAG_ADVERTSIING),
				jsonObj.getInt(TAG_BANNER),
				jsonObj.getInt(TAG_CLICKS)
				);
		*/
		
		return new Dashboard(1,
				"GAMMA DRINKS",
				69,
				123,
				2,
				4,
				15500,
				45,
				8,
				1230851
				);
	}

}
