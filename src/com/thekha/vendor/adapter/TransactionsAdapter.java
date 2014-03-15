package com.thekha.vendor.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thekha.vendor.activity.R;
import com.thekha.vendor.bean.Transaction;
import com.thekha.vendor.util.DateTimeConversion;

public class TransactionsAdapter extends BaseAdapter {

	private final Context context;
	private final List<Transaction> transactions = new ArrayList<Transaction>();
	
	public TransactionsAdapter(Context c){
		context = c;
	}
	
	public void add(Transaction t){
		transactions.add(t);
		notifyDataSetChanged();
	}
	
	public void clear(){
		transactions.clear();
		notifyDataSetInvalidated();
	}
	
	@Override
	public int getCount() {
		return transactions.size();
	}

	@Override
	public Object getItem(int position) {
		return transactions.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View itemLayout;
		final Transaction transaction;
		final TextView title, amount, description, timeStamp;
		
		if (convertView == null) {
			LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			itemLayout = inf.inflate(R.layout.transaction_listview_item, null);
        }else{
        	itemLayout = convertView;
        }
		
		transaction = transactions.get(position);
		
		title = (TextView) itemLayout.findViewById(R.id.transaction_title);
		title.setText(transaction.getTitle());
		
		amount = (TextView) itemLayout.findViewById(R.id.transaction_amount);
		amount.setText(String.valueOf(transaction.getAmount()));
		if(transaction.getAmount()<0)
			amount.setTextColor(context.getResources().getColor(R.color.negative_transaction));
		else
			amount.setTextColor(context.getResources().getColor(R.color.positive_transaction));
		
		description = (TextView) itemLayout.findViewById(R.id.transaction_description);
		description.setText(transaction.getDescription());
		
		timeStamp = (TextView) itemLayout.findViewById(R.id.transaction_timestamp);
		timeStamp.setText(transaction.getDateTime().format(DateTimeConversion.FF_NUMERIC));
		
		return itemLayout;
	}

}
