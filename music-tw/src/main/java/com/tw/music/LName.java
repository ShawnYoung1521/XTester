package com.tw.music;

public class LName {
	String mName;
	String mPath;

	public LName(String name, String path) {
		mName = name;
		mPath = path;
	}

	public LName(LName name) {
		mName = name.mName;
		mPath = name.mPath;
	}
}
