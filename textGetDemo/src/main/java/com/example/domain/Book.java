package com.example.domain;

public class Book {

	public String name; // ����
	public String autor; // ����
	public String description; // ���
	public String lastUpdateTime; // ������

	public String src; // ͼ��URL
	public String url;

	@Override
	public String toString() {
		return "name:" + name + " autor:" + autor + " lastUpdateTime:"
				+ lastUpdateTime + " description:" + description;
	}

}
