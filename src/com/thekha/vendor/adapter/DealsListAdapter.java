package com.thekha.vendor.adapter;

import java.util.ArrayList;
import java.util.List;

import com.thekha.vendor.bean.Deals;


import android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

public class DealsListAdapter extends BaseAdapter {


	private final List<Deals> dealsList = new ArrayList<Deals>(); 
	
	private final Context mContext;
	
	public DealsListAdapter(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
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
	public View getView(int position, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub

		final Deals deal = dealsList.get(position);
		
		LayoutInflater layInf = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout dealItemView = layInf.inflate(R.layout.activity_list_item, root)
		
		
		
		
		
		
		
		return null;
	}

}
