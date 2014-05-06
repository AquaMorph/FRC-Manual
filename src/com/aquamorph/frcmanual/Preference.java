package com.aquamorph.frcmanual;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Preference extends PreferenceActivity {
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.preference);
		
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean zoomenable = prefs.getBoolean("zoomenable", false);
		boolean enablecache = prefs.getBoolean("enablecache", false);
		String fontSize = prefs.getString("fontSize", "Large");
        System.out.println(zoomenable);
        System.out.println(enablecache);
        System.out.println(fontSize);
	}
}
