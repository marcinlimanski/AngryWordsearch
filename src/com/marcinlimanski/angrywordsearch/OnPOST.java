package com.marcinlimanski.angrywordsearch;

import org.json.JSONException;

public interface OnPOST {
	void onTaskPostCompleted(String httpData) throws JSONException;
}
