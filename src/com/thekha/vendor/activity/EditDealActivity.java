package com.thekha.vendor.activity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thekha.vendor.bean.Deals;
import com.thekha.vendor.bean.DealsPlacement;
import com.thekha.vendor.bean.Prices;
import com.thekha.vendor.dao.BusinessDAO;
import com.thekha.vendor.dao.LoginDAO;
import com.thekha.vendor.dao.PricesDAO;

public class EditDealActivity extends Activity {

	private String LOG_TAG;
	//private int RESULT_LOAD_IMAGE = 235;

	ActionBar actionBar;
	Deals oldDealObj, newDealObj;
	TextView title, description, code, from, to, smsMsg, emailMsg;	
	CheckBox checkRegular, checkSpecial, checkTopListing, checkHomePageBanner, checkCategoryBanner;
	EditText sms, email;
	Button changeImage;
	ImageView picture;
	//String picturePath, pictureName;
	String bid, uid;

	private ProgressDialog pDialog;

	private Prices prices;
	private PricesDAO pDAO = new PricesDAO();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_deal);
		setTitle(R.string.title_deals_view);
		LOG_TAG = getString(R.string.app_name);

		oldDealObj = (Deals) getIntent().getSerializableExtra(Deals.DEALS_KEY);
		bid = getIntent().getStringExtra(BusinessDAO.TAG_BID);
		uid = getIntent().getStringExtra(LoginDAO.TAG_USERID);

		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		title = (TextView) findViewById(R.id.editdeal_title);
		description = (TextView) findViewById(R.id.editdeal_description);
		code = (TextView) findViewById(R.id.editdeal_code);

		sms = (EditText) findViewById(R.id.editdeal_SMS);
		email = (EditText) findViewById(R.id.editdeal_email);

		from = (TextView) findViewById(R.id.editdeal_from);
		to = (TextView) findViewById(R.id.editdeal_to);
		checkRegular = (CheckBox) findViewById(R.id.editdeal_regular);
		checkSpecial = (CheckBox) findViewById(R.id.editdeal_special);
		checkTopListing = (CheckBox) findViewById(R.id.editdeal_top_listing);
		checkHomePageBanner = (CheckBox) findViewById(R.id.editdeal_home_page_banner);
		checkCategoryBanner = (CheckBox) findViewById(R.id.editdeal_category_banner);
		//changeImage = (Button) findViewById(R.id.editdeal_cover_image_button);
		picture = (ImageView) findViewById(R.id.editdeal_cover_image);
		smsMsg = (TextView) findViewById(R.id.editdeal_SMSmsg);
		emailMsg = (TextView) findViewById(R.id.editdeal_emailmsg);
		/*changeImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(
						Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});*/

		setUIFromBean();
	}

	/*
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
	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.actionmenu_edit_deal, menu);
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
			finish();
			break;
		case R.id.deal_done:
			if(setBeanFromUI()){
				Intent i = new Intent(EditDealActivity.this, TransactionActivity.class);
				i.putExtra(Prices.PRICES_KEY, prices);
				i.putExtra(Deals.DEALS_KEY, oldDealObj);
				i.putExtra(BusinessDAO.TAG_BID, bid);
				i.putExtra(LoginDAO.TAG_USERID, uid);
				i.putExtra("PrivateEDATAKey", newDealObj);
				startActivity(i);
				finish();
			}
			break;
		default:
			break;
		}
		return true;
	}

	private boolean setBeanFromUI() {

		DealsPlacement dp = new DealsPlacement(oldDealObj.getPlacement().getId(),
				checkRegular.isChecked(),
				checkSpecial.isChecked(),
				checkTopListing.isChecked(),
				checkHomePageBanner.isChecked(),
				checkCategoryBanner.isChecked());
		int cs, ce;
		if(!sms.getText().toString().isEmpty())
			cs = Integer.parseInt(sms.getText().toString());
		else
		{cs = 0;}

		if(!email.getText().toString().isEmpty())
			ce = Integer.parseInt(email.getText().toString());
		else
		{ce = 0;}
		
		newDealObj = new Deals(oldDealObj.getId(),
				oldDealObj.getTitle(),
				oldDealObj.getDescription(),
				oldDealObj.getCode(),
				oldDealObj.getImageURL(),
				oldDealObj.getFrom(),
				oldDealObj.getTo(),
				oldDealObj.getStatus(),
				dp,
				cs,
				ce);
		return true;
	}

	private void setUIFromBean() {
		title.setText(oldDealObj.getTitle());
		description.setText(oldDealObj.getDescription());
		code.setText(oldDealObj.getCode());
		
		from.setText(oldDealObj.getFrom().toString());
		to.setText(oldDealObj.getTo().toString());

		checkRegular.setChecked(oldDealObj.getPlacement().isRegular());
		// regular deal must be purchased.
		checkRegular.setChecked(true);
		checkRegular.setEnabled(false);

		checkSpecial.setChecked(oldDealObj.getPlacement().isSpecial());
		if(oldDealObj.getPlacement().isSpecial()){
			checkSpecial.setChecked(true);
			checkSpecial.setEnabled(false);
		}

		checkTopListing.setChecked(oldDealObj.getPlacement().isTopListing());
		if(oldDealObj.getPlacement().isTopListing()){
			checkTopListing.setChecked(true);
			checkTopListing.setEnabled(false);
		}

		checkHomePageBanner.setChecked(oldDealObj.getPlacement().isHomePageBanner());
		if(oldDealObj.getPlacement().isHomePageBanner()){
			checkHomePageBanner.setChecked(true);
			checkHomePageBanner.setEnabled(false);
		}

		checkCategoryBanner.setChecked(oldDealObj.getPlacement().isCategoryBanner());
		if(oldDealObj.getPlacement().isCategoryBanner()){
			checkCategoryBanner.setChecked(true);
			checkCategoryBanner.setEnabled(false);
		}

		smsMsg.setText("You have "+Integer.toString(oldDealObj.getSMSCount())+" SMS(s), only enter extra SMS(s) you want to buy.");
		emailMsg.setText("You have "+Integer.toString(oldDealObj.getEmailCount())+" E-Mail(s), only enter extra E-Mail(s) you want to buy.");
		
		String url = oldDealObj.getImageURL();
		if(!url.isEmpty()){
			String imageFileName = url.substring(url.lastIndexOf(File.separator)+1);
			File imageFile = new File(getApplicationContext().getExternalFilesDir(null)+File.separator+imageFileName);
			if(imageFile.exists()){
				picture.setImageURI(Uri.parse(getApplicationContext().getExternalFilesDir(null)+File.separator+imageFileName));
				new GetPriceTask().execute();
			}else{
				new DownloadFileFromURL().execute(url, imageFileName);
			}
		}

	}
	
	class DownloadFileFromURL extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
			if(!(activeNetworkInfo != null && activeNetworkInfo.isConnected())){
				Toast.makeText(getApplicationContext(), "Your internet is disabled, turn in on and then try again.", Toast.LENGTH_SHORT).show();
				cancel(true);
			}
			pDialog = new ProgressDialog(EditDealActivity.this);
			pDialog.setMessage("Looking for your cover image..");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			int count;
			try {
				URL url = new URL(params[0]);
				URLConnection conection = url.openConnection();
				conection.connect();
				int lenghtOfFile = conection.getContentLength();
				InputStream input = new BufferedInputStream(url.openStream(), 8192);
				OutputStream output = new FileOutputStream(getApplicationContext().getExternalFilesDir(null)+File.separator+params[1]);
				byte data[] = new byte[1024];
				long total = 0;
				while ((count = input.read(data)) != -1) {
					total += count;
					publishProgress("Downloading your cover image, "+(int)((total*100)/lenghtOfFile)+"% done.");
					output.write(data, 0, count);
				}
				output.flush();
				output.close();
				input.close();
			} catch (Exception e) {
				Log.d(LOG_TAG, e.getMessage());
				File f = new File(getApplicationContext().getExternalFilesDir(null)+File.separator+params[1]);
				if(f.exists())
					f.delete();
				publishProgress("dwf");
				pDialog.dismiss();
				cancel(true);
			}
			return getApplicationContext().getExternalFilesDir(null)+File.separator+params[1];
		}

		protected void onProgressUpdate(String... progress) {
			if(progress[0].equals("dwf")){
				Toast.makeText(getApplicationContext(), "Cannot find your cover image.", Toast.LENGTH_LONG).show();
				Log.d(LOG_TAG, "Cannot find business profile cover image.");
			}else{
				pDialog.setMessage(progress[0]);
				Log.d(LOG_TAG, progress[0]);
			}
				
		}

		@Override
		protected void onPostExecute(String img_path) {
			pDialog.dismiss();
			picture.setImageURI(Uri.parse(img_path));
			new GetPriceTask().execute();
		}
	}

	private class GetPriceTask  extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(EditDealActivity.this);
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
				startActivity(new Intent(EditDealActivity.this, DashboardActivity.class));
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
		checkRegular.setText(checkRegular.getText().toString() + " ("+prices.getRegular()+" credits/day)");
		checkSpecial.setText(checkSpecial.getText().toString() + " ("+prices.getSpecial()+" credits/day)");
		checkTopListing.setText(checkTopListing.getText().toString() + " ("+prices.getTopListing()+" credits/day)");
		checkHomePageBanner.setText(checkHomePageBanner.getText().toString() + " ("+prices.getHomePageBanner()+" credits/day)");
		checkCategoryBanner.setText(checkCategoryBanner.getText().toString() + " ("+prices.getCategoryBanner()+" credits/day)");
		sms.setHint(sms.getHint().toString() + " ("+prices.getPushSMS()+" credits/sms)");
		email.setHint(email.getHint().toString() + " ("+prices.getPushEMail()+" credits/email)");
	}
}
