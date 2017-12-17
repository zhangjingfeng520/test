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
        //去除默认指示器
        expandableListView.setGroupIndicator(null);

    }

    /**
     * 初始化数据
     */
    private void getData() {
        String[] strings=new String[]{"家人","朋友","好友","公司","同学"};
        colleges = new ArrayList<>();
        for(int j=0;j<5;j++) {

            College college = new College();

            college.name = strings[j];

            List<Classes> classesList = new ArrayList<>();

            for (int i = 1; i < 3; i++) {
                Classes classes = new Classes();

                classes.name = j+"计算机" + i + "班";

                List<String> list = new ArrayList<>();

                list.add("mm");
                list.add("dd");
                classes.students = list;

                classesList.add(classes);
            }

            college.classList = classesList;
            colleges.add(college);
        }

    }

}
