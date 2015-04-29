package com.marcinlimanski.angrywordsearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class SolutionActivity extends Activity {

	TextView TVSolution;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_solution);
		
		
		TVSolution = (TextView)findViewById(R.id.textBoxSolution);
		TVSolution.setTextSize(18);
		TVSolution.setText("");
		
		Bundle data = getIntent().getExtras();
		if (data != null) {
			
			
			try {
				JSONObject jsonObject = new JSONObject(LoadPuzzle.playingPuzzleSolution);
				//Accessing the SolutionWords arraY
				JSONArray solutionWordsArray =  jsonObject.getJSONArray("SolutionWords");
				
				for(int i=0; i<solutionWordsArray.length(); i++){
					JSONObject wordItem = solutionWordsArray.getJSONObject(i);
					
					TVSolution.append("Word: "+wordItem.getString("Word") + ", " + " Row: "+wordItem.getString("Row") + ", " + "Column: "+wordItem.getString("Column"));
					TVSolution.append("\n");
				}
				
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.solution, menu);
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
