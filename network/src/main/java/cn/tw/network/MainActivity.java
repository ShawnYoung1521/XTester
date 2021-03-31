package cn.tw.network;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import cn.xy.library.XApp;
import cn.xy.library.util.log.XLog;
import cn.xy.library.util.network.XNetwork;
import cn.xy.library.util.toast.XToast;

public class MainActivity extends AppCompatActivity implements XNetwork.OnNetworkStatusChangedListener{
    /**
     * openWirelessSettings                    : 打开网络设置界面
     * isConnected                             : 判断网络是否连接
     * isAvailable[Async]                      : 判断网络是否可用
     * isAvailableByPing[Async]                : 用 ping 判断网络是否可用
     * isAvailableByDns[Async]                 : 用 DNS 判断网络是否可用
     * getMobileDataEnabled                    : 判断移动数据是否打开
     * isMobileData                            : 判断网络是否是移动数据d
     * is4G                                    : 判断网络是否是 4G
     * getWifiEnabled                          : 判断 wifi 是否打开
     * setWifiEnabled                          : 打开或关闭 wifi
     * isWifiConnected                         : 判断 wifi 是否连接状态
     * isWifiAvailable[Async]                  : 判断 wifi 数据是否可用
     * getNetworkOperatorName                  : 获取移动网络运营商名称
     * getNetworkType                          : 获取当前网络类型
     * getIPAddress[Async]                     : 获取 IP 地址
     * getDomainAddress[Async]                 : 获取域名 IP 地址
     * getIpAddressByWifi                      : 根据 WiFi 获取网络 IP 地址
     * getGatewayByWifi                        : 根据 WiFi 获取网关 IP 地址
     * getNetMaskByWifi                        : 根据 WiFi 获取子网掩码 IP 地址
     * getServerAddressByWifi                  : 根据 WiFi 获取服务端 IP 地址
     * registerNetworkStatusChangedListener    : 注册网络状态改变监听器
     * isRegisteredNetworkStatusChangedListener: 判断是否注册网络状态改变监听器
     * unregisterNetworkStatusChangedListener  : 注销网络状态改变监听器
     * getWifiScanResult                       : 获取 WIFI 列表
     * addOnWifiChangedConsumer                : 增加 WIFI 改变监听
     * removeOnWifiChangedConsumer             : 移除 WIFI 改变监听
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        XApp.init(getApplication());
        XNetwork.registerNetworkStatusChangedListener(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_:
                startActivity(new Intent(this,wifiListActivity.class));
                break;
            case R.id.bt_e:
                XToast.getInstance().Text(""+XNetwork.getIpAddressByWifi()).show();
                break;
            case R.id.bt_d:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        XToast.getInstance().Text("freedomer.com.cn的IP: "+XNetwork.getDomainAddress("freedomer.com.cn")).show();
                    }
                }).start();
                break;
            case R.id.bt_c:
                XToast.getInstance().Text(""+XNetwork.getIPAddress(true)).show();
                break;
            case R.id.bt_b:
                XToast.getInstance().Text(""+XNetwork.getNetworkType()).show();
                break;
            case R.id.bt_a:
                XToast.getInstance().Text(""+XNetwork.getNetworkOperatorName()).show();
                break;
            case R.id.bt_9:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        XToast.getInstance().Text(""+XNetwork.isWifiAvailable()).show();
                    }
                }).start();
                break;
            case R.id.bt_8:
                XToast.getInstance().Text(""+XNetwork.isWifiConnected()).show();
                break;
            case R.id.bt_7:
                XLog.i(!XNetwork.getWifiEnabled());
                XNetwork.setWifiEnabled(!XNetwork.getWifiEnabled());
                break;
            case R.id.bt_6:
                XToast.getInstance().Text(""+XNetwork.getWifiEnabled()).show();
                break;
            case R.id.bt_5:
                XToast.getInstance().Text(""+XNetwork.is4G()).show();
                break;
            case R.id.bt_4:
                XToast.getInstance().Text(""+XNetwork.getMobileDataEnabled()).show();
                break;
            case R.id.bt_1:
                XNetwork.openWirelessSettings();
                break;
            case R.id.bt_2:
                XToast.getInstance().Text(""+XNetwork.isConnected()).show();
                break;
            case R.id.bt_3:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        XToast.getInstance().Text(""+XNetwork.isAvailable()).show();
                    }
                }).start();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XNetwork.unregisterNetworkStatusChangedListener(this);
    }

    @Override
    public void onDisconnected() {
        XToast.getInstance().Text("Disconnected").show();
    }

    @Override
    public void onConnected(XNetwork.NetworkType networkType) {
        XToast.getInstance().Text(networkType.name()+"  Connected").show();
    }
}
