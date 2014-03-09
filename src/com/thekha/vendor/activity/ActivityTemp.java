package com.thekha.vendor.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ActivityTemp extends Activity implements OnClickListener {

	public ActivityTemp() {
		// TODO Auto-generated constructor stub

	
	}
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.temp_screen);
		setTitle("TEMP");
		
		Button b1 = (Button) findViewById(R.id.button1);
		Button b2 = (Button) findViewById(R.id.button2);
		Button b3 = (Button) findViewById(R.id.button3);
		
		b1.setOnClickListener(this);
		b2.setOnClickListener(this);
		b3.setOnClickListener(this);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.business, menu);
		return super.onCreateOptionsMenu(menu);
	}
	

		@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
	
			Button b = (Button) arg0;
			

			if (b.getText().equals("Button1"))
			{
				startActivity(new Intent(ActivityTemp.this, DealsActivity.class));
			}
			if (b.getText().equals("Button2"))
			{
				startActivity(new Intent(ActivityTemp.this, BusinessActivity.class));
			}
			
	}


}
