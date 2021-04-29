package cn.tw.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import cn.xy.library.util.log.XLog;

public class MainActivity extends AppCompatActivity {

    private static final String TOPWAY_IP_CONFIG = "topway_ip_config"; //广播
    private static final String TOPWAY_IP_CONFIG_NETIP = "topway_ip_config_net_ip"; //net ip
    private static final String TOPWAY_IP_CONFIG_NETMASK = "topway_ip_config_net_netmask"; //net ip
    private static final String TOPWAY_IP_CONFIG_BROADCAST = "topway_ip_config_broadcast"; //broadcast ip
    private static final String TOPWAY_IP_CONFIG_RULE = "topway_ip_config_rule"; //gateway ip

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter homeFilter = new IntentFilter(TOPWAY_IP_CONFIG);
        registerReceiver(mIpconfigReceiver, homeFilter);
    }

    private BroadcastReceiver mIpconfigReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(TOPWAY_IP_CONFIG)) { //按下home键都有
                final String net_ip = intent.getStringExtra(TOPWAY_IP_CONFIG_NETIP);
                final String netmask_ip = intent.getStringExtra(TOPWAY_IP_CONFIG_NETMASK);
                final String broadcast_ip = intent.getStringExtra(TOPWAY_IP_CONFIG_BROADCAST);
                final String rule_ip = intent.getStringExtra(TOPWAY_IP_CONFIG_RULE);
                XLog.i(net_ip);
                XLog.i(netmask_ip);
                XLog.i(broadcast_ip);
                XLog.i(rule_ip);
                /*mTW.write(0x9f1a, 1, 0, "rwrite ifconfig tw_eth0 "+net_ip+" netmask "+netmask_ip+" broadcast "+broadcast_ip+" up");
                mTW.write(0x9f1a, 1, 0, "rwrite ip rule add from "+rule_ip+" lookup main pref 9000");
                mTW.write(0x9f1a, 1, 0, "rwrite ip rule add to "+rule_ip+" lookup main pref 9001");*/
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mIpconfigReceiver);
    }
}
