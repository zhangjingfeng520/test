package com.example.common;

import android.app.Application;
import android.content.Intent;
import com.example.service.LoadService;
import com.example.service.SearchService;

public class MyApp extends Application {

	@Override
	public void onCreate() {
		Intent service = new Intent(this, LoadService.class);
		startService(service);
		super.onCreate();
	}

	final SearchService mSearchService = new SearchService(this);

	public SearchService getmSearchService() {
		return mSearchService;
	}

}
