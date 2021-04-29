package cn.tw.ts7reverse;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.tw.john.TWUtil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TWUtil mTWUtil = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTWUtil = new TWUtil ();
        mTWUtil = TWTW.open();
        if(mTWUtil != null) {
            mTWUtil.addHandler(TAG,mHandler);
        }
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @NonNull
        @Override
        public String getMessageName(@NonNull Message message) {
            switch (message.what){
                /**按键信息**/
                case 0x0201:
                case 0x8201: {
                    break;
                }
                /**倒车信息**/
                case 0x9f1c:

                    break;
                /**白天黑夜模式**/
                case 0x0204:
                    break;
                /**扩展信息-配置信息**/
                case 0x0112:
                    break;
                /**模式**/
                case 0x0301:
                    break;
                /**系统音量**/
                case 0x0203:
                    break;
            }
            return super.getMessageName(message);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mTWUtil != null) {
            mTWUtil.close();
            mTWUtil = null;
        }
    }
}
