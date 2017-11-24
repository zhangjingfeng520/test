package com.example.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.common.MyItemTouchListener;
import com.example.domain.Record;
import com.example.reader.Read;
import com.example.textgetdemo.R;
import com.example.tool.LocalUtils;

public class LocalAdapter implements OnClickListener, OnLongClickListener {

	private Activity mContext;
	private LinearLayout parent;

	public static interface Iid {
		void setGame(String gameName);
	}

	public LocalAdapter(Activity context, LinearLayout parent) {
		this.parent = parent;
		this.mContext = context;
		itemTouchListener = new MyItemTouchListener(context);
	}

	public long getItemId(int position) {
		return position;
	}

	class ViewHolder {
		ImageView image;
		TextView name;
		TextView autor;
	}

	final MyItemTouchListener itemTouchListener;

	public View getView(Record info, int position) {

		ViewHolder holder = null;
		View convertView = null;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.local_item, null);
			holder.image = (ImageView) convertView
					.findViewById(R.id.local_image);
			holder.name = (TextView) convertView.findViewById(R.id.loacl_name);
			holder.autor = (TextView) convertView
					.findViewById(R.id.loacl_autor);
			convertView.setTag(holder);
		}

		holder.name.setText(info.name);
		holder.autor.setText(info.autor);
		Bitmap bm = BitmapFactory.decodeFile(info.src);
		if (bm != null)
			holder.image.setImageBitmap(bm);

		convertView.setTag(info);
		convertView.setOnClickListener(this);
		convertView.setOnLongClickListener(this);
		convertView.setOnTouchListener(itemTouchListener);

		return convertView;
	}

	@Override
	public void onClick(View v) {
		Record info = (Record) v.getTag();
		Intent intent = new Intent(mContext, Read.class);
		intent.putExtra("path", info.path);
		mContext.startActivity(intent);
	}

	@Override
	public boolean onLongClick(final View v) {
		final Record info = (Record) v.getTag();
		new AlertDialog.Builder(mContext).setTitle("É¾³ý").setMessage("ÊÇ·ñÉ¾³ý?")
				.setPositiveButton("ÊÇ", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						LocalUtils.delete(info, mContext);
						parent.removeView(v);
					}
				}).setNegativeButton("·ñ", null).show();
		return false;
	}
}
