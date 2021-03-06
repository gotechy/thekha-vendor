 package com.thekha.vendor.activity;

import hirondelle.date4j.DateTime;

import java.io.IOException;
import java.util.TimeZone;

import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.thekha.vendor.dao.BusinessDAO;
import com.thekha.vendor.dao.LoginDAO;

public class LoginActivity extends Activity {

	private String LOG_TAG;

	private EditText usernameView, passwordView;
	private Button login;
	private ProgressDialog pDialog;

	private String username , password;
	
	private String  failureReason="";

	LoginDAO ldao = new LoginDAO();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		getActionBar().hide();
		LOG_TAG = getString(R.string.app_name);

		try {
			startDashboardActivity(ldao.loginFromCache(getApplicationContext()));
		}
		catch (IOException e) {
			Log.d(LOG_TAG, "Cached file cannot be read, login manually.");
		} catch (JSONException e) {
			Log.d(LOG_TAG, "Cached response cannot be parsed, login manually.");
		}

		usernameView = (EditText) findViewById(R.id.login_username);
		passwordView = (EditText) findViewById(R.id.login_password);


		login = (Button) findViewById(R.id.login_button);
		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				username = usernameView.getText().toString();
				password = passwordView.getText().toString();
				if(username.isEmpty() || password.isEmpty()){
					Toast.makeText(getApplicationContext(), getString(R.string.incomplete_login_details_toastmsg), Toast.LENGTH_SHORT).show();
				}
				else{
					new LoginTask().execute(username, password);
				}
			}
		});

	}

	private void startDashboardActivity(String str){
		Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
		i.putExtra(LoginDAO.TAG_USERID, str.substring(0, str.indexOf(":")));
		i.putExtra(BusinessDAO.TAG_BID, str.substring(str.indexOf(":")+1));
		startActivity(i);
		finish();
	}

	private class LoginTask  extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(LoginActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
			// check for Internet connection
			ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
			if(!(activeNetworkInfo != null && activeNetworkInfo.isConnected())){
				Toast.makeText(getApplicationContext(), "Your internet is disabled, turn it on and then try again.", Toast.LENGTH_SHORT).show();
				cancel(true);
			}
		}

		@Override
		protected String doInBackground(String... params) {
			if(!isCancelled()){
				try {
					return ldao.login(getApplicationContext(), params[0], params[1]);
				} catch (JSONException e) {
					Log.d(LOG_TAG, "Cannot parse login service JSON response.");
					failureReason = "Something is very wrong, please contact our support services.";
					return null;
				} catch (IOException e) {
					Log.d(LOG_TAG, "Cannot cache login details.");
					return null;
				}
			}
			return null;
		}


		@Override
		protected void onPostExecute(String json) {
			super.onPostExecute(json);
			pDialog.dismiss();
			if (failureReason.isEmpty()){
				if(json != null){
					Log.d(LOG_TAG, "Logged In with UserID:BusinessID "+json+", at "+DateTime.now(TimeZone.getDefault()));
					startDashboardActivity(json);
				}
				else{
					Toast.makeText(getApplicationContext(), "Invalid Username or Password, please try again...", Toast.LENGTH_SHORT).show();
					cancel(true);
				}
			}
			else 
				Toast.makeText(getApplicationContext(), failureReason, Toast.LENGTH_LONG).show();
			
		}
		
		@Override
		protected void onCancelled() {
			super.onCancelled();
			pDialog.dismiss();
		}
	}
}
