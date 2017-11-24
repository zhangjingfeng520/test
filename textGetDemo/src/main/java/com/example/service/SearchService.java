package com.example.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.content.Context;
import android.os.Handler;

import com.example.domain.Book;
import com.example.domain.Info;
import com.example.domain.Record;
import com.example.domain.Search;
import com.example.tool.Const;
import com.example.tool.LocalUtils;
import com.example.tool.TextUtils;

public class SearchService {

	public static final String SEARCH = "http://www.biquge.com/xiaoshuodaquan/";
	private boolean lock;
	private Search search;
	private Context mContext;
	private Book book;

	public Book getBook() {
		return book;
	}

	public List<Info> getDirectory() {
		return search.getDirectory();
	}

	public Book getCache(String url) {
		if (book == null)
			return null;
		if (url.equals(book.url)) {
			return book;
		}
		return null;
	}

	public void insertBookToSQlite(String bookName, String autor, String src,
			String url) {
		Record record = new Record();
		record.name = bookName;
		record.url = url;
		record.autor = autor;
		record.src = src;
		record.path = TextUtils.getTxtPath(bookName);
		LocalUtils.insert(record, mContext);
	}

	static final String TAG = SearchService.class.getName();

	private String getSrc(String bookName) {
		return Const.saveiconpath + File.separator + bookName + ".png";
	}

	public void insert(Book book, String bookName) {
		String src = getSrc(bookName);
		insertBookToSQlite(book.name, book.autor, src, book.url);
	}

	public void saveIcon(byte[] bytes, String bookName) {

		File f = new File(Const.saveiconpath);
		if (!f.exists())
			f.mkdirs();

		// 本地地址
		String src = getSrc(bookName);

		// 存到本地
		File fe = new File(src);
		if (!fe.exists()) {
			try {
				fe.createNewFile();
				FileOutputStream fw = new FileOutputStream(fe);
				fw.write(bytes);
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public byte[] getBytes(String src) {
		byte[] bytes = null;
		// 类 URL 代表一个统一资源定位符，它是指向互联网“资源”的指针。
		URL url;
		try {
			url = new URL(src);
			// 每个 HttpURLConnection 实例都可用于生成单个请求，
			// 但是其他实例可以透明地共享连接到 HTTP 服务器的基础网络
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			InputStream stream = conn.getInputStream();
			try {
				bytes = readInputStream(stream);
				if (book != null)
					saveIcon(bytes, book.name);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bytes;
	}

	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[2044];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
			outStream.flush();
		}
		inStream.close();
		return outStream.toByteArray();
	}

	public SearchService(Context c) {
		search = new Search();
		this.mContext = c;
	}

	static final String baseUrl = "http://www.biquge.la/book/";

	public void close() {
		if (search != null) //
		{
			search.destory();
			search = null;
		}
	}

	public void get(final String url, final Handler h) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Book temp;
				try {
					temp = search.getBook(url);
				} catch (IOException e) {
					e.printStackTrace();
					h.obtainMessage(0).sendToTarget();
					return;
				}
				h.obtainMessage(1, temp).sendToTarget();
				book = temp;
			}
		}).start();
	}

	/**
	 * 
	 * @param key
	 *            搜索关键字
	 */
	public void search(final String key, final Handler h) {
		if (!lock) {
			lock = true;
			new Thread(new Runnable() {
				@Override
				public void run() {
					List<Book> list = null;
					try {
						list = search.search(SEARCH, key);
					} catch (IOException e) {
						e.printStackTrace();
						h.obtainMessage(0).sendToTarget();
					}
					h.obtainMessage(1, list).sendToTarget();
				}
			}).start();
		}
	}

	public void unlock() {
		lock = false;
	}

}
