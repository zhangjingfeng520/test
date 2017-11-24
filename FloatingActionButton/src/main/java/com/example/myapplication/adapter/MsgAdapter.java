package com.example.myapplication.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.bean.Msg;

import java.util.List;

/**
 * Created by Administrator on 2017/11/2.
 */

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.MyHolder> {

    private List<Msg> msgList;

    public MsgAdapter(List<Msg> msgList) {
        this.msgList = msgList;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        Msg msg = msgList.get(position);
        if (msg.getType() == Msg.TYPE_RECEIVED) {//接收
            holder.leftMsg.setText(msg.getContent());
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLatout.setVisibility(View.GONE);

        } else if (msg.getType() == Msg.TYPE_SEND) {//发送
            holder.rightMsg.setText(msg.getContent());
            holder.rightLatout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return msgList == null ? 0 : msgList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private LinearLayout leftLayout, rightLatout;
        TextView leftMsg, rightMsg;

        public MyHolder(View itemView) {
            super(itemView);
            leftLayout=(LinearLayout)itemView.findViewById(R.id.left_layout);
            rightLatout=(LinearLayout)itemView.findViewById(R.id.right_layout);
            leftMsg=(TextView)itemView.findViewById(R.id.msg_left);
            rightMsg=(TextView)itemView.findViewById(R.id.msg_right);
        }
    }
}
