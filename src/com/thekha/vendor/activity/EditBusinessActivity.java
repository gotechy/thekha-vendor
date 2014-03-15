package com.thekha.vendor.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.thekha.vendor.bean.Business;

public class EditBusinessActivity extends Activity {
	
	//PhoneListAdapter plAdapter;
	Business business;
	ActionBar actionBar;
	EditText name, phone1, phone2, facebook, website, email, addressLine1, addressLine2, locality, city, country, pinCode;
	Spinner type;
	CheckBox checkAC, checkVP, checkCC, checkSA, checkVeg, checkNonVeg;
	AutoCompleteTextView state;
	
	ArrayAdapter<CharSequence> typeAdapter;
	ArrayAdapter<String> stateAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_business);
		setTitle(R.string.edit_business);
		
		actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        business = (Business) getIntent().getSerializableExtra(Business.BUSINESS_KEY);
        
        // populate spinner for business type
		type = (Spinner) findViewById(R.id.editbusiness_type);
		typeAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
				R.array.business_types, R.layout.spinner_item);
		type.setAdapter(typeAdapter);
		/*
		type.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				TextView tv = (TextView) view;
				business.setType((String) tv.getText());
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// do nothing
			}
		});
		*/
		type.getSelectedItem();
		
		checkSA = (CheckBox) findViewById(R.id.check_sa);
		checkCC = (CheckBox) findViewById(R.id.check_cc);
		checkVP = (CheckBox) findViewById(R.id.check_vp);
		checkVeg = (CheckBox) findViewById(R.id.check_veg);
		checkNonVeg = (CheckBox) findViewById(R.id.check_nonveg);
		checkAC = (CheckBox) findViewById(R.id.check_ac);
		
		name = (EditText) findViewById(R.id.editbusiness_name);
		phone1 = (EditText) findViewById(R.id.editbusiness_phone1);
		phone2 = (EditText) findViewById(R.id.editbusiness_phone2);
		website = (EditText) findViewById(R.id.editbusiness_website);
		facebook = (EditText) findViewById(R.id.editbusiness_facebook);
		email = (EditText) findViewById(R.id.editbusiness_email);
		addressLine1 = (EditText) findViewById(R.id.editbusiness_addressline1);
		addressLine2 = (EditText) findViewById(R.id.editbusiness_addressline2);
		city = (EditText) findViewById(R.id.editbusiness_city);
		locality = (EditText) findViewById(R.id.editbusiness_locality);
		country = (EditText) findViewById(R.id.editbusiness_country);
		pinCode = (EditText) findViewById(R.id.editbusiness_pincode);
		
		// set state to be auto complete
		state = (AutoCompleteTextView) findViewById(R.id.editbusiness_state);
		stateAdapter = new ArrayAdapter<String>(getApplicationContext(),
				R.layout.spinner_item, getResources().getStringArray(R.array.indian_states));
		state.setAdapter(stateAdapter);
		setUIFromBean();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.actionmenu_edit_business, menu);
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
	    case R.id.business_done:
	    	// - Save the data and take back to profile.
	    	setBeanFromUI();
	    	Intent data = new Intent();
	    	data.putExtra(Business.BUSINESS_KEY, business);
	    	setResult(RESULT_OK,data);
	    	finish();
	    case R.id.business_cancel:
	    	setResult(RESULT_CANCELED);
	    	finish();
	    default:
	      break;
	    }
	    return true;
	  } 

	protected void setUIFromBean() {
		name.setText(business.getName());
		type.setSelection(typeAdapter.getPosition(business.getType()));
		checkAC.setChecked(business.getFacilities().isAc());
		checkVP.setChecked(business.getFacilities().isVp());
		checkSA.setChecked(business.getFacilities().isSa());
		checkCC.setChecked(business.getFacilities().isCc());
		checkVeg.setChecked(business.getFacilities().isVeg());
		checkNonVeg.setChecked(business.getFacilities().isNonVeg());
		phone1.setText(business.getPhone1());
		phone2.setText(business.getPhone2());
		email.setText(business.getEmail());
		facebook.setText(business.getFacebook());
		website.setText(business.getWebsite());
		addressLine1.setText(business.getAddress().getLine1());
		addressLine2.setText(business.getAddress().getLine2());
		locality.setText(business.getAddress().getLocality());
		city.setText(business.getAddress().getCity());
		country.setText(business.getAddress().getCountry());
		pinCode.setText(business.getAddress().getPin());
		state.setText(business.getAddress().getState());	
	}
	
	protected void setBeanFromUI() {
		business.setName(name.getText().toString());
		business.setType(type.getSelectedItem().toString());
		business.getFacilities().setAc(checkAC.isChecked());
		business.getFacilities().setCc(checkCC.isChecked());
		business.getFacilities().setSa(checkSA.isChecked());
		business.getFacilities().setVp(checkVP.isChecked());
		business.getFacilities().setVeg(checkVeg.isChecked());
		business.getFacilities().setNonVeg(checkNonVeg.isChecked());
		business.setPhone1(phone1.getText().toString());
		business.setPhone2(phone2.getText().toString());
		business.setEmail(email.getText().toString());
		business.setFacebook(facebook.getText().toString());
		business.setWebsite(website.getText().toString());
		business.getAddress().setLine1(addressLine1.getText().toString());
		business.getAddress().setLine2(addressLine2.getText().toString());
		business.getAddress().setLocality(locality.getText().toString());
		business.getAddress().setCity(city.getText().toString());
		business.getAddress().setCountry(country.getText().toString());
		business.getAddress().setPin(pinCode.getText().toString());
		business.getAddress().setState(state.getText().toString());
	}

}