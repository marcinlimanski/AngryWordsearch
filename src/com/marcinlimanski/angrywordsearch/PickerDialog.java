package com.marcinlimanski.angrywordsearch;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

public class PickerDialog extends DialogFragment {

	public PickerDialog() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		//Creating an object with date settings
		DateSettings dateSettings = new DateSettings(getActivity());
		
		//Getting current date
		Calendar calendar = Calendar.getInstance();
		
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		
		//Creating object DatePicekrDialog
		DatePickerDialog dialog;
		
		//Init this date picker object
		dialog = new DatePickerDialog(getActivity(), dateSettings, year, month, day);
		return dialog;
	}

}
