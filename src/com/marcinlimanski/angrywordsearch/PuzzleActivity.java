package com.marcinlimanski.angrywordsearch;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class PuzzleActivity extends ActionBarActivity implements OnPOST{
	PuzzleGridView puzzleGridView;
	int columns = LoadPuzzle.gridNumberOfColumns;
	public static float startDrawX;
	public static float startDrawY;
	public static float endDrawX = 0;
	public static float endDrawY = 0;
	private boolean letterSwitch = true;
	public static float[] pointAArray = new float[2];
	public static float[] pointBArray = new float[2];
	boolean submissionCanBeMadeFlag = false;
	
	//For datepicker
	private DateFormat formate=DateFormat.getDateInstance();
	private Calendar calendar=Calendar.getInstance();
	
	private int yearNow = calendar.get(Calendar.YEAR);
	private int mNow = calendar.get(Calendar.MONTH);
	private int monthNow = mNow +1;
	private int dayNow = calendar.get(Calendar.DAY_OF_MONTH);
	
	
	private int tempPos = 0;
	public static boolean startDrawFirstPoint = false;
	public static boolean startDrawSecondPoint = false;
	public static boolean clearPoints = false;
	int posA = 0;
	int posB = 0;
	
	//StringLits for updated  words to find
	private ArrayList<String> updatedWordsToFindArray = new ArrayList<String>();
	
	//Getting the column and row of given cell
	int x ;
	int y;
	int column;
	int numRows;
	int row;
	
	//Getting the column and row of given cell
	int x2;
	int y2;
	int column2;
	int numRows2;
	int row2;
	
	TextView TVWordsToFind;
	
	//Handeling found words 
	String tempWordFound = "";
	public static String jsonFoundWordsObject = "";

	public void UpdateTVWordsToFind(){
		String FoundWordsObjectString = SaveAndRestoreJSONPuzzle.RestoreFoundWords(PuzzleActivity.this, StartActivity.puzzleName);
		updatedWordsToFindArray.clear();
		
		//StringList array of all words to find 
		for (String temp : LoadPuzzle.wordsToFindArray){
			//Log.i("Word to find: ", temp);
			if(!FoundWordsObjectString.contains(temp)){
				updatedWordsToFindArray.add(temp);
			}
		}
		
	
		for (String temp : updatedWordsToFindArray){
			Log.i("Update words to find:", temp);
		}
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Updating the view by seeing what puzzles have been discovered
		UpdateTVWordsToFind();
		
		setContentView(R.layout.activity_puzzle);
		TVWordsToFind = (TextView)findViewById(R.id.tvWordsToSearch);
		TVWordsToFind.setTextSize(12);
		TVWordsToFind.setText("");
		
		//Updating the view by seeing what puzzles have been discovered
		UpdateTVWordsToFind();
		
		String editWords ="";
		for (String tempWord : updatedWordsToFindArray){
			editWords = editWords + ", " + tempWord;
		}
		
		if(editWords.equals("")){
			TVWordsToFind.setText("You have completed this puzzle!, submit your score using the options");
			submissionCanBeMadeFlag = true;
			
		}
		else{
			TVWordsToFind.setText(editWords.toString().substring(1));
		}
		

		pointAArray[0] = 0;
		pointAArray[1] = 0;
		pointBArray[0] = 0;
		pointBArray[1] = 0;
		
		//JSONObject of all found words
		jsonFoundWordsObject = SaveAndRestoreJSONPuzzle.RestoreFoundWords(PuzzleActivity.this, StartActivity.puzzleName);
		Log.i("FoundPuzzles for this:", jsonFoundWordsObject);
		
		puzzleGridView = (com.marcinlimanski.angrywordsearch.PuzzleGridView) findViewById(R.id.gvPuzzle);
		puzzleGridView.setAdapter(new PuzzleGridAdapter(PuzzleActivity.this));
		puzzleGridView.setNumColumns(columns);
		puzzleGridView.invalidate();
		puzzleGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				//This will give me the row and collumn of given cell 
				//Toast.makeText(PuzzleActivity.this, "Column: "+column + ", Row:" + row, Toast.LENGTH_SHORT).show();
				
				if(letterSwitch){
					startDrawFirstPoint = true;
					pointAArray[0] = view.getLeft();
					pointAArray[1] = view.getTop();
					puzzleGridView.invalidate();
					letterSwitch = false;
					
					//Extracting the column and row of givven cell 
					x = position % columns;
					y = position / columns;
					column = x;
					numRows = (int) Math.ceil(puzzleGridView.getCount()/columns) -1;
					row = (y - numRows) * -1;
					puzzleGridView.invalidate();
				}
				else{
					startDrawFirstPoint = false;
					puzzleGridView.invalidate();
					pointBArray[0] = view.getLeft();
					pointBArray[1] = view.getTop();
					letterSwitch = true;


					//Getting the column and row of given cell
					x2 = position % columns;
					y2 = position / columns;
					column2 = x2;
					numRows2 = (int) Math.ceil(puzzleGridView.getCount()/columns) -1;
					row2 = (y2 - numRows2) * -1;
					
					//Checking the col and row 
					Log.i("ROW and COl", "A: " + column + ", " + row + " B: " + column2 + ", " + row2);
					
					//Preform the check
					if(CheckWord(column, row, column2, row2)){
						Log.i("WOWOWOWOW", "Puzzle found");
						
						//Creating a json object to save the found words 
						try {
							if(jsonFoundWordsObject.equals("")){
								Log.i("Word array", tempWordFound + ", " + pointAArray[0] + ", " + pointAArray[1] + ", " +pointBArray[0] + ", " +pointBArray[1]);
								JSONObject jsonObject = new JSONObject();
								jsonObject.put("Word", tempWordFound);
								jsonObject.put("sX", pointAArray[0]);
								jsonObject.put("sY", pointAArray[1]);
								jsonObject.put("eX", pointBArray[0] );
								jsonObject.put("eY", pointBArray[1]);
								
								JSONArray jsonArray = new JSONArray();
								jsonArray.put(jsonObject);
								
								JSONObject JSONFoundWords = new JSONObject();
								JSONFoundWords.put("FoundWords", jsonArray);

								jsonFoundWordsObject = JSONFoundWords.toString(); 
								Log.i("jObject", JSONFoundWords.toString());
			
							}
							else{

								JSONObject jsonObject = new JSONObject();
								jsonObject.put("Word", tempWordFound);
								jsonObject.put("sX", pointAArray[0]);
								jsonObject.put("sY", pointAArray[1]);
								jsonObject.put("eX", pointBArray[0] );
								jsonObject.put("eY", pointBArray[1]);
								
								String checkWord="";
								JSONObject jsonMainObject = new JSONObject(jsonFoundWordsObject);
								JSONArray jsonArray = jsonMainObject.getJSONArray("FoundWords");
								//Checking if the array has this word already
								for (int i=0; i<jsonArray.length(); i++){
									JSONObject wordObject = jsonArray.getJSONObject(i);
									 checkWord = checkWord + wordObject.getString("Word").toString();
									
									
								}
								
								//if the constructed string does not contain temp word then add
								if(!checkWord.contains(tempWordFound)){
									jsonArray.put(jsonObject);
									
									jsonFoundWordsObject = jsonMainObject.toString();

									Log.i("jObject exists", jsonMainObject.toString());
			
								}
								else{
									Log.i("jObject exists", jsonMainObject.toString());
								}

								
							}
							
							
						} catch (JSONException e) {
							
							e.printStackTrace();
						}
						
						//Getting the column and row of given cell
						int x =0;
						int y =0;
						int column=0;
						int numRows=0;
						int row=0;
						
						//Getting the column and row of given cell
						int x2=0;
						int y2=0;
						int column2=0;
						int numRows2=0;
						int row2=0;
						
						//Saving the progress
						SaveAndRestoreJSONPuzzle.SaveWordsFound(PuzzleActivity.this, StartActivity.puzzleName, jsonFoundWordsObject);

						puzzleGridView.invalidate();
						
						String editWords ="";
						//Updating the view by seeing what puzzles have been discovered
						UpdateTVWordsToFind();
						
						
						for (String tempWord : updatedWordsToFindArray){
							editWords = editWords + ", " + tempWord;
						}
						
						//This is where you do the score send!!
						if(editWords.equals("")){
							TVWordsToFind.setText("You have completed this puzzle!, submit your score using the options");
							submissionCanBeMadeFlag = true;
						}
						else{
							TVWordsToFind.setText(editWords.toString().substring(1));
						}
						
					}
				}

				
				
				
				//Extracting the index of a given cell 
				//int index = x + y * columns;
				//String item = puzzleGridView.getItemAtPosition(index).toString();
				

				
				
				

			}
	    });
		
	}
	
	public boolean CheckWord(int columnOne, int rowOne, int columnTwo, int rowTwo){
		boolean result = false;
		tempWordFound = "";
		try {
			//Constructing the Json Solution object
			JSONObject jsonObject = new JSONObject(LoadPuzzle.playingPuzzleSolution);
			//Accessing the SolutionWords arraY
			JSONArray solutionWordsArray =  jsonObject.getJSONArray("SolutionWords");
			//Log.i("Solution words", )

			
			//Looping through the solutionWord array
			for (int i =0; i<solutionWordsArray.length(); i++){
				JSONObject wordSolution = solutionWordsArray.getJSONObject(i);
				int direction = 0;
				int Column = 0;
				int Row = 0;
				int wordLength = 0;
				
				direction = wordSolution.getInt("Direction");
				Column = wordSolution.getInt("Column");
				Row = wordSolution.getInt("Row");
				wordLength = wordSolution.getString("Word").length();
				
				//Switch for diffrent directions, will return the Column and Row of given letter
				switch(direction){
					case 0:
						//Coords of first letter
						int tempColumOneD0 = Column;
						int tempRowOneD0 =Row;
						//Coords of second letter
						int tempColumTwoD0 = Column - (wordLength -1);
						int tempRowTwoD0 = Row - (wordLength -1);
						Log.i("Direction 0", wordSolution.getString("Word").toString() +" : "+tempColumOneD0 + ", " + tempRowOneD0 + ", " + tempColumTwoD0 + ", " + tempRowTwoD0);
						
						if(tempColumOneD0 == columnOne && tempRowOneD0 == rowOne && tempColumTwoD0 == columnTwo && tempRowTwoD0 == rowTwo){
							Log.i("Puzzle found!", "Puzzle found");
							tempWordFound = wordSolution.getString("Word").toString();
							result = true; 
						}
						//Checking diffrent direction
						else if(tempColumOneD0 == columnTwo && tempRowOneD0 == rowTwo && tempColumTwoD0 == columnOne && tempRowTwoD0 == rowOne){
							Log.i("Puzzle found!", "Puzzle found");
							tempWordFound = wordSolution.getString("Word").toString();
							result = true; 
						}
						break;
						
					case 1:
						//Coords of first letter
						int tempColumOneD1 = Column;
						int tempRowOneD1 =Row;
						//Coords of second letter
						int tempColumTwoD1 = Column;
						int tempRowTwoD1 = Row - (wordLength -1);
						Log.i("Direction 0", wordSolution.getString("Word").toString() +" : "+tempColumOneD1 + ", " + tempRowOneD1 + ", " + tempColumTwoD1 + ", " + tempRowTwoD1);
						
						if(tempColumOneD1 == columnOne && tempRowOneD1 == rowOne && tempColumTwoD1 == columnTwo && tempRowTwoD1 == rowTwo){
							Log.i("Puzzle found!", "Puzzle found");
							tempWordFound = wordSolution.getString("Word").toString();
							result = true; 
						}
						//Checking diffrent direction
						else if(tempColumOneD1 == columnTwo && tempRowOneD1 == rowTwo && tempColumTwoD1 == columnOne && tempRowTwoD1 == rowOne){
							Log.i("Puzzle found!", "Puzzle found");
							tempWordFound = wordSolution.getString("Word").toString();
							result = true; 
						}
						break;
						
					case 2: 
						//Coords of first letter
						int tempColumOneD2 = Column;
						int tempRowOneD2 =Row;
						//Coords of second letter
						int tempColumTwoD2 = Column + (wordLength -1);
						int tempRowTwoD2 = Row - (wordLength -1);
						Log.i("Direction 2", wordSolution.getString("Word").toString() +" : "+tempColumOneD2 + ", " + tempRowOneD2 + ", " + tempColumTwoD2 + ", " + tempRowTwoD2);
						
						if(tempColumOneD2 == columnOne && tempRowOneD2 == rowOne && tempColumTwoD2 == columnTwo && tempRowTwoD2 == rowTwo){
							Log.i("Puzzle found!", "Puzzle found");
							tempWordFound = wordSolution.getString("Word").toString();
							result = true; 
						}
						//Checking diffrent direction
						else if(tempColumOneD2 == columnTwo && tempRowOneD2 == rowTwo && tempColumTwoD2 == columnOne && tempRowTwoD2 == rowOne){
							Log.i("Puzzle found!", "Puzzle found");
							tempWordFound = wordSolution.getString("Word").toString();
							result = true; 
						}
						break;
						
					case 3: 
						int tempColumOneD3 = Column;
						int tempRowOneD3 =Row;
						//Coords of second letter
						int tempColumTwoD3 = Column - (wordLength -1);
						int tempRowTwoD3 = Row;
						Log.i("Direction 3", wordSolution.getString("Word").toString() +" : "+tempColumOneD3 + ", " + tempRowOneD3 + ", " + tempColumTwoD3 + ", " + tempRowTwoD3);
						
						if(tempColumOneD3 == columnOne && tempRowOneD3 == rowOne && tempColumTwoD3 == columnTwo && tempRowTwoD3 == rowTwo){
							Log.i("Puzzle found!", "Puzzle found");
							tempWordFound = wordSolution.getString("Word").toString();
							result = true; 
						}
						//Checking diffrent direction
						else if(tempColumOneD3 == columnTwo && tempRowOneD3 == rowTwo && tempColumTwoD3 == columnOne && tempRowTwoD3 == rowOne){
							Log.i("Puzzle found!", "Puzzle found");
							tempWordFound = wordSolution.getString("Word").toString();
							result = true; 
						}
						break;
						
					case 4: 
						int tempColumOneD4 = Column;
						int tempRowOneD4 =Row;
						//Coords of second letter
						int tempColumTwoD4 = Column + (wordLength - 1);
						int tempRowTwoD4 = Row;
						Log.i("Direction 4", wordSolution.getString("Word").toString() +" : "+tempColumOneD4+ ", " + tempRowOneD4 + ", " + tempColumTwoD4 + ", " + tempRowTwoD4);
						
						if(tempColumOneD4 == columnOne && tempRowOneD4 == rowOne && tempColumTwoD4 == columnTwo && tempRowTwoD4 == rowTwo){
							Log.i("Puzzle found!", "Puzzle found");
							tempWordFound = wordSolution.getString("Word").toString();
							result = true; 
						}
						//Checking diffrent direction
						else if(tempColumOneD4 == columnTwo && tempRowOneD4 == rowTwo && tempColumTwoD4 == columnOne && tempRowTwoD4 == rowOne){
							Log.i("Puzzle found!", "Puzzle found");
							tempWordFound = wordSolution.getString("Word").toString();
							result = true; 
						}
						break;
						
					case 5: 
						//Coords of first letter
						int tempColumOneD5 = Column;
						int tempRowOneD5 =Row;
						//Coords of second letter
						int tempColumTwoD5 = Column - (wordLength -1);
						int tempRowTwoD5 = Row + (wordLength -1);
						Log.i("Direction 5", wordSolution.getString("Word").toString() +" : "+tempColumOneD5 + ", " + tempRowOneD5 + ", " + tempColumTwoD5 + ", " + tempRowTwoD5);
						
						if(tempColumOneD5 == columnOne && tempRowOneD5 == rowOne && tempColumTwoD5 == columnTwo && tempRowTwoD5 == rowTwo){
							Log.i("Puzzle found!", "Puzzle found");
							tempWordFound = wordSolution.getString("Word").toString();
							result = true; 
						}
						//Checking diffrent direction
						else if(tempColumOneD5 == columnTwo && tempRowOneD5 == rowTwo && tempColumTwoD5 == columnOne && tempRowTwoD5 == rowOne){
							Log.i("Puzzle found!", "Puzzle found");
							tempWordFound = wordSolution.getString("Word").toString();
							result = true; 
						}
						break;
						
					case 6: 
						//Coords of first letter
						int tempColumOneD6 = Column;
						int tempRowOneD6 =Row;
						//Coords of second letter
						int tempColumTwoD6 = Column;
						int tempRowTwoD6 = Row + wordLength -1;
						Log.i("Direction 6", wordSolution.getString("Word").toString() +" : "+tempColumOneD6 + ", " + tempRowOneD6 + ", " + tempColumTwoD6 + ", " + tempRowTwoD6);
						
						if(tempColumOneD6 == columnOne && tempRowOneD6 == rowOne && tempColumTwoD6 == columnTwo && tempRowTwoD6 == rowTwo){
							Log.i("Puzzle found!", "Puzzle found");
							tempWordFound = wordSolution.getString("Word").toString();
							result = true; 
						}
						//Checking diffrent direction
						else if(tempColumOneD6 == columnTwo && tempRowOneD6 == rowTwo && tempColumTwoD6 == columnOne && tempRowTwoD6 == rowOne){
							Log.i("Puzzle found!", "Puzzle found");
							tempWordFound = wordSolution.getString("Word").toString();
							result = true; 
						}
						break;
					case 7:
						//Coords of first letter
						int tempColumOneD7 = Column;
						int tempRowOneD7 =Row;
						//Coords of second letter
						int tempColumTwoD7 = Column + (wordLength -1);
						int tempRowTwoD7 = Row + (wordLength -1);
						Log.i("Direction 7", wordSolution.getString("Word").toString() +" : "+tempColumOneD7 + ", " + tempRowOneD7 + ", " + tempColumTwoD7 + ", " + tempRowTwoD7);
						
						if(tempColumOneD7 == columnOne && tempRowOneD7 == rowOne && tempColumTwoD7 == columnTwo && tempRowTwoD7 == rowTwo){
							Log.i("Puzzle found!", "Puzzle found");
							tempWordFound = wordSolution.getString("Word").toString();
							result = true; 
						}
						//Checking diffrent direction
						else if(tempColumOneD7 == columnTwo && tempRowOneD7 == rowTwo && tempColumTwoD7 == columnOne && tempRowTwoD7 == rowOne){
							Log.i("Puzzle found!", "Puzzle found");
							tempWordFound = wordSolution.getString("Word").toString();
							result = true; 
						}
						break;
				
					default: 
						break;
				}
				
				
				
			}
			
			
			
			
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		return result;
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.puzzle, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		boolean handle = false;
		
		//using switch statment to catch the diffrent id
		switch(id){
			case R.id.option_getsolution:
				String todaysPuzzleDateNow = String.valueOf(yearNow) + "-" + String.valueOf(monthNow) + "-"+ String.valueOf(dayNow);
				if(StartActivity.puzzleName.equals(todaysPuzzleDateNow))
				{
					Toast.makeText(PuzzleActivity.this, "Solution for todays puzzle can't be viewd!", Toast.LENGTH_SHORT).show();
				}
				else{
					try{
						//Constructing the Json Solution object
						JSONObject jsonObject = new JSONObject(LoadPuzzle.playingPuzzleSolution);
						//Accessing the SolutionWords arraY
						JSONArray solutionWordsArray =  jsonObject.getJSONArray("SolutionWords");
						String solutionArrayObject = solutionWordsArray.toString();
						Log.i("Solution: ", solutionWordsArray.toString());
						
						Intent solutionActivityIntent = new Intent(PuzzleActivity.this, SolutionActivity.class);
						solutionActivityIntent.putExtra("solutionArray", LoadPuzzle.playingPuzzleSolution);
						startActivity(solutionActivityIntent);
						
						
					}
					catch(JSONException e){
						e.printStackTrace();
					}
				
				}
					
				handle = true;
				break;
				
			case R.id.option_submitPuzzle:
				if(isInternetOn()){
					Log.i("Submit puzzle option", "active");
					if(submissionCanBeMadeFlag){
						//Getting user pass to use with bellow code
						String username = SharedPreferencesWrapper.getFromPrefs(PuzzleActivity.this, "username", "");
						String password = SharedPreferencesWrapper.getFromPrefs(PuzzleActivity.this, "password", "");
						
						TVWordsToFind.setText("You have completed this puzzle!!");
						String test= FormingSolutionSubmission.SubmitPuzzleSolution(LoadPuzzle.chosenPuzzleSolutionForSumbision, username, password);
						Log.i("submision object", test);
						
						//Assigning todays puzzle for latter user
						String[] params = {"http://08309.net.dcs.hull.ac.uk/api/wordsearch/submit", test};
						
						POSTAsync getTodaysPuzzle =  new POSTAsync(PuzzleActivity.this);
						getTodaysPuzzle.execute(params);
					}
					else{
						Toast.makeText(PuzzleActivity.this, "You need to complete the puzzle first", Toast.LENGTH_SHORT).show();
					}
				}
				else{
					Toast.makeText(PuzzleActivity.this, "You need to have internet connection tosubmit the score", Toast.LENGTH_SHORT).show();
				}
				
				handle = true;
				break;
			
			default:
				handle = super.onOptionsItemSelected(item);
				
		}
		return handle;
	}
	
	public final boolean isInternetOn() {
	     
	    // get Connectivity Manager object to check connection
	    ConnectivityManager connec =  
	                   (ConnectivityManager)getSystemService(StartActivity.CONNECTIVITY_SERVICE);
	     
	       // Check for network connections
	        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
	             connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
	             connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
	             connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
	            
	            
	            return true;
	             
	        } else if ( 
	          connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
	          connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {
	           
	            return false;
	        }
	      return false;
	    }
	
	@Override
	public void onBackPressed() {
		startDrawFirstPoint = false;
		startDrawSecondPoint = false;
		submissionCanBeMadeFlag = false;
		finish();
		
	}

	@Override
	public void onTaskPostCompleted(String httpData) throws JSONException {
		Toast.makeText(PuzzleActivity.this, "Puzzle solution submited", Toast.LENGTH_SHORT).show();
		
	}
}
