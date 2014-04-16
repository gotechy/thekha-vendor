package com.thekha.vendor.fragment;

import com.thekha.vendor.activity.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

public class DashboardFragment extends Fragment {
	
	ArrayAdapter<CharSequence> productViewTypeAdapter;
	Spinner productViewType;
	ProgressBar profileCompletion;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view =  inflater.inflate(R.layout.fragment_dashboard, container, false);
		
		//******* Setup Dashboard UI ***********
        productViewType = (Spinner) view.findViewById(R.id.dashboard_productviewtype);
        productViewTypeAdapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
				R.array.productView_types, R.layout.spinner_item);
        productViewType.setAdapter(productViewTypeAdapter);
        
        profileCompletion = (ProgressBar) view.findViewById(R.id.dashboard_profile);
        profileCompletion.setMax(100);
        profileCompletion.setIndeterminate(false);
        profileCompletion.setProgress(50);
        
        return view;
	}
}
