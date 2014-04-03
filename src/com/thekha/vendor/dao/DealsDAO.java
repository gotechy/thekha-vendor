package com.thekha.vendor.dao;

import hirondelle.date4j.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;

import com.thekha.vendor.bean.Business;
import com.thekha.vendor.bean.Deals;
import com.thekha.vendor.bean.DealsPlacement;
import com.thekha.vendor.util.ServiceHandler;

public class DealsDAO {
	
	Deals d;
	List<Deals> deals;
	String jsonResp;
	
	// Deal Tags
	public static final String TAG_DEALID = "deals_id";
	public static final String TAG_TITLE = "title";
	public static final String TAG_DESCRIPTION = "description";
	public static final String TAG_CODE = "code";
	public static final String TAG_IMAGEURL = "imageURL";
	public static final String TAG_FROM = "deals_from";
	public static final String TAG_TO = "deals_to";
	public static final String TAG_SMSCOUNT = "smscount";
	public static final String TAG_EMAILCOUNT = "emailcount";
	public static final String TAG_CREATEDBY = "created_by";
	public static final String TAG_UPDATEDBY = "updated_by";
	public static final String TAG_UPDATEDON = "updated_date";
	
	// Deal placement tags
	public static final String TAG_PLACEMENTID = "placement_id";
	public static final String TAG_REGULAR = "regular";
	public static final String TAG_SEPCIAL = "special";
	public static final String TAG_TOPLISTING = "topListing";
	public static final String TAG_HOMEPAGEBANNER = "homePageBanner";
	public static final String TAG_CATEGORYBANNER = "categoryBanner";

	public Boolean add(String uid, Deals deal) throws ClientProtocolException, IOException {
		
		List<NameValuePair> reqParams = new ArrayList<NameValuePair>();
		reqParams.add(new BasicNameValuePair(BusinessDAO.TAG_BID, uid));
		
		reqParams.add(new BasicNameValuePair(TAG_REGULAR, deal.getPlacement().isRegular()?"1":"0"));
		reqParams.add(new BasicNameValuePair(TAG_SEPCIAL, deal.getPlacement().isSpecial()?"1":"0"));
		reqParams.add(new BasicNameValuePair(TAG_TOPLISTING, deal.getPlacement().isTopListing()?"1":"0"));
		reqParams.add(new BasicNameValuePair(TAG_HOMEPAGEBANNER, deal.getPlacement().isHomePageBanner()?"1":"0"));
		reqParams.add(new BasicNameValuePair(TAG_CATEGORYBANNER, deal.getPlacement().isCategoryBanner()?"1":"0"));
		
		reqParams.add(new BasicNameValuePair(TAG_TITLE, deal.getTitle()));
		reqParams.add(new BasicNameValuePair(TAG_DESCRIPTION, deal.getDescription()));
		// TODO - image URL for deals
		reqParams.add(new BasicNameValuePair(TAG_IMAGEURL, ""));
		reqParams.add(new BasicNameValuePair(TAG_CODE, deal.getCode()));
		reqParams.add(new BasicNameValuePair(TAG_FROM, deal.getFrom().toString()));
		reqParams.add(new BasicNameValuePair(TAG_TO, deal.getTo().toString()));
		reqParams.add(new BasicNameValuePair(TAG_SMSCOUNT, String.valueOf(deal.getSMSCount())));
		reqParams.add(new BasicNameValuePair(TAG_EMAILCOUNT, String.valueOf(deal.getEmailCount())));
		
		reqParams.add(new BasicNameValuePair(TAG_CREATEDBY, String.valueOf(uid)));
		reqParams.add(new BasicNameValuePair(TAG_UPDATEDBY, String.valueOf(uid)));
		reqParams.add(new BasicNameValuePair(TAG_UPDATEDON, DateTime.now(TimeZone.getDefault()).toString()));

		ServiceHandler sh = new ServiceHandler();
		jsonResp = sh.makeServiceCall(ServiceHandler.CREATE_DEAL_SERVICE, ServiceHandler.POST, reqParams);
		if (jsonResp.isEmpty()) {
			return false;
		}
		return true;
	}

	public List<Deals> read(Context c, String uid) throws JSONException, ClientProtocolException, IOException{
		/*
		 * Read business profile from cache to get its deals, if data not in cache, invoke get profile service.
		 */
		
		BusinessDAO bdao = new BusinessDAO();
		Business b;
		try {
			b = bdao.readFromCache(c);
		} catch (IOException e) {
			b = bdao.read(uid);
		} catch (JSONException e) {
			b = bdao.read(uid);
		}
		
		List<NameValuePair> reqParams = new ArrayList<NameValuePair>();
		reqParams.add(new BasicNameValuePair(BusinessDAO.TAG_BID, String.valueOf(b.getId())));
		
		ServiceHandler sh = new ServiceHandler();
		jsonResp = sh.makeServiceCall(ServiceHandler.GET_DEALS_SERVICE, ServiceHandler.GET, reqParams);
		inflateDealsList();
		return deals;
	}
	
	public boolean update(Deals deal) throws ClientProtocolException, IOException {
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
		// TODO - image URL for deals
		reqParams.add(new BasicNameValuePair(TAG_IMAGEURL, ""));
		reqParams.add(new BasicNameValuePair(TAG_CODE, deal.getCode()));
		reqParams.add(new BasicNameValuePair(TAG_FROM, deal.getFrom().toString()));
		reqParams.add(new BasicNameValuePair(TAG_TO, deal.getTo().toString()));
		reqParams.add(new BasicNameValuePair(TAG_SMSCOUNT, String.valueOf(deal.getSMSCount())));
		reqParams.add(new BasicNameValuePair(TAG_EMAILCOUNT, String.valueOf(deal.getEmailCount())));
				
		ServiceHandler sh = new ServiceHandler();
		jsonResp = sh.makeServiceCall(ServiceHandler.UPDATE_DEAL_SERVICE, ServiceHandler.POST, reqParams);
		if (jsonResp.isEmpty()) {
			return false;
		}
		return true;
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
				dp,
				Integer.parseInt(jsonObj.getString(TAG_SMSCOUNT)),
				Integer.parseInt(jsonObj.getString(TAG_EMAILCOUNT))
		);		
	}
}
