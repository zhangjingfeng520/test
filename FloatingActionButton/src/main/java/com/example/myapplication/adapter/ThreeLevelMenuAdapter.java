package com.example.myapplication.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.bean.College;
import com.example.myapplication.view.CustomExpandableListView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Administrator on 2017/12/15.
 */

public class ThreeLevelMenuAdapter extends BaseExpandableListAdapter {
    private List<College> colleges;
    private Context context;

    public ThreeLevelMenuAdapter(List<College> colleges, Context context) {
        this.colleges = colleges;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return colleges == null ? 0 : colleges.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return colleges.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return colleges.get(i).classList.get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        GroupViewHolder groupViewHolder;
        if (view == null) {
            groupViewHolder = new GroupViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.group_view_layout, viewGroup, false);
            groupViewHolder.group_tv = (TextView) view.findViewById(R.id.group_tv);
            groupViewHolder.group_img = (ImageView) view.findViewById(R.id.group_img);
            view.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) view.getTag();
        }
        groupViewHolder.group_tv.setText(colleges.get(i).name);
        if (b) {
            groupViewHolder.group_img.setImageResource(R.mipmap.ic_collpase);
        } else
            groupViewHolder.group_img.setImageResource(R.mipmap.ic_expand);
        return view;
//        return getGenericView(colleges.get(i).name);
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        return getGenericExpandableListView(colleges.get(i));
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    private TextView getGenericView(String string) {
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView textView = new TextView(context);
        textView.setLayoutParams(layoutParams);

        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);

        textView.setPadding(100, 20, 0, 20);
        textView.setText(string);
//        textView.setTextColor(Color.RED);
        return textView;
    }


    /**
     * 返回子ExpandableListView 的对象  此时传入的是该大学下所有班级的集合。
     *
     * @param college
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public ExpandableListView getGenericExpandableListView(College college) {
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        CustomExpandableListView view = new CustomExpandableListView(context);

        // 加载班级的适配器
        ThreeLevelMenuSonAdapter adapter = new ThreeLevelMenuSonAdapter(college.classList, context);

        view.setAdapter(adapter);

        view.setPadding(20, 0, 0, 0);
        return view;
    }

    static class GroupViewHolder {
        TextView group_tv;
        ImageView group_img;
    }

    static class ChildViewHolder {
        TextView child_tv;
    }
}
