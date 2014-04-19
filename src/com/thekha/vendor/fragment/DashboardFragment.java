package com.thekha.vendor.fragment;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.thekha.vendor.activity.DashboardActivity;
import com.thekha.vendor.activity.R;
import com.thekha.vendor.bean.Dashboard;
import com.thekha.vendor.dao.DashboardDAO;

public class DashboardFragment extends Fragment {
	
	ArrayAdapter<CharSequence> productViewTypeAdapter;
	Spinner productViewType;
	ProgressBar profileCompletion;
	private ProgressDialog pDialog;
	ImageView iv ;
	TextView businessName, profileViews, noOfDealsActive, noOfDeals, credits, advertising, banner, clciks;
	
	Dashboard d;
	DashboardDAO ddao;
	View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		view =  inflater.inflate(R.layout.fragment_dashboard, container, false);
		
		//******* Setup Dashboard UI ***********
        productViewType = (Spinner) view.findViewById(R.id.dashboard_productviewtype);
        productViewTypeAdapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
				R.array.productView_types, R.layout.spinner_item);
        productViewType.setAdapter(productViewTypeAdapter);
        
        profileCompletion = (ProgressBar) view.findViewById(R.id.dashboard_profile);
        profileCompletion.setMax(100);
        profileCompletion.setIndeterminate(false);
        
        iv = (ImageView) view.findViewById(R.id.dashboard_businessListing);
        businessName = (TextView) view.findViewById(R.id.dashboard_product);
        profileViews = (TextView) view.findViewById(R.id.dashboard_productview);
        noOfDealsActive = (TextView) view.findViewById(R.id.dashboard_dealsavtive);
        noOfDeals = (TextView) view.findViewById(R.id.dashboard_dealstill);
        credits = (TextView) view.findViewById(R.id.dashboard_credit);
        advertising = (TextView) view.findViewById(R.id.dashboard_advertising);
        banner = (TextView) view.findViewById(R.id.dashboard_banner);
        clciks = (TextView) view.findViewById(R.id.dashboard_clicks);
        
        new BusinessTask().execute();
        return view;
	}
	
	private class BusinessTask  extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
			// check for internet connection
			ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
			if(!(activeNetworkInfo != null && activeNetworkInfo.isConnected())){
				Toast.makeText(getActivity().getApplicationContext(), "Your internet is disabled, turn it on and retry.", Toast.LENGTH_SHORT).show();
				cancel(true);
			}
		}

		@Override
		protected String doInBackground(Void... params) {
			if(!isCancelled()){
				try {
					ddao = new DashboardDAO();
					d = ddao.updateDashboard(((DashboardActivity)getActivity()).uid);
					return "Business profile successfully loaded.";
				} catch (JSONException e) {
					return "Something is very wrong, please contact our support services.";
				} catch (ClientProtocolException e) {
					return "Connection cannot be established, please try again later.";
				} catch (IOException e) {
					return "Connection cannot be established, please try again later.";
				} 
			}
			return null;
		}

		@Override
		protected void onPostExecute(String param) {
			super.onPostExecute(param);
			pDialog.dismiss();
			if(d!=null){
				setUIFromBean();
				Log.d(((DashboardActivity)getActivity()).LOG_TAG, param);
			}else{
				Log.d(((DashboardActivity)getActivity()).LOG_TAG, param);
				Toast.makeText(getActivity().getApplicationContext(), param, Toast.LENGTH_SHORT).show();
				startActivity(new Intent(getActivity(), DashboardActivity.class));
				getActivity().finish();
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			pDialog.dismiss();
		}

	}

	private void setUIFromBean() {
		// TODO Auto-generated method stub
		if(d.getStatus()==1){
			iv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_check));
		}else{
			iv.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_warning));
		}
		profileCompletion.setProgress(d.getProfileCompleteness());
		businessName.setText(d.getBusinessName());
        profileViews.setText(String.valueOf(d.getProfileViews()));
        noOfDealsActive.setText(String.valueOf(d.getNoOfDealsActive()));
        noOfDeals.setText(String.valueOf(d.getNoOfDeals()));
        credits.setText(String.valueOf(d.getCreditsBalance()));
        advertising.setText(String.valueOf(d.getAdvertising()));
        banner.setText(String.valueOf(d.getBanner()));
        clciks.setText(String.valueOf(d.getClicks()));
	}
}