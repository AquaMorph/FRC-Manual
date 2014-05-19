package com.aquamorph.frcmanual;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.annotation.SuppressLint;

public class CheckVersion {
   private String version = null;
   private String urlString = "http://frc-manual.usfirst.org/a/GetAllItems/ManualID=3";

   public volatile boolean parsingComplete = true;

   public String getVersion(){
      return version;
   }

   @SuppressLint("NewApi")
   public void readAndParseJSON(String in) {
      try {
         JSONObject reader = new JSONObject(in);

         JSONObject main  = reader.getJSONObject("data");
         if(main.has("LatestManualUpdate")){
        	 version = main.getString("LatestManualUpdate");
         }

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
      @SuppressWarnings("resource")
	java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
      return s.hasNext() ? s.next() : "";
   }
   
}