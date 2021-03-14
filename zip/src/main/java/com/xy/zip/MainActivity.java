package com.xy.zip;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import cn.xy.library.util.log.XLog;
import cn.xy.library.util.zip.XZip;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    /**
     * zipFiles          : 批量压缩文件
     * zipFile           : 压缩文件
     * unzipFile         : 解压文件
     * unzipFileByKeyword: 解压带有关键字的文件
     * getFilesPath      : 获取压缩文件中的文件路径链表
     * getComments       : 获取压缩文件中的注释链表
     */
    public void onClick(View view) throws IOException {
        switch (view.getId()){
            case R.id.zip_y:
                XZip.zipFile("/storage/emulated/0/DCIM/Alipay/a.jpg","/storage/emulated/0/DCIM/Alipay/zip/a.zip");
                XZip.zipFile("/storage/emulated/0/DCIM/Alipay/b.jpg","/storage/emulated/0/DCIM/Alipay/zip/b.zip");
                XZip.zipFile("/storage/emulated/0/DCIM/Alipay/c.jpg","/storage/emulated/0/DCIM/Alipay/zip/c.zip");
                break;
            case R.id.zip_p:
                Collection<String> srcFiles = new ArrayList<>();
                srcFiles.add("/storage/emulated/0/DCIM/Alipay/a.jpg");
                srcFiles.add("/storage/emulated/0/DCIM/Alipay/b.jpg");
                srcFiles.add("/storage/emulated/0/DCIM/Alipay/c.jpg");
                srcFiles.add("/storage/emulated/0/DCIM/Alipay/d.jpg");
                srcFiles.add("/storage/emulated/0/DCIM/Alipay/e.jpg");
                srcFiles.add("/storage/emulated/0/DCIM/Alipay/f.jpg");
                XZip.zipFiles(srcFiles,"/storage/emulated/0/DCIM/Alipay/zip/files.zip");
                break;
            case R.id.zip_j:
                XZip.unzipFile("/storage/emulated/0/DCIM/Alipay/zip/files.zip","/storage/emulated/0/DCIM/Alipay/unzip");
                break;
            case R.id.zip_j1:
                XZip.unzipFileByKeyword("/storage/emulated/0/DCIM/Alipay/zip/files.zip",
                        "/storage/emulated/0/DCIM/Alipay/unzip","a");
                break;
            case R.id.zip_h1:
                XLog.i(XZip.getFilesPath("/storage/emulated/0/DCIM/Alipay/zip/files.zip"));
                break;
            case R.id.zip_h2:
                XLog.i(XZip.getComments("/storage/emulated/0/DCIM/Alipay/zip/files.zip"));
                break;
        }
    }
}
