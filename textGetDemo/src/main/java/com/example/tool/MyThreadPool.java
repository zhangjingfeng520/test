package com.example.tool;

import java.util.ArrayList;
import java.util.List;

import com.example.domain.BaseTask;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * ����android ����Ϣ������ʵ�� ����UI�̺߳����̶߳Թ�����Դ��ͬʱ����
 * 
 * @author DYL
 * 
 */
@SuppressLint("HandlerLeak")
public final class MyThreadPool implements Runnable {

	private final Thread mThread;
	private Handler mHandler; // ��Ϣ����
	private boolean mQuit = false; // �Ƿ��˳�
	private List<BaseTask> tasks;

	static final String TAG = "MyThreadPool";

	public MyThreadPool() {
		mThread = new Thread(this);
		mThread.start();
		tasks = new ArrayList<BaseTask>();
	}

	/**
	 * UI�߳�
	 */
	public void removeAll() {
		if (mHandler != null)
			mHandler.removeMessages(1);
	}

	@Override
	public void run() {
		Looper.prepare();
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1: {
					BaseTask r = (BaseTask) msg.obj;
					if (r != null)
						r.run();
				}
					break;
				default:
					break;
				}
			}
		};
		Looper.loop();
	}

	/**
	 * UI�߳� �������
	 */
	public void execute(BaseTask r) {
		if (!mQuit) {
			if (mHandler != null) {
				if (!mHandler.hasMessages(1, r)) {
					mHandler.obtainMessage(1, r).sendToTarget();
					tasks.add(r);
				} else
					Log.w(TAG, "hasMessage");
			} else
				Log.e(TAG, "Handler = null");
		} else
			Log.e(TAG, "have Quit");
	}

	private void clear() {
		for (int i = 0; i < tasks.size(); i++)
			tasks.remove(i).mQuit = true;
	}

	public void shutdown() {
		if (!mQuit) {
			mQuit = true;
			if (mThread != null) {
				if (mHandler != null) {
					mHandler.getLooper().quit();
					mHandler.removeMessages(1);
					clear();
				}
				if (Thread.currentThread() != mThread) {
					if (mThread.isAlive())
						try {
							mThread.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
				}
			}
		}
	}
}