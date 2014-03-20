package com.thekha.vendor.activity;

import com.thekha.vendor.adapter.DealsListAdapter;
import com.thekha.vendor.bean.Deals;
import com.thekha.vendor.dao.DealsDAO;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuItem;

public class DealsViewActivity extends ListActivity {

	DealsListAdapter deals_list_adapter;
	Deals deal;	
	ActionBar actionBar;
	
	public DealsViewActivity() {
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

		deals_list_adapter = new DealsListAdapter(getApplicationContext());
		
		setListAdapter(deals_list_adapter);
		
        deal = (Deals) getIntent().getSerializableExtra(Deals.DEALS_KEY);
		
		deal = new DealsDAO().read();
		
		loadItems();
		
		
	}	
	
	public void onResume() {
		super.onResume();

		// Load saved ToDoItems, if necessary

		if (deals_list_adapter.getCount() == 0)
			loadItems();
	}

	private void loadItems() {

	deals_list_adapter.add(deal);
		
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
	    	startActivity(new Intent(getApplicationContext(), DealsActivity.class));
	    default:
	      break;
	    }
	    return true;
	  } 

	
	
}
