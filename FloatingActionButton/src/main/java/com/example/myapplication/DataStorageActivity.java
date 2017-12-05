package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.base.BaseActivity;
import com.example.myapplication.base.MyApplication;
import com.example.myapplication.db.MyDatabaseHelper;
import com.example.myapplication.view.ClearEditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class DataStorageActivity extends BaseActivity implements View.OnClickListener {
    private Button readerBtn, writerBtn, createBtn, addDataBtn;
    private TextView readerTv;
    private ClearEditText writerEt;
    private static final String TAG = "DataStorageActivity";
    private MyDatabaseHelper databaseHelper;

    @Override
    public int getLayoutId() {
        return R.layout.activity_data_storage;
    }

    @Override
    public void initData() {
        setToolbarTitle("数据存储");
        databaseHelper = new MyDatabaseHelper(MyApplication.getAppContext(), "zjf.db", null, 1);
    }

    @Override
    public void initView() {
        readerBtn = (Button) findViewById(R.id.reader_btn);
        writerBtn = (Button) findViewById(R.id.writer_btn);
        readerTv = (TextView) findViewById(R.id.reader_tv);
        writerEt = (ClearEditText) findViewById(R.id.writer_et);
        createBtn = (Button) findViewById(R.id.create_database);
        addDataBtn = (Button) findViewById(R.id.add_data);
        addDataBtn.setOnClickListener(this);
        createBtn.setOnClickListener(this);
        readerBtn.setOnClickListener(this);
        writerBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reader_btn:
                readerTv.setText(load());
                break;
            case R.id.writer_btn:
                save(writerEt.getText().toString());
                break;
            case R.id.create_database:
                databaseHelper.getWritableDatabase();
                break;
            case R.id.add_data:
                SQLiteDatabase db = databaseHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                //第一条
                values.put("name", "zjf");
                values.put("author", "zhang");
                values.put("pages", 23);
                values.put("price", 16.58);
                db.insert("book", null, values);
                values.clear();
                //第二条
                values.put("name", "jing");
                values.put("author", "feng");
                values.put("pages", 13);
                values.put("price", 12.35);
                db.insert("book", null, values);
                break;

        }
    }

    public void save(String data) {
//        String data = "data to save";
        FileOutputStream out;
        BufferedWriter writer = null;
        try {
            out = openFileOutput("data", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public String load() {
        FileInputStream in;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = openFileInput("data");
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }

    public void setSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("zjf", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("married", false);
        editor.putString("name", "zjf");
        editor.putInt("age", 24);
        editor.apply();
    }

    public void getSharedPreferences() {
        SharedPreferences pref = getSharedPreferences("zjf", MODE_PRIVATE);
        String name = pref.getString("name", "");
        int age = pref.getInt("age", 0);
        boolean married = pref.getBoolean("married", false);
        Log.d(TAG, "name:" + name + ",age:" + age + ",married:" + married);
    }

}
