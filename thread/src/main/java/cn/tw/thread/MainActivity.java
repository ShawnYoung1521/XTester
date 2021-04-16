package cn.tw.thread;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;

import cn.xy.library.XApp;
import cn.xy.library.util.log.XLog;
import cn.xy.library.util.network.XNetwork;
import cn.xy.library.util.thread.XThread;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        XApp.init(getApplication());
        ExecutorService pool = XThread.getFixedPool(10);
        XLog.i(pool+"   ");
        XThread.executeByFixed(10, new XThread.Task<Object>() {
            @Override
            public Object doInBackground() throws Throwable {
                return XNetwork.isAvailable();
            }

            @Override
            public void onSuccess(Object result) {
                XLog.i("onSuccess   "+result);
            }

            @Override
            public void onCancel() {
                XLog.i();
            }

            @Override
            public void onFail(Throwable t) {
                XLog.i(t.getMessage());
            }
        });
    }
}
