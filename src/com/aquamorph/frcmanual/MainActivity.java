package com.aquamorph.frcmanual;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity implements OnSharedPreferenceChangeListener,
        ActionBar.TabListener {

    Tabs mAdapter;
    ViewPager viewPager;

    // Tab titles
    private String[] tabs = {"Summary","The Arena","The Game","The Robot","The Tournament",
            "Glossary"};

	//Menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
		case R.id.reload:
			reload(this, this);
			return true;
		case R.id.updateCache:
			updateCache();
			return true;
		case R.id.action_settings:
			openSettings();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
    public static void reload(Context anyActivity, Activity activity) {
        activity.finish();
        Intent main = new Intent(anyActivity, MainActivity.class);
        anyActivity.startActivity(main);
    }
	public void openSettings() {
        Intent intent = new Intent(this, Preference.class);
		startActivity(intent);
	}
	public void updateCache() {
		setUpdateCache(true);
		reload(this,this);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    //sets up menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
                // Initilization
        mAdapter = new Tabs(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(6);
        viewPager.setAdapter(mAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // Adding tabs with titles
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
        }
		
		new JSON(this,this).execute("http://frc-manual.usfirst.org/a/GetAllItems/ManualID=3");
		loadPreferences();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		checkForReset();
	}
	
	//Preferences
	public static Boolean enablezoom = false;
	public static Boolean enablecache = false;
	public static Boolean updatecache = false;
	public static String fontSize = null;
	Boolean needReset = false;

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	  super.onConfigurationChanged(newConfig);
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,String key) {
		loadPreferences();
		needReset = true;
	}
	
	public void loadPreferences() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(
                getApplicationContext());
	    boolean zoom = settings.getBoolean("enablezoom", false);
	    boolean cache = settings.getBoolean("enablecache", false);
	    String font = settings.getString("fontSize", "Large");
	    settings.registerOnSharedPreferenceChangeListener(MainActivity.this);
		if (zoom == true)setEnableZoom(true);
		else setEnableZoom(false);	
		if (cache == true)enablecache=true;
		else enablecache=false;	
		setFontSize(font);
	}
	
	public static  Boolean getEnableZoom() {
		return enablezoom;
	}
	public static void setEnableZoom(Boolean input) {
		enablezoom = input;
	}
	public static  Boolean getEnableCache() {
		return enablecache;
	}
	public static Boolean getUpdateCache() {
		return updatecache;
	}
	public static String getFontSize() {
		return fontSize;
	}
	public void setUpdateCache(Boolean input) {
		updatecache = input;
	}
	public void setFontSize(String input) {
		fontSize = input;
	}
	
	public void checkForReset() {
		while (needReset==true) {
    		needReset = false;
    		reload(this, this);
    	}
	}

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {}

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {}
}