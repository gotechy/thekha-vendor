package com.thekha.vendor.activity;

import com.thekha.vendor.adapter.DealsListAdapter;
import com.thekha.vendor.bean.Deals;
import com.thekha.vendor.dao.DealsDAO;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;

public class DealsViewActivity extends ListActivity {

	DealsListAdapter deals_list_adapter;
	Deals deal;	
	
	public DealsViewActivity() {
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		deals_list_adapter = new DealsListAdapter(getApplicationContext());
		
		setListAdapter(deals_list_adapter);
		
        deal = (Deals) getIntent().getSerializableExtra(Deals.DEALS_KEY);
		
		deal = new DealsDAO().read();
		
		
	}	
	
	public void onResume() {
		super.onResume();

		// Load saved ToDoItems, if necessary

		if (deals_list_adapter.getCount() == 0)
			loadItems();
	}

	private void loadItems() {

	deals_list_adapter.add(deal);
		
	}
	
	
	
}
