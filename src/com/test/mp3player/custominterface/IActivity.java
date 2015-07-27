package com.test.mp3player.custominterface;

import android.media.MediaPlayer.OnCompletionListener;

public interface IActivity extends OnCompletionListener{
	void setDuration(int duration);
	void setSBProgress(int arg);

}
