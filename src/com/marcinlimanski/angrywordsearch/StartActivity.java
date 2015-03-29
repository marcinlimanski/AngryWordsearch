package com.marcinlimanski.angrywordsearch;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
		populateListView();
		registerClickCallback();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
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
			case R.id.option_logout:
				handle = true;
				break;
			
			case R.id.option_accinfo:
				handle = true;
				break;
				
			case R.id.option_unregister:
				AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
				builder.setMessage("Are you sure you? All account data will be lost!");
				handle = true;
				break;
				
			default:
				handle = super.onOptionsItemSelected(item);
				
		}
		return handle;
	}
	
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
}
