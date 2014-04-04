package com.thekha.vendor.activity;

import hirondelle.date4j.DateTime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.thekha.vendor.bean.Deals;
import com.thekha.vendor.bean.Prices;
import com.thekha.vendor.dao.DealsDAO;
import com.thekha.vendor.dao.LoginDAO;
import com.thekha.vendor.dao.PricesDAO;
import com.thekha.vendor.util.UploadImage;

public class AddDealActivity extends Activity {
	
	private String LOG_TAG;
	private int RESULT_LOAD_IMAGE = 32;
	
	ActionBar actionBar;
	private Deals deal = new Deals();
	private DealsDAO dealDAO = new DealsDAO();
	private Prices prices;
	private PricesDAO pDAO = new PricesDAO();
	
//	TextView name, type, facilities,  phone1, phone2, address, email,website, facebook;
	EditText title, description, code, smsCount, emailCount;	
	CheckBox checkRegular, checkSpecial, checkTopListing, checkHomePageBanner, checkCategoryBanner;
	ImageButton picture;
	String picturePath, pictureName;
	static int fromToFlag;
	static Button  fromDate, fromTime, toDate, toTime;
	String imageURL, bid;
	private ProgressDialog pDialog;
	static String dateString, timeString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_deal);
		setTitle(R.string.title_deals_view);
		LOG_TAG = getString(R.string.app_name);
		bid = getIntent().getStringExtra(LoginDAO.TAG_USERID);
		// For current model uid == bid unless app implemented for multiple business profiles.
		
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
		picture = (ImageButton) findViewById(R.id.iButton_aed_cover_image);
		
		checkRegular = (CheckBox) findViewById(R.id.cb_regular);
		checkSpecial = (CheckBox) findViewById(R.id.cb_special);
		checkTopListing = (CheckBox) findViewById(R.id.cb_top_listing);
		checkHomePageBanner = (CheckBox) findViewById(R.id.cb_home_page_banner);
		checkCategoryBanner = (CheckBox) findViewById(R.id.cb_category_banner);
		
		// regular deal must be purchased.
		checkRegular.setChecked(true);
		checkRegular.setEnabled(false);
		
		picture.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(
						Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});
		
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
		
		new GetPriceTask().execute();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			pictureName = picturePath.substring(picturePath.lastIndexOf(File.separator)+1);
			cursor.close();
			try {
				File afile =new File(picturePath);
				picturePath = getApplicationContext().getExternalFilesDir(null) + File.separator + pictureName;
				File bfile =new File(picturePath);
				InputStream inStream = new FileInputStream(afile);
				
				OutputStream outStream = new FileOutputStream(bfile);
				byte[] buffer = new byte[1024];
				int length;
				while ((length = inStream.read(buffer)) > 0){
					outStream.write(buffer, 0, length);
				}
				inStream.close();
				outStream.close();
				picture.setImageBitmap(BitmapFactory.decodeFile(picturePath));
			} catch (FileNotFoundException e) {
				Toast.makeText(getApplicationContext(), "Cannot find selected cover image.", Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				Toast.makeText(getApplicationContext(), "Cannot find selected cover image.", Toast.LENGTH_SHORT).show();
			}
		}
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
				try {
					//TODO - Un-Comment this code.
					//UploadImage.upload(picturePath);
					deal.setImageURL(UploadImage.upload_folder+pictureName);
					return dealDAO.add(bid, deal);
				} catch (ClientProtocolException e) {
					return false;
				} catch (IOException e) {
					return false;
				}
			return false;
		}
		
		@Override
        protected void onPostExecute(Boolean param) {
            super.onPostExecute(param);
			pDialog.dismiss();
			if(param){
				Toast.makeText(getApplicationContext(), "Your deal has been added, you will hear from us shortly.", Toast.LENGTH_LONG).show();
				Log.d(LOG_TAG, "Deal successfully added, at "+DateTime.now(TimeZone.getDefault()));
				Intent i = new Intent();
				i.putExtra(Prices.PRICES_KEY, prices);
				i.putExtra(Deals.DEALS_KEY, deal);
				startActivity(i);
				finish();
			}else{
				Toast.makeText(getApplicationContext(), "Connection cannot be established, please try again later.", Toast.LENGTH_SHORT).show();
			}
		}
		
		@Override
		protected void onCancelled() {
			super.onCancelled();
			pDialog.dismiss();
		}
	}
	
	private class GetPriceTask  extends AsyncTask<Void, Void, String> {
		
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
		protected String doInBackground(Void... params) {
			if(!isCancelled())
				try {prices = null;
					prices = pDAO.read();
					return "Prices loaded successfully.";
				} catch (JSONException e) {
		        	return "Something is very wrong, please contact our support services.";
				} catch (ClientProtocolException e) {
					return "Connection cannot be established, please try again later.";
				} catch (IOException e) {
					return "Connection cannot be established, please try again later.";
				} 
			return null;
		}
		
		@Override
        protected void onPostExecute(String param) {
            super.onPostExecute(param);
			pDialog.dismiss();
			if(prices != null){
				setPrices();
                Log.d(LOG_TAG, param);
            }else{
            	Log.d(LOG_TAG, param);
            	Toast.makeText(getApplicationContext(), param, Toast.LENGTH_SHORT).show();
            	startActivity(new Intent(AddDealActivity.this, DashboardActivity.class));
            	finish();
            }
		}
		
		@Override
		protected void onCancelled() {
			super.onCancelled();
			pDialog.dismiss();
		}
	}
	
	private void setPrices() {
		checkRegular.setText(checkRegular.getText().toString() + " ("+prices.getRegular()+" credits)");
		checkSpecial.setText(checkSpecial.getText().toString() + " ("+prices.getSpecial()+" credits)");
		checkTopListing.setText(checkTopListing.getText().toString() + " ("+prices.getTopListing()+" credits)");
		checkHomePageBanner.setText(checkHomePageBanner.getText().toString() + " ("+prices.getHomePageBanner()+" credits)");
		checkCategoryBanner.setText(checkCategoryBanner.getText().toString() + " ("+prices.getCategoryBanner()+" credits)");
		smsCount.setHint(smsCount.getHint().toString() + " ("+prices.getPushSMS()+" credits per sms)");
		emailCount.setHint(emailCount.getHint().toString() + " ("+prices.getPushEMail()+" credits per email)");
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
	    	startActivity(getParentActivityIntent());
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
