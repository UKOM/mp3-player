package com.test.mp3player.ui;

import java.util.ArrayList;
import java.util.Map;

import com.test.mp3player.R;
import com.test.mp3player.custominterface.IActivity;
import com.test.mp3player.custominterface.IService;
import com.test.mp3player.service.AudioService;
import com.test.mp3player.thread.LoadJsonTask;
import com.test.mp3player.utils.Constants;
import com.test.mp3player.utils.Operator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.media.MediaPlayer;

public class DisplayActivity extends FragmentActivity implements IActivity {
	
	private int displayState = Constants.STATE_PAUSING;

	private SeekBar seekBar ;
	
	private ViewPager vPager;
	private PlayListFragment listFragment;
	private Fragment lrcFragment ;
	private ArrayList<Fragment> pagers;
	private FragmentPagerAdapter pagerAdapter;
	private FragmentManager fragmentManager;
	
	private Map<String, Long> map; 
	
	public void setMap(Map<String, Long> m){
		map = m;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display_activity_layout);
		
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		vPager = (ViewPager) findViewById(R.id.vpager);
		fragmentManager = getSupportFragmentManager();
		
		LoadJsonTask task = new LoadJsonTask(this);
		task.execute();
		
		/*if(Constants.STATE_PLAYING == displayState){
			setStatePlaying();
		}else if (Constants.STATE_PAUSING == displayState) {
			setStatePause();
		}*/

		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				mService.setTouchState(false);
				mService.setPlayTime(seekBar.getProgress());
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				mService.setTouchState(true);
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				
			}
		});
		
		
		
	}
	
	public void showPlaylist(Map<String, Long> map){
		if (map == null) {
			listFragment = new PlayListFragment();
		}else {
			listFragment = new PlayListFragment(map);
			titles = listFragment.getTitles();
			setCurrentTitle(titles);
			Log.i("DisplayActivity", "titles-0 is"+titles[0]);
		}
		lrcFragment = new LrcFragement();
		pagers = new ArrayList<Fragment>();
		pagers.add(listFragment);
		pagers.add(lrcFragment);
		pagerAdapter = new FragmentPagerAdapter(fragmentManager) {
			public int getCount() {
				// TODO Auto-generated method stub
				return pagers.size();
			}
			
			public Fragment getItem(int arg0) {
				// TODO Auto-generated method stub
				return pagers.get(arg0);
			}
		};
		
		vPager.setAdapter(pagerAdapter);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && data != null) {
			Log.i("onActivityResult", "map return!!");
			
			map = (Map<String, Long>) data.getSerializableExtra("selectPositions");
			if(map != null){
				listFragment.changeData(map);
				titles = listFragment.getTitles();
				setCurrentTitle(titles);
				
				initServ();
				
				pagerAdapter.destroyItem(vPager, 0, listFragment);
				pagerAdapter.instantiateItem(vPager, 0);
				
				AudioService.setListMap(map);
			}
		}
	}
	
	public void setCurrentTitle(String... title) {
		currentTitle = title[0];
	}
	
	
	/*
	@Override
	protected void onResume() {
		//如果后台正在播放，当界面回到前台时，再次开始更新SeekBar
		if (mService != null) {
			mService.setUIState(true);
			mService.reUpdateProgress();
		}
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		mService.setUIState(false);
		super.onPause();
	}
	*/
	@Override
	protected void onStart() {
		AudioService.setIActivity(this);
		Intent intent = new Intent(this,AudioService.class);
		startService(intent);
		
		
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		if(Constants.STATE_PAUSING == displayState){
			Intent intent = new Intent(this,AudioService.class);
			stopService(intent);
		}
		super.onStop();
	}
	
	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.btn_play_pause:
			if(Constants.STATE_PAUSING == displayState){
				displayState = Constants.STATE_PLAYING;
				initServ();
				mService.callServStart(currentTitle);
				setLvSelection(currentTitle);
			}else if(Constants.STATE_PLAYING == displayState){
				displayState = Constants.STATE_PAUSING;
				mService.callServPause();
			}
			break;
			
		case R.id.btn_set_list:
			Intent intent = new Intent(this,SetListActivity.class);
			int requestCode = 103;
			startActivityForResult(intent, requestCode);
			break;
		}
	}
	
	public void initServ(){
		
		AudioService.setListMap(map);
		//mService.setTitles(titles);
	}
	

	
	private static IService mService ;
	public static void setIService(IService iService){
		mService = iService;
	}
	
	//private boolean isServActive = false;
	private String[] titles;
	private String currentTitle;
	
	//以下全部为实现IActivity 接口的方法，主要由后台回调，根据后台播放进度，实时更新前台状态
	@Override
	public void setDuration(int duration) {
		Log.i("DisplayActivity", "setDuration is called!!!");
		seekBar.setMax(duration);
		seekBar.invalidate();
	}
	
	@Override
	public final void setSBProgress(int progress) {
		seekBar.setProgress(progress);
	}
	
	@Override
	public void onCompletion(MediaPlayer mp) {
		currentTitle = Operator.nextTitle(titles, currentTitle);
		AudioService.clearProgress();
		mService.callServStart(currentTitle);
		setLvSelection(currentTitle);
		
	}

	public void play(String title){
		AudioService.clearProgress();
		mService.callServStart(title);
		setLvSelection(currentTitle);
	}

	public void setLvSelection(String title) {
		int i = Operator.getIndex(titles, title);
		listFragment.setSelection(i);
	}

}
