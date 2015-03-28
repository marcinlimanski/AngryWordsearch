package com.marcinlimanski.angrywordsearch;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends ActionBarActivity implements OnHTTPReg{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	//Pre generated menu 
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
	
	public void btnRegClicked(View v){
		if(v.getId() == R.id.btnReg){
			//Extracting the user info from textboxes 
			EditText textName = (EditText)this.findViewById(R.id.tbFirstNameReg);
			String nameReg = textName.getText().toString();
			
			EditText textSurname = (EditText)this.findViewById(R.id.tbSurnameReg);
			String surnameReg = textSurname.getText().toString();
			
			EditText textUserName = (EditText)this.findViewById(R.id.tbUsernameReg);
			String usernameReg = textUserName.getText().toString();
			
			EditText textPassword = (EditText)this.findViewById(R.id.tbPasswordReg);
			String passwordReg = textPassword.getText().toString();
			
			//Debug to see if user information is extracted
			Log.i("User Info: ", nameReg + ", " + surnameReg + ", " + usernameReg + ", " + passwordReg);
			
			if(!nameReg.equals("") && !surnameReg.equals("") && !usernameReg.equals("") && !passwordReg.equals("")){
				//Constructing a get request 
				String url  = "http://08309.net.dcs.hull.ac.uk/api/admin/register?"
						+ "firstname="+ nameReg +
						"&Surname="+ surnameReg +
						"&username="+ usernameReg+
						"&password="+ passwordReg;
				
				//Using HttpClient to send the extracted data to register a user
				RegHTTPAsync regUser =  new RegHTTPAsync(this);
				regUser.execute(url);
			}
			else{
				Log.i("", "no values ");
			}
		}
	}
	
	public void btnLogInMainClicked(View v){
		if(v.getId() == R.id.btnLogInMain){
			Intent logViewIntent = new Intent(this, LogActivity.class);
			startActivity(logViewIntent);
		}
	}

	@Override
	public void onTaskCompleted(String httpData) {
		Log.i("onTaskComplete: ", "done");
		Log.i("Server Response: ", httpData.toString());
		if(httpData.contains("OK")){
			Intent startViewIntent = new Intent(this, StartActivity.class);
			startActivity(startViewIntent);
		}
		
		
	}
	
}
