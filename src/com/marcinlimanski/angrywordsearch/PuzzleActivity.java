package com.marcinlimanski.angrywordsearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class PuzzleActivity extends ActionBarActivity {
	PuzzleGridView puzzleGridView;
	int columns = LoadPuzzle.gridNumberOfColumns;
	public static float startDrawX;
	public static float startDrawY;
	public static float endDrawX = 0;
	public static float endDrawY = 0;
	private boolean letterSwitch = true;
	public static float[] pointAArray = new float[2];
	public static float[] pointBArray = new float[2];
	
	private int tempPos = 0;
	public static boolean startDrawFirstPoint = false;
	public static boolean startDrawSecondPoint = false;
	public static boolean clearPoints = false;
	int posA = 0;
	int posB = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_puzzle);
		pointAArray[0] = 0;
		pointAArray[1] = 0;
		pointBArray[0] = 0;
		pointBArray[1] = 0;
		puzzleGridView = (com.marcinlimanski.angrywordsearch.PuzzleGridView) findViewById(R.id.gvPuzzle);
		puzzleGridView.setAdapter(new PuzzleGridAdapter(PuzzleActivity.this));
		puzzleGridView.setNumColumns(columns);
		puzzleGridView.invalidate();
		puzzleGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				/*
				if(letterSwitch){
					puzzleGridView.invalidate();
					clearPoints = false;
					startDrawFirstPoint = true;
					pointAArray[0] = view.getLeft();
					pointAArray[1] = view.getTop();
					puzzleGridView.invalidate();
					letterSwitch = false;
					posA = position;
				}
				else{
					startDrawSecondPoint = true;
					pointBArray[0] = view.getLeft();
					pointBArray[1] = view.getTop();
					letterSwitch = true;
					puzzleGridView.invalidate();
					clearPoints= true;
					posB = position;
					//Toast.makeText(PuzzleActivity.this, "A: "+posA + ", B:" + posB, Toast.LENGTH_SHORT).show();
				}
				*/
			
				
				
				
				
				//Toast.makeText(PuzzleActivity.this,"x: "+ xView + ", y: " + yView, Toast.LENGTH_LONG).show();
				
				//Getting the column and row of given cell
				int x = position % columns;
				int y = position / columns;
				int column = x;
				int numRows = (int) Math.ceil(puzzleGridView.getCount()/columns) -1;
				int row = y - numRows;
				
				
				Toast.makeText(PuzzleActivity.this, "Column: "+column + ", Row:" + row, Toast.LENGTH_SHORT).show();
				
				//Extracting the index of a given cell 
				int index = x + y * columns;
				String item = puzzleGridView.getItemAtPosition(index).toString();
				

				
				
				

			}
	    });
		
	}
	
	public ArrayList<Integer> CheckWord(){
		ArrayList<Integer> result = new ArrayList<Integer>();
		try {
			//Constructing the Json Solution object
			JSONObject jsonObject = new JSONObject(LoadPuzzle.playingPuzzleSolution);
			//Accessing the SolutionWords arraY
			JSONArray solutionWordsArray =  jsonObject.getJSONArray("SolutionWords");
			

			
			//Looping through the solutionWord array
			for (int i =0; i<solutionWordsArray.length(); i++){
				JSONObject wordSolution = solutionWordsArray.getJSONObject(i);
				
				int direction = wordSolution.getInt("Direction");
				int Column = wordSolution.getInt("Column");
				int Row = wordSolution.getInt("Row");
				int wordLength = wordSolution.getString("Word").length();
				
				//Switch for diffrent directions, will return the Column and Row of given letter
				switch(direction){
					case 0:
						break;
						
					case 1:
						//Coords of first letter
						result.add(Column);
						result.add(Row);
						//Coords of second letter
						result.add(Column - wordLength);
						result.add(Row);
						break;
						
					case 2: 
						break;
						
					case 3: 
						break;
						
					case 4: 
						break;
						
					case 5: 
						//Coords of first letter
						result.add(Column);
						result.add(Row);
						//Coords of second letter
						result.add(Column - wordLength);
						result.add(Row - wordLength);
						break;
						
					case 6: 
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
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		startDrawFirstPoint = false;
		startDrawSecondPoint = false;
		finish();
	}
}
