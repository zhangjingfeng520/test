package com.example.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.example.domain.Info;
import com.example.domain.Record;

public class LocalUtils {

	static final String TABLE = "index";

	/**
	 * 从数据库获取本地书籍
	 */
	public static List<Record> getBooks(Context context) {
		List<Record> list = null;
		MyOpenSQHelper helper = new MyOpenSQHelper(context);
		list = helper.inquiryData();
		helper.close();
		return list;
	}

	/**
	 * 插入数据到数据库
	 */
	public static void insert(Record book, Context c) {
		MyOpenSQHelper helper = new MyOpenSQHelper(c);
		helper.insertData(book);
		helper.close();
	}

	public static void delete(Record book, Context c) {
		MyOpenSQHelper helper = new MyOpenSQHelper(c);
		helper.deleteData("bookname=?", new String[] { book.name });
		helper.close();
	}

	// public static void saveIndexToXml(String key, String value, Context c) {
	// c.getSharedPreferences(TABLE, Context.MODE_PRIVATE).edit()
	// .putString(key, value).commit();
	// }

	// public static String getValuefromXml(String key, Context c) {
	// return c.getSharedPreferences(TABLE, Context.MODE_PRIVATE).getString(
	// key, "0");
	// }

	/**
	 * 获取本地书籍目录
	 */
	public static List<Info> getRecords(String key) throws IOException {
		List<Info> list = new ArrayList<Info>();
		File f = new File(Const.saverecordpath + "/" + key + ".txt");
		if (f.exists()) // ...
		{
			FileReader fr = null;
			try {
				fr = new FileReader(f);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			if (fr != null) // ..
			{
				BufferedReader br = new BufferedReader(fr);
				String text;

				while ((text = br.readLine()) != null) {
					String url = text.substring(0, text.indexOf("///"));
					String s = text.substring(text.indexOf("///") + 3);
					Info info = new Info();
					info.url = url;
					info.text = s;
					list.add(info);
				}
				br.close();
				fr.close();
			}
		}
		return list;
	}
}
