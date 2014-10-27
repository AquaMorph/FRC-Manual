package com.aquamorph.frcmanual;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JSON extends AsyncTask<String, Void, Boolean> {

    Context appContext;

    public JSON(Context context) {
        appContext = context;
    }

    Boolean updating = false;

    ProgressDialog dialog;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(appContext, "Checking for update", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Boolean doInBackground(String... urls) {
        try {
            HttpGet httppost = new HttpGet(urls[0]);
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(httppost);

            int status = response.getStatusLine().getStatusCode();

            if (status == 200) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(appContext);
                HttpEntity entity = response.getEntity();
                String data = EntityUtils.toString(entity);
                String version = null;
                JSONObject reader = new JSONObject(data);
                JSONObject main = reader.getJSONObject("data");
                if (main.has("LatestManualUpdate")) {
                    version = main.getString("LatestManualUpdate");
                }
                if (!version.equals(//"1")){
                        prefs.getString("version", null))) {
                    updating = true;
                    //Update Version Number
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("version", version);
                    editor.commit();
                    //Update HTML
                    Functions.saveFile("summary", html(reader, "178"), appContext);
                    Functions.saveFile("arena", html(reader, "179"), appContext);
                    Functions.saveFile("game", html(reader, "180"), appContext);
                    Functions.saveFile("robot", html(reader, "181"), appContext);
                    Functions.saveFile("tournament", html(reader, "182"), appContext);
                    Functions.saveFile("glossary", html(reader, "183"), appContext);
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
        if (result == false)
            Toast.makeText(appContext, "Unable to fetch data from server", Toast.LENGTH_LONG).show();
        if (updating == true) {
            Toast.makeText(appContext, "Reloading update", Toast.LENGTH_SHORT).show();
            MainActivity.reload(appContext);
        }

    }


    public String html(JSONObject reader, String page) throws JSONException {
        String html = "<html><body>";

        JSONObject head = reader.getJSONObject("data").getJSONObject("SubChapter").getJSONObject("3").getJSONObject("children").getJSONObject(page);
        html = html + head.getString("item_content_text");

        if (head.has("children")) {
            JSONObject children = head.getJSONObject("children");
            JSONArray sub1 = new JSONArray(children.names().toString());
            List<String> reorder1 = new ArrayList<String>();
            for (int i = sub1.length() - 1; i >= 0; i--) {
                reorder1.add(sub1.getString(i));
            }
            Collections.sort(reorder1);
            sub1 = new JSONArray(reorder1);
            if (page.equals("183")) {
                for (int i = sub1.length() - 1; i >= 0; i--) {
                    JSONObject children2 = children.getJSONObject(Integer.toString(sub1.getInt(i)));
                    html = html + "<h2>" + children2.getString("secdisp") + " " + children2.getString("item_name") + "</h2>";
                    html = html + children2.getString("item_content_text");
                    if (children2.has("children")) {
                        JSONObject children3 = children2.getJSONObject("children");
                        JSONArray sub2 = new JSONArray(children3.names().toString());
                        List<String> reorder2 = new ArrayList<String>();
                        for (int j = sub2.length() - 1; j >= 0; j--) {
                            reorder2.add(sub2.getString(j));
                        }
                        Collections.sort(reorder2);
                        sub2 = new JSONArray(reorder2);
                        for (int j = 0; j < sub2.length(); j++) {
                            JSONObject children4 = children3.getJSONObject((String) sub2.get(j));
                            html = html + "<h3>" + children4.getString("secdisp") + " " + children4.getString("item_name") + "</h3>";
                            html = html + children4.getString("item_content_text");
                            if (children4.has("children")) {
                                JSONObject children5 = children4.getJSONObject("children");
                                JSONArray sub3 = new JSONArray(children5.names().toString());
                                List<String> reorder3 = new ArrayList<String>();
                                for (int k = sub3.length() - 1; k >= 0; k--) {
                                    reorder3.add(sub3.getString(k));
                                }
                                Collections.sort(reorder3);
                                sub3 = new JSONArray(reorder3);
                                for (int k = 0; k < sub3.length(); k++) {
                                    JSONObject children6 = children5.getJSONObject((String) sub3.get(k));
                                    html = html + children6.getString("item_name");
                                    html = html + children6.getString("item_content_text");
                                }
                            }
                        }
                    }
                }
            } else {
                for (int i = 0; i < sub1.length(); i++) {
                    JSONObject children2 = children.getJSONObject(Integer.toString(sub1.getInt(i)));
                    html = html + "<h2>" + children2.getString("secdisp") + " " + children2.getString("item_name") + "</h2>";
                    html = html + children2.getString("item_content_text");
                    if (children2.has("children")) {
                        JSONObject children3 = children2.getJSONObject("children");
                        JSONArray sub2 = new JSONArray(children3.names().toString());
                        List<String> reorder2 = new ArrayList<String>();
                        for (int j = sub2.length() - 1; j >= 0; j--) {
                            reorder2.add(sub2.getString(j));
                        }
                        Collections.sort(reorder2);
                        sub2 = new JSONArray(reorder2);
                        for (int j = 0; j < sub2.length(); j++) {
                            JSONObject children4 = children3.getJSONObject((String) sub2.get(j));
                            html = html + "<h3>" + children4.getString("secdisp") + " " + children4.getString("item_name") + "</h3>";
                            html = html + children4.getString("item_content_text");
                            if (children4.has("children")) {
                                JSONObject children5 = children4.getJSONObject("children");
                                JSONArray sub3 = new JSONArray(children5.names().toString());
                                List<String> reorder3 = new ArrayList<String>();
                                for (int k = sub3.length() - 1; k >= 0; k--) {
                                    reorder3.add(sub3.getString(k));
                                }
                                Collections.sort(reorder3);
                                sub3 = new JSONArray(reorder3);
                                for (int k = 0; k < sub3.length(); k++) {
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