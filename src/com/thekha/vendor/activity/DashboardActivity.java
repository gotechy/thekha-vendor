package com.thekha.vendor.activity;

import java.io.IOException;

import org.json.JSONException;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.thekha.vendor.dao.LoginDAO;

public class DashboardActivity extends FragmentActivity {
			
	ActionBar actionBar;
	private ListView drawerList;
	private ActionBarDrawerToggle drawerToggle;
	private DrawerLayout drawerLayout;
	private String[] drawerMenu;
	private String title, drawerTitle;
	private String uid;
	
	ArrayAdapter<String> drawerAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_dashboard);
		setTitle(R.string.dashboard_activity_title);
				
		// Get User ID for any other operation from intent.
		if(savedInstanceState==null)
			uid = getIntent().getStringExtra(LoginDAO.TAG_USERID);
		else{
			uid = savedInstanceState.getCharSequence(LoginDAO.TAG_USERID).toString();
		}
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
	}
	
	@Override
	protected void onStart() {
		// Check UID, if null get from cache, otherwise login user again manually.
		if(uid == null){
        	try {
        		LoginDAO ldao = new LoginDAO();
    			uid = ldao.loginFromCache(getApplicationContext());
    			if(uid == null){
    				Toast.makeText(getApplicationContext(), "Please login again.", Toast.LENGTH_SHORT).show();
        			Intent i = new Intent(DashboardActivity.this, LoginActivity.class);
        			startActivity(i);
        			finish();
    			}
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
        		startActivity(i);
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
        		startActivity(i);
        	}
        	if(temp.getText().toString().equals(drawerMenu[5])){
        		new LoginDAO().logout(getApplicationContext());
        		finish();
        	}
        }
    }

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		// Save the user's current state
		savedInstanceState.putString(LoginDAO.TAG_USERID, uid);
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		uid = savedInstanceState.getString(LoginDAO.TAG_USERID);
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
