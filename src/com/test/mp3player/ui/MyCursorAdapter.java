package com.test.mp3player.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.test.mp3player.R;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MyCursorAdapter extends SimpleCursorAdapter implements OnItemClickListener{
	private Context myContext;
	private Cursor myCursor;
	
	private HashMap<Integer, Boolean> isSelected ;

	public MyCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
		myContext = context;
		myCursor = c ;
		
		init();
	}
	
	public void init(){
		isSelected = new HashMap<Integer, Boolean>();
		for (int i = 0; i < getCount(); i++) {
			isSelected.put(i, false);			
		}
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		
		if(convertView == null){
			viewHolder = new ViewHolder();
			
			convertView  = newView(myContext, myCursor, parent);
			viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
			
			convertView.setTag(viewHolder);
			
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.checkBox.setChecked(isSelected.get(position));	
		
		myCursor.moveToPosition(position);
		
		bindView(convertView, myContext, myCursor);
		return convertView;
	}
	
	class ViewHolder{
		CheckBox checkBox;
	}
	
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ViewHolder holder = (ViewHolder) view.getTag();
		
		boolean flag = isSelected.get(position);
		if (!flag) {
			flag = true;
			isSelected.put(position, flag);
		}else {
			flag = false;
			isSelected.put(position, flag);
		}
		holder.checkBox.setChecked(flag);
	}	
	
	
	public ArrayList<Integer> getSelectedPostion(){
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < getCount(); i++) {
			if(isSelected.get(i)){
				list.add(i);
			}
		}
		return list;
	}
	
	public ArrayList<Long> getSelectedId(){
		ArrayList<Long> list = new ArrayList<Long>();
		for (int i = 0; i < getCount(); i++) {
			if(isSelected.get(i)){
				list.add(getItemId(i));
			}
		}
		return list;
	}
	
	
	public  HashMap<String, Long> getTitleAndId(String columnName) {
		if(myCursor == null){
			throw new RuntimeException("The argument cursor can't be null!");
		}
		
		int index = myCursor.getColumnIndex(columnName);
		HashMap<String, Long> map = new HashMap<String, Long>(); 
		
		for(int i = 0;i<isSelected.size();i++){
			if(isSelected.get(i)){
				myCursor.moveToPosition(i);
				String title = myCursor.getString(index);
				map.put(title, getItemId(i));
			}
		}
		
		return map;
	}
}
