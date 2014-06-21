package com.aquamorph.frcmanual;

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

@SuppressLint("ValidFragment")
public class Page extends Fragment {

	String file;

	public Page(String url) {
		file = url;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View myFragmentView = inflater.inflate(R.layout.webview, container,
				false);

		final WebView webView = (WebView) myFragmentView
				.findViewById(R.id.webview1);

		Functions.webViewSettings(webView);
		Functions.zoom(webView);
		Functions.cache(webView, getActivity());

		webView.setWebViewClient(new MyWebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				// pd.dismiss();
				Functions.javascript(webView, url);
				Functions.fontSize(webView);
			}

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Functions.error(webView);
			}
		});

		webView.loadUrl("file:///data/data/com.aquamorph.frcmanual/files/"
				+ file);

		return myFragmentView;

	}

	// Redirects links to browser
	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(intent);
			return true;
		}
	}

}
