package com.aquamorph.frcmanual;

import com.aquamorph.frcmanual.R;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Updates extends Fragment {
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {        
        super.onActivityCreated(savedInstanceState);
    }
	
	@SuppressLint("SetJavaScriptEnabled")
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View myFragmentView = inflater.inflate(R.layout.webview, container, false);
		
		final WebView webView = (WebView) myFragmentView.findViewById(R.id.webview1);
		String url = "http://frc-manual.usfirst.org/Updates/0";
		
		Functions.webViewSettings(webView);
		Functions.zoom(webView);
		Functions.cache(webView, getActivity());
        Functions.javascript(webView, url);
        webView.loadUrl(url);
        
        webView.setWebViewClient(new MyWebViewClient() {
         @Override
         public void onPageFinished(WebView view, String url)
         {
        	 Functions.javascript(webView, url);
        	 Functions.javascript(webView, url);
        	 Functions.javascript(webView, url);
         }
        });
        
        return myFragmentView;
		

	}
	

	
	
	private class MyWebViewClient extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        if (Uri.parse(url).getHost().equals("www.example.com")) {
	            // This is my web site, so do not override; let my WebView load the page
	            return false;
	        }
	        // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
	        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	        startActivity(intent);
	        return true;
	    }
	}

}
