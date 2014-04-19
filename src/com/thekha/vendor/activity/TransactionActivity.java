package com.thekha.vendor.activity;

import hirondelle.date4j.DateTime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.thekha.vendor.bean.Deals;
import com.thekha.vendor.bean.Prices;
import com.thekha.vendor.bean.Transaction;
import com.thekha.vendor.dao.DealsDAO;
import com.thekha.vendor.dao.LoginDAO;
import com.thekha.vendor.dao.TransactionDAO;
import com.thekha.vendor.util.UploadImage;

public class TransactionActivity extends Activity {

	private String LOG_TAG;

	ActionBar actionBar;
	Transaction transaction;
	TransactionDAO transactionDAO;
	TextView regular, regular_qty, special,  special_qty, top, top_qty, homeBanner, homeBanner_qty, categoryBanner, categoryBanner_qty,
	sms, sms_qty, email, email_qty, totalView, balanceView, infoView;
	private ProgressDialog pDialog;
	private Deals deal, nDeal;
	private Prices prices;
	private int total, days;
	private DealsDAO dealDAO = new DealsDAO();
	String bid, picturePath, pictureName;
	List<Transaction> transactions = new ArrayList<Transaction>();
	private boolean transactionReady = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transaction);
		setTitle(R.string.business);
		LOG_TAG = getString(R.string.app_name);


		actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);

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
		balanceView = (TextView) findViewById(R.id.payment_balance);
		infoView = (TextView) findViewById(R.id.payment_info);

		transactionDAO = new TransactionDAO();
		bid = getIntent().getStringExtra(LoginDAO.TAG_USERID); 
		deal = (Deals) getIntent().getSerializableExtra(Deals.DEALS_KEY);
		prices = (Prices) getIntent().getSerializableExtra(Prices.PRICES_KEY);
		nDeal = (Deals) getIntent().getSerializableExtra("PrivateEDATAKey");
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		//TODO - Save the user's current state
		Log.i(LOG_TAG, "onSaveInstanceState");
		//savedInstanceState.putString(LoginDAO.TAG_USERID, uid);
	}

	@Override
	protected void onStart() {
		super.onStart();
		new BalanceTask().execute();
	}

	/*
	 *Sets up UI and calculates the total amount.
	 *If activity is started from AddDealActivity. 
	 */
	private void setUpUI(){
		String qty, cost, daysstr;
		days = deal.getFrom().numDaysFrom(deal.getTo());
		daysstr = String.valueOf(days);

		total = 0;		
		if(deal.getPlacement().isRegular()){
			qty = daysstr;
			cost = prices.getRegular();
			total += (Integer.valueOf(cost) * days);
		}else{
			qty = "0";
			cost = "0";
		}
		regular_qty.setText(prices.getRegular()+" x "+qty);
		regular.setText(String.valueOf(Integer.valueOf(cost)*Integer.valueOf(qty)));

		if(deal.getPlacement().isSpecial()){
			qty = daysstr;
			cost = prices.getSpecial();
			total += (Integer.valueOf(cost) * days);
		}else{
			qty = "0";
			cost = "0";
		}
		special_qty.setText(prices.getSpecial()+" x "+qty);
		special.setText(String.valueOf(Integer.valueOf(cost)*Integer.valueOf(qty)));

		if(deal.getPlacement().isTopListing()){
			qty = daysstr;
			cost = prices.getTopListing();
			total += (Integer.valueOf(cost) * days);
		}else{
			qty = "0";
			cost = "0";
		}
		top_qty.setText(prices.getTopListing()+" x "+qty);
		top.setText(String.valueOf(Integer.valueOf(cost)*Integer.valueOf(qty)));

		if(deal.getPlacement().isHomePageBanner()){
			qty = daysstr;
			cost = prices.getHomePageBanner();
			total += (Integer.valueOf(cost) * days);
		}else{
			qty = "0";
			cost = "0";
		}
		homeBanner_qty.setText(prices.getHomePageBanner()+" x "+qty);
		homeBanner.setText(String.valueOf(Integer.valueOf(cost)*Integer.valueOf(qty)));

		if(deal.getPlacement().isCategoryBanner()){
			qty = daysstr;
			cost = prices.getCategoryBanner();
			total += (Integer.valueOf(cost) * days);
		}else{
			qty = "0";
			cost = "0";
		}
		categoryBanner_qty.setText(prices.getCategoryBanner()+" x "+qty);
		categoryBanner.setText(String.valueOf(Integer.valueOf(cost)*Integer.valueOf(qty)));

		sms_qty.setText(prices.getPushSMS()+" x "+deal.getSMSCount());
		sms.setText(String.valueOf(Integer.valueOf(prices.getPushSMS())*deal.getSMSCount()));
		total += (Integer.valueOf(prices.getPushSMS())*deal.getSMSCount());

		email_qty.setText(prices.getPushEMail()+" x "+deal.getEmailCount());
		email.setText(String.valueOf(Integer.valueOf(prices.getPushEMail())*deal.getEmailCount()));
		total += (Integer.valueOf(prices.getPushEMail())*deal.getEmailCount());

		totalView.setText(String.valueOf(total));
		balanceView.setText(String.valueOf(Transaction.getBalance()));
		if(total<Transaction.getBalance()){
			infoView.setText("Sufficient balance available for this transaction. Press the Done button to activate the deal.");
			transactionReady = true;
		}else{
			infoView.setText("In-Sufficient balance for this transaction. Please purchase atleast "
					+(total-Transaction.getBalance())
					+" credits and then try again.");
			transactionReady = false;
		}
	}
	
	/*
	 *Sets up UI and calculates the total amount.
	 *If activity is started from EditDealActivity. 
	 */
	private void setUpUIEditDeal(){
		String qty, cost, daysstr;
		days = nDeal.getFrom().numDaysFrom(nDeal.getTo())+1;
		daysstr = String.valueOf(days);

		total = 0;		
		if((!deal.getPlacement().isRegular()) && nDeal.getPlacement().isRegular()){
			qty = daysstr;
			cost = prices.getRegular();
			total += (Integer.valueOf(cost) * days);
		}else{
			qty = "0";
			cost = "0";
		}
		regular_qty.setText(prices.getRegular()+" x "+qty);
		regular.setText(String.valueOf(Integer.valueOf(cost)*Integer.valueOf(qty)));

		if((!deal.getPlacement().isSpecial()) && nDeal.getPlacement().isSpecial()){
			qty = daysstr;
			cost = prices.getSpecial();
			total += (Integer.valueOf(cost) * days);
		}else{
			qty = "0";
			cost = "0";
		}
		special_qty.setText(prices.getSpecial()+" x "+qty);
		special.setText(String.valueOf(Integer.valueOf(cost)*Integer.valueOf(qty)));

		if((!deal.getPlacement().isTopListing()) && nDeal.getPlacement().isTopListing()){
			qty = daysstr;
			cost = prices.getTopListing();
			total += (Integer.valueOf(cost) * days);
		}else{
			qty = "0";
			cost = "0";
		}
		top_qty.setText(prices.getTopListing()+" x "+qty);
		top.setText(String.valueOf(Integer.valueOf(cost)*Integer.valueOf(qty)));

		if((!deal.getPlacement().isHomePageBanner()) && nDeal.getPlacement().isHomePageBanner()){
			qty = daysstr;
			cost = prices.getHomePageBanner();
			total += (Integer.valueOf(cost) * days);
		}else{
			qty = "0";
			cost = "0";
		}
		homeBanner_qty.setText(prices.getHomePageBanner()+" x "+qty);
		homeBanner.setText(String.valueOf(Integer.valueOf(cost)*Integer.valueOf(qty)));

		if((!deal.getPlacement().isCategoryBanner()) && nDeal.getPlacement().isCategoryBanner()){
			qty = daysstr;
			cost = prices.getCategoryBanner();
			total += (Integer.valueOf(cost) * days);
		}else{
			qty = "0";
			cost = "0";
		}
		categoryBanner_qty.setText(prices.getCategoryBanner()+" x "+qty);
		categoryBanner.setText(String.valueOf(Integer.valueOf(cost)*Integer.valueOf(qty)));

		sms_qty.setText(prices.getPushSMS()+" x "+nDeal.getSMSCount());
		sms.setText(String.valueOf(Integer.valueOf(prices.getPushSMS())*nDeal.getSMSCount()));
		total += (Integer.valueOf(prices.getPushSMS())*nDeal.getSMSCount());

		email_qty.setText(prices.getPushEMail()+" x "+nDeal.getEmailCount());
		email.setText(String.valueOf(Integer.valueOf(prices.getPushEMail())*nDeal.getEmailCount()));
		total += (Integer.valueOf(prices.getPushEMail())*nDeal.getEmailCount());

		totalView.setText(String.valueOf(total));
		balanceView.setText(String.valueOf(Transaction.getBalance()));
		if(total<Transaction.getBalance()){
			infoView.setText("Sufficient balance available for this transaction. Press the Done button to activate the deal.");
			transactionReady = true;
		}else{
			infoView.setText("In-Sufficient balance for this transaction. Please purchase atleast "
					+(total-Transaction.getBalance())
					+" credits and then try again.");
			transactionReady = false;
		}
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
		case android.R.id.home:
			Intent upIntent = NavUtils.getParentActivityIntent(this);
			if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
				TaskStackBuilder.create(this)
				.addNextIntentWithParentStack(upIntent)
				.startActivities();
			} else {
				NavUtils.navigateUpTo(this, upIntent);
			}
			break;
		case R.id.transaction_cancel:
			startActivity(getParentActivityIntent());
			finish();
			break;
		case R.id.transaction_done:
			if(transactionReady){
				inflateTransactionObject();
				new TransactionTask().execute();
			}else{
				item.setVisible(transactionReady);
				infoView.setTextColor(getResources().getColor(R.color.negative_transaction));
			}
			break;
		default:
			break;
		}
		return true;
	} 

	private void inflateTransactionObject() {
		transaction = new Transaction(
				Transaction.TITLE_DEBIT,
				String.valueOf(total)+" debited for deal "+deal.getTitle(),
				(total*-1)
				);
	}

	private class TransactionTask  extends AsyncTask<Void, Void, Boolean> {
		
		int prevOrientation;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//Lork orientation change
			prevOrientation = getRequestedOrientation();
			if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			} else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			} else {
			    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
			}
			// Showing progress dialog
			pDialog = new ProgressDialog(TransactionActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
			// check for internet connection
			ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
			if(!(activeNetworkInfo != null && activeNetworkInfo.isConnected())){
				Toast.makeText(getApplicationContext(), "Your internet is disabled, turn it on and then try again.", Toast.LENGTH_SHORT).show();
				cancel(true);
			}
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			if(!isCancelled()){
				try {
					if(nDeal == null){
						picturePath = deal.getImageURL();
						File afile =new File(picturePath);
						pictureName = "dealImg_"
								+bid+"_"+
								DateTime.today(TimeZone.getDefault())+"_"+
								DateTime.now(TimeZone.getDefault()).getMinute()+":"+DateTime.now(TimeZone.getDefault()).getSecond()
								+picturePath.substring(picturePath.lastIndexOf("."));
						//+"."+picturePath.substring(picturePath.lastIndexOf(".")+1);
						picturePath = getApplicationContext().getExternalFilesDir(null) + File.separator + pictureName;
						File bfile =new File(picturePath);
						InputStream inStream;
						inStream = new FileInputStream(afile);
						OutputStream outStream = new FileOutputStream(bfile);
						byte[] buffer = new byte[1024];
						int length;
						while ((length = inStream.read(buffer)) > 0){
							outStream.write(buffer, 0, length);
						}
						inStream.close();
						outStream.close();
						deal.setImageURL(UploadImage.upload_folder+File.separator+pictureName);
						
						deal.setStatus(Deals.STATUS_ACTIVE);
						UploadImage.upload(picturePath);
						
						Integer dealid = dealDAO.add(bid, deal, transaction);
						if(dealid != null){
							return true;
						}
					}else{
						nDeal.setSMSCount(deal.getSMSCount()+nDeal.getSMSCount());
						nDeal.setEmailCount(deal.getEmailCount()+nDeal.getEmailCount());
						nDeal.setStatus(Deals.STATUS_ACTIVE);
						return dealDAO.update(nDeal, bid, transaction);
					}
				} catch (ClientProtocolException e) {
					return false;
				} catch (IOException e) {
					return false;
				} catch (JSONException e) {
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
				Toast.makeText(getApplicationContext(), "Your transaction was successful and deal has been updated.", Toast.LENGTH_LONG).show();
				Log.d(LOG_TAG, "Transaction and Deal successfully added, at "+DateTime.now(TimeZone.getDefault()));
				finish();
			}else{
				Toast.makeText(getApplicationContext(), "Connection cannot be established, please try again later.", Toast.LENGTH_SHORT).show();
			}
			setRequestedOrientation(prevOrientation);
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			pDialog.dismiss();
			setRequestedOrientation(prevOrientation);
		}
	}

	private class BalanceTask  extends AsyncTask<Void, Void, String> {

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
				Toast.makeText(getApplicationContext(), "Your internet is disabled, turn it on and then try again.", Toast.LENGTH_SHORT).show();
				cancel(true);
			}
		}

		@Override
		protected String doInBackground(Void... params) {
			if(!isCancelled()){
				try {
					transactions = null;
					transactions = transactionDAO.read(getApplicationContext(), bid);
					return "Transactions successfully loaded.";
				} catch (JSONException e) {
					return "Something is very wrong, please contact our support services.";
				} catch (ClientProtocolException e) {
					return "Connection cannot be established, please try again later.";
				} catch (IOException e) {
					return "Connection cannot be established, please try again later.";
				} 
			}
			return null;
		}

		@Override
		protected void onPostExecute(String param) {
			super.onPostExecute(param);
			pDialog.dismiss();
			if(transactions != null){
				Log.d(LOG_TAG, param);
				Transaction.setBalance(0);
				Iterator<Transaction> itr = transactions.iterator();
				while(itr.hasNext()){
					Transaction.setBalance(Transaction.getBalance() + itr.next().getAmount());
				}
				if(nDeal == null)
					setUpUI();
				else
					setUpUIEditDeal();
			}else{
				Log.d(LOG_TAG, param);
				Toast.makeText(getApplicationContext(), param, Toast.LENGTH_SHORT).show();
				finish();
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			pDialog.dismiss();
		}
	}
}