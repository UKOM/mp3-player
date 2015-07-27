package com.test.mp3player.custominterface;

import java.util.Map;

public interface IService {

	void setListMap(Map<String, Long> map);
	void setTitles(String[] strings);
	void callServStart(String currentTitle);
	void callServPause();
	void setPlayTime(int time);
	void setTouchState(boolean arg);
}
