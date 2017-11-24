//package com.example.textgetdemo;
//
//import java.io.IOException;
//import java.util.List;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.view.Menu;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.example.common.MyApp;
//import com.example.domain.Info;
//import com.example.tool.TextUtils;
//import com.example.tool.LoaclUtils;
//
//@SuppressLint("HandlerLeak")
//public class LookActivity extends Activity {
//
//	TextView textVie;
//	int index = 0;
//	Button btnNext;
//	List<Info> list;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.look);
//		textVie = (TextView) findViewById(R.id.textView1);
//		btnNext = (Button) findViewById(R.id.btnNext);
//		Intent intent = getIntent();
//		String bookUrl = intent.getStringExtra("bookUrl");
//		String url = null;
//		final String book = intent.getStringExtra("book");
//		String v = intent.getStringExtra("index");
//		final String name = book + v;
//		final Handler h = new Handler() {
//			@Override
//			public void handleMessage(Message msg) {
//				super.handleMessage(msg);
//				String r = (String) msg.obj;
//				textVie.setText(r);
//			}
//		};
//
//		try {
//			index = Integer.valueOf(v);
//		} catch (Exception e) {
//		}
//
//		if (bookUrl != null && !bookUrl.equals("")) //
//		{
//			try {
//				list = LoaclUtils.getRecords(book);
//				Info info = list.get(index);
//				url = info.url;
//				setTitle(info.text);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		} else {
//			list = ((MyApp) getApplication()).getList();
//			final String title = intent.getStringExtra("title");
//			setTitle(title + "");
//			url = getIntent().getStringExtra("url");
//		}
//
//		btnNext.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				index++;
//				final Info info = list.get(index);
//				setTitle(info.text);
//				new Thread(new MyThread(h, (book + index), info.url)).start();
//			}
//		});
//
//		new Thread(new MyThread(h, name, url)).start();
//	}
//
//	static final class MyThread implements Runnable {
//		Handler h;
//		String name;
//		String url;
//
//		public MyThread(Handler h, String name, String url) {
//			this.h = h;
//			this.name = name;
//			this.url = url;
//		}
//
//		@Override
//		public void run() {
//			TextUtils.getContent(url, name, h);
//		}
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
//
//}
