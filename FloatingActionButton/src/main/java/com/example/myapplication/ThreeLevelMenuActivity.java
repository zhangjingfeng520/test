package com.example.myapplication;

import android.util.Log;
import android.widget.ExpandableListView;

import com.example.myapplication.adapter.ThreeLevelMenuAdapter;
import com.example.myapplication.base.BaseActivity;
import com.example.myapplication.bean.Classes;
import com.example.myapplication.bean.College;

import java.util.ArrayList;
import java.util.List;

public class ThreeLevelMenuActivity extends BaseActivity {

    private ExpandableListView expandableListView;
    private List<College> colleges;
    private static final String TAG = "ThreeLevelMenuActivity";

    @Override
    public int getLayoutId() {
        return R.layout.activity_three_level_menu;
    }

    @Override
    public void initData() {
        setToolbarTitle("三级分组菜单");
    }

    @Override
    public void initView() {
        getData();
        expandableListView = (ExpandableListView) findViewById(R.id.three_level_menu);
        expandableListView.setAdapter(new ThreeLevelMenuAdapter(colleges, this));
        Log.d(TAG, "initView: "+colleges.toString());

    }

    /**
     * 初始化数据
     */
    private void getData() {

        College college = new College();

        college.name = "科技大学";

        List<Classes> classesList = new ArrayList<>();

        for (int i = 1; i < 3; i++) {
            Classes classes = new Classes();

            classes.name = "计算机" + i + "班";

            List<String> list = new ArrayList<>();

            list.add("mm");
            list.add("dd");
            classes.students = list;

            classesList.add(classes);
        }

        college.classList = classesList;


        colleges = new ArrayList<>();
        colleges.add(college);
    }

}
