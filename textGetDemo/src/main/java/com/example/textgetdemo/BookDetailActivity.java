package com.example.textgetdemo;

import com.example.common.MyApp;
import com.example.domain.Book;
import com.example.service.SearchService;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class BookDetailActivity extends Activity {
	ImageView icon; //
	TextView name, autor, lastUpdate, description;
	LinearLayout bookLayout;
	LinearLayout loadding;
	Button btnRead;
	String bookName;

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_detail);
		initWidget();
		String url = getIntent().getStringExtra("url");
		MyApp app = (MyApp) getApplication();
		final SearchService mService = app.getmSearchService();
		if (url != null && !url.equals("")) {
			loadding.setVisibility(View.VISIBLE);
			Book book = mService.getCache(url);
			if (book != null) {
				loadding.setVisibility(View.GONE);
				showBookDetail(book, mService);
			} else {
				mService.get(url, new Handler() {
					@Override
					public void handleMessage(Message msg) {
						super.handleMessage(msg);
						loadding.setVisibility(View.GONE);
						switch (msg.what) {
						case 1:
							Book book = (Book) msg.obj;
							if (book != null)
								showBookDetail(book, mService);
							break;
						case 0:
							Toast.makeText(getApplicationContext(), "º”‘ÿ ß∞‹",
									Toast.LENGTH_SHORT).show();
							break;
						default:
							break;
						}
					}
				});
			}
		}
	}

	private void showBookDetail(final Book book, final SearchService mService) {
		bookLayout.setVisibility(View.VISIBLE);
		final Handler h = new Handler() {
			public void handleMessage(Message msg) {
				Bitmap bm = (Bitmap) msg.obj;
				if (bm != null)
					icon.setImageBitmap(bm);
			};
		};
		new Thread(new Runnable() {

			@Override
			public void run() {
				byte[] bytes = mService.getBytes(book.src);
				if (bytes != null) {
					Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0,
							bytes.length);
					h.obtainMessage(1, bm).sendToTarget();
				}
			}
		}).start();
		bookName = book.name;
		name.setText(book.name);
		autor.setText(book.autor);
		lastUpdate.setText(book.lastUpdateTime);
		description.setText(book.description);
	}

	private void initWidget() {
		btnRead = (Button) findViewById(R.id.btnRead);
		btnRead.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ListActivity.class);
				intent.putExtra("book", bookName);
				startActivity(intent);
			}
		});
		loadding = (LinearLayout) findViewById(R.id.loadding);
		bookLayout = (LinearLayout) findViewById(R.id.book);
		icon = (ImageView) findViewById(R.id.icon);
		name = (TextView) findViewById(R.id.name);
		autor = (TextView) findViewById(R.id.autor);
		lastUpdate = (TextView) findViewById(R.id.lastUpdate);
		description = (TextView) findViewById(R.id.description);
	}

}
