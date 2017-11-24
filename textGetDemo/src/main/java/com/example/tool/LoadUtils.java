package com.example.tool;

import java.util.List;
import android.content.Context;
import android.util.Log;

import com.example.domain.BaseTask;
import com.example.domain.Info;
import com.example.service.LoadService;

public class LoadUtils {

	LoadService mService = null;
	Context mContext;

	public static void insert(String key, int i, Context c) {
		c.getSharedPreferences(LOAD_TABLE, Context.MODE_PRIVATE).edit()
				.putInt(key, i).commit();
	}

	public static int get(String key, Context c) {
		return c.getSharedPreferences(LOAD_TABLE, Context.MODE_PRIVATE).getInt(
				key, -1);
	}

	public LoadUtils(LoadService service, Context c) {
		this.mService = service;
		this.mContext = c;
	}

	static final String LOAD_TABLE = "load";

	public static final class Task extends BaseTask {

		String book;
		Context mContext;
		List<Info> list;
		TextUtils textUtils;
		LoadService mService;

		public Task(LoadService service, Context c, List<Info> list, String book) {
			this.book = book;
			this.mContext = c;
			this.list = list;
			textUtils = new TextUtils(c);
		}

		public void load(Context c, Info info, String book, int i) {
			Runnable r = info.r;
			if (r == null) {
				textUtils.loadText(null, info, book);
				insert(book, i, c);
			}
		}

		@Override
		public void run() {
			Context c = mContext;
			int size = list.size();
			int index = get(book, c);
			Log.e("loadBook", "index:" + index + "size:" + size);

			for (int i = 0; i < size && !mQuit; i++) {
				if (i <= index) {
					// TODO ÒÑ¾­ÏÂÔØ
				} else {
					Info info = list.get(i);
					load(c, info, book, i);
				}
			}
		}
	}

	public static boolean isLoad(int i, String key, Context c) {
		if (i <= get(key, c))
			return true;
		return false;
	}

	public void loadBook(List<Info> list, String book) {
		LoadService service = mService;
		service.Load(new Task(service, mContext, list, book), false);
	}
}
