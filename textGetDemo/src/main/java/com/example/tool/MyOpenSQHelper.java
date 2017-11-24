package com.example.tool;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.domain.Record;

/**
 * 
 * @author DYL
 * 
 */
public class MyOpenSQHelper {

	/*
	 * �滻�ַ����еĵ�����
	 */
	String parse(String str) {
		if (str != null) {
			if (str.indexOf("'") != -1)
				return str.replaceAll("'", "''");
		}
		return str;
	}

	private SQLiteDatabase sdb;
	private final MyOpenHelper mHelper;
	private static final String DB_NAME = "MyRead.db";
	private static final int DATABASE_VERSION = 3;// ���ݿ�汾 ��������˰�װ��ʱ���ִ��
													// onUpgrade����

	public MyOpenSQHelper(Context context) {
		mHelper = new MyOpenHelper(context.getApplicationContext(), DB_NAME,
				null, DATABASE_VERSION);
		open();
	}

	private void open() {
		try {
			sdb = mHelper.getWritableDatabase();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public void close() {
		if (mHelper != null) {
			mHelper.close();
		}
		if (sdb != null) {
			sdb.close();
			sdb = null;
		}
	}

	static final String TAG = "MyOpenSQHelper";

	/**
	 * �õ�����ID �����ֵ
	 */
	public int getMaxId() {
		int maxId = 0;
		String sql = "select max(_id) from books";
		Cursor cursor = sdb.rawQuery(sql, null);
		if (cursor != null) {
			cursor.moveToFirst();
			maxId = cursor.getInt(0);
			cursor.close();
		}
		return maxId;
	}

	/*
	 * �õ����ݿ��¼����
	 */
	public long getDataCount() {
		long count = 0l;
		Cursor cursor = sdb.rawQuery("select count(*) from books", null);
		if (cursor != null) {
			cursor.moveToFirst();
			count = cursor.getLong(0);
			cursor.close();
		}
		return count;
	}

	public void insertData(Record info) {
		String instr = "insert into books (bookname,autor,url,icon,path) values("
				+ "'"
				+ parse(info.name)
				+ "'"
				+ ","
				+ "'"
				+ parse(info.autor)
				+ "'"
				+ ","
				+ "'"
				+ parse(info.url)
				+ "'"
				+ ","
				+ "'"
				+ parse(info.src)
				+ "'"
				+ ","
				+ "'"
				+ parse(info.path)
				+ "'"
				+ ")";
		try {
			sdb.execSQL(instr);
		} catch (Exception e) {
		}
	}

	// ��ѯ����
	// ���˰����ǲ�ѯ������,�������ҷ��˺ܵͼ��Ĵ���
	public ArrayList<Record> inquiryData() {
		ArrayList<Record> list = new ArrayList<Record>();
		if (sdb != null) {
			Cursor cursor = sdb.query("books", null, null, null, null, null,
					null);
			quiry(list, cursor);
		} else
			Log.e(TAG, "null SQLiteBase");
		return list;
	}

	public void quiry(ArrayList<Record> list, Cursor cursor) {
		if (cursor == null)
			Log.e(TAG, "cursor is null");

		else if (cursor.moveToFirst()) {
			do {
				Record info = new Record(); // ע����䲻Ҫ����do{}while֮��
				String bookname = cursor.getString(cursor
						.getColumnIndex("bookname"));
				String autor = cursor.getString(cursor.getColumnIndex("autor"));
				String icon = cursor.getString(cursor.getColumnIndex("icon"));
				String url = cursor.getString(cursor.getColumnIndex("url"));
				String path = cursor.getString(cursor.getColumnIndex("path"));
				info.name = bookname;
				info.autor = autor;
				info.src = icon;
				info.url = url;
				info.path = path;
				list.add(info);
			} while (cursor.moveToNext());
		}
		if (cursor != null)
			cursor.close();
	}

	// ɾ����¼
	public int deleteData(String where, String[] whereAgs) {
		return sdb.delete("books", where, whereAgs);
	}

	private static final class MyOpenHelper extends SQLiteOpenHelper {

		private static final String DATABASE_CREATE = "create table books ("
				+ "bookname  text primary key, autor text not null, "
				+ "icon text not null," + "url text not null,"
				+ "path text not null" + ")";

		private static final String DATABASE_DELETE = "DROP TABLE IF EXISTS history";

		public MyOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("onUpgrade", "Upgrading database from version " + oldVersion
					+ " to " + newVersion + ", which will destroy all old data");
			db.execSQL(DATABASE_DELETE);
			onCreate(db);
		}
	}
}
