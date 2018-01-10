package com.example.myapplication;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.adapter.MyMapAdapter;
import com.example.myapplication.baiduyuyin.ActivityWakeUpRecog;
import com.example.myapplication.bean.MyMap;
import com.example.myapplication.view.ClearEditText;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    FloatingActionButton fabBtn, fabBtn1;
    CoordinatorLayout rootLayout;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    Toolbar toolbar;
    TabLayout tabLayout;
    LinearLayout button_ll;
    NavigationView navigationView;
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ButtonBarLayout buttonBarLayout;
    RecyclerView mRecyclerView;
    List<MyMap> myMapList;
    private static final String TAG = "zjf";
    private static final int REQUEST_CODE_FILE = 100;
    private static final int REQUEST_CODE_CONTACTS = 101;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ActivityCollector.addActivity(this);
        rootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);
        initData();
        initView();
    }

    private void initData() {
        myMapList = new ArrayList<MyMap>();
        myMapList.add(new MyMap("登陆提示框", "0"));
        myMapList.add(new MyMap("分组list", "1"));
        myMapList.add(new MyMap("拨打", "2"));
        myMapList.add(new MyMap("TEST", "3"));
        myMapList.add(new MyMap("百度地图", "4"));
        myMapList.add(new MyMap("消息气泡", "5"));
        myMapList.add(new MyMap("list刷新", "6"));
        myMapList.add(new MyMap("recycler刷新", "7"));
        myMapList.add(new MyMap("文件选择器", "8"));
        myMapList.add(new MyMap("网络请求", "9"));
        myMapList.add(new MyMap("图表", "10"));
        myMapList.add(new MyMap("通讯录", "11"));
        myMapList.add(new MyMap("广播.通知.服务", "12"));
        myMapList.add(new MyMap("拨号盘", "13"));
        myMapList.add(new MyMap("数据存储", "14"));
        myMapList.add(new MyMap("语音识别", "15"));
        myMapList.add(new MyMap("语音唤醒", "16"));
        myMapList.add(new MyMap("唤醒后识别", "17"));
        myMapList.add(new MyMap("多媒体", "18"));

    }

    private void initView() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //设置actionbar返回，第一个是不带箭头，第二个带箭头
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(drawerToggle);

        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitleEnabled(false);//Title固定
        buttonBarLayout = (ButtonBarLayout) findViewById(R.id.playButton);
        buttonBarLayout.setOnClickListener(this);

        //RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(sglm);
        MyMapAdapter myMapAdapter = new MyMapAdapter(this, myMapList);
        mRecyclerView.setAdapter(myMapAdapter);
        myMapAdapter.setMyClickItemListener(new MyMapAdapter.onMyClickItemListener() {
            @Override
            public void onMyClickItem(int position) {
                Intent intent = new Intent();
                switch (position) {
                    case 0:
                        showLoginDialog();
                        break;
                    case 1:
//                        intent.setAction(Intent.ACTION_VIEW);
//                        intent.setData(Uri.parse("http://www.baidu.com"));
//                        startActivity(intent);
                        intent.setClass(MainActivity.this,ThreeLevelMenuActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent.setAction(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:10086"));
                        startActivity(intent);
                        break;
                    case 3:
                        intent.setClass(MainActivity.this, TestActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent.setClass(MainActivity.this, MapActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        intent.setClass(MainActivity.this, MessageActivity.class);
                        startActivity(intent);
                        break;
                    case 6://recyclerView下拉上拉刷新
                        intent.setClass(MainActivity.this, MySwipeRefreshActivity.class);
                        startActivity(intent);
                        break;
                    case 7://listView下拉上拉刷新
                        intent.setClass(MainActivity.this, ListSwipeRefreshActivity.class);
                        startActivity(intent);
                        break;
                    case 8://文件选择器
//                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N_MR1) {
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED) {
                            openFilePicker();
                        } else {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                    , REQUEST_CODE_FILE);
                        }
                        break;
                    case 9://网络请求
                        intent.setClass(MainActivity.this, BooksByCatsActivity.class);
                        startActivity(intent);
                        break;
                    case 10://图表
                        intent.setClass(MainActivity.this, MPAndroidChartActivity.class);
                        startActivity(intent);
                        break;
                    case 11://通讯录
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS)
                                == PackageManager.PERMISSION_GRANTED) {
                            intent.setClass(MainActivity.this, ContactsActivity.class);
                            startActivity(intent);
                        } else
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS},
                                    REQUEST_CODE_CONTACTS);
                        break;
                    case 12:
                        intent.setClass(MainActivity.this, MyBroadcastActivity.class);
                        startActivity(intent);
                        break;
                    case 13:
                        intent.setClass(MainActivity.this, DialerActivity.class);
                        startActivity(intent);
                        break;
                    case 14:
                        intent.setClass(MainActivity.this, DataStorageActivity.class);
                        startActivity(intent);
                        break;
                    case 15:
                        intent.setClass(MainActivity.this,ActivityMiniRecog.class);
                        startActivity(intent);
                        break;
                    case 16:
                        intent.setClass(MainActivity.this,ActivityMiniWakeUp.class);
                        startActivity(intent);
                        break;
                    case 17:
                        intent.setClass(MainActivity.this,ActivityWakeUpRecog.class);
                        startActivity(intent);
                        break;
                    case 18:
                        intent.setClass(MainActivity.this,MediaActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });


        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.i(TAG, "verticalOffset: " + verticalOffset + "getTotalScrollRange: " + appBarLayout.getTotalScrollRange());
                if (verticalOffset == 0) {
                    toolbarState = CollapsingToolbarLayoutState.EXPANDED;
                    collapsingToolbarLayout.setTitle("FAB1");
                    fabBtn.setVisibility(View.GONE);
//                    nestedScrollView.setNestedScrollingEnabled(false);//appBarLayout不响应滚动事件

                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    toolbarState = CollapsingToolbarLayoutState.COLLAPSED;
                    collapsingToolbarLayout.setTitle("");
                    buttonBarLayout.setVisibility(View.VISIBLE);
                    fabBtn.setVisibility(View.VISIBLE);

                } else {
                    if (toolbarState != CollapsingToolbarLayoutState.INTERNEDIATE) {
                        if (toolbarState == CollapsingToolbarLayoutState.COLLAPSED) {
                            buttonBarLayout.setVisibility(View.GONE);
                        }
                        toolbarState = CollapsingToolbarLayoutState.INTERNEDIATE;
                        collapsingToolbarLayout.setTitle("FAB");
                    }
                }
            }
        });

        fabBtn = (FloatingActionButton) findViewById(R.id.fabBtn);
        fabBtn.setOnClickListener(this);
        fabBtn1 = (FloatingActionButton) findViewById(R.id.fabBtn1);
        fabBtn1.setOnClickListener(this);
//        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
//        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
//        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
//        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));
//        tabLayout.addTab(tabLayout.newTab().setText("Tab 4"));

//        button_ll = (LinearLayout) findViewById(R.id.button_ll);
//        for (int i = 0; i < 20; i++) {
//            Button button = new Button(this);
//            button.setText("11");
//            button.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
//            button.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
//            button_ll.addView(button);
//        }

        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setItemIconTintList(null);//item的icon保持原色彩
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navItem1:
                        break;
                    case R.id.navItem2:
                        break;
                    case R.id.navItem3:
                        break;
                    case R.id.navItem4:
                        break;
                }
                return false;
            }
        });

    }

    //打开文件选择器
    private void openFilePicker() {
        new LFilePicker()
                .withActivity(MainActivity.this)
                .withRequestCode(100)
                .withTitle("手机内部存储")
                .withBackgroundColor("#2196f3")
                .start();
    }

    private CollapsingToolbarLayoutState toolbarState;

    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }

    //抖动
    public void setStartShake(int counts, View v) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(500);
        v.startAnimation(translateAnimation);
    }

    //权限回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_FILE) {
            //文件选择器
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openFilePicker();
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    setPermissionDialog("访问储存空间", requestCode);
                }
            }
        } else if (requestCode == REQUEST_CODE_CONTACTS) {
            //通讯录
            if (permissions[0].equals(Manifest.permission.READ_CONTACTS) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(MainActivity.this, ContactsActivity.class);
                startActivity(intent);
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_CONTACTS)) {
                    setPermissionDialog("访问通讯录", requestCode);
                }
            }
        }
    }

    //提示设置权限
    private void setPermissionDialog(String str, int requestCode) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage("请赋予 " + str + " 的权限，不开启将无法正常使用该功能!")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//可能压在一个新建的栈中
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);//activity不压在栈中
                        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);//从任务列表中清除
                        startActivity(intent);
                    }
                })
                .create();
        alertDialog.show();
    }

    //响应请求返回
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);
                Toast.makeText(getApplicationContext(), "选中了" + list.size() + "个文件", Toast.LENGTH_SHORT).show();
                for (String str : list) {
                    File file = new File(str);
                    Log.d(TAG, "name:" + file.getName());
                }
            }
        }
    }

    //Activity彻底运行起来时调用
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    //Activity配置发生变化时
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))//左侧按钮打开抽屉
             return true;
        switch(item.getItemId()){
            case R.id.setting:
                startActivity(new Intent(MainActivity.this,SettingsActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean playState = false;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabBtn:
                Snackbar.make(rootLayout, "hello world", Snackbar.LENGTH_SHORT)
                        .setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        }).show();
                break;
            case R.id.fabBtn1:
                if (playState == false) {
                    playState = true;
//                    nestedScrollView.setNestedScrollingEnabled(false);
                    fabBtn1.setImageResource(R.mipmap.zanting);
                } else {
                    playState = false;
//                    nestedScrollView.setNestedScrollingEnabled(true);
                    fabBtn1.setImageResource(R.mipmap.bofang);
                }
                break;
            case R.id.playButton:
                appBarLayout.setExpanded(true);
                playState = true;
//                nestedScrollView.setNestedScrollingEnabled(false);
                fabBtn1.setImageResource(R.mipmap.zanting);
                break;
        }
    }

    private View mView;
    private LayoutInflater mLayoutInflater;
    private ClearEditText userName, password;
    private Button login_button;
    private TextInputLayout userName_til, password_til;
    private CheckBox checkBox;
    private View mProgressView;
    private View mLoginFormView;
    private boolean show = false;

    //弹出登陆
    private void showLoginDialog() {

        mLayoutInflater = LayoutInflater.from(MainActivity.this);
        mView = mLayoutInflater.inflate(R.layout.alert_login, null);

        userName_til = (TextInputLayout) mView.findViewById(R.id.userName_til);
        password_til = (TextInputLayout) mView.findViewById(R.id.password_til);
        userName = (ClearEditText) mView.findViewById(R.id.userName);
        password = (ClearEditText) mView.findViewById(R.id.password);
        login_button = (Button) mView.findViewById(R.id.login_button);
        mLoginFormView = mView.findViewById(R.id.login_form);
        mProgressView = mView.findViewById(R.id.login_progress);
        checkBox= (CheckBox) mView.findViewById(R.id.checkbox);
        pref=getSharedPreferences("zjf",MODE_PRIVATE);
        Boolean isRemember=pref.getBoolean("isRemember",false);
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("登陆")
                .setMessage("")
                .setView(mView)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        final AlertDialog dialog = alert.create();
        dialog.show();

        if(isRemember){
            checkBox.setChecked(true);
            ((ClearEditText)dialog.findViewById(R.id.userName)).setText(pref.getString("account",""));
            ((ClearEditText)dialog.findViewById(R.id.password)).setText(pref.getString("password",""));
        }

        userName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT) {
                    if (TextUtils.isEmpty(userName.getText())) {
                        setStartShake(5, userName_til);
                    }
                }
                return false;
            }
        });
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (TextUtils.isEmpty(password.getText())) {
                        setStartShake(5, password_til);
                    }
                }
                return false;
            }
        });
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(userName.getText())) {
                    setStartShake(5, userName_til);
                } else if (TextUtils.isEmpty(password.getText())) {
                    setStartShake(5, password_til);
                } else {
                    mLoginFormView.setVisibility(View.GONE);
                    mProgressView.setVisibility(View.VISIBLE);
                    mProgressView.animate().setDuration(1500).alpha(1)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    if (password.getText().toString().equals("123")) {
                                        editor = pref.edit();
                                        if(checkBox.isChecked()) {
                                            editor.putString("account", userName.getText().toString());
                                            editor.putString("password", password.getText().toString());
                                            editor.putBoolean("isRemember",true);
                                        }else
                                            editor.clear();
                                        editor.apply();
                                        show = true;
                                    } else
                                        show = false;

                                    if (show == false) {
                                        mProgressView.setVisibility(View.GONE);
                                        mLoginFormView.setVisibility(View.VISIBLE);
                                        password_til.setError("密码错误!");
                                        setStartShake(5, password_til);
                                    } else {
                                        dialog.dismiss();
                                    }
                                }
                            });
                }
            }
        });

    }

    //外部启动该Activity
    public static void startAction(Context context, String data) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("param1", data);
        context.startActivity(intent);

    }

    //双击back退出应用
    private Toast zToast;

    @Override
    public void onBackPressed() {
        if (zToast == null) {
            zToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        }
        if (zToast.getView().getParent() == null) {
            zToast.setText("再次点击返回键退出应用");
            zToast.show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ActivityCollector.removeActivity(this);
    }
}
