package com.thekha.vendor.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.thekha.vendor.dao.BusinessDAO;
import com.thekha.vendor.dao.LoginDAO;

public class DealsListAdapter extends BaseAdapter {


	private List<Deals> dealsList; 
	
	private final Context mContext;
	private final String bid, uid;
	
	public DealsListAdapter(Context context, String bid1, String uid1) {
		mContext = context;
		dealsList = new ArrayList<Deals>();
		bid = bid1;
		uid = uid1;
	}

	public DealsListAdapter(Context context, String bid1, String uid1, List<Deals> deals) {
		mContext = context;
		dealsList = deals;
		bid = bid1;
		uid = uid1;
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
		
		final TextView DescView = (TextView) convertView.findViewById(R.id.dv_deal_desc);
		DescView.setText(deal.getDescription() == null ? "":deal.getDescription());
		
		final ImageButton editImageView = (ImageButton) convertView.findViewById(R.id.dv_edit);
		final TextView titleView = (TextView) convertView.findViewById(R.id.dv_deal_title);
		if(deal.getStatus().equals(Deals.STATUS_PENDING)){
			titleView.setTextColor(mContext.getResources().getColor(R.color.negative_transaction));
			titleView.setText(deal.getTitle()+" ("+deal.getStatus()+")");		// deal.getTitle() == null ? "":deal.getTitle()
			editImageView.setOnClickListener( new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent editDeals = new Intent( mContext.getApplicationContext(), EditDealActivity.class);
					editDeals.putExtra(Deals.DEALS_KEY, deal);
					editDeals.putExtra(BusinessDAO.TAG_BID, bid);
					editDeals.putExtra(LoginDAO.TAG_USERID, uid);
					mContext.startActivity(editDeals);
					((DealsViewActivity) mContext).finish();
					//((DealsViewActivity) mContext).startActivityForResult(editDeals, DealsViewActivity.EDIT_DEAL_REQUEST);
				}
			});
		}
		if(deal.getStatus().equals(Deals.STATUS_ACTIVE)){
			titleView.setTextColor(mContext.getResources().getColor(R.color.positive_transaction));
			titleView.setText(deal.getTitle()+" ("+deal.getStatus()+")");
			editImageView.setOnClickListener( new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent editDeals = new Intent( mContext.getApplicationContext(), EditDealActivity.class);
					editDeals.putExtra(Deals.DEALS_KEY, deal);
					editDeals.putExtra(BusinessDAO.TAG_BID, bid);
					editDeals.putExtra(LoginDAO.TAG_USERID, uid);
					mContext.startActivity(editDeals);
					((DealsViewActivity) mContext).finish();
				}
			});
		}
		if(deal.getStatus().equals(Deals.STATUS_COMPLETED)){
			titleView.setTextColor(Color.parseColor("#999999"));
			titleView.setText(deal.getTitle()+" ("+deal.getStatus()+")");
			editImageView.setVisibility(View.INVISIBLE);
		}
		
		return convertView;
	}

	public void update(Deals item) {
		if(dealsList.contains(item)){
			dealsList.set(dealsList.indexOf(item), item);
			notifyDataSetChanged();
		}
	}
	
}
