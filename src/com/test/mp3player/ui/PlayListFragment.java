package com.test.mp3player.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Map;

import com.test.mp3player.R;
import com.test.mp3player.utils.Operator;

public class PlayListFragment extends Fragment implements OnItemClickListener{
	private View view;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private Map<String, Long> map; 
	private String[] titles;
	private DisplayActivity mActivity;
	
	public PlayListFragment(){
		
	}
	
	public PlayListFragment(Map<String, Long> m){
		map = m ;
		titles = pullTitles(m);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.play_list_fragment, container, false);
		listView = (ListView) view.findViewById(R.id.listView2);
		
		if(titles != null){
			adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice, titles);
			listView.setAdapter(adapter);
		}
		
		mActivity = (DisplayActivity) getActivity();
		
		listView.setOnItemClickListener(this);
		
		return view;
	}
	
	public String[] getTitles() {
		return titles;
	}
	
	public String[] pullTitles(Map<String, Long> m){
		if (map != null) {
			return Operator.drawKey(map);
		}
		return null;
	}
	
	public void changeData(Map<String, Long> map) {
		this.map = map;
		titles = pullTitles(map);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String title = adapter.getItem(position);
		mActivity.play(title);
		
	}
	
	public void setSelection(int position){
		listView.setSelection(position);
	}
}
