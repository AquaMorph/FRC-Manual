package com.aquamorph.frcmanual;

import java.io.FileOutputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceActivity;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;

@SuppressLint("NewApi")
public class Functions extends PreferenceActivity {
	
	public static void javascript(WebView view, String url) {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT){
			//KitKat and above
	        view.evaluateJavascript("var img = document.getElementsByTagName('img'); for (var i = 0; i < img.length; ++i)img[i].style.maxWidth='100%';",null);
	        view.evaluateJavascript("var img = document.getElementsByTagName('img'); for (var i = 0; i < img.length; ++i) {img[i].style.height='';} ",null);
	        view.evaluateJavascript("var img = document.getElementsByTagName('table'); for (var i = 0; i < img.length; ++i) {img[i].style.width='';} ",null);
	        view.evaluateJavascript("var img = document.getElementsByTagName('table'); for (var i = 0; i < img.length; ++i) {img[i].style.overflow='hidden';} ",null);
//	        view.evaluateJavascript("document.body.style.zoom = '200%'; ", null);
	        //view.evaluateJavascript("document.body.style.background = 'blue'; ", null);
		} else{
		    // Jellybean and below
			view.loadUrl("javascript:var con = document.getElementsByTagName('img'); for (var i = 0; i < con.length; ++i)con[i].style.maxWidth='100%';"
			        + "var img = document.getElementsByTagName('img'); for (var i = 0; i < img.length; ++i) {img[i].style.height='';} "
			        + "var img = document.getElementsByTagName('table'); for (var i = 0; i < img.length; ++i) {img[0].style.width='';} ");
		}
   }
	
	public static void fontSize(WebView view) {
	   if(MainActivity.getFontSize().equals("Large"))view.loadUrl("javascript:var img = document.body.style.fontSize='20px';");
	   else if(MainActivity.getFontSize().equals("Medium"))view.loadUrl("javascript:var img = document.body.style.fontSize='16px';");
	   else if(MainActivity.getFontSize().equals("Small"))view.loadUrl("javascript:var img = document.body.style.fontSize='12px';");
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
//       view.getSettings().setLoadWithOverviewMode(false);
       view.getSettings().setUseWideViewPort(false);
   }

   public static boolean isNetworkAvailable(Activity test) {
       ConnectivityManager connectivityManager = (ConnectivityManager) test.getSystemService(Context.CONNECTIVITY_SERVICE);
       NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
       return activeNetworkInfo != null;
   }

	public static void error(WebView view) {
		view.loadData("<h1>Please check your internet connection or update the cache</h1>", "text/html", "utf-8");
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
}