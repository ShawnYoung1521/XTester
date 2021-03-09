package cn.xy.windowmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import cn.xy.library.XApp;
import cn.xy.library.util.screen.XScreen;
import cn.xy.library.util.toast.XToast;

public class MainActivity extends AppCompatActivity {

    EasyTouchView mEasyTouchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        XApp.init(getApplication());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        intData();
        setKeepScreenOn(this,true);
        final DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        EasyTouchView.getInstance().initEasyTouch(this,mDisplayMetrics);
    }

    private void intData() {
        XToast.getInstance().Text("屏幕宽： "+XScreen.getScreenWidth()+
                "  高： "+XScreen.getScreenHeight()+
                "   APP宽： "+XScreen.getAppScreenWidth()+
                "   高： "+XScreen.getAppScreenHeight()).show();
    }

    /**
     * @param activity
     * @param keepScreenOn 是否开启屏幕常亮
     */
    public void setKeepScreenOn(Activity activity, boolean keepScreenOn) {
        if(keepScreenOn){
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }else{
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.open:
                EasyTouchView.getInstance().addIconView();
                break;
            case R.id.close:
                EasyTouchView.getInstance().removeIcon();
                break;
        }
    }
}
