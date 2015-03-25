package com.marcinlimanski.angrywordsearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class LogInActivity  extends Activity implements OnHTTPReg{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}
	
	public void btnLoginLogClicked(View v){
		if(v.getId() == R.id.btnLoginLog){
			
			//Extracting the user info from textboxes 
			EditText textUserName = (EditText)this.findViewById(R.id.tbUserNameLog);
			String userName = textUserName.getText().toString();
			
			EditText textPassword = (EditText)this.findViewById(R.id.tbPasswordLog);
			String password = textPassword.getText().toString();
			
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
			Intent startViewIntent = new Intent(this, StartActivity.class);
			startActivity(startViewIntent);
		}
		
	}
}
