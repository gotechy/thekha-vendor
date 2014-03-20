package com.thekha.vendor.adapter;

import java.util.ArrayList;
import java.util.List;

import com.thekha.vendor.activity.ActivityTemp;
import com.thekha.vendor.activity.DealsActivity;
import com.thekha.vendor.activity.DealsViewActivity;
import com.thekha.vendor.activity.R;
import com.thekha.vendor.bean.Deals;


import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DealsListAdapter extends BaseAdapter {


	private List<Deals> dealsList; 
	
	private final Context mContext;
	
	public DealsListAdapter(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		dealsList = new ArrayList<Deals>();
	}

	public DealsListAdapter(Context context, List<Deals> deals) {
		// TODO Auto-generated constructor stub
		mContext = context;
		dealsList = deals;
	}

	
	public void add(Deals item) {

		dealsList.add(item);
		notifyDataSetChanged();

	}
	
	// Clears the list adapter of all items.
	
	public void clear(){

		dealsList.clear();
		notifyDataSetChanged();
	
	}	
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dealsList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return dealsList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View arg1, ViewGroup parent) {
		// TODO Auto-generated method stub

		final Deals deal = dealsList.get(position);
		
		LayoutInflater layInf = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dealItemView = (View) layInf.inflate(R.layout.deals_item, parent, false);
		
		final TextView titleView = (TextView) dealItemView.findViewById(R.id.dv_deal_title);
		titleView.setText( deal.getTitle() == null ? "":deal.getTitle());
		
		final TextView DescView = (TextView) dealItemView.findViewById(R.id.dv_deal_desc);
		DescView.setText(deal.getDescription() == null ? "":deal.getDescription());
		
		final ImageButton EditImageView = (ImageButton) dealItemView.findViewById(R.id.dv_edit);
		EditImageView.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intentDealsActivity = new Intent( mContext, DealsActivity.class);
				intentDealsActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intentDealsActivity);
				
				
			}
		});
				
		return dealItemView;
	}


}
