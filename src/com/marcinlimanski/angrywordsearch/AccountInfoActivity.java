package com.marcinlimanski.angrywordsearch;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AccountInfoActivity extends ActionBarActivity implements OnHTTPReg{
	private String passwordConfirmChange = "";
	private String passwordChange = "";
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_info);
		
		//Setting up the TextView
		TextView fullNameEditText =(TextView)findViewById(R.id.tvFullName);
		//Assigning the textView with the data grabbed from the intent
		fullNameEditText.setText(getIntent().getExtras().getString("fullname"));
	}

	public void btnChangePasswordClicked(View v){
		if(v.getId() == R.id.btnChangePassword){
			//Grabing both password values
			EditText textPassword = (EditText)this.findViewById(R.id.tbPasswordChange);
			passwordChange = textPassword.getText().toString();
			
			EditText textPasswordConfirm = (EditText)this.findViewById(R.id.tbPasswordChangeConfirm);
			passwordConfirmChange = textPasswordConfirm.getText().toString();
			if(!passwordChange.equals("") && !passwordConfirmChange.equals("")){
				if (passwordChange.equals(passwordConfirmChange)){
					//Sending the http request to change the password
					AlertDialog.Builder passwordChangeBuilder = new AlertDialog.Builder(AccountInfoActivity.this);
					passwordChangeBuilder.setMessage("Are you sure you want to change your password?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//Extracting the user credentials from shared pref class
							String username = SharedPreferencesWrapper.getFromPrefs(AccountInfoActivity.this, "username", "");
							String password = SharedPreferencesWrapper.getFromPrefs(AccountInfoActivity.this, "password", "");
							
							//Debug
							//Toast.makeText(StartActivity.this, username + "," + password, Toast.LENGTH_LONG).show();
							
							//Creating a ASync thread
							//Using HttpClient to send the extracted data to register a user
							String url = "http://08309.net.dcs.hull.ac.uk/api/admin/change?username="+username+"&oldpassword="
							+password+"&newpassword="+passwordConfirmChange;
							RegHTTPAsync regUser =  new RegHTTPAsync(AccountInfoActivity.this);
							regUser.execute(url);
							
						}
					});
					AlertDialog passwordChangedDialog = passwordChangeBuilder.create();
					passwordChangedDialog.show();
				}
				else{
					AlertDialog.Builder wrongPasswordBuilder = new AlertDialog.Builder(AccountInfoActivity.this);
					wrongPasswordBuilder.setMessage("Please check if both passwords are the same!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					});
					AlertDialog wrongPasswordDialog = wrongPasswordBuilder.create();
					wrongPasswordDialog.show();
				}
			}else{
				AlertDialog.Builder noPasswordBuilder = new AlertDialog.Builder(AccountInfoActivity.this);
				noPasswordBuilder.setMessage("Please enter password!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				});
				AlertDialog wrongPasswordDialog = noPasswordBuilder.create();
				wrongPasswordDialog.show();
			}
			
			
		}
	}

	@Override
	public void onTaskCompleted(String httpData) {
		if(httpData.contains("OK")){
			//Overrdiing the password in shared pref
			SharedPreferencesWrapper.saveToPrefs(this, "password", passwordConfirmChange);
			finish();
		}
	}
	
	
	
	
}
