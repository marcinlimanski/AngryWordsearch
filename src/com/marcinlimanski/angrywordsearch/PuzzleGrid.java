package com.marcinlimanski.angrywordsearch;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PuzzleGrid extends BaseAdapter{

	Context context;
	String items = "abcdef";
	
	public PuzzleGrid(Context c){
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
		puzzleView.setText(String.valueOf(items.charAt(position)));
		
		return puzzleView;
	}

}
