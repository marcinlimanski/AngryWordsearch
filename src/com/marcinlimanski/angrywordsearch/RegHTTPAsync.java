package com.marcinlimanski.angrywordsearch;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import android.os.AsyncTask;
import android.util.Log;

public class RegHTTPAsync extends AsyncTask<String, Integer, String> {

	private OnHTTPReg listener;
	
	//Creating a constructor for this class and init the listener
	public RegHTTPAsync(OnHTTPReg listener){
		this.listener = listener;
	}
	
	//Overrdiding the methos allows us to do something when the Async task will be complete 
	@Override
	protected void onPostExecute(String httpData){
		try {
			listener.onTaskCompleted(httpData);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	@SuppressWarnings("deprecation")
	@Override
	protected String doInBackground(String... params) {
		String url = params[0].toString();
		Log.i("Params[0]: ", url.toString());
		
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = client.execute(httpGet);
		    StatusLine statusLine = response.getStatusLine();
		    int statusCode = statusLine.getStatusCode();
		    if (statusCode == 200) {
		    	Log.i("Status code", "200");
		    	HttpEntity entity = response.getEntity();
		       	InputStream content = entity.getContent();
		       	BufferedReader reader = 
					new BufferedReader(new InputStreamReader(content));
		       	String line;
		       	while ((line = reader.readLine()) != null) {
		       	  builder.append(line);
		       	  //Log.i("HTTP: ", builder.append(line).toString());
		       	}
		   	}
		    else
		    {
		    	Log.i("Error: ", "400");
		    }
		}
		catch (Exception e){
			Log.i("getHttpData error: ", e.getMessage());
			return null;
		}
		String dataString =  builder.toString();
		
		
		return dataString;
	}

}
