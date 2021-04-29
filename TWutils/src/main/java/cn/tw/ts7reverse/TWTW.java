package cn.tw.ts7reverse;

import android.tw.john.TWUtil;

public class TWTW extends TWUtil {
	private static final String TAG = "TWTW";
	
	public TWTW() {
	}
	
	public TWTW(int clazz) {
		super(clazz);
	}
	
	private static TWTW mTW = new TWTW(0x10);
	private static int mCount = 0;
	public static TWTW open() {
		if(mCount++ == 0) {
	        if(mTW.open(new short[] {0x0106, 0x010a, 0x0112, 0x0201, 0x0202, 0x0203, 0x020b, 0x0301, 0x0302, 0x0501, 0x0505, 0x0507, 0x0508, 0x0509, (short)0x9e00, (short)0x9e11, (short)0x9f10, (short)0x9f1b, (short)0x9f1c}) != 0) {
	        	mCount--;
	        	return null;
	        }
	        mTW.start();
		}
		return mTW;
	}

	public void close() {
		if(mCount > 0) {
			if(--mCount == 0) {
				stop();
				super.close();
			}
		}
	}

	public static int mModel = -1;
	public static int mCType = 0;
	
	public int ssWrite(int what, int arg1, int arg2) {
		return write(0x0501, what, 0x02, new byte[] {(byte)arg1, (byte)arg2});
	}
	
	public int ssWrite(int what) {
		return ssWrite(what, 0, 0);
	}
	
	public int ssWrite(int what, int arg1) {
		return ssWrite(what, arg1, 0);
	}
}
