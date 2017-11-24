package com.example.myapplication.bean;

/**
 * Created by Administrator on 2017/11/15.
 */
public class ContactSortModel {

    private String name;//显示的数据
    private String sortLetters;//显示数据拼音的首字母

    public ContactSortModel(String name, String sortLetters) {
        this.name = name;
        this.sortLetters = sortLetters;
    }

    public ContactSortModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
}