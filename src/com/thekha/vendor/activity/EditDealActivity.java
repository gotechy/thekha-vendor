package com.thekha.vendor.activity;

import hirondelle.date4j.DateTime;

import java.util.TimeZone;

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thekha.vendor.bean.Deals;
import com.thekha.vendor.dao.DealsDAO;

public class EditDealActivity extends Activity {
private String LOG_TAG;
	
	ActionBar actionBar;
	Deals deal;
//	TextView name, type, facilities,  phone1, phone2, address, email,website, facebook;
	TextView title, description, code, from, to;	
	CheckBox checkRegular, checkSpecial, checkTopListing, checkHomePageBanner, checkCategoryBanner;
	EditText sms, email;
	
	String imageURL;
	private ProgressDialog pDialog;

	private DealsDAO dealDAO = new DealsDAO();;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_deal);
		setTitle(R.string.title_deals_view);
		LOG_TAG = getString(R.string.app_name);
		
		deal = (Deals) getIntent().getSerializableExtra(Deals.DEALS_KEY);
        
		actionBar = getActionBar();

        title = (TextView) findViewById(R.id.editdeal_title);
		description = (TextView) findViewById(R.id.editdeal_description);
		code = (TextView) findViewById(R.id.editdeal_code);
		
		sms = (EditText) findViewById(R.id.editdeal_SMS);
		email = (EditText) findViewById(R.id.editdeal_email);
		
		from = (TextView) findViewById(R.id.editdeal_from);
		to = (TextView) findViewById(R.id.editdeal_to);
		checkRegular = (CheckBox) findViewById(R.id.editdeal_regular);
		checkSpecial = (CheckBox) findViewById(R.id.editdeal_special);
		checkTopListing = (CheckBox) findViewById(R.id.editdeal_top_listing);
		checkHomePageBanner = (CheckBox) findViewById(R.id.editdeal_home_page_banner);
		checkCategoryBanner = (CheckBox) findViewById(R.id.editdeal_category_banner);
		
		setUIFromBean();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.actionmenu_edit_deal, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.deal_done:
	    	setBeanFromUI();
	    	new EditDealTask().execute();
	    	break;
	    case R.id.deal_cancel:
	    	setResult(RESULT_CANCELED);
	    	finish();
	    default:
	      break;
	    }
	    return true;
	}
	
private class EditDealTask  extends AsyncTask<Void, Void, Boolean> {
		
		@Override
        protected void onPreExecute() {
            super.onPreExecute();
            // check for internet connection
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if(!(activeNetworkInfo != null && activeNetworkInfo.isConnected())){
            	Toast.makeText(getApplicationContext(), "Your internet is disabled, turn it on and then try again.", Toast.LENGTH_SHORT).show();
            	cancel(true);
            }
            // Showing progress dialog
            pDialog = new ProgressDialog(EditDealActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
		
		@Override
		protected Boolean doInBackground(Void... params) {
			return dealDAO.update(deal);
		}
		
		@Override
        protected void onPostExecute(Boolean param) {
            super.onPostExecute(param);
			pDialog.dismiss();
			if(param){
				Toast.makeText(getApplicationContext(), "Your deal has been updated.", Toast.LENGTH_SHORT).show();
				Log.d(LOG_TAG, "Deal successfully updated, at "+DateTime.now(TimeZone.getDefault()));
				Intent data = new Intent();
		    	data.putExtra(Deals.DEALS_KEY, deal);
		    	setResult(RESULT_OK,data);
		    	finish();
			}else{
				Toast.makeText(getApplicationContext(), "Deal cannot be saved, please try again later.", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void setBeanFromUI() {
	
	deal.setTitle(title.getText().toString());
	deal.setDescription(description.getText().toString());
	deal.setCode(code.getText().toString());
	deal.setImageURL(imageURL);

	deal.setFrom(new DateTime(from.getText().toString()));
	deal.setTo(new DateTime(to.getText().toString()));

	deal.getPlacement().setRegular(checkRegular.isChecked());
	deal.getPlacement().setSpecial(checkSpecial.isChecked());
	deal.getPlacement().setTopListing(checkTopListing.isChecked());
	deal.getPlacement().setHomePageBanner(checkHomePageBanner.isChecked());
	deal.getPlacement().setCategoryBanner(checkRegular.isChecked());
	
	deal.setSMSCount(Integer.parseInt(sms.getText().toString()));
	deal.setEmailCount(Integer.parseInt(email.getText().toString()));
	
	
}
	
	private void setUIFromBean() {
		title.setText(deal.getTitle());
		description.setText(deal.getDescription());
		code.setText(deal.getCode());
		//TODO - image in deals
		//deal.getImageURL(imageURL);

		from.setText(deal.getFrom().toString());
		to.setText(deal.getTo().toString());
		
		checkRegular.setChecked(deal.getPlacement().getRegular());
		checkSpecial.setChecked(deal.getPlacement().getSpecial());
		checkTopListing.setChecked(deal.getPlacement().getTopListing());
		checkHomePageBanner.setChecked(deal.getPlacement().getHomePageBanner());
		checkRegular.setChecked(deal.getPlacement().getCategoryBanner());
		
		sms.setText(Integer.toString(deal.getSMSCount()));
		email.setText(Integer.toString(deal.getEmailCount()));
		
	}
}
