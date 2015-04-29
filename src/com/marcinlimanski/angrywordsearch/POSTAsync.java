package com.marcinlimanski.angrywordsearch;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import android.os.AsyncTask;
import android.util.Log;

public class POSTAsync extends AsyncTask<String, Integer, String> {
	private OnPOST listener;

	//Creating a constructor for this class and init the listener
	public POSTAsync(OnPOST listener){
		this.listener = listener;
	}
	
	//Overrdiding the methos allows us to do something when the Async task will be complete 
	@Override
	protected void onPostExecute(String httpData){
		try {
			listener.onTaskPostCompleted(httpData);;
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}
	
	@Override
	protected String doInBackground(String... params) {
		String url = params[0].toString();
		String data = params[1].toString();

		Log.i("Params[0]: ", url.toString());
		Log.i("Params[1]: ", data.toString());
		
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("content-type", "application/json");
		StringEntity stringEntity = null;
		try {
			stringEntity = new StringEntity(data);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		httpPost.setEntity(stringEntity);
		try {
			HttpResponse response = httpClient.execute(httpPost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
