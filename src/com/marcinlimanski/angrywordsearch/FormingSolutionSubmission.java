package com.marcinlimanski.angrywordsearch;

import org.json.JSONException;
import org.json.JSONObject;

public class FormingSolutionSubmission {

	public FormingSolutionSubmission() {
		// TODO Auto-generated constructor stub
	}
	
	public static String SubmitPuzzleSolution(String completedSolution, String name, String password){
		String  PuzzleSubmission = "";
		try{
			JSONObject solution = new JSONObject(completedSolution);
			
			//Submision object 
			JSONObject jsonSubmissionObject = new JSONObject();
			jsonSubmissionObject.put("Username", name);
			jsonSubmissionObject.put("Password", password);
			jsonSubmissionObject.put("Solution", solution);
			
			JSONObject mainObject = new JSONObject();
			mainObject.put("Submission", jsonSubmissionObject);
			
			
			PuzzleSubmission = mainObject.toString();
			
			
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		
		
		
		
		return PuzzleSubmission;
	}

}
