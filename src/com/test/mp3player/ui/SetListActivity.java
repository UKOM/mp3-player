package com.test.mp3player.ui;


import java.util.HashMap;

import com.test.mp3player.R;
import com.test.mp3player.thread.SaveJsonThread;
import com.test.mp3player.utils.Operator;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SetListActivity extends Activity {
	private MyCursorAdapter adapter;
	private ListView listView;
	private String title;
	
	private  Cursor cursor ;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_list_activity_layout);

		listView = (ListView) findViewById(R.id.listView1);

		ContentResolver resolver = getContentResolver();
		Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		cursor = resolver.query(uri, null, null, null, null);

		Log.i("SetListActivity", "i am normal until now!!!");

		if (cursor != null) {
			cursor.moveToFirst();
		}

		/*
		 * int titleIndex = cursor
		 * .getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
		 * String title = cursor.getString(titleIndex);
		 */

		title = android.provider.MediaStore.Audio.Media.TITLE;

		adapter = new MyCursorAdapter(this, R.layout.set_list_item_layout,
				cursor, new String[] { title },
				new int[] { R.id.set_list_item_tv },
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

		listView.setAdapter(adapter);
		listView.setOnItemClickListener(adapter);
	}

	public void btnOnClick(View v) {
		//Toast.makeText(this, adapter.getSelectedPostion().size()+"", Toast.LENGTH_SHORT).show();
		
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		HashMap<String, Long> map = adapter.getTitleAndId(title);
		bundle.putSerializable("selectPositions", map);
		
		//Toast.makeText(this, Operator.LIST_PATH, Toast.LENGTH_LONG).show();
		
		SaveJsonThread thread = new SaveJsonThread(map);
		thread.start();
		
		intent.putExtras(bundle);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	protected void onDestroy() {
		if(cursor != null){
			cursor.close();
		}
		super.onDestroy();
	}
}
