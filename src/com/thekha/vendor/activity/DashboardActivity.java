package com.thekha.vendor.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.thekha.vendor.bean.Business;
import com.thekha.vendor.dao.BusinessDAO;

public class DashboardActivity extends Activity {
	
	ActionBar actionBar;
	private ListView drawerList;
	private ActionBarDrawerToggle drawerToggle;
	private DrawerLayout drawerLayout;
	private String[] drawerMenu;
	String title, drawerTitle;
		
	Business business;
	BusinessDAO businessDAO;
	
	ArrayAdapter<CharSequence> productViewTypeAdapter;
	Spinner productViewType;
	ProgressBar profileCompletion;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		setTitle(R.string.dashboard_activity_title);
        
		// ******** Setup Navigation Drawer *********
		actionBar = getActionBar();
        title = getResources().getString(R.string.dashboard_activity_title);
		drawerTitle = getResources().getString(R.string.drawer_title);
		drawerMenu = getResources().getStringArray(R.array.drawer_menu);
		
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawerList.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.drawer_list_item, drawerMenu));
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
        //**************************************
        // TODO - get from server
        //business = businessDAO.read();
        
        //******* Setup Dashboard UI ***********
        productViewType = (Spinner) findViewById(R.id.dashboard_productviewtype);
        productViewTypeAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
				R.array.productView_types, R.layout.spinner_item);
        productViewType.setAdapter(productViewTypeAdapter);
        
        profileCompletion = (ProgressBar) findViewById(R.id.dashboard_profile);
        profileCompletion.setMax(100);
        profileCompletion.setIndeterminate(false);
        businessDAO = new BusinessDAO();
        business = businessDAO.read();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		profileCompletion.setProgress(business.getCompletionPercentage());
		
		
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
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        	TextView temp = (TextView) view;
        	if(temp.getText().toString().equals(drawerMenu[0]))
        		startActivity(new Intent(getApplicationContext(), BusinessActivity.class));
        	if(temp.getText().toString().equals(drawerMenu[1]))
        		startActivity(new Intent(getApplicationContext(), BusinessActivity.class));
        	if(temp.getText().toString().equals(drawerMenu[2]))
        		startActivity(new Intent(getApplicationContext(), TransactionActivity.class));
        	if(temp.getText().toString().equals(drawerMenu[3]))
        		startActivity(new Intent(getApplicationContext(), BusinessActivity.class));
        	if(temp.getText().toString().equals(drawerMenu[4]))
        		startActivity(new Intent(getApplicationContext(), ContactUsActivity.class));
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
