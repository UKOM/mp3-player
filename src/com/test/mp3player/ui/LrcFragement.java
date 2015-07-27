package com.test.mp3player.ui;

import com.test.mp3player.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LrcFragement extends Fragment {
	private String lrc = "<暂不支持显示歌词功能>";
	private View view ;
	private TextView lrcView ;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.lrc_fragement, container, false);
		lrcView = (TextView) view.findViewById(R.id.lrc_tv);
		lrcView.setText(lrc);
		
		return view;
	}
}
