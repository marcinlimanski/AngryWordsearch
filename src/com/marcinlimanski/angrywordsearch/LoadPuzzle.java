package com.marcinlimanski.angrywordsearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class LoadPuzzle {
	public static String playingPuzzleId= "";
	public static String gridWords = "";
	public static ArrayList<String> wordsToFindArray = new ArrayList<String>();
	public static int gridNumberOfColumns;
	public static String playingPuzzleSolution;
	
	public static String puzzlesWordsToFind = "";
	//Constructor
	public LoadPuzzle() {
		
	}
	
	public static boolean InitPuzzle(String puzzle){
		boolean result = false;
		try{
			playingPuzzleId = "";
			gridWords = "";
			gridNumberOfColumns = 0;
			playingPuzzleSolution = "";
			//Parsing the Array formated puzzle
			if(StartActivity.puzzleFormatFlag){
				//Create a global JSONObject with data
				JSONObject puzzleObject = new JSONObject(puzzle);
				//Constructing a first array that holds two objects
				JSONArray puzzleAndSolutionArray = puzzleObject.getJSONArray("PuzzleAndSolution");
				//Extracting the Puzzle object
				JSONObject choosenPuzzleObject = puzzleAndSolutionArray.getJSONObject(0);
				JSONObject targetPuzzleObject = choosenPuzzleObject.getJSONObject("Puzzle");
				
				//Extracting JSON Solution object
				JSONObject choosenSolutionObject = puzzleAndSolutionArray.getJSONObject(1);
				JSONObject playingPuzzleSolutionObject = choosenSolutionObject.getJSONObject("Solution");
				playingPuzzleSolution = playingPuzzleSolutionObject.toString();
				//Checking if the object puzzle has been accessed correctly 
				Log.i("Playing Puzzle Solution", playingPuzzleSolutionObject.toString());
				
				//Extracting the Id of the puzzle 
				playingPuzzleId = targetPuzzleObject.getString("Id");
				//Checking if the object puzzle has been accessed correctly 
				//Log.i("Object id", playingPuzzleId.toString());
				
				wordsToFindArray.clear();
				//Extracting the words to find
				JSONArray wordsArray = targetPuzzleObject.getJSONArray("Words");
				for(int i=0; i<wordsArray.length(); i++){
					wordsToFindArray.add(wordsArray.getString(i).toString());
				}
				
				puzzlesWordsToFind = "";
				//Checkingif the words are parsed correctly 
				for (String temp : wordsToFindArray){
					Log.i("Word: ", temp);
					puzzlesWordsToFind = puzzlesWordsToFind + ",  " +temp.toString();
				}
				
				//Extracting the gridwords and column number
				JSONArray gridWordsArray = targetPuzzleObject.getJSONArray("Grid");
				//Assigning the number of columns for the grid 
				gridNumberOfColumns = gridWordsArray.length();
				for(int i=gridWordsArray.length() -1; i>=0; i--){
					
					gridWords = gridWords + gridWordsArray.getString(i).toString();
				}
				
				result = true;

			}
			//Parsing the normal formated puzzle
			else if(StartActivity.puzzleFormatFlag == false)
			{
				//Create a global JSONObject with data
				JSONObject puzzleObject = new JSONObject(puzzle);
				//Constructing a first array that holds two objects
				JSONObject puzzleAndSolutionPuzzle = puzzleObject.getJSONObject("PuzzleAndSolution");
				JSONObject targetPuzzleObject = puzzleAndSolutionPuzzle.getJSONObject("Puzzle");

				//Extracting JSON Solution object
				JSONObject playingPuzzleSolutionObject = puzzleAndSolutionPuzzle.getJSONObject("Solution");
				playingPuzzleSolution = playingPuzzleSolutionObject.toString();
				//Checking if the object puzzle has been accessed correctly 
				Log.i("Playing Puzzle Solution", playingPuzzleSolutionObject.toString());
				
				
				//Extracting the Id of the puzzle 
				playingPuzzleId = targetPuzzleObject.getString("Id");
				//Checking if the object puzzle has been accessed correctly 
				//Log.i("Object id", playingPuzzleId.toString());
				
				wordsToFindArray.clear();
				//Extracting the words to find
				JSONArray wordsArray = targetPuzzleObject.getJSONArray("Words");
				for(int i=0; i<wordsArray.length(); i++){
					wordsToFindArray.add(wordsArray.getString(i).toString());
				}
				
				puzzlesWordsToFind = "";
				//Checkingif the words are parsed correctly 
				for (String temp : wordsToFindArray){
					puzzlesWordsToFind = puzzlesWordsToFind + ",  " +temp.toString();
					Log.i("Word: ", temp);
				}
				
				//Extracting the gridwords and column number
				JSONArray gridWordsArray = targetPuzzleObject.getJSONArray("Grid");
				//Assigning the number of columns for the grid 
				gridNumberOfColumns = gridWordsArray.length();
				for(int i=gridWordsArray.length() -1; i>=0; i--){
					
					gridWords = gridWords + gridWordsArray.getString(i).toString();
				}
				result = true;
				
			}
			else{
				Log.i("Failed to load puzzle", "Puzzle Failed");
			}
			
			
		}
		catch(Exception e){
			e.printStackTrace();
			return result;
		}
		
		return result;
	}
	
	

}
