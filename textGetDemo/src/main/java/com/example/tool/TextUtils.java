package com.example.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.example.domain.Info;

public class TextUtils {

	Context mContext;

	public TextUtils(Context c) {
		this.mContext = c;
	}

	public static final String TXT_PATH = Const.savebookpath + File.separator;

	public static String getTxtPath(String book) {
		return TXT_PATH + book + ".txt";
	}

	private void save(String title, String text, String bookName) {
		File file = new File(TXT_PATH);

		if (!file.exists()) {
			file.mkdirs();
		}

		file = new File(getTxtPath(bookName));

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Log.e("save", "title:" + title);

		try {
			FileWriter fo = new FileWriter(file, true);
			fo.write("\n\n" + "     " + title + "\n\n");
			fo.write(text);
			fo.flush();
			fo.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getContent(Info info, String bookName, Handler h) {
		File f = new File(Const.savebookpath + File.separator + bookName
				+ ".txt");
		if (f.exists()) {
			StringBuffer sb = new StringBuffer();
			try {
				BufferedReader br = new BufferedReader(new FileReader(f));
				String str = null;
				while ((str = br.readLine()) != null) {
					sb.append(str);
				}
				br.close();
				h.obtainMessage(-1, sb.toString()).sendToTarget();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else
			getText(h, info, bookName);
	}

	public void loadText(Handler h, Info info, String name) {
		getText(h, info, name);
	}

	private void getText(Handler h, Info info, String book) {
		Document doc = null;
		String text = null;
		try {
			doc = Jsoup.connect(info.url).get();
		} catch (IOException e1) {
			e1.printStackTrace();
			getText(h, info, book);
		}
		if (doc != null) // ..
		{
			Element e = doc.getElementById("content");
			text = e.text();
			save(info.text, text, book);
		}
		if (h != null)
			h.obtainMessage(1, text).sendToTarget();
	}
}
