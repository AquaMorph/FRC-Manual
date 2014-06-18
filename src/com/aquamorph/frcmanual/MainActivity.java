package com.aquamorph.frcmanual;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnSharedPreferenceChangeListener {
	
	ViewPager viewPager=null;
	private final String TAG = "Main Activity";

	//Menu
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
		case R.id.reload:
			Log.i(TAG, "Reload Item Clicked");
			reload();
			return true;
		case R.id.updateCache:
			Log.i(TAG, "Updates Cache Item Clicked");
			updateCache();
			return true;
		case R.id.action_settings:
			Log.i(TAG, "Settings Item Clicked");
			openSettings();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	private void reload() {
		finish();
		startActivity(getIntent());
	}
	public void openUpdates() {
		Intent intent = new Intent(this, Updates.class);
		startActivity(intent);
	}
	public void openSettings() {
		Intent intent = new Intent(this, Preference.class);
		startActivity(intent);
	}
	public void updateCache() {
		setUpdateCache(true);
		reload();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		viewPager = (ViewPager) findViewById(R.id.pager);
		// use a number higher than half your fragments.
        viewPager.setOffscreenPageLimit(6);
		FragmentManager fragmentManager=getSupportFragmentManager();
		viewPager.setAdapter(new MyAdapter(fragmentManager));
		
		
		//Functions.checkUpdate(getApplicationContext(), this);
		loadPreferences();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//	        Window w = getWindow(); // in Activity's onCreate() for instance
//	        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
	    }
		checkForReset();
	}
	
	protected void onLoadFinished(){
		//Functions.checkUpdate(getApplicationContext(), this);
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
	
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,String key) {
		loadPreferences();
		needReset = true;
	}
	
	public void loadPreferences() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	    boolean zoom = settings.getBoolean("enablezoom", false);
	    boolean cache = settings.getBoolean("enablecache", false);
	    String font = settings.getString("fontSize", "Large");
	    settings.registerOnSharedPreferenceChangeListener(MainActivity.this);
		if (zoom == true)setEnableZoom(true);
		else setEnableZoom(false);	
		if (cache == true)enablecache=true;
		else enablecache=false;	
		setFontSize(font);
		Log.i("test","Value: " + font);
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
    		reload();
    	}
	}
}

class MyAdapter extends FragmentPagerAdapter {

	public MyAdapter(FragmentManager fm) {
		super(fm);
	}
	
	@Override
	public Fragment getItem(int i) {
		Fragment fragment=null;
		if(i==0)fragment=new Summary();
		if(i==1)fragment=new Arena();
		if(i==2)fragment=new Game();
		if(i==3)fragment=new Robot();
		if(i==4)fragment=new Tournament();
		if(i==5)fragment=new Glossary();
		//if(i==6)fragment=new Rules();
		return fragment;
	}

	@Override
	public int getCount() {
		return 6;
	}
	
	public CharSequence getPageTitle(int position) {
		@SuppressWarnings("unused")
		String title=new String();
		if(position==0)return "Summary";
		if(position==1)return "The Arena";
		if(position==2)return "The Game";
		if(position==3)return "The Robot";
		if(position==4)return "The Tournament";
		if(position==5)return "Glossary";
		//if(position==6)return "Rules";
		return null;
	}
}
