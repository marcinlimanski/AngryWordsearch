package com.marcinlimanski.angrywordsearch;

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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_puzzle);
		
		puzzleGridView = (com.marcinlimanski.angrywordsearch.PuzzleGridView) findViewById(R.id.gvPuzzle);
		puzzleGridView.setAdapter(new PuzzleGridAdapter(PuzzleActivity.this));
		puzzleGridView.setNumColumns(columns);
		puzzleGridView.invalidate();
		puzzleGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				
				if(letterSwitch){
					puzzleGridView.invalidate();
					clearPoints = false;
					startDrawFirstPoint = true;
					pointAArray[0] = view.getLeft();
					pointAArray[1] = view.getTop();
					puzzleGridView.invalidate();
					letterSwitch = false;
				}
				else{
					startDrawSecondPoint = true;
					pointBArray[0] = view.getLeft();
					pointBArray[1] = view.getTop();
					letterSwitch = true;
					puzzleGridView.invalidate();
					clearPoints= true;
				}
			
					
				
				
				
				//Toast.makeText(PuzzleActivity.this,"x: "+ xView + ", y: " + yView, Toast.LENGTH_LONG).show();
				/*
				//Getting the column and row of given cell
				int x = position % columns;
				int y = position / columns;
				int column = x + 1;
				int row = y + 1;
				
				//Extracting the index of a given cell 
				int index = x + y * columns;
				String item = puzzleGridView.getItemAtPosition(index).toString();
				
				view.setBackgroundColor(1);;
				
				for(int i=0; i<3; i++){
					int newX = x - i;
					int newY = y + i;
					int newIndex = newX + newY * columns; 
					String newItem = puzzleGridView.getItemAtPosition(newIndex).toString();
					Log.i("Choosen item is: ", newItem);
				}
				*/

			}
	    });
		
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
}
