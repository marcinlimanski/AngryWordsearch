package com.marcinlimanski.angrywordsearch;

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
		if(PuzzleActivity.startDrawSecondPoint){
			DrawPoint.SinglePoint(canvas, PuzzleActivity.pointBArray[0], PuzzleActivity.pointBArray[1]);
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
