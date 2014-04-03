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

import android.content.Context;

import com.thekha.vendor.bean.Deals;
import com.thekha.vendor.bean.Transaction;
import com.thekha.vendor.util.ServiceHandler;

public class TransactionDAO {

	Transaction t;
	List<Transaction> transactions;
	String jsonResp;

	// Tags for transaction
	private static final String TAG_TITLE = "title";
	private static final String TAG_AMOUNT = "amount";
	private static final String TAG_DESCRIPTION = "description";
	private static final String TAG_TIMESTAMP = "timestamp";

	private static final String TITLE_DEBIT = "Debited";

	public List<Transaction> read(Context c, String uid) throws JSONException, ClientProtocolException, IOException{

		List<NameValuePair> reqParams = new ArrayList<NameValuePair>();
		reqParams.add(new BasicNameValuePair(LoginDAO.TAG_USERID, uid));

		ServiceHandler sh = new ServiceHandler();
		jsonResp = sh.makeServiceCall(ServiceHandler.GET_TRANSACTIONS_SERVICE, ServiceHandler.GET, reqParams);
		inflateTransactionsList();
		return transactions;
	}

	public Boolean add(Deals d, Transaction t) throws ClientProtocolException, IOException {

		List<NameValuePair> reqParams = new ArrayList<NameValuePair>();
		reqParams.add(new BasicNameValuePair(DealsDAO.TAG_DEALID, String.valueOf(d.getId())));

		reqParams.add(new BasicNameValuePair(TAG_TITLE, TITLE_DEBIT));

		String desc = String.valueOf((t.getAmount()*-1))+" debited for deal "+d.getTitle();
		reqParams.add(new BasicNameValuePair(TAG_DESCRIPTION, desc));

		ServiceHandler sh = new ServiceHandler();
		jsonResp = sh.makeServiceCall(ServiceHandler.CREATE_DEAL_SERVICE, ServiceHandler.POST, reqParams);
		if (jsonResp.isEmpty()) {
			return false;
		}
		return true;
	}

	private void inflateTransactionsList() throws JSONException{
		JSONTokener tokener = new JSONTokener(jsonResp);
		JSONArray jsonArr = new JSONArray(tokener);
		transactions = new ArrayList<Transaction>();
		for(int i=0; i<jsonArr.length(); i++){
			JSONObject jsonObj = jsonArr.getJSONObject(i);
			inflateTransactionObject(jsonObj);
			transactions.add(t);
		}
	}

	private void inflateTransactionObject(JSONObject jsonObj) throws JSONException{

		t = new Transaction(
				jsonObj.getString(TAG_TITLE),
				jsonObj.getString(TAG_DESCRIPTION),
				Integer.valueOf(jsonObj.getString(TAG_AMOUNT)),
				jsonObj.getString(TAG_TIMESTAMP)
				);
	}
}
