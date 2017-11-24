package com.example.tool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.example.domain.Record;

/*
 * Ê×Ò³ÍÆ¼ö 
 */
public class Recommend {

	static final String HOME_URL = "http://www.biquge.com/";

	Document doc = null;

	public List<Record> getHots() {
		List<Record> list = new ArrayList<Record>();
		if (doc == null)
			try {
				doc = Jsoup.connect(HOME_URL).get();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		if (doc != null) {
			Elements es = doc.select("div.item");
			for (Element element : es) {
				Record record = new Record();
				Element e = element.select("img").first();
				record.src = e.attr("src");
				record.autor = element.select("span").first().text();
				Element e1 = element.getElementsByTag("dt").first().select("a")
						.first();
				record.url = e1.absUrl("href");
				record.name = e1.text();
				list.add(record);
			}
		}
		return list;
	}

	public List<Record> getMinors() {
		List<Record> list = new ArrayList<Record>();
		if (doc == null)
			try {
				doc = Jsoup.connect(HOME_URL).get();
			} catch (IOException e2) {
				e2.printStackTrace();
			}

		if (doc != null) {
			Elements es = doc.select("div.top");

			for (Element element : es) {

				Record record = new Record();
				Element e = element.select("img").first();
				record.src = e.attr("src");

				Element e1 = element.getElementsByTag("dt").first().select("a")
						.first();
				record.url = e1.absUrl("href");
				record.name = e1.text();
				list.add(record);
			}
		}
		return list;
	}

	public List<Record> getList() {
		if (doc == null)
			try {
				doc = Jsoup.connect(HOME_URL).get();
			} catch (IOException e2) {
				e2.printStackTrace();
			}

		List<Record> list = new ArrayList<Record>();
		Element et = doc.getElementById("newscontent");
		Elements es = et.select("li");

		for (Element element : es) {
			Record record = new Record();
			Element e = element.select("a").first();
			record.url = e.absUrl("href");
			record.name = e.text();
			Elements ets = element.select("span");

			record.sort = ets.first().text();
			int size = ets.size();
			Element e2;
			if (3 >= size) {
				e2 = ets.get(2);
			} else {
				e2 = ets.get(3);
			}
			if (e2 != null)
				record.autor = e2.text();
			list.add(record);
		}
		return list;
	}
}
