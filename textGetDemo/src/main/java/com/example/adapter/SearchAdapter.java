package com.example.adapter;

import java.util.List;

import com.example.domain.Book;
import com.example.textgetdemo.R;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchAdapter extends BaseAdapter {

	List<Book> books;
	Context mContext;

	class ViewHolder {
		TextView name;
		TextView autor;
	}

	public SearchAdapter(List<Book> books, Activity c) {
		this.books = books;
		this.mContext = c;
	}

	@Override
	public int getCount() {
		return books.size();
	}

	@Override
	public Object getItem(int position) {
		return books.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.search_item, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.autor = (TextView) convertView.findViewById(R.id.autor);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Book book = books.get(position);
		holder.name.setText(book.name);
		holder.autor.setText(book.autor);
		return convertView;
	}

}
