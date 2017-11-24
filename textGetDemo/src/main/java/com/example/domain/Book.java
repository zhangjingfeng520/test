package com.example.domain;

public class Book {

	public String name; // 名称
	public String autor; // 作者
	public String description; // 简介
	public String lastUpdateTime; // 最后更新

	public String src; // 图标URL
	public String url;

	@Override
	public String toString() {
		return "name:" + name + " autor:" + autor + " lastUpdateTime:"
				+ lastUpdateTime + " description:" + description;
	}

}
