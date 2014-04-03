package com.thekha.vendor.activity;

import hirondelle.date4j.DateTime;

import java.io.IOException;
import java.util.TimeZone;

import org.apache.http.client.ClientProtocolException;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.thekha.vendor.bean.Deals;
import com.thekha.vendor.bean.Prices;
import com.thekha.vendor.bean.Transaction;
import com.thekha.vendor.dao.TransactionDAO;

public class TransactionActivity extends Activity {

	private String LOG_TAG;

	ActionBar actionBar;
	Transaction transaction;
	TransactionDAO transactionDAO;
	TextView regular, regular_qty, special,  special_qty, top, top_qty, homeBanner, homeBanner_qty, categoryBanner, categoryBanner_qty, sms, sms_qty, email, email_qty, totalView;
	private ProgressDialog pDialog;
	private Deals deal;
	private Prices prices;
	private int total;

	static final int EDIT_BUSINESS_REQUEST = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transaction);
		setTitle(R.string.business);
		LOG_TAG = getString(R.string.app_name);

		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		regular = (TextView) findViewById(R.id.payment_regular);
		regular_qty = (TextView) findViewById(R.id.payment_regular_qty);
		special = (TextView) findViewById(R.id.payment_special);
		special_qty = (TextView) findViewById(R.id.payment_special_qty);
		top = (TextView) findViewById(R.id.payment_top);
		top_qty = (TextView) findViewById(R.id.payment_top_qty);
		homeBanner = (TextView) findViewById(R.id.payment_homebanner);
		homeBanner_qty = (TextView) findViewById(R.id.payment_homebanner_qty);
		categoryBanner = (TextView) findViewById(R.id.payment_categorybanner);
		categoryBanner_qty = (TextView) findViewById(R.id.payment_categorybanner_qty);
		sms = (TextView) findViewById(R.id.payment_sms);
		sms_qty = (TextView) findViewById(R.id.payment_sms_qty);
		email = (TextView) findViewById(R.id.payment_email);
		email_qty = (TextView) findViewById(R.id.payment_email_qty);
		totalView = (TextView) findViewById(R.id.payment_total);

		transactionDAO = new TransactionDAO();
		deal = (Deals) getIntent().getSerializableExtra(Deals.DEALS_KEY);
		prices = (Prices) getIntent().getSerializableExtra(Prices.PRICES_KEY);

		setUpUI();
	}

	/*
	 *Sets up UI and calculates the total amount. 
	 */
	 private void setUpUI(){
		String qty, cost;
		total = 0;
		if(deal.getPlacement().isRegular()){
			qty = "1";
			cost = prices.getRegular();
			total += Integer.valueOf(cost);
		}else{
			qty = "0";
			cost = "0";
		}
		regular_qty.setText(prices.getRegular()+" x "+qty);
		regular.setText(cost);

		if(deal.getPlacement().isSpecial()){
			qty = "1";
			cost = prices.getSpecial();
			total += Integer.valueOf(cost);
		}else{
			qty = "0";
			cost = "0";
		}
		regular_qty.setText(prices.getSpecial()+" x "+qty);
		regular.setText(cost);

		if(deal.getPlacement().isTopListing()){
			qty = "1";
			cost = prices.getTopListing();
			total += Integer.valueOf(cost);
		}else{
			qty = "0";
			cost = "0";
		}
		regular_qty.setText(prices.getTopListing()+" x "+qty);
		regular.setText(cost);

		if(deal.getPlacement().isHomePageBanner()){
			qty = "1";
			cost = prices.getHomePageBanner();
			total += Integer.valueOf(cost);
		}else{
			qty = "0";
			cost = "0";
		}
		regular_qty.setText(prices.getHomePageBanner()+" x "+qty);
		regular.setText(cost);

		if(deal.getPlacement().isCategoryBanner()){
			qty = "1";
			cost = prices.getCategoryBanner();
			total += Integer.valueOf(cost);
		}else{
			qty = "0";
			cost = "0";
		}
		regular_qty.setText(prices.getCategoryBanner()+" x "+qty);
		regular.setText(cost);

		sms_qty.setText(prices.getPushSMS()+" x "+deal.getSMSCount());
		sms.setText(Integer.valueOf(prices.getPushSMS())*deal.getSMSCount());
		total += Integer.valueOf(prices.getPushSMS())*deal.getSMSCount();

		email_qty.setText(prices.getPushEMail()+" x "+deal.getEmailCount());
		email.setText(Integer.valueOf(prices.getPushEMail())*deal.getEmailCount());
		total += Integer.valueOf(prices.getPushSMS())*deal.getSMSCount();

		totalView.setText(String.valueOf(total));
	 }

	 @Override
	 public boolean onCreateOptionsMenu(Menu menu) {
		 // Inflate the menu; this adds items to the action bar if it is present.
		 getMenuInflater().inflate(R.menu.actionmenu_transaction, menu);
		 return super.onCreateOptionsMenu(menu);
	 }

	 @Override
	 public boolean onOptionsItemSelected(MenuItem item) {
		 switch (item.getItemId()) {
		 // action with ID action_refresh was selected
		 case R.id.transaction_cancel:
			 startActivity(getParentActivityIntent());
			 break;
		 case R.id.transaction_done:
			 inflateTransactionObject();
			 new TransactionTask().execute();
			 break;
		 default:
			 break;
		 }
		 return true;
	 } 
	 
	 private void inflateTransactionObject() {
		 transaction = new Transaction(
				 Transaction.TITLE_DEBIT,
				 String.valueOf((total*-1))+" debited for deal "+deal.getTitle(),
				 total
			);
	 }

	 private class TransactionTask  extends AsyncTask<Void, Void, Boolean> {

		 @Override
		 protected void onPreExecute() {
			 super.onPreExecute();
			 // Showing progress dialog
			 pDialog = new ProgressDialog(TransactionActivity.this);
			 pDialog.setMessage("Please wait...");
			 pDialog.setCancelable(false);
			 pDialog.show();
			 // check for internet connection
			 ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			 NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
			 if(!(activeNetworkInfo != null && activeNetworkInfo.isConnected())){
				 Toast.makeText(getApplicationContext(), "Your internet is disabled, turn it on and retry.", Toast.LENGTH_SHORT).show();
				 cancel(true);
			 }
		 }

		 @Override
		 protected Boolean doInBackground(Void... params) {
			 if(!isCancelled()){
				 try {
					 return transactionDAO.add(String.valueOf(deal.getId()), transaction);
				 } catch (ClientProtocolException e) {
					 return false;
				 } catch (IOException e) {
					 return false;
				 }

			 }
			 return false;
		 }

		 @Override
		 protected void onPostExecute(Boolean param) {
			 super.onPostExecute(param);
			 pDialog.dismiss();
				if(param){
					Toast.makeText(getApplicationContext(), "Your transaction was successful.", Toast.LENGTH_LONG).show();
					Log.d(LOG_TAG, "Transaction successfully added, at "+DateTime.now(TimeZone.getDefault()));
					startActivity(new Intent(TransactionActivity.this, DashboardActivity.class));
					finish();
				}else{
					Toast.makeText(getApplicationContext(), "Connection cannot be established, please try again later.", Toast.LENGTH_SHORT).show();
				}
		 }

		 @Override
		 protected void onCancelled() {
			 super.onCancelled();
			 pDialog.dismiss();
		 }
	 }
}