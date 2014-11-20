package com.aquamorph.frcmanual;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.aquamorph.frcmanual.view.SlidingTabLayout;

public class MainActivity extends FragmentActivity implements OnSharedPreferenceChangeListener {

    SlidingTabLayout mSlidingTabLayout;
    Tabs mAdapter;
    ViewPager viewPager;
    Toolbar toolbar;

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
        toolbar.inflateMenu(R.menu.main);
        return true;
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);

        // Set an OnMenuItemClickListener to handle menu item clicks
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.updateCache:
                        updateCache();
                        return true;
                    case R.id.action_settings:
                        openSettings();
                        return true;
                    default:
                        return true;
                }
            }
        });

        // Inflate a menu to be displayed in the toolbar
        toolbar.inflateMenu(R.menu.main);

        mAdapter = new Tabs(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(6);
        viewPager.setAdapter(mAdapter);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.text_light));
        mSlidingTabLayout.setViewPager(viewPager);

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
}