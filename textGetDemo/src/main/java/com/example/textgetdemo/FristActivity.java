package com.example.textgetdemo;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.adapter.FristAdapter;
import com.example.common.MyItemTouchListener;
import com.example.domain.PicData;
import com.example.domain.Record;
import com.example.tool.Recommend;

@SuppressLint("HandlerLeak")
public class FristActivity extends Activity implements OnItemClickListener {

	List<Record> hots;
	List<Record> minors;
	List<Record> lists;

	GridView hot;
	GridView minor;
	LinearLayout layout;

	Recommend recommend;

//	final Handler h = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//
//			switch (msg.what) {
//			case 0: {
//				Application application = getApplication();
//				hot.setAdapter(new FristAdapter(application, hots, h));
//				minor.setAdapter(new FristAdapter(application, minors, h));
//				hot.setOnItemClickListener(FristActivity.this);
//				minor.setOnItemClickListener(FristActivity.this);
//				initList();
//			}
//				break;
//			case 1:
//				PicData data = (PicData) msg.obj;
//				if (data != null) {
//					Bitmap bm;
//					byte[] bytes = data.bytes;
//					bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//					data.iv.setImageBitmap(bm);
//				}
//				break;
//			default:
//				break;
//			}
//		}
//	};

	void initList() {
		MyItemTouchListener listener = new MyItemTouchListener(this);
		List<Record> list = lists;
		int size = list.size();
		MyClick myClick = new MyClick();
		for (int i = 0; i < size; i++) {
			Record r = list.get(i);
			View v = View.inflate(this, R.layout.frist_list_item, null);
			TextView sort = (TextView) v.findViewById(R.id.sort);
			TextView name = (TextView) v.findViewById(R.id.name);
			TextView autor = (TextView) v.findViewById(R.id.autor);
			sort.setText(r.sort);
			name.setText(r.name);
			autor.setText(r.autor);
			v.setTag(r.url);
			v.setOnClickListener(myClick);
			v.setOnTouchListener(listener);
			layout.addView(v);
		}
	}

	final class MyClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.frist_list:
				String url = (String) v.getTag();
				Intent intent = new Intent(v.getContext(),
						BookDetailActivity.class);
				intent.putExtra("url", url);
				startActivity(intent);
				break;
			default:
				break;
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frist);
		hot = (GridView) findViewById(R.id.hots);
		minor = (GridView) findViewById(R.id.minor);
		layout = (LinearLayout) findViewById(R.id.list);
		recommend = new Recommend();
		new Thread(new Runnable() {
			@Override
			public void run() {
				hots = recommend.getHots();
				minors = recommend.getMinors();
				lists = recommend.getList();
				//h.obtainMessage(0).sendToTarget();
			}
		}).start();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.hots:
			String url = hots.get(position).url;
			Intent intent = new Intent(view.getContext(),
					BookDetailActivity.class);
			intent.putExtra("url", url);
			startActivity(intent);
			break;
		case R.id.minor:
			String url1 = minors.get(position).url;
			Intent intent1 = new Intent(view.getContext(),
					BookDetailActivity.class);
			intent1.putExtra("url", url1);
			startActivity(intent1);
			break;
		default:
			break;
		}
	}
}
