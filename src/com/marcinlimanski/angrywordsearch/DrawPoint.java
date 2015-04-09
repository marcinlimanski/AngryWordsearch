package com.marcinlimanski.angrywordsearch;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;

public class DrawPoint {

	public DrawPoint() {
		// TODO Auto-generated constructor stub
	}
	
	//Drawing just one point on the grid
	public static void SinglePoint(Canvas canvas, float startX, float startY){
		Paint paint = new Paint();
		paint.setColor(Color.CYAN);;

		canvas.drawCircle(startX + 23, startY + 40, 35, paint);
		
	}
	
	public static void ClearPoint(Canvas canvas, float startX, float startY){
		Paint paint = new Paint();
		paint.setColor(Color.TRANSPARENT);;

		canvas.drawCircle(startX + 23, startY + 40, 35, paint);
		
	}
	
	
	//Drawing the whole word 
	public static void DrawWord(Canvas canvas, float startX, float startY, float endX, float endY){
		Paint paint = new Paint();
		
		paint.setColor(Color.CYAN);
		paint.setStrokeCap(Cap.ROUND);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		
		paint.setStrokeWidth(60);
		
		
		canvas.drawLine(startX + 15, startY + 25, endX + 15, endY + 25, paint);
	}
}
