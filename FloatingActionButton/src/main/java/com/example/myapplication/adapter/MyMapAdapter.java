package com.example.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.myapplication.bean.MyMap;

import java.util.List;

/**
 * Created by Administrator on 2017/11/7.
 */

public class MyMapAdapter extends RecyclerView.Adapter<MyMapAdapter.MyMapHolder> {
    private Context context;
    private List<MyMap> myMapList;
    private onMyClickItemListener mListener;

    public MyMapAdapter(Context context, List<MyMap> myMapList) {
        this.context = context;
        this.myMapList = myMapList;
    }

    @Override
    public MyMapAdapter.MyMapHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_map, parent,false);
        MyMapHolder myMapHolder = new MyMapHolder(view);
        return myMapHolder;
    }

    @Override
    public void onBindViewHolder(MyMapAdapter.MyMapHolder holder, final int position) {
        holder.mButton.setText(myMapList.get(position).getKey());
        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onMyClickItem(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return myMapList == null ? 0 : myMapList.size();
    }

    public class MyMapHolder extends RecyclerView.ViewHolder {
        private Button mButton;

        public MyMapHolder(View itemView) {
            super(itemView);
            mButton = (Button) itemView.findViewById(R.id.map_button);
        }
    }

    public interface onMyClickItemListener {
        void onMyClickItem(int position);
    }

    public void setMyClickItemListener(onMyClickItemListener myClickItemListener) {
        mListener = myClickItemListener;
    }
}
