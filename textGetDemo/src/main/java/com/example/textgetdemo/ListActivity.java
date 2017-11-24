package com.example.textgetdemo;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.common.MyApp;
import com.example.domain.BaseTask;
import com.example.domain.Info;
import com.example.reader.Read;
import com.example.service.LoadService;
import com.example.service.LoadService.Mybinder;
import com.example.service.SearchService;
import com.example.tool.LoadUtils;
import com.example.tool.TextUtils;

/**
 * 小说章节列表 本书名称《武极天下》 笔趣阁小说阅读网作为源
 * 
 * @author huiyuan
 * 
 */
@SuppressLint({ "HandlerLeak", "ShowToast" })
public class ListActivity extends Activity {

	ListView listView;
	LoadService mService;
	String book;

	ServiceConnection connection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = ((Mybinder) service).getService();
			mService.Load(r, true);
		}
	};

	List<Info> infos;

	final Handler h = new Handler() {
		public void handleMessage(android.os.Message msg) {
			loadding.setVisibility(View.GONE);
			if (msg.what > 0) {
				listView.setAdapter(new BaseAdapter() {

					final Context c = getApplicationContext();

					final class ViewHolder {
						TextView text;
					}

					@Override
					public View getView(final int position, View convertView,
							ViewGroup parent) {

						ViewHolder holder = null;

						if (convertView == null) {
							holder = new ViewHolder();
							convertView = View.inflate(c, R.layout.list_item,
									null);
							holder.text = (TextView) convertView
									.findViewById(R.id.text);
							convertView.setTag(holder);

						} else {
							holder = (ViewHolder) convertView.getTag();
						}

						holder.text.setText(infos.get(position).text);

						return convertView;
					}

					@Override
					public long getItemId(int position) {
						return position;
					}

					@Override
					public Object getItem(int position) {
						return infos.get(position);
					}

					@Override
					public int getCount() {
						return infos.size();
					}
				});
			} else
				Toast.makeText(getApplicationContext(), "获取章节目录失败", 0).show();
		}
	};

	final BaseTask r = new BaseTask() {

		@Override
		public void run() {
			LoadUtils loadUtils = new LoadUtils(mService,
					getApplicationContext());
			MyApp app = (MyApp) getApplication();
			infos = app.getmSearchService().getDirectory();
			// 开始下载本书
			loadUtils.loadBook(infos, book);
			h.obtainMessage(infos.size()).sendToTarget();
		}
	};

	boolean mQuit = false;

	@Override
	protected void onDestroy() {
		mQuit = true;
		unbindService(connection);
		super.onDestroy();
	}

	LinearLayout loadding;
	String path;

	@Override
	protected void onStart() {
		MyApp app = (MyApp) getApplication();
		SearchService service = app.getmSearchService();
		service.insert(service.getBook(), book);
		super.onStart();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		listView = (ListView) findViewById(R.id.listView1);
		book = getIntent().getStringExtra("book");
		path = TextUtils.getTxtPath(book);
		TextView text = (TextView) findViewById(R.id.text);
		text.setText(book + "");
		Button btn = (Button) findViewById(R.id.btnRead);

		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent it = new Intent();
				it.setClass(v.getContext(), Read.class);
				it.putExtra("path", path);
				startActivity(it);
			}
		});

		loadding = (LinearLayout) findViewById(R.id.loadding);
		Intent service = new Intent(this, LoadService.class);
		bindService(service, connection, Context.BIND_AUTO_CREATE);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 跳转到阅读器...
				Intent it = new Intent();
				it.setClass(view.getContext(), Read.class);
				it.putExtra("path", path);
				startActivity(it);
			}
		});
	}
}
