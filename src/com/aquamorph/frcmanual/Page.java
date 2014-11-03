package com.aquamorph.frcmanual;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Page extends Fragment {

    String file=null;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
        //selects tab contents
        Bundle bundle=getArguments();
        if(bundle.getInt("tab")==0)file="summary";
        if(bundle.getInt("tab")==1)file="arena";
        if(bundle.getInt("tab")==2)file="game";
        if(bundle.getInt("tab")==3)file="robot";
        if(bundle.getInt("tab")==4)file="tournament";
        if(bundle.getInt("tab")==5)file="glossary";

		View view = inflater.inflate(R.layout.webview, container,false);

		final WebView webView = (WebView) view.findViewById(R.id.webview);

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

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Functions.error(webView);
			}

		});

		webView.loadUrl("file:///data/data/com.aquamorph.frcmanual/files/" + file);

		return view;
	}

    //gets tab number for fragment
    public static Fragment newInstance(int i) {
        Page fragment = new Page();
        Bundle args = new Bundle();
        args.putInt("tab", i);
        fragment.setArguments(args);
        return fragment;
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
