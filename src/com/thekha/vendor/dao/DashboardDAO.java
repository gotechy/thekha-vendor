package com.thekha.vendor.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.net.http.AndroidHttpClient;

import com.thekha.vendor.bean.Dashboard;

public class DashboardDAO {
	
	String TAG_STATUS = "status";
	String TAG_BUSINESSNAME = "businessName";
	String TAG_PROFILECOMPLETENESS = "profileCompletness";
	String TAG_PROFILEVIEWS = "profileViews";
	String TAG_NOOFDEALSACTIVE = "totalActiveDeals";
	String TAG_NOOFDEALS = "totalDeals";
	String TAG_CREDITSBALANCE = "creditsBalance";
	String TAG_ADVERTSIING = "advertising";
	String TAG_BANNER = "banner";
	String TAG_CLICKS = "clicks";

	public Dashboard updateDashboard(String uid, String bid) throws ClientProtocolException,
			IOException, JSONException {
		String json = "";
		List<NameValuePair> reqParams = new ArrayList<NameValuePair>();
		reqParams.add(new BasicNameValuePair(LoginDAO.TAG_USERID, uid));
		reqParams.add(new BasicNameValuePair(BusinessDAO.TAG_BID, bid));
		
		AndroidHttpClient httpClient = AndroidHttpClient.newInstance("");
		// attach service to URL
		String URL = "http://www.gotechy.in/demo/thekha/dashboard.php";
		BasicResponseHandler responseHandler = new BasicResponseHandler();
		String paramString = URLEncodedUtils.format(reqParams, "utf-8");
		URL += "?" + paramString;
		HttpGet getRequest = new HttpGet(URL);
		json = httpClient.execute(getRequest, responseHandler);
		httpClient.close();
		if (json.isEmpty()) {
			return null;
		}
		JSONTokener tokener = new JSONTokener(json);
		JSONObject jsonObj = new JSONObject(tokener);
		return new Dashboard(Integer.valueOf(jsonObj.getString(TAG_STATUS)),
				jsonObj.getString(TAG_BUSINESSNAME),
				Integer.valueOf(jsonObj.getString(TAG_PROFILECOMPLETENESS)),
				Integer.valueOf(jsonObj.getString(TAG_PROFILEVIEWS)),
				Integer.valueOf(jsonObj.getString(TAG_NOOFDEALSACTIVE)),
				Integer.valueOf(jsonObj.getString(TAG_NOOFDEALS)),
				Integer.valueOf(jsonObj.getString(TAG_CREDITSBALANCE)),
				Integer.valueOf(jsonObj.getString(TAG_ADVERTSIING)),
				Integer.valueOf(jsonObj.getString(TAG_BANNER)),
				Integer.valueOf(jsonObj.getString(TAG_CLICKS))
				);
	}

}
