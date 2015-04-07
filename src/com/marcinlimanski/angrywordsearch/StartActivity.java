package com.marcinlimanski.angrywordsearch;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends ActionBarActivity implements OnHTTPReg{
	public boolean unregisterFlag = false;
	public boolean logOutFlag = false;
	public boolean accInfoFlag = false;
	public boolean wordSearchFlag = false;
	public boolean getOldPuzzleflag = false;
	private String username = "";
	private String password = "";
	public static String globalDates = "";
	
	public static String selectedPuzzleDate = ""; 
	
	//For datepicker
	DateFormat formate=DateFormat.getDateInstance();
	Calendar calendar=Calendar.getInstance();
		
	//For sending the oldpuzel meta data
	private String choosenPuzzleDate = "";
	private String choosenPuzzleID = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		populateListView();
		registerClickCallback();
		
		
	}

	//Button hevent for puzzle
	public void btnPuzzleClicked(View v){
		if(v.getId() == R.id.btnPuzzle){
			Intent viewPussleIntent = new Intent(this, PuzzleActivity.class);
			startActivity(viewPussleIntent);
			
		}
	}
	
	//Options menu event 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}

	//Menu event
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		boolean handle = false;
		
		//using switch statment to catch the diffrent id
		switch(id){
			case R.id.option_logout:
				AlertDialog.Builder logoutBuilder = new AlertDialog.Builder(StartActivity.this);
				logoutBuilder.setMessage("Are you sure you want to log out?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//Extracting the user credentials from shared pref class
						username = SharedPreferencesWrapper.getFromPrefs(StartActivity.this, "username", "");
						password = SharedPreferencesWrapper.getFromPrefs(StartActivity.this, "password", "");
						
						SharedPreferencesWrapper.removFromPrefs(StartActivity.this, "username", username);
						SharedPreferencesWrapper.removFromPrefs(StartActivity.this, "password", password);
						finish();
						
					}
				});
				//Building dialog box 
				AlertDialog logoutDialog = logoutBuilder.create();
				logoutDialog.show();
				handle = true;
				break;
			
			case R.id.option_accinfo:
				accInfoFlag = true;

				//Extracting the user credentials from shared pref class
				username = SharedPreferencesWrapper.getFromPrefs(StartActivity.this, "username", "");
				password = SharedPreferencesWrapper.getFromPrefs(StartActivity.this, "password", "");
				
				//Creating a ASync thread
				//Using HttpClient to send the extracted data to register a user
				String url = "http://08309.net.dcs.hull.ac.uk/api/admin/details?username="+
				username+"&password="+password;
				RegHTTPAsync regUser =  new RegHTTPAsync(StartActivity.this);
				regUser.execute(url);
				handle = true;
				break;
				
			case R.id.option_unregister:
				AlertDialog.Builder unregisterBuilder = new AlertDialog.Builder(StartActivity.this);
				unregisterBuilder.setMessage("Are you sure you? All account data will be lost!").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					
					//Event lisiner for the dialog box
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//Setting check flag 
						unregisterFlag = true;
						
						//Extracting the user credentials from shared pref class
						username = SharedPreferencesWrapper.getFromPrefs(StartActivity.this, "username", "");
						password = SharedPreferencesWrapper.getFromPrefs(StartActivity.this, "password", "");
						
						//Debug
						//Toast.makeText(StartActivity.this, username + "," + password, Toast.LENGTH_LONG).show();
						
						//Creating a ASync thread
						//Using HttpClient to send the extracted data to register a user
						String url = "http://08309.net.dcs.hull.ac.uk/api/admin/unregister?username="+
						username+"&password="+password;
						RegHTTPAsync regUser =  new RegHTTPAsync(StartActivity.this);
						regUser.execute(url);
						
					}
				});
				//Building dialog box 
				AlertDialog unregisterDialog = unregisterBuilder.create();
				unregisterDialog.show();
				handle = true;
				break;
				
			default:
				handle = super.onOptionsItemSelected(item);
				
		}
		return handle;
	}
	
	//ListView for all puzzles 
	private void populateListView(){
		//Create list of items
		String[] listOfNames = {"marcin", "hannha", "tom"};	
		
		//build adapter 
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this, //some context for activity 
				R.layout.data_item, //Layout to use
				listOfNames); //Items to display
		
		
		//Configure list view
		ListView list = (ListView) findViewById(R.id.listView1);
		list.setAdapter(adapter);
	}
	
	//Callback method for ListView
	private void registerClickCallback(){
		ListView list = (ListView) findViewById(R.id.listView1);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
				//init view
				TextView textView = (TextView) viewClicked;
				
				//Building a string 
				String message = "You clicked #:" + position + ", which string is: " + textView.getText().toString();
				
				//Displaying the message 
				Toast.makeText(StartActivity.this, message, Toast.LENGTH_LONG).show();
				
			}
		});
	}
	
	//Clicked event for Today's puzzle
	public void btnTodaysPuzzleClicked(View v) throws IOException, JSONException{
		if(v.getId() == R.id.btnTodaysPuzzle){
			
			String test = SaveAndRestoreJSONPuzzle.RestoreJSONPuzzleandSolution(StartActivity.this, "2015-4-6");
			Log.i("New dates object: ", test);
			
			
		}
	}
	
	//Clicked event for adding new puzzle
	public void btnAddPuzzleClicked(View v) throws JSONException, IOException{
		if(v.getId() == R.id.btnAddPuzzle){
			
			setDate();

		}
	}
	
	public void setDate(){
		new DatePickerDialog(StartActivity.this, datePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
	}
	
	//Datepicker Dialog
	DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, monthOfYear);
			calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			int updatedMonth = monthOfYear + 1;
			Log.i("Date: ",  year +"-"+updatedMonth+"-"+dayOfMonth);
			
			//Assigning the choosen puzzle date to be pased for puzle save 
			choosenPuzzleDate = year +"-"+updatedMonth+"-"+dayOfMonth;
			
			//Sending a request for the old puzzle object
			String url = "http://08309.net.dcs.hull.ac.uk/api/wordsearch/puzzle?date=" + year +"-"+updatedMonth+"-"+dayOfMonth;
			RegHTTPAsync getOldPuzzle =  new RegHTTPAsync(StartActivity.this);
			getOldPuzzle.execute(url);
			getOldPuzzleflag = true;
			
		}
	};
	
	//AsyncTask for HTTP client 
	@Override
	public void onTaskCompleted(String httpData) {
		if(unregisterFlag){
			if(httpData.contains("OK")){
				SharedPreferencesWrapper.removFromPrefs(this, "username", username);
				SharedPreferencesWrapper.removFromPrefs(this, "password", password);
				finish();
			}
			unregisterFlag = false;
		}
		else if(accInfoFlag){
			if(!httpData.equals("")){
				try {

					//JSON object with all data from httpData
					JSONObject jasonObject = new JSONObject(httpData);
					//Accessing the class attribute
					String name = jasonObject.get("FullName").toString();
					
					//Creatin a new data intent to pass to new activity
					Intent accInfoViewIntent = new Intent(this, AccountInfoActivity.class);
					accInfoViewIntent.putExtra("fullname", name.toString());
					startActivity(accInfoViewIntent);
					
					
					

					//Toast.makeText(this, name + ", " + surname, Toast.LENGTH_LONG).show();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				accInfoFlag = false;
			}
			
		}
		else if(getOldPuzzleflag){
			
			
			try{
				
				if(!httpData.contains("null")){
					//JSON object with all data from httpData
					//JSONObject jsonObject = new JSONObject(httpData);
					//JSONObject puzzleAndSolutions = jsonObject.getJSONObject("PuzzleAndSolution");
					//JSONObject puzzleObject = puzzleAndSolutions.getJSONObject("Puzzle");
					//choosenPuzzleID = puzzleObject.getString("Id");
					//String puzzleID = PuzzleObject.getString("Id").toString();
					//Log.i("Puzzle ID: ", choosenPuzzleID.toString());
					
					//Saving the puzzleAndSolution
					try {
						SaveAndRestoreJSONPuzzle.SaveJSONPuzzleAndSolution(StartActivity.this, httpData, choosenPuzzleDate);
					} catch (IOException e) {
						
						e.printStackTrace();
					}
				}
				else{
					//If no puzzle is found then a message is displayed
					Toast.makeText(StartActivity.this, "No puzzle avaliaable for this day yet!", Toast.LENGTH_SHORT).show();
				}
				
				
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
			choosenPuzzleID = "";
			choosenPuzzleDate = "";
			
			getOldPuzzleflag = false;
		}
			
		
	}
}
