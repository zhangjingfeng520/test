package com.example.tool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.example.domain.Info;

public class DirectoryUtils {

	/**
	 * 获取小说目录
	 */
	public static List<Info> getDirectory(String url) {
		final List<Info> list = new ArrayList<Info>();
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (doc != null) {
			Element ele = doc.getElementById("list");

			Elements es = ele.select("dt");
			ListIterator<Element> links;

			if (es.size() > 1)
				links = ele.getElementsByTag("a").listIterator(9);
			else
				links = ele.getElementsByTag("a").listIterator();

			while (links.hasNext()) {
				Element link = links.next();
				Info info = new Info();
				info.url = link.attr("abs:href");
				info.text = link.text();
				list.add(info);
			}
		}
		return list;
	}

	/**
	 * 获取小说目录
	 */
	public static List<Info> getDirectory(Document doc) {
		final List<Info> list = new ArrayList<Info>();
		if (doc != null) {
			Element ele = doc.getElementById("list");
			ListIterator<Element> links;

			Elements es = ele.select("dt");

			if (es.size() > 1)
				links = ele.getElementsByTag("a").listIterator(9);
			else
				links = ele.getElementsByTag("a").listIterator();
			// 获取网页所有的超链接
			// Elements links = doc.select("a[href]");
			while (links.hasNext()) {
				Element link = links.next();
				Info info = new Info();
				info.url = link.attr("abs:href");
				info.text = link.text();
				list.add(info);
			}
		}
		return list;
	}
}
