package com.marcinlimanski.angrywordsearch;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class RegActivity extends Activity implements OnHTTPReg{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reg);
	}
	
	//Event handlrer for Reg submit 
	public void btnRegUserClicked(View v){
		//Double checking if the button has been pressed
		if(v.getId() == R.id.btnRegUser){
			
			//Extracting the user info from textboxes 
			EditText textName = (EditText)this.findViewById(R.id.tbFirstNameReg);
			String nameReg = textName.getText().toString();
			
			EditText textSurname = (EditText)this.findViewById(R.id.tbSurnameReg);
			String surnameReg = textSurname.getText().toString();
			
			EditText textUserName = (EditText)this.findViewById(R.id.tbUserNameReg);
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
