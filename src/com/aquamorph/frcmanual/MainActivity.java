package com.aquamorph.frcmanual;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.net.ParseException;
import android.os.AsyncTask;
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
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
		case R.id.reload:
			//Log.i(TAG, "Reload Item Clicked");
			reload();
			return true;
		case R.id.updateCache:
			//Log.i(TAG, "Updates Cache Item Clicked");
			updateCache();
			return true;
		case R.id.action_settings:
			//Log.i(TAG, "Settings Item Clicked");
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
        viewPager.setOffscreenPageLimit(6);
		FragmentManager fragmentManager=getSupportFragmentManager();
		viewPager.setAdapter(new MyAdapter(fragmentManager));
		
		new JSON().execute("http://frc-manual.usfirst.org/a/GetAllItems/ManualID=3");
		loadPreferences();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//	        Window w = getWindow(); // in Activity's onCreate() for instance
//	        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//	    }
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
	
	
	class JSON extends AsyncTask<String, Void, Boolean> {
		
		Boolean updating = false;
			
			ProgressDialog dialog;
			
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				Toast.makeText(getApplicationContext(), "Checking for update", Toast.LENGTH_SHORT).show();			
			}
			
			@Override
			protected Boolean doInBackground(String... urls) {
				try {
					HttpGet httppost = new HttpGet(urls[0]);
					HttpClient httpclient = new DefaultHttpClient();
					HttpResponse response = httpclient.execute(httppost);

					int status = response.getStatusLine().getStatusCode();

					if (status == 200) {
						SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
						HttpEntity entity = response.getEntity();
						String data = EntityUtils.toString(entity);
						String version = null;
						JSONObject reader = new JSONObject(data);
						JSONObject main  = reader.getJSONObject("data");
						if(main.has("LatestManualUpdate")){
							version = main.getString("LatestManualUpdate");
						}
				        if(!version.equals(//"1")){
				        		prefs.getString("version", null))){
				        	updating = true;
				    		//Update Version Number
				        	Editor editor = prefs.edit();
				            editor.putString("version", version);
				            editor.commit();
				            //Update HTML
				        	Functions.saveFile("summary", html(reader,"178"), getApplicationContext());
					        Functions.saveFile("arena", html(reader,"179"), getApplicationContext());
					        Functions.saveFile("game", html(reader,"180"), getApplicationContext());
					        Functions.saveFile("robot", html(reader,"181"), getApplicationContext());
					        Functions.saveFile("tournament", html(reader,"182"), getApplicationContext());
					        Functions.saveFile("glossary", html(reader,"183"), getApplicationContext());
				        }
						return true;
					}
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return false;
			}
			
			@Override
			protected void onPostExecute(Boolean result) {
				if(result == false)Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();
				if(updating == true){
					Toast.makeText(getApplicationContext(), "Reloading update", Toast.LENGTH_SHORT).show();
					reload();
				}

			}
		}
		
		
		public String html(JSONObject reader, String page) throws JSONException{
			String html = "<html><body>";

	        JSONObject head = reader.getJSONObject("data").getJSONObject("SubChapter").getJSONObject("3").getJSONObject("children").getJSONObject(page);
	        html = html + head.getString("item_content_text");
	        
	        if(head.has("children")){
		         JSONObject children = head.getJSONObject("children");
		         JSONArray sub1 = new JSONArray(children.names().toString());
		         List<String> reorder1 = new ArrayList<String>();
		         for(int i=sub1.length()-1;i>=0;i--){
		        	 reorder1.add(sub1.getString(i));
		         }
		         Collections.sort(reorder1);
		         sub1 = new JSONArray(reorder1);
		         if(page.equals("183")){
			         for(int i=sub1.length()-1;i>=0;i--){
			        	 JSONObject children2 = children.getJSONObject(Integer.toString(sub1.getInt(i)));
			        	 html = html + "<h2>" + children2.getString("secdisp")+ " " + children2.getString("item_name") + "</h2>";
			        	 html = html + children2.getString("item_content_text");
			        	 if(children2.has("children")){
				        	 JSONObject children3 = children2.getJSONObject("children");
				        	 JSONArray sub2 = new JSONArray(children3.names().toString());
				        	 List<String> reorder2 = new ArrayList<String>();
					         for(int j=sub2.length()-1;j>=0;j--){
					        	 reorder2.add(sub2.getString(j));
					         }
					         Collections.sort(reorder2);
					         sub2 = new JSONArray(reorder2);
				        	 for(int j=0;j<sub2.length();j++){
				        		 JSONObject children4 = children3.getJSONObject((String) sub2.get(j));
				        		 html = html + "<h3>" + children4.getString("secdisp")+ " " + children4.getString("item_name") + "</h3>";
				        		 html = html + children4.getString("item_content_text");
				        		 if(children4.has("children")){
				        			 JSONObject children5 = children4.getJSONObject("children");
				        			 JSONArray sub3 = new JSONArray(children5.names().toString());
				        			 List<String> reorder3 = new ArrayList<String>();
							         for(int k=sub3.length()-1;k>=0;k--){
							        	 reorder3.add(sub3.getString(k));
							         }
							         Collections.sort(reorder3);
							         sub3 = new JSONArray(reorder3);
				        			 for(int k=0;k<sub3.length();k++){
				        				 JSONObject children6 = children5.getJSONObject((String) sub3.get(k));
				        				 html = html + children6.getString("item_name");
				        				 html = html + children6.getString("item_content_text");
				        			 }
				        		 }
				        	 }
			        	 }
			         }
		         }
		         else {
		        	 for(int i=0;i<sub1.length();i++){
			        	 JSONObject children2 = children.getJSONObject(Integer.toString(sub1.getInt(i)));
			        	 html = html + "<h2>" + children2.getString("secdisp")+ " " + children2.getString("item_name") + "</h2>";
			        	 html = html + children2.getString("item_content_text");
			        	 if(children2.has("children")){
				        	 JSONObject children3 = children2.getJSONObject("children");
				        	 JSONArray sub2 = new JSONArray(children3.names().toString());
				        	 List<String> reorder2 = new ArrayList<String>();
					         for(int j=sub2.length()-1;j>=0;j--){
					        	 reorder2.add(sub2.getString(j));
					         }
					         Collections.sort(reorder2);
					         sub2 = new JSONArray(reorder2);
				        	 for(int j=0;j<sub2.length();j++){
				        		 JSONObject children4 = children3.getJSONObject((String) sub2.get(j));
				        		 html = html + "<h3>" + children4.getString("secdisp")+ " " + children4.getString("item_name") + "</h3>";
				        		 html = html + children4.getString("item_content_text");
				        		 if(children4.has("children")){
				        			 JSONObject children5 = children4.getJSONObject("children");
				        			 JSONArray sub3 = new JSONArray(children5.names().toString());
				        			 List<String> reorder3 = new ArrayList<String>();
							         for(int k=sub3.length()-1;k>=0;k--){
							        	 reorder3.add(sub3.getString(k));
							         }
							         Collections.sort(reorder3);
							         sub3 = new JSONArray(reorder3);
				        			 for(int k=0;k<sub3.length();k++){
				        				 JSONObject children6 = children5.getJSONObject((String) sub3.get(k));
				        				 html = html + children6.getString("item_name");
				        				 html = html + children6.getString("item_content_text");
				        			 }
				        		 }
				        	 }
			        	 }
			         }
		         }
	        }
	        html = html + "</body></html>";
	        return html;
		}
	}


class MyAdapter extends FragmentPagerAdapter {

	public MyAdapter(FragmentManager fm) {super(fm);k}

	@Override
	public Fragment getItem(int i) {
		Fragment fragment=null;
		if(i==0)fragment=new Page("summary");
		if(i==1)fragment=new Page("arena");
		if(i==2)fragment=new Page("game");
		if(i==3)fragment=new Page("robot");
		if(i==4)fragment=new Page("tournament");
		if(i==5)fragment=new Page("glossary");
		return fragment;
	}

	@Override
	public int getCount() {
		return 6;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		@SuppressWarnings("unused")
		String title=new String();
		if(position==0)return "Summary";
		if(position==1)return "The Arena";
		if(position==2)return "The Game";
		if(position==3)return "The Robot";
		if(position==4)return "The Tournament";
		if(position==5)return "Glossary";
		return null;
	}
	
}
