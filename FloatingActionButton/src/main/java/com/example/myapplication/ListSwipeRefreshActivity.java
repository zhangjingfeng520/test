package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.myapplication.view.SwipeRefreshView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/8.
 */

public class ListSwipeRefreshActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private Toolbar toolbar;
    private ListView listView;
    private List<String> list;
    ArrayAdapter arrayAdapter;
    private SwipeRefreshView swipeRefreshView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_swipe_refresh);
        initData();
        initView();
    }

    private void initData() {
        list = new ArrayList<String>();
        list.add("1");
        list.add("1");
        list.add("1");

    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //
        swipeRefreshView=(SwipeRefreshView)findViewById(R.id.list_swipe_refresh_view);
        swipeRefreshView.setEnabled(true);
        swipeRefreshView.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshView.setOnRefreshListener(this);
        swipeRefreshView.setItemCount(8);
        swipeRefreshView.setOnLoadMoreListerner(new SwipeRefreshView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        list.add("上拉刷新");
                        arrayAdapter.notifyDataSetChanged();
                        swipeRefreshView.setLoading(false);
                    }
                },1200);
            }
        });
        //
        listView = (ListView) findViewById(R.id.list);
        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,list);
        listView.setAdapter(arrayAdapter);

    }

    @Override
    public void onRefresh() {
        swipeRefreshView.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                list.add("下拉刷新");
                arrayAdapter.notifyDataSetChanged();
                swipeRefreshView.setRefreshing(false);
            }
        },1200);
    }

}
