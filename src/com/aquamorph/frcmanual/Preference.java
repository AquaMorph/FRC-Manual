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
		String version = prefs.getString("version", null);
		String summary = prefs.getString("summary", null);
		String arena = prefs.getString("arena", null);
		String game = prefs.getString("game", null);
		String robot = prefs.getString("robot", null);
		String tournament = prefs.getString("tournament", null);
		String glossary = prefs.getString("glossary", null);
	}
}
