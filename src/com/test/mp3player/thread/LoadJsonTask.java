package com.test.mp3player.thread;

import java.io.IOException;
import java.util.Map;

import com.test.mp3player.ui.DisplayActivity;
import com.test.mp3player.utils.Operator;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class LoadJsonTask extends AsyncTask<Void, Void, Map<String, Long>> {
	DisplayActivity mActivity;
	
	public LoadJsonTask(DisplayActivity activity){
		mActivity = activity;
	}
	
	@Override
	protected Map<String, Long> doInBackground(Void... params) {
		String 	str = null;
		Map<String, Long> map = null;
		try {
			str = Operator.readJson();
			map = Operator.parseJson2Map(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}	
	
	@Override
	protected void onPostExecute(Map<String, Long> result) {
		mActivity.setMap(result);
		Log.i("LoadJsonTask", result.toString());
		mActivity.showPlaylist(result);
	}
}
