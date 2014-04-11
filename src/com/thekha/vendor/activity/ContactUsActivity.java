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
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.thekha.vendor.bean.Query;
import com.thekha.vendor.dao.ContactUsDAO;
import com.thekha.vendor.dao.LoginDAO;

public class ContactUsActivity extends Activity {
	
	private String LOG_TAG;
	
	ActionBar actionBar;
	EditText name, email, phone, subject, message;
	Spinner type;
	ArrayAdapter<CharSequence> typeAdapter;
	Query query;

	private ProgressDialog pDialog;
	
	ContactUsDAO cudao = new ContactUsDAO();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contactus);
		setTitle(R.string.contactus_activity_title);
		LOG_TAG = getString(R.string.app_name);
		
		actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        name = (EditText) findViewById(R.id.contactus_name);
        email = (EditText) findViewById(R.id.contactus_email);
        phone = (EditText) findViewById(R.id.contactus_phone);
        subject = (EditText) findViewById(R.id.contactus_subject);
        message = (EditText) findViewById(R.id.contactus_message);
        
        type = (Spinner) findViewById(R.id.contactus_type);
        typeAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
				R.array.query_types, R.layout.spinner_item);
		type.setAdapter(typeAdapter);
		
	}

	protected void setBeanFromUI(){
		query = new Query(name.getText().toString(),
				type.getSelectedItem().toString(),
				phone.getText().toString(),
				email.getText().toString(),
				subject.getText().toString(),
				message.getText().toString());
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.actionmenu_contactus, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.contact_done:
	    	setBeanFromUI();
			new ContactUsTask().execute(getIntent().getStringExtra(LoginDAO.TAG_USERID));
			break;
	    case android.R.id.home:
			Intent upIntent = NavUtils.getParentActivityIntent(this);
			if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
				TaskStackBuilder.create(this)
				.addNextIntentWithParentStack(upIntent)
				.startActivities();
			} else {
				NavUtils.navigateUpTo(this, upIntent);
			}
			finish();
			break;
		default:
			break;
	    }
	    return true;
	  }
	
	private class ContactUsTask  extends AsyncTask<String, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(ContactUsActivity.this);
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
		protected Boolean doInBackground(String... params) {
			if(!isCancelled()){
				try {
					return cudao.add(params[0], query);
				} catch (ClientProtocolException e) {
					return false;
				} catch (IOException e) {
					return false;
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean param) {
			super.onPostExecute(param);
			pDialog.dismiss();
			if(param){
				Toast.makeText(getApplicationContext(), "Your query has been received. We will contact you in 72Hrs.", Toast.LENGTH_LONG).show();
				Log.d(LOG_TAG, "Query raised, at "+DateTime.now(TimeZone.getDefault()));
				Intent data = new Intent();
				setResult(RESULT_OK,data);
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
