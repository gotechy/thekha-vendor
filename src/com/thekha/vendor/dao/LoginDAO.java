package com.thekha.vendor.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;

import com.thekha.vendor.util.ServiceHandler;

public class LoginDAO {
	
	
	private final String cacheFileName = "login";
	
	// JSON Node names - request
	private static final String TAG_USERNAME = "username";
	private static final String TAG_PASSWORD = "password";

	// JSON Node names - response
	public static final String TAG_USERID = "id";
	//private static final String TAG_CREATEDBY = "created_by";
	//private static final String TAG_UPDATEDBY = "updated_by";
	//private static final String TAG_CREATEDDATE = "created_date";
	//private static final String TAG_UPDATEDDATE = "updated_date";
	//private static final String TAG_LASTLOGIN = "last_login";

	public String login(Context c, String username, String password) throws JSONException, IOException{
		ServiceHandler sh = new ServiceHandler();
		
		List<NameValuePair> reqParams = new ArrayList<NameValuePair>();
		reqParams.add(new BasicNameValuePair(TAG_USERNAME, username));
		reqParams.add(new BasicNameValuePair(TAG_PASSWORD, password));
		
		String jsonResp = sh.makeServiceCall(ServiceHandler.LOGIN_SERVICE, ServiceHandler.POST, reqParams);
		
		cache(c, jsonResp);
		
		return parseJSON(jsonResp);
		
	}
	
	private String parseJSON(String json) throws JSONException{
		JSONTokener tokener = new JSONTokener(json);
		JSONArray jsonArr = new JSONArray(tokener);
		JSONObject jsonObj = jsonArr.getJSONObject(0);
		return jsonObj.getString(TAG_USERID);
	}
	
	public void cache(Context c, String str) throws IOException{
		File cacheFile = new File(c.getCacheDir()+File.separator+cacheFileName);
		FileWriter fw = new FileWriter(cacheFile);
		fw.write(str);
		fw.flush();
		fw.close();
	}
	
	public void logout(Context c){
		File cacheFile = new File(c.getCacheDir()+File.separator+cacheFileName);
		if(cacheFile.exists())
			cacheFile.delete();
	}

	public String loginFromCache(Context c) throws IOException, JSONException {
	String json = null;
		File cacheFile = new File(c.getCacheDir()+File.separator+cacheFileName);
		FileReader fr = new FileReader(cacheFile);
			BufferedReader br = new BufferedReader(fr);
			if(br.ready()){
			json = br.readLine();
			br.close();
			fr.close();
			
			}
		return parseJSON(json);
	}

}