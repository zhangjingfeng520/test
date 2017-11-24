package com.example.myapplication.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.bean.Info;

import java.util.List;

/**
 * Created by Administrator on 2017/11/1.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Info> infoList;
    private int resourceId;
    private final static int TYPE_ITEM = 0;//普通Item
    private final static int TYPE_FOOTER = 1;//FooterView
    public final static int OVER = 0;//
    public final static int LOADING = 1;//
    private int load_more_status = 0;

    private int count;
    private int dataCount;
    public void setCount(int count) {
        this.count = count;
    }
    public MyRecyclerAdapter(List<Info> infoList, int resourceId) {
        this.infoList = infoList;
        this.resourceId = resourceId;
        this.dataCount=(infoList==null?0:infoList.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(resourceId, parent, false);
            MyHolder myHolder = new MyHolder(view);
            return myHolder;
        } else if (viewType == TYPE_FOOTER) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_footer, parent, false);
                return new FooterHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {

        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else
            return TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder!=null&&holder instanceof MyHolder) {
            MyHolder myHolder = (MyHolder) holder;
            myHolder.imageView.setImageResource(infoList.get(position).getImgID());
            myHolder.textView.setText(infoList.get(position).getName());
        }
        else if (holder!=null&&holder instanceof FooterHolder) {
            FooterHolder footerHolder = (FooterHolder) holder;
//            footerHolder.mLinearLayout.setVisibility(View.GONE);
            switch (load_more_status) {
                case LOADING:
                    footerHolder.mLinearLayout.setVisibility(View.VISIBLE);
                    footerHolder.mProgressBar.setVisibility(View.VISIBLE);
                    footerHolder.mTextView.setText("正在加载...");
                    break;
                case OVER:
//                    footerHolder.mLinearLayout.setVisibility(View.GONE);
                    footerHolder.mProgressBar.setVisibility(View.GONE);
                    footerHolder.mTextView.setText("上拉加载更多...");
                    break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return infoList == null ? 0 : infoList.size()+1;
    }


    class MyHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;

        public MyHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.icon);
            textView = (TextView) itemView.findViewById(R.id.name);
        }
    }

    private class FooterHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;
        private ProgressBar mProgressBar;
        private LinearLayout mLinearLayout;

        public FooterHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.load_tv);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.load_progress);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.view_footer_ll);
        }
    }

    //下拉刷新
    public void downRefresh(List<Info> mList) {
        mList.addAll(infoList);
        infoList.clear();
        infoList.addAll(mList);
        dataCount=infoList.size();
        notifyItemInserted(0);
    }

    //修改更新状态
    public void changeMoreStatus(int status) {
        load_more_status = status;
        notifyDataSetChanged();
    }
}
