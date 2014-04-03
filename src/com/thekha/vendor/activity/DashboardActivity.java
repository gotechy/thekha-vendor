package com.thekha.vendor.activity;

import java.io.File;
import java.io.IOException;

import org.json.JSONException;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.thekha.vendor.bean.Business;
import com.thekha.vendor.dao.BusinessDAO;
import com.thekha.vendor.dao.LoginDAO;

public class DashboardActivity extends Activity {
	
	private String LOG_TAG;
	
	ActionBar actionBar;
	private ListView drawerList;
	private ActionBarDrawerToggle drawerToggle;
	private DrawerLayout drawerLayout;
	private String[] drawerMenu;
	private String title, drawerTitle;
	private String uid;
		
	Business business;
	BusinessDAO businessDAO = new BusinessDAO();;
	
	ArrayAdapter<CharSequence> productViewTypeAdapter;
	ArrayAdapter<String> drawerAdapter;
	Spinner productViewType;
	ProgressBar profileCompletion;
	
	private static final int ADD_DEAL_REQUEST = 0;
	private static final int CONTACT_US_REQUEST = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		setTitle(R.string.dashboard_activity_title);
		LOG_TAG = getString(R.string.app_name);
		
		// Get User ID for any other operation from intent.
		uid = getIntent().getStringExtra(LoginDAO.TAG_USERID);
		
        // ******** Setup Navigation Drawer *********
		actionBar = getActionBar();
        title = getResources().getString(R.string.dashboard_activity_title);
		drawerTitle = getResources().getString(R.string.drawer_title);
		drawerMenu = getResources().getStringArray(R.array.drawer_menu);
		
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        
        drawerAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.drawer_list_item, drawerMenu);
        drawerList.setAdapter(drawerAdapter);
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(title);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(drawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        
        //******* Setup Dashboard UI ***********
        productViewType = (Spinner) findViewById(R.id.dashboard_productviewtype);
        productViewTypeAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
				R.array.productView_types, R.layout.spinner_item);
        productViewType.setAdapter(productViewTypeAdapter);
        
        profileCompletion = (ProgressBar) findViewById(R.id.dashboard_profile);
        profileCompletion.setMax(100);
        profileCompletion.setIndeterminate(false);
        
	}
	
	@Override
	protected void onStart() {
		// Check UID, if null get from cache, otherwise login user again manually.
		if(uid == null){
        	try {
        		LoginDAO ldao = new LoginDAO();
    			uid = ldao.loginFromCache(getApplicationContext());
    		}
    		catch (IOException e) {
    			Toast.makeText(getApplicationContext(), "Please login again.", Toast.LENGTH_SHORT).show();
    			Intent i = new Intent(DashboardActivity.this, LoginActivity.class);
    			startActivity(i);
    			finish();
    		} catch (JSONException e) {
    			Toast.makeText(getApplicationContext(), "Please login again.", Toast.LENGTH_SHORT).show();
    			Intent i = new Intent(DashboardActivity.this, LoginActivity.class);
    			startActivity(i);
    			finish();
    		}
        }
		super.onStart();
	}
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        	TextView temp = (TextView) view;
        	if(temp.getText().toString().equals(drawerMenu[0])){
        		Intent i = new Intent(getApplicationContext(), AddDealActivity.class);
        		i.putExtra(LoginDAO.TAG_USERID, uid);
        		startActivityForResult(i, ADD_DEAL_REQUEST);
        	}
        	if(temp.getText().toString().equals(drawerMenu[1])){
        		Intent i = new Intent(getApplicationContext(), DealsViewActivity.class);
        		i.putExtra(LoginDAO.TAG_USERID, uid);
        		startActivity(i);
        	}
        	if(temp.getText().toString().equals(drawerMenu[2])){
        		Intent i = new Intent(getApplicationContext(), TransactionsViewActivity.class);
        		i.putExtra(LoginDAO.TAG_USERID, uid);
        		startActivity(i);
        	}
        	if(temp.getText().toString().equals(drawerMenu[3])){
        		Intent i = new Intent(getApplicationContext(), BusinessActivity.class);
        		i.putExtra(LoginDAO.TAG_USERID, uid);
        		startActivity(i);
        	}
        	if(temp.getText().toString().equals(drawerMenu[4])){
        		Intent i = new Intent(getApplicationContext(), ContactUsActivity.class);
        		i.putExtra(LoginDAO.TAG_USERID, uid);
        		startActivityForResult(i, CONTACT_US_REQUEST);
        	}
        	if(temp.getText().toString().equals(drawerMenu[5])){
        		new LoginDAO().logout(getApplicationContext());
        		finish();
        	}
        }
    }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		// - Check result code and request code.
		if(requestCode == ADD_DEAL_REQUEST && resultCode == RESULT_OK){
			// TODO - Result of add deal.
			// uid = data.getStringExtra(BusinessDAO.TAG_UID);
		}
		if(requestCode == CONTACT_US_REQUEST && resultCode == RESULT_OK){
			// TODO - Result of contact us.
		}
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// Save the user's current game state
		savedInstanceState.putString(LoginDAO.TAG_USERID, uid);
		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Always call the superclass so it can restore the view hierarchy
		uid = savedInstanceState.getString(LoginDAO.TAG_USERID);
		super.onRestoreInstanceState(savedInstanceState);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.actionmenu_empty, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	/* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        //boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
	    // action with ID action_refresh was selected
       	default:
           return super.onOptionsItemSelected(item);
	    }
	}
	

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        drawerToggle.onConfigurationChanged(newConfig);
    }
    
}
