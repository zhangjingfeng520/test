package com.example.viewpage;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private List<Fragment> mList;
    private FirstFragment firstFragment;
    private SecondFragment secondFragment;
    private MyPagerAdapter myPagerAdapter;
    private String[] titles;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_page);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        mTabLayout = (TabLayout) findViewById(R.id.mTabLayout);
        mTabLayout.setupWithViewPager(mViewPager);

        toolbar = (Toolbar)findViewById(R.id.mToolbar);
        //标题
        toolbar.setTitle("ViewPager");
        //返回
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        initPager();

    }

    private void initPager() {
        mList = new ArrayList<Fragment>();
        firstFragment = new FirstFragment();
        secondFragment = new SecondFragment();
        mList.add(firstFragment);
        mList.add(secondFragment);
        titles=new String[]{"1","2"};
        myPagerAdapter=new MyPagerAdapter(getSupportFragmentManager(),mList,titles);
        mViewPager.setAdapter(myPagerAdapter);

    }

    public static class FirstFragment extends Fragment {
        public FirstFragment() {
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View mView = inflater.inflate(R.layout.first_fragment_layout, container, false);
            TextView mTextView = (TextView) mView.findViewById(R.id.mTextView);

            return mView;

        }
    }

    public static class SecondFragment extends Fragment {
        public SecondFragment() {
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View mView = inflater.inflate(R.layout.second_fragment_layout, container, false);
            TextView mTextView = (TextView) mView.findViewById(R.id.mTextView);

            return mView;

        }
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mList;
        private String[] titles;

        public MyPagerAdapter(FragmentManager fm, List<Fragment> list,String[] titles) {
            super(fm);
            mList = list;
            this.titles=titles;
        }

        @Override
        public Fragment getItem(int position) {
            return mList == null ? null : mList.get(position);
        }

        @Override
        public int getCount() {
            return mList == null ? 0 : mList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
