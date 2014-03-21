package com.thekha.vendor.activity;

import hirondelle.date4j.DateTime;

import java.util.TimeZone;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.thekha.vendor.bean.Business;
import com.thekha.vendor.dao.BusinessDAO;

public class EditBusinessActivity extends Activity {

	private String LOG_TAG;
	private int uid, RESULT_LOAD_IMAGE = 0;

	Business business;
	BusinessDAO businessDAO = new BusinessDAO();;
	ActionBar actionBar;
	EditText name, phone1, phone2, facebook, website, email, addressLine1, addressLine2, locality, city, country, pinCode;
	Spinner type;
	CheckBox checkAC, checkVP, checkCC, checkSA, checkVeg, checkNonVeg;
	AutoCompleteTextView state;
	Button changeImage;
	ImageView iv;
	private ProgressDialog pDialog;

	ArrayAdapter<CharSequence> typeAdapter;
	ArrayAdapter<String> stateAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_business);
		setTitle(R.string.edit_business);
		LOG_TAG = getString(R.string.app_name);

		actionBar = getActionBar();

		business = (Business) getIntent().getSerializableExtra(Business.BUSINESS_KEY);
		uid = Integer.parseInt(getIntent().getStringExtra(BusinessDAO.TAG_UID));

		// populate spinner for business type
		type = (Spinner) findViewById(R.id.editbusiness_type);
		typeAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
				R.array.business_types, R.layout.spinner_item);
		type.setAdapter(typeAdapter);

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

		iv = (ImageView) findViewById(R.id.editbusiness_picture);

		changeImage = (Button) findViewById(R.id.editbusiness_picture_button);
		changeImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO - Change Image of business profile
				Intent i = new Intent(
						Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});

		// set state to be auto complete
		state = (AutoCompleteTextView) findViewById(R.id.editbusiness_state);
		stateAdapter = new ArrayAdapter<String>(getApplicationContext(),
				R.layout.spinner_item, getResources().getStringArray(R.array.indian_states));
		state.setAdapter(stateAdapter);

		setUIFromBean();
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
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			iv.setImageBitmap(BitmapFactory.decodeFile(picturePath));
			business.setImageURL(picturePath);
			
			//TODO - Upload Selected Picture
		}
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
		case R.id.business_done:
			new EditBusinessTask().execute();
			break;
		case R.id.business_cancel:
			setResult(RESULT_CANCELED);
			finish();
		default:
			break;
		}
		return true;
	}

	private class EditBusinessTask  extends AsyncTask<Void, Void, Boolean> {

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
			pDialog = new ProgressDialog(EditBusinessActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			setBeanFromUI();
			return businessDAO.update(uid, business);
		}

		@Override
		protected void onPostExecute(Boolean param) {
			super.onPostExecute(param);
			pDialog.dismiss();
			if(param){
				Toast.makeText(getApplicationContext(), "Your business profile has been updated.", Toast.LENGTH_SHORT).show();
				Log.d(LOG_TAG, "Business profile successfully updated, at "+DateTime.now(TimeZone.getDefault()));
				Intent data = new Intent();
				data.putExtra(Business.BUSINESS_KEY, business);
				setResult(RESULT_OK,data);
				finish();
			}else{
				Toast.makeText(getApplicationContext(), "Business profile cannot be saved, please try again later.", Toast.LENGTH_SHORT).show();
			}
		}
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