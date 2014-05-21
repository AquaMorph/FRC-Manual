package com.aquamorph.frcmanual;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import android.annotation.SuppressLint;

public class JSON {
   private String html;
   private String pageString = null;
   private String urlString = "http://frc-manual.usfirst.org/a/GetAllItems/ManualID=3";

   public volatile boolean parsingComplete = true;
   public JSON(String page){
      this.pageString = page;
   }
   public String getHTML(){
      return html;
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
	         List<String> reorder1 = new ArrayList<String>();
	         for(int i=sub1.length()-1;i>=0;i--){
	        	 reorder1.add(sub1.getString(i));
	         }
	         Collections.sort(reorder1);
	         sub1 = new JSONArray(reorder1);
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