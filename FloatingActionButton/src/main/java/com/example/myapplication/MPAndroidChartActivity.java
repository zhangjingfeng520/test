package com.example.myapplication;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.myapplication.base.BaseActivity;
import com.example.myapplication.fragment.BarChartFragment;
import com.example.myapplication.fragment.LineChartFragment;
import com.example.myapplication.fragment.PieChartFragment;
import com.example.myapplication.fragment.RadarChartFragment;

public class MPAndroidChartActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {
    private BottomNavigationBar mBottomNavigationBar;
    private LineChartFragment lineChartFragment;
    private BarChartFragment barChartFragment;
    private PieChartFragment pieChartFragment;
    private RadarChartFragment radarChartFragment;
    private BadgeItem badgeItem;
    private static final String TAG = "MPAndroidChartActivity";


    @Override
    public int getLayoutId() {
        return R.layout.activity_mpandroid_chart;
    }

    @Override
    public void initData() {
        setToolbarTitle("图表");
        Log.d(TAG, "initData: 2");
    }

    @Override
    public void initView() {
        badgeItem=new BadgeItem();
        badgeItem.setHideOnSelect(false)
                .setText("11")
                .setBackgroundColorResource(R.color.colorAccent)
                .setBorderWidth(0);
        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_DEFAULT);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar.setBarBackgroundColor(R.color.white);//set background color for navigation bar
        mBottomNavigationBar.setInActiveColor(R.color.hui);
        mBottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.home_che_no, "折线").setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.mipmap.home_my_no, "柱状").setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.mipmap.home_no, "圆饼").setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.mipmap.home_shenpi_no, "雷达").setActiveColorResource(R.color.colorPrimary).setBadgeItem(badgeItem))
                .setFirstSelectedPosition(0)
                .initialise();
        mBottomNavigationBar.setTabSelectedListener(this);
        setDefaultFragment();
        
    }

    private void setDefaultFragment() {
        lineChartFragment= LineChartFragment.newInstance("折线");
        getSupportFragmentManager().beginTransaction().replace(R.id.content_ll,lineChartFragment).commit();
        setToolbarTitle("折线");

    }

    @Override
    public void onTabSelected(int position) {
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        switch(position){
            case 0:
                if(lineChartFragment==null){
                    lineChartFragment=LineChartFragment.newInstance("折线");
                }
                fragmentTransaction.replace(R.id.content_ll,lineChartFragment);
                setToolbarTitle("折线");
                break;
            case 1:
                if(barChartFragment==null){
                    barChartFragment=BarChartFragment.newInstance("柱状");
                }
                fragmentTransaction.replace(R.id.content_ll,barChartFragment);
                setToolbarTitle("柱状");

                break;
            case 2:
                if(pieChartFragment==null){
                    pieChartFragment=PieChartFragment.newInstance("圆饼");
                }
                fragmentTransaction.replace(R.id.content_ll,pieChartFragment);
                setToolbarTitle("圆饼");
                break;
            case 3:
                if(radarChartFragment==null){
                    radarChartFragment=RadarChartFragment.newInstance("雷达");
                }
                fragmentTransaction.replace(R.id.content_ll,radarChartFragment);
                setToolbarTitle("雷达");
                badgeItem.hide(true);
                break;
            default:
                break;
        }
        fragmentTransaction.commit();

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}
