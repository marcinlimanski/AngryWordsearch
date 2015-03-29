package com.marcinlimanski.angrywordsearch;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class LogActivity extends ActionBarActivity implements OnHTTPReg{
	private String userName = "";
	private String password = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.log, menu);
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
	
	public void btnLogUserClicked(View v){
		if(v.getId() == R.id.btnLogUser){
			//Extracting the user info from textboxes 
			EditText textUserName = (EditText)this.findViewById(R.id.tbUserNameLog);
			userName = textUserName.getText().toString();
			
			EditText textPassword = (EditText)this.findViewById(R.id.tbPasswordLog);
			password = textPassword.getText().toString();
			
			//checking if the calues are not empty
			if(!userName.equals("") && !password.equals("")){
				
				String url = "http://08309.net.dcs.hull.ac.uk/api/admin/details?username=" + userName +"&password="+password;
				
				//Using HttpClient to send the extracted data to register a user
				RegHTTPAsync logUser =  new RegHTTPAsync(this);
				logUser.execute(url);
			}
		}
	}

	@Override
	public void onTaskCompleted(String httpData) {
		Log.i("Async task completed: ", httpData.toString());
		if(httpData.contains("FullName")){
			//Saving credentials to sharedPref
			SharedPreferencesWrapper.saveToPrefs(this, "username", userName);
			SharedPreferencesWrapper.saveToPrefs(this, "password", password);
			
			Intent startViewIntent = new Intent(this, StartActivity.class);
			startActivity(startViewIntent);
		}
		
	}
}
