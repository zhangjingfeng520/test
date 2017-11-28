package com.example.myapplication;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.myapplication.adapter.BooksByCatsAdapter;
import com.example.myapplication.api.ApiHelper;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.base.BaseActivity;
import com.example.myapplication.bean.jsonbean.BooksByCats;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BooksByCatsActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ApiService apiService;

    private List<BooksByCats.BooksBean> mlist=new ArrayList<BooksByCats.BooksBean>();
    private static final String TAG = "BooksByCatsActivity";
    private int lastVisibleItem;
    private int limit = 20;
    private int start=0;
    private BooksByCatsAdapter booksByCatsAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public int getLayoutId() {
        return R.layout.activity_books_by_cats;
    }

    @Override
    public void initData() {
        apiService = ApiHelper.getInstance(this).getService();
    }

    @Override
    public void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        booksByCatsAdapter = new BooksByCatsAdapter(BooksByCatsActivity.this, mlist);
        recyclerView.setAdapter(booksByCatsAdapter);
//        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_view);
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(this);
        getBooksByCats();

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (lastVisibleItem == recyclerView.getAdapter().getItemCount() - 1 &&
                        recyclerView.getAdapter().getItemCount() > limit - 1) {
                   booksByCatsAdapter.changeMoreStatus(BooksByCatsAdapter.LOADING);
                    start=booksByCatsAdapter.getItemCount();
                    getBooksByCats();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (linearLayoutManager instanceof LinearLayoutManager) {
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    @Override
    public void onRefresh() {
        start=0;
        swipeRefreshLayout.setRefreshing(true);
        getBooksByCats();
    }

    public void getBooksByCats() {
        Subscription subscription = apiService.getBooksByCats("male", "hot", "玄幻", "东方玄幻", start, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<com.example.myapplication.bean.jsonbean.BooksByCats>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(com.example.myapplication.bean.jsonbean.BooksByCats booksByCats) {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d(TAG, "onNext: " + new Gson().toJson(booksByCats));
                        mlist = new ArrayList<BooksByCats.BooksBean>();
                        mlist = booksByCats.getBooks();
                        if(start==0){
                            booksByCatsAdapter.setData(mlist);
                        }else {
                            booksByCatsAdapter.addData(mlist);
                        }
                    }
                });
        addSubscribe(subscription);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: 2222");
    }

}
