package com.marcinlimanski.angrywordsearch;

import org.apache.http.client.HttpClient;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class RegActivity extends Activity {
	
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
			
			
		}
	}
}
