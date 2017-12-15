package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.myapplication.bean.Classes;

import java.util.List;

/**
 *
 * Created by Administrator on 2017/12/15.
 */

public class ThreeLevelMenuSonAdapter extends BaseExpandableListAdapter {

    private List<Classes> classes;

    private Context context;


    public ThreeLevelMenuSonAdapter(List<Classes> classes, Context context) {
        this.classes = classes;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return classes.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return classes.get(groupPosition).students.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return classes.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return classes.get(groupPosition).students.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        return getGenericView(classes.get(groupPosition).name);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        return getGenericView(classes.get(groupPosition).students.get(childPosition));
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return true;
    }


    /**
     * 根据字符串生成布局，，因为我没有写layout.xml 所以用java 代码生成
     *
     *      实际中可以通过Inflate加载自己的自定义布局文件，设置数据之后并返回
     * @param string
     * @return
     */
    private TextView getGenericView(String string) {

        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView textView = new TextView(context);
        textView.setLayoutParams(layoutParams);

        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);

        textView.setPadding(40, 20, 0, 20);
        textView.setText(string);
        textView.setTextColor(Color.RED);
        return textView;
    }
}
