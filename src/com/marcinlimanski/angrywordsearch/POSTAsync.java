package com.marcinlimanski.angrywordsearch;

import org.json.JSONException;

import android.os.AsyncTask;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		return null;
	}

}
