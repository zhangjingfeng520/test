package com.example.myapplication;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.adapter.MsgAdapter;
import com.example.myapplication.bean.Msg;
import com.example.myapplication.view.ClearEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MessageActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    Toolbar toolbar;
    RecyclerView msgRecyclerView;
    ClearEditText clearEditText;
    TextView sendTextView;
    List<Msg> msgList;
    MsgAdapter msgAdapter;
    LinearLayoutManager layoutManager;
    SwipeRefreshLayout srl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initData();
        initView();

    }

    private void initData() {
        msgList=new ArrayList<Msg>();
        Msg msg=new Msg("你好",Msg.TYPE_RECEIVED);
        msgList.add(msg);
        Msg msg2=new Msg("你叫什么",Msg.TYPE_RECEIVED);
        msgList.add(msg2);
        Msg msg3=new Msg("你好,我叫。。",Msg.TYPE_SEND);
        msgList.add(new Msg("可以。",Msg.TYPE_SEND));
        msgList.add(new Msg("22。",Msg.TYPE_SEND));
        msgList.add(new Msg("33。",Msg.TYPE_SEND));
        msgList.add(new Msg("44。",Msg.TYPE_SEND));
        msgList.add(new Msg("哦。",Msg.TYPE_RECEIVED));
        msgList.add(new Msg("哦ss。",Msg.TYPE_RECEIVED));

    }

    private void initView() {

        //设置actionbar返回，第一个是左上角小图标能否点击，第二是是否有小箭头,第三个隐藏标题
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        srl=(SwipeRefreshLayout)findViewById(R.id.srl);
        srl.setColorSchemeResources(R.color.colorAccent);
        srl.setEnabled(true);
        srl.setOnRefreshListener(this);
        clearEditText= (ClearEditText) findViewById(R.id.msg_edit_view);
        sendTextView=(TextView)findViewById(R.id.msg_text_view);
        msgRecyclerView=(RecyclerView)findViewById(R.id.msg_recycler_view);
        layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        layoutManager.setStackFromEnd(true);//滚动到底部
        msgRecyclerView.setLayoutManager(layoutManager);
        msgAdapter=new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(msgAdapter);
        sendTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!"".equals(clearEditText.getText().toString())){
                    Msg msg=new Msg(clearEditText.getText().toString(),Msg.TYPE_SEND);
                    msgList.add(msg);
                    msgAdapter.notifyItemInserted(msgList.size()-1);
                    msgRecyclerView.scrollToPosition(msgList.size()-1);
                    clearEditText.setText("");
                }
            }
        });
        clearEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu,menu);
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

    //刷新
    @Override
    public void onRefresh() {
        srl.setRefreshing(true);
        final Random random=new Random();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                msgList.add(new Msg("www"+random.nextInt(100),Msg.TYPE_RECEIVED));
//                msgAdapter.notifyDataSetChanged();
                msgAdapter.notifyItemInserted(msgList.size()-1);
                msgRecyclerView.scrollToPosition(msgList.size()-1);
                srl.setRefreshing(false);
            }
        },1200);
    }
}
