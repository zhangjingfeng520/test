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
		// 设置WebView属性，能够执行Javascript脚本
		helper.getSettings().setJavaScriptEnabled(true);
		// 设置可以支持缩放
		helper.getSettings().setSupportZoom(true);
		// 设置默认缩放方式尺寸是far
		helper.getSettings().setDefaultZoom(
				android.webkit.WebSettings.ZoomDensity.FAR);
		// 设置出现缩放工具
		helper.getSettings().setBuiltInZoomControls(true);
		// 自适应屏幕
		helper.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		// 加载需要显示的网页
		helper.loadUrl("http://www.open-open.com/jsoup/");
		// 设置Web视图
		helper.setWebViewClient(new HelloWebViewClient());
	}

	// Web视图
	private class HelloWebViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
}
