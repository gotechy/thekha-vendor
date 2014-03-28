package com.thekha.vendor.activity;

import hirondelle.date4j.DateTime;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.thekha.vendor.bean.Deals;
import com.thekha.vendor.dao.BusinessDAO;
import com.thekha.vendor.dao.DealsDAO;
import com.thekha.vendor.dao.LoginDAO;

public class AddDealActivity extends Activity {
	
	private String LOG_TAG;
	
	ActionBar actionBar;
	private Deals deal = new Deals();
	private DealsDAO dealDAO = new DealsDAO();
	
//	TextView name, type, facilities,  phone1, phone2, address, email,website, facebook;
	EditText title, description, code, smsCount, emailCount;	
	CheckBox checkRegular, checkSpecial, checkTopListing, checkHomePageBanner, checkCategoryBanner;
	static int fromToFlag;
	static Button  fromDate, fromTime, toDate, toTime;
	String imageURL, uid;
	private ProgressDialog pDialog;


	
	static String dateString, timeString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_deal);
		setTitle(R.string.title_deals_view);
		LOG_TAG = getString(R.string.app_name);
		uid = getIntent().getStringExtra(LoginDAO.TAG_USERID);
		
		actionBar = getActionBar();

        title = (EditText) findViewById(R.id.edit_aed_deal_title);
		description = (EditText) findViewById(R.id.edit_aed_deal_description);
		code = (EditText) findViewById(R.id.edit_aed_deal_code);
		smsCount = (EditText) findViewById(R.id.edit_aed_deal_SMS);
		emailCount = (EditText) findViewById(R.id.edit_aed_deal_email);
		
		fromDate = (Button) findViewById(R.id.button_aed_deal_active_fromDate);
		fromTime = (Button) findViewById(R.id.button_aed_deal_active_fromTime);
		toDate = (Button) findViewById(R.id.button_aed_deal_active_toDate);
		toTime = (Button) findViewById(R.id.button_aed_deal_active_toTime);
		
		checkRegular = (CheckBox) findViewById(R.id.cb_regular);
		checkSpecial = (CheckBox) findViewById(R.id.cb_special);
		checkTopListing = (CheckBox) findViewById(R.id.cb_top_listing);
		checkHomePageBanner = (CheckBox) findViewById(R.id.cb_home_page_banner);
		checkCategoryBanner = (CheckBox) findViewById(R.id.cb_category_banner);
		
		// regular deal must be purchased.
		checkRegular.setChecked(true);
		checkRegular.setEnabled(false);
		
		
		fromDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDatePickerDialog();
				fromToFlag = 1 ;
			}
		});
		toDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDatePickerDialog();
				fromToFlag = 2 ;
			}
		});
		fromTime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showTimePickerDialog();
				fromToFlag = 3;
			}
		});
		toTime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showTimePickerDialog();
				fromToFlag = 4 ;
			}
		});
	}
	
	private class AddDealTask  extends AsyncTask<Void, Void, Boolean> {
		
		@Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(AddDealActivity.this);
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
		protected Boolean doInBackground(Void... params) {
			if(!isCancelled())
				return dealDAO.add(uid, deal);
			return null;
		}
		
		@Override
        protected void onPostExecute(Boolean param) {
            super.onPostExecute(param);
			pDialog.dismiss();
			if(param){
				Toast.makeText(getApplicationContext(), "Your deal has been added, you will hear from us shortly.", Toast.LENGTH_LONG).show();
				Log.d(LOG_TAG, "Deal successfully added, at "+DateTime.now(TimeZone.getDefault()));
				
				Intent data = new Intent();
				data.putExtra(LoginDAO.TAG_USERID, uid);
				data.putExtra(Deals.DEALS_KEY, deal);
				setResult(RESULT_OK, data);
				finish();
			}else{
				Toast.makeText(getApplicationContext(), "Your deal cannot be saved, please try again later.", Toast.LENGTH_SHORT).show();
			}
		}
		
		@Override
		protected void onCancelled() {
			super.onCancelled();
			pDialog.dismiss();
		}
	}

	public static void setFromToDateTime() {
		if ( fromToFlag == 1)
			fromDate.setText(dateString);
		else if (fromToFlag == 2)
			toDate.setText(dateString);
		else if (fromToFlag == 3)
			fromTime.setText(timeString);
		else if (fromToFlag == 4)
			toTime.setText(timeString);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.actionmenu_add_edit_deals, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.aed_done:
	    	if(setBeanFromUI())
	    		new AddDealTask().execute();
	    	break;
	    case R.id.aed_cancel:
	    	setResult(RESULT_CANCELED);
	    	finish();
	    default:
	      break;
	    }
	    return true;
	  } 

	private void showDatePickerDialog() {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
	}

	private void showTimePickerDialog() {
		DialogFragment newFragment = new TimePickerFragment();
		newFragment.show(getFragmentManager(), "timePicker");
	}
	
	// DialogFragment used to pick a ToDoItem deadline date

	public static class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			// Use the current date as the default date in the picker

			final GregorianCalendar c = (GregorianCalendar) GregorianCalendar.getInstance();
			int year = c.get(GregorianCalendar.YEAR);
			int month = c.get(GregorianCalendar.MONTH);
			int day = c.get(GregorianCalendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			setDateString(year, monthOfYear, dayOfMonth);
			setFromToDateTime();
		}

	}

	// DialogFragment used to pick a ToDoItem deadline time

	public static class TimePickerFragment extends DialogFragment implements
			TimePickerDialog.OnTimeSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);

			// Create a new instance of TimePickerDialog and return
			return new TimePickerDialog(getActivity(), this, hour, minute,
					true);
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			setTimeString(hourOfDay, minute, 0);
			setFromToDateTime();
		}
	}


	private static void setDateString(int year, int monthOfYear, int dayOfMonth) {

		// Increment monthOfYear for Calendar/Date -> Time Format setting
		monthOfYear++;
		String mon = "" + monthOfYear;
		String day = "" + dayOfMonth;

		if (monthOfYear < 10)
			mon = "0" + monthOfYear;
		if (dayOfMonth < 10)
			day = "0" + dayOfMonth;

		dateString = year + "-" + mon + "-" + day;
	}

	private static void setTimeString(int hourOfDay, int minute, int mili) {
		String hour = "" + hourOfDay;
		String min = "" + minute;

		if (hourOfDay < 10)
			hour = "0" + hourOfDay;
		if (minute < 10)
			min = "0" + minute;

		timeString = hour + ":" + min + ":00";
	}

	private boolean setBeanFromUI() {
		if(!title.getText().toString().isEmpty())
			deal.setTitle(title.getText().toString());
		else{makeToastForIncompleteForm();return false;}
		
		if(!description.getText().toString().isEmpty())
			deal.setDescription(description.getText().toString());
		else
			{makeToastForIncompleteForm();return false;}
		
		if(!code.getText().toString().isEmpty())
			deal.setCode(code.getText().toString());
		else
			{makeToastForIncompleteForm();return false;}
		
		// TODO - image URL
		deal.setImageURL("");
		
		DateTime temp;
		if(DateTime.isParseable(fromDate.getText().toString())){
			temp = new DateTime(fromDate.getText().toString());
			deal.setFrom(temp);
		}else
			{makeToastForIncompleteForm();return false;}
		
		DateTime temp2;
		if(DateTime.isParseable(toDate.getText().toString())){
			temp2 = new DateTime(toDate.getText().toString());
			if(temp.lt(temp2))
				{deal.setTo(new DateTime(toDate.getText().toString()));}
			else
				{Toast.makeText(getApplicationContext(), "From date cannot be after To date.", Toast.LENGTH_LONG).show();return false;}
		}else
			{makeToastForIncompleteForm();return false;}
				
		if(!smsCount.getText().toString().isEmpty())
			deal.setSMSCount(Integer.parseInt(smsCount.getText().toString()));
		else
			{makeToastForIncompleteForm();return false;}
		
		if(!emailCount.getText().toString().isEmpty())
			deal.setEmailCount(Integer.parseInt(emailCount.getText().toString()));
		else
			{makeToastForIncompleteForm();return false;}

		deal.getPlacement().setRegular(checkRegular.isChecked());
		deal.getPlacement().setSpecial(checkSpecial.isChecked());
		deal.getPlacement().setTopListing(checkTopListing.isChecked());
		deal.getPlacement().setHomePageBanner(checkHomePageBanner.isChecked());
		deal.getPlacement().setCategoryBanner(checkRegular.isChecked());
		
		return true;
	}
	
	private void makeToastForIncompleteForm(){
		Toast.makeText(getApplicationContext(), "Please completely fill the form.", Toast.LENGTH_LONG).show();
	}
	
}
