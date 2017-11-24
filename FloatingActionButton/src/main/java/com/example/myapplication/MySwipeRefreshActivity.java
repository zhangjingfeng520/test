package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.myapplication.adapter.MyRecyclerAdapter;
import com.example.myapplication.bean.Info;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/7.
 */

public class MySwipeRefreshActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private Toolbar toolbar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private List<Info> infoList;
    private MyRecyclerAdapter myRecyclerAdapter;
    private int lastVisibleItem;
    private LinearLayoutManager llm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_refresh);
        initData();
        initView();
    }

    private void initData() {
        infoList = new ArrayList<Info>();
        infoList.addAll(Info.infos);
        infoList.add(new Info(R.mipmap.hong, "1"));
        infoList.add(new Info(R.mipmap.hong, "2"));
        infoList.add(new Info(R.mipmap.hong, "3"));
        infoList.add(new Info(R.mipmap.hong, "3"));
        infoList.add(new Info(R.mipmap.hong, "3"));
        infoList.add(new Info(R.mipmap.hong, "3"));
        infoList.add(new Info(R.mipmap.hong, "3"));
        infoList.add(new Info(R.mipmap.hong, "3"));
        infoList.add(new Info(R.mipmap.hong, "3"));
        infoList.add(new Info(R.mipmap.hong, "3"));

    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        //
        mRecyclerView = (RecyclerView) findViewById(R.id.refresh_recycler_view);
        llm = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(llm);
        myRecyclerAdapter = new MyRecyclerAdapter(infoList, R.layout.list_item);
        myRecyclerAdapter.setCount(15);
        mRecyclerView.setAdapter(myRecyclerAdapter);
        //添加分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState== RecyclerView.SCROLL_STATE_IDLE&&lastVisibleItem==myRecyclerAdapter.getItemCount()-1&&
                        myRecyclerAdapter.getItemCount()>15){
                    myRecyclerAdapter.changeMoreStatus(myRecyclerAdapter.LOADING);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            infoList.add(new Info(R.mipmap.hong, "上拉刷新"));
                            myRecyclerAdapter.changeMoreStatus(myRecyclerAdapter.OVER);
                        }
                    },1200);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(llm instanceof LinearLayoutManager) {
                    lastVisibleItem = llm.findLastVisibleItemPosition();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<Info> mlist = new ArrayList<Info>();
                mlist.add(new Info(R.mipmap.lan, "2"));
//                myRecyclerAdapter.notifyDataSetChanged();
//                myRecyclerAdapter.notifyItemInserted(infoList.size()-1);
//                mRecyclerView.scrollToPosition(infoList.size()-1);
                myRecyclerAdapter.downRefresh(mlist);
                mRecyclerView.scrollToPosition(0);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }

    private static final String TAG = "MySwipeRefreshActivity";
}
