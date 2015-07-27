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
	// �����б���·��
	public final static String LIST_PATH = Environment
			.getExternalStorageDirectory() + "/list.txt";
	public final static File file = new File(LIST_PATH);

	// ��Map �б���ļ�ֵ��ת��ΪJson ��ʽ���ַ���
	public static String parseMap2Json(Map<String, Long> map) {
		JSONObject jo = new JSONObject(map);
		return jo.toString();
	}

	// ���ڽ�Json ��ʽ���ַ�������ΪMap ���󣬵������str ����Ϊ0 ʱ������null
	public static Map<String, Long> parseJson2Map(String str) {
		Gson gson = new Gson();
		Map<String, Long> map = null;
		if (str.length() != 0) {
			map = gson.fromJson(str, new TypeToken<Map<String, Long>>() {
			}.getType());
		}
		
		return map;
	}

	// ��Json ��ʽ���ַ������浽�ֻ��洢��
	public static void saveJson(String str) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(str.getBytes());
		fos.close();
	}

	// ���ڴ��ֻ��洢�м���Json ����
	public static String readJson() throws IOException {
		String str;
		FileInputStream fis = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		StringBuilder sb = new StringBuilder();
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
		br.close();
		// �������Ĳ����б��ļ��Ƿ�Ϊ��
		if (sb.length() == 0) {
			// throw new
		}
		return sb.toString();
	}

	// ���ڽ����沥���б����� ��Map ����ȡ�ļ���
	public static String[] drawKey(Map<String, Long> map) {
		Set<String> set = map.keySet();
		String[] strs = set.toArray(new String[0]);
		return strs;
	}
	
	//�õ���ǰ��������һ��������������Ҳ������ͷ����б��еĵ�һ�׸���
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
