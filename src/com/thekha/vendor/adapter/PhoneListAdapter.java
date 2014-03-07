package com.thekha.vendor.adapter;

import java.util.ArrayList;
import java.util.List;

import com.thekha.vendor.activity.R;
import com.thekha.vendor.bean.Phone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class PhoneListAdapter extends BaseAdapter {

	private final Context context;
	private final List<Phone> phones = new ArrayList<Phone>();
	
	public PhoneListAdapter(Context c){
		context = c;
	}
	
	public void add(Phone p){
		phones.add(p);
		notifyDataSetChanged();
	}
	
	public void clear(){
		phones.clear();
		notifyDataSetInvalidated();
	}
	
	@Override
	public int getCount() {
		return phones.size();
	}

	@Override
	public Object getItem(int position) {
		return phones.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Phone phone = phones.get(position);
		
		LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		TableRow itemLayout = (TableRow) inf.inflate(R.layout.edit_phone_listview, null);
		
		final EditText phoneNum = (EditText) itemLayout.findViewById(R.id.editbusiness_phone);
		if(!phone.getNumber().isEmpty())
			phoneNum.setText(phone.getNumber());
		
		final Spinner phoneTypeSpinner = (Spinner) itemLayout.findViewById(R.id.editbusiness_phonetype);
		ArrayAdapter<CharSequence> phoneTypeSpinnerrAdapter = ArrayAdapter.createFromResource(context,
				R.array.phone_types, android.R.layout.simple_list_item_1);
		phoneTypeSpinner.setAdapter(phoneTypeSpinnerrAdapter);
		if(!phone.getType().isEmpty()){
			phoneTypeSpinner.setSelection(phoneTypeSpinnerrAdapter.getPosition(phone.getType()));
		}
		phoneTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				TextView tv = (TextView) view;
				Toast.makeText(context, tv.getText(), Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// do nothing
			}
		});
		return null;
	}

}
