package com.thekha.vendor.activity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class BusinessActivity extends Activity {
	// TODO - add website to ui
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_business);
		setTitle(R.string.business);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.business, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // action with ID action_refresh was selected
	    case R.id.business_menu:
	      Toast.makeText(this, "Edit Selected", Toast.LENGTH_SHORT).show();
	      Intent i = new Intent(getApplicationContext(), EditBusinessActivity.class);
	      startActivity(i);
	      break;
	    default:
	      break;
	    }
	    return true;
	  } 

}