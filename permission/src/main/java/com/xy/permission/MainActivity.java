package com.xy.permission;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import cn.xy.library.XApp;
import cn.xy.library.util.log.XLog;
import cn.xy.library.util.permissions.XPermission;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        XApp.init(getApplication());
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.getpermissions:
                for (String s:XPermission.getPermissions()){
                    XLog.i(s);
                }
                break;
            case R.id.getpermission_:
                /**单个权限*/
                XPermission.permission(PermissionsContract.AidioPermissions).callback(new XPermission.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        XLog.i();
                    }

                    @Override
                    public void onDenied() {
                        XLog.i();}
                }).request();
                break;
            case R.id.getpermission_s:
                /**权限组*/
                XPermission.permissionGroup(PermissionsContract.FileReadPermissions).callback(new XPermission.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        XLog.i();}

                    @Override
                    public void onDenied() {
                        XLog.i();}
                }).request();
                break;
            case R.id.getpermission_a:
                XLog.i(XPermission.isGranted(PermissionsContract.AidioPermissions));
                break;
        }
    }
}
