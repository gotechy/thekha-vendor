package com.thekha.vendor.activity;

import java.io.IOException;
import java.util.List;

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
import android.widget.Toast;

import com.thekha.vendor.adapter.DealsListAdapter;
import com.thekha.vendor.bean.Deals;
import com.thekha.vendor.dao.BusinessDAO;
import com.thekha.vendor.dao.DealsDAO;
import com.thekha.vendor.dao.LoginDAO;

public class DealsViewActivity extends Activity {

	private String LOG_TAG;
	
	DealsListAdapter dealsAdapter;
	Deals deal;	
	ActionBar actionBar;
	private ProgressDialog pDialog;
	String uid;
	private DealsDAO dealDAO;
	private ListView lv;
	
	List<Deals> deals;
	public static final int ADD_DEAL_REQUEST = 0;
	public static final int EDIT_DEAL_REQUEST = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deals_view);
		LOG_TAG = getString(R.string.app_name);
		uid = getIntent().getStringExtra(LoginDAO.TAG_USERID);
		dealDAO = new DealsDAO();
		
		actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        lv = (ListView) findViewById(R.id.editdeals_listView);

        new DealsTask().execute();
	}	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ADD_DEAL_REQUEST && resultCode == RESULT_OK){
	    	dealsAdapter.add((Deals) data.getSerializableExtra(Deals.DEALS_KEY));
	    }
	    if (requestCode == EDIT_DEAL_REQUEST && resultCode == RESULT_OK){
	    	dealsAdapter.update((Deals) data.getSerializableExtra(Deals.DEALS_KEY));
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.actionmenu_view_deals, menu);
		return super.onCreateOptionsMenu(menu);
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
	    case R.id.dv_add_deals:
	    	// - Save the data and take back to profile.
	    	Intent i = new Intent(getApplicationContext(), AddDealActivity.class);
    		i.putExtra(LoginDAO.TAG_USERID, uid);
    		startActivityForResult(i, ADD_DEAL_REQUEST);
    		break;
	    default:
	      break;
	    }
	    return true;
	  } 

private class DealsTask  extends AsyncTask<Void, Void, Void> {
		
		@Override
        protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(DealsViewActivity.this);
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
		protected Void doInBackground(Void... params) {
			if(!isCancelled()){
				try {
					
					deals = dealDAO.read(getApplicationContext(), uid);
				} catch (JSONException e) {
					Log.d(LOG_TAG, "Cannot parse dashboard service JSON response.");
				}
			}
			return null;
		}
		
		@Override
        protected void onPostExecute(Void param) {
            super.onPostExecute(param);
			if(deals != null){
				dealsAdapter = new DealsListAdapter(DealsViewActivity.this, deals);
				lv.setAdapter(dealsAdapter);
	            Log.d(LOG_TAG, "Deals successfully loaded.");
	            //TODO - implement caching
	            //cacheData();
	            //Log.d(LOG_TAG, "Business profile successfully cached.");
			}else{
				Toast.makeText(getApplicationContext(), "Your deals cannot be loaded, please try again later.", Toast.LENGTH_SHORT).show();
			}
			pDialog.dismiss();            
		}
		
		@Override
		protected void onCancelled() {
			super.onCancelled();
			pDialog.dismiss();
		}
	}
	
}
