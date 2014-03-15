package com.thekha.vendor.activity;

import com.thekha.vendor.bean.Query;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class ContactUsActivity extends Activity {
	
	ActionBar actionBar;
	EditText name, email, phone, subject, message;
	Spinner type;
	ArrayAdapter<CharSequence> typeAdapter;
	Query query;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contactus);
		setTitle(R.string.contactus_activity_title);
		
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
	    case android.R.id.home:
          Intent upIntent = NavUtils.getParentActivityIntent(this);
          if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
              TaskStackBuilder.create(this)
                      .addNextIntentWithParentStack(upIntent)
                      .startActivities();
          } else {
              NavUtils.navigateUpTo(this, upIntent);
          }
          return true;
	    case R.id.contact_done:
	    	setBeanFromUI();
	    	// TODO - save data
	    }
	    return true;
	  }
}
