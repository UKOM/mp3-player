package com.test.mp3player.thread;

import java.io.IOException;
import java.util.HashMap;

import com.test.mp3player.utils.Operator;

public class SaveJsonThread extends Thread {
	HashMap<String, Long> map;
	
	public SaveJsonThread(HashMap<String, Long> m) {
		map = m ;
	}

	public void run() {
		String str = Operator.parseMap2Json(map);
		try {
			Operator.saveJson(str);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
}
