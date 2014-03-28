package com.thekha.vendor.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;

import android.net.http.AndroidHttpClient;

public class ServiceHandler {

	static String response = null;
	public final static int GET = 1;
	public final static int POST = 2;
	
	private String URL = "http://gotechy.in/demo/thekha/api/services/data/thekha/v2/";
	
	private static String secret = "12345";
	private static final String TAG_SECRET = "secret";
	
	public static final String LOGIN_SERVICE = "login/UserLogin";
	public static final String GET_PROFILE_SERVICE = "getProfile/Profile";
	public static final String UPDATE_PROFILE_SERVICE = "profile/UpdateProfile";
	public static final String CREATE_PROFILE_SERVICE = "profile/CreateProfile";
	public static final String CREATE_DEAL_SERVICE = "createDeal/CreateDeal";
	public static final String GET_DEALS_SERVICE = "getDeal/Deal";
	public static final String UPDATE_DEAL_SERVICE = "updateDeal/Deal";
	public static final String POST_QUERY_SERVICE = ""; //TODO
	

	public ServiceHandler() {

	}
	
	/**
	 * Making service call
	 * 
	 * @url - url to make request
	 * @method - http request method
	 * */
	public String makeServiceCall(String service, int method) {
		return this.makeServiceCall(service, method, null);
	}

	/**
	 * Making service call
	 * 
	 * @url - url to make request
	 * @method - http request method
	 * @params - http request params
	 * */
	public String makeServiceCall(String service, int method,
			List<NameValuePair> params) {
		try {
			// http client
			AndroidHttpClient httpClient = AndroidHttpClient.newInstance("");
			
			// attach service to URL
			URL += service;
			BasicResponseHandler responseHandler = new BasicResponseHandler();
			String json = "";
			// Checking http request method type
			if (method == POST) {
				URL += "?" + TAG_SECRET +"="+secret;
				HttpPost postRequest = new HttpPost(URL);
				if (params != null) {
					postRequest.setEntity(new UrlEncodedFormEntity(params));
				}
				json = httpClient.execute(postRequest, responseHandler);

			} else if (method == GET) {
				params.add(new BasicNameValuePair(TAG_SECRET, secret));
				String paramString = URLEncodedUtils.format(params, "utf-8");
				URL += "?" + paramString;
				HttpGet getRequest = new HttpGet(URL);
				json = httpClient.execute(getRequest, responseHandler);
			}
			httpClient.close();
			return json;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}