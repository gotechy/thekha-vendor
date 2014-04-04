package com.thekha.vendor.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import com.thekha.vendor.bean.Business;
import com.thekha.vendor.bean.Query;
import com.thekha.vendor.util.ServiceHandler;

public class ContactUsDAO {
	Business b;
	String jsonResp;
	
	// Query Tags
	private static final String TAG_NAME = "name";
	private static final String TAG_TYPE = "type";
	private static final String TAG_PHONE = "phone";
	private static final String TAG_EMAIL = "email";
	private static final String TAG_SUBJECT = "subject";
	private static final String TAG_DESCRIPTION = "description";
	
	public boolean add(String uid, Query query) throws ClientProtocolException, IOException {
		List<NameValuePair> reqParams = new ArrayList<NameValuePair>();
		reqParams.add(new BasicNameValuePair(LoginDAO.TAG_USERID, String.valueOf(uid)));
		
		reqParams.add(new BasicNameValuePair(TAG_NAME, query.getName()));
		reqParams.add(new BasicNameValuePair(TAG_TYPE, query.getType()));
		reqParams.add(new BasicNameValuePair(TAG_PHONE, query.getPhone()));
		reqParams.add(new BasicNameValuePair(TAG_EMAIL, query.getEmail()));
		reqParams.add(new BasicNameValuePair(TAG_SUBJECT, query.getSubject()));
		reqParams.add(new BasicNameValuePair(TAG_DESCRIPTION, query.getMessage()));
		
		ServiceHandler sh = new ServiceHandler();
		jsonResp = sh.makeServiceCall(ServiceHandler.CREATE_QUERY_SERVICE, ServiceHandler.POST, reqParams);
		if (jsonResp != null) {
			return true;
		}
		return false;
	}

}
