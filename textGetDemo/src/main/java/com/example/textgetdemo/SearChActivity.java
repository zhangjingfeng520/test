package com.example.textgetdemo;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.adapter.SearchAdapter;
import com.example.common.MyApp;
import com.example.domain.Book;
import com.example.service.SearchService;

@SuppressLint("HandlerLeak")
public class SearChActivity extends Activity implements OnItemClickListener {

	EditText etSearch;
	Button btnSearch;
	SearchService mService;
	LinearLayout loadding;
	ListView listView;
	SearchAdapter adapter;
	List<Book> books;

	void hide() {
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(this.getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		etSearch = (EditText) findViewById(R.id.editSearch);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		mService = ((MyApp) getApplication()).getmSearchService();
		initWidget();

		btnSearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				hide();
				String key = etSearch.getText().toString();
				final Activity c = SearChActivity.this;
				if (key != null && !key.equals("")) {
					loadding.setVisibility(View.VISIBLE);
					mService.search(key, new Handler() {
						@SuppressWarnings("unchecked")
						@Override
						public void handleMessage(Message msg) {
							loadding.setVisibility(View.GONE);
							mService.unlock();
							switch (msg.what) {
							case 1: { // ËÑË÷³É¹¦
								books = (List<Book>) msg.obj;
								if (books != null) // ...
								{
									if (books.size() > 0) {
										adapter = new SearchAdapter(books, c);
										listView.setAdapter(adapter);
									}
								}
							}
								break;
							case 0: {
								Toast.makeText(getApplicationContext(), "ËÑË÷Ê§°Ü",
										Toast.LENGTH_SHORT).show();
							}
								break;
							default:
								break;
							}
							super.handleMessage(msg);
						}
					});
				} else
					Toast.makeText(c, "ÇëÊäÈëËÑË÷¹Ø¼ü×Ö", Toast.LENGTH_SHORT).show();
			}
		});
	}

	String bookName;

	private void initWidget() {
		loadding = (LinearLayout) findViewById(R.id.loadding);
		listView = (ListView) findViewById(R.id.list);
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String url = books.get(position).url;
		Intent intent1 = new Intent(view.getContext(), BookDetailActivity.class);
		intent1.putExtra("url", url);
		startActivity(intent1);
	}
}
