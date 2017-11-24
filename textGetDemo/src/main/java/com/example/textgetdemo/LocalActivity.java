package com.example.textgetdemo;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.adapter.LocalAdapter;
import com.example.domain.Record;
import com.example.tool.LocalUtils;

/**
 * ±æµÿ Èø‚
 * 
 * @author DYL
 * 
 */
public class LocalActivity extends Activity {

	LocalAdapter adapter;
	LinearLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.local);
		layout = (LinearLayout) findViewById(R.id.local_layout);
		adapter = new LocalAdapter(this, layout);
		initData();
	}

	void initData() {
		List<Record> list = LocalUtils.getBooks(this);
		int position = 0;
		for (Record record : list) {
			layout.addView(adapter.getView(record, position));
			position++;
		}
	}
}
