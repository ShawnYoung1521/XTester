package cn.tw.packagemanger;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import cn.xy.library.XApp;
import cn.xy.library.util.app.XApplication;
import cn.xy.library.util.toast.XToast;

public class MainActivity extends AppCompatActivity {


    String packageName = "cn.tw.packagemanger";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        XApp.init(getApplication());
    }

    public void all_app(View view){
        startActivity(new Intent(this,AppListActivity.class));
    }

    public void isAppInstalled(View view){
        XToast.getInstance().Text(""+ XApplication.isAppInstalled(packageName)).show();
    }
    public void isAppDebug(View view){
        XToast.getInstance().Text(""+ XApplication.isAppDebug(packageName)).show();
    }
    public void isAppSystem(View view){
        XToast.getInstance().Text(""+ XApplication.isAppSystem(packageName)).show();
    }
    public void isAppRunning(View view){
        XToast.getInstance().Text(""+ XApplication.isAppRunning(packageName)).show();
    }
    public void getAppIcon(View view){
        XToast.getInstance().Text(""+ XApplication.getAppIcon(packageName)).show();
    }
    public void getAppName(View view){
        XToast.getInstance().Text(""+ XApplication.getAppName(packageName)).show();
    }
    public void getAppPath(View view){
        XToast.getInstance().Text(""+ XApplication.getAppPath(packageName)).show();
    }
    public void getAppVersionName(View view){
        XToast.getInstance().Text(""+ XApplication.getAppVersionName(packageName)).show();
    }
    public void getAppVersionCode(View view){
        XToast.getInstance().Text(""+ XApplication.getAppVersionCode(packageName)).show();
    }
    public void getAppSignature(View view){
        XToast.getInstance().Text(""+ XApplication.getAppSignature(packageName)).show();
    }
    public void getAppUid(View view){
        XToast.getInstance().Text(""+ XApplication.getAppUid(packageName)).show();
    }
    public void getAppInfo(View view){
        XToast.getInstance().Text(""+ XApplication.getAppInfo(packageName)).show();
    }

    boolean b = false;
    public void changeAppState(View view){
        b = !b;
        XApplication.changeAppState(packageName, b);
    }
    public void changActivityState(View view){
        b = !b;
        XApplication.changActivityState(new ComponentName("cn.xy.windowmanager","cn.xy.windowmanager.MainActivity"),b);
    }

}
