package com.example.textgetdemo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity {

	LocalActivityManager manager;
	private RadioGroup radioGroup;
	private ViewPager mPager;
	// Tab页面列表
	private List<View> listViews;

	public static final class MyPagerAdapter extends PagerAdapter {

		private List<View> lists;

		public MyPagerAdapter(List<View> mListViews) {
			this.lists = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(lists.get(arg1));

		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return lists.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(lists.get(arg1), 0);
			return lists.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// 禁止方法在Activity 的onCreate方法中使用：
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		manager = new LocalActivityManager(this, false);
		manager.dispatchCreate(savedInstanceState);
		initWidget();
	}

	private static final String index[] = { "1", "2", "3", "4" };

	private View getView(String id, Intent intent) {
		return manager.startActivity(id, intent).getDecorView();
	}

	private void initWidget() {
		mPager = (ViewPager) findViewById(R.id.viewpager_main);
		listViews = new ArrayList<View>();
		MyPagerAdapter mpAdapter = new MyPagerAdapter(listViews);
		Intent connectIntent = new Intent(this, FristActivity.class);
		listViews.add(getView(index[0], connectIntent));
		Intent sceneIntent = new Intent(this, SearChActivity.class);
		listViews.add(getView(index[1], sceneIntent));
		Intent appIntent = new Intent(this, LocalActivity.class);
		listViews.add(getView(index[2], appIntent));
		Intent myselfIntent = new Intent(this, MySelfActivity.class);
		listViews.add(getView(index[3], myselfIntent));
		mPager.setAdapter(mpAdapter);
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
		radioGroup = (RadioGroup) findViewById(R.id.view_main_radio);
		radioGroup.check(R.id.view_radio_connect);
		radioGroup.setOnCheckedChangeListener(new MyRadioCheckListener(mPager));
	}

	public static final class MyRadioCheckListener implements
			OnCheckedChangeListener {

		ViewPager pager;

		public MyRadioCheckListener(ViewPager pager) {
			this.pager = pager;
		}

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.view_radio_connect:
				pager.setCurrentItem(0);
				break;
			case R.id.view_radio_scene:
				pager.setCurrentItem(1);
				break;
			case R.id.view_radio_app:
				pager.setCurrentItem(2);
				break;
			case R.id.view_radio_myself:
				pager.setCurrentItem(3);
				break;
			default:
				break;
			}
		}

	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				radioGroup.check(R.id.view_radio_connect);
				break;
			case 1:
				radioGroup.check(R.id.view_radio_scene);
				break;
			case 2:
				radioGroup.check(R.id.view_radio_app);
				break;
			case 3:
				radioGroup.check(R.id.view_radio_myself);
			}
		}
	}

}
