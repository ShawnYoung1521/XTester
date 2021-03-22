package cn.xy.windowmanager;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

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

    private void TestToast() {
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int w = rand.nextInt(XScreen.getScreenWidth()) ;
        int h = rand.nextInt(XScreen.getScreenHeight()) ;
        XToast.getInstance().Text("随机位置的Toast")
                .TextColor(R.color.colorPrimary) //字体颜色
                .TextSize(20) //字体大小
                .BackGroundColor(R.color.colorAccent) //背景颜色
                .LayoutParamsX(w) //偏离XToast左边距离 受Gravity影响
                .LayoutParamsY(h) //偏离XToastY坐标距离 受Gravity影响
                .Gravity(Gravity.BOTTOM|Gravity.LEFT) //Toast显示位置的重心设置
                .RoundedCorners(3) //背景圆角大小
                .ShowTime(XToast.LENGTH_SHORTSHORT) //显示时长
                .show();
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
            case R.id.toast:
                TestToast();
                break;
        }
    }


}
