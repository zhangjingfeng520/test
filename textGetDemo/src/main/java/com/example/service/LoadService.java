package com.example.service;

import com.example.domain.BaseTask;
import com.example.tool.ThreadUtils;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class LoadService extends Service {

	private ThreadUtils threadUtils;
	private Mybinder mybinder;
	private int bindCount;

	public final class Mybinder extends Binder {
		public LoadService getService() {
			return LoadService.this;
		}
	}

	static final String TAG = "LoadService";

	@Override
	public IBinder onBind(Intent intent) {
		bindCount++;
		return mybinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		bindCount--;
		if (bindCount == 0) {
			stopSelf();
			Log.v(TAG, "·þÎñÏú»Ù");
		}
		return super.onUnbind(intent);
	}

	public void Load(BaseTask r, boolean isRemollc) {
		if (isRemollc)
			Log.i(TAG, "remollc");
		threadUtils.exe(r);
	}

	@Override
	public void onCreate() {
		threadUtils = new ThreadUtils();
		mybinder = new Mybinder();
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		threadUtils.shut();
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

}
