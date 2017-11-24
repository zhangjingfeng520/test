package com.example.myapplication.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.myapplication.R;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2017/10/26.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    private CompositeSubscription mCompositeSubscription;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        Log.i(TAG, getClass().getSimpleName());
        initToolbar();
        initData();
        initView();
    }

    private void initToolbar() {
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Log.d(TAG, "initToolbar: 1");
    }

    public abstract int getLayoutId();
    public abstract void initData();
    public abstract void initView();

    public void setToolbarTitle(String s){
        getSupportActionBar().setTitle(s);
    }

    protected void unSubscribe(){
        if(mCompositeSubscription!=null){
            mCompositeSubscription.unsubscribe();
        }
    }
    protected void addSubscribe(Subscription subscription){
        if(mCompositeSubscription==null){
          mCompositeSubscription=new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unSubscribe();
    }
}
