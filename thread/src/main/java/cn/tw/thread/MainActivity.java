package cn.tw.thread;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import cn.xy.library.XApp;
import cn.xy.library.util.log.XLog;
import cn.xy.library.util.thread.XThread;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        XApp.init(getApplication());
        for (int i = 0;i <= 10;i ++){
            final int finalI = i;
            XThread.executeByFixed(20, new XThread.Task<Object>() {
                @Override
                public Object doInBackground() throws Throwable {
                    return "正在执行第"+ finalI +"个任务";
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
}
