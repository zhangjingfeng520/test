package com.example.myapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.myapplication.adapter.MyArrayAdapter;
import com.example.myapplication.adapter.MyRecyclerAdapter;
import com.example.myapplication.bean.Info;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    Button button1,button2;
    ProgressBar progressBar;
    ListView listView;
    RecyclerView recyclerView,recyclerViewH;
    String[] data={"item1","item2","item3"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
//        ActivityCollector.addActivity(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //设置actionbar返回，第一个是左上角小图标能否点击，第二是是否有小箭头,第三个隐藏标题
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        progressBar=(ProgressBar)findViewById(R.id.progress_bar);
        button1=(Button)findViewById(R.id.button1);
        button1.setOnClickListener(this);
        button2=(Button)findViewById(R.id.button2);
        button2.setOnClickListener(this);

        MyArrayAdapter myArrayAdapter=new MyArrayAdapter(TestActivity.this,R.layout.list_item,Info.infos);
        listView=(ListView)findViewById(R.id.list);
        listView.setAdapter(myArrayAdapter);

        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        MyRecyclerAdapter myRecyclerAdapter=new MyRecyclerAdapter(Info.infos,R.layout.list_item);
        recyclerView.setAdapter(myRecyclerAdapter);

        recyclerViewH=(RecyclerView)findViewById(R.id.recycler_view_h);
        //水平
//        LinearLayoutManager linearLayoutManagerH=new LinearLayoutManager(this);
//        linearLayoutManagerH.setOrientation(LinearLayoutManager.HORIZONTAL);
//        recyclerViewH.setLayoutManager(linearLayoutManagerH);
        //瀑布
        StaggeredGridLayoutManager sgm=new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        recyclerViewH.setLayoutManager(sgm);
        //
        MyRecyclerAdapter myRecyclerAdapterH=new MyRecyclerAdapter(Info.infos,R.layout.list_item);
        recyclerViewH.setAdapter(myRecyclerAdapterH);


    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button1:
                int progress=progressBar.getProgress();
                progressBar.setProgress(progress+10);
                break;
            case R.id.button2:
                ProgressDialog progressDialog=new ProgressDialog(TestActivity.this);
                progressDialog.setTitle("加载");
                progressDialog.setMessage("请稍等...");
                progressDialog.setCancelable(true);
                progressDialog.show();
                break;
            default:
                break;
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ActivityCollector.removeActivity(this);
    }

}
