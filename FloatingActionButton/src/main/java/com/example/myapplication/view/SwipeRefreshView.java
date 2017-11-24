package com.example.myapplication.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.myapplication.R;

/**
 * Created by Administrator on 2017/11/7.
 */

public class SwipeRefreshView extends SwipeRefreshLayout {

    private final View mFooterView;
    private final int mScaledTouchSlop;

    private ListView mListView;
    private RecyclerView mRecyclerView;
    private int mItemCount;
    private boolean isLoading;

    private static final String TAG = "SwipeRefreshView";
    private OnLoadMoreListener mListener;
    private int lastItem;

    public SwipeRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 填充底部加载布局
        mFooterView = View.inflate(context, R.layout.view_footer, null);
        // 表示控件移动的最小距离，手移动的距离大于这个距离才能拖动控件
        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mListView == null || mRecyclerView == null) {
            if (getChildCount() > 0) {
                if (getChildAt(0) instanceof ListView) {
                    mListView = (ListView) getChildAt(0);
                    setListViewOnScroll();
                } else if (getChildAt(0) instanceof RecyclerView) {
                    mRecyclerView = (RecyclerView) getChildAt(0);
                    setRecyclerViewOnScroll();
                }

            }
        }
    }

    //设置RecyclerView的滑动监听
    private void setRecyclerViewOnScroll() {
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 移动过程中判断时候能下拉加载更多
                if (canLoadMore()) {
                    //加载数据
                    loadData();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastItem=((LinearLayoutManager)mRecyclerView.getLayoutManager()).findLastVisibleItemPosition();
            }
        });

    }

    //设置ListView的滑动监听
    private void setListViewOnScroll() {
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                // 移动过程中判断时候能下拉加载更多
                if (canLoadMore()) {
                    //加载数据
                    loadData();
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
    }

    /**
     * 在分发事件的时候处理子控件的触摸事件
     */
    private float mDownY, mUpY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                // 移动过程中判断时候能下拉加载更多
                if (canLoadMore()) {
                    //加载数据
                    loadData();
                }
                break;
            case MotionEvent.ACTION_UP:
                mUpY = ev.getY();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void loadData() {
        if (mListView != null||mRecyclerView!=null) {
            // 设置加载状态，让布局显示出来
            setLoading(true);
            mListener.onLoadMore();
        }
    }

    public void setLoading(boolean loading) {
        //修改当前的状态
        isLoading = loading;
        if (isLoading) {
            // 显示布局
            mListView.addFooterView(mFooterView);
        } else {
            // 隐藏布局
            mListView.removeFooterView(mFooterView);
            // 重置滑动的坐标
            mDownY = 0;
            mUpY = 0;
        }
    }

    /**
     * 判断是否满足加载更多条件
     */
    private boolean canLoadMore() {
        // 1. 是上拉状态
        boolean condition1 = (mDownY - mUpY) >= mScaledTouchSlop;
        if (condition1) {
            Log.d(TAG, "canLoadMore: 上拉状态");
        }
        // 2. 当前页面可见的item是最后一个条目,一般最后一个条目位置需要大于第一页的数据长度
        boolean condition2 = false;
        if (mListView != null && mListView.getAdapter() != null) {
            if (mItemCount > 0) {
                if (mListView.getAdapter().getCount() < mItemCount) {
                    // 第一页未满，禁止下拉
                    condition2 = false;
                } else {
                    condition2 = mListView.getLastVisiblePosition() == (mListView.getAdapter().getCount() - 1);
                }
            } else {
                // 未设置数据长度，则默认第一页数据不满时也可以上拉
                condition2 = mListView.getLastVisiblePosition() == (mListView.getAdapter().getCount() - 1);
            }
        }
        if (condition2) {
            Log.d(TAG, "canLoadMore: 是最后一个");
        }

        boolean condition3 = !isLoading;
        if (condition2) {
            Log.d(TAG, "canLoadMore: 不是在加载状态");
        }
        return condition1 && condition2 && condition3;
    }

    //上拉回调
    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setOnLoadMoreListerner(OnLoadMoreListener onLoadMoreListerner) {
        this.mListener = onLoadMoreListerner;

    }
    //开启上拉最少条数
    public void setItemCount(int itemCount) {
        this.mItemCount = itemCount;
    }


}
