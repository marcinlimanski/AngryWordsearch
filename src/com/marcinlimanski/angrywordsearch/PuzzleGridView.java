package com.marcinlimanski.angrywordsearch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;

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
		
		Paint paint = new Paint();
		
		paint.setColor(Color.BLUE);
		paint.setStrokeCap(Cap.ROUND);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		
		paint.setStrokeWidth(60);
		
		
		canvas.drawLine(404, 87, 0, 261, paint);
	}
	
	

}
