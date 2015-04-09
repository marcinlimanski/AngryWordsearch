package com.marcinlimanski.angrywordsearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class PuzzleGridView extends GridView {

	public PuzzleGridView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public PuzzleGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public PuzzleGridView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}


	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		
		// invalidate(); // Redraws.
		super.onDraw(canvas);
		
		if(PuzzleActivity.startDrawFirstPoint){
			DrawPoint.SinglePoint(canvas, PuzzleActivity.pointAArray[0], PuzzleActivity.pointAArray[1]);
		}
		else{
			DrawPoint.ClearPoint(canvas, PuzzleActivity.pointAArray[0], PuzzleActivity.pointAArray[1]);
		}

		
		try{
			JSONObject jsonMainObject = new JSONObject(PuzzleActivity.jsonFoundWordsObject);
			JSONArray jsonArray = jsonMainObject.getJSONArray("FoundWords");
			//Checking if the array has this word already
			for (int i=0; i<jsonArray.length(); i++){
				JSONObject wordObject = jsonArray.getJSONObject(i);

				int startX = (Integer) wordObject.get("sX");
				int startY =  (Integer) wordObject.get("sY");
				int endX = (Integer) wordObject.get("eX");
				int endY = (Integer) wordObject.get("eY");
				
				float testX = startX;
				float testY = startY;
				float test1X = endX;
				float test1Y = endY;
				
				DrawPoint.DrawWord(canvas, testX, testY, test1X, test1Y);
				
			}
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		
		

		
	}

	@Override
	public void setOnTouchListener(OnTouchListener l) {
		// TODO Auto-generated method stub
		super.setOnTouchListener(l);
	}

	@Override
	public void setOnItemClickListener(android.widget.AdapterView.OnItemClickListener listener) {
		// TODO Auto-generated method stub
		super.setOnItemClickListener(listener);
		
		
	}
	
	
	
	
	
	
	
	
	
	
	

}
