package com.aquamorph.frcmanual;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;

@SuppressLint("NewApi")
public class Functions extends PreferenceActivity {
	
	
	
	static CheckVersion version;
	static JSON page;
	
	public static void javascript(WebView view, String url) {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT){
			//KitKat and above
			view.evaluateJavascript("document.getElementById('header_container').style.display='none';",null);
	        view.evaluateJavascript("document.getElementById('footer').style.display='none';",null);
	        view.evaluateJavascript("document.getElementsByClassName('ChapterTools RightPos')[0].style.visibility='hidden';",null);
	        view.evaluateJavascript("document.getElementsByClassName('pdfbutton')[0].style.visibility='hidden';",null);
	        view.evaluateJavascript("document.getElementsByClassName('showhide_button1')[0].style.visibility='hidden';",null);
	        view.evaluateJavascript("document.getElementsByClassName('colmid')[0].style.right='100%';",null);
	        view.evaluateJavascript("document.getElementsByClassName('colleft')[0].style.right='initial';",null);
	        view.evaluateJavascript("document.getElementsByClassName('col1')[0].style.width='98%';",null);
	        view.evaluateJavascript("var con = document.getElementsByClassName('col1')[0].style.top='-60px'; ", null);
	        view.evaluateJavascript("document.getElementsByClassName('colmask threecol')[0].style.right='2%';",null);
	        view.evaluateJavascript("document.getElementsByClassName('colmask threecol')[0].style.width='';",null);
	        view.evaluateJavascript("var img = document.getElementsByTagName('img'); for (var i = 0; i < img.length; ++i)img[i].style.maxWidth='100%';",null);
	        view.evaluateJavascript("var img = document.getElementsByTagName('img'); for (var i = 0; i < img.length; ++i) {img[i].style.height='';} ",null);
	        view.evaluateJavascript("var img = document.getElementsByTagName('table'); for (var i = 0; i < img.length; ++i) {img[i].style.width='';} ",null);
	        view.evaluateJavascript("document.getElementsByTagName('body')[0].style.zoom = '200%'; ", null);
		} else{
		    // Jellybean and below
			view.loadUrl("javascript:var con = document.getElementById('header_container').style.display='none';"
			        + "var con = document.getElementById('footer').style.display='none';"
			        + "var con = document.getElementsByClassName('ChapterTools RightPos')[0].style.visibility='hidden';"
			        + "var con = document.getElementsByClassName('showhide_button1')[0].style.visibility='hidden';"
			        + "var con = document.getElementsByClassName('colmid')[0].style.right='100%';"
			        + "var con = document.getElementsByClassName('colleft')[0].style.right='initial';"
			        + "var con = document.getElementsByClassName('col1')[0].style.width='98%';"
			        + "var con = document.getElementsByClassName('col1')[0].style.top='-50px'; "
			        + "var con = document.getElementsByClassName('colmask threecol')[0].style.right='2%';"
			        + "var con = document.getElementsByClassName('colmask threecol')[0].style.width='';"
			        + "var con = document.getElementsByTagName('img'); for (var i = 0; i < con.length; ++i)con[i].style.maxWidth='100%';"
			        + "var img = document.getElementsByTagName('img'); for (var i = 0; i < img.length; ++i) {img[i].style.height='';} "
			        + "var img = document.getElementsByTagName('table'); for (var i = 0; i < img.length; ++i) {img[0].style.width='';} ");
		}
   }
	
	public static void fontSize(WebView view) {
	   if(MainActivity.getFontSize().equals("Large"))view.loadUrl("javascript:var img = document.body.style.fontSize='16px';");
	   else if(MainActivity.getFontSize().equals("Medium"))view.loadUrl("javascript:var img = document.body.style.fontSize='';");
	   else if(MainActivity.getFontSize().equals("Small"))view.loadUrl("javascript:var img = document.body.style.fontSize='10px';");
   }

	public static void zoom(WebView view) {
       if (MainActivity.getEnableZoom()==true)view.getSettings().setBuiltInZoomControls(true);
       else view.getSettings().setBuiltInZoomControls(false);
   }

	public static void cache(WebView view, Activity test) {
		//view.getSettings().setCacheMode( WebSettings.LOAD_NO_CACHE );
		if (!isNetworkAvailable(test))view.getSettings().setCacheMode( WebSettings.LOAD_CACHE_ONLY );
		else {
			if(MainActivity.getUpdateCache()==true)view.getSettings().setCacheMode( WebSettings.LOAD_DEFAULT );
			else {
				if(MainActivity.getEnableCache()==true)view.getSettings().setCacheMode( WebSettings.LOAD_CACHE_ONLY );
				else view.getSettings().setCacheMode( WebSettings.LOAD_DEFAULT );
			}
		}
	}

   @SuppressLint({ "SetJavaScriptEnabled", "SdCardPath" })
   @SuppressWarnings("deprecation")
   public static void webViewSettings(WebView view) {
       view.getSettings().setJavaScriptEnabled(true);
       view.getSettings().setDomStorageEnabled(true);
       view.setVerticalScrollBarEnabled(false);
       view.setHorizontalScrollBarEnabled(false);
       view.getSettings().setLoadWithOverviewMode(true);
       view.getSettings().setUseWideViewPort(true);
       view.getSettings().setRenderPriority(RenderPriority.HIGH);
       view.getSettings().setAllowFileAccess(true);
       view.getSettings().setAppCacheEnabled(false);      
   }

   public static boolean isNetworkAvailable(Activity test) {
       ConnectivityManager connectivityManager = (ConnectivityManager) test.getSystemService(Context.CONNECTIVITY_SERVICE);
       NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
       return activeNetworkInfo != null;
   }

	public static void error(WebView view) {
		view.loadData("<h1>Please check your internet connection or update the cache</h1>", "text/html", "utf-8");
	}
	
	public static void checkUpdate(Context ctx, Activity act){
		if(isNetworkAvailable(act)==true){
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
			String version = getVersion();
	        if(version!=prefs.getString("version", null))update(ctx);
		}
	}
	
	public static void update(Context ctx){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		Editor editor = prefs.edit();
		//Update Version Number
        editor.putString("version", getVersion());
        editor.commit();
        //Summary Page
        saveFile("summary",getHTML("178"),ctx);
        saveFile("arena",getHTML("179"),ctx);
        saveFile("game",getHTML("180"),ctx);
        saveFile("robot",getHTML("181"),ctx);
        saveFile("tournament",getHTML("182"),ctx);
        saveFile("glossary",getHTML("183"),ctx);
	}
	
	//Parses HTML code for a page
	public static String getHTML(String page){
		JSON html = new JSON(page);
		html.fetchJSON();
        while(html.parsingComplete){
        	try{
        	      Thread.sleep(100);
        	   } catch (InterruptedException e){
        	      e.printStackTrace();
        	   }
        	   continue;
        }
        return html.getHTML();
	}
	
	//Saves string to a file
	public static void saveFile(String name, String data, Context ctx){
		FileOutputStream outputStream;
        try {
          outputStream = ctx.openFileOutput(name, Context.MODE_PRIVATE);
          outputStream.write(data.getBytes());
          outputStream.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
	}
	
	public static String getVersion(){
		version = new CheckVersion();
        version.fetchJSON();
        while(version.parsingComplete){
        	try{
        	      Thread.sleep(100);
        	   } catch (InterruptedException e){
        	      e.printStackTrace();
        	   }
        	   continue;
        }
        return version.getVersion();
	}
	   

}