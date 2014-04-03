package com.thekha.vendor.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.thekha.vendor.adapter.TransactionsAdapter;
import com.thekha.vendor.bean.Transaction;
import com.thekha.vendor.dao.LoginDAO;
import com.thekha.vendor.dao.TransactionDAO;

public class TransactionsViewActivity extends Activity {

	private String LOG_TAG;

	TextView balance;
	ActionBar actionBar;
	List<Transaction> transactions = new ArrayList<Transaction>();
	TransactionsAdapter transactionAdapter;
	ListView lv;
	TransactionDAO tdao;

	private ProgressDialog pDialog;
	String uid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transactions_view);
		setTitle(R.string.transaction_activity_title);
		LOG_TAG = getString(R.string.app_name);

		uid = getIntent().getStringExtra(LoginDAO.TAG_USERID);
		tdao = new TransactionDAO();

		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		balance = (TextView) findViewById(R.id.transactions_balance);
		balance.setText("0");
		
		lv = (ListView) findViewById(R.id.transactions_transactions);
		
		new TransactionsTask().execute();

	}

	private class TransactionsTask  extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(TransactionsViewActivity.this);
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
					transactions = tdao.read(getApplicationContext(), uid);
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
				transactionAdapter = new TransactionsAdapter(TransactionsViewActivity.this, transactions);
				lv.setAdapter(transactionAdapter);
				Log.d(LOG_TAG, param);
				updateBalance();				
			}else{
				Log.d(LOG_TAG, param);
				Toast.makeText(getApplicationContext(), param, Toast.LENGTH_SHORT).show();
				startActivity(new Intent(TransactionsViewActivity.this, DashboardActivity.class));
				finish();
			}           
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			pDialog.dismiss();
		}
	}
	
	private void updateBalance(){
		Transaction.setBalance(0);
		Iterator<Transaction> itr = transactions.iterator();
		while(itr.hasNext()){
			Transaction.setBalance(Transaction.getBalance() + itr.next().getAmount());
		}
		balance.setText(String.valueOf(Transaction.getBalance()));	
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.actionmenu_business, menu);
		//return super.onCreateOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// action with ID action_refresh was selected
		case android.R.id.home:
			Intent upIntent = NavUtils.getParentActivityIntent(this);
			if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
				// This activity is NOT part of this app's task, so create a new task
				// when navigating up, with a synthesized back stack.
				TaskStackBuilder.create(this)
				// Add all of this activity's parents to the back stack
				.addNextIntentWithParentStack(upIntent)
				// Navigate up to the closest parent
				.startActivities();
			} else {
				// This activity is part of this app's task, so simply
				// navigate up to the logical parent activity.
				NavUtils.navigateUpTo(this, upIntent);
			}
			return true;
		}
		return true;
	}
}
