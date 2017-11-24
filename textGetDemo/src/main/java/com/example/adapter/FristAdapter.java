package com.example.adapter;

import java.util.List;

import com.example.common.MyApp;
import com.example.domain.PicData;
import com.example.domain.Record;
import com.example.textgetdemo.R;

import android.app.Application;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FristAdapter extends BaseAdapter {

	Application mApplication;
	List<Record> hots;
	Handler h;

	public FristAdapter(Application a, List<Record> list, Handler h) {
		mApplication = a;
		hots = list;
		this.h = h;
	}

	class ViewHolder {
		ImageView iv;
		TextView name, autor;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder vHolder;
		if (convertView == null) {
			vHolder = new ViewHolder();
			convertView = View.inflate(mApplication, R.layout.frist_hots_item,
					null);
			vHolder.iv = (ImageView) convertView.findViewById(R.id.icon);
			vHolder.name = (TextView) convertView.findViewById(R.id.name);
			vHolder.autor = (TextView) convertView.findViewById(R.id.autor);
			convertView.setTag(vHolder);
		} else {
			vHolder = (ViewHolder) convertView.getTag();
		}

		final Record r = hots.get(position);

		vHolder.name.setText(r.name);
		if (r.autor != null)
			vHolder.autor.setText(r.autor);

		if (!r.IsShow) {
			r.IsShow = true;
			new Thread(new Runnable() {

				@Override
				public void run() {
					PicData pic = new PicData(vHolder.iv);
					MyApp app = (MyApp) mApplication;
					pic.bytes = app.getmSearchService().getBytes(r.src);
					Log.e("TAG", r.src);
					h.obtainMessage(1, pic).sendToTarget();
				}
			}).start();
		}
		return convertView;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public Object getItem(int position) {
		return hots.get(position);
	}

	@Override
	public int getCount() {
		return hots.size();
	}
}
