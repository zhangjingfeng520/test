package com.example.myapplication;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.adapter.SortAdapter;
import com.example.myapplication.base.BaseActivity;
import com.example.myapplication.bean.ContactSortModel;
import com.example.myapplication.utils.PinyinComparator;
import com.example.myapplication.utils.PinyinUtils;
import com.example.myapplication.view.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContactsActivity extends BaseActivity implements SearchView.OnQueryTextListener, View.OnClickListener {
    private static final String TAG = "ContactsActivity";
    private ListView listView;
    private SideBar sideBar;
    private TextView dialog, title;
    private FloatingActionButton addFAB;
    private List<ContactSortModel> list = new ArrayList<ContactSortModel>();
    private SortAdapter sortAdapter;
    private int status = 0;
    private CoordinatorLayout rootLayout;


    @Override
    public int getLayoutId() {
        return R.layout.activity_contacts;
    }

    @Override
    public void initData() {
        setToolbarTitle("通讯录");
    }

    @Override
    public void initView() {
        dialog = (TextView) findViewById(R.id.tv_dialog);
        title = (TextView) findViewById(R.id.tv_title);
        listView = (ListView) findViewById(R.id.contacts_list_view);
        sideBar = (SideBar) findViewById(R.id.sidebar);
        sideBar.setTextView(dialog);
        sortAdapter = new SortAdapter(this, list);
        listView.setAdapter(sortAdapter);
        addFAB = (FloatingActionButton) findViewById(R.id.add_fab);
        addFAB.setOnClickListener(this);
        rootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);
        getPhoneContacts("我");

        initEvents();

    }

    private void initEvents() {
        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = sortAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    listView.setSelection(position);
                }
            }
        });
        //ListView的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplication(), ((ContactSortModel) sortAdapter.getItem(i)).getName(), Toast.LENGTH_SHORT).show();
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                dialog.setVisibility(View.GONE);
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if (i > 0) {
                    dialog.setVisibility(View.VISIBLE);
                    Log.d("dispatchTouchEvent", "dispatchTouchEvent: 555");
                }
                dialog.setText(list.get(i).getSortLetters());
                title.setText(list.get(i).getSortLetters());
                sideBar.changeChoose(list.get(i).getSortLetters());
                Log.d(TAG, "onScroll: " + i);
                if (listView.getLastVisiblePosition() == sortAdapter.getCount() - 1) {

                    dialog.setVisibility(View.GONE);
                }
                if (count > 0) {
                    title.setText("找到 " + count + " 个联系人");
                } else if (count == 0) {
                    title.setText("没有找到联系人");
                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contacts_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (permissions[0] == Manifest.permission.READ_CONTACTS && grantResults.equals(PackageManager.PERMISSION_GRANTED)) {
                getPhoneContacts("我");
            }
        } else if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
            setPermissionDialog();
        }
    }

    //提示设置权限
    private void setPermissionDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage("请赋予访问存储的权限，不开启将无法正常使用该功能!")
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

    private void getPhoneContacts(String name) {
        ContentResolver resolver = getContentResolver();
        //获取手机联系人
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};

        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+ "=?"
                + " AND " + ContactsContract.CommonDataKinds.Phone.TYPE + "='" +
                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE + "'";

        String selection1 = ContactsContract.CommonDataKinds.Phone.TYPE + "='" +
                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE + "'";

        Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                , projection, selection, new String[]{name}, null);
        Cursor cursor1 = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                , projection, selection1, null, null);

//        if (cursor.moveToNext()) {
//            String number = cursor.getString(2);
//            Toast.makeText(this, number, Toast.LENGTH_SHORT).show();
//        }
        while (cursor1.moveToNext()) {
            Log.d(TAG, "name: " + cursor1.getString(1) + " , " + "phone: " + cursor1.getString(2));
            list.add(new ContactSortModel(cursor1.getString(1), "A"));
        }

        List<ContactSortModel> mSortList = filledData(list);
        // 根据a-z进行排序
        Collections.sort(mSortList, new PinyinComparator());
        list.clear();
        list.addAll(mSortList);
        sortAdapter.notifyDataSetChanged();
        cursor.close();
        cursor1.close();
    }


    @Override
    public boolean onQueryTextSubmit(String query) {

        Toast.makeText(this, query, Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
//        Toast.makeText(this, newText, Toast.LENGTH_SHORT).show();
        filterData(newText);
        return false;
    }

    List<ContactSortModel> sortModelList = new ArrayList<>();
    int count = -1;

    //按字母排序
    private void filterData(String filterStr) {
        if (TextUtils.isEmpty(filterStr)) {
            // 根据a-z进行排序
            Collections.sort(list, new PinyinComparator());
            sortAdapter.updateListView(list);
            count = -1;
        } else {
            sortModelList.clear();
            count = 0;
            //
            for (ContactSortModel sortModel : list) {
                String name = sortModel.getName();
                if (name.toUpperCase().indexOf(filterStr.toString().toUpperCase()) != -1 || PinyinUtils.getPingYin(name).toUpperCase().startsWith(filterStr.toString().toUpperCase())) {
                    sortModelList.add(sortModel);
                    count++;
                }
            }
            // 根据a-z进行排序
            Collections.sort(sortModelList, new PinyinComparator());
            sortAdapter.updateListView(sortModelList);
        }
    }

    //设置首字母属性
    private List<ContactSortModel> filledData(List<ContactSortModel> date) {
        List<ContactSortModel> mSortList = new ArrayList<>();
        ArrayList<String> indexString = new ArrayList<>();

        for (int i = 0; i < date.size(); i++) {
            ContactSortModel sortModel = new ContactSortModel();
            sortModel.setName(date.get(i).getName());
            String pinyin = PinyinUtils.getPingYin(date.get(i).getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
                if (!indexString.contains(sortString)) {
                    indexString.add(sortString);
                }
            } else {
                sortModel.setSortLetters(sortString.toUpperCase());
                if (!indexString.contains(sortString)) {
                    indexString.add(sortString);
                }
            }
            mSortList.add(sortModel);
        }
        Collections.sort(indexString);
        sideBar.setIndexText(indexString);
        return mSortList;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_fab:
                Snackbar.make(rootLayout, "添加联系人", Snackbar.LENGTH_LONG).setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }).show();
                break;
        }
    }
}
