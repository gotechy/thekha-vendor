package com.thekha.vendor.activity;

import com.thekha.vendor.adapter.PhoneListAdapter;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EditBusinessActivity extends Activity {
	
	PhoneListAdapter plAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_business);
		setTitle(R.string.edit_business);
		
		final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
		
        // populate spinner for business type
		Spinner typeSpinner = (Spinner) findViewById(R.id.editbusiness_type);
		ArrayAdapter<CharSequence> typeSpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
				R.array.business_types, R.layout.spinner_item);
		typeSpinner.setAdapter(typeSpinnerAdapter);
		typeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				TextView tv = (TextView) view;
				Toast.makeText(getApplicationContext(), tv.getText(), Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// do nothing
			}
		});
		
		// set state to be auto complete
		AutoCompleteTextView stateTextView = (AutoCompleteTextView) findViewById(R.id.editbusiness_state);
		ArrayAdapter<String> stateTextViewAdapter = new ArrayAdapter<String>(getApplicationContext(),
				R.layout.spinner_item, getResources().getStringArray(R.array.indian_states));
		stateTextView.setAdapter(stateTextViewAdapter);
		
		// set up list view for phone numbers
		ListView phoneList = (ListView) findViewById(R.id.editbusiness_phonelist);
		phoneList.setFooterDividersEnabled(true);
		TextView phoneFooter = (TextView) getLayoutInflater().inflate(R.layout.spinner_item, null);
		phoneList.addFooterView(phoneFooter);
		phoneFooter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub	
			}
		});
		// TODO - list of numbers
		plAdapter = new PhoneListAdapter(getApplicationContext());
		phoneList.setAdapter(plAdapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_business, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // action with ID action_refresh was selected
	    case android.R.id.home:
            // TODO - Change to Dashboard Ativity
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

	    case R.id.editbusiness_actionmenu_done:
	    	// TODO - Save the data and take back to profile.
	    	Toast.makeText(this, "Edit Selected", Toast.LENGTH_SHORT).show();
	    	break;
	    case R.id.editbusiness_actionmenu_cancel:
	    	// TODO - Cancel and take back to profile.
	    	NavUtils.navigateUpFromSameTask(this);
	        return true;
	    default:
	      break;
	    }
	    return true;
	  } 

}
