package com.marcinlimanski.angrywordsearch;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class SaveAndRestoreJSONPuzzle implements OnHTTPReg{
	private static String allDates = "";
	static Context contextB;
	public SaveAndRestoreJSONPuzzle() {
		// TODO Auto-generated constructor stub
	}
	
	public static boolean SaveJSONTodaysPuzzleAndSolution(Context context, String puzzleObject, String solutionObject ,String puzzleDate){
		
		Log.i("Today Puzzle: ", puzzleObject.toString());
		
		Log.i("Todays solution: ", solutionObject.toString());
		
		return false;
		
	}
	
	//Save the JSON PuzzleAndsolutio ton internal memory 
	public static boolean SaveJSONPuzzleAndSolution(Context context, String puzzleAndSolutionObject, String puzzleDate) throws IOException{
		
		boolean result = false;
		
		try{
			File fileCheck = new File("/data/data/com.marcinlimanski.angrywordsearch/files/"+puzzleDate+".json");
			if(!fileCheck.exists()){
				//Saving the puzzle
				String newData = puzzleAndSolutionObject.toString();
				FileOutputStream fos = context.openFileOutput(puzzleDate+".json", context.MODE_PRIVATE);
				fos.write(newData.getBytes());
				fos.close();
				result = true;
			}
			else{
				//Toast.makeText(contextB, "This puzzle already existis", Toast.LENGTH_SHORT).show();
				Log.i("File: ", "Puzzle Exists");
				result = false;
			}
				
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		
		
		return result;
		
		
	}
	
	//Restores JSON object from internal memory
	public static String RestoreJSONPuzzleandSolution(Context context, String filePath){
		String data = "";
		try{
			FileInputStream fin = context.openFileInput(filePath+".json");
			BufferedInputStream bis = new BufferedInputStream(fin);
			StringBuffer textBuffer = new StringBuffer();
			while(bis.available() != 0){
				char c  = (char)bis.read();
				textBuffer.append(c);
			}
			bis.close();
			fin.close();
			
			data = textBuffer.toString();
			
			
			StartActivity.globalDates = textBuffer.toString();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		return data;
	}
	
	//Save JSONObject just for dates
	public static void SaveJSONDates(String puzzleDate, Context context) throws JSONException, IOException{
	
		Log.i("File: ", "exists");
		Log.i("raw globalDates", StartActivity.globalDates);
		
		JSONObject jsonObjMain = new JSONObject(StartActivity.globalDates); //Your existing object
		JSONArray jsonArray_puzzleDates = jsonObjMain.getJSONArray("PuzzleDates"); //Array where you wish to append
		JSONObject jO = new JSONObject(); //new Json Object
		
		
		//Add data
		jO.put("date", puzzleDate);
		//Append
		jsonArray_puzzleDates.put(jO);
		Log.i("Josob array:", jsonArray_puzzleDates.toString());
		
		JSONObject puzzleObj = new JSONObject();
		puzzleObj.put("PuzzleDates", jsonArray_puzzleDates);
		
		
		StartActivity.globalDates = puzzleObj.toString();
		Log.i("globalDates:", StartActivity.globalDates);
		
		
		String newData = StartActivity.globalDates;
		FileOutputStream fos = context.openFileOutput("dates.json", Context.MODE_PRIVATE);
		fos.write(newData.getBytes());
		fos.close();
		
	}
	
	public static String RestoreJSONSates(Context context) throws IOException, JSONException{
		String data = "";
		
		FileInputStream fin = context.openFileInput("dates.json");
		BufferedInputStream bis = new BufferedInputStream(fin);
		StringBuffer textBuffer = new StringBuffer();
		while(bis.available() != 0){
			char c  = (char)bis.read();
			textBuffer.append(c);
		}
		bis.close();
		fin.close();
		
		data = textBuffer.toString();
		
		
		StartActivity.globalDates = textBuffer.toString();
		return data;
		
	}
	
	
	public static void createObject(Context context) throws JSONException, IOException{
		Log.i("File: ", "does not exists");
		
		JSONObject data = new JSONObject();
		data.put("date", "test-test-test");
		
		JSONArray jArray = new JSONArray();
		jArray.put(data);
		
		JSONObject datesObject = new JSONObject();
		datesObject.putOpt("PuzzleDates", jArray);
		
		String newData = datesObject.toString();
		FileOutputStream fos = context.openFileOutput("dates.json", context.MODE_PRIVATE);
		fos.write(newData.getBytes());
		fos.close();
	}

	@Override
	public void onTaskCompleted(String httpData) {
		// TODO Auto-generated method stub
		
	}

}
