package com.thekha.vendor.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.thekha.vendor.bean.Business;
import com.thekha.vendor.bean.Facilities;
import com.thekha.vendor.dao.BusinessDAO;

public class BusinessActivity extends Activity {
	
	ActionBar actionBar;
	Business business;
	TextView name, type, facilities,  phone1, phone2, address, email, website, facebook;
	
	static final int EDIT_BUSINESS_REQUEST = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_business);
		setTitle(R.string.business);
		
		actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
		
		business = new BusinessDAO().read();
		
		name = (TextView) findViewById(R.id.business_name);
		type = (TextView) findViewById(R.id.business_type);
		facilities = (TextView) findViewById(R.id.business_facilities);
		phone1 = (TextView) findViewById(R.id.business_phone1);
		phone2 = (TextView) findViewById(R.id.business_phone2);
		address = (TextView) findViewById(R.id.business_address);
		email = (TextView) findViewById(R.id.business_email);
		facebook = (TextView) findViewById(R.id.business_facebook);
		website = (TextView) findViewById(R.id.business_website);
		setUIFromBean();
	}
	
	public void setUIFromBean(){
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
		facility.replace(facility.length()-3, facility.length(), "");
		facilities.setText(facility);
		phone1.setText(business.getPhone1());
		phone2.setText(business.getPhone2());
		address.setText(business.getAddress().toString());
		email.setText(business.getEmail());
		facebook.setText(business.getFacebook());
		website.setText(business.getWebsite());
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

}