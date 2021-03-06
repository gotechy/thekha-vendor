package com.thekha.vendor.dao;

import hirondelle.date4j.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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
import com.thekha.vendor.bean.DealsPlacement;
import com.thekha.vendor.bean.Transaction;
import com.thekha.vendor.util.ServiceHandler;

public class DealsDAO {
	
	Deals d;
	List<Deals> deals;
	String jsonResp;
	
	// Deal Tags
	public static final String TAG_DEALID = "deals_id";
	private static final String TAG_TITLE = "title";
	private static final String TAG_DESCRIPTION = "description";
	private static final String TAG_CODE = "code";
	private static final String TAG_IMAGEURL = "imageURL";
	private static final String TAG_FROM = "deals_from";
	private static final String TAG_TO = "deals_to";
	private static final String TAG_SMSCOUNT = "smscount";
	private static final String TAG_EMAILCOUNT = "emailcount";
	//private static final String TAG_CREATEDBY = "created_by";
	//private static final String TAG_UPDATEDBY = "updated_by";
	private static final String TAG_STATUS = "status";
		
	// Deal placement tags
	private static final String TAG_PLACEMENTID = "placement_id";
	private static final String TAG_REGULAR = "regular";
	private static final String TAG_SEPCIAL = "special";
	private static final String TAG_TOPLISTING = "topListing";
	private static final String TAG_HOMEPAGEBANNER = "homePageBanner";
	private static final String TAG_CATEGORYBANNER = "categoryBanner";

	public int add(String bid, String uid, Deals deal, Transaction t) throws ClientProtocolException, IOException, JSONException {
		
		List<NameValuePair> reqParams = new ArrayList<NameValuePair>();
		reqParams.add(new BasicNameValuePair(BusinessDAO.TAG_BID, bid));
		
		reqParams.add(new BasicNameValuePair(TAG_STATUS, deal.getStatus()));
		
		reqParams.add(new BasicNameValuePair(TAG_REGULAR, deal.getPlacement().isRegular()?"1":"0"));
		reqParams.add(new BasicNameValuePair(TAG_SEPCIAL, deal.getPlacement().isSpecial()?"1":"0"));
		reqParams.add(new BasicNameValuePair(TAG_TOPLISTING, deal.getPlacement().isTopListing()?"1":"0"));
		reqParams.add(new BasicNameValuePair(TAG_HOMEPAGEBANNER, deal.getPlacement().isHomePageBanner()?"1":"0"));
		reqParams.add(new BasicNameValuePair(TAG_CATEGORYBANNER, deal.getPlacement().isCategoryBanner()?"1":"0"));
		
		reqParams.add(new BasicNameValuePair(TAG_TITLE, deal.getTitle()));
		reqParams.add(new BasicNameValuePair(TAG_DESCRIPTION, deal.getDescription()));
		
		reqParams.add(new BasicNameValuePair(TAG_IMAGEURL, deal.getImageURL()));
		reqParams.add(new BasicNameValuePair(TAG_CODE, deal.getCode()));
		reqParams.add(new BasicNameValuePair(TAG_FROM, deal.getFrom().toString()));
		reqParams.add(new BasicNameValuePair(TAG_TO, deal.getTo().toString()));
		reqParams.add(new BasicNameValuePair(TAG_SMSCOUNT, String.valueOf(deal.getSMSCount())));
		reqParams.add(new BasicNameValuePair(TAG_EMAILCOUNT, String.valueOf(deal.getEmailCount())));
		
		reqParams.add(new BasicNameValuePair(LoginDAO.TAG_USERID, uid));
		reqParams.add(new BasicNameValuePair("ttitle", t.getTitle()));
		reqParams.add(new BasicNameValuePair("tamount", String.valueOf(t.getAmount())));
		reqParams.add(new BasicNameValuePair("tdescription", t.getDescription()));

		ServiceHandler sh = new ServiceHandler();
		jsonResp = sh.makeServiceCall(ServiceHandler.CREATE_DEAL_SERVICE, ServiceHandler.POST, reqParams);
		if (jsonResp.isEmpty()) {
			return (Integer) null;
		}
		Integer n = null;
		JSONTokener tokener = new JSONTokener(jsonResp);
		JSONArray jsonArr = new JSONArray(tokener);
		for(int i=0; i<jsonArr.length(); i++){
			JSONObject jobj = jsonArr.getJSONObject(i);
			Iterator itr = jobj.keys();
			while(itr.hasNext()){
				String str = (String) itr.next();
				n = jobj.getInt(str);
			}
		}
		return n;
	}

	public List<Deals> read(Context c, String uid) throws JSONException, ClientProtocolException, IOException{
		/*
		 * Read business profile from cache to get its deals, if data not in cache, invoke get profile service.
		 */
		/*
		BusinessDAO bdao = new BusinessDAO();
		Business b;
		try {
			b = bdao.readFromCache(c);
		} catch (IOException e) {
			b = bdao.read(uid);
		} catch (JSONException e) {
			b = bdao.read(uid);
		}*/
		
		List<NameValuePair> reqParams = new ArrayList<NameValuePair>();
		reqParams.add(new BasicNameValuePair(BusinessDAO.TAG_BID, uid));
		
		ServiceHandler sh = new ServiceHandler();
		jsonResp = sh.makeServiceCall(ServiceHandler.GET_DEALS_SERVICE, ServiceHandler.GET, reqParams);
		inflateDealsList();
		return deals;
	}
	
	public boolean update(Deals deal, String uid, Transaction t) throws ClientProtocolException, IOException, JSONException {
		List<NameValuePair> reqParams = new ArrayList<NameValuePair>();
		reqParams.add(new BasicNameValuePair(TAG_DEALID, String.valueOf(deal.getId())));
		reqParams.add(new BasicNameValuePair(TAG_PLACEMENTID, String.valueOf(deal.getPlacement().getId())));
		
		reqParams.add(new BasicNameValuePair(TAG_REGULAR, deal.getPlacement().isRegular()?"1":"0"));
		reqParams.add(new BasicNameValuePair(TAG_SEPCIAL, deal.getPlacement().isSpecial()?"1":"0"));
		reqParams.add(new BasicNameValuePair(TAG_TOPLISTING, deal.getPlacement().isTopListing()?"1":"0"));
		reqParams.add(new BasicNameValuePair(TAG_HOMEPAGEBANNER, deal.getPlacement().isHomePageBanner()?"1":"0"));
		reqParams.add(new BasicNameValuePair(TAG_CATEGORYBANNER, deal.getPlacement().isCategoryBanner()?"1":"0"));
		
		reqParams.add(new BasicNameValuePair(TAG_TITLE, deal.getTitle()));
		reqParams.add(new BasicNameValuePair(TAG_DESCRIPTION, deal.getDescription()));
		reqParams.add(new BasicNameValuePair(TAG_IMAGEURL, deal.getImageURL()));
		reqParams.add(new BasicNameValuePair(TAG_CODE, deal.getCode()));
		reqParams.add(new BasicNameValuePair(TAG_FROM, deal.getFrom().toString()));
		reqParams.add(new BasicNameValuePair(TAG_TO, deal.getTo().toString()));
		reqParams.add(new BasicNameValuePair(TAG_SMSCOUNT, String.valueOf(deal.getSMSCount())));
		reqParams.add(new BasicNameValuePair(TAG_EMAILCOUNT, String.valueOf(deal.getEmailCount())));
		reqParams.add(new BasicNameValuePair(TAG_STATUS, deal.getStatus()));
		
		reqParams.add(new BasicNameValuePair(LoginDAO.TAG_USERID, uid));
		reqParams.add(new BasicNameValuePair("ttitle", t.getTitle()));
		reqParams.add(new BasicNameValuePair("tamount", String.valueOf(t.getAmount())));
		reqParams.add(new BasicNameValuePair("tdescription", t.getDescription()));
				
		ServiceHandler sh = new ServiceHandler();
		jsonResp = sh.makeServiceCall(ServiceHandler.UPDATE_DEAL_SERVICE, ServiceHandler.POST, reqParams);
		if (jsonResp.isEmpty()) {
			return false;
		}
		Integer n = null;
		JSONTokener tokener = new JSONTokener(jsonResp);
		JSONArray jsonArr = new JSONArray(tokener);
		for(int i=0; i<jsonArr.length(); i++){
			JSONObject jobj = jsonArr.getJSONObject(i);
			Iterator itr = jobj.keys();
			while(itr.hasNext()){
				String str = (String) itr.next();
				n = jobj.getInt(str);
				if(n==1)
					return true;
			}
		}
		return false;
	}

	private void inflateDealsList() throws JSONException{
		JSONTokener tokener = new JSONTokener(jsonResp);
		JSONArray jsonArr = new JSONArray(tokener);
		deals = new ArrayList<Deals>();
		for(int i=0; i<jsonArr.length(); i++){
			JSONObject jsonObj = jsonArr.getJSONObject(i);
			inflateDealObject(jsonObj);
			deals.add(d);
		}
	}
	
	private void inflateDealObject(JSONObject jsonObj) throws JSONException{
		DealsPlacement dp = new DealsPlacement(
				jsonObj.getInt(TAG_PLACEMENTID),
				jsonObj.getString(TAG_REGULAR).equals("1"),
				jsonObj.getString(TAG_SEPCIAL).equals("1"),
				jsonObj.getString(TAG_TOPLISTING).equals("1"),
				jsonObj.getString(TAG_HOMEPAGEBANNER).equals("1"),
				jsonObj.getString(TAG_CATEGORYBANNER).equals("1")
				);
		d = new Deals(
				jsonObj.getInt(TAG_DEALID),
				jsonObj.getString(TAG_TITLE),
				jsonObj.getString(TAG_DESCRIPTION),
				jsonObj.getString(TAG_CODE),
				jsonObj.getString(TAG_IMAGEURL),
				new DateTime(jsonObj.getString(TAG_FROM)),
				new DateTime(jsonObj.getString(TAG_TO)),
				jsonObj.getString(TAG_STATUS),
				dp,
				Integer.parseInt(jsonObj.getString(TAG_SMSCOUNT)),
				Integer.parseInt(jsonObj.getString(TAG_EMAILCOUNT))
		);		
	}
}
