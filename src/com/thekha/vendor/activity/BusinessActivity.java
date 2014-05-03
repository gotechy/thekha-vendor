package com.thekha.vendor.activity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thekha.vendor.bean.Business;
import com.thekha.vendor.bean.Facilities;
import com.thekha.vendor.dao.BusinessDAO;
import com.thekha.vendor.dao.LoginDAO;

public class BusinessActivity extends Activity {

	private String LOG_TAG;

	ActionBar actionBar;
	Business business;
	BusinessDAO businessDAO;
	TextView name, type, facilities,  phone1, phone2, address, email, website, facebook;
	ImageView picture;
	private ProgressDialog pDialog;

	private String bid;

	static final int EDIT_BUSINESS_REQUEST = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_business);
		setTitle(R.string.business);
		LOG_TAG = getString(R.string.app_name);

		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		name = (TextView) findViewById(R.id.business_name);
		type = (TextView) findViewById(R.id.business_type);
		facilities = (TextView) findViewById(R.id.business_facilities);
		phone1 = (TextView) findViewById(R.id.business_phone1);
		phone2 = (TextView) findViewById(R.id.business_phone2);
		address = (TextView) findViewById(R.id.business_address);
		email = (TextView) findViewById(R.id.business_email);
		facebook = (TextView) findViewById(R.id.business_facebook);
		website = (TextView) findViewById(R.id.business_website);
		picture = (ImageView) findViewById(R.id.business_picture);

		businessDAO = new BusinessDAO();
		bid = getIntent().getStringExtra(BusinessDAO.TAG_BID);
		new BusinessTask().execute(bid);
	}

	@Override
	protected void onStart() {
		// Check UID, if null get from cache, otherwise login user again manually.
		if(bid == null){
			try {
				LoginDAO ldao = new LoginDAO();
				String temp = ldao.loginFromCache(getApplicationContext());
    			bid = temp.substring(temp.indexOf(":"));
			}
			catch (IOException e) {
				Toast.makeText(getApplicationContext(), "Please login again.", Toast.LENGTH_SHORT).show();
				startActivity(new Intent(BusinessActivity.this, LoginActivity.class));
				finish();
			} catch (JSONException e) {
				Toast.makeText(getApplicationContext(), "Please login again.", Toast.LENGTH_SHORT).show();
				startActivity(new Intent(BusinessActivity.this, LoginActivity.class));
				finish();
			}
		}
		super.onStart();
	}

	private void setUIFromBean(){
		name.setText(business.getName());
		type.setText(business.getType());

		StringBuilder facility = new StringBuilder();
		if(business.getFacilities().isAc())
			facility.append(Facilities.AC+" | ");
		if(business.getFacilities().isSa())
			facility.append(Facilities.SA+" | ");
		if(business.getFacilities().isCc())
			facility.append(Facilities.CC+" | ");
		if(business.getFacilities().isVp())
			facility.append(Facilities.VP+" | ");
		if(business.getFacilities().isVeg())
			facility.append(Facilities.VEG+" | ");
		if(business.getFacilities().isNonVeg())
			facility.append(Facilities.NON_VEG+" | ");
		if(facility.length()>3)
			facility.replace(facility.length()-3, facility.length(), "");
		facilities.setText(facility);

		phone1.setText(business.getPhone1());
		phone2.setText(business.getPhone2());
		address.setText(business.getAddress().toString());
		email.setText(business.getEmail());
		facebook.setText(business.getFacebook());
		website.setText(business.getWebsite());

		String url = business.getImageURL();
		if(!url.isEmpty()){
			String imageFileName = url.substring(url.lastIndexOf(File.separator)+1);
			File imageFile = new File(getApplicationContext().getExternalFilesDir(null)+File.separator+imageFileName);
			if(imageFile.exists()){
				picture.setImageURI(Uri.parse(getApplicationContext().getExternalFilesDir(null)+File.separator+imageFileName));
			}else{
				new DownloadFileFromURL().execute(url, imageFileName);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.actionmenu_business, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// action with ID action_refresh was selected
		case android.R.id.home:
			Intent upIntent = NavUtils.getParentActivityIntent(BusinessActivity.this);
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
				//NavUtils.navigateUpTo(this, upIntent);
				finish();
			}
			break;
		case R.id.business_edit:
			Intent editBusiness = new Intent(getApplicationContext(), EditBusinessActivity.class);
			editBusiness.putExtra(Business.BUSINESS_KEY, business);
			startActivityForResult(editBusiness, EDIT_BUSINESS_REQUEST);
			break;
		default:
			break;
		}
		return true;
	} 

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// Check which request we're responding to
		if (requestCode == EDIT_BUSINESS_REQUEST){
			if(resultCode == RESULT_OK) {
				business = (Business) data.getSerializableExtra(Business.BUSINESS_KEY);
				setUIFromBean();
			}
			if(resultCode == RESULT_CANCELED){
				setUIFromBean();
			}
		}
	}

	private class BusinessTask  extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(BusinessActivity.this);
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
		protected String doInBackground(String... params) {
			if(!isCancelled()){
				try {
					business = null;
					business = businessDAO.read(params[0]);
					return "Business profile successfully loaded.";
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
			if(business!=null){
				setUIFromBean();
				Log.d(LOG_TAG, param);
			}else{
				Log.d(LOG_TAG, param);
				Toast.makeText(getApplicationContext(), param, Toast.LENGTH_SHORT).show();
				startActivity(new Intent(BusinessActivity.this, DashboardActivity.class));
				finish();
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			pDialog.dismiss();
		}
	}
	
	class DownloadFileFromURL extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
			if(!(activeNetworkInfo != null && activeNetworkInfo.isConnected())){
				Toast.makeText(getApplicationContext(), "Your internet is disabled, turn in on and then try again.", Toast.LENGTH_SHORT).show();
				cancel(true);
			}
			pDialog = new ProgressDialog(BusinessActivity.this);
			pDialog.setMessage("Looking for your cover image..");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			int count;
			try {
				URL url = new URL(params[0]);
				URLConnection conection = url.openConnection();
				conection.connect();
				int lenghtOfFile = conection.getContentLength();
				InputStream input = new BufferedInputStream(url.openStream(), 8192);
				OutputStream output = new FileOutputStream(getApplicationContext().getExternalFilesDir(null)+File.separator+params[1]);
				byte data[] = new byte[1024];
				long total = 0;
				while ((count = input.read(data)) != -1) {
					total += count;
					publishProgress("Downloading your cover image, "+(int)((total*100)/lenghtOfFile)+"% done.");
					output.write(data, 0, count);
				}
				output.flush();
				output.close();
				input.close();
			} catch (Exception e) {
				Log.d(LOG_TAG, e.getMessage());
				File f = new File(getApplicationContext().getExternalFilesDir(null)+File.separator+params[1]);
				if(f.exists())
					f.delete();
				publishProgress("dwf");
				pDialog.dismiss();
				cancel(true);
			}
			return getApplicationContext().getExternalFilesDir(null)+File.separator+params[1];
		}

		protected void onProgressUpdate(String... progress) {
			if(progress[0].equals("dwf")){
				Toast.makeText(getApplicationContext(), "Cannot find your cover image.", Toast.LENGTH_LONG).show();
				Log.d(LOG_TAG, "Cannot find business profile cover image.");
			}else{
				pDialog.setMessage(progress[0]);
				Log.d(LOG_TAG, progress[0]);
			}
				
		}

		@Override
		protected void onPostExecute(String img_path) {
			pDialog.dismiss();
			picture.setImageURI(Uri.parse(img_path));
		}
	}
}