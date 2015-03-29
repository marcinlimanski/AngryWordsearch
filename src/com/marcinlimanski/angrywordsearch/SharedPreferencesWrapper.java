package com.marcinlimanski.angrywordsearch;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesWrapper {
	//Method that allows us to write to shared preferences 
	public static void saveToPrefs(Context context, String key, String value){
		
		//Setting up shared preferences 
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		final SharedPreferences.Editor editor = prefs.edit();
		//Putting the JSON key and string value in to the editor
		editor.putString(key, value);
		//Commiting the change 
		editor.commit();
		
	}
	
	//Method that allows us to delete from shared preferences 
	public static void removFromPrefs(Context context, String key, String value){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		final SharedPreferences.Editor editor = prefs.edit();
		editor.remove(key);
		editor.commit();
		
	}
	
	//Method to retrive data from shared preferences 
	public static String getFromPrefs(Context context, String key, String defaultValue){
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		try{
			return sharedPrefs.getString(key, defaultValue);
		}
		catch(Exception e){
			e.printStackTrace();
			return defaultValue;
		}
		
	}
}
