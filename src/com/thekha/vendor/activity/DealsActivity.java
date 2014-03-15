package com.thekha.vendor.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.thekha.vendor.bean.Deals;
import com.thekha.vendor.dao.DealsDAO;


import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

public class DealsActivity extends Activity {
	
	ActionBar actionBar;
	Deals deal;
//	TextView name, type, facilities,  phone1, phone2, address, email,website, facebook;
	EditText title, description, code, smsCount, emailCount;	
	CheckBox checkRegular, checkSpecial, checkTopListing, checkHomePageBanner, checkCategoryBanner;
	static int fromToFlag;
	static Button  fromDate, fromTime, toDate, toTime;
	String imageURL;

	
	static String dateString, timeString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_edit_deals);
		setTitle(R.string.title_deals_view);

		actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        deal = (Deals) getIntent().getSerializableExtra(Deals.DEALS_KEY);
		
		deal = new DealsDAO().read();
		
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
		
		setUIFromBean();
		
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


	public static void setFromToDateTime() {
		// TODO Auto-generated method stub
		

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
	    case R.id.aed_done:
	    	// - Save the data and take back to profile.
	    	setBeanFromUI();
	    	Intent data = new Intent();
	    	data.putExtra(Deals.DEALS_KEY, deal);
	    	setResult(RESULT_OK,data);
	    	finish();
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


	
	
	private void setUIFromBean() {
		// TODO Auto-generated method stub
		
		title.setText(deal.getTitle());
		description.setText(deal.getDescription());
		code.setText(deal.getCode());
//		deal.getImageURL(imageURL);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.ENGLISH);
		
		GregorianCalendar temp = new GregorianCalendar();
		
		try {
		
			temp.setTime(dateFormat.parse(fromDate.getText().toString()+" "+fromTime.getText().toString()));
			deal.setFrom(temp);
		
			temp.setTime(dateFormat.parse(toDate.getText().toString()+" "+toTime.getText().toString()));
			deal.setTo(temp);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		checkRegular.setChecked(deal.getPlacement().getRegular());
		checkSpecial.setChecked(deal.getPlacement().getSpecial());
		checkTopListing.setChecked(deal.getPlacement().getTopListing());
		checkHomePageBanner.setChecked(deal.getPlacement().getHomePageBanner());
		checkRegular.setChecked(deal.getPlacement().getCategoryBanner());
		
		smsCount.setText(Integer.toString(deal.getSMSCount()));
		emailCount.setText(Integer.toString(deal.getEmailCount()));
		
	}

	private void setBeanFromUI() {
		// TODO Auto-generated method stub

		deal.setTitle(title.getText().toString());
		deal.setDescription(description.getText().toString());
		deal.setCode(code.getText().toString());
		deal.setImageURL(imageURL);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.ENGLISH);
		
		GregorianCalendar temp = new GregorianCalendar();
		
		try {
		
			temp.setTime(dateFormat.parse(fromDate.getText().toString()+" "+fromTime.getText().toString()));
			deal.setFrom(temp);
		
			temp.setTime(dateFormat.parse(toDate.getText().toString()+" "+toTime.getText().toString()));
			deal.setTo(temp);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		deal.getPlacement().setRegular(checkRegular.isChecked());
		deal.getPlacement().setSpecial(checkSpecial.isChecked());
		deal.getPlacement().setTopListing(checkTopListing.isChecked());
		deal.getPlacement().setHomePageBanner(checkHomePageBanner.isChecked());
		deal.getPlacement().setCategoryBanner(checkRegular.isChecked());
		
		deal.setSMSCount(Integer.parseInt(smsCount.getText().toString()));
		deal.setEmailCount(Integer.parseInt(emailCount.getText().toString()));
		
		
	}


	
}
