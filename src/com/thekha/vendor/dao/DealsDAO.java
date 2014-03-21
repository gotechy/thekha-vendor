package com.thekha.vendor.dao;

import hirondelle.date4j.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.thekha.vendor.bean.Deals;
import com.thekha.vendor.bean.DealsPlacement;
import com.thekha.vendor.util.ServiceHandler;

public class DealsDAO {
	
	Deals d;
	List<Deals> deals;
	String jsonResp;

	public static final String TAG_UID = "user_id";
	public static final String TAG_PLACEMENTID = "placement";
	
	// Deal Tags
	public static final String TAG_DEALSID = "deals_id";
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
	public static final String TAG_REGULAR = "regular";
	public static final String TAG_SEPCIAL = "special";
	public static final String TAG_TOPLISTING = "topListing";
	public static final String TAG_HOMEPAGEBANNER = "homePageBanner";
	public static final String TAG_CATEGORYBANNER = "categoryBanner";

	public Deals readSample(){
		d = new Deals ( "Free Chicken", "There would be a free chicken treat only for today!!!", "FBP11101", 
				"", DateTime.now(TimeZone.getDefault()), DateTime.now(TimeZone.getDefault()),
				new DealsPlacement( Boolean.FALSE , Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE), 500, 100);
		return d;
	}

	public Boolean add(String uid, Deals deal) {
		List<NameValuePair> reqParams = new ArrayList<NameValuePair>();
		reqParams.add(new BasicNameValuePair(TAG_UID, String.valueOf(uid)));
		
		reqParams.add(new BasicNameValuePair(TAG_REGULAR, deal.getPlacement().getRegular()?"1":"0"));
		reqParams.add(new BasicNameValuePair(TAG_SEPCIAL, deal.getPlacement().getSpecial()?"1":"0"));
		reqParams.add(new BasicNameValuePair(TAG_TOPLISTING, deal.getPlacement().getTopListing()?"1":"0"));
		reqParams.add(new BasicNameValuePair(TAG_HOMEPAGEBANNER, deal.getPlacement().getHomePageBanner()?"1":"0"));
		reqParams.add(new BasicNameValuePair(TAG_CATEGORYBANNER, deal.getPlacement().getCategoryBanner()?"1":"0"));
		
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

	public List<Deals> read(String uid) throws JSONException{
		List<NameValuePair> reqParams = new ArrayList<NameValuePair>();
		reqParams.add(new BasicNameValuePair(TAG_UID, String.valueOf(uid)));
		
		ServiceHandler sh = new ServiceHandler();
		jsonResp = sh.makeServiceCall(ServiceHandler.GET_DEALS_SERVICE, ServiceHandler.GET, reqParams);
		inflateDealsList();
		return deals;
	}
	
	public boolean update(Deals deal) {
		List<NameValuePair> reqParams = new ArrayList<NameValuePair>();
		reqParams.add(new BasicNameValuePair(TAG_DEALSID, String.valueOf(deal.getId())));
		
		reqParams.add(new BasicNameValuePair(TAG_REGULAR, deal.getPlacement().getRegular()?"1":"0"));
		reqParams.add(new BasicNameValuePair(TAG_SEPCIAL, deal.getPlacement().getSpecial()?"1":"0"));
		reqParams.add(new BasicNameValuePair(TAG_TOPLISTING, deal.getPlacement().getTopListing()?"1":"0"));
		reqParams.add(new BasicNameValuePair(TAG_HOMEPAGEBANNER, deal.getPlacement().getHomePageBanner()?"1":"0"));
		reqParams.add(new BasicNameValuePair(TAG_CATEGORYBANNER, deal.getPlacement().getCategoryBanner()?"1":"0"));
		
		reqParams.add(new BasicNameValuePair(TAG_TITLE, deal.getTitle()));
		reqParams.add(new BasicNameValuePair(TAG_DESCRIPTION, deal.getDescription()));
		// TODO - image URL for deals
		reqParams.add(new BasicNameValuePair(TAG_IMAGEURL, ""));
		reqParams.add(new BasicNameValuePair(TAG_CODE, deal.getCode()));
		reqParams.add(new BasicNameValuePair(TAG_FROM, deal.getFrom().toString()));
		reqParams.add(new BasicNameValuePair(TAG_TO, deal.getTo().toString()));
		reqParams.add(new BasicNameValuePair(TAG_SMSCOUNT, String.valueOf(deal.getSMSCount())));
		reqParams.add(new BasicNameValuePair(TAG_EMAILCOUNT, String.valueOf(deal.getEmailCount())));
		
		// TODO - created by, updated by
		reqParams.add(new BasicNameValuePair(TAG_CREATEDBY, String.valueOf(deal.getId())));
		reqParams.add(new BasicNameValuePair(TAG_UPDATEDBY, String.valueOf(deal.getId())));
		reqParams.add(new BasicNameValuePair(TAG_UPDATEDON, DateTime.now(TimeZone.getDefault()).toString()));
		
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
				jsonObj.getInt(TAG_DEALSID),
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
