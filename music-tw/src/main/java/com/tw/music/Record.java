package com.tw.music;

public class Record {
	String mName;
	int mIndex;
	int mLevel;
	LName[] mLName;
	int mLength;
	public int mCLength;
	Record mCNext;
	Record mBNext;
	Record mPrev;

	public Record(String name, int index, int level) {
		mName = name;
		mIndex = index;
		mLevel = level;
	}

	public Record(String name, int index, int level, Record prev) {
		mName = name;
		mIndex = index;
		mLevel = level;
		mPrev = prev;
	}

	public void clearRecord() {
		for(int i = 0; i < mCLength; i++) {
			mLName[i] = null;
		}
		mCLength = 0;
		mLName = null;
		mLength = 0;
		if(mCNext != null) {
			mCNext.clearRecord();
			mCNext = null;
		}
		if(mBNext != null) {
			mBNext.clearRecord();
			mBNext = null;
		}
	}

	public void setLength(int length) {
		if(mLength != length) {
			clearRecord();
			if (length > 0) {
    			mLName = new LName[length];
    			mLength = length;
			}
		}
	}

	public void setNext(Record next) {
		if(mCNext != next) {
			if((mBNext != null) && (mBNext != next)) {
				mBNext.clearRecord();
				mBNext = null;
			}
			mBNext = mCNext;
			mCNext = next;
		}
	}

	public Record getNext(int index) {
		if((mCNext != null) && (mCNext.mIndex == index)) {
			return mCNext;
		} else if((mBNext != null) && (mBNext.mIndex == index)) {
			return mBNext;
		} else {
			return null;
		}
	}

	public void add(String name, String path) {
		if(mCLength < mLength) {
			mLName[mCLength++] = new LName(name, path);
		}
	}

	public void add(LName name) {
		if(mCLength < mLength) {
			mLName[mCLength++] = name;
		}
	}

	public void copyLName(Record record) {
		setLength(record.mLength);
		mCLength = 0;
		for(LName n : record.mLName) {
			add(new LName(n));
		}
	}
}
