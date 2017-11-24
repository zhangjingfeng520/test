package com.example.myapplication.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.bean.Info;

import java.util.List;

/**
 * Created by Administrator on 2017/10/31.
 */
public class MyArrayAdapter extends ArrayAdapter<Info>{
    private List<Info> infoList;
    private Context context;
    private int resourceId;

    public MyArrayAdapter(@NonNull Context context, @LayoutRes int resourceId, @NonNull List<Info> infoList) {
        super(context, resourceId, infoList);
        this.infoList=infoList;
        this.context=context;
        this.resourceId=resourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null) {
            holder=new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(resourceId, parent, false);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            holder.textView = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.imageView.setImageResource(infoList.get(position).getImgID());
        holder.textView.setText(infoList.get(position).getName());

        return convertView;

    }
    class  ViewHolder{
        ImageView imageView;
        TextView textView;
    }
}