package com.aquamorph.frcmanual;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.annotation.SuppressLint;
import android.util.Log;

public class JSON {
   private String html;
   private String version = "version";
   private String pageString = null;
   private String urlString = "http://frc-manual.usfirst.org/a/GetAllItems/ManualID=3";

   public volatile boolean parsingComplete = true;
   public JSON(String page){
      this.pageString = page;
   }
   public String getHTML(){
      return html;
   }
   public String getVersion(){
      return version;
   }

   @SuppressLint("NewApi")
   public void readAndParseJSON(String in) {
      try {
         JSONObject reader = new JSONObject(in);


         JSONObject head = reader.getJSONObject("data").getJSONObject("SubChapter").getJSONObject("3").getJSONObject("children").getJSONObject(pageString);
         html = head.getString("item_content_text");
         
         if(head.has("children")){
	         JSONObject children = head.getJSONObject("children");
	         JSONArray sub1 = new JSONArray(children.names().toString());
	         for(int i=sub1.length()-1;i>=0;i--){
	        	 JSONObject children2 = children.getJSONObject(Integer.toString(sub1.getInt(i)));
	        	 html = html + "<h2>" + children2.getString("secdisp")+ " " + children2.getString("item_name") + "</h2>";
	        	 html = html + children2.getString("item_content_text");
	        	 if(children2.has("children")){
		        	 JSONObject children3 = children2.getJSONObject("children");
		        	 JSONArray sub2 = new JSONArray(children3.names().toString());
		        	 html = html + sub2;
		        	 for(int j=sub2.length()-1;j>=0;j--){
		        		 JSONObject children4 = children3.getJSONObject((String) sub2.get(j));
		        		 html = html + "<h3>" + children4.getString("secdisp")+ " " + children4.getString("item_name") + "</h3>";
		        		 html = html + children4.getString("item_content_text");
		        	 }
	        	 }
	         }
         }
         
      
                  

 


         JSONObject main  = reader.getJSONObject("data");
         version = main.getString("LatestManualUpdate");

         parsingComplete = false;



        } catch (Exception e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
        }

   }
   public void fetchJSON(){
      Thread thread = new Thread(new Runnable(){
         @Override
         public void run() {
         try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
         InputStream stream = conn.getInputStream();

      String data = convertStreamToString(stream);

      readAndParseJSON(data);
         stream.close();

         } catch (Exception e) {
            e.printStackTrace();
         }
         }
      });

       thread.start(); 		
   }
   static String convertStreamToString(java.io.InputStream is) {
      java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
      return s.hasNext() ? s.next() : "";
   }
   
}