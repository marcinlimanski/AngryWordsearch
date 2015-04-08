package com.marcinlimanski.angrywordsearch;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PuzzleGridAdapter extends BaseAdapter{

	Context context;
	String items = LoadPuzzle.gridWords;
	
	public PuzzleGridAdapter(Context c){
		context = c;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.length();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return items.charAt(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView puzzleView = new TextView(context);
		puzzleView.setTextSize(20);
		puzzleView.setText(String.valueOf(items.charAt(position)));
		
		
		
		return puzzleView;
	}
	
	
	
	
	
	
	
	

}
