package com.thekha.vendor.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.thekha.vendor.adapter.TransactionsAdapter;
import com.thekha.vendor.bean.Transaction;
import com.thekha.vendor.dao.TransactionDAO;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

public class TransactionsViewActivity extends Activity {
	
	TextView balance;
	ActionBar actionBar;
	List<Transaction> transactions = new ArrayList<Transaction>();
	TransactionsAdapter transactionAdapter;
	ListView transactionsListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transactions_view);
		setTitle(R.string.transaction_activity_title);
		
		actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
		transactions.add(TransactionDAO.read());
		transactions.add(TransactionDAO.read());
		transactions.add(TransactionDAO.read());
		
		balance = (TextView) findViewById(R.id.transactions_balance);
		int amount = transactions.get(0).getBalance();
		balance.setText(String.valueOf(amount));
		
		transactionsListView = (ListView) findViewById(R.id.transactions_transactions);
		transactionAdapter = new TransactionsAdapter(getApplicationContext());
		Iterator<Transaction> itr = transactions.iterator();
		while(itr.hasNext()){
			transactionAdapter.add(itr.next());
		}
		transactionsListView.setAdapter(transactionAdapter);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.actionmenu_business, menu);
		//return super.onCreateOptionsMenu(menu);
		return true;
	}
	
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // action with ID action_refresh was selected
	    case android.R.id.home:
          Intent upIntent = NavUtils.getParentActivityIntent(this);
          if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
              // This activity is NOT part of this app's task, so create a new task
              // when navigating up, with a synthesized back stack.
              TaskStackBuilder.create(this)
                      // Add all of this activity's parents to the back stack
                      .addNextIntentWithParentStack(upIntent)
                      // Navigate up to the closest parent
                      .startActivities();
          } else {
              // This activity is part of this app's task, so simply
              // navigate up to the logical parent activity.
              NavUtils.navigateUpTo(this, upIntent);
          }
          return true;
	    }
	    return true;
	  }
}
