package cn.xy.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import cn.xy.library.XApp;
import cn.xy.library.util.convert.XConvert;
import cn.xy.library.util.log.XLog;
import cn.xy.library.util.permissions.XPermission;
import cn.xy.library.util.screen.XScreen;
import cn.xy.library.util.toast.XToast;

public class MainActivity extends AppCompatActivity {
    public static String permission_RECORD_AUDIO = "android.permission.WRITE_SETTINGS";
    public static String[] AidioPermissions = {
            permission_RECORD_AUDIO
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        XApp.init(getApplication());
        /**单个权限*/
        XPermission.permission(AidioPermissions).callback(new XPermission.SimpleCallback() {
            @Override
            public void onGranted() {
                XLog.i();
            }

            @Override
            public void onDenied() {
                XLog.i();}
        }).request();
    }

    /**
     * getScreenWidth     : 获取屏幕的宽度（单位：px）
     * getScreenHeight    : 获取屏幕的高度（单位：px）
     * getAppScreenWidth  : 获取应用屏幕的宽度（单位：px）
     * getAppScreenHeight : 获取应用屏幕的高度（单位：px）
     * getScreenDensity   : 获取屏幕密度
     * getScreenDensityDpi: 获取屏幕密度 DPI
     * setFullScreen      : 设置屏幕为全屏
     * setNonFullScreen   : 设置屏幕为非全屏
     * toggleFullScreen   : 切换屏幕为全屏与否状态
     * isFullScreen       : 判断屏幕是否为全屏
     * setLandscape       : 设置屏幕为横屏
     * setPortrait        : 设置屏幕为竖屏
     * isLandscape        : 判断是否横屏
     * isPortrait         : 判断是否竖屏
     * getScreenRotation  : 获取屏幕旋转角度
     * screenShot         : 截屏
     * isScreenLock       : 判断是否锁屏
     * setSleepDuration   : 设置进入休眠时长
     * getSleepDuration   : 获取进入休眠时长
     */

    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_screen_0:
                Toast("获取屏幕的宽度: "+XScreen.getScreenWidth());
                break;
            case R.id.bt_screen_1:
                Toast("获取屏幕的高度: "+XScreen.getScreenHeight());
                break;
            case R.id.bt_screen_2:
                Toast("获取应用屏幕的宽度: "+XScreen.getAppScreenWidth());
                break;
            case R.id.bt_screen_3:
                Toast("获取应用屏幕的高度: "+XScreen.getAppScreenHeight());
                break;
            case R.id.bt_screen_4:
                Toast("获取屏幕密度: "+XScreen.getScreenDensity());
                break;
            case R.id.bt_screen_5:
                Toast("获取屏幕密度 DPI: "+XScreen.getScreenDensityDpi());
                break;
            case R.id.bt_screen_6:
                Toast("设置屏幕为全屏");
                XScreen.setFullScreen(MainActivity.this);
                break;
            case R.id.bt_screen_7:
                Toast("设置屏幕为非全屏");
                XScreen.setNonFullScreen(MainActivity.this);
                break;
            case R.id.bt_screen_8:
                Toast("切换屏幕为全屏与否状态");
                XScreen.toggleFullScreen(MainActivity.this);
                break;
            case R.id.bt_screen_9:
                Toast("判断屏幕是否为全屏: "+XScreen.isFullScreen(MainActivity.this));
                break;
            case R.id.bt_screen_10:
                Toast("设置屏幕为横屏");
                XScreen.setLandscape(MainActivity.this);
                break;
            case R.id.bt_screen_11:
                Toast("设置屏幕为竖屏");
                XScreen.setPortrait(MainActivity.this);
                break;
            case R.id.bt_screen_12:
                Toast("判断是否横屏: "+XScreen.isLandscape());
                break;
            case R.id.bt_screen_13:
                Toast("判断是否竖屏: "+XScreen.isPortrait());
                break;
            case R.id.bt_screen_14:
                Toast("获取屏幕旋转角度: "+XScreen.getScreenRotation(MainActivity.this));
                break;
            case R.id.bt_screen_15:
                Bitmap bitmap = XScreen.screenShot(MainActivity.this);
                findViewById(R.id.image).setBackground(XConvert.bitmap2Drawable(bitmap));
                findViewById(R.id.image).setVisibility(View.VISIBLE);
                findViewById(R.id.image).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        findViewById(R.id.image).setVisibility(View.GONE);
                    }
                });
                Toast("截屏");
                break;
            case R.id.bt_screen_16:
                Toast("判断是否锁屏"+XScreen.isScreenLock());
                break;
            case R.id.bt_screen_17:
                Toast("设置进入休眠时长");
                i = 5;
                Toast(String.valueOf(--i));
                XScreen.setSleepDuration(5000);
                mHandler.removeMessages(0x01);
                mHandler.sendEmptyMessageDelayed(0x01,10000);
                break;
            case R.id.bt_screen_18:
                Toast("获取进入休眠时长: "+XScreen.getSleepDuration());
                break;
        }
    }
    int i = 5;
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (i >= 0){
                Toast(String.valueOf(--i));
                mHandler.removeMessages(0x01);
                mHandler.sendEmptyMessageDelayed(0x01,10000);
            }else {
                mHandler.removeMessages(0x01);
            }
        }
    };

    public void Toast(String s){
        XToast.getInstance().Text(s).show();
    }
}
