package com.test.mp3player.service;


import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.test.mp3player.custominterface.IActivity;
import com.test.mp3player.custominterface.IService;
import com.test.mp3player.ui.DisplayActivity;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class AudioService extends Service implements IService{

	private MediaPlayer mediaPlayer = new MediaPlayer();

	private String tag = "AudioService";
	
	private static int progress;
	
	private Timer timer;
	private TimerTask task;
	private static Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			progress += 250;
			mActivity.setSBProgress(progress);
		}
	};
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		DisplayActivity.setIService(this);
		super.onCreate();
	}
	@Override
	public void onDestroy() {
		if(mediaPlayer != null){
			mediaPlayer.release();
			mediaPlayer = null;
		}
		if(timer!=null){
			timer.cancel();
		}
		super.onDestroy();
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(tag, "AudioService is starting!!");
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void play(String title){
		Long id = listMap.get(title);
		//Log.i("AudioService", "playMp3 method is playing "+title+",it's id is "+id);
		Uri contentUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
		if(mediaPlayer != null){
			try {
				mediaPlayer.reset();
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mediaPlayer.setDataSource(this, contentUri);
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("AudioService", "mediaPlayer's state is erro!!");
			}
			mediaPlayer.prepareAsync();
			mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
						public void onPrepared(MediaPlayer mp) {
							mActivity.setDuration(mp.getDuration());
							
							mp.seekTo(progress);
							mp.start();
							updateProgress();
						}
					});
			mediaPlayer.setOnCompletionListener(mActivity);
		}
		
	}
	
	private void updateProgress() {
	
			timer = new Timer();
			task = new TimerTask() {
				public void run() {
					
					if (!isTouching) {
						handler.sendEmptyMessage(0);
					}
				}
			};
			timer.schedule(task,0,250);
		
	}
	
	
	
	
	private static IActivity mActivity;
	public static void setIActivity(IActivity iActivity){
		mActivity = iActivity;
	}
	
	private boolean isTouching;
	private Map<String, Long> listMap; 
	
	//以下全部为实现IServic 接口的方法，主要由前台回调，响应前台，以实现同步

	@Override
	public void setListMap(Map<String, Long> map) {
		listMap = map;
	}

	@Override
	public void callServStart(String currentTitle) {
		setTouchState(false);
		play(currentTitle);
	}

	@Override
	public void callServPause() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
			setTouchState(true);
		}
	}

	@Override
	public void setPlayTime(int time) {
		progress = time;
		
		mediaPlayer.seekTo(time);
		
	}

	@Override
	public void setTouchState(boolean arg) {
		isTouching = arg;
		
	}


	@Override
	public void setTitles(String[] strings) {
		//titles = strings;
	}	
	
	public static void clearProgress(){
		progress = 0;
	}
}
