package cn.tw.network;

import android.app.Activity;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.List;

import cn.xy.library.XApp;
import cn.xy.library.util.network.XNetwork;
import cn.xy.library.util.network.XNetwork.WifiScanResults;

public class wifiListActivity extends Activity implements XApp.Consumer<WifiScanResults>{

    List<ScanResult> scanResults;ListView listView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_list);
        WifiScanResults wifiScanResults = XNetwork.getWifiScanResult();
        scanResults = wifiScanResults.getFilterResults();
        listView = findViewById(R.id.wifi_list);
        listView.setAdapter(WifiAdapter);
        XNetwork.addOnWifiChangedConsumer(this);

    }

    private BaseAdapter WifiAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return scanResults.size();
        }

        @Override
        public Object getItem(int position) {
            return scanResults.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            if(convertView == null) {
                v = newView(parent);
            } else {
                v = convertView;
            }
            bindView(v, position, parent);
            return v;
        }

        class ViewHolder {
            TextView network_name;
            TextView capabilities;
            TextView level;
            TextView access_point;
            TextView hessid;
        }

        private View newView(ViewGroup parent) {
            View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.wifi_item, parent, false);
            ViewHolder vh = new ViewHolder();
            vh.network_name = (TextView) v.findViewById(R.id.network_name);
            vh.capabilities = (TextView) v.findViewById(R.id.capabilities);
            vh.level = (TextView) v.findViewById(R.id.level);
            vh.access_point = (TextView) v.findViewById(R.id.access_point);
            vh.hessid = (TextView) v.findViewById(R.id.hessid);
            v.setTag(vh);
            return v;
        }

        private void bindView(View v, final int position, ViewGroup parent) {
            ViewHolder vh = (ViewHolder) v.getTag();
            vh.network_name.setText(scanResults.get(position).SSID);
            vh.capabilities.setText(scanResults.get(position).capabilities);
            vh.level.setText(String.valueOf(scanResults.get(position).level));
            vh.access_point.setText(scanResults.get(position).BSSID);
            WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            wm.calculateSignalLevel(5,scanResults.get(position).level);
            vh.hessid.setText(String.valueOf(wm.calculateSignalLevel(scanResults.get(position).level,5)));
        }
    };

    @Override
    public void accept(WifiScanResults wifiScanResults) {
        scanResults = wifiScanResults.getFilterResults();
        listView.setAdapter(WifiAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XNetwork.removeOnWifiChangedConsumer(this);
    }
}
