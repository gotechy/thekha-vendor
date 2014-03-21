package com.thekha.vendor.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.thekha.vendor.activity.DealsViewActivity;
import com.thekha.vendor.activity.EditDealActivity;
import com.thekha.vendor.activity.R;
import com.thekha.vendor.bean.Deals;

public class DealsListAdapter extends BaseAdapter {


	private List<Deals> dealsList; 
	
	private final Context mContext;
	
	public DealsListAdapter(Context context) {
		mContext = context;
		dealsList = new ArrayList<Deals>();
	}

	public DealsListAdapter(Context context, List<Deals> deals) {
		mContext = context;
		dealsList = deals;
	}

	public void add(Deals item) {
		dealsList.add(item);
		notifyDataSetChanged();
	}
	
	public void clear(){
		dealsList.clear();
		notifyDataSetChanged();
	}	
	
	
	@Override
	public int getCount() {
		return dealsList.size();
	}

	@Override
	public Object getItem(int pos) {
		return dealsList.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Deals deal = dealsList.get(position);
		
		if(convertView == null){
			LayoutInflater layInf = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = (View) layInf.inflate(R.layout.deals_item, parent, false);
		}
		
		final TextView titleView = (TextView) convertView.findViewById(R.id.dv_deal_title);
		titleView.setText( deal.getTitle() == null ? "":deal.getTitle());
		
		final TextView DescView = (TextView) convertView.findViewById(R.id.dv_deal_desc);
		DescView.setText(deal.getDescription() == null ? "":deal.getDescription());
		
		final ImageButton EditImageView = (ImageButton) convertView.findViewById(R.id.dv_edit);
		EditImageView.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent editDeals = new Intent( mContext.getApplicationContext(), EditDealActivity.class);
				editDeals.putExtra(Deals.DEALS_KEY, deal);
				((DealsViewActivity) mContext).startActivityForResult(editDeals, DealsViewActivity.EDIT_DEAL_REQUEST);
			}
		});
		
		return convertView;
	}

	public void update(Deals item) {
		if(dealsList.contains(item)){
			dealsList.set(dealsList.indexOf(item), item);
			notifyDataSetChanged();
		}
		
	}
	
}
