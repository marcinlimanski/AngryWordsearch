package com.marcinlimanski.angrywordsearch;
import java.io.IOException;

import org.json.JSONException;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.Toast;

public class DateSettings implements DatePickerDialog.OnDateSetListener {
	Context context;
	public DateSettings(Context context) {
		this.context = context;
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		//StartActivity.selectedPuzzleDate = year + "-" + monthOfYear + "-" + dayOfMonth;
		String date = year + "-" + monthOfYear + "-" + dayOfMonth;
		
		
		
		
		
		//Toast.makeText(context, "Selected date: " + dayOfMonth + ", " + monthOfYear + ", " + year, Toast.LENGTH_SHORT).show();
		
	}

}
