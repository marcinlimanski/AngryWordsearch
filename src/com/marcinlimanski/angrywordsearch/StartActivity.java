package com.marcinlimanski.angrywordsearch;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
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
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
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
	public boolean getTodaysPuzzleflag=false;
	public boolean assignTodaysPuzzle = false;
	
	
	
	private String username = "";
	private String password = "";
	public static String globalDates = "";
	
	public static String puzzleName = "";
	
	public static String selectedPuzzleDate = ""; 
	
	//For datepicker
	DateFormat formate=DateFormat.getDateInstance();
	Calendar calendar=Calendar.getInstance();
	
	int yearNow = calendar.get(Calendar.YEAR);
	int mNow = calendar.get(Calendar.MONTH);
	int monthNow = mNow +1;
	int dayNow = calendar.get(Calendar.DAY_OF_MONTH);
		
	//For sending the oldpuzel meta data
	private String choosenPuzzleDate = "";
	private String choosenPuzzleID = "";
	
	//Global flag for setting the puzzle format type
	//False - normal format, True - array format 
	public static boolean puzzleFormatFlag = false;
	
	private String tempPuzzle = "";
	private String tempSolution = "";
	String[] listOfDates = {""};
	
	ArrayList<String> puzzleDatesArray;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		populateListView();
		registerClickCallback();
		
		//Getting user pass to use with bellow code
		username = SharedPreferencesWrapper.getFromPrefs(StartActivity.this, "username", "");
		password = SharedPreferencesWrapper.getFromPrefs(StartActivity.this, "password", "");
		
		//Assigning todays puzzle for latter user
		String url = "http://08309.net.dcs.hull.ac.uk/api/wordsearch/current?username="+ username+ "&password=" +password;
		RegHTTPAsync getTodaysPuzzle =  new RegHTTPAsync(StartActivity.this);
		getTodaysPuzzle.execute(url);
		assignTodaysPuzzle = true;
		
		Log.i("On create global date", globalDates);
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
		puzzleDatesArray = new ArrayList<String>();
		
		//Accessing the list of dates
		try {
			String DatesObject = SaveAndRestoreJSONPuzzle.RestoreJSONSates(StartActivity.this);
			JSONObject jasonObject = new JSONObject(DatesObject); 
			JSONArray DatesArray = jasonObject.getJSONArray("PuzzleDates");
			
			for(int i =0; i<DatesArray.length(); i++){
				JSONObject entryArray = DatesArray.getJSONObject(i);
				Log.i("Date: ", entryArray.getString("date").toString());
				puzzleDatesArray.add(entryArray.getString("date").toString());

			}

		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		//Remove the test puzzle
		puzzleDatesArray.remove(0);
		//build adapter 
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this, //some context for activity 
				R.layout.data_item, //Layout to use
				puzzleDatesArray); //Items to display
		
		
		//Configure list view
		ListView list = (ListView) findViewById(R.id.listView1);
		list.setAdapter(adapter);
		registerForContextMenu(list);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
		if(v.getId() == R.id.listView1){
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
			String[] menuItems = getResources().getStringArray(R.array.itemMenu);
			for (int i = 0; i<menuItems.length; i++) {
			      menu.add(Menu.NONE, i, i, menuItems[i]);
			}
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		int menuItemIndex = item.getItemId();
		String[] menuItems = getResources().getStringArray(R.array.itemMenu);
		String menuItemName = menuItems[menuItemIndex];
		String puzzleItemName = puzzleDatesArray.get(info.position);
		
		if(menuItemName.equals("Delete puzzle")){
			//Toast.makeText(this, "delete this puzzle: " + listItemName, Toast.LENGTH_SHORT).show();
			
			try{
				//Deleting the date from the dateObject 
				String DatesObject = SaveAndRestoreJSONPuzzle.RestoreJSONSates(StartActivity.this);
				JSONObject jasonObject = new JSONObject(DatesObject); 
				JSONArray DatesArray = jasonObject.getJSONArray("PuzzleDates");
				int pos =0;
				for(int i =0; i<DatesArray.length(); i++){
					JSONObject entryArray = DatesArray.getJSONObject(i);
					if(entryArray.getString("date").toString().equals(StartActivity.puzzleName)){
						pos = i;
					}
					
				}
				DatesArray.remove(pos);
				
				String newDateObject = jasonObject.toString();
				//Overriding the dates object wiht new file 
				SaveAndRestoreJSONPuzzle.SaveJSONObjectDates(StartActivity.this, newDateObject);
				
				//Deleting the Puzzle and solution
				File file = new File("/data/data/com.marcinlimanski.angrywordsearch/files/"+puzzleItemName+ "ArrayFormat"+".json");
				File file2 = new File("/data/data/com.marcinlimanski.angrywordsearch/files/"+puzzleItemName+ ".json");
				boolean deleted = file.delete();
				boolean deleted2 = file2.delete();
				if(deleted == false){
					if(deleted2 == false){
						
					}
					Log.i("Puzzle deleted", "Puzzle deleted");
				}
				else{
					Log.i("Puzzle deleted", "Puzzle deleted");
				}
				
				//Deleting the foundWords file
				File file3 = new File("/data/data/com.marcinlimanski.angrywordsearch/files/"+puzzleItemName+"WordsFound.json");
				boolean deleted3 = file.delete();
				if(deleted3== false){
					Log.i("FoundWords NOT deleted", "FoundWords NOT deleted");
				}
				else{
					Log.i("FoundWords deleted", "FoundWords deleted");
				}
				populateListView();
			}
			catch(JSONException e){
				e.printStackTrace();
			}
			catch(IOException e){
				e.printStackTrace();
			}
			
			
		}
		
		
		//Toast.makeText(this, "selected index is: " + listItemName, Toast.LENGTH_SHORT).show();
		
		return true;
		
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
				puzzleName = textView.getText().toString();
				String jsonPuzzleAndSolution = SaveAndRestoreJSONPuzzle.RestoreJSONPuzzleandSolution(StartActivity.this, puzzleName);
				
				//Check if the puzzle loaded correctly 
				//Log.i("Puzzle loade: ", jsonPuzzleAndSolution);
				
				//Load choosen puzzle 
				if(LoadPuzzle.InitPuzzle(jsonPuzzleAndSolution)){
					Intent viewPussleIntent = new Intent(StartActivity.this, PuzzleActivity.class);
					startActivity(viewPussleIntent);
				}
				else{
					Toast.makeText(StartActivity.this, "Sorry, there was a problem loading the puzzle", Toast.LENGTH_SHORT).show();
				}
				
				
				
				
			}
		});
	}
	
	//Clicked event for Today's puzzle
	public void btnTodaysPuzzleClicked(View v) throws IOException, JSONException{
		puzzleName = "";
		if(v.getId() == R.id.btnTodaysPuzzle){
			

			String todaysPuzzleDateNow = String.valueOf(yearNow) + "-" + String.valueOf(monthNow) + "-"+ String.valueOf(dayNow);
			//setting name for the found words file
			Log.i("Todas puzzle date 1: ", yearNow +" "+ monthNow + " " + dayNow);
			puzzleName = todaysPuzzleDateNow;
			if(!SaveAndRestoreJSONPuzzle.RestoreJSONPuzzleandSolution(StartActivity.this, puzzleName).equals("")){


				Log.i("Todas puzzle date: 2", puzzleName);
				//Loading the puzzle
				String jsonPuzzleAndSolution = SaveAndRestoreJSONPuzzle.RestoreJSONPuzzleandSolution(StartActivity.this, puzzleName);
				//Load choosen puzzle 
				if(LoadPuzzle.InitPuzzle(jsonPuzzleAndSolution)){
					Intent viewPussleIntent = new Intent(StartActivity.this, PuzzleActivity.class);
					startActivity(viewPussleIntent);
				}
				else{
					Toast.makeText(StartActivity.this, "Sorry, there was a problem loading the puzzle", Toast.LENGTH_SHORT).show();
				}
			}
			else{
				//String test1 = SaveAndRestoreJSONPuzzle.RestoreJSONSates(StartActivity.this);
				//Log.i("New dates object: ", test1);
				String url = "http://08309.net.dcs.hull.ac.uk/api/wordsearch/solution?id="+choosenPuzzleID;
				RegHTTPAsync getTodaysPuzzleSolution =  new RegHTTPAsync(StartActivity.this);
				getTodaysPuzzleSolution.execute(url);
				getTodaysPuzzleflag = true;
			}
			
			
			
			
			
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
	public void onTaskCompleted(String httpData) throws JSONException {
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
				
				if(!httpData.contains("null") && !httpData.equals("")){
					
					//Saving the puzzleAndSolution
					try {
						if(SaveAndRestoreJSONPuzzle.SaveJSONPuzzleAndSolution(StartActivity.this, httpData, choosenPuzzleDate)){
							//If the save of the file will be sucessful then a puzzle date reference will be added 
							Log.i("Dates Array ", globalDates);
							SaveAndRestoreJSONPuzzle.SaveJSONDates(choosenPuzzleDate, StartActivity.this);
							String test1 = SaveAndRestoreJSONPuzzle.RestoreJSONSates(StartActivity.this);
							populateListView();
							Log.i("Dates Array after updating", test1.toString());
						}
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
			
			getOldPuzzleflag = false;
		}
		else if(getTodaysPuzzleflag){
			try{	
				if(!httpData.contains("null")){
					int year = calendar.get(Calendar.YEAR);
					int m = calendar.get(Calendar.MONTH);
					int month = m +1;
					int day = calendar.get(Calendar.DAY_OF_MONTH);
					
					String todaysPuzzleDate = String.valueOf(year) + "-" + String.valueOf(month) + "-"+ String.valueOf(day);
					//setting name for the found words file
					puzzleName = todaysPuzzleDate;
					//Saving the puzzleAndSolution
					try {
						if(SaveAndRestoreJSONPuzzle.SaveJSONTodaysPuzzleAndSolution(StartActivity.this, tempPuzzle, httpData, todaysPuzzleDate)){
							//If the save of the file will be sucessful then a puzzle date reference will be added 
							SaveAndRestoreJSONPuzzle.SaveJSONDates(todaysPuzzleDate, StartActivity.this);
							populateListView();
							//Loading the puzzle
							String jsonPuzzleAndSolution = SaveAndRestoreJSONPuzzle.RestoreJSONPuzzleandSolution(StartActivity.this, puzzleName);
							//Load choosen puzzle 
							if(LoadPuzzle.InitPuzzle(jsonPuzzleAndSolution)){
								Intent viewPussleIntent = new Intent(StartActivity.this, PuzzleActivity.class);
								startActivity(viewPussleIntent);
							}
							else{
								Toast.makeText(StartActivity.this, "Sorry, there was a problem loading the puzzle", Toast.LENGTH_SHORT).show();
							}
							
						}
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
			
			getTodaysPuzzleflag = false;
		}
		else if(assignTodaysPuzzle){
			//Assigning temp puzzle so it can be joined with its solution 
			tempPuzzle = httpData;
			Log.i("Todays puzzle id:", httpData);
			//Extracting Puzzel id
			//JSON object with all data from httpData
			JSONObject jsonObject = new JSONObject(httpData);
			JSONObject puzzleObject = jsonObject.getJSONObject("Puzzle");
			choosenPuzzleID = puzzleObject.getString("Id");
			
			Log.i("Todays puzzle id:", choosenPuzzleID);
			assignTodaysPuzzle = false;
		}
		
	}
}
