package com.example.textgetdemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class MySelfActivity extends Activity {

	WebView helper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myself);
		helper = (WebView) findViewById(R.id.web);
		// ����WebView���ԣ��ܹ�ִ��Javascript�ű�
		helper.getSettings().setJavaScriptEnabled(true);
		// ���ÿ���֧������
		helper.getSettings().setSupportZoom(true);
		// ����Ĭ�����ŷ�ʽ�ߴ���far
		helper.getSettings().setDefaultZoom(
				android.webkit.WebSettings.ZoomDensity.FAR);
		// ���ó������Ź���
		helper.getSettings().setBuiltInZoomControls(true);
		// ����Ӧ��Ļ
		helper.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		// ������Ҫ��ʾ����ҳ
		helper.loadUrl("http://www.open-open.com/jsoup/");
		// ����Web��ͼ
		helper.setWebViewClient(new HelloWebViewClient());
	}

	// Web��ͼ
	private class HelloWebViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
}
