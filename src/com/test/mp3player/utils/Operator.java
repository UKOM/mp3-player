package com.test.mp3player.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.os.Environment;

public class Operator {
	// 播放列表保存路径
	public final static String LIST_PATH = Environment
			.getExternalStorageDirectory() + "/list.txt";
	public final static File file = new File(LIST_PATH);

	// 将Map 中保存的键值对转化为Json 格式的字符串
	public static String parseMap2Json(Map<String, Long> map) {
		JSONObject jo = new JSONObject(map);
		return jo.toString();
	}

	// 用于将Json 格式的字符串解析为Map 对象，当传入的str 长度为0 时，返回null
	public static Map<String, Long> parseJson2Map(String str) {
		Gson gson = new Gson();
		Map<String, Long> map = null;
		if (str.length() != 0) {
			map = gson.fromJson(str, new TypeToken<Map<String, Long>>() {
			}.getType());
		}
		
		return map;
	}

	// 将Json 格式的字符串保存到手机存储中
	public static void saveJson(String str) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(str.getBytes());
		fos.close();
	}

	// 用于从手机存储中加载Json 数据
	public static String readJson() throws IOException {
		String str;
		FileInputStream fis = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		StringBuilder sb = new StringBuilder();
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
		br.close();
		// 检查读出的播放列表文件是否为空
		if (sb.length() == 0) {
			// throw new
		}
		return sb.toString();
	}

	// 用于将保存播放列表数据 的Map 中提取文件名
	public static String[] drawKey(Map<String, Long> map) {
		Set<String> set = map.keySet();
		String[] strs = set.toArray(new String[0]);
		return strs;
	}
	
	//得到当前歌曲的下一个歌曲名，如果找不到，就返回列表中的第一首歌曲
	public static String nextTitle(String[] titles,String title) {
		for (int i = 0; i < titles.length; i++) {
			if (title.equals(titles[i])) {
				if (i == titles.length-1) {
					return titles[0];
				}
				return titles[i+1];
			}
		}
		return titles[0];
	}
	
	public static int getIndex(String[] titles,String title){
		for (int i = 0; i < titles.length; i++) {
			if (title.equals(titles[i])) {
				return i;
			}
		}
		return 0;
	}
}
