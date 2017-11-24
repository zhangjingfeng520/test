package com.example.domain;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

import com.example.tool.Const;
import com.example.tool.DirectoryUtils;

public class Search {

	private List<Info> records; // 小说目录
	private Document doc = null;

	public Book getBook(String url) throws IOException {
		final Book book = new Book();
		book.url = url;
		doc = Jsoup.connect(url).get();
		if (doc != null) //
		{
			Element e0 = doc.getElementsByClass("box_con").first();
			Element e1 = e0.getElementById("maininfo");
			Element e2 = e1.getElementById("info");
			book.name = e2.select("h1").get(0).text();
			Elements e3 = e2.select("p");
			book.autor = e3.get(0).text();
			book.lastUpdateTime = e3.get(2).text();
			book.description = e1.getElementById("intro").select("p").get(0)
					.text();
			book.src = e0.getElementById("sidebar").getElementById("fmimg")
					.select("img").first().absUrl("src");
			// 保存目录到本地
			saveRecords(book.name);
		}
		return book;
	}

	public void saveRecords(String key) {
		records = DirectoryUtils.getDirectory(doc);
		final String base = Const.saverecordpath;
		File f = new File(base);
		if (!f.exists())
			f.mkdirs();
		File fe = new File(base + File.separator + key + ".txt");
		if (!fe.exists())
			try {
				if (fe.createNewFile()) //
				{
					FileWriter fw = new FileWriter(fe);
					BufferedWriter bw = new BufferedWriter(fw);

					for (Info info : records) {
						bw.write(info.url + "///" + info.text);
						bw.newLine();
					}
					bw.close();
					fw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	/**
	 * 获取目录
	 */
	public List<Info> getDirectory() {
		if (records != null)
			return records;
		if (doc != null)
			return DirectoryUtils.getDirectory(doc);
		return null;
	}

	public void destory() {
		doc = null;
		if (records != null) {
			records.clear();
			records = null;
		}
	}

	public List<Book> search(String search, String key) throws IOException {
		Document doc = Jsoup.connect(search).get();
		List<Book> list = new ArrayList<Book>();
		if (doc != null) {
			Elements es = doc.getElementById("main").select("li");
			for (Element element : es) {
				String t = element.ownText();
				String autor = t.substring(t.indexOf("/") + 1);
				Element e = element.child(0);
				String bookName = e.text();
				Log.v("TAG", "autor:" + autor + "bookName:" + bookName);
				if (bookName.contains(key)) {
					Book book = new Book();
					book.autor = autor;
					book.url = e.absUrl("href");
					book.name = bookName;
					list.add(book);
				} else if (autor.contains(key)) {
					Book book = new Book();
					book.autor = autor;
					book.url = e.absUrl("href");
					book.name = bookName;
					list.add(book);
				}
			}
		}
		return list;
	}
}
